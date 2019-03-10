package cc.mashroom.hedgehog.device.camera;

import  android.view.TextureView;

import  java.io.File;

public  interface  Camera
{
	public  void  release();

	public  Camera  preview(String cameraId, TextureView textureView, ErrorStateCallback errorStateCallback)  throws  Exception;

	public  void  recordVideo(File outputFile)  throws  Exception;

    public  void  continuePreview()  throws  Exception;

	public  Camera  switchOrientation()  throws  Exception;

    public  File  stopRecordVideo()  throws  Exception;

    public  void  takePicture(PhotoTakenListener listener)  throws  Exception;
}
