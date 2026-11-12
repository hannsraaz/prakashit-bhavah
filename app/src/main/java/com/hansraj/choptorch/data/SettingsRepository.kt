package com.hansraj.choptorch.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.settingsStore by preferencesDataStore("choptorch_settings")
data class AppSettings(val gestures:Boolean=true,val haptics:Boolean=true,val pocket:Boolean=true,val sensitivity:Float=0.65f,val cooldown:Long=850)
class SettingsRepository(private val context: Context) {
 private val gestures=booleanPreferencesKey("gestures"); private val haptics=booleanPreferencesKey("haptics"); private val pocket=booleanPreferencesKey("pocket"); private val sensitivity=floatPreferencesKey("sensitivity"); private val cooldown=longPreferencesKey("cooldown")
 val settings=context.settingsStore.data.map { AppSettings(it[gestures]?:true,it[haptics]?:true,it[pocket]?:true,it[sensitivity]?:.65f,it[cooldown]?:850) }
 suspend fun update(block: MutablePreferences.()->Unit) { context.settingsStore.edit(block) }
 fun keys()=Keys(gestures,haptics,pocket,sensitivity,cooldown)
 data class Keys(val gestures:Preferences.Key<Boolean>,val haptics:Preferences.Key<Boolean>,val pocket:Preferences.Key<Boolean>,val sensitivity:Preferences.Key<Float>,val cooldown:Preferences.Key<Long>)
}
