package com.hansraj.choptorch.core
import android.content.Context
import android.hardware.camera2.*
import kotlinx.coroutines.flow.MutableStateFlow
class TorchController(context:Context) {
 private val manager=context.getSystemService(CameraManager::class.java); private var id:String?=null
 val enabled=MutableStateFlow(false); val available=MutableStateFlow(false)
 private val callback=object:CameraManager.TorchCallback(){override fun onTorchModeChanged(cameraId:String,on:Boolean){if(cameraId==id) enabled.value=on};override fun onTorchModeUnavailable(cameraId:String){if(cameraId==id) available.value=false}}
 init { id=manager.cameraIdList.firstOrNull { manager.getCameraCharacteristics(it).get(CameraCharacteristics.LENS_FACING)==CameraCharacteristics.LENS_FACING_BACK && manager.getCameraCharacteristics(it).get(CameraCharacteristics.FLASH_INFO_AVAILABLE)==true }; available.value=id!=null; manager.registerTorchCallback(callback,null) }
 fun toggle():Result<Boolean> = runCatching { val next=!enabled.value; manager.setTorchMode(id?:error("No rear flash"),next); next }
 fun close(){runCatching{manager.unregisterTorchCallback(callback)}}
}
