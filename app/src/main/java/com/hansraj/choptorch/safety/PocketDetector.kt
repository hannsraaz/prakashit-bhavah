package com.hansraj.choptorch.safety
import android.content.Context
import android.hardware.*
class PocketDetector(context:Context):SensorEventListener {private val sm=context.getSystemService(SensorManager::class.java);private var near=false;private var dark=false; val supported:Boolean
 init {val p=sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);val l=sm.getDefaultSensor(Sensor.TYPE_LIGHT);supported=p!=null||l!=null;p?.let{sm.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL)};l?.let{sm.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL)}}
 fun isLikelyPocket()=near && dark; override fun onSensorChanged(e:SensorEvent){if(e.sensor.type==Sensor.TYPE_PROXIMITY)near=e.values[0]<e.sensor.maximumRange; if(e.sensor.type==Sensor.TYPE_LIGHT)dark=e.values[0]<8};override fun onAccuracyChanged(s:Sensor?,a:Int){};fun close(){sm.unregisterListener(this)} }
