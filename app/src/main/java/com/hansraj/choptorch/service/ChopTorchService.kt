package com.hansraj.choptorch.service
import android.app.Service
import android.content.Intent
import android.hardware.*
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.hansraj.choptorch.core.*
import com.hansraj.choptorch.gesture.ChopDetector
import com.hansraj.choptorch.safety.PocketDetector
import com.hansraj.choptorch.data.SettingsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class ChopTorchService:Service(),SensorEventListener {
 companion object { const val ACTION_START="com.hansraj.choptorch.START"; const val ACTION_STOP="com.hansraj.choptorch.STOP"; var running=false; fun start(c:android.content.Context){ContextCompat.startForegroundService(c,Intent(c,ChopTorchService::class.java).setAction(ACTION_START))} }
 private lateinit var sensors:SensorManager;private lateinit var torch:TorchController;private lateinit var haptics:HapticController;private lateinit var pocket:PocketDetector;private val detector=ChopDetector();private var gyro=0f;private var gyroPresent=false;private val scope=CoroutineScope(SupervisorJob()+Dispatchers.Main.immediate);private var gestures=true;private var haptic=true;private var protectPocket=true
 override fun onStartCommand(intent:Intent?,flags:Int,startId:Int):Int {if(intent?.action==ACTION_STOP){stopSelf();return START_NOT_STICKY};NotificationHelper.create(this);startForeground(NotificationHelper.ID,NotificationHelper.notification(this));if(!running){running=true;sensors=getSystemService(SensorManager::class.java);torch=TorchController(this);haptics=HapticController(this);pocket=PocketDetector(this);scope.launch{SettingsRepository(this@ChopTorchService).settings.collectLatest{gestures=it.gestures;haptic=it.haptics;protectPocket=it.pocket;detector.configure(it.sensitivity,it.cooldown)}};sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let{sensors.registerListener(this,it,SensorManager.SENSOR_DELAY_GAME)};sensors.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let{gyroPresent=true;sensors.registerListener(this,it,SensorManager.SENSOR_DELAY_GAME)}};return START_NOT_STICKY}
 override fun onSensorChanged(e:SensorEvent){if(e.sensor.type==Sensor.TYPE_GYROSCOPE)gyro=kotlin.math.abs(e.values[0])+kotlin.math.abs(e.values[1]);if(e.sensor.type==Sensor.TYPE_ACCELEROMETER&&gestures&&detector.onSample(e.values[0],e.values[1],e.values[2],if(gyroPresent) gyro else null,System.currentTimeMillis())&&(!protectPocket||!pocket.isLikelyPocket()))torch.toggle().onSuccess{if(haptic)haptics.pulse(it)}}
 override fun onAccuracyChanged(s:Sensor?,a:Int){};override fun onDestroy(){scope.cancel();if(running){sensors.unregisterListener(this);pocket.close();torch.close()};running=false;super.onDestroy()};override fun onBind(i:Intent?):IBinder?=null
}
