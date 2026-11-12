package com.hansraj.choptorch.compat

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.PowerManager
import android.util.Log

/** Runtime facts only: no behavior is inferred from a manufacturer name. */
data class DeviceCapabilities(
    val manufacturer: String, val model: String, val api: Int,
    val accelerometer: Boolean, val gyroscope: Boolean, val proximity: Boolean,
    val ambientLight: Boolean, val rearFlash: Boolean, val ignoringBatteryOptimizations: Boolean
) {
    fun report() = listOf(
        "Device: $manufacturer $model (API $api)",
        "Accelerometer: $accelerometer; Gyroscope: $gyroscope",
        "Proximity: $proximity; Ambient light: $ambientLight",
        "Rear flash: $rearFlash; battery unrestricted: $ignoringBatteryOptimizations"
    )
}
object DeviceDiagnostics {
 fun inspect(context: Context): DeviceCapabilities {
  val sensors=context.getSystemService(SensorManager::class.java)
  val camera=context.getSystemService(CameraManager::class.java)
  val flash=runCatching { camera.cameraIdList.any { id ->
   val c=camera.getCameraCharacteristics(id)
   c.get(CameraCharacteristics.LENS_FACING)==CameraCharacteristics.LENS_FACING_BACK && c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)==true
  }}.getOrDefault(false)
  val pm=context.getSystemService(PowerManager::class.java)
  return DeviceCapabilities(Build.MANUFACTURER,Build.MODEL,Build.VERSION.SDK_INT,
   sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null,sensors.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!=null,
   sensors.getDefaultSensor(Sensor.TYPE_PROXIMITY)!=null,sensors.getDefaultSensor(Sensor.TYPE_LIGHT)!=null,flash,
   pm.isIgnoringBatteryOptimizations(context.packageName)).also { Log.i("ChopTorchDiagnostics",it.report().joinToString(" | ")) }
 }
}
