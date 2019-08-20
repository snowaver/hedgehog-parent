package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.content.res.TypedArray;

import  android.graphics.Typeface;
import  android.text.InputType;
import  android.util.AttributeSet;
import  android.view.LayoutInflater;
import  android.view.View;
import android.widget.ImageView;
import  android.widget.RelativeLayout;
import  android.widget.TextView;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.util.ObjectUtils;
import  lombok.AccessLevel;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  StyleableEditView     extends  RelativeLayout
{
	public          CharSequence  getText()
	{
		return  ObjectUtils.cast(contentSwitcher.getDisplayedChildPosition() == 0 ? contentSwitcher.getDisplayedChild() : contentSwitcher.getDisplayedChild().findViewById(R.id.text_inputor),TextView.class).getText();
	}

	public  StyleableEditView  setText( CharSequence  text )
	{
		ObjectUtils.cast(contentSwitcher.getDisplayedChildPosition() == 0 ? contentSwitcher.getDisplayedChild() : contentSwitcher.getDisplayedChild().findViewById(R.id.text_inputor),TextView.class).setText(   text );

		return  this;
	}

	@Setter( value =  AccessLevel.PRIVATE )
	@Getter
	@Accessors( chain  = true )
	private  ViewSwitcher  contentSwitcher;
	@Setter( value =  AccessLevel.PRIVATE )
	@Getter
	@Accessors( chain  = true )
	private  TextView    title;

	public  StyleableEditView(     Context  context , AttributeSet  attributeSet )
	{
		super( context, attributeSet );

		LayoutInflater.from(context).inflate( R.layout.styleable_editview, this );

		TypedArray  typedArray = context.obtainStyledAttributes( attributeSet,R.styleable.StyleableEditView );

		this.setContentSwitcher(ObjectUtils.cast(super.findViewById(R.id.content_switcher),ViewSwitcher.class).setDisplayedChild(typedArray.getInteger(R.styleable.StyleableEditView_mode,0))).setTitle( ObjectUtils.cast(super.findViewById(R.id.title),TextView.class) );

		if( typedArray.hasValue(R.styleable.StyleableEditView_android_title    ) )
		{
			this.title.setVisibility(        View.VISIBLE );

			this.title.setText( typedArray.getString(R.styleable.StyleableEditView_android_title) );
		}

		if( typedArray.hasValue(R.styleable.StyleableEditView_titleTextColor   ) )
		{
			this.title.setTextColor( typedArray.getColor(R.styleable.StyleableEditView_titleTextColor,context.getResources().getColor(R.color.black)) );
		}

		if( typedArray.hasValue(R.styleable.StyleableEditView_rightArrowDirection) && typedArray.getInteger(R.styleable.StyleableEditView_rightArrowDirection,0) == 1 )
		{
			ObjectUtils.cast(super.findViewById(R.id.right_arrow),ImageView.class).setImageResource( R.drawable.lt_alr );
		}

		ObjectUtils.cast(super.findViewById(R.id.right_arrow),ImageView.class).setVisibility( typedArray.getBoolean(R.styleable.StyleableEditView_showRightArrow,false) ? View.VISIBLE : View.GONE );
		
		TextView  content = ObjectUtils.cast( contentSwitcher.getDisplayedChildPosition() == 0 ? contentSwitcher.getDisplayedChild() : contentSwitcher.getDisplayedChild().findViewById(R.id.text_inputor) );

		content.setTextColor( typedArray.getColor(R.styleable.StyleableEditView_android_textColor,context.getResources().getColor(    R.color.black)) );

		if( typedArray.hasValue(     R.styleable.StyleableEditView_android_hint) )
		{
			content.setHint(    typedArray.getString(R.styleable.StyleableEditView_android_hint ) );
		}

		if( typedArray.hasValue(R.styleable.StyleableEditView_android_inputType) )
		{
			content.setInputType(    typedArray.getInt( R.styleable.StyleableEditView_android_inputType,0 ) );
		}

		content.setTextAlignment( typedArray.getInt(R.styleable.StyleableEditView_android_textAlignment,TextView.TEXT_ALIGNMENT_VIEW_START) );

		content.setTypeface( Typeface.createFromAsset(super.getResources().getAssets(),typedArray.hasValue(R.styleable.StyleableEditView_textTypefacePath) ? typedArray.getString(R.styleable.StyleableEditView_textTypefacePath) : "font/droid_sans_mono.ttf") );  typedArray.recycle();
	}
}
