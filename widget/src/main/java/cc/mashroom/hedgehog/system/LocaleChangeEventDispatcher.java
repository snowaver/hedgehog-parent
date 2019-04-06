package cc.mashroom.hedgehog.system;

import  java.util.List;
import  java.util.Locale;
import  java.util.concurrent.CopyOnWriteArrayList;

public  class  LocaleChangeEventDispatcher
{
    private  final  static  List<LocaleChangeListener>  LISTENERS = new  CopyOnWriteArrayList<LocaleChangeListener>();

    public interface  LocaleChangeListener
    {
        public  void  onChange(    Locale  locale );
    }

    public  static  void  addListener(    LocaleChangeListener  listener )
    {
        if( listener != null )
        {
            LISTENERS.add(     listener );
        }

    }

    public  static  void  removeListener( LocaleChangeListener  listener )
    {
        if( listener != null )
        {
            LISTENERS.remove(  listener );
        }
    }

    public  static  void  onChange( Locale  locale )
    {
        for( LocaleChangeListener  listener : LISTENERS )  listener.onChange(   locale );
    }
}
