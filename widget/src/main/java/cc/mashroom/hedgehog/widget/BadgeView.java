package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.graphics.Canvas;
import  android.graphics.Color;
import  android.graphics.Paint;
import  android.util.AttributeSet;
import  android.view.View;

import  cc.mashroom.hedgehog.util.DensityUtils;
import  lombok.Setter;

public  class  BadgeView  extends  View
{
	public  BadgeView( Context  context,AttributeSet  attributeSet )
	{
		super( context, attributeSet );
	}

	@Setter
	private  String   badge;

	protected  void  onDraw( Canvas  canvas )
	{
		super.onDraw( canvas );

		canvas.drawARGB(  0, 0, 0, 0 );

		Paint  circleBackgroundPaint = new  Paint();

		circleBackgroundPaint.setColor( Color.RED );

		circleBackgroundPaint.setAntiAlias(  true );

		canvas.drawCircle( Math.min(getWidth(),getHeight())/2f,Math.min(getWidth(),getHeight())/2f,Math.min(getWidth(),getHeight())/2f,circleBackgroundPaint );

		Paint  badgePaint=new  Paint();

		badgePaint.setColor(   Color.WHITE );

		badgePaint.setTextSize( DensityUtils.px(getContext(), 10) );

		badgePaint.setFakeBoldText(   true );

		badgePaint.setTextAlign(Paint.Align.CENTER);

		canvas.drawText( badge,Math.min(getWidth(),getHeight())/2f,Math.min(getWidth(),getHeight())/2f+(badgePaint.getFontMetrics().descent-badgePaint.getFontMetrics().ascent)/2f-badgePaint.getFontMetrics().descent,badgePaint );
	}

	public  void  setBadge(   int  badge,int  invisibleThresholdValue,int  excceedThresholdValue,String  excceedText )
	{
		if( badge!= invisibleThresholdValue )
		{
			setBadge( badge >= excceedThresholdValue ? excceedText : String.valueOf(badge) );
		}

		super.setVisibility( (badge <= invisibleThresholdValue) ? View.GONE : View.VISIBLE );  super.postInvalidate();
	}
}