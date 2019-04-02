package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.content.res.TypedArray;
import  android.graphics.Color;
import  androidx.core.content.res.ResourcesCompat;

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
import  lombok.AccessLevel;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  StyleableEditView  extends  RelativeLayout
{
	public  Editable  getText()
	{
		return  this.inputor.getText();
	}

    public  StyleableEditView  setInputorKeyListener(    OnKeyListener  listener )
    {
        this.inputor.setOnKeyListener( listener );

        return  this;
    }

	public  StyleableEditView  setText(    String  text )
	{
		this.inputor.setText(   text );

		return  this;
	}

	@Setter(value=AccessLevel.PRIVATE )
	@Accessors( chain  = true )
	private  EditText  inputor;
	@Setter(value=AccessLevel.PRIVATE )
	@Accessors( chain  = true )
	private  TextView    title;

	public  StyleableEditView(     Context  context , AttributeSet  attributeSet )
	{
		super( context, attributeSet );

		LayoutInflater.from(context).inflate(   R.layout.prompt_inputbox , this );

		TypedArray  typedArray = context.obtainStyledAttributes(   attributeSet ,R.styleable.StyleableEditView );

		this.setInputor(ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class)).setTitle( ObjectUtils.cast(super.findViewById(R.id.title),TextView.class) );

		if( typedArray.hasValue(    R.styleable.StyleableEditView_android_title) )
		{
			this.title.setVisibility(     View.VISIBLE );

			this.title.setText( typedArray.getString( R.styleable.StyleableEditView_android_title) );
		}

		if( typedArray.hasValue(R.styleable.StyleableEditView_android_editable) && !typedArray.getBoolean(R.styleable.StyleableEditView_android_editable,true) )
		{
			this.inputor.setFocusableInTouchMode(false );  this.inputor.setFocusable( false );  this.inputor.setKeyListener( null );
		}

		if( typedArray.hasValue(     R.styleable.StyleableEditView_android_hint) )
		{
			this.inputor.setHint( typedArray.getString(R.styleable.StyleableEditView_android_hint) );
		}

		if( typedArray.hasValue(R.styleable.StyleableEditView_android_inputType) )
		{
			this.inputor.setInputType( typedArray.getInt( R.styleable.StyleableEditView_android_inputType, 0 ) );

			if( typedArray.getInt(R.styleable.StyleableEditView_android_inputType,0) == InputType.TYPE_TEXT_VARIATION_PASSWORD + 1 )
			{
				ObjectUtils.cast(super.findViewById(R.id.edit_inputor),EditText.class).setTransformationMethod(   new  AsteriskPasswordTransformationMethod() );
			}
		}

		inputor.setTextColor( typedArray.getColor(R.styleable.StyleableEditView_android_textColor,Color.BLACK) );

		inputor.setTypeface( ResourcesCompat.getFont( super.getContext() , R.font.droid_sans_mono) );

		super.findViewById(R.id.top_border).setVisibility( typedArray.getBoolean(R.styleable.StyleableEditView_enableTopBorder,false)?View.VISIBLE: View.GONE );

		super.findViewById(R.id.bottom_border).setVisibility(  typedArray.getBoolean(R.styleable.StyleableEditView_enableBottomBorder , false) ? View.VISIBLE : View.GONE );  typedArray.recycle();
	}
}
