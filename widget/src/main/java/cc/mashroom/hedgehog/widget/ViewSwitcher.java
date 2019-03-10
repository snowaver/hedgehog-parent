package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.util.AttributeSet;
import  android.view.View;
import  android.widget.RelativeLayout;

import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  ViewSwitcher  extends  RelativeLayout
{
	public  ViewSwitcher( Context  context,AttributeSet  attributes )
	{
		super( context, attributes );
	}

	@Accessors( chain=true )
	@Getter
	@Setter
	protected  int  displayedChildPosition;

	public  ViewSwitcher  setDisplayedChild( int  positon )
	{
		for( int  i = 0;i <= getChildCount() - 1;i = i+ 1 )
		{
			super.getChildAt(i == positon ? setDisplayedChildPosition(positon).getDisplayedChildPosition() : i).setVisibility( i == positon ? View.VISIBLE : View.GONE );
		}

		return  this;
	}

	public  View  getDisplayedChild()
	{
		return  super.getChildAt( displayedChildPosition );
	}
}
