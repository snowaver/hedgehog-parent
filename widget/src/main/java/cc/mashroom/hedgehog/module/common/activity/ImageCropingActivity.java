package cc.mashroom.hedgehog.module.common.activity;

import  android.content.Intent;
import  android.graphics.Bitmap;
import  android.net.Uri;
import  android.os.Bundle;
import  android.view.WindowManager;
import  android.widget.TextView;

import  com.irozon.sneaker.Sneaker;
import  com.steelkiwi.cropiwa.CropIwaView;
import  com.steelkiwi.cropiwa.config.CropIwaSaveConfig;

import  java.io.File;
import  java.net.URI;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.util.ContextUtils;
import  cc.mashroom.hedgehog.util.DensityUtils;
import  cc.mashroom.util.FileUtils;
import  cc.mashroom.util.ObjectUtils;

public  class  ImageCropingActivity    extends  AbstractActivity  implements  CropIwaView.CropSaveCompleteListener
{
	protected  void  onCreate( Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		super.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );

		setContentView( R.layout.activity_image_croping  );

		ObjectUtils.cast(super.findViewById(R.id.additional_text),TextView.class).setOnClickListener( (view) -> {try{ ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).crop(new  CropIwaSaveConfig.Builder(Uri.fromFile(FileUtils.createFileIfAbsent(new  File(application().getCacheDir(),"image.png.tmp"),null))).setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).setSize(200,200).build()); }catch(Exception  e){ e.printStackTrace(); }} );

		float  cropScale = ( (float)  super.getResources().getDisplayMetrics().widthPixels / 2-DensityUtils.px(this,11) )   / ( (float)  super.getResources().getDisplayMetrics().widthPixels / 2 );

		ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).configureOverlay().setCropScale(cropScale).apply();

		ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).setCropSaveCompleteListener(this );

		ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).setErrorListener( (error) -> setSneakerView(Sneaker.with(this),com.irozon.sneaker.R.drawable.ic_error,R.string.cropping_error,R.color.white).autoHide(true).setDuration(3000).setHeight(DensityUtils.dp(this,ContextUtils.getStatusBarHeight(this))+50).sneakError() );

		ObjectUtils.cast(super.findViewById(R.id.crop_view),CropIwaView.class).setImageUri( Uri.fromFile(new  File(super.getIntent().getStringExtra("PATH"))) );
	}

	public  void  onCroppedRegionSaved(    Uri  bitmapUri )
	{
		try
		{
			super.putResultDataAndFinish( this,1,new  Intent().putExtra("CROPPED",application().cache(-1,FileUtils.readFileToByteArray(new  File(URI.create(bitmapUri.toString()))),1).getPath()) );
		}
		catch( Throwable  e )
		{
			super.error( e );

			setSneakerView(Sneaker.with(this),com.irozon.sneaker.R.drawable.ic_error,R.string.cropping_error,R.color.white).autoHide(true).setDuration(3000).setHeight(DensityUtils.dp(this,ContextUtils.getStatusBarHeight(this))+50).sneakError();
		}
	}
}
