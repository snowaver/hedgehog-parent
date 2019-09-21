package cc.mashroom.hedgehog.module.sample;

import  android.content.Intent;
import  android.os.Bundle;
import  android.view.View;
import  android.widget.Toast;

import  com.irozon.sneaker.Sneaker;

import  androidx.core.app.ActivityCompat;

import  java.io.File;
import  java.util.UUID;

import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.module.common.activity.AlbumMediaMultichoiceActivity;
import  cc.mashroom.hedgehog.module.common.activity.CamcorderActivity;
import  cc.mashroom.hedgehog.module.common.activity.ImageCropingActivity;
import  cc.mashroom.hedgehog.module.common.activity.VideoPreviewActivity;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import  cc.mashroom.hedgehog.util.DensityUtils;
import  cc.mashroom.hedgehog.util.ImageUtils;
import  cc.mashroom.hedgehog.widget.BottomSheetEditor;
import  cc.mashroom.hedgehog.widget.FlexibleSimpleDraweeView;
import  cc.mashroom.hedgehog.widget.HeaderBar;
import  cc.mashroom.hedgehog.widget.StyleableEditView;
import  cc.mashroom.util.ObjectUtils;

public  class  SampleActivity  extends  AbstractActivity  implements  HeaderBar.OnItemClickListener
{
    protected  void  onCreate( Bundle  savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        super.setContentView(  R.layout.activity_sample  );

        BottomSheetEditor editor = new BottomSheetEditor(this,5).setOnEditCompleteListener( (text) -> ObjectUtils.cast(super.findViewById(R.id.editview_external),StyleableEditView.class).setText(text) );
        /*
        ObjectUtils.cast(super.findViewById(R.id.editview_external),StyleableEditView.class).setOnClickListener( (v) -> ActivityCompat.startActivityForResult(this,new  Intent(this,EditorActivity.class).putExtra("LIMITATION",12).putExtra("TITLE","USERNAME").putExtra("EDIT_CONTENT","SNOWAVER"),0,null) );
        */
        ObjectUtils.cast(super.findViewById(R.id.editview_external),StyleableEditView.class).setOnClickListener( (v) -> editor.withText(((StyleableEditView)  v).getText()).show() );

        ObjectUtils.cast(super.findViewById(R.id.header_bar),HeaderBar.class).addDropdownItem(R.string.album_photo_and_video,R.color.white,18,DensityUtils.px(this,150),DensityUtils.px(this,50)).addDropdownItem(R.string.photo,R.color.white,18,DensityUtils.px(this,150),DensityUtils.px(this,50)).setOnItemClickListener( this );

        super.findViewById(R.id.to_image_cropping).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,ImageCropingActivity.class).putExtra("PATH",ImageUtils.toUri(this,R.drawable.lavender).toString()),null) );

        super.findViewById(R.id.to_camcorder_button).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,CamcorderActivity.class),null) );

        super.findViewById(R.id.to_album_multimedia_choice_button).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,AlbumMediaMultichoiceActivity.class).putExtra("MAX_COUNT",4).putExtra("MAX_FILE_SIZE",10*1024*1024L),null) );

        super.findViewById(R.id.show_sneaker_window_button).setOnClickListener( (button) -> super.showSneakerWindow(Sneaker.with(this),com.irozon.sneaker.R.drawable.ic_error,R.string.registration_form_error,R.color.white,R.color.red) );

        super.findViewById(R.id.download_and_preview_video_button).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,VideoPreviewActivity.class).putExtra("CACHE_FILE_PATH",new  File(application().getCacheDir(),UUID.randomUUID().toString().toUpperCase()).getPath()).putExtra("URL","http://192.168.1.114:8011/file/AF5A303C404511C8746A4511AA1CD9AD"),null) );

        ObjectUtils.cast(super.findViewById(R.id.drawee),FlexibleSimpleDraweeView.class).setImageURI( ImageUtils.toUri(this,R.drawable.lavender),this );
    }

    public  void  onItemClick( View itemView,int position )
    {
        Toast.makeText(this,"<<  "+position+"  >>",Toast.LENGTH_LONG).show();
    }
}
