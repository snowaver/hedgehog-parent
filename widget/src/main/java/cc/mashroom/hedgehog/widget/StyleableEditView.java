package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.content.res.TypedArray;

import  android.graphics.Typeface;
import  android.text.InputType;
import  android.util.AttributeSet;
import  android.view.LayoutInflater;
import  android.view.View;
import  android.widget.EditText;
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
	public      CharSequence  getText()
	{
		return  this.content.getText();
	}

	public  StyleableEditView  setText( CharSequence  text )
	{
		this.content.setText(   text );

		return  this;
	}

	@Setter(value=AccessLevel.PRIVATE )
	@Getter
	@Accessors( chain  = true )
	private  EditText  content;
	@Setter(value=AccessLevel.PRIVATE )
	@Getter
	@Accessors( chain  = true )
	private  TextView    title;

	public  StyleableEditView(     Context  context , AttributeSet  attributeSet )
	{
		super( context, attributeSet );

		LayoutInflater.from(context).inflate( R.layout.styleable_editview, this );

		TypedArray  typedArray = context.obtainStyledAttributes(   attributeSet ,R.styleable.StyleableEditView );

		this.setContent(ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class)).setTitle( ObjectUtils.cast(super.findViewById(R.id.title),TextView.class) );

		if( typedArray.hasValue(    R.styleable.StyleableEditView_android_title) )
		{
			this.title.setVisibility(        View.VISIBLE );

			this.title.setText( typedArray.getString( R.styleable.StyleableEditView_android_title) );
		}

		if( typedArray.hasValue(R.styleable.StyleableEditView_android_editable) && !typedArray.getBoolean(R.styleable.StyleableEditView_android_editable,true) )
		{
			this.content.setFocusableInTouchMode(   false );  content.setFocusable( false );  this.content.setKeyListener( null );

			this.content.setTextColor( context.getResources().getColor(R.color.darkgray ) );
		}
		else
		{
			this.content.setTextColor( typedArray.getColor(R.styleable.StyleableEditView_android_textColor,context.getResources().getColor( R.color.black ) ) );
		}

		if( typedArray.hasValue(     R.styleable.StyleableEditView_android_hint) )
		{
			this.content.setHint( typedArray.getString(R.styleable.StyleableEditView_android_hint) );
		}

		if( typedArray.hasValue(R.styleable.StyleableEditView_android_inputType) )
		{
			this.content.setInputType( typedArray.getInt( R.styleable.StyleableEditView_android_inputType, 0 ) );

			if( typedArray.getInt(R.styleable.StyleableEditView_android_inputType,0) == InputType.TYPE_TEXT_VARIATION_PASSWORD+1 )
			{
				this.content.setTransformationMethod(  new  AsteriskPasswordTransformationMethod() );
			}
		}

		ObjectUtils.cast(super.findViewById(R.id.right_arrow),TextView.class).setVisibility( typedArray.getBoolean(R.styleable.StyleableEditView_showRightArrow,false) ? View.VISIBLE : View.GONE );

		content.setTextAlignment( typedArray.getInt(R.styleable.StyleableEditView_android_textAlignment,TextView.TEXT_ALIGNMENT_VIEW_START) );

		content.setTypeface( Typeface.createFromAsset(super.getResources().getAssets(),typedArray.hasValue(R.styleable.StyleableEditView_textTypefacePath) ? typedArray.getString(R.styleable.StyleableEditView_textTypefacePath) : "font/droid_sans_mono.ttf") );  typedArray.recycle();
	}
}
