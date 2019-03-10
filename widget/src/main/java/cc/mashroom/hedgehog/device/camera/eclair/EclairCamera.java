package cc.mashroom.hedgehog.device.camera.eclair;

import  android.app.Activity;
import  android.graphics.BitmapFactory;
import  android.graphics.ImageFormat;
import  android.view.TextureView;

import  cc.mashroom.hedgehog.device.MediaRecorder;
import  cc.mashroom.hedgehog.device.camera.ErrorStateCallback;
import  cc.mashroom.hedgehog.device.camera.PhotoTakenListener;
import  cc.mashroom.hedgehog.system.PermissionChecker;
import  cc.mashroom.hedgehog.util.ImageUtils;
import  cc.mashroom.util.collection.map.ConcurrentHashMap;
import  cc.mashroom.util.collection.map.HashMap;
import  cc.mashroom.util.collection.map.Map;
import  lombok.AccessLevel;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.SneakyThrows;
import  lombok.experimental.Accessors;

import  android.hardware.Camera;
import  android.hardware.Camera.CameraInfo;
import  android.media.CamcorderProfile;
import  android.media.MediaRecorder.AudioSource;
import  android.media.MediaRecorder.VideoSource;

import  java.io.File;

public  class  EclairCamera  implements  cc.mashroom.hedgehog.device.camera.Camera,Camera.PictureCallback
{
	public  final  static  Map<Integer,CameraInfo>  CAMERA_INFOS = new  ConcurrentHashMap<Integer,CameraInfo>();

	public  EclairCamera(Activity  context )
	{
		this.context = context;
	}

	@Setter( value = AccessLevel.PROTECTED )
	@Accessors( chain=true )
	private  PhotoTakenListener  photoTakenListener;
	@Setter( value = AccessLevel.PROTECTED )
	@Accessors( chain=true )
	private  File  videoOutputFile;
	@Setter( value = AccessLevel.PROTECTED )
	@Accessors( chain=true )
	private  Activity  context;
	private  TextureView  textureView;
	private  int   position;
	@Setter( value = AccessLevel.PROTECTED )
	@Accessors( chain=true )
	private  Camera  camera;
	@Setter( value = AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
	private  MediaRecorder    videoRecorder;
	@Setter( value = AccessLevel.PROTECTED )
	@Accessors( chain=true )
	private  Map<Integer,Camera.Size>  optimalSizes  = new HashMap<Integer,Camera.Size>();
	@Setter( value = AccessLevel.PROTECTED )
	@Getter
	@Accessors( chain=true )
	private  ErrorStateCallback  errorStateCallback;

	static
	{
		for( int  i = 0;i<= Camera.getNumberOfCameras()-1;i++ )
		{
			CameraInfo  cameraInfo   = new  CameraInfo();

			Camera.getCameraInfo(  i , cameraInfo );

			CAMERA_INFOS.addEntry( i , cameraInfo );
		}
	}
	
	public  void  takePicture( PhotoTakenListener  listener )  throws  Exception
	{
		setPhotoTakenListener(   listener );

		camera.takePicture(null,null,this );
	}
	
	public  EclairCamera  preview( String  cameraID,TextureView  textureView,ErrorStateCallback  errorStateCallback )  throws  Exception
	{
		release();

		setErrorStateCallback(errorStateCallback).setCamera( Camera.open(this.position  = Integer.parseInt( cameraID )) );

		if( camera == null || !PermissionChecker.checkCameraPermission(camera) )
		{
			throw  new  IllegalStateException( "SQUIRREL-WIDGET:  ** ECLAIR  CAMERA **  camera  permission  is  required,  but  it  is  not  granted." );
		}

		optimalSizes.addEntry(0,CameraCaptureUtils.getOptimalSize(context.getWindowManager().getDefaultDisplay(),camera.getParameters().getSupportedPreviewSizes())).addEntry(1,CameraCaptureUtils.getOptimalSize(context.getWindowManager().getDefaultDisplay(),camera.getParameters().getSupportedPictureSizes())).addEntry( 2,CameraCaptureUtils.getOptimalSize(context.getWindowManager().getDefaultDisplay(),camera.getParameters().getSupportedVideoSizes()) );

		camera.setDisplayOrientation(  90 );

		Camera.Parameters  parameters    = camera.getParameters();

		parameters.setRotation(   CAMERA_INFOS.get( this.position ).facing == CameraInfo.CAMERA_FACING_FRONT ? 270 : 90 );

		//  video  (disabled  default  by  front  facting  camera)  should  be  enabled  to  flip  horizontally  if  using  front  facing  camera.
		if( CAMERA_INFOS.get( this.position ).facing   == CameraInfo.CAMERA_FACING_FRONT )
		{
			parameters.set( "video-flip","flip-v" );
		}

		parameters.setPreviewSize( optimalSizes.get(0).width,optimalSizes.get(0).height );

		parameters.setPictureFormat(  ImageFormat.JPEG );

		parameters.setPictureSize( optimalSizes.get(1).width,optimalSizes.get(1).height );

		camera.setParameters(  parameters );

		camera.setPreviewTexture(  (this.textureView = textureView).getSurfaceTexture() );

		camera.startPreview();
		
		return  this;
	}

	public  void  release()
	{
		if( camera   != null )
		{
			camera.stopPreview();

			camera.release( );
		}
	}

	public  File     stopRecordVideo()  throws  Exception
	{
		videoRecorder.close();

		videoRecorder  = null;

        camera.stopPreview( );

		return   videoOutputFile;
	}

	@SneakyThrows
	public  void  onPictureTaken( byte[]  bytes , Camera  camera )
	{
		if( CAMERA_INFOS.get( this.position ).facing   == CameraInfo.CAMERA_FACING_FRONT )
		{
			bytes = ImageUtils.readBitmapToByteArray( ImageUtils.flipHorizontal(BitmapFactory.decodeByteArray(bytes,0, bytes.length)) );
		}

		photoTakenListener.onPhotoTaken(    bytes );
	}

	public  void     continuePreview()  throws  Exception
	{
		preview( String.valueOf(this.position),this.textureView,this.errorStateCallback );
	}

	public  EclairCamera  switchOrientation()    throws  Exception
	{
		if( EclairCamera.CAMERA_INFOS.size()  >= 2 )
		{
			preview( String.valueOf( position = (position+1 >= CAMERA_INFOS.size() ? 0 : position+1) ),textureView,errorStateCallback );
		}

		return  this;
	}

	public  void  recordVideo( File  outputFile )throws  Exception
	{
		setVideoOutputFile(outputFile).setVideoRecorder(new  MediaRecorder()).getVideoRecorder().prepare(camera,AudioSource.MIC,VideoSource.CAMERA,CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH),videoOutputFile,CAMERA_INFOS.get(position).facing == CameraInfo.CAMERA_FACING_FRONT ? 270 : 90).wrapped().start();
	}
}
