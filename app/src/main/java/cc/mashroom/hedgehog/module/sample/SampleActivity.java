package cc.mashroom.hedgehog.module.sample;

import  android.content.Intent;
import  android.os.Bundle;

import  com.irozon.sneaker.Sneaker;

import  androidx.core.app.ActivityCompat;
import  cc.mashroom.hedgehog.R;
import  cc.mashroom.hedgehog.module.common.activity.AlbumMediaMultichoiceActivity;
import  cc.mashroom.hedgehog.module.common.activity.CamcorderActivity;
import cc.mashroom.hedgehog.module.common.activity.ImageCropingActivity;
import  cc.mashroom.hedgehog.parent.AbstractActivity;
import cc.mashroom.hedgehog.util.ImageUtils;

public  class  SampleActivity  extends  AbstractActivity
{
    protected  void  onCreate( Bundle  savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        super.setContentView(  R.layout.activity_sample  );

        super.findViewById(R.id.to_image_cropping).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,ImageCropingActivity.class).putExtra("PATH",ImageUtils.toUri(this,R.drawable.lavender).toString()),null) );

        super.findViewById(R.id.to_camcorder_button).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,CamcorderActivity.class),null) );

        super.findViewById(R.id.to_album_multimedia_choice_button).setOnClickListener( (button) -> ActivityCompat.startActivity(this,new  Intent(this,AlbumMediaMultichoiceActivity.class),null) );

        super.findViewById(R.id.show_sneaker_window_button).setOnClickListener( (button) -> super.showSneakerWindow(Sneaker.with(this),com.irozon.sneaker.R.drawable.ic_error,R.string.registration_form_error,R.color.white,R.color.red) );
    }
}
