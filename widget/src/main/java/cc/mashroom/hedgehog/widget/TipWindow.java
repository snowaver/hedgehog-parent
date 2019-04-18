package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.view.LayoutInflater;
import android.view.View;
import  android.view.ViewGroup;
import  android.widget.PopupWindow;

public  class  TipWindow  extends  PopupWindow
{
    public  TipWindow( Context  context,int  layout,boolean  outsideTouchable )
    {
        super( LayoutInflater.from(context).inflate(layout,null),ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true );

        super.setTouchable( true );

        super.setOutsideTouchable( outsideTouchable );
    }

    public  TipWindow( Context  context,View  view, boolean  outsideTouchable )
    {
        super( view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true );

        super.setTouchable( true );

        super.setOutsideTouchable( outsideTouchable );
    }
}
