package cc.mashroom.hedgehog.module.common.activity;

import  android.Manifest;
import  android.graphics.SurfaceTexture;
import  android.hardware.camera2.CameraCharacteristics;
import  android.hardware.camera2.CameraDevice;
import  android.os.Bundle;
import  androidx.annotation.NonNull;
import  android.view.TextureView;
import  android.widget.RelativeLayout;
import  android.widget.TextView;

import  com.aries.ui.widget.alert.UIAlertDialog;
import  com.facebook.drawee.view.SimpleDraweeView;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.device.camera.Camera;
import  cc.mashroom.hedgehog.device.camera.eclair.EclairCamera;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.module.common.listener.CamcorderListener;
import  cc.mashroom.hedgehog.device.camera.ErrorStateCallback;
import  cc.mashroom.hedgehog.util.ContextUtils;
import  cc.mashroom.hedgehog.util.ImageUtils;
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

@RuntimePermissions

public  class  CamcorderActivity  extends  AbstractActivity      implements  TextureView.SurfaceTextureListener,ErrorStateCallback
{
	protected  void  onCreate(  Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		ContextUtils.setupImmerseBar( this );

		super.setContentView( R.layout.activity_camcorder );

		setCaptureFlag( super.getIntent().getIntExtra("CAPTURE_FLAG",3) );

		ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).setTitle(super.getString(titles.get(captureFlag)) );

		ObjectUtils.cast(super.findViewById(R.id.take_picture_or_record_video_button),SimpleDraweeView.class).setImageURI( ImageUtils.toUri(this,R.drawable.red_placeholder) );

		ObjectUtils.cast(super.findViewById(R.id.texture_view),TextureView.class).setSurfaceTextureListener( this );

		RelativeLayout.LayoutParams  layoutParams     = ObjectUtils.cast( super.findViewById(R.id.header_bar).getLayoutParams() );

		layoutParams.topMargin = ContextUtils.getStatusBarHeight(  this );

		super.findViewById(R.id.header_bar).setLayoutParams(layoutParams);

		Sneaky.click( ObjectUtils.cast(super.findViewById(R.id.additional_text),TextView.class),(v)->camera.switchOrientation() );
	}

	private  Map<Integer,Integer>  titles = new  HashMap<Integer,Integer>().addEntry(1,R.string.taking_photo).addEntry(2,R.string.recording_video).addEntry( 3,R.string.taking_photo_or_recording_video );

	@Accessors( chain= true )
	@Setter
	private  CamcorderListener  camcorderListener;
	@Accessors( chain= true )
	@Setter
	private  Camera   camera;
	@Accessors( chain= true )
	@Setter
	private  int  captureFlag   = 3;
	@Accessors( chain= true )
	@Setter
	@Getter
	private  boolean  isRequestingNeedPermissions;

	@SneakyThrows
	public  void  onSurfaceTextureAvailable(   SurfaceTexture  surface,int  width,int  height )
	{
		CamcorderActivityPermissionsDispatcher.checkPermissionsWithPermissionCheck(     this );
	}

	public  void  onError( CameraDevice  device,int  errorCode,Throwable  throwable )
	{
		ContextUtils.finish( this );
	}

	public  void  onSurfaceTextureUpdated( SurfaceTexture  surface )
	{

	}

	public  boolean  onSurfaceTextureDestroyed(  SurfaceTexture  surface )
	{
		if( camera  != null )
		{
			camera.release();
		}

		return  false;
	}

	public  void  onSurfaceTextureSizeChanged( SurfaceTexture  surface,int  width,int  height )
	{

	}

	@Override
	public  void  onRequestPermissionsResult(   int  requestCode,@NonNull  String[]  permissions,@NonNull  int[]  grantedResults )
	{
		super.onRequestPermissionsResult( requestCode, permissions, grantedResults );

		CamcorderActivityPermissionsDispatcher.onRequestPermissionsResult( this,requestCode,grantedResults );

		if( ! PermissionUtils.verifyPermissions(grantedResults) )  ContextUtils.finish( this );
	}

	@NeedsPermission( {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO} )
	@SneakyThrows
	public  void  checkPermissions()
	{
		//  lollipop  camera  (camera2)  do  not  work  well  on  xiaomi  4a  for  runtime  exception  ( stop  failed:  -1007 )  but  no  way  to  resolve  it,  so  use  eclair  camera  (camera1)  instead.
		this.setCamcorderListener( new  CamcorderListener(this,camera = new  EclairCamera(this).preview(String.valueOf(CameraCharacteristics.LENS_FACING_FRONT),ObjectUtils.cast(super.findViewById(R.id.texture_view),TextureView.class),this),captureFlag) );

		ObjectUtils.cast(super.findViewById(R.id.take_picture_or_record_video_button),SimpleDraweeView.class).setOnTouchListener( camcorderListener );

		ObjectUtils.cast(super.findViewById(R.id.take_picture_or_record_video_button),SimpleDraweeView.class).setOnLongClickListener(    camcorderListener );

		ObjectUtils.cast(super.findViewById(R.id.take_picture_or_record_video_button),SimpleDraweeView.class).setOnClickListener( camcorderListener );

		ObjectUtils.cast(super.findViewById(R.id.preview_button),SimpleDraweeView.class).setOnClickListener(  camcorderListener );

		ObjectUtils.cast(super.findViewById(R.id.confirm_button),SimpleDraweeView.class).setOnClickListener(  camcorderListener );
	}

	@OnShowRationale( {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO} )

	public  void  showPermissionRationale(     PermissionRequest  permissionRequest )
	{
		new  UIAlertDialog.DividerIOSBuilder(this).setBackgroundRadius(15).setTitle(R.string.notice).setTitleTextSize(18).setMessage(R.string.camcorder_permission_check).setMessageTextSize(18).setCancelable(false).setCanceledOnTouchOutside(false).setNegativeButtonTextSize(18).setNegativeButton(R.string.close,(dialog, which) -> {permissionRequest.cancel();  ContextUtils.finish(this);}).setPositiveButtonTextSize(18).setPositiveButton(R.string.ok,(dialog,which) -> permissionRequest.proceed()).create().setWidth((int)  (CamcorderActivity.this.getResources().getDisplayMetrics().widthPixels*0.9)).show();
	}
}
