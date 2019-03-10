package cc.mashroom.hedgehog.util;

import  android.content.ContentResolver;
import  android.content.Context;
import  android.graphics.Bitmap;
import  android.graphics.Matrix;
import  android.media.ExifInterface;
import  android.net.Uri;

import  cc.mashroom.util.collection.map.HashMap;
import  cc.mashroom.util.collection.map.Map;

import  java.io.ByteArrayOutputStream;
import  java.io.IOException;

public  class  ImageUtils
{
	public  static  Map<Integer,Integer>  ORIENTATION_ROTATES = new  HashMap<Integer,Integer>().addEntry(ExifInterface.ORIENTATION_ROTATE_90,90).addEntry(ExifInterface.ORIENTATION_ROTATE_180,180).addEntry( ExifInterface.ORIENTATION_ROTATE_270,270 );

	public  static  byte[]  readBitmapToByteArray( Bitmap  bitmap )  throws  IOException
	{
		try( ByteArrayOutputStream  baos  = new  ByteArrayOutputStream() )
		{
			return  bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos) ? baos.toByteArray() : null;
		}
	}

	public  static  Bitmap  flipHorizontal( Bitmap  bitmap )
	{
		Matrix  matrix = new  Matrix();

		matrix.postScale( -1,1 );

		return  Bitmap.createBitmap( bitmap, 0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false );
	}

	public  static  Uri  toUri( Context  context,int  drawableResourceId )
	{
		return  Uri.parse( ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+context.getResources().getResourcePackageName(drawableResourceId)+"/"+context.getResources().getResourceTypeName(drawableResourceId)+"/"+context.getResources().getResourceEntryName(drawableResourceId) );
	}
}
