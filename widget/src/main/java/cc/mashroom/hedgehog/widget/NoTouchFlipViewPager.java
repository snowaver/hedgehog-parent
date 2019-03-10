package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import androidx.viewpager.widget.ViewPager;
import  android.util.AttributeSet;
import  android.view.MotionEvent;

public  class  NoTouchFlipViewPager  extends  ViewPager
{
	public  NoTouchFlipViewPager( Context  context,AttributeSet  attributes )
	{
		super( context,attributes );
	}

	public  boolean  onInterceptTouchEvent( MotionEvent  event )
	{
		return  false;
	}

	public  boolean  onTouchEvent( MotionEvent  event )
	{
		return  false;
	}
}
