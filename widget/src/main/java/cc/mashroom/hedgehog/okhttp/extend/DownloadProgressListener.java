package cc.mashroom.hedgehog.okhttp.extend;

public  interface  DownloadProgressListener
{
	public  void  onProgress( long  contentLength,long  readBytesCount,boolean  isCompleted );
}
