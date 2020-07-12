package com.candycam.photoeditor.CropImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.candycam.photoeditor.Edit_Activity;
import com.candycam.photoeditor.R;
import com.candycam.photoeditor.StartActivity;
import com.isseiaoki.simplecropview.CropImageView;

public class CropActivity extends Activity implements OnClickListener {
    public static Bitmap a_image;
    CropImageView cropimageview;
    private TextView tv_9_16;
    private TextView tv_16_9;
    private ImageView ic_done;
    private ImageView ic_back;
    private ImageView rotate_left;
    private ImageView rotate_right;
    private LinearLayout free;
    private LinearLayout Original;
    private LinearLayout l1_1;
    private LinearLayout l3_4;
    private LinearLayout lll_4_3;
    private LinearLayout lll_9_16;
    private LinearLayout lll_16_9;
    private ImageView free_1;
    private ImageView Original_1;
    private ImageView ll1_1;
    private ImageView ll3_4;
    private ImageView ll_4_3;
    private ImageView ll_9_16;
    private ImageView ll_16_9;
    private TextView t_free;
    private TextView t_Original;
    private TextView t1_1;
    private TextView t3_4;
    private TextView tv_4_3;

    private void Initializ_All() {
        this.cropimageview = (CropImageView) findViewById(R.id.cropImageView);
        this.free = (LinearLayout) findViewById(R.id.free);
        this.free.setOnClickListener(this);
        this.Original = (LinearLayout) findViewById(R.id.Original);
        this.Original.setOnClickListener(this);
        this.l1_1 = (LinearLayout) findViewById(R.id.l1_1);
        this.l1_1.setOnClickListener(this);
        this.l3_4 = (LinearLayout) findViewById(R.id.l3_4);
        this.l3_4.setOnClickListener(this);
        this.lll_4_3 = (LinearLayout) findViewById(R.id.lll_4_3);
        this.lll_4_3.setOnClickListener(this);
        this.lll_9_16 = (LinearLayout) findViewById(R.id.lll_9_16);
        this.lll_9_16.setOnClickListener(this);
        this.lll_16_9 = (LinearLayout) findViewById(R.id.lll_16_9);
        this.lll_16_9.setOnClickListener(this);
        this.free_1 = (ImageView) findViewById(R.id.free_1);
        this.Original_1 = (ImageView) findViewById(R.id.Original_1);
        this.ll1_1 = (ImageView) findViewById(R.id.ll1_1);
        this.ll3_4 = (ImageView) findViewById(R.id.ll3_4);
        this.ll_4_3 = (ImageView) findViewById(R.id.ll_4_3);
        this.ll_9_16 = (ImageView) findViewById(R.id.ll_9_16);
        this.ll_16_9 = (ImageView) findViewById(R.id.ll_16_9);
        this.t_free = (TextView) findViewById(R.id.t_free);
        this.t_Original = (TextView) findViewById(R.id.t_Original);
        this.t1_1 = (TextView) findViewById(R.id.t1_1);
        this.t3_4 = (TextView) findViewById(R.id.t3_4);
        this.tv_4_3 = (TextView) findViewById(R.id.tv_4_3);
        this.tv_9_16 = (TextView) findViewById(R.id.tv_9_16);
        this.tv_16_9 = (TextView) findViewById(R.id.tv_16_9);
        Set_Text_Color();
        this.free_1.setColorFilter(getResources().getColor(R.color.crop_op_bg));
        this.t_free.setTextColor(getResources().getColor(R.color.crop_op_bg));
        this.cropimageview.setCropMode(CropImageView.CropMode.FREE);
        this.ic_done = (ImageView) findViewById(R.id.buttonDone);
        this.ic_done.setOnClickListener(this);
        this.ic_back = (ImageView) findViewById(R.id.buttonCancel);
        this.ic_back.setOnClickListener(this);
        this.rotate_left = (ImageView) findViewById(R.id.buttonRotateLeft);
        this.rotate_left.setOnClickListener(this);
        this.rotate_right = (ImageView) findViewById(R.id.buttonRotateRight);
        this.rotate_right.setOnClickListener(this);
    }

    private void Set_Text_Color() {
        this.free_1.setColorFilter(null);
        this.Original_1.setColorFilter(null);
        this.ll1_1.setColorFilter(null);
        this.ll3_4.setColorFilter(null);
        this.ll_4_3.setColorFilter(null);
        this.ll_9_16.setColorFilter(null);
        this.ll_16_9.setColorFilter(null);
        this.t_free.setTextColor(getResources().getColor(R.color.white));
        this.t_Original.setTextColor(getResources().getColor(R.color.white));
        this.t1_1.setTextColor(getResources().getColor(R.color.white));
        this.t3_4.setTextColor(getResources().getColor(R.color.white));
        this.tv_4_3.setTextColor(getResources().getColor(R.color.white));
        this.tv_9_16.setTextColor(getResources().getColor(R.color.white));
        this.tv_16_9.setTextColor(getResources().getColor(R.color.white));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_crop);
        Initializ_All();
        getWindow().setFlags(1024,1024);
        this.cropimageview.setImageBitmap(StartActivity.photo);
    }


    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.buttonCancel :
                break;
            case R.id.buttonDone :
                a_image = this.cropimageview.getCroppedBitmap();
                Intent intent = new Intent(this, Edit_Activity.class);
                startActivity(intent);
            default:
                switch (id) {
                    case R.id.buttonRotateLeft :
                        cropimageview.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                        break;
                    case R.id.buttonRotateRight :
                        cropimageview.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                        break;
                    default:
                        switch (id) {
                            case R.id.lll_16_9 :
                                Set_Text_Color();
                                this.ll_16_9.setColorFilter(getResources().getColor(R.color.crop_op_bg));
                                this.tv_16_9.setTextColor(getResources().getColor(R.color.crop_op_bg));
                                cropimageview.setCropMode(CropImageView.CropMode.RATIO_16_9);
                                break;
                            case R.id.l1_1 :
                                Set_Text_Color();
                                this.ll1_1.setColorFilter(getResources().getColor(R.color.crop_op_bg));
                                this.t1_1.setTextColor(getResources().getColor(R.color.crop_op_bg));
                                cropimageview.setCropMode(CropImageView.CropMode.SQUARE);
                                break;
                            case R.id.l3_4 :
                                Set_Text_Color();
                                this.ll3_4.setColorFilter(getResources().getColor(R.color.crop_op_bg));
                                this.t3_4.setTextColor(getResources().getColor(R.color.crop_op_bg));
                                cropimageview.setCropMode(CropImageView.CropMode.RATIO_3_4);
                                break;
                            case R.id.lll_4_3 :
                                Set_Text_Color();
                                this.ll_4_3.setColorFilter(getResources().getColor(R.color.crop_op_bg));
                                this.tv_4_3.setTextColor(getResources().getColor(R.color.crop_op_bg));
                                cropimageview.setCropMode(CropImageView.CropMode.RATIO_4_3);
                                break;
                            case R.id.lll_9_16 :
                                Set_Text_Color();
                                this.ll_9_16.setColorFilter(getResources().getColor(R.color.crop_op_bg));
                                this.tv_9_16.setTextColor(getResources().getColor(R.color.crop_op_bg));
                                cropimageview.setCropMode(CropImageView.CropMode.RATIO_9_16);

                                break;
                            case R.id.Original :
                                Set_Text_Color();
                                this.Original_1.setColorFilter(getResources().getColor(R.color.crop_op_bg));
                                this.t_Original.setTextColor(getResources().getColor(R.color.crop_op_bg));
                                cropimageview.setCropMode(CropImageView.CropMode.FIT_IMAGE);

                                break;
                            case R.id.free:
                                Set_Text_Color();
                                this.free_1.setColorFilter(getResources().getColor(R.color.crop_op_bg));
                                this.t_free.setTextColor(getResources().getColor(R.color.crop_op_bg));
                                cropimageview.setCropMode(CropImageView.CropMode.FREE);
                                break;
                            default:
                                return;
                        }
                        return;
                }
                return;
        }
        finish();
    }

}
