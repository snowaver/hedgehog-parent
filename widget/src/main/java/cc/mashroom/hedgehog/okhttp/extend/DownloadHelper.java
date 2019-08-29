package cc.mashroom.hedgehog.okhttp.extend;

import  java.io.File;
import  java.io.FileOutputStream;
import  java.io.IOException;
import  java.io.InputStream;
import  java.io.OutputStream;

import  cc.mashroom.util.FileUtils;
import  okhttp3.ResponseBody;

public  class  DownloadHelper
{
	private static  File  write( ResponseBody  responseBody,File  writeToFile,DownloadProgressListener  downloadProgressListener )     throws  IOException
	{
		try( InputStream  is       = responseBody.byteStream();   OutputStream  os = new  FileOutputStream(FileUtils.createFileIfAbsent(writeToFile.getPath(),null)) )
		{
			long  contentLength = responseBody.contentLength();

			byte[]  cache = new  byte[ 256 ];

			long  readByteCount = 0;

			int   length = 0;

			while( (length  = is.read(cache)) != -1 )
			{
				os.write( cache,  0,length );  downloadProgressListener.onProgress( contentLength,readByteCount=readByteCount+length,false );
			}

			os.flush();

			downloadProgressListener.onProgress( contentLength,readByteCount,true );  return  writeToFile;
		}
	}

	public  static  void  writeInNewThread( ResponseBody  responseBody,File  writeToFile,DownloadProgressListener  downloadProgressListener )
	{
		new  Thread(() -> { try{ write(responseBody,writeToFile,downloadProgressListener); }catch( IOException  e ){ downloadProgressListener.onError(e); }}).start();
	}
}
