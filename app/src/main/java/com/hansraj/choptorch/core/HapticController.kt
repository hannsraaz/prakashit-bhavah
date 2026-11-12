package com.hansraj.choptorch.core
import android.content.Context
import android.os.*
class HapticController(context:Context){private val vibrator=if(Build.VERSION.SDK_INT>=31) context.getSystemService(VibratorManager::class.java).defaultVibrator else @Suppress("DEPRECATION") context.getSystemService(Vibrator::class.java); fun pulse(on:Boolean){if(vibrator.hasVibrator()) vibrator.vibrate(VibrationEffect.createOneShot(if(on) 45 else 25,VibrationEffect.DEFAULT_AMPLITUDE))}}
