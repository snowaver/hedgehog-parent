package cc.mashroom.hedgehog.module.common.activity;

import  android.net.Uri;
import  android.os.Bundle;
import  android.widget.Toast;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.webkit.DynamicService;
import  cc.mashroom.util.FileUtils;
import  cc.mashroom.util.ObjectUtils;

import  java.io.File;

import  cc.mashroom.hedgehog.util.ContextUtils;
import  es.dmoral.toasty.Toasty;
import  lombok.SneakyThrows;
import  me.relex.photodraweeview.PhotoDraweeView;
import  okhttp3.ResponseBody;
import  retrofit2.Call;
import  retrofit2.Callback;
import  retrofit2.Response;

public  class  ImagePreviewActivity  extends  AbstractActivity
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
			application().getDefaultRetrofit().create(DynamicService.class).get(super.getIntent().getStringExtra("URL")).enqueue
			(
				new  Callback<ResponseBody>()
				{
					public  void  onFailure( Call<ResponseBody>  call,Throwable  t )
					{
						Toasty.error(ImagePreviewActivity.this,ImagePreviewActivity.this.getString(R.string.network_or_internal_server_error),Toast.LENGTH_LONG,false).show();
					}

					@SneakyThrows
					public  void  onResponse(Call<ResponseBody>  call,Response<ResponseBody>  retrofitResponse )
					{
						if( retrofitResponse.code()== 200 )
						{
							ObjectUtils.cast(ImagePreviewActivity.this.findViewById(R.id.picture),PhotoDraweeView.class).setPhotoUri( Uri.fromFile(FileUtils.createFileIfAbsent(imageFile,retrofitResponse.body().bytes())) );
						}
						else
						{
							Toasty.error(ImagePreviewActivity.this,ImagePreviewActivity.this.getString(R.string.file_not_found),Toast.LENGTH_LONG,false).show();
						}
					}
				}
			);
		}
	}
}
