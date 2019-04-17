package cc.mashroom.hedgehog.module.common.activity;

import  android.os.Bundle;
import  android.view.SurfaceHolder;
import  android.view.SurfaceView;
import  android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import  android.widget.Toast;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.device.MediaPlayer;
import  cc.mashroom.hedgehog.webkit.DynamicService;
import  cc.mashroom.util.FileUtils;
import  cc.mashroom.util.ObjectUtils;

import  java.io.File;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import  cc.mashroom.hedgehog.util.ContextUtils;
import  es.dmoral.toasty.Toasty;
import  lombok.Setter;
import  lombok.SneakyThrows;
import  lombok.experimental.Accessors;
import  okhttp3.ResponseBody;
import  retrofit2.Call;
import  retrofit2.Callback;
import  retrofit2.Response;

public  class  VideoPreviewActivity  extends  AbstractActivity  implements  SurfaceHolder.Callback,android.media.MediaPlayer.OnPreparedListener,Runnable
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

		ObjectUtils.cast(super.findViewById(R.id.video_surface),SurfaceView.class).getHolder().addCallback( this );
	}

	private  ThreadPoolExecutor  progressUpdateThread = new ScheduledThreadPoolExecutor(1 );

	@Accessors( chain= true )
	@Setter
	private  File  videoFile;
	@Accessors( chain= true )
	@Setter
	private  MediaPlayer  player;

	public  void  run()
	{
		if( player.getPlayer().isPlaying()  )
		{
			application().getMainLooperHandler().post(   () -> ObjectUtils.cast(findViewById(R.id.seek_bar),SeekBar.class).setProgress(player.getPlayer().getCurrentPosition()) );
		}
	}

	public  void  surfaceChanged( SurfaceHolder  holder,int  format,int  width,int  height )
	{

	}

	@SneakyThrows
	public  void  surfaceDestroyed( SurfaceHolder  holder )
	{
		player.close();

		progressUpdateThread.remove(   this );
	}

	public  void  onPrepared(    android.media.MediaPlayer  mediaPlayer )
	{
		SurfaceView  surfaceView=ObjectUtils.cast( super.findViewById(R.id.video_surface) );

		float  maxScale = Math.max( (float)  mediaPlayer.getVideoWidth()/(float)  surfaceView.getWidth(),(float)  mediaPlayer.getVideoHeight()/(float)  surfaceView.getHeight() );

		ViewGroup.LayoutParams  layout   = surfaceView.getLayoutParams();

		layout.width  = (int)  Math.ceil( (float)  mediaPlayer.getVideoWidth() / maxScale );

		layout.height = (int)  Math.ceil( (float)  mediaPlayer.getVideoHeight()/ maxScale );

		surfaceView.setLayoutParams( layout );

		progressUpdateThread.execute(  this );
	}

	public  void  onDownloadError(  Throwable   throwable )
	{

	}

	@SneakyThrows
	public  void  surfaceCreated(   final  SurfaceHolder  surfaceHolder )
	{
		if( videoFile.exists() )
		{
			setPlayer(new  MediaPlayer().play(    videoFile.getPath(),surfaceHolder,this) );
		}
		else
		{
			application().getDefaultRetrofit().create(DynamicService.class).get(super.getIntent().getStringExtra("URL")).enqueue
			(
				new  Callback<ResponseBody>()
				{
					public  void  onFailure( Call<ResponseBody>  call,Throwable  throwable )
					{
						Toasty.error(VideoPreviewActivity.this,VideoPreviewActivity.this.getString(R.string.network_or_internal_server_error),Toast.LENGTH_LONG,false).show();
					}

					@SneakyThrows
					public  void  onResponse(Call<ResponseBody>  call,Response<ResponseBody>     retrofitResponse )
					{
						if( retrofitResponse.code()== 200 )
						{
							setPlayer(new  MediaPlayer().play( FileUtils.createFileIfAbsent(videoFile,retrofitResponse.body().bytes()).getPath(),surfaceHolder,VideoPreviewActivity.this) );
						}
						else
						{
							Toasty.error(VideoPreviewActivity.this,VideoPreviewActivity.this.getString(R.string.file_not_found),Toast.LENGTH_LONG,false).show();
						}
					}
				}
			);
		}
	}
}
