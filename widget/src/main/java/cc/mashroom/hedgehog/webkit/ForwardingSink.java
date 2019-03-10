package cc.mashroom.hedgehog.webkit;

import  java.io.IOException;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import  okio.Buffer;
import  okio.BufferedSink;

public  class  ForwardingSink<T>  extends  okio.ForwardingSink
{
	public  ForwardingSink( BufferedSink  bufferedSink,long  contentLength,String  md5,ProgressCallback  callback )
	{
		super( bufferedSink );

		this.setMd5(md5).setCallback(callback).setContentLength( contentLength );
	}

	@Setter( value=AccessLevel.PRIVATE )
	@Accessors( chain=true )
	private  long  contentLength;
	@Setter( value=AccessLevel.PRIVATE )
	@Accessors( chain=true )
	private  String  md5;
	@Setter( value=AccessLevel.PRIVATE )
	@Accessors( chain=true )
	private  long  writeByteCount   = 0;
	@Setter( value=AccessLevel.PRIVATE )
	@Accessors( chain=true )
	private  ProgressCallback  callback;

	public  void  write(    Buffer  buffer,long  byteCount )  throws  IOException
	{
		super.write( buffer,byteCount );

		callback.onProgress( md5,contentLength,writeByteCount = writeByteCount+byteCount );
	}
}
