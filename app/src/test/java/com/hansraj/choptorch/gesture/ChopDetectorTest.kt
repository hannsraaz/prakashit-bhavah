package com.hansraj.choptorch.gesture
import org.junit.Assert.*
import org.junit.Test
class ChopDetectorTest { @Test fun `opposite deliberate chops trigger once`(){val d=ChopDetector(.9f,850);assertFalse(d.onSample(1f,13f,9f,1f,100));assertTrue(d.onSample(1f,-13f,9f,1f,350));assertFalse(d.onSample(1f,13f,9f,1f,420));assertFalse(d.onSample(1f,-13f,9f,1f,500))};@Test fun `accelerometer only works without gyro`(){val d=ChopDetector(.9f);assertFalse(d.onSample(1f,13f,9f,null,100));assertTrue(d.onSample(1f,-13f,9f,null,350))};@Test fun `ordinary movement ignored`(){val d=ChopDetector();assertFalse(d.onSample(1f,3f,9f,1f,10));assertFalse(d.onSample(1f,-3f,9f,1f,100))} }
