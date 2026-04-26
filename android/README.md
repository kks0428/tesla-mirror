# Android build notes

## Missing from workspace right now
- Gradle wrapper
- local.properties with Android SDK path
- Android SDK / build-tools on the machine that builds the app

## Expected next step
From a machine with Android SDK and Gradle or Android Studio:

1. Open `tesla-mirror/android`
2. Generate Gradle wrapper if needed
3. Sync project
4. Build `:app:assembleDebug`

## Current scope
This project currently includes:
- Compose app shell
- foreground service shell
- local HTTP/WebSocket server skeleton
- Tesla web client assets bundled in app assets

It does not yet include:
- MediaProjection pipeline
- real WebRTC sender integration
- production signaling state machine
