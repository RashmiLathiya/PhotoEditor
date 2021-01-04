package com.candycam.photoeditor.Effect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.candycam.photoeditor.Edit_Activity;
import com.candycam.photoeditor.HorizontalListView;
import com.candycam.photoeditor.R;
import com.candycam.photoeditor.jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class Effect_Activity extends AppCompatActivity {

    HorizontalListView effect_list, smoke_list, lens_list;
    GPUImageView mGPUImageView;
    ImageView back,save,effect,smoke,lens;
    public static Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);

        effect_list=findViewById(R.id.effect_list);
        mGPUImageView=findViewById(R.id.gpuimage);
        mGPUImageView.setImage(Edit_Activity.bitmap_img);
        back=findViewById(R.id.back);
        save=findViewById(R.id.save);
        effect=findViewById(R.id.effect);
        smoke=findViewById(R.id.smoke);
        lens=findViewById(R.id.lens);
        smoke_list=findViewById(R.id.smoke_list);
        lens_list=findViewById(R.id.lens_list);

        getWindow().setFlags(1024,1024);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
        effect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        lens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Add_Image_Effect();
        Add_Image_Smoke();

    }

    private void Add_Image_Smoke() {
        ArrayList arrayList=new ArrayList();
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        effect_list.setAdapter((ListAdapter) new Smoke_Adapter(this, arrayList));
    }

    private void Add_Image_Effect() {

        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(R.drawable.cup2));

        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));

        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));

        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));

        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));

        arrayList.add(Integer.valueOf(R.drawable.cup2));
        arrayList.add(Integer.valueOf(R.drawable.cup2));

        effect_list.setAdapter((ListAdapter) new EffectsAdapter(this, arrayList));

        effect_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mGPUImageView.setImage(Edit_Activity.bitmap_img);

                }
                if (position == 1) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_1977));

                }
                if (position == 2) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_AMARO));
                }
                if (position == 3) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_BRANNAN));

                }
                if (position == 4) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_EARLYBIRD));

                }
                if (position == 5) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_HEFE));

                }
                if (position == 6) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_HUDSON));

                }
                if (position == 7) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_INKWELL));

                }
                if (position == 8) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_LOMO));

                }
                if (position == 9) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_LORDKELVIN));

                }
                if (position == 10) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_NASHVILLE));

                }
                if (position == 11) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_NASHVILLE));

                }
                if (position == 12) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_SIERRA));

                }
                if (position == 13) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_SUTRO));
                }
                if (position == 14) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_TOASTER));

                }
                if (position == 15) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_VALENCIA));
                }
                if (position == 16) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_WALDEN));
                }
                if (position == 17) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.I_XPROII));

                }
                if (position == 18) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.CONTRAST));

                }

                if (position == 19) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.BRIGHTNESS));

                }
                if (position == 20) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.SEPIA));

                }
                if (position == 21) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.VIGNETTE));

                }
                if (position == 22) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.TONE_CURVE));

                }
                if (position == 23) {
                    mGPUImageView.setFilter(GPUImageFilterTools.createFilterForType(Effect_Activity.this, GPUImageFilterTools.FilterType.LOOKUP_AMATORKA));
                }

            }
        });
    }

    private void saveImage() {
        String FileName = System.currentTimeMillis() + ".jpg";
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "GPUImage" + "/" + FileName);
        file.getParentFile().mkdirs();

        FileOutputStream out = null;
        bitmap = mGPUImageView.getGPUImage().getBitmapWithFilterApplied();
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Edit_Activity.Edit_photo=true;
            Intent intent=new Intent(Effect_Activity.this,Edit_Activity.class);
            intent.putExtra("imagePath", file);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
