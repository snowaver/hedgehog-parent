package cc.mashroom.hedgehog.module.common.listener;

import  android.content.Context;
import  android.widget.BaseAdapter;
import  android.widget.Toast;

import  java.io.File;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.system.Media;
import  cn.refactor.library.SmoothCheckBox;
import  es.dmoral.toasty.Toasty;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  MediaMultichoicesListener  extends  MultichoicesListener<Media>
{
    @Accessors(chain=true)
    @Setter
    protected  Context   context;
    @Accessors(chain=true)
    @Setter
    protected  long  maxFileSize;

    public  boolean  validateCheckedObject( Media  media )
    {
        File   mediaFile   = new  File( media.getPath() );

        if( !  mediaFile.exists() || mediaFile.length()  >  this.maxFileSize )
        {
            Toasty.warning(context,context.getString(R.string.album_multichoice_out_of_file_size_limitation_error),Toast.LENGTH_LONG,false).show();

            return  false;
        }

        return      super.validateCheckedObject(  media );
    }

    public  MediaMultichoicesListener( Context  context,BaseAdapter  adapter,int  maxCount,long  maxFileSize,SmoothCheckBox.OnCheckedChangeListener  checkedChangeListenerCallback )
    {
        super( adapter,maxCount,checkedChangeListenerCallback );  this.setContext(context).setMaxFileSize( maxFileSize );
    }
}
