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

import  cc.mashroom.util.ObjectUtils;

public  class  FlexibleSimpleDraweeView  extends  com.facebook.drawee.view.SimpleDraweeView
{
    private  final  ControllerListener  listener = new  BaseControllerListener<ImageInfo>()
    {
        public  void  onFinalImageSet( String  id,@Nullable  ImageInfo  imageInfo,@Nullable  Animatable  animatable )
        {
	        updateFlexibleLayout( imageInfo );
        }

        public  void  onIntermediateImageSet( String  id, @Nullable  ImageInfo  imageInfo )
        {
	        updateFlexibleLayout( imageInfo );
        }
    };

    private  void  updateFlexibleLayout( ImageInfo  imageInfo )
    {
        if( imageInfo != null )
        {
            ViewGroup.LayoutParams  draweeLayoutParams = super.getLayoutParams();

	        draweeLayoutParams.width = (int)  ((float)  imageInfo.getWidth()*/*DensityUtils.px(super.getContext(),*/super.getMaxHeight()/*)*//imageInfo.getHeight() );

            super.setLayoutParams( draweeLayoutParams );
        }
    }

    public  FlexibleSimpleDraweeView( Context  context )
    {
        super( context );
    }

    public  FlexibleSimpleDraweeView( Context  context,GenericDraweeHierarchy   hierarchy )
    {
        super( context,hierarchy  );
    }

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes )
    {
        super( context,attributes );
    }

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes,int  defaultStyle )
    {
        super( context,attributes,defaultStyle );
    }

    public  FlexibleSimpleDraweeView( Context  context,AttributeSet  attributes,int  defaultStyleAttribute,int  defaultStyleResource )
    {
        super( context,attributes,defaultStyleAttribute,  defaultStyleResource );
    }

    public  void  setImageURI( Uri  uri,Object  callerContext )
    {
        super.setController( ObjectUtils.cast(super.getControllerBuilder(),PipelineDraweeControllerBuilder.class).setControllerListener(listener).setCallerContext(callerContext).setUri(uri).setOldController(getController()).build() );
    }
}
