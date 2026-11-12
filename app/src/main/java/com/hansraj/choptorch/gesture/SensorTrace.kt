package com.hansraj.choptorch.gesture

import android.util.Log
/** Debug-only bounded sensor trace; logging is opt-in at the call site and never persisted. */
class SensorTrace(private val enabled:Boolean) {
 private val entries=ArrayDeque<String>()
 fun add(x:Float,y:Float,z:Float,now:Long){if(!enabled)return;entries.addLast("$now,$x,$y,$z");if(entries.size>200)entries.removeFirst()}
 fun dump():List<String>{val copy=entries.toList();if(enabled)Log.d("ChopTorchTrace",copy.joinToString("\\n"));return copy}
}
