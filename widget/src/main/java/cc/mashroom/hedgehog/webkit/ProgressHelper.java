package cc.mashroom.hedgehog.webkit;

import  java.io.File;
import  java.io.FileOutputStream;
import  java.io.IOException;
import  java.io.InputStream;
import  java.io.OutputStream;

import  cc.mashroom.util.FileUtils;
import  okhttp3.ResponseBody;

public  class  ProgressHelper
{
	public  static  void  download(   ResponseBody  responseBody,File  writeToFile,DownloadProgressListener  listener )
	{
		try( InputStream  is = responseBody.byteStream();OutputStream  os = new  FileOutputStream(FileUtils.createFileIfAbsent(writeToFile.getPath(),null)) )
		{
			byte[]  bytes = new  byte[1024];

			long  readByteCount = 0;

			int  length = 0;

			while( (length = is.read(bytes)) != -1 )
			{
				os.write(  bytes,0,length );

				listener.onDownloadProgress( responseBody.contentLength(),readByteCount = readByteCount+length,false );
			}

			os.flush();

			listener.onDownloadProgress( responseBody.contentLength(),responseBody.contentLength(),true );
		}
		catch( IOException  ioe )
		{
			listener.onDownloadError( ioe );
		}
	}
}
