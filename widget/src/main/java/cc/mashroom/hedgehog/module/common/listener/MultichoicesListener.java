package cc.mashroom.hedgehog.module.common.listener;

import  android.widget.BaseAdapter;
import  android.widget.CompoundButton;
import  android.widget.Toast;

import  java.util.HashSet;
import  java.util.Set;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.util.ObjectUtils;
import  cn.refactor.library.SmoothCheckBox;
import  es.dmoral.toasty.Toasty;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  MultichoicesListener<T>  implements  SmoothCheckBox.OnCheckedChangeListener
{
	public  MultichoicesListener( BaseAdapter  adapter,int  maxCount,CompoundButton.OnCheckedChangeListener  checkedChangeListenerCallback )
	{
		this.setAdapter(adapter).setMaxCount(maxCount).setCheckedChangeListenerCallback( checkedChangeListenerCallback );
	}

	@Accessors( chain =true )
	@Setter
	protected  BaseAdapter  adapter;
	@Getter
	protected  Set<T>  choicesMapper= new  HashSet<T>();
	@Accessors( chain =true )
	@Setter
	@Getter
	protected  int  maxCount;
	@Accessors( chain =true )
	@Setter
	protected  CompoundButton.OnCheckedChangeListener  checkedChangeListenerCallback;

	public  void  onCheckedChanged(    SmoothCheckBox  smoothCheckBox,boolean  isChecked )
	{
		T  checkedObject   = ObjectUtils.cast( smoothCheckBox.getTag() );

		if( checkedObject  == null )
		{
			throw  new  IllegalArgumentException("SQUIRREL-CLIENT:  ** MULTICHOICES  LISTENER **  tag  missing  error" );
		}

		if( !isChecked )
		{
			this.choicesMapper.remove(  checkedObject );
		}
		else
		{
			//  replace  the  previous  choice  if  the  max  count  is  one,  so  it  is  not  necessary  that  deselect  the  previous  choice  manually  if  select  one  but  want  a  new  choice.
			if( this.maxCount == 1 )
			{
				this.choicesMapper.clear();

				this.choicesMapper.add( checkedObject );
			}
			else
			if( !choicesMapper.contains(checkedObject) )
			{
				smoothCheckBox.setChecked( choicesMapper.size() <= maxCount-1 && choicesMapper.add(checkedObject),true );

				if( isChecked   != smoothCheckBox.isChecked() )
				{
					Toasty.warning(smoothCheckBox.getContext(),smoothCheckBox.getContext().getString(R.string.multichoice_limitation_error),Toast.LENGTH_LONG,false).show();
				}
			}
		}

		checkedChangeListenerCallback.onCheckedChanged((CompoundButton)  null,isChecked );
	}
}
