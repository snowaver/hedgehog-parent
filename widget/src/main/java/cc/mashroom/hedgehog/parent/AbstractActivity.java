package cc.mashroom.hedgehog.parent;

import  android.app.Activity;
import  android.content.Intent;
import  android.os.Bundle;
import  androidx.appcompat.app.AppCompatActivity;

import  java.util.LinkedList;

import  cc.mashroom.util.ObjectUtils;

public  abstract  class  AbstractActivity  extends  AppCompatActivity
{
	public  final  static  LinkedList<Activity>  STACK = new  LinkedList<Activity>();

	protected  void  onCreate( Bundle  savedInstanceState )
	{
//		System.out.println( "//*ACTIVITY.STACK:  "+STACK );

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

//		System.out.println( "//*ACTIVITY.STACK:  "+STACK );
	}

	public  void  error( Throwable  e )
	{
		e.printStackTrace( );
	}

	public  Application   application()
	{
		return  ObjectUtils.cast( super.getApplication() );
	}
}
