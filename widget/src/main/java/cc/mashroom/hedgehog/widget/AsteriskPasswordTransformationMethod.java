package cc.mashroom.hedgehog.widget;

import  android.text.method.PasswordTransformationMethod;
import  android.view.View;

import  lombok.AllArgsConstructor;

@AllArgsConstructor

public  class AsteriskPasswordTransformationMethod  extends  PasswordTransformationMethod
{
    public  CharSequence  getTransformation( CharSequence  source,View  view )
    {
        return  new  PasswordCharSequence( source );
    }
    
    @AllArgsConstructor
    private  class  PasswordCharSequence  implements  CharSequence
    {
        public  char  charAt( int  index ){ return  '*'; }

        public  CharSequence  subSequence(  int  start, int  end ){ return  source.subSequence( start,end ); }

        private  CharSequence  source;

        public  int  length(){  return  source.length(); }
    }
}