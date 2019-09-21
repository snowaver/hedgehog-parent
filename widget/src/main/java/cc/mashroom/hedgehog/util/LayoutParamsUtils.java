package cc.mashroom.hedgehog.util;

import  android.view.View;
import  android.view.ViewGroup;

public  class  LayoutParamsUtils
{
    public  final  static  <V extends View>  V  update( V  view,Integer  width,Integer  height )
    {
        ViewGroup.LayoutParams  layoutParams = view.getLayoutParams();

        if( width != null )
        {
            layoutParams.width = width;
        }

        if( height!= null )
        {
            layoutParams.height=height;
        }

        view.setLayoutParams( layoutParams );

        return  view;
    }
}
