package cc.mashroom.hedgehog.device;

import  java.io.Closeable;
import  java.io.IOException;

import  android.view.SurfaceHolder;

import  lombok.Getter;
import  lombok.NonNull;

public  class  MediaPlayer  implements  Closeable
{
	public  MediaPlayer  pause()
	{
		player.pause();  return this;
	}

	public  void  close()
	{
		player.stop( );  player.release();
	}

	public  MediaPlayer  start()
	{
		player.pause();  return this;
	}

	@Getter
	private  android.media.MediaPlayer  player = new  android.media.MediaPlayer();

	public  MediaPlayer  play( @NonNull  String  path,SurfaceHolder  surfaceHolder,android.media.MediaPlayer.OnPreparedListener  preparedListener )  throws  IllegalArgumentException,SecurityException,IllegalStateException,IOException
	{
		return  play(path,surfaceHolder,preparedListener,null,null );
	}

	public  MediaPlayer  play( @NonNull  String  path,SurfaceHolder  surfaceHolder,android.media.MediaPlayer.OnPreparedListener  preparedListener,android.media.MediaPlayer.OnCompletionListener  completionListener,android.media.MediaPlayer.OnSeekCompleteListener  seekCompleteListener )  throws  IllegalArgumentException,SecurityException,IllegalStateException,IOException
	{
		player.setOnErrorListener( (player, what , extra) -> false );

		if( preparedListener!= null )
		{
			player.setOnPreparedListener( preparedListener );
		}

		if( completionListener   != null )
		{
			player.setOnCompletionListener(     completionListener );
		}

		if( surfaceHolder   != null )
		{
			player.setDisplay(   surfaceHolder );
		}

		if( seekCompleteListener != null )
		{
			player.setOnSeekCompleteListener( seekCompleteListener );
		}
		
		player.setDataSource( path );  player.prepareAsync();

		player.start();

		return    this;
	}
}
