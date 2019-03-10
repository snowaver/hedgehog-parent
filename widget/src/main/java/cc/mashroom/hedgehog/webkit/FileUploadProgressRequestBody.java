package cc.mashroom.hedgehog.webkit;

import  java.io.IOException;

import  lombok.AccessLevel;
import  lombok.Setter;
import  lombok.experimental.Accessors;
import  okhttp3.MediaType;
import  okhttp3.RequestBody;
import  okio.BufferedSink;
import  okio.Okio;

public  class  FileUploadProgressRequestBody    extends  RequestBody
{
	public  FileUploadProgressRequestBody( String  md5,RequestBody  requestBody )
	{
		super();

		this.setMd5(md5).setRequestBody( requestBody );
	}

	@Setter( value=AccessLevel.PRIVATE )
	@Accessors( chain=true )
	protected  String   md5;
	@Setter( value=AccessLevel.PRIVATE )
	@Accessors( chain=true )
	protected  BufferedSink    bufferedSink;
	@Setter( value=AccessLevel.PRIVATE )
	@Accessors( chain=true )
	protected  RequestBody  requestBody;

	public  long  contentLength()   throws  IOException
	{
		return  requestBody.contentLength();
	}

	public  MediaType  contentType()
	{
		return    requestBody.contentType();
	}

	public  void  writeTo( BufferedSink  sink )  throws  IOException
	{
		requestBody.writeTo( bufferedSink != null ? bufferedSink : (bufferedSink = Okio.buffer(new  ForwardingSink(sink,contentLength(),md5,UploadingProgressManager.INSTANCE))) );  bufferedSink.flush();
	}
}