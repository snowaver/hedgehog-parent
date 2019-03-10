package cc.mashroom.hedgehog.webkit;

public  interface  ProgressCallback
{
	public  void  onProgress(String md5, long contentLength, long writeBytesCount);
}
