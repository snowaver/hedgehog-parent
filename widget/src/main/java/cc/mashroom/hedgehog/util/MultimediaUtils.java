package cc.mashroom.hedgehog.util;

import  android.app.Activity;
import  android.content.Context;
import  android.media.AudioManager;
import  android.media.MediaMetadataRetriever;
import  android.media.MediaPlayer;
import  android.view.Surface;

import  java.io.File;
import  java.io.IOException;

import  cc.mashroom.util.ObjectUtils;

public  class   MultimediaUtils
{
    public  static  void  setupCellphoneMode( Activity  context )
    {
        AudioManager  audioManager = ObjectUtils.cast( context.getSystemService(Context.AUDIO_SERVICE) );

        audioManager.setSpeakerphoneOn( audioManager.isWiredHeadsetOn() );

        audioManager.setMode( AudioManager.MODE_IN_CALL );
    }

    public  static  long  getDuration(   File  mediaFile )
    {
        MediaMetadataRetriever  metadata  = new  MediaMetadataRetriever();

        try
        {
            metadata.setDataSource( mediaFile.getPath() );

            return  new  Double(metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)).longValue();
        }
        finally
        {
            metadata.release();
        }
    }

    public  static  void  play( String  path,Surface  surface )  throws  IOException,IllegalArgumentException,SecurityException,IllegalStateException
    {
        MediaPlayer  player = new  MediaPlayer();

        player.setSurface( surface );

        player.setDataSource( path );

        player.prepare();
    }
}