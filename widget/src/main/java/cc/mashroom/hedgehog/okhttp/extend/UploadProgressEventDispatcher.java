package cc.mashroom.hedgehog.okhttp.extend;

import  cc.mashroom.util.collection.map.Map;
import cc.mashroom.util.stream.Stream;

import  java.util.List;
import  java.util.concurrent.CopyOnWriteArrayList;

public  class  UploadProgressEventDispatcher
{
	protected  static  List<UploadProgressListener>  listeners = new  CopyOnWriteArrayList<UploadProgressListener>();

	public  static  void  addListener(    UploadProgressListener  listener )
	{
		if( listener != null )
		{
			listeners.add(    listener );
		}
	}

	public  static  void  removeListener( UploadProgressListener  listener )
	{
		if( listener != null )
		{
			listeners.remove( listener );
		}
	}

	public  static  void  progress(  Map<String,Integer>  uploadProgresses )
	{
		Stream.forEach( listeners,(listener) -> listener.progress(uploadProgresses) );
	}
}
