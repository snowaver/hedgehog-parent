package cc.mashroom.hedgehog.device.camera.lollipop;

import  android.util.Size;

import  lombok.AllArgsConstructor;
import  lombok.Getter;
import  lombok.ToString;

@AllArgsConstructor
@ToString
public  class  SizeComparator  implements  Comparable<SizeComparator>
{
	@Getter
	protected  Size  display;
	@Getter
	protected  Size  size;

	public  int  compareTo( SizeComparator  sizeComparator )
	{
		return  getDiagonal()- sizeComparator.getDiagonal();
	}

	protected  int   getDiagonal()
	{
		return  (int)  Math.abs( (double)  Math.sqrt(display.getWidth()*display.getWidth()+display.getHeight()*display.getHeight())-Math.sqrt(size.getWidth()*size.getWidth()+size.getHeight()*size.getHeight()) );
	}
}
