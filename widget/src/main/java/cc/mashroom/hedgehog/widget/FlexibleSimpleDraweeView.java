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

import  cc.mashroom.hedgehog.util.LayoutParamsUtils;
import  cc.mashroom.util.FileUtils;
import  cc.mashroom.util.ObjectUtils;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.SneakyThrows;
import  lombok.experimental.Accessors;

public  class  FlexibleSimpleDraweeView  extends  com.facebook.drawee.view.SimpleDraweeView
{
    private  final   ControllerListener  controllerListener= new  BaseControllerListener<ImageInfo>()
    {
        public  void  onFinalImageSet( String  id,@Nullable  ImageInfo  imageInfo,@Nullable  Animatable  animatable )
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

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes )
    {
        super( context,attributes );
    }

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes,int  defaultStyleAttribute,int  defaultStyleResource )
    {
        super( context,attributes,defaultStyleAttribute,  defaultStyleResource );
    }

    @SneakyThrows
    private  void  updateFlexibleLayout( boolean  cache,  ImageInfo  imageInfo  )
    {
        if( imageInfo!= null       )
        {
            float  widthScaleRatio = ((float)  super.getWidth()-(float)  imageInfo.getWidth())/ imageInfo.getWidth();

            float  heightScaleRatio= ((float) super.getHeight()-(float) imageInfo.getHeight())/imageInfo.getHeight();

            float  scaleRatio = 1+(Math.abs(widthScaleRatio)< Math.abs(heightScaleRatio) ? widthScaleRatio : heightScaleRatio );

            LayoutParamsUtils.update( this,    (int)  (imageInfo.getWidth()*scaleRatio), (int)(imageInfo.getHeight()*   scaleRatio) );
        }

        if( cache&&cacheFile != null   && !cacheFile.exists() )
        {
            FileUtils.copyFile( ObjectUtils.cast(Fresco.getImagePipelineFactory().getMainFileCache().getResource(new  SimpleCacheKey(uri.toString())),FileBinaryResource.class).getFile(),cacheFile );
        }
    }

    public  void  setImageURI( Uri  uri,Object  callerContext )
    {
        this.uri  = uri;

        super.setController( ObjectUtils.cast(super.getControllerBuilder(),PipelineDraweeControllerBuilder.class).setControllerListener(controllerListener).setCallerContext(callerContext).setUri(uri).setOldController(getController()).build() );
    }
}
