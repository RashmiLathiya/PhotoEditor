package com.candycam.photoeditor.Album;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.candycam.photoeditor.R;
import java.util.ArrayList;
import java.util.Collections;

public class MyCreationActivity extends Activity  {
    private ImageView back;
    private CreationAdapter galleryAdapter;
    private GridView lstList;
    private TextView no_image;
    private ArrayList<String> array_image=new ArrayList<>();



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my_creation);

        no_image = findViewById(R.id.no_image);
        lstList =  findViewById(R.id.lstList);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MyCreationActivity.this.finish();
            }
        });
        galleryAdapter = new CreationAdapter(this, array_image);
        lstList.setAdapter(this.galleryAdapter);

    }



    public void onBackPressed() {
        finish();
    }
}
