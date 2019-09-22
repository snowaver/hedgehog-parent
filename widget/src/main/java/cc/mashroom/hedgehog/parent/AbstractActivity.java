package cc.mashroom.hedgehog.parent;

import  android.app.Activity;
import  android.content.Intent;
import  android.graphics.Typeface;
import  android.os.Bundle;
import  android.view.LayoutInflater;
import  android.view.View;
import  android.widget.ImageView;

import  com.codesgood.views.JustifiedTextView;
import  com.irozon.sneaker.Sneaker;

import  androidx.appcompat.app.AppCompatActivity;

import  java.util.List;
import  java.util.concurrent.CopyOnWriteArrayList;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.util.ContextUtils;
import  cc.mashroom.hedgehog.util.LayoutParamsUtils;
import  cc.mashroom.util.ObjectUtils;

public  abstract  class  AbstractActivity  extends  AppCompatActivity
{
	public  final  static  List<Activity>  STACK =        new  CopyOnWriteArrayList<Activity>();

	protected  void  onCreate( Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		STACK.add( this );
	}

	public  void  putResultDataAndFinish( Activity  context,int  resultCode,Intent  resultData )
	{
		context.setResult( resultCode,resultData );

		context.finish( );
	}

	public  Application   application()
	{
		return  ObjectUtils.cast( super.getApplication() );
	}

	protected  void  onDestroy()
	{
		super.onDestroy();

		STACK.remove( this );
	}

	public  void  error( Throwable  e )
	{
		e.printStackTrace( );
	}

	public  void  showSneakerWindow( Sneaker  sneaker, int  iconResId, int  titleResId, int  titleColorResId, int  backgroundColorResId )
	{
		View  sneakerView  = LayoutInflater.from(this).inflate( R.layout.sliding_sneaker,sneaker.getView() , false );

		ObjectUtils.cast(sneakerView.findViewById(cc.mashroom.hedgehog.R.id.icon ),ImageView.class).setImageResource( iconResId );

		ObjectUtils.cast(sneakerView.findViewById(cc.mashroom.hedgehog.R.id.title),JustifiedTextView.class).setText( super.getResources().getString(titleResId) );

		ObjectUtils.cast(sneakerView.findViewById(cc.mashroom.hedgehog.R.id.title),JustifiedTextView.class).setTextColor( super.getResources().getColor(titleColorResId) );

		ObjectUtils.cast(sneakerView.findViewById(cc.mashroom.hedgehog.R.id.title),JustifiedTextView.class).setTypeface( Typeface.createFromAsset(super.getAssets(),"font/droid_sans_mono.ttf") );

		sneaker.autoHide(true).setDuration(3000 );

		LayoutParamsUtils.update( sneakerView.findViewById(R.id.head_content), null, ContextUtils.getStatusBarHeight(     this) );

		sneakerView.setBackgroundColor( super.getResources().getColor( backgroundColorResId ) );      sneaker.sneakCustom( sneakerView );
	}
}
