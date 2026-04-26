const statusEl = document.getElementById('status');
const videoEl = document.getElementById('remoteVideo');
const latencyEl = document.getElementById('latency');
const fpsEl = document.getElementById('fps');
const bitrateEl = document.getElementById('bitrate');

statusEl.textContent = 'waiting for local signaling';

const sessionId = new URLSearchParams(location.search).get('sessionId') || 'local';
const token = new URLSearchParams(location.search).get('token') || '';
const wsProtocol = location.protocol === 'https:' ? 'wss:' : 'ws:';
const ws = new WebSocket(`${wsProtocol}//${location.host}/signal`);

let pc;
let dc;

function setStatus(text) {
  statusEl.textContent = text;
}

function send(message) {
  ws.send(JSON.stringify(message));
}

function createPeer() {
  pc = new RTCPeerConnection();

  pc.ontrack = (event) => {
    videoEl.srcObject = event.streams[0];
  };

  pc.ondatachannel = (event) => {
    dc = event.channel;
    dc.onmessage = (e) => {
      try {
        const msg = JSON.parse(e.data);
        if (msg.type === 'stats.latency') latencyEl.textContent = `RTT: ${msg.rttMs}ms`;
        if (msg.type === 'stats.fps') fpsEl.textContent = `FPS: ${msg.value}`;
        if (msg.type === 'stats.bitrate') bitrateEl.textContent = `Bitrate: ${msg.kbps}kbps`;
      } catch {}
    };
  };

  pc.onicecandidate = (event) => {
    if (event.candidate) {
      send({ type: 'session.ice-candidate', sessionId, candidate: event.candidate });
    }
  };
}

ws.onopen = () => {
  setStatus('signaling connected');
  createPeer();
  send({ type: 'session.join', sessionId, token });
};

ws.onmessage = async (event) => {
  const msg = JSON.parse(event.data);

  if (msg.type === 'session.offer') {
    await pc.setRemoteDescription({ type: 'offer', sdp: msg.sdp });
    const answer = await pc.createAnswer();
    await pc.setLocalDescription(answer);
    send({ type: 'session.answer', sessionId, sdp: answer.sdp });
    setStatus('webrtc negotiating');
  }

  if (msg.type === 'session.ice-candidate' && msg.candidate) {
    await pc.addIceCandidate(msg.candidate);
  }

  if (msg.type === 'session.state') {
    setStatus(msg.value);
  }

  if (msg.type === 'error') {
    setStatus(`error: ${msg.code}`);
  }
};

videoEl.addEventListener('click', (event) => {
  if (!dc || dc.readyState !== 'open') return;
  const rect = videoEl.getBoundingClientRect();
  const x = (event.clientX - rect.left) / rect.width;
  const y = (event.clientY - rect.top) / rect.height;
  dc.send(JSON.stringify({ type: 'input.tap', x, y }));
});
