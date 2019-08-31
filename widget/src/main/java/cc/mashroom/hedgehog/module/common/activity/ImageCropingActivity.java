package cc.mashroom.hedgehog.module.common.activity;

import  android.content.Intent;
import  android.graphics.Bitmap;
import  android.net.Uri;
import  android.os.Bundle;

import  com.google.common.collect.Lists;
import  com.irozon.sneaker.Sneaker;
import  com.steelkiwi.cropiwa.CropIwaView;
import  com.steelkiwi.cropiwa.config.CropIwaSaveConfig;

import  java.io.File;
import  java.net.URI;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.system.Media;
import  cc.mashroom.hedgehog.system.MediaType;
import  cc.mashroom.hedgehog.util.DensityUtils;
import  cc.mashroom.hedgehog.widget.ViewSwitcher;
import  cc.mashroom.util.FileUtils;
import  cc.mashroom.util.ObjectUtils;

/**
 *  crop  image  and  return  medias   (intent  parameters:  MEDIAS)  by  result  data.  intent  parameters:  IMAGE_FILE_PATH  (local  image  path  or  network  image  uri).
 */
public  class  ImageCropingActivity   extends  AbstractActivity   implements  CropIwaView.CropSaveCompleteListener
{
	protected  void  onCreate( Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		setContentView( R.layout.activity_image_croping  );

		ObjectUtils.cast(super.findViewById(R.id.additional_switcher),ViewSwitcher.class).setOnClickListener( (view) -> {try{ ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).crop(new  CropIwaSaveConfig.Builder(Uri.fromFile(FileUtils.createFileIfAbsent(new  File(application().getCacheDir(),"image.png.tmp"),null))).setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).setSize(200,200).build()); }catch(Exception  e){ e.printStackTrace(); }} );

		float  cropScale = ( (float)  super.getResources().getDisplayMetrics().widthPixels / 2-DensityUtils.px(this,11) ) / ((float)  super.getResources().getDisplayMetrics().widthPixels/2);

		ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).configureOverlay().setCropScale(cropScale).apply();

		ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).setCropSaveCompleteListener(this );

		ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).setErrorListener( (error) -> super.showSneakerWindow(Sneaker.with(this),com.irozon.sneaker.R.drawable.ic_error,R.string.image_cropping_error,R.color.white,R.color.red) );

		String  path = getIntent().getStringExtra( "IMAGE_FILE_PATH" );

		ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).setImageUri( path.trim().startsWith("/") ? Uri.fromFile(new  File(path)) : Uri.parse(path) );
	}

	public  void  onCroppedRegionSaved(    Uri  bitmapUri )
	{
		try
		{
			super.putResultDataAndFinish( this,1,new  Intent().putExtra("MEDIAS",Lists.newArrayList(new  Media(MediaType.IMAGE,0,new  File(URI.create(bitmapUri.toString())).getPath(),0))) );
		}
		catch( Throwable  e )
		{
			super.error( e );

			super.showSneakerWindow( Sneaker.with(this), com.irozon.sneaker.R.drawable.ic_error,R.string.image_cropping_error,R.color.white,R.color.red );
		}
	}
}
