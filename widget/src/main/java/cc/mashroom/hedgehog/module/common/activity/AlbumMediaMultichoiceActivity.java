package cc.mashroom.hedgehog.module.common.activity;

import  android.Manifest;
import  android.content.Intent;
import  android.graphics.Typeface;
import  android.os.Bundle;
import  androidx.annotation.NonNull;
import  android.view.View;
import  android.view.ViewGroup;
import  android.widget.ListView;
import  android.widget.TextView;

import  com.aries.ui.widget.BasisDialog;
import  com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import  com.aries.ui.widget.alert.UIAlertDialog;
import  com.aries.ui.widget.progress.UIProgressDialog;
import  com.irozon.sneaker.Sneaker;

import  androidx.annotation.UiThread;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.module.common.adapters.AlbumMediaMultichoiceListviewAdapter;
import  cc.mashroom.hedgehog.system.Media;
import  cc.mashroom.hedgehog.util.ContextUtils;
import  cc.mashroom.hedgehog.util.DensityUtils;
import  cc.mashroom.hedgehog.util.StyleUnifier;
import  cc.mashroom.util.ObjectUtils;
import  cc.mashroom.hedgehog.widget.HeaderBar;
import  cc.mashroom.util.collection.map.HashMap;
import  cc.mashroom.util.collection.map.Map;
import  cn.refactor.library.SmoothCheckBox;
import  lombok.Setter;
import  lombok.experimental.Accessors;
import  permissions.dispatcher.NeedsPermission;
import  permissions.dispatcher.OnShowRationale;
import  permissions.dispatcher.PermissionRequest;
import  permissions.dispatcher.PermissionUtils;
import  permissions.dispatcher.RuntimePermissions;

import  java.io.Serializable;
import  java.util.List;

/**
 *  choose  album  medias  and  return  chosenes  (intent  parameters:  MEDIAS)  by  result  data.  intent  parameters:  MEDIA_TYPE  (1.photo,  2.video,  3.photo  and  video.  other  values  are  illegal.  default  3),  MAX_COUNT  (max  chosen  medias  count,  zero  is  illegal  and  negative  for  no  limitation.  default:  -1)  and  MAX_FILE_SIZE  (max  file  size,  zero  is  illegal  and  negative  for  no  limitation.  default:  -1).
 */
@RuntimePermissions

public  class  AlbumMediaMultichoiceActivity    extends  AbstractActivity  implements  UIActionSheetDialog.OnItemClickListener,SmoothCheckBox.OnCheckedChangeListener
{
	@OnShowRationale( value={Manifest.permission.READ_EXTERNAL_STORAGE} )

	public  void  showPermissionRationale(   PermissionRequest  permissionRequest )
	{
		application().getMainLooperHandler().post( () -> StyleUnifier.unify(new  UIAlertDialog.DividerIOSBuilder(this).setBackgroundRadius(15).setTitle(R.string.notice).setTitleTextSize(18).setMessage(R.string.album_require_album_permission).setMessageTextSize(18).setCancelable(false).setCanceledOnTouchOutside(false).setNegativeButtonTextSize(18).setNegativeButton(R.string.close,(dialog,which) -> {permissionRequest.cancel();  ContextUtils.finish(this);}).setPositiveButtonTextSize(18).setPositiveButton(R.string.ok,(dialog,which) -> permissionRequest.proceed()).create().setWidth((int)  (super.getResources().getDisplayMetrics().widthPixels*0.9)),Typeface.createFromAsset(super.getAssets(),"font/droid_sans_mono.ttf")).show() );
	}

	@Accessors( chain = true )
	@Setter
	protected  int   mediaType   =  3;
	@Setter
	protected  boolean  initialized = false;
    @Accessors( chain = true )
    @Setter
    protected  int   maxCount    = -1;
	protected  UIProgressDialog  progressDialog;
    @Accessors( chain = true )
    @Setter
    protected  long  maxFileSize = -1;

	protected  void  onCreate(      Bundle  savedInstanceState )
	{
		super.onCreate(savedInstanceState );

		super.setContentView(R.layout.activity_album_media_multichoice );

		this.setMediaType(super.getIntent().getIntExtra("MEDIA_TYPE",3)).setMaxCount(super.getIntent().getIntExtra("MAX_COUNT",-1)).setMaxFileSize( super.getIntent().getLongExtra("MAX_FILE_SIZE", -1) );

		if( mediaType != 1 && mediaType != 2 && mediaType != 3 )
        {
            throw  new  IllegalArgumentException( String.format("HEDGEHOG-PARENT:  ** ALBUM  MEDIA  MULTICHOICE  ACTIVITY **  media  type  (%d)  is  illegal.",   mediaType) );
        }

		super.findViewById(R.id.additional_switcher).setOnClickListener( (view) -> StyleUnifier.unify(new  UIActionSheetDialog.ListIOSBuilder(this).setBackgroundRadius(15).addItem(R.string.photo).addItem(R.string.video).addItem(R.string.album_photo_and_video).setItemsTextSize(18).setCancel(R.string.close).setCancelTextColorResource(R.color.red).setCancelTextSize(18).setItemsMinHeight(DensityUtils.px(this,50)).setPadding(DensityUtils.px(this,10)).setCanceledOnTouchOutside(true).setOnItemClickListener(this).create(),Typeface.createFromAsset(super.getAssets(),"font/droid_sans_mono.ttf")).show() );

		super.findViewById(R.id.ok_button).setOnClickListener( (view) -> super.putResultDataAndFinish(this,0,new  Intent().putExtra("MEDIAS",ObjectUtils.cast(ObjectUtils.cast(ObjectUtils.cast(super.findViewById(R.id.album_media_list),ListView.class).getAdapter(),AlbumMediaMultichoiceListviewAdapter.class).getChosenMedias(),Serializable.class))) );

		this.progressDialog = StyleUnifier.unify(new  UIProgressDialog.WeBoBuilder(this).setTextSize(18).setMessage(R.string.waiting).setCanceledOnTouchOutside(false).create(),Typeface.createFromAsset(this.getAssets(),"font/droid_sans_mono.ttf")).setWidth(DensityUtils.px(this,220)).setHeight( DensityUtils.px(this,150) );
	}

	protected  void  onStart()
	{
		super.onStart();
		//  initialize  or  retain  the  album  media  listview  state  by  initialized  flag,  else  the  list  adapter  is  reset  in  the  initialize  method  after  permissions  granted  and  seems  a  scrolling  to  the  list  top.
		if( !    initialized )
		{
			new  Thread(() -> AlbumMediaMultichoiceActivityPermissionsDispatcher.afterPermissionsGrantedWithPermissionCheck(this)).start();
		}
	}

	@NeedsPermission( value={Manifest.permission.READ_EXTERNAL_STORAGE} )

	public  void   afterPermissionsGranted()
	{
		application().getMainLooperHandler().post(  () -> initialize() );
	}

	public  void  initialize()
	{
		progressDialog.show();

		initialized    = true;

		ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).setTitle( super.getString(this.titles.get(this.mediaType)) );

		super.findViewById(R.id.additional_switcher).setVisibility( mediaType == 1 || mediaType == 2 ? View.GONE : View.VISIBLE );

		ObjectUtils.cast(super.findViewById(R.id.album_media_list),ListView.class).setAdapter( new  AlbumMediaMultichoiceListviewAdapter( this,mediaType,3,maxCount,maxFileSize,this) );  this.progressDialog.cancel();
	}

	private  Map<Integer,Integer>  titles = new  HashMap<Integer,Integer>().addEntry(1,R.string.photo).addEntry(2,R.string.video).addEntry( 3,R.string.album_photo_and_video );

	@UiThread
	public  void  onRequestPermissionsResult( int  requestCode, @NonNull  String[]  permissions, @NonNull  int[]  grantedResults )
	{
		super.onRequestPermissionsResult( requestCode,permissions,grantedResults );

		AlbumMediaMultichoiceActivityPermissionsDispatcher.onRequestPermissionsResult( this,requestCode,grantedResults );

		if( !PermissionUtils.verifyPermissions(grantedResults) )
		{
			super.showSneakerWindow( Sneaker.with(this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed( () -> ContextUtils.finish(this),500)),com.irozon.sneaker.R.drawable.ic_error,R.string.permission_denied,R.color.white,R.color.red );
		}
	}

	public  void  onClick( BasisDialog  dialog,View  view,int  position )
	{
		ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).setTitle(     super.getString(this.titles.get(position+1)) );

		ObjectUtils.cast(super.findViewById(R.id.album_media_list),ListView.class).setAdapter(         new  AlbumMediaMultichoiceListviewAdapter(this,position+1,3,this.maxCount,this.maxFileSize,this) );
	}

	public  void  onCheckedChanged( SmoothCheckBox compoundButton,boolean checked )
	{
		ListView  listview = ObjectUtils.cast( super.findViewById(R.id.album_media_list),ListView.class );

		List<Media>  choosedMedias  = ObjectUtils.cast(listview.getAdapter(),AlbumMediaMultichoiceListviewAdapter.class).getChosenMedias();

		if( this.maxCount == 1  && checked )
		{
			int  firstVisiblePosition=listview.getFirstVisiblePosition();

			for( int  position = firstVisiblePosition;      position <= listview.getLastVisiblePosition();  position= position+1 )
			{
				ViewGroup line = ObjectUtils.cast( listview.getChildAt(position-firstVisiblePosition), ViewGroup.class );

				for(      int  childPosition = 0;childPosition <= line.getChildCount()-1;childPosition++ )
				{
					View  childView   = line.getChildAt( childPosition );

					if( ObjectUtils.cast(childView.findViewById(R.id.multichoice_checkbox),SmoothCheckBox.class).isChecked() && !choosedMedias.contains(ObjectUtils.cast(childView.findViewById(R.id.multichoice_checkbox),SmoothCheckBox.class).getTag()) )
					{
						ObjectUtils.cast(childView.findViewById(R.id.multichoice_checkbox),SmoothCheckBox.class).setChecked( false,false );
					}
				}
			}
		}

		super.findViewById(R.id.ok_button).setEnabled( ! choosedMedias.isEmpty() );

		ObjectUtils.cast(super.findViewById(R.id.ok_button),TextView.class).setTextColor( super.getResources().getColor(choosedMedias.isEmpty() ? R.color.darkgray : R.color.limegreen/* color  drakgray  for  disabled  but  limegreen  for  enabled */) );
	}
}