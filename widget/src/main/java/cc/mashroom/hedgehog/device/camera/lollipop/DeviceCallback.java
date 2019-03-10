package cc.mashroom.hedgehog.device.camera.lollipop;

import  android.hardware.camera2.CameraAccessException;
import  android.hardware.camera2.CameraDevice;
import  android.hardware.camera2.CaptureRequest;
import  android.view.Surface;

import  com.google.common.collect.Lists;

import  cc.mashroom.util.collection.map.HashMap;
import  lombok.AllArgsConstructor;

@AllArgsConstructor

public  class  DeviceCallback  extends  CameraDevice.StateCallback
{
    protected    LollipopCamera  context;

    public  void  onDisconnected( CameraDevice  device )
    {
        device.close();
    }

    public  void  onOpened( CameraDevice  device )
    {
        try
        {
            Surface  surface = new  Surface( context.getTextureView().getSurfaceTexture() );

            context.setPreviewBuilder( CameraCaptureUtils.configCaptureRequestBuilder(context.setCurrentCameraDevice(device).getCurrentCameraDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW),Lists.newArrayList(surface),new  HashMap<CaptureRequest.Key<Integer>,Integer>().addEntry(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE).addEntry(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)) );

            device.createCaptureSession( Lists.newArrayList(surface,context.getImageReader().getSurface()),context.getCaptureSessionStateCallback(),context.getLoopHandler() );
        }
        catch( CameraAccessException  e )
        {
            context.getErrorStateCallback().onError( device,0,e );
        }
    }

    public  void  onError( CameraDevice  cameraDevice,int  error )
    {
        context.getErrorStateCallback().onError( cameraDevice,error,null );
    }
}
