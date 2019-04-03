package cc.mashroom.hedgehog.sample.parent;

import  android.content.Context;
import  android.content.res.Configuration;
import  android.os.Build;

import  com.facebook.drawee.backends.pipeline.Fresco;

import  java.util.Locale;

public  class  Application    extends  cc.mashroom.hedgehog.parent.Application
{
    public  void  onCreate()
    {
        super.onCreate();

        Fresco.initialize( this );

        Configuration  configuration= super.getResources().getConfiguration();

        Locale  locale = Locale.forLanguageTag( super.getSharedPreferences("CONFIGURATION", Context.MODE_PRIVATE).getString("LOCAL",Locale.ENGLISH.toLanguageTag()) );

        if( Build.VERSION.SDK_INT   < Build.VERSION_CODES.N )
        {
            configuration.locale     = locale;
        }
        else
        {
            configuration.setLocale( locale );
        }

        super.getResources().updateConfiguration( configuration,getResources().getDisplayMetrics() );
    }
}
