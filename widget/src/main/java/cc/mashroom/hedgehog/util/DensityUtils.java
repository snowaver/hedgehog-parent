package cc.mashroom.hedgehog.util;

import  android.content.Context;

public  class  DensityUtils
{
	public  static  int  px( Context  context,double  dp )
	{
		return  (int)  ( dp*context.getResources().getDisplayMetrics().density+0.5f );
	}

	public  static  int  dp( Context  context,double  px )
	{
		return  (int)  ( px/context.getResources().getDisplayMetrics().density+0.5f );
	}
}
