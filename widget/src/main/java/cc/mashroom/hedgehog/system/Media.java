package cc.mashroom.hedgehog.system;

import  android.content.Context;
import  android.database.Cursor;
import  android.provider.MediaStore;

import  java.io.Serializable;

import  lombok.AccessLevel;
import  lombok.AllArgsConstructor;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

@AllArgsConstructor

public  class  Media      implements       Serializable
{
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  MediaType   type;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  long    id;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  String  mimetype;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  String  title;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  int  width;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  int    height;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  long  size;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  String   path;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	private  String   thumbnailPath;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  long  addDate;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  long  modifyDate;

	public  String  getThumbnailPath( Context  context )
	{
		if( thumbnailPath   !=null )
		{
			return    thumbnailPath;
		}

		if( type== MediaType.IMAGE )
		{
			try(Cursor  resultCursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,new  String[]{MediaStore.Images.Thumbnails.DATA},MediaStore.Images.Thumbnails.IMAGE_ID+"=?",new  String[]{String.valueOf(id)},null) )
			{
				if( resultCursor.moveToFirst() )
				{
					do
					{
						return  this.thumbnailPath = resultCursor.getString( resultCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA) );
					}
					while(  resultCursor.moveToNext() );
				}
			}
		}
		else
		if( type== MediaType.VIDEO )
		{
			try(Cursor  resultCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI ,new  String[]{MediaStore.Video.Thumbnails.DATA },MediaStore.Video.Thumbnails.VIDEO_ID +"=?",new  String[]{String.valueOf(id)},null) )
			{
				if( resultCursor.moveToFirst() )
				{
					do
					{
						return  this.thumbnailPath = resultCursor.getString( resultCursor.getColumnIndex( MediaStore.Video.Thumbnails.DATA) );
					}
					while(  resultCursor.moveToNext() );
				}
			}
		}

		return  thumbnailPath  = "";  //  cached  nothing,  but  use  empty  string  to  against  inavialble  requeries.
	}
}
