package cc.mashroom.hedgehog.parent;

import  android.app.Activity;
import  android.content.Intent;
import  android.os.Bundle;
import  android.view.LayoutInflater;
import  android.view.View;
import  android.widget.ImageView;
import  android.widget.TextView;

import  com.irozon.sneaker.Sneaker;

import  androidx.appcompat.app.AppCompatActivity;

import  java.util.LinkedList;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.util.ObjectUtils;

public  abstract  class  AbstractActivity  extends  AppCompatActivity
{
	public  final  static  LinkedList<Activity>  STACK  = new  LinkedList<Activity>();

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

	protected  void  onDestroy()
	{
		super.onDestroy();

		STACK.remove( this );
	}

	public  void  error( Throwable  e )
	{
		e.printStackTrace( );
	}

	public  Sneaker  setSneakerView( Sneaker  sneaker, int  icon, int  title, int  titleColor )
	{
		View  sneakerAttatchedView = LayoutInflater.from(this).inflate( R.layout.sliding_sneaker,sneaker.getView(),false );

		ObjectUtils.cast(sneakerAttatchedView.findViewById(R.id.icon),ImageView.class).setImageResource(   icon );

		ObjectUtils.cast(sneakerAttatchedView.findViewById(R.id.title),TextView.class).setText( title );

		ObjectUtils.cast(sneakerAttatchedView.findViewById(R.id.title),TextView.class).setTextColor( titleColor );

		return       sneaker;
	}

	public  Application   application()
	{
		return  ObjectUtils.cast( super.getApplication() );
	}
}
