package cc.mashroom.hedgehog.okhttp.extend;

public  interface  ProgressCallback
{
	public  void  onProgress(String md5, long contentLength, long writeBytesCount);
}
