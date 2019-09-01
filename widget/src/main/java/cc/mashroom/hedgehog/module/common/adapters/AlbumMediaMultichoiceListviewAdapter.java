package cc.mashroom.hedgehog.module.common.adapters;

import  android.content.Intent;
import  android.net.Uri;
import  androidx.core.app.ActivityCompat;
import  android.view.View;
import  android.widget.ImageView;

import  com.facebook.drawee.view.SimpleDraweeView;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.module.common.activity.ImagePreviewActivity;
import  cc.mashroom.hedgehog.module.common.activity.VideoPreviewActivity;
import  cc.mashroom.hedgehog.module.common.listener.MediaMultichoicesListener;
import  cc.mashroom.hedgehog.module.common.listener.MultichoicesListener;
import  cc.mashroom.hedgehog.module.common.activity.AlbumMediaMultichoiceActivity;
import  cc.mashroom.hedgehog.parent.BaseMulticolumnAdapter;
import  cc.mashroom.hedgehog.system.Media;
import  cc.mashroom.hedgehog.system.MediaStore;
import  cc.mashroom.hedgehog.system.MediaType;
import  cc.mashroom.util.ObjectUtils;
import  cc.mashroom.util.collection.map.HashMap;
import  cc.mashroom.util.collection.map.Map;
import  cn.refactor.library.SmoothCheckBox;
import  lombok.AccessLevel;
import  lombok.Setter;
import  lombok.experimental.Accessors;

import  java.io.File;
import  java.util.ArrayList;
import  java.util.List;

public  class     AlbumMediaMultichoiceListviewAdapter   extends  BaseMulticolumnAdapter<Media>
{
	private  final  static  Map<Integer,Uri[]>  MEDIA_TYPE_URIS = new  HashMap<Integer,Uri[]>().addEntry(1,new  Uri[]{android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI}).addEntry(2,new  Uri[]{android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI}).addEntry(3,new  Uri[]{android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI});

	public  AlbumMediaMultichoiceListviewAdapter( AlbumMediaMultichoiceActivity  context,int  mediaType,int  columnSize,int  maxCount,long  maxFileSize,SmoothCheckBox.OnCheckedChangeListener  checkedChangeListener )
	{
		super( context, MediaStore.get( context,MEDIA_TYPE_URIS.get(mediaType)), columnSize,R.layout.activity_album_media_multichoice_line );

		this.setContext(context).setMeidas(items).setMultichoicesListener( new  MediaMultichoicesListener(context,this,maxCount,maxFileSize,checkedChangeListener) );
	}

	@Setter( value=AccessLevel.PROTECTED )
	@Accessors( chain=true )
	protected  MultichoicesListener  multichoicesListener;
	@Setter( value=AccessLevel.PROTECTED )
	@Accessors( chain=true )
	protected  AlbumMediaMultichoiceActivity  context;
	@Setter( value=AccessLevel.PROTECTED )
	@Accessors( chain=true )
	protected  int maxCount;
	@Setter( value=AccessLevel.PROTECTED )
	@Accessors( chain=true )
	protected  List<Media>  meidas;

	public  List<Media>  getChosenMedias()
	{
		return  new  ArrayList<Media>(multichoicesListener.getChoicesMapper() );
	}

	public  void  getChildView( int  rowIndex,int  columnIndex,Media  media,View  convertView )
	{
		ObjectUtils.cast(convertView.findViewById(R.id.image),SimpleDraweeView.class).setImageURI(Uri.fromFile(new  File(media.getPath())) );

		ObjectUtils.cast(convertView.findViewById(R.id.play_button),ImageView.class).setVisibility( media.getType() ==  MediaType.VIDEO ? View.VISIBLE : View.GONE );

		ObjectUtils.cast(convertView.findViewById(R.id.multichoice_checkbox),SmoothCheckBox.class).setTag( media );

		ObjectUtils.cast(convertView.findViewById(R.id.multichoice_checkbox),SmoothCheckBox.class).setOnCheckedChangeListener(   multichoicesListener );

		ObjectUtils.cast(convertView.findViewById(R.id.multichoice_checkbox),SmoothCheckBox.class).setChecked(  this.multichoicesListener.getChoicesMapper().contains(media) );

		ObjectUtils.cast(convertView.findViewById(R.id.image),SimpleDraweeView.class).setOnClickListener( (view) -> ActivityCompat.startActivity(context,new  Intent(context,media.getType() == MediaType.IMAGE ? ImagePreviewActivity.class : VideoPreviewActivity.class).putExtra("CACHE_FILE_PATH",media.getPath()),null) );

		ObjectUtils.cast(convertView.findViewById(R.id.play_button), ImageView.class).setOnClickListener( (view) -> ActivityCompat.startActivity(context,new  Intent(context,VideoPreviewActivity.class).putExtra("CACHE_FILE_PATH",media.getPath()),null) );
	}
}
