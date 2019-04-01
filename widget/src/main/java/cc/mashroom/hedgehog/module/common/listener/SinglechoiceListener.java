package cc.mashroom.hedgehog.module.common.listener;

import  android.widget.BaseAdapter;

import  java.util.concurrent.atomic.AtomicReference;

import  cc.mashroom.util.ObjectUtils;
import  cn.refactor.library.SmoothCheckBox;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  SinglechoiceListener<T>  implements  SmoothCheckBox.OnCheckedChangeListener
{
	public  SinglechoiceListener(   BaseAdapter  adapter , SmoothCheckBox.OnCheckedChangeListener  checkedChangeListener )
	{
		this.setAdapter(adapter).setCheckedChangeListenerCallback(checkedChangeListener );
	}

	@Accessors( chain =true )
	@Setter
	protected  BaseAdapter  adapter;
	@Accessors( chain =true )
	@Setter
	@Getter
	protected  AtomicReference<T>  checked = new  AtomicReference<T>();
	@Accessors( chain =true )
	@Setter
	protected  SmoothCheckBox.OnCheckedChangeListener  checkedChangeListenerCallback;

	public  void  onCheckedChanged(    SmoothCheckBox  smoothCheckBox,boolean  isChecked )
	{
		T  checkedObject = ObjectUtils.cast( smoothCheckBox.getTag() );

		if( checkedObject  == null )
		{
			throw  new  IllegalArgumentException( "SQUIRREL-CLIENT:  ** SINGLECHOICE  LISTENER **  tag  missing  error" );
		}

		if( !isChecked || checkedObject == checked.get() && isChecked )
		{
			return;
		}

		checked.set(checkedObject );

		this.adapter.notifyDataSetChanged();

		this.checkedChangeListenerCallback.onCheckedChanged( smoothCheckBox , isChecked );
	}
}
