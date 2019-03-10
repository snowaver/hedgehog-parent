package cc.mashroom.hedgehog.device;

import  android.hardware.Camera;
import  android.media.CamcorderProfile;

import  java.io.File;
import  java.io.IOException;
import  java.util.concurrent.atomic.AtomicBoolean;

import  cc.mashroom.hedgehog.system.PermissionChecker;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  MediaRecorder  implements  AutoCloseable
{
	public  android.media.MediaRecorder wrapped()
	{
		return    recorder;
	}

	private  android.media.MediaRecorder  recorder = new  android.media.MediaRecorder();
	@Accessors(chain=true )
	@Setter
    @Getter
	private  Camera camera;
	private  AtomicBoolean  isCameraUnlocked  = new  AtomicBoolean();

    public  void    close()
    {
        this.recorder.setPreviewDisplay(  null );

        recorder.stop();

        recorder.reset(  );

        recorder.release();

        if( this.camera != null && this.isCameraUnlocked.compareAndSet(true,false) )  this.camera.lock();
    }

	public  MediaRecorder  prepare( int  audioSource,int  audioEncoder,int  outputFormat,File  outputFile )  throws  IllegalStateException,IOException
	{
		recorder.setOnErrorListener( (android.media.MediaRecorder  mediaRecorder,int  what,int  extra) -> recorder.release() );

		recorder.setAudioSource(   audioSource );

		if( !PermissionChecker.checkAudioRecordPermission(recorder) )
		{
			throw  new  IllegalStateException( "SQUIRREL-WIDGET:  ** MEDIA  RECORDER **  audio  record  permission  is  required,  but  it  is  not  granted." );
		}

		recorder.setOutputFormat( outputFormat );

		recorder.setAudioEncoder( audioEncoder );

		recorder.setOutputFile( outputFile.getPath() );

		recorder.prepare();

		return  this;
	}

    public  MediaRecorder  prepare( Camera  camera,int  audioSource,int  videoSource,CamcorderProfile  profile,File  outputFile,int  orientiatonHint )  throws  IllegalStateException,IOException
    {
        recorder.setOnErrorListener( (android.media.MediaRecorder  mediaRecorder,int  what,int  extra) -> recorder.release() );

        setCamera( camera ).getCamera().unlock();

        isCameraUnlocked.compareAndSet( false , true );

        recorder.setCamera(   camera );

        recorder.setAudioSource(   audioSource );

        if( !PermissionChecker.checkAudioRecordPermission(recorder) )
        {
            throw  new  IllegalStateException( "SQUIRREL-WIDGET:  ** MEDIA  RECORDER **  audio  record  permission  is  required,  but  it  is  not  granted." );
        }

        recorder.setVideoSource(   videoSource );

        if( !PermissionChecker.checkVideoRecordPermission(recorder) )
        {
            throw  new  IllegalStateException( "SQUIRREL-WIDGET:  ** MEDIA  RECORDER **  video  record  permission  is  required,  but  it  is  not  granted." );
        }

        recorder.setProfile( profile );

        recorder.setOrientationHint( orientiatonHint );

        recorder.setOutputFile( outputFile.getPath() );

        recorder.prepare();

        return  this;
    }

	public  MediaRecorder  prepare( int  audioSource,int  audioEncoder,int  videoSource,int  videoEncoder,int  outputFormat,File  outputFile,int  orientationHint,android.util.Size  size )  throws  IllegalStateException,IOException
	{
		recorder.setOnErrorListener( (android.media.MediaRecorder  mediaRecorder,int  what,int  extra) -> recorder.release() );

		recorder.setAudioSource(   audioSource );

		if( !PermissionChecker.checkAudioRecordPermission(recorder) )
		{
			throw  new  IllegalStateException( "SQUIRREL-WIDGET:  ** MEDIA  RECORDER **  audio  record  permission  is  required,  but  it  is  not  granted." );
		}

		recorder.setVideoSource(   videoSource );

		if( !PermissionChecker.checkVideoRecordPermission(recorder) )
		{
			throw  new  IllegalStateException( "SQUIRREL-WIDGET:  ** MEDIA  RECORDER **  video  record  permission  is  required,  but  it  is  not  granted." );
		}

		recorder.setOutputFormat( outputFormat );
		
		recorder.setOutputFile( outputFile.getPath() );
		
		recorder.setVideoEncodingBitRate(5*1000*1000 );
		
		recorder.setVideoFrameRate(30);
		
		recorder.setVideoSize( size.getWidth()  , size.getHeight() );
		
		recorder.setVideoEncoder( videoEncoder );
		
		recorder.setAudioEncoder( audioEncoder );
		
		recorder.setOrientationHint( orientationHint );

		recorder.prepare();

		return  this;
	}
}
