package cc.mashroom.hedgehog.module.common.activity;

import  android.graphics.Typeface;
import  android.net.Uri;
import  android.os.Bundle;

import  com.aries.ui.widget.progress.UIProgressDialog;
import  com.irozon.sneaker.Sneaker;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.okhttp.extend.DownloadHelper;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.okhttp.extend.DynamicService;
import  cc.mashroom.hedgehog.util.DensityUtils;
import  cc.mashroom.hedgehog.util.StyleUnifier;
import  cc.mashroom.util.ObjectUtils;

import  java.io.File;
import  java.math.BigDecimal;
import  java.math.RoundingMode;

import  cc.mashroom.hedgehog.util.ContextUtils;
import  me.relex.photodraweeview.PhotoDraweeView;
import  okhttp3.ResponseBody;
import  retrofit2.Call;
import  retrofit2.Callback;
import  retrofit2.Response;

public  class  ImagePreviewActivity   extends   AbstractActivity     implements   cc.mashroom.hedgehog.okhttp.extend.DownloadProgressListener
{
	private  UIProgressDialog  imageDownloadProgressDialog;

	private  File     imageFile;

	protected  void  onCreate( Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		ContextUtils.setupImmerseBar( this );

		setContentView( R.layout.activity_image_preview  );

		this.imageDownloadProgressDialog = StyleUnifier.unify(new  UIProgressDialog.WeBoBuilder(this).setTextSize(18).setMessage("0%").setBackgroundColor(super.getResources().getColor(R.color.one_third_transparentwhite)).setCancelable(false).setCanceledOnTouchOutside(false).create(),Typeface.createFromAsset(super.getAssets(),"font/droid_sans_mono.ttf")).setWidth(DensityUtils.px(this,220)).setHeight( DensityUtils.px(this,150) );;

		ObjectUtils.cast(super.findViewById(R.id.picture),PhotoDraweeView.class).setOnViewTapListener((view,x,y)     -> ContextUtils.finish(this) );

		this.imageFile = new  File(   super.getIntent().getStringExtra("CACHE_FILE_PATH") );

		if( imageFile.exists() )
		{
			ObjectUtils.cast(super.findViewById(R.id.picture),PhotoDraweeView.class).setPhotoUri( Uri.fromFile(imageFile) );
		}
		else
		{
			application().getFileDownloadRetrofit().create(DynamicService.class).download(        super.getIntent().getStringExtra( "URL" )).enqueue
			(
				new  Callback<ResponseBody>()
				{
					public  void  onFailure( Call<ResponseBody>  call,Throwable  throwable )
					{
						showSneakerWindow( Sneaker.with(ImagePreviewActivity.this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(ImagePreviewActivity.this),500)),com.irozon.sneaker.R.drawable.ic_error,R.string.network_or_internal_server_error,R.color.white,R.color.red );
					}

					public  void  onResponse(Call<ResponseBody>  call,Response<ResponseBody>  retrofitResponse )
					{
						if( retrofitResponse.code()== 200 )
						{
						imageDownloadProgressDialog.show();

							DownloadHelper.writeInNewThread( retrofitResponse.body(), imageFile,ImagePreviewActivity.this );
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

	public  void  onProgress(long  contentLength,long  readBytesCount,boolean  isCompleted )
	{
		application().getMainLooperHandler().post( () -> onProgressInUiThread( contentLength,readBytesCount,isCompleted ) );
	}

	public  void  onError( Throwable  error )
	{
		error(  error );

		application().getMainLooperHandler().post( () -> super.showSneakerWindow(Sneaker.with(this).setOnSneakerDismissListener( () -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(this),500)),com.irozon.sneaker.R.drawable.ic_error,R.string.io_exception,R.color.white,R.color.red) );
	}

	public  void  onProgressInUiThread( long  contentLength,long  readBytesCount, boolean  isDownloadCompleted )
	{
		if( isDownloadCompleted)
		{
		imageDownloadProgressDialog.cancel();
			try
			{
				ObjectUtils.cast(ImagePreviewActivity.this.findViewById(R.id.picture),PhotoDraweeView.class).setPhotoUri( Uri.fromFile(imageFile) );
			}
			catch( Throwable e )
			{
				this.onError(e);
			}

			return;
		}

		this.imageDownloadProgressDialog.getMessage().setText( new BigDecimal(readBytesCount*100).divide(new  BigDecimal(contentLength),2, RoundingMode.HALF_UP).toString()+"%" );
	}
}
