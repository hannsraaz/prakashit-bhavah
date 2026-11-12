# Hardware compatibility test matrix

Status means tested on physical hardware, not an inferred manufacturer capability. The shipped build has only compile/unit-test validation; all device rows below are intentionally unverified.

| Device family / OS | Foreground torch | Locked screen | Removed from recents | Process killed | Status |
|---|---|---|---|---|---|
| Samsung / One UI | Pending | Pending | Pending | Pending | Not tested |
| Xiaomi / MIUI or HyperOS | Pending | Pending | Pending | Pending | Not tested |
| Vivo / Funtouch OS | Pending | Pending | Pending | Pending | Not tested |
| Motorola / Hello UI | Pending | Pending | Pending | Pending | Not tested |
| Pixel / stock Android | Pending | Pending | Pending | Pending | Not tested |

## Test procedure

1. In Diagnostics, confirm accelerometer and rear-flash availability; record optional sensor availability.
2. Start monitoring visibly, perform a calibrated double-chop, and verify torch state and haptic feedback.
3. Repeat with the screen locked; record whether the service notification and torch remain responsive.
4. Swipe the app from recents, then repeat. Force-stop or kill the process separately; Android intentionally does not guarantee recovery after force-stop.
5. Capture logcat using tag `ChopTorchDiagnostics` for sensor, service and hardware evidence.

Manufacturer-specific battery-management instructions may be offered as troubleshooting only. They must never enable a manufacturer-specific detection path.
