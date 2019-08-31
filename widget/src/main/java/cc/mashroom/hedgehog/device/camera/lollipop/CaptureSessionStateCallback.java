package cc.mashroom.hedgehog.device.camera.lollipop;

import  android.hardware.camera2.CameraAccessException;
import  android.hardware.camera2.CameraCaptureSession;

import  cc.mashroom.hedgehog.device.camera.CameraState;
import  lombok.AllArgsConstructor;

@AllArgsConstructor

public  class  CaptureSessionStateCallback  extends  CameraCaptureSession.StateCallback
{
    protected    LollipopCamera  context;

    public  void  onConfigureFailed(  CameraCaptureSession  session )
    {
        context.getErrorStateCallback().onError(  session == null ? null : session.getDevice(),0,null );
    }

    public  void  onConfigured(       CameraCaptureSession  session )
    {
        try
        {
            this.context.setCaptureSession(session).getCaptureSession().setRepeatingRequest( context.getPreviewBuilder().build(),null,context.getLoopHandler() );

            if( this.context.getState() == CameraState.RECORD_VIDEO )
            {
                this.context.getVideoRecorder().unwrap().start();
            }
        }
        catch( CameraAccessException  e )
        {
            context.getErrorStateCallback().onError( session == null ? null : session.getDevice(),0,e );
        }
    }
}
