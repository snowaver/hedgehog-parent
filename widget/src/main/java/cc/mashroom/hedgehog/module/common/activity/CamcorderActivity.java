package cc.mashroom.hedgehog.module.common.activity;

import  android.Manifest;
import  android.graphics.SurfaceTexture;
import  android.graphics.Typeface;
import  android.hardware.camera2.CameraCharacteristics;
import  android.hardware.camera2.CameraDevice;
import  android.os.Bundle;
import  androidx.annotation.NonNull;
import  android.view.TextureView;
import  android.widget.RelativeLayout;

import  com.aries.ui.widget.alert.UIAlertDialog;
import  com.facebook.drawee.view.SimpleDraweeView;
import  com.irozon.sneaker.Sneaker;

import  androidx.annotation.UiThread;
import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.device.camera.Camera;
import  cc.mashroom.hedgehog.device.camera.eclair.EclairCamera;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.module.common.listener.CamcorderListener;
import  cc.mashroom.hedgehog.device.camera.ErrorStateCallback;
import  cc.mashroom.hedgehog.util.ContextUtils;
import  cc.mashroom.hedgehog.util.StyleUnifier;
import  cc.mashroom.hedgehog.util.ImageUtils;
import  cc.mashroom.hedgehog.widget.ViewSwitcher;
import  cc.mashroom.util.ObjectUtils;

import  cc.mashroom.util.collection.map.HashMap;
import  cc.mashroom.util.collection.map.Map;
import  cc.mashroom.hedgehog.widget.HeaderBar;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.SneakyThrows;
import  lombok.experimental.Accessors;
import  permissions.dispatcher.NeedsPermission;
import  permissions.dispatcher.OnShowRationale;
import  permissions.dispatcher.PermissionRequest;
import  permissions.dispatcher.PermissionUtils;
import  permissions.dispatcher.RuntimePermissions;

/**
 *  take  photo  or  record  video  and  return  medias  (intent  parameters:  MEDIAS)  by  result  data.  intent  parameters:  MEDIA_TYPE  (1.photo  2.video  3.photo  or  video.  NOTE:  click  to  take  photo  but  long  press  to  record  video).  .
 */
@RuntimePermissions

public  class  CamcorderActivity  extends   AbstractActivity    implements   TextureView.SurfaceTextureListener,ErrorStateCallback
{
	protected  void  onCreate(  Bundle  savedInstanceState )
	{
		super.onCreate(savedInstanceState);

		ContextUtils.setupImmerseBar(this);

		super.setContentView( R.layout.activity_camcorder );

		this.mediaType = super.getIntent().getIntExtra( "MEDIA_TYPE", 3 );

		ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).setTitle(  super.getString(titles.get(mediaType)) );

		ObjectUtils.cast(super.findViewById(R.id.take_picture_or_record_video_button),SimpleDraweeView.class).setImageURI( ImageUtils.toUri(this,R.drawable.red_placeholder) );

		ObjectUtils.cast(super.findViewById(R.id.texture_view),TextureView.class).setSurfaceTextureListener( this );

		RelativeLayout.LayoutParams  layoutParams     = ObjectUtils.cast( super.findViewById(R.id.header_bar).getLayoutParams() );

		layoutParams.topMargin = ContextUtils.getStatusBarHeight(  this );

		super.findViewById(R.id.header_bar).setLayoutParams(layoutParams);

		ObjectUtils.cast(super.findViewById(R.id.additional_switcher),ViewSwitcher.class).setOnClickListener( (view) -> {try{camera.switchOrientation();}  catch(Exception  e)  { e.printStackTrace(); }} );
	}

	private  Map<Integer,Integer>  titles = new  HashMap<Integer,Integer>().addEntry(1,R.string.camera_take_photo).addEntry(2,R.string.camera_record_video).addEntry( 3,R.string.camera_take_photo_or_record_video );

	@Accessors( chain = true )
	@Setter
	private  CamcorderListener  camcorderListener;
	@Accessors( chain = true )
	@Setter
	private  Camera    camera;
	@Accessors( chain = true )
	@Setter
	private  int    mediaType;
	@Accessors( chain = true )
	@Setter
	@Getter
	private  boolean  isRequestingNeedPermissions;

	@SneakyThrows
	public  void  onSurfaceTextureAvailable(   SurfaceTexture  surface,int  width,int  height )
	{
		new  Thread(       () -> CamcorderActivityPermissionsDispatcher.afterPermissionsGrantedWithPermissionCheck(this)).start();
	}

	public  void  onError( CameraDevice  device,int  errorCode,Throwable  throwable )
	{
		error(    throwable );

		ContextUtils.finish( this );
	}

	public  void  onSurfaceTextureUpdated( SurfaceTexture  surface )
	{

	}

	public  boolean  onSurfaceTextureDestroyed(  SurfaceTexture  surface )
	{
		if( camera   != null )
		{
			camera.release( );
		}

		return  false;
	}

	public  void  onSurfaceTextureSizeChanged( SurfaceTexture  surface,int  width,int  height )
	{

	}

	@UiThread
	@Override
	public  void  onRequestPermissionsResult(   int  requestCode,@NonNull  String[]  permissions,@NonNull  int[]  grantedResults )
	{
		super.onRequestPermissionsResult( requestCode, permissions, grantedResults );

		CamcorderActivityPermissionsDispatcher.onRequestPermissionsResult( CamcorderActivity.this, requestCode , grantedResults );

		if( !    PermissionUtils.verifyPermissions(grantedResults) )
		{
			super.showSneakerWindow( Sneaker.with(this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(this),500)),com.irozon.sneaker.R.drawable.ic_error,R.string.permission_denied,R.color.white,R.color.red );
		}
	}

	@NeedsPermission( {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO} )

	public  void  afterPermissionsGranted()
	{
		application().getMainLooperHandler().post(   () -> initialize() );
	}

	@SneakyThrows
	public  void  initialize()
	{
		//  lollipop  camera  (camera2)  do  not  work  well  on  xiaomi  4a  for  runtime  exception  ( stop  failed:  -1007 )  but  no  way  to  resolve  it  for  xiaomi  customized  android  system,  so  use  eclair  camera  (camera1)  instead.
		try
		{
			this.setCamcorderListener( new  CamcorderListener(this,camera = new  EclairCamera(this).preview(String.valueOf(CameraCharacteristics.LENS_FACING_FRONT),ObjectUtils.cast(super.findViewById(R.id.texture_view),TextureView.class),this),mediaType) );
		}
		catch( IllegalStateException  ise )
		{
			super.error(ise );

			super.showSneakerWindow( Sneaker.with(this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(this),500)),com.irozon.sneaker.R.drawable.ic_error,R.string.permission_denied,R.color.white,R.color.red );

			return;
		}

		ObjectUtils.cast(super.findViewById(R.id.take_picture_or_record_video_button),SimpleDraweeView.class).setOnTouchListener( camcorderListener );

		ObjectUtils.cast(super.findViewById(R.id.take_picture_or_record_video_button),SimpleDraweeView.class).setOnLongClickListener( camcorderListener );

		ObjectUtils.cast(super.findViewById(R.id.take_picture_or_record_video_button),SimpleDraweeView.class).setOnClickListener( camcorderListener );

		ObjectUtils.cast(super.findViewById(R.id.preview_button),SimpleDraweeView.class).setOnClickListener(  camcorderListener );

		ObjectUtils.cast(super.findViewById(R.id.confirm_button),SimpleDraweeView.class).setOnClickListener(  camcorderListener );
	}

	@OnShowRationale( {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO} )

	public  void  showPermissionRationale(     PermissionRequest  permissionRequest )
	{
		application().getMainLooperHandler().post( () -> StyleUnifier.unify(new  UIAlertDialog.DividerIOSBuilder(this).setBackgroundRadius(15).setTitle(R.string.notice).setTitleTextSize(18).setMessage(R.string.camera_require_camera_and_audio_record_permission).setMessageTextSize(18).setCancelable(false).setCanceledOnTouchOutside(false).setNegativeButtonTextSize(18).setNegativeButton(R.string.close,(dialog, which) -> {permissionRequest.cancel();  ContextUtils.finish(this);}).setPositiveButtonTextSize(18).setPositiveButton(R.string.ok,(dialog,which) -> permissionRequest.proceed()).create().setWidth((int)  (CamcorderActivity.this.getResources().getDisplayMetrics().widthPixels*0.9)),Typeface.createFromAsset(super.getAssets(),"font/droid_sans_mono.ttf")).show() );
	}
}
