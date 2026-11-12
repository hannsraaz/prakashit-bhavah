package com.hansraj.choptorch.gesture

/** Deterministic replay for recorded CSV trace lines: timestamp,x,y,z[,gyro]. */
object SensorTraceReplay {
 fun replay(lines: List<String>, detector: ChopDetector): Int = lines.count { line ->
  val v=line.split(',').mapNotNull(String::toFloatOrNull)
  if(v.size < 4) false else detector.onSample(v[1],v[2],v[3],v.getOrNull(4),v[0].toLong())
 }
}
