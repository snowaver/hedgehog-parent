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
		super.onCreate(     savedInstanceState );

		super.setContentView(   R.layout.activity_editor );

		String  editContent = !super.getIntent().hasExtra("EDIT_CONTENT") ? "" : super.getIntent().getStringExtra( "EDIT_CONTENT" );

		if( StringUtils.isNotBlank(editContent) )
		{
			ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).setText(    editContent );

			ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).setSelection( editContent.length() );
		}

		ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).setTitle( super.getIntent().hasExtra("TITLE") ? super.getIntent().getStringExtra("TITLE") : super.getString(R.string.edit) );

		ObjectUtils.cast(super.findViewById(R.id.ok_button),Button.class).setOnClickListener( this );
	}

	public  void  onClick(   View  finishButton )
	{
		if( StringUtils.isNotBlank(    ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).getText().toString().trim()) )
		{
			super.putResultDataAndFinish( this,0,new  Intent().putExtra("EDIT_CONTENT",ObjectUtils.cast(super.findViewById(R.id.edit), EditText.class).getText().toString().trim()) );
		}
		else
		{
			Toasty.error(this,    super.getString(R.string.content_empty_error),Toast.LENGTH_LONG,false).show();
		}
	}
}