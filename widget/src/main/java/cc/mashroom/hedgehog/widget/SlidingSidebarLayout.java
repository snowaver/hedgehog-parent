package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.content.res.TypedArray;
import  androidx.drawerlayout.widget.DrawerLayout;
import  android.util.AttributeSet;
import  android.view.View;

import  cc.mashroom.hedgehog.R;

public  class  SlidingSidebarLayout  extends  DrawerLayout  implements  DrawerLayout.DrawerListener
{
	public  SlidingSidebarLayout( Context  context , AttributeSet  attributeSet )
	{
		super( context,attributeSet );

		addDrawerListener(this );

		TypedArray  typedArray = super.getContext().obtainStyledAttributes( attributeSet,R.styleable.SlidingSidebarLayout );

		this.transformMode = typedArray.getInt( R.styleable.SlidingSidebarLayout_transformMode,0 );  typedArray.recycle();
	}

	protected  Integer  transformMode;

	public  void  onDrawerSlide( View  drawer, float  slideOffset )
	{
		View  contentView = findViewWithTag( "content" );

		contentView.setTranslationX(   drawer.getMeasuredWidth() * slideOffset );

		contentView.setPivotX(0);

		contentView.setPivotY( contentView.getMeasuredHeight()/2 );

		if( transformMode  == 1 )
		{
			contentView.setScaleX( 0.8f + 0.2f * (1-slideOffset) );

			contentView.setScaleY( 0.8f + 0.2f * (1-slideOffset) );
		}

		contentView.invalidate();
	}

	public  void  onDrawerClosed( View  drawerView )
	{

	}

	public  void  onDrawerStateChanged(   int  newState )
	{

	}

	public  void  onDrawerOpened( View  drawerView )
	{

	}
}
