# WebRTC Vendor Notes

## Current reality
Android WebRTC dependency selection is not as straightforward as older guides suggest.

Historically many examples used:
- `org.webrtc:google-webrtc`

But current ecosystem signals show that this may be deprecated, stale, or inconsistently resolvable depending on repository state.

## Project stance
Before locking implementation, verify on the x86_64 build machine which of these is practically resolvable and maintained:

1. `org.webrtc:google-webrtc`
2. a maintained fork/package such as a modern Maven Central distribution
3. a vendored prebuilts strategy if necessary

## Engineering rule
Keep Tesla Mirror source structured so that the concrete WebRTC package can be swapped with minimal changes.

## Immediate implication
- keep signaling protocol stable
- keep session coordinator stable
- keep sender abstraction stable
- do not hardwire vendor-specific classes into app-wide code too early
