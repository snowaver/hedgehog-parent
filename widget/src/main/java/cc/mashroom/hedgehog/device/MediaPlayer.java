package cc.mashroom.hedgehog.device;

import  java.io.IOException;

import  android.view.SurfaceHolder;

public  class  MediaPlayer  implements  AutoCloseable
{
	public  void  close()   throws  Exception
	{
		player.stop( );

		player.release();
	}

	private  android.media.MediaPlayer  player = new  android.media.MediaPlayer();

	public  MediaPlayer  play( String  path,SurfaceHolder  surfaceHolder,android.media.MediaPlayer.OnPreparedListener  preparedListener )  throws  IllegalArgumentException,SecurityException,IllegalStateException,IOException
	{
		player.setOnErrorListener( (player,what,extra) -> false );

		if( preparedListener!= null )
		{
			player.setOnPreparedListener( preparedListener );
		}

		if( surfaceHolder   != null )
		{
			player.setDisplay(surfaceHolder);
		}
		
		player.setDataSource( path );
		
		player.prepare();
		
		player.start();
		
		return  this;
	}
}
