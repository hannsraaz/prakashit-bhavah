package com.hansraj.choptorch.gesture

import kotlin.math.sqrt

/** Collects deliberate sample magnitudes and proposes a threshold without retaining user data. */
class GestureCalibration {
 private val samples=mutableListOf<Float>()
 fun record(x:Float,y:Float){if(samples.size<80)samples+=sqrt(x*x+y*y)}
 fun suggestedSensitivity():Float?=samples.takeIf{it.size>=8}?.let { values ->
  val median=values.sorted()[values.size/2]
  ((median-8f)/11f).coerceIn(.2f,1f)
 }
 fun clear(){samples.clear()}
}
