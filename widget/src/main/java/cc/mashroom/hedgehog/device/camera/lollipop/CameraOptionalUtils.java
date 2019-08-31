package cc.mashroom.hedgehog.device.camera.lollipop;

import  android.hardware.camera2.CaptureRequest;
import  android.media.ImageReader;
import  android.util.Size;
import  android.view.Surface;

import  com.google.common.collect.TreeMultimap;

import  java.nio.ByteBuffer;
import  java.util.Collection;

import  cc.mashroom.util.IOUtils;
import  cc.mashroom.util.collection.map.Map;

public  class      CameraOptionalUtils
{
    public  static  CaptureRequest.Builder  configCaptureRequestBuilder( CaptureRequest.Builder  captureRequestBuilder,Collection<Surface>  surfaces,Map<CaptureRequest.Key<Integer>,Integer>  captureFeatures )
    {
        for( java.util.Map.Entry<CaptureRequest.Key<Integer>,Integer>  entry : captureFeatures.entrySet() )
        {
            captureRequestBuilder.set(entry.getKey(),entry.getValue() );
        }

        for( Surface  surface  : surfaces )
        {
            captureRequestBuilder.addTarget( surface );
        }

        return  captureRequestBuilder;
    }

    public  static  int  getOrientationHint( int  sensorOrientation,int  rotation )
    {
        if( sensorOrientation ==  90 )
        {
            return  LollipopCamera.ORIENTATIONS.get( rotation );
        }
        else
        if( sensorOrientation == 270 )
        {
            return  LollipopCamera.REVERSE_ORIENTATIONS.get( rotation );
        }

        return  0;
    }

    public  static  Size  getOptimalSize( Size  displaySize, Size[]  supportSizes )
    {
        TreeMultimap<Double,SizeComparator>  computedSizes = TreeMultimap.create();

        for( Size  size:supportSizes )
        {
            computedSizes.put( Math.abs(((double)  size.getWidth()/size.getHeight())-((double)  displaySize.getHeight()/displaySize.getWidth())),new  SizeComparator(displaySize,size) );
        }

        return      computedSizes.entries().iterator().next().getValue().getSize();
    }

    public  static  byte[]  getImageReaderBytes(   ImageReader  reader )
    {
        try
        {
            ByteBuffer  buf = reader.acquireNextImage().getPlanes()[0].getBuffer();

            byte[]  bytes = new  byte[buf.remaining()];  buf.get(bytes);

            return  bytes;
        }
        finally
        {
            IOUtils.closeQuietly( reader );
        }
    }
}
