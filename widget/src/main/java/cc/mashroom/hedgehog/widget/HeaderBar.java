package cc.mashroom.hedgehog.widget;

import  android.app.Activity;
import  android.content.Context;
import  android.content.res.TypedArray;
import  android.graphics.drawable.ColorDrawable;
import  android.util.AttributeSet;
import  android.view.LayoutInflater;
import  android.view.WindowManager;
import  android.widget.RelativeLayout;
import  android.widget.TextView;

import  cc.mashroom.util.ObjectUtils;
import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.util.ContextUtils;

public  class  HeaderBar  extends  RelativeLayout
{
	public  void  setTitle( CharSequence  title )
	{
		ObjectUtils.cast(super.findViewById(R.id.title),TextView.class).setText( title );
	}

	public  HeaderBar( Context  context,AttributeSet  attributes )
	{
		super( context,attributes );

		LayoutInflater.from(context).inflate( R.layout.header_bar, this );

		TypedArray  typedArray    = context.obtainStyledAttributes( attributes,R.styleable.HeaderBar );

		if( typedArray.getBoolean(R.styleable.HeaderBar_immersive,false) )
		{
			ObjectUtils.cast(context,Activity.class).getWindow().addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );

			ColorDrawable  color = new  ColorDrawable( ObjectUtils.cast(super.getBackground(),ColorDrawable.class).getColor() );

			color.setAlpha(   128 );

			ObjectUtils.cast(context,Activity.class).getWindow().setStatusBarColor( color.getColor() );
		}

		ObjectUtils.cast(super.findViewById(R.id.back_text),TextView.class).setText(  !typedArray.hasValue(R.styleable.HeaderBar_backText) ? "" : typedArray.getString(R.styleable.HeaderBar_backText) );

		ObjectUtils.cast(super.findViewById(R.id.title),TextView.class).setText( !typedArray.hasValue(R.styleable.HeaderBar_android_title) ? "" : typedArray.getString(R.styleable.HeaderBar_android_title) );

		ObjectUtils.cast(super.findViewById(R.id.back_text),TextView.class).setOnClickListener( (textview) -> ContextUtils.finish(ObjectUtils.cast(context)) );

		ObjectUtils.cast(super.findViewById(R.id.additional_text),TextView.class).setText( !typedArray.hasValue(R.styleable.HeaderBar_additionalText) ? "" : typedArray.getString(R.styleable.HeaderBar_additionalText) );  typedArray.recycle();
	}
}
