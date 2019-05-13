package cc.mashroom.hedgehog.module.common.activity;

import  android.content.Intent;
import  android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import  android.view.View;
import  android.widget.Button;
import  android.widget.EditText;
import android.widget.TextView;
import  android.widget.Toast;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.util.ObjectUtils;
import  cc.mashroom.util.StringUtils;
import  cc.mashroom.hedgehog.widget.HeaderBar;
import  es.dmoral.toasty.Toasty;

public  class  EditorActivity  extends  AbstractActivity  implements  View.OnClickListener,TextWatcher
{
	protected  void  onCreate( Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		super.setContentView(   R.layout.activity_editor );

		ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).setTitle( super.getIntent().hasExtra("TITLE") ? super.getIntent().getStringExtra("TITLE") : super.getString(R.string.edit) );

		String  content = !super.getIntent().hasExtra("EDIT_CONTENT") ? "" : super.getIntent().getStringExtra( "EDIT_CONTENT" );

		ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).addTextChangedListener( this );

		ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).setText( content );

		ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).setSelection(content.length());

		this.limitation = super.getIntent().getIntExtra( "LIMITATION",-1 );

		ObjectUtils.cast(super.findViewById(R.id.finish_button),TextView.class).setOnClickListener(  this );
	}

	private  int  limitation = -1;

	public  void  onClick(  View  finishBtn )
	{
		if( StringUtils.isNotBlank(ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).getText().toString().trim()) )
		{
			super.putResultDataAndFinish( this,0,new  Intent().putExtra("EDIT_CONTENT",ObjectUtils.cast(super.findViewById(R.id.edit), EditText.class).getText().toString().trim()) );
		}
		else
		{
			Toasty.error(this,super.getString(R.string.content_empty_error),Toast.LENGTH_LONG,false).show();
		}
	}

	public  void  beforeTextChanged( CharSequence  text,int  start,int  count,int  after )
	{

	}

	public  void  afterTextChanged(Editable  textEditable )
	{
		ObjectUtils.cast(super.findViewById(R.id.notes),TextView.class).setTextColor( super.getResources().getColor(this.limitation == -1 || textEditable.length() <= this.limitation ? R.color.darkgray : R.color.red) );

		ObjectUtils.cast(super.findViewById(R.id.notes),TextView.class).setText( textEditable.length()+(this.limitation == -1 ? "" : "/"+limitation) );
	}

	public  void  onTextChanged(     CharSequence  text,int  start,int before,int  count )
	{
		ObjectUtils.cast(super.findViewById(R.id.notes),TextView.class).setText( text.length()+(this.limitation == -1 ? "" : "/"+limitation) );

		ObjectUtils.cast(super.findViewById(R.id.notes),TextView.class).setTextColor( super.getResources().getColor(this.limitation == -1 || text.length() <= this.limitation ? R.color.darkgray : R.color.red) );
	}
}