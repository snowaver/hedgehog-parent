package cc.mashroom.hedgehog.module.common.activity;

import android.graphics.Typeface;
import  android.os.Bundle;
import  android.view.SurfaceHolder;
import  android.view.SurfaceView;
import  android.view.View;
import  android.view.ViewGroup;
import  android.widget.ImageView;
import  android.widget.RelativeLayout;
import  android.widget.SeekBar;

import androidx.core.content.res.ResourcesCompat;

import com.aries.ui.widget.progress.UIProgressDialog;
import  com.irozon.sneaker.Sneaker;

import  cc.mashroom.hedgehog.R;
import cc.mashroom.hedgehog.okhttp.extend.DownloadHelper;
import cc.mashroom.hedgehog.okhttp.extend.DownloadProgressListener;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.device.MediaPlayer;
import cc.mashroom.hedgehog.okhttp.extend.DynamicService;
import cc.mashroom.hedgehog.util.DensityUtils;
import cc.mashroom.hedgehog.util.ExtviewsAdapter;
import  cc.mashroom.hedgehog.widget.HeaderBar;
import  cc.mashroom.util.FileUtils;
import  cc.mashroom.util.ObjectUtils;

import  java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import  java.util.concurrent.ScheduledThreadPoolExecutor;
import  java.util.concurrent.TimeUnit;
import  java.util.concurrent.atomic.AtomicBoolean;

import  cc.mashroom.hedgehog.util.ContextUtils;
import  lombok.Setter;
import  lombok.SneakyThrows;
import  lombok.experimental.Accessors;
import  okhttp3.ResponseBody;
import  retrofit2.Call;
import  retrofit2.Callback;
import  retrofit2.Response;

public  class  VideoPreviewActivity  extends  AbstractActivity  implements  SurfaceHolder.Callback,android.media.MediaPlayer.OnPreparedListener,android.media.MediaPlayer.OnCompletionListener,android.media.MediaPlayer.OnSeekCompleteListener,SeekBar.OnSeekBarChangeListener,Runnable,DownloadProgressListener
{
	protected  void  onCreate( Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		ContextUtils.setupImmerseBar( this );

		setContentView( R.layout.activity_video_preview  );

		RelativeLayout.LayoutParams  layoutParams = ObjectUtils.cast( super.findViewById( R.id.header_bar ).getLayoutParams() );

		layoutParams.topMargin = ContextUtils.getStatusBarHeight(  this );

		super.findViewById(R.id.header_bar).setLayoutParams(layoutParams);

		this.setVideoFile( new  File(getIntent().getStringExtra("PATH")));

		ObjectUtils.cast(super.findViewById(R.id.video_surface),SurfaceView.class).getHolder().addCallback(this);

		ObjectUtils.cast(super.findViewById(R.id.video_surface),SurfaceView.class).setKeepScreenOn(       true );

		ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).setOnClickListener( (bar ) -> {} );

		ObjectUtils.cast(super.findViewById(R.id.seek_bar).getParent(),View.class).setOnClickListener( ( controlPanel ) -> {} );

		this.videoDownloadProgressDialog = ExtviewsAdapter.adapter(new  UIProgressDialog.WeBoBuilder(this).setTextSize(18).setMessage("0%").setCancelable(false).setCanceledOnTouchOutside(false).create(),Typeface.createFromAsset(super.getAssets(),"font/droid_sans_mono.ttf")).setWidth(DensityUtils.px(this,220)).setHeight( DensityUtils.px(this,150) );
	}

	private  AtomicBoolean  isTrackingTouch  = new AtomicBoolean( false );

	private  AtomicBoolean  isMediaPlayerPrepared  = new  AtomicBoolean();

	@Accessors( chain=true )
	@Setter
	private  File videoFile;
	private  UIProgressDialog  videoDownloadProgressDialog;
	@Accessors( chain=true )
	@Setter
	private  MediaPlayer  player;

	public  void  run()
	{
		if( player.getPlayer().isPlaying()&& !this.isTrackingTouch.get() )
		{
			application().getMainLooperHandler().post(   () -> ObjectUtils.cast(findViewById(R.id.seek_bar),SeekBar.class).setProgress(player.getPlayer().getCurrentPosition()) );
		}
	}

	public  void  surfaceChanged( SurfaceHolder  holder,int  format,int  width,int  height )
	{

	}

	private  ScheduledThreadPoolExecutor  progressUpdateThread = new  ScheduledThreadPoolExecutor(1 );

	@SneakyThrows
	public  void  surfaceDestroyed( SurfaceHolder  holder )
	{
		if( player != null )  player.close();

		progressUpdateThread.remove(  this );

		this.progressUpdateThread.shutdown();
	}

	public  void  onPrepared(     android.media.MediaPlayer  mediaPlayer )
	{
		isMediaPlayerPrepared.set(    true );

		mediaPlayer.start();

		application().getMainLooperHandler().post(  () -> initializeUIOnPlayerPrepared(mediaPlayer) );
	}

	protected  void  initializeUIOnPlayerPrepared( android.media.MediaPlayer   mediaPlayer )
	{
		ObjectUtils.cast(super.findViewById(R.id.play_button),ImageView.class).setVisibility(        View.GONE );

		SurfaceView  surfaceView=ObjectUtils.cast( super.findViewById(R.id.video_surface) );

		float  maxScale = Math.max( (float)  mediaPlayer.getVideoWidth()/(float)  surfaceView.getWidth(),(float)  mediaPlayer.getVideoHeight()/(float)  surfaceView.getHeight() );

		ViewGroup.LayoutParams  layout    = surfaceView.getLayoutParams();

		layout.width  = (int)  Math.ceil( (float)  mediaPlayer.getVideoWidth() / maxScale );

		layout.height = (int)  Math.ceil( (float)  mediaPlayer.getVideoHeight()/ maxScale );

		surfaceView.setLayoutParams(layout );

		this.progressUpdateThread.scheduleAtFixedRate( this, 0,1000,TimeUnit.MILLISECONDS );

		ObjectUtils.cast(super.findViewById(R.id.seek_bar),SeekBar.class).setOnSeekBarChangeListener(     this );

		ObjectUtils.cast(super.findViewById(R.id.video_surface),SurfaceView.class).setOnClickListener( (surface) -> {if( player.getPlayer().isPlaying() ){ player.pause();  ObjectUtils.cast(super.findViewById(R.id.play_button),ImageView.class).setVisibility(View.VISIBLE); }} );

		ObjectUtils.cast(super.findViewById(R.id.seek_bar),SeekBar.class).setMax(    mediaPlayer.getDuration() );

		ObjectUtils.cast(super.findViewById(R.id.play_button), ImageView.class).setOnClickListener( (playButton) -> {player.getPlayer().start();  ObjectUtils.cast(super.findViewById(R.id.play_button),ImageView.class).setVisibility(View.GONE);} );
	}

	protected    void  onPause()
	{
		super.onPause();

		if( this.player != null &&   this.player.getPlayer().isPlaying() )   player.pause();
	}

	@SneakyThrows
	public  void  surfaceCreated(   final   SurfaceHolder  surfaceHolder )
	{
		if( application().getFileDownloadRetrofit()==null )
		{
			throw  new  IllegalStateException( "HEDGEHOT-PARENT:  ** APPLICATION **  file  download  retrofit  in  application  should  be  set  first." );
		}

		if( videoFile.exists() )
		{
			setPlayer( new  MediaPlayer().play(   videoFile.getPath(),surfaceHolder,this,this,this) );
		}
		else
		{
			application().getFileDownloadRetrofit().create(DynamicService.class).get( getIntent().getStringExtra("URL")).enqueue
			(
				new  Callback<ResponseBody>()
				{
					public  void  onFailure( Call<ResponseBody>  call,Throwable  throwable )
					{
						showSneakerWindow( Sneaker.with(VideoPreviewActivity.this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(VideoPreviewActivity.this),500)),com.irozon.sneaker.R.drawable.ic_error,R.string.network_or_internal_server_error,R.color.white,R.color.red );
					}

					@SneakyThrows
					public  void  onResponse(Call<ResponseBody>  call,Response<ResponseBody>   retrofitResponse )
					{
						if( retrofitResponse.code()== 200 )
						{
						videoDownloadProgressDialog.show();

							DownloadHelper.download(      retrofitResponse.body(),FileUtils.createFileIfAbsent(videoFile,null),VideoPreviewActivity.this );
						}
						else
						{
							showSneakerWindow( Sneaker.with(VideoPreviewActivity.this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(VideoPreviewActivity.this),500)),com.irozon.sneaker.R.drawable.ic_error,R.string.file_not_found,R.color.white,R.color.red );
						}
					}
				}
			);
		}
	}

	@SneakyThrows
	public  void  onCompletion(   android.media.MediaPlayer  mediaPlayer )
	{
		this.player.getPlayer().seekTo(  0 );

        application().getMainLooperHandler().post( () -> {ObjectUtils.cast(super.findViewById(R.id.seek_bar),SeekBar.class).setProgress(0);  ObjectUtils.cast(super.findViewById(R.id.play_button),ImageView.class).setVisibility(View.VISIBLE);} );
	}

	public  void  onStartTrackingTouch( SeekBar  seekBar )
	{
		this.isTrackingTouch.compareAndSet( false, true );
	}

	public  void  onProgressChanged( SeekBar  seekBar  , int  progress , boolean  fromUser )
	{
		if( fromUser   )
		{
			this.player.getPlayer().seekTo(    progress );
		}
	}

	public  void  onStopTrackingTouch(  SeekBar  seekBar )
	{
		this.isTrackingTouch.compareAndSet( true, false );
	}

	public  void  onSeekComplete( android.media.MediaPlayer  mediaPlayer )
	{

	}
	@Override
	public  void  onError( Throwable  error )
	{
		showSneakerWindow( Sneaker.with(VideoPreviewActivity.this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(VideoPreviewActivity.this),500)),  com.irozon.sneaker.R.drawable.ic_error,R.string.network_or_internal_server_error,R.color.white,R.color.red );
	}
	@Override
	public  void  onProgress(long  contentLength,long  readBytesCount,boolean  isCompleted )
	{
		if( isCompleted)
		{
		videoDownloadProgressDialog.cancel();

			try
			{
				this.setPlayer(new  MediaPlayer().play(this.videoFile.getPath(),ObjectUtils.cast(super.findViewById(R.id.video_surface),SurfaceView.class).getHolder(),this,this,this) );
			}
			catch( IOException ioe )
			{
				super.showSneakerWindow( Sneaker.with(VideoPreviewActivity.this).setOnSneakerDismissListener(() -> application().getMainLooperHandler().postDelayed(() -> ContextUtils.finish(VideoPreviewActivity.this),500)),        com.irozon.sneaker.R.drawable.ic_error,R.string.io_exception,R.color.white,R.color.red );
			}

			return;
		}

		this.videoDownloadProgressDialog.getMessage().setText( new  BigDecimal(readBytesCount).divide(new  BigDecimal(contentLength),2,RoundingMode.HALF_UP).toEngineeringString()+"%" );
	}
}
