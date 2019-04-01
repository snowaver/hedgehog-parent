package cc.mashroom.hedgehog.parent;

import  android.content.Context;
import  android.view.LayoutInflater;
import  android.view.View;
import  android.view.ViewGroup;

import  java.util.List;

import  cc.mashroom.util.ObjectUtils;
import  lombok.AccessLevel;
import  lombok.AllArgsConstructor;
import  lombok.NoArgsConstructor;
import  lombok.Setter;
import  lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
public  abstract  class  BaseMulticolumnAdapter<I>  extends  android.widget.BaseAdapter
{
	@Setter( value = AccessLevel.PROTECTED )
	@Accessors(chain=true )
	protected  Context context;
	@Setter( value = AccessLevel.PROTECTED )
	@Accessors(chain=true )
	protected  List<I>   items;
	@Setter( value = AccessLevel.PROTECTED )
	@Accessors(chain=true )
	protected  int  columnSize;
	@Setter( value = AccessLevel.PROTECTED )
	@Accessors(chain=true )
	protected  int  parentLayoutId;

	public  long  getItemId( int  position )
	{
		return    position;
	}

	public  List<I> getItem( int  position )
	{
		return  this.items.subList(  position*3 , (position+1)*3 > items.size()-1 ? items.size()-1 : (position+1)*3 );
	}

	public  int  getCount()
	{
		return  items.size()%columnSize== 0 ? items.size()/columnSize : items.size()/columnSize+1;
	}

	public  View  getView(      int  position , View  convertView , ViewGroup  parent )
	{
		if( convertView == null )  convertView = LayoutInflater.from(context).inflate( parentLayoutId,parent, false );

		if( ObjectUtils.cast(convertView,ViewGroup.class).getChildCount()     != this.columnSize )
		{
			throw  new  IllegalArgumentException( "MASHROOM-WIDGET:  ** BASE  MULTI-COLUMN  ADAPTER **  row  child  view  count  should  be  equal  to  column  size." );
		}

		List<I>  items= getItem( position );

		for( int  i = 0;i <= ObjectUtils.cast(convertView,ViewGroup.class).getChildCount()-1;i++ )
		{
			ObjectUtils.cast(convertView,ViewGroup.class).getChildAt(i).setVisibility( i >= items.size() ? View.INVISIBLE : View.VISIBLE );

			if( i <= items.size()-1 )
			{
				getChildView( position,i,items.get(i),ObjectUtils.cast(convertView,ViewGroup.class).getChildAt( i ) );
			}
		}

		return  convertView;
	}

	public  abstract  void  getChildView( int  rowIndex,int  columnIndex,I  i,View  convertView );
}
