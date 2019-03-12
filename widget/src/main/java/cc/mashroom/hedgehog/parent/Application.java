package cc.mashroom.hedgehog.parent;

import  android.content.Context;
import  android.graphics.Bitmap;
import  android.graphics.BitmapFactory;
import  android.media.ThumbnailUtils;
import  android.os.Handler;
import  android.os.Looper;
import  android.provider.MediaStore;

import  java.io.File;

import  cc.mashroom.hedgehog.util.ImageUtils;
import  cc.mashroom.util.DigestUtils;
import  cc.mashroom.util.FileUtils;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.SneakyThrows;
import  retrofit2.Retrofit;

public  class  Application   extends  android.app.Application
{
	public  void  onCreate()
	{
		super.onCreate();

		this.setCacheDir( FileUtils.createDirectoryIfAbsent( super.getDir(    ".hedgehog" , Context.MODE_PRIVATE ) ) );
	}

	@Setter
	@Getter
	private  File  cacheDir;
	@Getter
	private  Handler  mainLooperHandler = new  Handler( Looper.getMainLooper() );
	@Getter
	private  Retrofit  defaultRetrofit = new  Retrofit.Builder().baseUrl("https://mashroom.cc//").build();

	@SneakyThrows
	public  File  cache( int  imageId,File  cachingFile,int  contentType )
	{
		return  cache( imageId,FileUtils.readFileToByteArray(cachingFile),contentType );
	}

	@SneakyThrows
	public  File  cache( int  imageId,byte[]  cachingFileBytes,int  contentType )
	{
		Bitmap  bitmap=null;

		File  cachedFile = FileUtils.createFileIfAbsent( new  File(cacheDir,"file/"+DigestUtils.md5Hex(cachingFileBytes).toUpperCase()),cachingFileBytes );

		if( contentType==3 )
		{
			bitmap = ThumbnailUtils.createVideoThumbnail( cachedFile.getPath(),MediaStore.Video.Thumbnails.MINI_KIND );
		}
		else
		if( contentType==1 )
		{
			bitmap = imageId != -1 ? MediaStore.Images.Thumbnails.getThumbnail(super.getContentResolver(),imageId,MediaStore.Images.Thumbnails.MINI_KIND,null) : BitmapFactory.decodeByteArray( cachingFileBytes,0,cachingFileBytes.length );
		}
		else
		if( contentType==2 )
		{
			return  cachedFile;
		}
		else
		{
			throw  new  IllegalArgumentException( String.format("SQUIRREL-CLIENT:  ** APPLICATION **  content  type  ( %d )  is  not  supported  for  caching.",contentType) );
		}

		FileUtils.createFileIfAbsent( new  File(cacheDir,"file/"+cachedFile.getName()+"$TMB").getPath(),ImageUtils.readBitmapToByteArray(bitmap  /*ThumbnailUtils.extractThumbnail(bitmap,(int)  (((double)  bitmap.getWidth()/bitmap.getHeight())*DensityUtils.px(this,90)),DensityUtils.px(this,90))*/) );  return  cachedFile;
	}
}
