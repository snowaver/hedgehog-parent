package cc.mashroom.hedgehog.device.camera;

import  android.hardware.camera2.CameraDevice;

public  interface  ErrorStateCallback
{
	public  void  onError(CameraDevice device, int errorCode, Throwable e);
}
