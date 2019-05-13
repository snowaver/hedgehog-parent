package cc.mashroom.hedgehog.module.sample;

import  android.content.Intent;
import  android.os.Bundle;
import  android.view.View;
import  android.widget.Toast;

import  com.irozon.sneaker.Sneaker;

import  androidx.core.app.ActivityCompat;
import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.module.common.activity.AlbumMediaMultichoiceActivity;
import  cc.mashroom.hedgehog.module.common.activity.CamcorderActivity;
import cc.mashroom.hedgehog.module.common.activity.EditorActivity;
import  cc.mashroom.hedgehog.module.common.activity.ImageCropingActivity;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.util.DensityUtils;
import  cc.mashroom.hedgehog.util.ImageUtils;
import  cc.mashroom.hedgehog.widget.HeaderBar;
import  cc.mashroom.hedgehog.widget.StyleableEditView;
import  cc.mashroom.util.ObjectUtils;

public  class  SampleActivity  extends  AbstractActivity  implements  HeaderBar.OnItemClickListener
{
    protected  void  onCreate( Bundle  savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        super.setContentView(  R.layout.activity_sample  );

        ObjectUtils.cast(super.findViewById(R.id.editview_external),StyleableEditView.class).setOnClickListener( (v) -> ActivityCompat.startActivityForResult(this,new  Intent(this,EditorActivity.class).putExtra("LIMITATION",12).putExtra("TITLE","USERNAME").putExtra("EDIT_CONTENT","SNOWAVER"),0,null) );

        ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).addDropdownItem(R.string.album_photo_and_video,R.color.white,18,DensityUtils.px(this,150),DensityUtils.px(this,50)).addDropdownItem(R.string.photo,R.color.white,18,DensityUtils.px(this,150),DensityUtils.px(this,50)).setOnItemClickListener( this );

        super.findViewById(R.id.to_image_cropping).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,ImageCropingActivity.class).putExtra("PATH",ImageUtils.toUri(this,R.drawable.lavender).toString()),null) );

        super.findViewById(R.id.to_camcorder_button).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,CamcorderActivity.class),null) );

        super.findViewById(R.id.to_album_multimedia_choice_button).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,AlbumMediaMultichoiceActivity.class),null) );

        super.findViewById(R.id.show_sneaker_window_button).setOnClickListener( (button) -> super.showSneakerWindow(Sneaker.with(this),com.irozon.sneaker.R.drawable.ic_error,R.string.registration_form_error,R.color.white,R.color.red) );
    }

    public  void  onItemClick( View itemView,int position )
    {
        Toast.makeText(this,"<<  "+position+"  >>",Toast.LENGTH_LONG).show();
    }
}
