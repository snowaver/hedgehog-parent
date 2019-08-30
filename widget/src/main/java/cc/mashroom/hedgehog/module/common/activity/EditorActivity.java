package cc.mashroom.hedgehog.module.common.activity;

import  android.content.Intent;
import  android.os.Bundle;
import  android.text.Editable;
import  android.text.TextWatcher;
import  android.view.View;
import  android.widget.EditText;
import  android.widget.TextView;

import  com.irozon.sneaker.Sneaker;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.util.ObjectUtils;
import  cc.mashroom.util.StringUtils;
import  cc.mashroom.hedgehog.widget.HeaderBar;

/**
 *  edit  the  content  and  returns  edit  content  (intent  parameters:  CONTENT)  as  result  data.  intent  parameters:  TITLE  (title  on  headbar),  CONTENT  (content  to  be  edited)  AND  MAX_COUNT  (max  character  count,  zero  is  illegal  and  negative  for  no  limitation).
 */
public  class  EditorActivity  extends  AbstractActivity  implements  View.OnClickListener,TextWatcher
{
	public  void  beforeTextChanged( CharSequence  text,int  start,int  count,int  after )
	{

	}

	public  void  onClick(  View  finishbtn )
	{
		if( StringUtils.isNotBlank(ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).getText().toString().trim()) )
		{
			if( this.maxCount == -1 || ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).getText().length() >= maxCount+1 )
			{
				super.showSneakerWindow( Sneaker.with(this),com.irozon.sneaker.R.drawable.ic_error,R.string.content_length_out_of_limitation,R.color.white,R.color.red );
			}
			else
			{
				super.putResultDataAndFinish( this,0,new  Intent().putExtra("CONTENT",ObjectUtils.cast(super.findViewById(R.id.edit), EditText.class).getText().toString().trim()) );
			}
		}
		else
		{
			super.showSneakerWindow( Sneaker.with(this),com.irozon.sneaker.R.drawable.ic_error,R.string.content_empty,R.color.white,R.color.red );
		}
	}

	private  int  maxCount = -1;

	protected  void  onCreate( Bundle  savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		super.setContentView(   R.layout.activity_editor );

		ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).setTitle(    super.getIntent().hasExtra("TITLE") ? super.getIntent().getStringExtra("TITLE") : super.getString(R.string.edit) );

		this.maxCount = super.getIntent().getIntExtra("MAX_COUNT",-1 );

		String  content = !super.getIntent().hasExtra("CONTENT") ? "" : super.getIntent().getStringExtra( "CONTENT" );

		ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).addTextChangedListener( this );

		ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).setText( content );

		ObjectUtils.cast(super.findViewById(R.id.finish_button),TextView.class).setOnClickListener( this );

		ObjectUtils.cast(super.findViewById(R.id.edit),EditText.class).setSelection(content.length());
	}

	public  void  afterTextChanged(Editable  textEditable )
	{
		ObjectUtils.cast(super.findViewById(R.id.notes),TextView.class).setTextColor( super.getResources().getColor(this.maxCount == -1 || textEditable.length() <= this.maxCount ? R.color.darkgray : R.color.red) );

		ObjectUtils.cast(super.findViewById(R.id.notes),TextView.class).setText( this.maxCount <= -1 ? ""+textEditable.length() : StringUtils.rightPad(""+textEditable.length(),Math.max(2,(maxCount+"").length())," ")+" /"+StringUtils.leftPad(""+maxCount,Math.max(2,(maxCount+"").length())," ") );
	}

	public  void  onTextChanged(   CharSequence  text  ,int  start,int before,int  count )
	{
		ObjectUtils.cast(super.findViewById(R.id.notes),TextView.class).setTextColor( super.getResources().getColor(this.maxCount == -1 || text.length() <= this.maxCount ? R.color.darkgray : R.color.red) );

		ObjectUtils.cast(super.findViewById(R.id.notes),TextView.class).setText( this.maxCount <= -1 ? ""+text.length() : StringUtils.rightPad(""+text.length(),Math.max(2,(maxCount+"").length())," ")+" /"+StringUtils.leftPad(""+maxCount,Math.max(2,(maxCount+"").length())," ") );
	}
}