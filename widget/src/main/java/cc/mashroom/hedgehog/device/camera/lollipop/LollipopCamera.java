package cc.mashroom.hedgehog.device.camera.lollipop;

import  java.io.File;
import  java.io.IOException;
import  java.util.List;

import  com.google.common.collect.Lists;

import  android.Manifest;
import  android.app.Activity;
import  android.content.Context;
import  android.content.pm.PackageManager;
import  android.graphics.ImageFormat;
import  android.graphics.SurfaceTexture;
import  android.hardware.camera2.CameraAccessException;
import  android.hardware.camera2.CameraCaptureSession;
import  android.hardware.camera2.CameraCharacteristics;
import  android.hardware.camera2.CameraDevice;
import  android.hardware.camera2.CameraManager;
import  android.hardware.camera2.CaptureRequest;
import  android.hardware.camera2.params.StreamConfigurationMap;
import  android.media.ImageReader;
import  android.media.MediaRecorder.AudioEncoder;
import  android.media.MediaRecorder.AudioSource;
import  android.media.MediaRecorder.OutputFormat;
import  android.media.MediaRecorder.VideoEncoder;
import  android.media.MediaRecorder.VideoSource;
import  android.os.Handler;
import  android.os.HandlerThread;
import  android.util.Size;
import  android.view.Surface;
import  android.view.TextureView;

import  cc.mashroom.hedgehog.device.camera.Camera;
import  cc.mashroom.hedgehog.device.camera.CameraState;
import  cc.mashroom.hedgehog.device.camera.ErrorStateCallback;
import  cc.mashroom.hedgehog.device.camera.PhotoTakenListener;
import  cc.mashroom.util.IOUtils;
import  cc.mashroom.util.ObjectUtils;
import  cc.mashroom.util.collection.map.HashMap;
import  cc.mashroom.util.collection.map.Map;
import  cc.mashroom.hedgehog.device.MediaRecorder;
import  lombok.AccessLevel;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  LollipopCamera   implements    Camera
{
	public  static  Map<Integer,Integer>  REVERSE_ORIENTATIONS = new  HashMap<Integer,Integer>().addEntry(Surface.ROTATION_0,270).addEntry(Surface.ROTATION_90,180).addEntry(Surface.ROTATION_180,90).addEntry( Surface.ROTATION_270,0 );

	public  static  Map<Integer,Integer>  ORIENTATIONS = new  HashMap<Integer,Integer>().addEntry(Surface.ROTATION_0,90).addEntry(Surface.ROTATION_90,0).addEntry(Surface.ROTATION_180,270).addEntry( Surface.ROTATION_270,180 );

	public  LollipopCamera(       Activity context )           throws  CameraAccessException
	{
		this.setContext(context).setCameraManager(ObjectUtils.cast(context.getSystemService(Context.CAMERA_SERVICE))).setDeviceStateCallback(new  DeviceCallback(this)).setCaptureSessionStateCallback(new  CaptureSessionStateCallback(this)).setCameraIds( Lists.newArrayList(cameraManager.getCameraIdList()) );
	}

	@Setter( value=AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
	private  Handler  loopHandler;
    @Setter( value=AccessLevel.PROTECTED )
    @Getter
    @Accessors( chain=true )
    private  Activity  context;
	@Setter( value=AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
    private  TextureView  textureView;
	@Setter( value=AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
	private  CameraState  state  = CameraState.NONE;
    @Setter( value=AccessLevel.PROTECTED )
    @Accessors( chain=true )
    private  CameraManager  cameraManager;
    @Setter( value=AccessLevel.PROTECTED )
    @Getter
    @Accessors( chain=true )
    private  CameraDevice  currentCameraDevice;
    @Setter( value=AccessLevel.PROTECTED )
    @Accessors( chain=true )
    private  String   currentCameraId;
    @Setter( value=AccessLevel.PROTECTED )
    @Accessors( chain=true )
    private  List<String>   cameraIds;
	@Setter( value=AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
    private  PhotoTakenListener  photoTakenListener;
    @Setter( value=AccessLevel.PROTECTED )
    @Getter
    @Accessors( chain=true )
    private  ErrorStateCallback  errorStateCallback;
	@Setter( value=AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
    private  ImageReader  imageReader;
	@Setter( value=AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
    private  CameraCaptureSession.StateCallback  captureSessionStateCallback;
	@Setter( value=AccessLevel.PROTECTED )
	@Accessors( chain=true )
    private  int    sensorOrientation;
	@Setter( value=AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
    private  CaptureRequest.Builder  previewBuilder;
	@Setter( value=AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
    private  MediaRecorder  videoRecorder;
	@Setter( value=AccessLevel.PROTECTED )
	@Accessors( chain=true )
    private  File     videoOutputFile;
    @Setter( value=AccessLevel.PROTECTED )
    @Getter
    @Accessors( chain=true )
    private  CameraCaptureSession    captureSession;
    @Setter( value=AccessLevel.PROTECTED )
    @Accessors( chain=true )
    private  Map<Integer,Size>  optimalSizes  = new  HashMap<Integer,Size>();
    @Setter( value=AccessLevel.PROTECTED )
    @Getter
    @Accessors( chain=true )
    private  CameraDevice.StateCallback  deviceStateCallback;
	@Setter( value=AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
    private  CameraCaptureSession.CaptureCallback  captureCallback = new  CameraCaptureSession.CaptureCallback(){};

	public  Size  getPreviewSize()
	{
		return  optimalSizes.get( 0 );
	}

	private  LollipopCamera   setHandler()
    {
        HandlerThread  handlerThread= new  HandlerThread( "CAMERA-HANLDER" );

        handlerThread.start();

        return  setLoopHandler(  new  Handler( handlerThread.getLooper() ) );
    }

	public  LollipopCamera  preview( String  cameraId,TextureView  textureView,ErrorStateCallback  errorStateCallback )  throws  CameraAccessException
	{
		if( context.checkCallingOrSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || context.checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED )
		{
			throw  new  IllegalStateException( "MASHROOM-PARENT:  ** LOLLIPOP  CAMERA **  camera  and  recoding  audio  permissions  are  needed" );
		}

		setHandler().setErrorStateCallback(errorStateCallback).setTextureView(textureView).setState(CameraState.PREVIEW).setCurrentCameraId(cameraId).setSensorOrientation( cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.SENSOR_ORIENTATION) );

        StreamConfigurationMap  configuation = cameraManager.getCameraCharacteristics(cameraId).get( CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP );

        optimalSizes.addEntry(0, CameraOptionalUtils.getOptimalSize(new  Size(textureView.getWidth(),textureView.getHeight()),configuation.getOutputSizes(SurfaceTexture.class))).addEntry(1, CameraOptionalUtils.getOptimalSize(new  Size(textureView.getWidth(),textureView.getHeight()),configuation.getOutputSizes(ImageFormat.JPEG))).addEntry( 2, CameraOptionalUtils.getOptimalSize(new  Size(textureView.getWidth(),textureView.getHeight()),configuation.getOutputSizes(android.media.MediaRecorder.class)) );

        textureView.getSurfaceTexture().setDefaultBufferSize( optimalSizes.get(0).getWidth(),optimalSizes.get(0).getHeight() );

        setImageReader(ImageReader.newInstance(optimalSizes.get(1).getWidth(),optimalSizes.get(1).getHeight(),ImageFormat.JPEG,2)).getImageReader().setOnImageAvailableListener( (reader) -> photoTakenListener.onPhotoTaken(CameraOptionalUtils.getImageReaderBytes(reader)),loopHandler );

		cameraManager.openCamera( currentCameraId,deviceStateCallback,null );
		
		return  this;
	}

	public  void  recordVideo( File  outputFile )  throws  IOException,CameraAccessException
	{
		captureSession.close();

        StreamConfigurationMap  configuation = cameraManager.getCameraCharacteristics(currentCameraId).get( CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP );

        Surface  surface   = new  Surface( textureView.getSurfaceTexture() );

        setState(CameraState.RECORD_VIDEO).setVideoOutputFile(outputFile).setVideoRecorder(new  MediaRecorder().prepare(AudioSource.MIC,AudioEncoder.AAC,VideoSource.SURFACE,VideoEncoder.H264,OutputFormat.MPEG_4,outputFile, CameraOptionalUtils.getOrientationHint(sensorOrientation,context.getWindowManager().getDefaultDisplay().getRotation()),optimalSizes.get(2))).setPreviewBuilder( CameraOptionalUtils.configCaptureRequestBuilder(currentCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW),Lists.newArrayList(surface,videoRecorder.unwrap().getSurface()),new  HashMap<CaptureRequest.Key<Integer>,Integer>().addEntry(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE).addEntry(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)) );

        currentCameraDevice.createCaptureSession( Lists.newArrayList(surface,videoRecorder.unwrap().getSurface()),captureSessionStateCallback,loopHandler );
	}

    public  void  continuePreview()  throws  CameraAccessException
    {
        release();

        setHandler().preview(currentCameraId,textureView,errorStateCallback);
    }

    public  File  stopRecordVideo()  throws  CameraAccessException
    {
        captureSession.stopRepeating();

        captureSession.abortCaptures();
		//  do  not  work  on  xiaomi  4a  for  runtime  exception  ( stop  failed:  -1007 )
	    videoRecorder.unwrap().stop( );

        return  videoOutputFile;
    }

    public  void  release()
    {
	    IOUtils.closeQuietly(captureSession,currentCameraDevice,imageReader,videoRecorder );

	    loopHandler.getLooper().quit();
    }

    public  LollipopCamera  switchOrientation() throws  CameraAccessException
    {
        release();

        setHandler().preview( cameraIds.get(cameraIds.indexOf(currentCameraId)+1 >= cameraIds.size() ? 0 : cameraIds.indexOf(currentCameraId)+1),textureView,errorStateCallback );

        return  this;
    }

    public  void  takePicture( PhotoTakenListener  listener )  throws  CameraAccessException
    {
        if( this.state == CameraState.TAKE_PICTURE )
        {
            return;
        }

        setState(CameraState.TAKE_PICTURE).setPhotoTakenListener( listener );

        captureSession.stopRepeating();

        captureSession.abortCaptures();

        captureSession.capture( CameraOptionalUtils.configCaptureRequestBuilder(currentCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE),Lists.newArrayList(imageReader.getSurface()),new  HashMap<CaptureRequest.Key<Integer>,Integer>().addEntry(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE).addEntry(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH).addEntry(CaptureRequest.JPEG_ORIENTATION,(ORIENTATIONS.get(context.getWindowManager().getDefaultDisplay().getRotation())+sensorOrientation+270)%360)).build(),captureCallback,loopHandler );
    }
}
