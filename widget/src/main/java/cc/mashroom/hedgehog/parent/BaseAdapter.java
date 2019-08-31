package cc.mashroom.hedgehog.parent;

import  java.util.List;

import  lombok.AccessLevel;
import  lombok.AllArgsConstructor;
import  lombok.NoArgsConstructor;
import  lombok.Setter;
import  lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
public  abstract  class  BaseAdapter<I>  extends  android.widget.BaseAdapter
{
	@Setter( value = AccessLevel.PROTECTED )
	@Accessors(chain=true )
	protected  List<I>  items;

	public  int  getCount()  { return  items.size(); }

	public  long  getItemId( int  position )  { return  position; }

	public  I  getItem( int  position )  { return  items.get(  position ); }
}
