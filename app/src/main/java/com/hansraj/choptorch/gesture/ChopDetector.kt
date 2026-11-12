package com.hansraj.choptorch.gesture

import kotlin.math.abs
import kotlin.math.sqrt

/** Detects two directional, wrist-like pulses; deliberately unlike a generic shake detector. */
class ChopDetector(private var sensitivity: Float=.65f, private var cooldownMs:Long=850) {
 private var firstAt=0L; private var firstSign=0; private var lastTrigger=0L
 fun configure(s:Float, cooldown:Long) { sensitivity=s; cooldownMs=cooldown }
 fun onSample(x:Float,y:Float,z:Float, gyro:Float?=null, timeMs:Long):Boolean {
  val horizontal=sqrt(x*x+y*y); val threshold=8.5f + (1f-sensitivity)*11f
  // Gyroscope is an enhancement only. Accelerometer-only devices remain supported.
  if (horizontal<threshold || abs(z)>15f || (gyro != null && gyro < .15f)) return false
  val sign=if (y+x*.20f >=0) 1 else -1
  if(firstAt==0L || timeMs-firstAt>700 || sign==firstSign){ firstAt=timeMs; firstSign=sign; return false }
  val hit=lastTrigger==0L || timeMs-lastTrigger>cooldownMs
  firstAt=0; firstSign=0; if(hit) lastTrigger=timeMs
  return hit
 }
 fun reset(){firstAt=0;firstSign=0}
}
