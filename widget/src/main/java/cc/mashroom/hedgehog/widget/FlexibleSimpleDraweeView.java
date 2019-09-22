package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.graphics.drawable.Animatable;
import  android.net.Uri;
import  androidx.annotation.Nullable;
import  android.util.AttributeSet;

import  com.facebook.binaryresource.FileBinaryResource;
import  com.facebook.cache.common.SimpleCacheKey;
import  com.facebook.drawee.backends.pipeline.Fresco;
import  com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import  com.facebook.drawee.controller.BaseControllerListener;
import  com.facebook.drawee.controller.ControllerListener;
import  com.facebook.drawee.generic.GenericDraweeHierarchy;
import  com.facebook.imagepipeline.image.ImageInfo;

import  java.io.File;
import  java.io.IOException;

import  cc.mashroom.hedgehog.util.LayoutParamsUtils;
import  cc.mashroom.util.FileUtils;
import  cc.mashroom.util.ObjectUtils;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  FlexibleSimpleDraweeView  extends  com.facebook.drawee.view.SimpleDraweeView
{
    private  final   ControllerListener  controllerListener= new  BaseControllerListener<ImageInfo>()
    {
        public  void  onFinalImageSet( String  id,@Nullable  ImageInfo  imageInfo,@Nullable     Animatable  animatable )
        {
	        updateFlexibleLayout( true   ,  imageInfo );
        }

        public  void  onIntermediateImageSet( String  id, @Nullable  ImageInfo  imageInfo )
        {
            updateFlexibleLayout( false  ,  imageInfo );
        }
    };

    @Accessors(  chain = true )
    @Setter
    @Getter
    protected  Uri  uri;
    @Accessors(  chain = true )
    @Setter
    @Getter
    protected  File  cacheFile;

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes , int  defaultStyle )
    {
        super( context,attributes,defaultStyle);
    }

    public  FlexibleSimpleDraweeView( Context  context )
    {
        super(       context );
    }

    public  FlexibleSimpleDraweeView( Context  context,GenericDraweeHierarchy   hierarchy )
    {
        super( context, hierarchy );
    }

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes,int  defaultStyleAttribute,int  defaultStyleResource )
    {
        super( context,attributes,defaultStyleAttribute,  defaultStyleResource );
    }

    private  void  updateFlexibleLayout( boolean  cache,  ImageInfo  imageInfo  )
    {
        if( imageInfo!= null       )
        {
            double  scale = (double)  imageInfo.getWidth()/ imageInfo.getHeight() > (double)  super.getMaxWidth()/super.getMaxHeight() ? (double)  super.getMaxWidth()/imageInfo.getWidth() : (double)  super.getMaxHeight()/imageInfo.getHeight();

            LayoutParamsUtils.update( this,(int)  (imageInfo.getWidth()*scale)  ,(int)  (imageInfo.getHeight()*scale) );
        }

        if( cache&&cacheFile != null   && !cacheFile.exists() )
        {
            try
            {
                FileUtils.copyFile( ObjectUtils.cast(Fresco.getImagePipelineFactory().getMainFileCache().getResource(new  SimpleCacheKey(uri.toString())),FileBinaryResource.class).getFile(),cacheFile );
            }
            catch(  IOException  e )
            {
                e.printStackTrace();
            }
        }
    }

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes )
    {
        super( context,attributes );
    }

    public  void  setImageURI( Uri  uri,Object  callerContext )
    {
        this.uri  = uri;

        super.setController( ObjectUtils.cast(super.getControllerBuilder(),PipelineDraweeControllerBuilder.class).setControllerListener(controllerListener).setCallerContext(callerContext).setUri(uri).setOldController(getController()).build() );
    }
}
