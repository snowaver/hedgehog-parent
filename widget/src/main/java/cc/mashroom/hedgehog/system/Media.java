package cc.mashroom.hedgehog.system;

import  java.io.Serializable;

import  lombok.AccessLevel;
import  lombok.AllArgsConstructor;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

@AllArgsConstructor

public  class  Media  implements  Serializable
{
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  MediaType   type;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  int  id;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  String  path;
	@Setter( AccessLevel.PROTECTED )
	@Accessors( chain = true )
	@Getter
	private  long  modifyDate;
}
