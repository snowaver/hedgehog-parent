package cc.mashroom.hedgehog.system;

import  lombok.AllArgsConstructor;
import  lombok.Getter;

@AllArgsConstructor

public  enum   MediaType
{
	IMAGE(0),VIDEO(1);
	@Getter
	private  int  value;

	public  static  MediaType  valueOf( int  value )
	{
		for( MediaType  packetType : MediaType.values() )
		{
			if( value == packetType.getValue() )
			{
				return  packetType;
			}
		}
		
		throw  new  IllegalArgumentException( String.format("MASHROOM-WIDGET:  ** MEDIA  TYPE **  no  media  type  defined  for  %d",value) );
	}
}
