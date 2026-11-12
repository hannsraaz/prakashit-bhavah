package com.hansraj.choptorch

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hansraj.choptorch.compat.DeviceDiagnostics
import com.hansraj.choptorch.core.TorchController
import com.hansraj.choptorch.data.AppSettings
import com.hansraj.choptorch.data.SettingsRepository
import com.hansraj.choptorch.service.ChopTorchService
import kotlinx.coroutines.launch

private val Ink=Color(0xFF06070A); private val Glass=Color.White.copy(alpha=.075f); private val GlassLine=Color.White.copy(alpha=.16f); private val Glow=Color(0xFFFFD35A)
class MainActivity:ComponentActivity(){
 private lateinit var torch:TorchController;private lateinit var repo:SettingsRepository
 private val permission=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){}
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);torch=TorchController(this);repo=SettingsRepository(this);permission.launch(arrayOf(Manifest.permission.CAMERA,Manifest.permission.POST_NOTIFICATIONS));setContent{ChopApp(torch,repo)}}
 override fun onDestroy(){torch.close();super.onDestroy()}
}
@Composable fun ChopApp(torch:TorchController,repo:SettingsRepository){val context=androidx.compose.ui.platform.LocalContext.current;val caps=remember{DeviceDiagnostics.inspect(context)};var settingsPage by remember{mutableStateOf(false)};val torchOn by torch.enabled.collectAsState();val settings by repo.settings.collectAsState(AppSettings());MaterialTheme(colorScheme=darkColorScheme(primary=Glow,background=Ink,surface=Color.Transparent)){Surface(color=Ink){Box(Modifier.fillMaxSize().background(Brush.radialGradient(listOf(Color(0xFF3A2C0C),Ink),radius=1100f))){AnimatedContent(settingsPage){if(it) Settings(settings,repo,{settingsPage=false}) else Home(torchOn,torch.available.value,ChopTorchService.running,caps.accelerometer,caps.gyroscope,{torch.toggle()},{ChopTorchService.start(context)},{settingsPage=true})}}}}}
@Composable private fun GlassCard(modifier:Modifier=Modifier,content:@Composable ColumnScope.()->Unit){Column(modifier.clip(RoundedCornerShape(28.dp)).background(Glass).border(1.dp,GlassLine,RoundedCornerShape(28.dp)).padding(18.dp),content=content)}
@Composable fun Home(on:Boolean,available:Boolean,running:Boolean,accelerometer:Boolean,gyro:Boolean,toggle:()->Unit,start:()->Unit,settings:()->Unit){Column(Modifier.fillMaxSize().padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally){Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Prakashit Bhavah",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold);Text("LIQUID LIGHT",color=Glow,style=MaterialTheme.typography.labelMedium)};IconButton(settings,Modifier.clip(CircleShape).background(Glass).border(1.dp,GlassLine,CircleShape)){Icon(Icons.Default.Settings,"Settings")}};Spacer(Modifier.height(42.dp));Box(Modifier.size(218.dp).shadow(if(on)28.dp else 12.dp,CircleShape,ambientColor=Glow,spotColor=Glow).clip(CircleShape).background(if(on)Brush.radialGradient(listOf(Color.White,Glow,Color(0xFFB4770C))) else Brush.linearGradient(listOf(Color.White.copy(.16f),Color.White.copy(.04f)))).border(1.dp,Color.White.copy(.45f),CircleShape).clickable(enabled=available){toggle()},contentAlignment=Alignment.Center){Icon(if(on)Icons.Default.FlashlightOn else Icons.Default.FlashlightOff,"Toggle flashlight",Modifier.size(84.dp),tint=if(on)Ink else Glow)};Spacer(Modifier.height(19.dp));Text(if(on)"Light is on" else "Light is off",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.SemiBold);Text(if(available)"Tap the glass to toggle" else "Rear torch unavailable",color=Color.White.copy(.57f));Spacer(Modifier.height(28.dp));GlassCard(Modifier.fillMaxWidth()){Status("Gesture monitor",if(running)"Listening" else "Paused",running);HorizontalDivider(color=GlassLine);Status("Motion engine",if(accelerometer)if(gyro)"Motion + gyro" else "Motion only" else "Unavailable",accelerometer);HorizontalDivider(color=GlassLine);Status("Pocket protection","Sensor-aware",true)};Spacer(Modifier.weight(1f));Button(onClick=start,enabled=!running&&accelerometer,shape=RoundedCornerShape(22.dp),colors=ButtonDefaults.buttonColors(containerColor=Glow,contentColor=Ink),modifier=Modifier.fillMaxWidth().height(58.dp)){Icon(Icons.Default.Sensors,"",Modifier.padding(end=8.dp));Text(if(running)"MONITORING ACTIVE" else "START MONITORING",fontWeight=FontWeight.Bold)};Text("Android and device policy can limit locked-screen monitoring.",Modifier.padding(top=13.dp),style=MaterialTheme.typography.labelSmall,textAlign=TextAlign.Center,color=Color.White.copy(.48f))}}
@Composable fun Status(label:String,value:String,ok:Boolean){Row(Modifier.fillMaxWidth().padding(vertical=10.dp),verticalAlignment=Alignment.CenterVertically){Icon(if(ok)Icons.Default.CheckCircle else Icons.Default.Cancel,"",tint=if(ok)Glow else Color.White.copy(.38f),modifier=Modifier.size(19.dp));Text(label,Modifier.padding(start=10.dp).weight(1f));Text(value,color=Color.White.copy(.62f),style=MaterialTheme.typography.labelMedium)}}
@Composable fun Settings(s:AppSettings,repo:SettingsRepository,back:()->Unit){val k=repo.keys();val scope=rememberCoroutineScope();Column(Modifier.fillMaxSize().padding(20.dp)){Row(verticalAlignment=Alignment.CenterVertically){IconButton(back,Modifier.clip(CircleShape).background(Glass)){Icon(Icons.Default.ArrowBack,"Back")};Text("Settings",Modifier.padding(start=8.dp),style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)};Spacer(Modifier.height(25.dp));GlassCard(Modifier.fillMaxWidth()){SettingSwitch("Gesture control","Listen for double-chop",s.gestures){v->scope.launch{repo.update{this[k.gestures]=v}}};SettingSwitch("Haptic feedback","Tactile confirmation",s.haptics){v->scope.launch{repo.update{this[k.haptics]=v}}};SettingSwitch("Pocket protection","Blocks likely pocket activation",s.pocket){v->scope.launch{repo.update{this[k.pocket]=v}}}};Spacer(Modifier.height(15.dp));GlassCard(Modifier.fillMaxWidth()){Text("Gesture feel",style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.SemiBold);Text("Sensitivity",Modifier.padding(top=12.dp),color=Color.White.copy(.7f));Slider(s.sensitivity,onValueChange={v->scope.launch{repo.update{this[k.sensitivity]=v}}},valueRange=.2f..1f);Text("Cooldown · ${s.cooldown} ms",color=Color.White.copy(.7f));Slider(s.cooldown.toFloat(),onValueChange={v->scope.launch{repo.update{this[k.cooldown]=v.toLong()}}},valueRange=500f..2000f)};Spacer(Modifier.height(18.dp));Text("Battery guidance",style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.SemiBold);Text("Allow notifications and remove battery restrictions if your device offers it. Firmware may still limit long-running or locked-screen service use.",color=Color.White.copy(.56f),modifier=Modifier.padding(top=7.dp))}}
@Composable fun SettingSwitch(title:String,sub:String,checked:Boolean,change:(Boolean)->Unit){Row(Modifier.fillMaxWidth().padding(vertical=10.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(title,fontWeight=FontWeight.Medium);Text(sub,style=MaterialTheme.typography.bodySmall,color=Color.White.copy(.54f))};Switch(checked,change,colors=SwitchDefaults.colors(checkedThumbColor=Ink,checkedTrackColor=Glow))}}
