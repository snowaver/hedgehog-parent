package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.view.LayoutInflater;
import  android.view.ViewGroup;
import  android.widget.PopupWindow;

import  cc.mashroom.hedgehog.R;

public  class  TipWindow  extends  PopupWindow
{
    public  TipWindow( Context  context,int  layout,boolean  outsideTouchable )
    {
        super( LayoutInflater.from(context).inflate(layout,null),ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true );

        super.setTouchable( true );

        super.setOutsideTouchable( outsideTouchable );

//        super.setBackgroundDrawable( context.getResources().getDrawable(R.color.black) );
    }
}
