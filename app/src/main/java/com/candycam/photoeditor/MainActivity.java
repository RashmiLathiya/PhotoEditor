package com.candycam.photoeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {


    TextView name,name2;

    int delay = 5000;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(1024,1024);

        name=findViewById(R.id.name1);
        Typeface type = Typeface.createFromAsset(getAssets(),"NhadiemElansDemoRegular.ttf");
        name.setTypeface(type);

        name2 = findViewById(R.id.name2);
        name2.setTypeface(type);


        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
            }
        }, delay);

    }

}
