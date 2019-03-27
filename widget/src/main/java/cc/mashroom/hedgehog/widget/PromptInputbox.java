package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.content.res.TypedArray;
import  android.graphics.Color;
import  android.text.Editable;
import  android.text.InputType;
import  android.util.AttributeSet;
import  android.view.LayoutInflater;
import  android.view.View;
import  android.widget.EditText;
import  android.widget.RelativeLayout;
import  android.widget.TextView;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.util.ObjectUtils;

public  class  PromptInputbox  extends  RelativeLayout
{
	public  PromptInputbox  setText( String  text )
	{
		ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setText(   text );

		return  this;
	}

    public  void  setOnKeyListener(     OnKeyListener  listener )
    {
        ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setOnKeyListener( listener );
    }

	public  Editable  getText()
	{
		return  ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).getText();
	}

	public  PromptInputbox(     Context  context , AttributeSet  attributeSet )
	{
		super( context,attributeSet );

		LayoutInflater.from(context).inflate( R.layout.prompt_inputbox, this );

		TypedArray  typedArray = context.obtainStyledAttributes( attributeSet, R.styleable.PromptInputbox );

		if( typedArray.hasValue(    R.styleable.PromptInputbox_android_title) )
		{
			ObjectUtils.cast(super.findViewById(R.id.title),TextView.class).setText( typedArray.getString( R.styleable.PromptInputbox_android_title ) );
		}
		else
		{
			ObjectUtils.cast(super.findViewById(R.id.title),TextView.class).setVisibility(      View.GONE );
		}

		if( typedArray.hasValue(R.styleable.PromptInputbox_android_editable)&&!typedArray.getBoolean(R.styleable.PromptInputbox_android_editable,true) )
		{
			ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setKeyListener(   null );

			ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setFocusable(    false );

			ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setFocusableInTouchMode( false );
		}

		ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setHint( typedArray.getString(R.styleable.PromptInputbox_android_hint) );

		if( typedArray.hasValue(R.styleable.PromptInputbox_android_inputType) )
		{
			ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setInputType( typedArray.getInt(R.styleable.PromptInputbox_android_inputType, 0) );

			if( typedArray.getInt(R.styleable.PromptInputbox_android_inputType,0) == InputType.TYPE_TEXT_VARIATION_PASSWORD+1 )
			{
				ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setTransformationMethod(   new  AsteriskPasswordTransformationMethod() );
			}
		}

		ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setTextColor( typedArray.getColor(R.styleable.PromptInputbox_android_textColor,Color.BLACK) );

		super.findViewById(R.id.top_border).setVisibility( typedArray.getBoolean(R.styleable.PromptInputbox_enableTopBorder,false) ? View.VISIBLE : View.GONE );

		super.findViewById(R.id.bottom_border).setVisibility( typedArray.getBoolean(R.styleable.PromptInputbox_enableBottomBorder,false) ? View.VISIBLE : View.GONE );  typedArray.recycle();
	}
}
