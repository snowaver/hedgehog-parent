package cc.mashroom.hedgehog.device.camera.eclair;

import  android.hardware.Camera;
import  android.view.Display;

import  lombok.AllArgsConstructor;
import  lombok.Getter;
import  lombok.ToString;

@AllArgsConstructor
@ToString
public  class  SizeComparator  implements  Comparable<SizeComparator>
{
	@Getter
	protected  Display   display;
	@Getter
	protected  Camera.Size  size;

	public  int  compareTo( SizeComparator  sizeComparator )
	{
		return  getDiagonal()- sizeComparator.getDiagonal();
	}

	protected  int  getDiagonal()
	{
		return  (int)  Math.abs( (double)  Math.sqrt(display.getWidth()*display.getWidth()+display.getHeight()*display.getHeight())-Math.sqrt(size.width*size.width+size.height*size.height) );
	}
}
