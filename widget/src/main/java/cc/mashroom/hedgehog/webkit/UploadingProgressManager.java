package cc.mashroom.hedgehog.webkit;

import  cc.mashroom.util.collection.map.HashMap;
import  cc.mashroom.util.collection.map.Map;
import  lombok.NoArgsConstructor;

@NoArgsConstructor

public  class  UploadingProgressManager  implements  ProgressCallback
{
	public  Integer  getUploadingProgress( String  md5 )
	{
		return  uploadingProgresses.get(    md5 );
	}
	
	public  static  UploadingProgressManager  INSTANCE = new  UploadingProgressManager();

	public  void  onProgress( String  md5,long  contentLength,long  writeBytesCount )
	{
		Integer  uploadingProgress  = uploadingProgresses.get( md5 );

		int  progress = (int)  ( ((double)  writeBytesCount/contentLength)*100 );

		if( uploadingProgress == null  || uploadingProgress.intValue()  != progress )
		{
			UploadProgressEventDispatcher.progress( uploadingProgresses.addEntry(md5,progress) );
			
			if( progress == 100 )
			{
				uploadingProgresses.remove( md5 );
			}
		}
	}

	private  Map<String,Integer>  uploadingProgresses= new HashMap<String,Integer>();
}