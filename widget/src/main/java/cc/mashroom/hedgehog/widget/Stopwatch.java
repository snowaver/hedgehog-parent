package cc.mashroom.hedgehog.widget;

import  org.joda.time.DateTime;
import  org.joda.time.DateTimeZone;

import  android.content.Context;
import  android.os.SystemClock;
import  android.util.AttributeSet;
import  android.widget.Chronometer;

public  class  Stopwatch  extends  Chronometer
{
	public  Stopwatch( Context  context,AttributeSet  attributeSet )
	{
		super( context,attributeSet );
	}

	public  Stopwatch  start( String  showFormat )
	{
		super.setOnChronometerTickListener( (chronometer) -> chronometer.setText(new  DateTime(SystemClock.elapsedRealtime()-chronometer.getBase(),DateTimeZone.UTC).toString(chronometer.getFormat())) );

		super.setFormat( showFormat );

		super.setBase( SystemClock.elapsedRealtime() );
		
		super.start();
		
		return   this;
	}

	public  CharSequence stopForText()
	{
		super.stop( );

		return  super.getText();
	}
}
