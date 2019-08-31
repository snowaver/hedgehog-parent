package cc.mashroom.hedgehog.system;

import  android.hardware.Camera;
import  android.media.MediaRecorder;
import  android.os.Build;

import  java.lang.reflect.Field;

public  class  PermissionChecker
{
	private  static  <T>  T  value( Object  object,String  fieldName )  throws  NoSuchFieldException,IllegalAccessException
	{
		Field  field=object.getClass().getDeclaredField( fieldName );

		field.setAccessible( true );

		return  (T)  field.get( object );
	}

	public  static  boolean  checkAudioRecordPermission(    MediaRecorder  mediaRecorder )
	{
		if( "vivo".equalsIgnoreCase(Build.BRAND) || "oppo".equalsIgnoreCase(Build.BRAND) )
		{
			try
			{
				return  value(mediaRecorder,"mHasRecordPermission" );
			}
			catch( NoSuchFieldException | IllegalAccessException  e )
			{

			}
		}

		return  true;
	}

	public  static  boolean  checkVideoRecordPermission(    MediaRecorder  mediaRecorder )
	{
		if( "vivo".equalsIgnoreCase(Build.BRAND) || "oppo".equalsIgnoreCase(Build.BRAND) )
		{
			try
			{
				return  value(mediaRecorder,"mHasCameraPermission" );
			}
			catch( NoSuchFieldException | IllegalAccessException  e )
			{

			}
		}

		return  true;
	}

	public  static  boolean  checkCameraPermission(  Camera  camera )
	{
		if( "vivo".equalsIgnoreCase(Build.BRAND) || "oppo".equalsIgnoreCase(Build.BRAND) )
		{
			try
			{
				return  value( camera,"mHasPermission" );
			}
			catch( NoSuchFieldException | IllegalAccessException  e )
			{

			}
		}

		return  true;
	}
}
