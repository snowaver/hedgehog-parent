package cc.mashroom.hedgehog.parent;

import  android.app.Activity;
import  android.content.Intent;
import  android.os.Bundle;
import  android.view.LayoutInflater;
import  android.view.View;
import  android.view.ViewGroup;
import  android.widget.ImageView;
import  android.widget.TextView;

import  com.irozon.sneaker.Sneaker;

import  androidx.appcompat.app.AppCompatActivity;

import  java.util.LinkedList;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.util.ContextUtils;
import  cc.mashroom.util.ObjectUtils;
import io.github.leibnik.justifytextview.JustifyTextView;

public  abstract  class  AbstractActivity  extends  AppCompatActivity
{
	public  final  static  LinkedList<Activity>  STACK = new  LinkedList<Activity>();

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

		ObjectUtils.cast(sneakerView.findViewById(cc.mashroom.hedgehog.R.id.icon),ImageView.class).setImageResource( iconResId );

		ObjectUtils.cast(sneakerView.findViewById(cc.mashroom.hedgehog.R.id.title),JustifyTextView.class).setText(  titleResId );

		ObjectUtils.cast(sneakerView.findViewById(cc.mashroom.hedgehog.R.id.title),JustifyTextView.class).setTextColor( super.getResources().getColor(titleColorResId) );

		sneaker.autoHide(true).setDuration( 3000 );

		ViewGroup.LayoutParams  layoutParams = sneakerView.findViewById(R.id.head_content).getLayoutParams();

		layoutParams.height = ContextUtils.getStatusBarHeight(this );

        sneakerView.findViewById(R.id.head_content).setLayoutParams(  layoutParams );

		sneakerView.setBackgroundColor( super.getResources().getColor( backgroundColorResId ) );      sneaker.sneakCustom( sneakerView );
	}
}
