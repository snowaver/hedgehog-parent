package cc.mashroom.hedgehog.webkit;

public  interface  DownloadProgressListener
{
	public  void  onDownloadProgress(long contentLength, long readBytesCount, boolean completed);
	
	public  void  onDownloadError(Throwable cause);
}
