# ChopTorch

Native Kotlin/Compose torch controller with a directional double-chop recognizer. It requires a rear flash, motion sensors, and the user starting the foreground monitor from the app.

## Device notes

Camera and notification permissions are requested on launch. On Samsung devices, allow notifications and exclude ChopTorch from battery optimization for the best chance of continued screen-off monitoring. Android and OEM policy can still stop foreground services; screen-off behavior must be tested on the physical device. The service never attempts an impermissible background launch.

## Build

Open the project in Android Studio with API 36 installed, then run `assembleDebug`. Unit tests are under `app/src/test`.
