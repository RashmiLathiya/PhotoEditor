package com.candycam.photoeditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class ShareActivity extends Activity  {
    private ImageView home,facebook,back,insta,whatsap,share_image,more;



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_share);

            share_image=findViewById(R.id.share_image);
            this.back = (ImageView) findViewById(R.id.back);
            this.back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            Glide.with((Activity) this).load(Edit_Activity.save_bitmap).into(share_image);
            this.home = (ImageView) findViewById(R.id.home);
            this.home.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ShareActivity.this,StartActivity.class);
                    startActivity(intent);
                }
            });
            this.whatsap = (ImageView) findViewById(R.id.whatsap);
            this.whatsap.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            this.insta = (ImageView) findViewById(R.id.insta);
            this.insta.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            this.facebook = (ImageView) findViewById(R.id.facebook);
            this.facebook.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            this.more = (ImageView) findViewById(R.id.more);
            this.more.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


    }
}
