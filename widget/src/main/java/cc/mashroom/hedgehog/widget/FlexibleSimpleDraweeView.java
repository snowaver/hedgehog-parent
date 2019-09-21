package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.graphics.drawable.Animatable;
import  android.net.Uri;
import  androidx.annotation.Nullable;
import  android.util.AttributeSet;
import  android.view.ViewGroup;

import  com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import  com.facebook.drawee.controller.BaseControllerListener;
import  com.facebook.drawee.controller.ControllerListener;
import  com.facebook.drawee.generic.GenericDraweeHierarchy;
import  com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;

import  cc.mashroom.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public  class  FlexibleSimpleDraweeView  extends  com.facebook.drawee.view.SimpleDraweeView
{
    private  final   ControllerListener  controllerListener   = new  BaseControllerListener<ImageInfo>()
    {
        public  void  onFinalImageSet( String  id,@Nullable  ImageInfo  imageInfo,@Nullable  Animatable  animatable )
        {
	        updateFlexibleLayout(    imageInfo );
        }

        public  void  onIntermediateImageSet( String  id, @Nullable  ImageInfo  imageInfo )
        {
	        updateFlexibleLayout(    imageInfo );
        }
    };

    private  void  updateFlexibleLayout( ImageInfo  imageInfo )
    {
        if(      imageInfo != null )
        {
            ViewGroup.LayoutParams   draweeviewLayoutParameters =  super.getLayoutParams();
            System.err.println( super.getWidth()+"/"+super.getHeight());
            draweeviewLayoutParameters.width = (int)   ((float)  imageInfo.getWidth()*super.getWidth() / imageInfo.getHeight() );

            super.setLayoutParams(draweeviewLayoutParameters );
        }
    }

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes ,    int  defaultStyle )
    {
        super( context,attributes,defaultStyle );
    }

    public  FlexibleSimpleDraweeView( Context  context )
    {
        super(       context );
    }

    public  FlexibleSimpleDraweeView( Context  context,GenericDraweeHierarchy   hierarchy )
    {
        super( context,hierarchy  );
    }

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes )
    {
        super( context,attributes );
    }

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes,int  defaultStyleAttribute,int  defaultStyleResource )
    {
        super( context,attributes,defaultStyleAttribute,  defaultStyleResource );
    }
    @Accessors(  chain = true )
    @Setter
    @Getter
    protected  File  cacheFile;

    public  void  setImageURI( Uri  uri,Object  callerContext )
    {
        super.setController( ObjectUtils.cast(super.getControllerBuilder(),PipelineDraweeControllerBuilder.class).setControllerListener(controllerListener).setCallerContext(callerContext).setUri(uri).setOldController(getController()).build() );
    }
}
