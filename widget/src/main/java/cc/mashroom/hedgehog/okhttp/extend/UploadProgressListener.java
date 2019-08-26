package cc.mashroom.hedgehog.okhttp.extend;

import  cc.mashroom.util.collection.map.Map;

public  interface  UploadProgressListener
{
	public  void  progress(Map<String, Integer> uploadProgresses);
}
