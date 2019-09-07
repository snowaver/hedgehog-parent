package cc.mashroom.hedgehog.module.common.listener;

import  android.widget.BaseAdapter;
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
	public  MultichoicesListener( BaseAdapter  adapter,int  maxCount,SmoothCheckBox.OnCheckedChangeListener  checkedChangeListenerCallback )
	{
		if( maxCount == 0   )
		{
			throw  new  IllegalArgumentException( "HEDGEHOG-PARENT:  ** MULTICHOICES  LISTENER **  max  count  or  max  file  size  should  not  be  zero." );
		}

		this.setAdapter(adapter).setMaxCount(maxCount).setCheckedChangeListenerCallback( checkedChangeListenerCallback );
	}

	public  boolean  validateCheckedObject(  T  object )
	{
		return     true;
	}

	@Accessors( chain =true )
	@Setter
	protected  SmoothCheckBox.OnCheckedChangeListener       checkedChangeListenerCallback;
	@Accessors( chain =true )
	@Setter
	protected  BaseAdapter  adapter;
	@Getter
	protected  Set<T>  choicesMapper= new  HashSet<T>();
	@Accessors( chain =true )
	@Setter
	@Getter
	protected  int  maxCount;

	public  void  onCheckedChanged( SmoothCheckBox  smoothCheckBox  , boolean  isChecked )
	{
		T  checkedObject   = ObjectUtils.cast( smoothCheckBox.getTag() );

		if( checkedObject  == null )
		{
			throw  new  IllegalArgumentException("HEDGEHOG-PARENT:  ** MULTICHOICES  LISTENER **  tag  missing  error" );
		}

		if( !isChecked )
		{
			this.choicesMapper.remove(  checkedObject );
		}
		else
		{
			if( !validateCheckedObject( checkedObject) )
			{
				smoothCheckBox.setChecked(   false  /* revert  smoothcheckbox  state */ );

				return ;
			}
			//  replace  the  previous  choice  if  the  max  count  is  one,  so  it  is  not  necessary  that  deselect  the  previous  choice  manually  if  select  one  but  want  a  new  choice.  notify  data  set  changed  event  may  lead  to  coruscating,  so  remove  it  and  need  a  manually  state  switching.
			if( this.maxCount == 1 )
			{
				this.choicesMapper.clear();

				this.choicesMapper.add( checkedObject );
			}
			else
			if( !choicesMapper.contains(checkedObject) )
			{
				smoothCheckBox.setChecked((this.choicesMapper.size() <= this.maxCount-1 || this.maxCount <= -1)&&this.choicesMapper.add(checkedObject),true );

				if( isChecked !=  smoothCheckBox.isChecked() )
				{
					Toasty.warning(smoothCheckBox.getContext(),smoothCheckBox.getContext().getString(R.string.album_multichoice_exceed_max_count_error,this.maxCount),Toast.LENGTH_LONG,false).show();
				}
			}
		}

		this.checkedChangeListenerCallback.onCheckedChanged( smoothCheckBox , isChecked );
	}
}
