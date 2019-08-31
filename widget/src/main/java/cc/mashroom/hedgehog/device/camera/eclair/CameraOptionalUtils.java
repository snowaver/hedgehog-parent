package cc.mashroom.hedgehog.device.camera.eclair;

import  android.hardware.Camera;
import  android.view.Display;

import  com.google.common.collect.TreeMultimap;

import  java.util.List;

public  class  CameraOptionalUtils
{
    public  static  Camera.Size  getOptimalSize( Display  display,List<Camera.Size>  supportSizes )
    {
        TreeMultimap<Double,SizeComparator>  computedSizes = TreeMultimap.create();

        for( Camera.Size  size : supportSizes )
        {
            computedSizes.put( Math.abs(((double)  size.width/size.height)-((double)  display.getHeight()/display.getWidth())),new  SizeComparator(display,size) );
        }

        return      computedSizes.entries().iterator().next().getValue().getSize();
    }
}
