package cc.mashroom.hedgehog.module.common.activity;

import  android.content.Intent;
import  android.os.Bundle;
import  android.view.View;
import  android.widget.Button;
import  android.widget.EditText;
import  android.widget.Toast;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.util.ObjectUtils;
import  cc.mashroom.util.StringUtils;
import  cc.mashroom.hedgehog.widget.HeaderBar;
import  es.dmoral.toasty.Toasty;

public  class  EditorActivity  extends  AbstractActivity  implements  View.OnClickListener
{
	protected  void  onCreate( Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		super.setContentView(   R.layout.activity_editor );

		ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).setTitle( super.getString(R.string.edit) );

		ObjectUtils.cast(super.findViewById(R.id.ok_button),Button.class).setOnClickListener(  this );
	}

	public  void  onClick( View  view )
	{
		if( StringUtils.isNotBlank(ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).getText().toString().trim()) )
		{
			super.putResultDataAndFinish( this,0,new  Intent().putExtra("EDIT_CONTENT",ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).getText().toString().trim()) );
		}
		else
		{
			Toasty.error(this,getString(R.string.content_blank_error),Toast.LENGTH_LONG,false).show();
		}
	}
}