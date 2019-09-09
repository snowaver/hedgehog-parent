package cc.mashroom.hedgehog.system;

import  java.util.LinkedList;
import  java.util.List;

import  com.google.common.collect.Ordering;

import  android.content.Context;
import  android.database.Cursor;
import  android.net.Uri;

import  cc.mashroom.util.StringUtils;
import  cc.mashroom.util.collection.map.HashMap;
import  cc.mashroom.util.collection.map.Map;

public  class  MediaStore
{
	private  final  static  Map<Uri,MediaType>  MEDIA_TYPES = new  HashMap<Uri,MediaType>().addEntry(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaType.IMAGE).addEntry( android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,MediaType.VIDEO );
	
	public  static  List<Media>  get( Context  context,   boolean  isLazyInitializeThumbnailInfo,Uri  ...  uris )
	{
		List<Media>  medias = new  LinkedList<Media>();
		
		for( Uri  uri : uris )
		{
			Map<Long,Media>  joiner = new  HashMap<Long,Media>();

			Map<Long,String> thumbnailPathJoiner = new  HashMap<Long, String>();

			MediaType  mediaType= MEDIA_TYPES.get(uri);

			try( Cursor  resultCursor = context.getContentResolver().query(uri,new  String[]{android.provider.MediaStore.MediaColumns._ID,android.provider.MediaStore.MediaColumns.MIME_TYPE,android.provider.MediaStore.MediaColumns.TITLE,android.provider.MediaStore.MediaColumns.WIDTH,android.provider.MediaStore.MediaColumns.HEIGHT,android.provider.MediaStore.MediaColumns.SIZE,android.provider.MediaStore.MediaColumns.DATA,android.provider.MediaStore.MediaColumns.DATE_ADDED,android.provider.MediaStore.MediaColumns.DATE_MODIFIED},null,null,null) )
			{
				if( resultCursor.moveToFirst() )
				{
					do
					{
						Media  media = new  Media( mediaType,resultCursor.getLong(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns._ID)),resultCursor.getString(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns.MIME_TYPE)),resultCursor.getString(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns.TITLE)),resultCursor.getInt(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns.WIDTH)),resultCursor.getInt(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns.HEIGHT)),resultCursor.getLong(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns.SIZE)),resultCursor.getString(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)),null,resultCursor.getLong(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DATE_ADDED))*1000,resultCursor.getLong(resultCursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DATE_MODIFIED))*1000 );

						if( !    isLazyInitializeThumbnailInfo  )
						{
							joiner.put( media.getId() ,  media );
						}

						medias.add(     media );
					}
					while( resultCursor.moveToNext() );
				}
			}

			if( !isLazyInitializeThumbnailInfo )
			{
				if( mediaType==MediaType.IMAGE )
				{
					try(Cursor  resultCursor = context.getContentResolver().query(android.provider.MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,new  String[]{android.provider.MediaStore.Images.Thumbnails.IMAGE_ID,android.provider.MediaStore.Images.Thumbnails.DATA},null,null,null) )
					{
						if( resultCursor.moveToFirst() )
						{
							do
							{
								thumbnailPathJoiner.put( resultCursor.getLong(resultCursor.getColumnIndex(android.provider.MediaStore.Images.Thumbnails.IMAGE_ID)),resultCursor.getString(resultCursor.getColumnIndex(android.provider.MediaStore.Images.Thumbnails.DATA)) );
							}
							while(  resultCursor.moveToNext() );
						}
					}
				}
				else
				if( mediaType==MediaType.VIDEO )
				{
					try(Cursor  resultCursor = context.getContentResolver().query(android.provider.MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI ,new  String[]{ android.provider.MediaStore.Video.Thumbnails.VIDEO_ID, android.provider.MediaStore.Video.Thumbnails.DATA},null,null,null) )
					{
						if( resultCursor.moveToFirst() )
						{
							do
							{
								thumbnailPathJoiner.put( resultCursor.getLong(resultCursor.getColumnIndex( android.provider.MediaStore.Video.Thumbnails.VIDEO_ID)),resultCursor.getString(resultCursor.getColumnIndex( android.provider.MediaStore.Video.Thumbnails.DATA)) );
							}
							while(  resultCursor.moveToNext() );
						}
					}
				}

				for(java.util.Map.Entry<Long,Media>  entry : joiner.entrySet() )
				{
					String  thumbnailPath = thumbnailPathJoiner.get( entry.getKey() );

					entry.getValue().setThumbnailPath( StringUtils.isBlank(thumbnailPath) ? "" : thumbnailPath );  //  cached  nothing,  but  use  empty  string  to  against  inavialble  requeries.
				}
			}
		}
		
		return  Ordering.from((Media  current,Media  next) -> Long.valueOf(next.getModifyDate()).compareTo(Long.valueOf(current.getModifyDate()))).sortedCopy( medias );
	}
}
