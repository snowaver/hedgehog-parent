package cc.mashroom.hedgehog.util;

import  android.app.Activity;
import  android.app.ActivityManager;
import  android.content.ComponentName;
import  android.content.Context;
import  android.os.Build;
import  android.view.View;
import  android.view.WindowManager;
import  android.view.inputmethod.InputMethodManager;

import  cc.mashroom.util.ObjectUtils;

public  class  ContextUtils
{
    public  static  void  hideSoftinput( Activity  context,View  currentFocus  )
    {
        InputMethodManager  inputMethodManager = ObjectUtils.cast(context.getSystemService(Context.INPUT_METHOD_SERVICE) );

        if( inputMethodManager.isActive()  && currentFocus != null  && currentFocus.getWindowToken() != null )
        {
            inputMethodManager.hideSoftInputFromWindow( currentFocus.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS );
        }
    }

    public  static  void  hideSoftinput(Activity  context )
    {
        hideSoftinput( context,context.getCurrentFocus() );
    }

    public  static  void  finish( Activity  context )
    {
        hideSoftinput( context );

        context.finish();
    }

    public  static  void  showSoftinput(  Activity  context,View  currentFocus )
    {
        InputMethodManager  inputMethodManager = ObjectUtils.cast(context.getSystemService(Context.INPUT_METHOD_SERVICE) );

        if( !inputMethodManager.isActive() && currentFocus != null  && currentFocus.getWindowToken() != null )
        {
            inputMethodManager.showSoftInputFromInputMethod(currentFocus.getWindowToken(),InputMethodManager.SHOW_FORCED );
        }
    }

    public  static  int   getStatusBarHeight( Activity  context )
    {
        int  resId = context.getResources().getIdentifier( "status_bar_height","dimen","android" );

        return  resId <= 0 ? 0 : context.getResources().getDimensionPixelSize( resId );
    }


    public  static  boolean  isApplicationRunningBackground(  Context  context )
    {
        for( ActivityManager.RunningAppProcessInfo  processInfo : ObjectUtils.cast(context.getSystemService(Context.ACTIVITY_SERVICE),ActivityManager.class).getRunningAppProcesses() )
        {
            if( processInfo.processName.equals(context.getPackageName()) )
            {
                return  processInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }

        return  false;
    }

    public  static  boolean  setupImmerseBar( Activity  context )
    {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
        {
            context.getWindow().addFlags(     WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );

            return  true;
        }

        return  false;
    }

    public  static  void  setVisibility(  int  visibility,View...  views )
    {
        for( View  view : views )view.setVisibility(visibility );
    }

    public  static  boolean  isServiceRunning(Context  context,Class<?>  serviceClass )
    {
        return  ObjectUtils.cast(context.getSystemService(Context.ACTIVITY_SERVICE),ActivityManager.class).getRunningServiceControlPanel(new  ComponentName(context,serviceClass)) != null;
    }
}
