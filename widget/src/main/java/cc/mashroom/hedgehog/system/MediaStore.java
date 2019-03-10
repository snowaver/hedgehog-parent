package cc.mashroom.hedgehog.system;

import  java.util.LinkedList;
import  java.util.List;

import  com.google.common.collect.Ordering;

import  android.content.Context;
import  android.database.Cursor;
import  android.net.Uri;
import  cc.mashroom.util.collection.map.HashMap;
import  cc.mashroom.util.collection.map.Map;

public  class  MediaStore
{
	private  final  static  Map<Uri,MediaType>  MEDIA_TYPES = new  HashMap<Uri,MediaType>().addEntry(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaType.IMAGE).addEntry( android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,MediaType.VIDEO );
	
	public  static  List<Media>  get( Context  context,Uri...  uris )
	{
		List<Media>  medias = new  LinkedList<Media>();
		
		for( Uri  uri : uris )
		{
			try( Cursor  resultCursor = context.getContentResolver().query(uri,null,null,null,null) )
			{
				if( resultCursor.moveToFirst() )
				{
					do
					{
						medias.add( new  Media(MEDIA_TYPES.get(uri),resultCursor.getInt(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns._ID)),resultCursor.getString(resultCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATA)),Long.parseLong(resultCursor.getString(resultCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_MODIFIED)))*1000) );
					}
					while( resultCursor.moveToNext() );
				}
			}
		}
		
		return  Ordering.from((Media  current,Media  next) -> Long.valueOf(next.getModifyDate()).compareTo(Long.valueOf(current.getModifyDate()))).sortedCopy( medias );
	}
}
