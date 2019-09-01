package cc.mashroom.hedgehog.parent;

import  android.content.Context;
import  android.graphics.Bitmap;
import  android.graphics.BitmapFactory;
import  android.media.ThumbnailUtils;
import  android.os.Handler;
import  android.os.Looper;
import  android.provider.MediaStore;

import  org.apache.commons.codec.binary.Hex;

import  java.io.File;
import  java.io.IOException;

import  cc.mashroom.hedgehog.util.ImageUtils;
import  cc.mashroom.util.DigestUtils;
import  cc.mashroom.util.FileUtils;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;
import  retrofit2.Retrofit;

public  class  Application   extends  android.app.Application
{
	public  void  onCreate()
	{
		super.onCreate();

		this.setCacheDir( super.getDir(".hedgehog",Context.MODE_PRIVATE) ).setFileDownloadRetrofit( new  Retrofit.Builder().baseUrl("https://mashroom.cc/").build() );
	}

	@Accessors( chain=true )
	@Setter
	@Getter
	private  File  cacheDir;
	@Accessors( chain=true )
	@Setter
	@Getter
	private  Retrofit fileDownloadRetrofit;
	@Getter
	private  Handler  mainLooperHandler = new  Handler( Looper.getMainLooper() );
	/**
	 * @see  #cache(  int  , byte[] , int )
	 */
	public  File  cache( int  imageId,       File  cachingFile,int  contentType )  throws  IOException
	{
		return  cache( imageId,FileUtils.readFileToByteArray(cachingFile),contentType );
	}
	/**
	 *  cache  the  file  named  by  file  md5  and  return  the  cached  file,  then  cache  the  thumbnail  (path:  cache  file  path  +  $TMB)  if  the  content  type  is  image  or  video.  content  types:  2  (image),  3  (audio)  and  4  (video).
	 */
	public  File  cache( int  imageId,byte[]  cachingFileBytes,int  contentType )  throws  IOException
	{
		Bitmap  bmp  = null;

		File  cachedFile = FileUtils.createFileIfAbsent( new  File(cacheDir,"file/"+new  String(Hex.encodeHex(DigestUtils.md5(cachingFileBytes))).toUpperCase()),cachingFileBytes );

		if( contentType==4 )
		{
            bmp = ThumbnailUtils.createVideoThumbnail( cachedFile.getPath(),MediaStore.Video.Thumbnails.MINI_KIND );
		}
		else
		if( contentType==2 )
		{
            bmp = imageId != -1 ? MediaStore.Images.Thumbnails.getThumbnail(super.getContentResolver(),imageId,MediaStore.Images.Thumbnails.MINI_KIND,null) : BitmapFactory.decodeByteArray( cachingFileBytes,0,cachingFileBytes.length );
		}
		else
		if( contentType==3 )
		{
			return  cachedFile;
		}
		else
		{
			throw  new  IllegalArgumentException( String.format("HEDGEHOT-PARENT:  ** APPLICATION **  the  content  type  ( %d )  is  not  supported.",contentType) );
		}

		FileUtils.createFileIfAbsent( new  File(cacheDir,"file/"+cachedFile.getName()+"$TMB").getPath(),ImageUtils.readBitmapToByteArray(bmp  /*ThumbnailUtils.extractThumbnail(bitmap,(int)  (((double)  bitmap.getWidth()/bitmap.getHeight())*DensityUtils.px(this,90)),DensityUtils.px(this,90))*/) );  return  cachedFile;
	}
}
