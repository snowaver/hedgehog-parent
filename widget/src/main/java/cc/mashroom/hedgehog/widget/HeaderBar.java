package cc.mashroom.hedgehog.widget;

import  android.app.Activity;
import  android.content.Context;
import  android.content.res.TypedArray;
import  android.graphics.Typeface;
import  android.graphics.drawable.ColorDrawable;
import  android.util.AttributeSet;
import  android.view.Gravity;
import  android.view.LayoutInflater;
import  android.view.View;
import  android.view.WindowManager;
import  android.widget.LinearLayout;
import  android.widget.RelativeLayout;
import  android.widget.TextView;

import  androidx.annotation.ColorRes;
import  androidx.annotation.NonNull;
import  androidx.annotation.StringRes;

import  cc.mashroom.hedgehog.util.DensityUtils;
import  cc.mashroom.util.ObjectUtils;
import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.util.ContextUtils;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  HeaderBar  extends  RelativeLayout       implements  View.OnClickListener
{
	public  HeaderBar  addDropdownItem( @StringRes  int  text,@ColorRes  int  textColor,float  textSize,Typeface  textTypeface,int  textGravity,@NonNull  LinearLayout.LayoutParams  layoutParams,int  leftPadding,int  topPadding,int  rightPadding,int  bottomPadding,int  dividerHeight )
	{
		if( this.addtionalDropdownContent.getChildCount()   >= 1 )
		{
			View  divider = new  View( super.getContext() );

			LinearLayout.LayoutParams  dividerLayoutParams = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,dividerHeight );  dividerLayoutParams.setMargins( DensityUtils.px(super.getContext(),10),0,DensityUtils.px(super.getContext(),10),0 );

			divider.setLayoutParams(  dividerLayoutParams );

			divider.setBackgroundResource(R.color.darkslategrey );

			this.addtionalDropdownContent.addView(divider );
		}

		TextView  textview  = new  TextView( super.getContext() );

		textview.setLayoutParams( layoutParams );

		textview.setPadding(      leftPadding,topPadding, rightPadding, bottomPadding );

		textview.setText(    text );

		textview.setTextColor( super.getContext().getResources().getColor(textColor ) );

		textview.setTextSize(   textSize );

		textview.setTypeface(     textTypeface );

		textview.setGravity( textGravity );

		textview.setOnClickListener(this );          this.addtionalDropdownContent.addView( textview );

		return  this;
	}

	public  HeaderBar  addDropdownItem( @StringRes  int  text,@ColorRes  int  textColor,float  textSize,int  width,int  height )
	{
		return  addDropdownItem( text,textColor,textSize,Typeface.createFromAsset(super.getContext().getAssets(),"font/droid_sans_mono.ttf"),Gravity.RIGHT|Gravity.CENTER_VERTICAL,new  LinearLayout.LayoutParams(width,height),DensityUtils.px(super.getContext(),10),0,DensityUtils.px(super.getContext(),10),0,1 );
	}

	protected  LinearLayout  addtionalDropdownContent;

	public  interface   OnItemClickListener
	{
		public  void  onItemClick( View  itemView,int  position );
	}
	@Accessors( chain=true )
	@Setter
	@Getter
	private  OnItemClickListener  onItemClickListener;

	public  void  onClick( View  v )
	{
		if( this.onItemClickListener   != null )
		{
			this.onItemClickListener.onItemClick( v, this.addtionalDropdownContent.indexOfChild(v)/2 );
		}
	}

	public  void  setTitle(CharSequence  title )
	{
		ObjectUtils.cast(super.findViewById(R.id.title),TextView.class).setText(title );
	}

	public  HeaderBar( Context  context,AttributeSet  attributes )
	{
		super( context,attributes );

		LayoutInflater.from(context).inflate( R.layout.header_bar, this );

		addtionalDropdownContent   = new  LinearLayout( context );

		addtionalDropdownContent.setLayoutParams( new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT) );

		addtionalDropdownContent.setBackgroundResource(    R.drawable.header_bar_dropdown_background );

		addtionalDropdownContent.setOrientation(  LinearLayout.VERTICAL );

		ObjectUtils.cast(super.findViewById(R.id.additional_text),TextView.class).setOnClickListener( (addtionalText) -> new  TipWindow(context,addtionalDropdownContent,true).showAsDropDown(this,context.getResources().getDisplayMetrics().widthPixels,0) );

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

		ObjectUtils.cast(super.findViewById(R.id.back_text),TextView.class).setOnClickListener( (textview) -> ContextUtils.finish( ObjectUtils.cast(context ) ) );

		ObjectUtils.cast(super.findViewById(R.id.additional_text),TextView.class).setText( !typedArray.hasValue(R.styleable.HeaderBar_additionalText) ? "" : typedArray.getString(R.styleable.HeaderBar_additionalText) );  typedArray.recycle();
	}
}
