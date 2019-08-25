package cc.mashroom.hedgehog.module.common.activity;

import  android.net.Uri;
import  android.os.Bundle;

import  com.irozon.sneaker.Sneaker;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.webkit.DynamicService;
import  cc.mashroom.util.FileUtils;
import  cc.mashroom.util.ObjectUtils;

import  java.io.File;

import  cc.mashroom.hedgehog.util.ContextUtils;
import  lombok.SneakyThrows;
import  me.relex.photodraweeview.PhotoDraweeView;
import  okhttp3.ResponseBody;
import  retrofit2.Call;
import  retrofit2.Callback;
import  retrofit2.Response;

public  class  ImagePreviewActivity   extends    AbstractActivity
{
	protected  void  onCreate( Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		ContextUtils.setupImmerseBar( this );

		setContentView( R.layout.activity_image_preview  );

		ObjectUtils.cast(super.findViewById(R.id.picture),PhotoDraweeView.class).setOnViewTapListener( (view,x,y) -> ContextUtils.finish(this) );

		File  imageFile   = new  File( super.getIntent().getStringExtra( "PATH" ) );

		if( imageFile.exists()  )
		{
			ObjectUtils.cast(super.findViewById(R.id.picture),PhotoDraweeView.class).setPhotoUri( Uri.fromFile(imageFile) );
		}
		else
		{
			if( application().getFileDownloadRetrofit() == null )
			{
				throw  new  IllegalStateException( "HEDGEHOT-PARENT:  ** APPLICATION **  file  download  retrofit  in  application  should  be  set  first." );
			}

			application().getFileDownloadRetrofit().create(DynamicService.class).get(super.getIntent().getStringExtra("URL")).enqueue
			(
				new  Callback<ResponseBody>()
				{
					public  void  onFailure( Call<ResponseBody>  call,Throwable  t )
					{
						showSneakerWindow( Sneaker.with(ImagePreviewActivity.this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(ImagePreviewActivity.this),500)),com.irozon.sneaker.R.drawable.ic_error,R.string.network_or_internal_server_error,R.color.white,R.color.red );
					}

					@SneakyThrows
					public  void  onResponse(Call<ResponseBody>  call,Response<ResponseBody>  retrofitResponse )
					{
						if( retrofitResponse.code()     ==  200 )
						{
							ObjectUtils.cast(ImagePreviewActivity.this.findViewById(R.id.picture),PhotoDraweeView.class).setPhotoUri( Uri.fromFile(FileUtils.createFileIfAbsent(imageFile,retrofitResponse.body().bytes())) );
						}
						else
						{
							showSneakerWindow( Sneaker.with(ImagePreviewActivity.this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(ImagePreviewActivity.this),500)),com.irozon.sneaker.R.drawable.ic_error,R.string.file_not_found,R.color.white,R.color.red );
						}
					}
				}
			);
		}
	}
}
