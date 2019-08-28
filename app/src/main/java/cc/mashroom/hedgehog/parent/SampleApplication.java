package cc.mashroom.hedgehog.parent;

import  android.content.Context;
import  android.content.res.Configuration;
import  android.os.Build;

import  com.facebook.drawee.backends.pipeline.Fresco;

import  java.util.Locale;
import  retrofit2.Retrofit;

public  class  SampleApplication  extends  cc.mashroom.hedgehog.parent.Application
{
    public  void  onCreate()
    {
        super.onCreate();

        Fresco.initialize( this );

        Configuration  configuration    = super.getResources().getConfiguration();

        Locale  locale = Locale.forLanguageTag( super.getSharedPreferences("CONFIGURATION",Context.MODE_PRIVATE).getString("LOCAL",Locale.ENGLISH.toLanguageTag()) );

        if( Build.VERSION.SDK_INT   < Build.VERSION_CODES.N )
        {
            configuration.locale     = locale;
        }
        else
        {
            configuration.setLocale( locale );
        }

        super.getResources().updateConfiguration( configuration,getResources().getDisplayMetrics() );

        super.setFileDownloadRetrofit( new  Retrofit.Builder().baseUrl("http://192.168.1.114:8011").build() );
    }
}
