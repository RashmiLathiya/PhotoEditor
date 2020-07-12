package com.candycam.photoeditor;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.candycam.photoeditor.CropImage.CropActivity;
import com.candycam.photoeditor.Effect.Effect_Activity;
import com.candycam.photoeditor.Sticker.StickerAdapter;
import com.candycam.photoeditor.Sticker.StickerView;
import com.candycam.photoeditor.Text.CardFontStyleAdapter;
import com.candycam.photoeditor.Text.CustomTextView;
import com.candycam.photoeditor.jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Edit_Activity extends AppCompatActivity {

    private static Bitmap load_bitmap;
    private static Canvas load_canvas;
    ImageView image_gallery,Effect_button,text_button,sticker_button,Adjust_button,save;
    public static Bitmap save_bitmap;
    HorizontalListView effect_list;
    SeekBar brightness;
    TextView seek_value;
    GridView grid_sticker;
    StickerAdapter stickerAdapter;
    ArrayList<Integer> arrayList;
    FrameLayout frameLayout;
    ArrayList<View> mviews;
    EditText Add_Text;
    ImageView keyborad,font,color,align,done,align_left,align_right,align_center;
    ImageView st1,st2,st3,st4,st5;
    LinearLayout l_font_list,l_color_list,l_grid_sticker,all_button;
    InputMethodManager inputMethodManager;
    HorizontalListView font_list,color_list;
    Typeface type;
    String[] s = {"font1.ttf", "font2.ttf", "font3.ttf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf", "font8.ttf", "font9.ttf", "font11.ttf", "font12.ttf", "font13.ttf", "font49.ttf", "font50.ttf"};
    RelativeLayout align_list;
    private Dialog dialog;
    private Bitmap textBitmap;
    StickerView Sticker_View;
    int i=-1;
    Boolean effct_b=true;
    Boolean stick_b,adj_b,text_b;
    ImageView mImageView;
    public static String Folder_name="candy camera";
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private CustomTextView mCurrentTextView;
    public ArrayList<View> mStickers;
    public static String urlImage;
    private String m_Text=" ";
    public static Bitmap bitmap_img;
    public static boolean Edit_photo=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_);
        getWindow().setFlags(1024,1024);

        mImageView=findViewById(R.id.gpuimage);
        all_button=findViewById(R.id.all_button);
        if (Edit_photo==true){
                mImageView.setImageBitmap(Effect_Activity.bitmap);
        }else {
            bitmap_img=CropActivity.a_image;
            mImageView.setImageBitmap(bitmap_img);
        }

        mviews=new ArrayList<>();
        mStickers = new ArrayList<>();
        effect_list=findViewById(R.id.effect_list);
        grid_sticker=findViewById(R.id.grid_sticker);
        brightness=findViewById(R.id.brightness);
        seek_value=findViewById(R.id.seek_value);
        frameLayout=findViewById(R.id.framelayout);
        text_button=findViewById(R.id.text);
        sticker_button=findViewById(R.id.stiker);
        l_grid_sticker=findViewById(R.id.l_grid_sticker);
        Effect_button=findViewById(R.id.effect);
        Adjust_button=findViewById(R.id.adjust);
        save=findViewById(R.id.save);

        Effect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Edit_Activity.this, Effect_Activity.class);
                startActivity(intent);
            }
        });

        sticker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l_grid_sticker.getVisibility()==View.GONE){
                    effect_list.setVisibility(View.GONE);
                    brightness.setVisibility(View.GONE);
                    seek_value.setVisibility(View.GONE);
                    l_grid_sticker.setVisibility(View.VISIBLE);


                }else {
                    l_grid_sticker.setVisibility(View.GONE);
                }
                Add_Sticker();

            }
        });

        Adjust_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seek_value.getVisibility()==View.GONE || brightness.getVisibility()==View.GONE){
                    effect_list.setVisibility(View.GONE);
                    brightness.setVisibility(View.VISIBLE);
                    seek_value.setVisibility(View.VISIBLE);
                    l_grid_sticker.setVisibility(View.GONE);
                }else {
                    brightness.setVisibility(View.GONE);
                    seek_value.setVisibility(View.GONE);
                }

            }
        });

        text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(text_b=true){
                    effect_list.setVisibility(View.GONE);
                    brightness.setVisibility(View.GONE);
                    seek_value.setVisibility(View.GONE);
                    l_grid_sticker.setVisibility(View.GONE);
                }else {
                    effect_list.setVisibility(View.VISIBLE);
                    brightness.setVisibility(View.VISIBLE);
                    seek_value.setVisibility(View.VISIBLE);
                    l_grid_sticker.setVisibility(View.VISIBLE);
                }
                Text_Dialog_Box();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_bitmap = getMainFrameBitmap();
                saveImage(Edit_Activity.this.save_bitmap);
                Intent intent=new Intent(Edit_Activity.this,ShareActivity.class);
                startActivity(intent);

                Toast.makeText(Edit_Activity.this, "image saved", Toast.LENGTH_SHORT).show();
            }
        });

        Add_Brightness();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Edit_Activity.this,StartActivity.class);
        startActivity(intent);
        finish();
    }

    private void Text_Dialog_Box() {

            dialog = new Dialog(Edit_Activity.this);
            dialog.requestWindowFeature(1);
            dialog.setContentView(R.layout.activity_text);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Add_Text=dialog.findViewById(R.id.edittext);
            keyborad=dialog.findViewById(R.id.iv_keyboard);
            font=dialog.findViewById(R.id.iv_fontstyle);
            color=dialog.findViewById(R.id.iv_color);
            align=dialog.findViewById(R.id.iv_gravity);
            l_font_list=dialog.findViewById(R.id.lyfontlist);
            l_color_list=dialog.findViewById(R.id.lycolorlist);
            font_list=dialog.findViewById(R.id.gvfontlist);
            color_list=dialog.findViewById(R.id.gvcolorlist);
            align_list=dialog.findViewById(R.id.align_list);
            align_left=dialog.findViewById(R.id.align_left);
            align_center=dialog.findViewById(R.id.align_center);
            align_right=dialog.findViewById(R.id.align_right);
            final TextView textView2 = (TextView) dialog.findViewById(R.id.txtEnteredText);
            textView2.setDrawingCacheEnabled(true);
            done=dialog.findViewById(R.id.iv_done);


            final TextView textView = new TextView(Edit_Activity.this);
            inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ArrayList HSVColors = HSVColors();
            final ArrayList arrayList = HSVColors;

            keyborad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l_color_list.setVisibility(View.GONE);
                    l_font_list.setVisibility(View.GONE);
                    align_list.setVisibility(View.GONE);
                    inputMethodManager.showSoftInput(Add_Text,0);
                    Add_Text.requestFocus();
                }
            });


            font.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l_font_list.setVisibility(View.VISIBLE);
                    l_color_list.setVisibility(View.GONE);
                    align_list.setVisibility(View.GONE);
                    inputMethodManager.hideSoftInputFromWindow(Add_Text.getWindowToken(),0);
                }
            });
            font_list.setAdapter(new CardFontStyleAdapter(Edit_Activity.this, s));
            font_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    type = Typeface.createFromAsset(getAssets(), s[i]);
                    Add_Text.setTypeface(type);
                    textView.setTypeface(type);
                }
            });
            color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l_color_list.setVisibility(View.VISIBLE);
                    l_font_list.setVisibility(View.GONE);
                    align_list.setVisibility(View.GONE);
                    inputMethodManager.hideSoftInputFromWindow(Add_Text.getWindowToken(),0);
                }
            });
            color_list.setDividerWidth(20);
            color_list.setAdapter(new ArrayAdapter<Integer>(getApplicationContext(), 17367043, HSVColors) {
                public View getView(int i, View view, ViewGroup viewGroup) {
                    TextView textView = (TextView) super.getView(i, view, viewGroup);
                    textView.setBackgroundColor(((Integer) arrayList.get(i)).intValue());
                    textView.setText("");
                    textView.setLayoutParams(new ListView.LayoutParams(-1, -1));
                    ListView.LayoutParams layoutParams = (ListView.LayoutParams) textView.getLayoutParams();
                    layoutParams.width = 70;
                    layoutParams.height = 130;
                    textView.setLayoutParams(layoutParams);
                    textView.requestLayout();
                    return textView;
                }
            });
            color_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    i = (Integer) parent.getItemAtPosition(position);
                    Add_Text.setTextColor(i);
                    textView.setTextColor(i);
                }
            });

            align.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    align_list.setVisibility(View.VISIBLE);
                    l_color_list.setVisibility(View.GONE);
                    l_font_list.setVisibility(View.GONE);
                    inputMethodManager.hideSoftInputFromWindow(Add_Text.getWindowToken(),0);

                    align_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Add_Text.setGravity(Gravity.LEFT);
                        }
                    });
                    align_center.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Add_Text.setGravity(Gravity.CENTER);
                        }
                    });

                    align_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Add_Text.setGravity(Gravity.RIGHT);
                        }
                    });

                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                public void onClick(View view) {
                    inputMethodManager.hideSoftInputFromWindow(Add_Text.getWindowToken(),0);
                    String string = Add_Text.getText().toString();
                    if (!string.isEmpty()) {
                        textView2.setText(string);
                        textView2.setTypeface(type);
                        textView2.setTextColor(i);
                        textView2.setGravity(17);
                        ImageView imageView = new ImageView(Edit_Activity.this);
                        textView2.buildDrawingCache();
                        imageView.setImageBitmap(textView2.getDrawingCache());
                        textBitmap = loadBitmapFromView(imageView);
                        Bitmap textBitmap1 = add_bitmap(textBitmap);
                        textView2.setDrawingCacheEnabled(false);
                        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(Add_Text.getWindowToken(), 0);
                        final CustomTextView customTextView = new CustomTextView(Edit_Activity.this);
                        customTextView.setBitmap(textBitmap1);
                        frameLayout.addView(customTextView, new FrameLayout.LayoutParams(-1, -1, 17));
                        mviews.add(customTextView);
                        customTextView.setInEdit(true);
                        setCurrentEditForText(customTextView);
                        customTextView.setOperationListener(new CustomTextView.OperationListener() {
                            public void onDeleteClick() {
                                mviews.remove(customTextView);
                                frameLayout.removeView(customTextView);
                            }
                            public void onEdit(CustomTextView customTextView) {
                                    mCurrentTextView.setInEdit(false);
                                    mCurrentTextView = customTextView;
                                    mCurrentTextView.setInEdit(true);
                            }

                            public void onTop(CustomTextView customTextView) {
                                    int indexOf = mStickers.indexOf(customTextView);
                                    if (indexOf != mStickers.size() - 1) {
                                        mStickers.add(mStickers.size(), (CustomTextView) mStickers.remove(indexOf));
                                 }
                            }
                        });
                        dialog.dismiss();
                        return;
                    }
                    Toast.makeText(Edit_Activity.this, "text empty", 0).show();
                }
            });

            dialog.show();
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -1;
        window.setAttributes(layoutParams);

    }

    public void setCurrentEditForText(CustomTextView customTextView) {
        if (mCurrentTextView != null) {
            mCurrentTextView.setInEdit(false);
        }
        mCurrentTextView = customTextView;
        customTextView.setInEdit(true);
    }

    private void Add_Brightness() {
        brightness.setMax(200);
        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String s= String.valueOf(progress);
                seek_value.setText(s);

                mImageView.setColorFilter(setBrightness(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    private void Add_Sticker() {

            arrayList=new ArrayList<>();
            arrayList.add(R.drawable.s1);
            arrayList.add(R.drawable.s2);
            arrayList.add(R.drawable.s3);
            arrayList.add(R.drawable.s4);
            arrayList.add(R.drawable.s5);
            arrayList.add(R.drawable.s6);
            arrayList.add(R.drawable.s7);
            arrayList.add(R.drawable.s8);
            arrayList.add(R.drawable.s9);
            arrayList.add(R.drawable.s10);
            arrayList.add(R.drawable.s11);
            arrayList.add(R.drawable.s12);
            arrayList.add(R.drawable.s13);
            arrayList.add(R.drawable.s14);
            arrayList.add(R.drawable.s15);
            arrayList.add(R.drawable.s16);
            arrayList.add(R.drawable.s17);
            arrayList.add(R.drawable.s18);
            arrayList.add(R.drawable.s19);
            arrayList.add(R.drawable.s20);
            arrayList.add(R.drawable.s21);
            arrayList.add(R.drawable.s22);
            arrayList.add(R.drawable.s23);
            arrayList.add(R.drawable.s24);
            arrayList.add(R.drawable.s25);
            arrayList.add(R.drawable.s26);
            arrayList.add(R.drawable.s27);
            arrayList.add(R.drawable.s28);
            arrayList.add(R.drawable.s29);
            arrayList.add(R.drawable.s30);
            arrayList.add(R.drawable.s31);
            arrayList.add(R.drawable.s32);
            arrayList.add(R.drawable.s33);
            arrayList.add(R.drawable.s34);
            arrayList.add(R.drawable.s35);
            arrayList.add(R.drawable.s36);
            arrayList.add(R.drawable.s37);
            arrayList.add(R.drawable.s38);
            arrayList.add(R.drawable.s39);
            arrayList.add(R.drawable.s40);
            arrayList.add(R.drawable.s41);
            arrayList.add(R.drawable.s42);
            arrayList.add(R.drawable.s43);
            arrayList.add(R.drawable.s44);
            arrayList.add(R.drawable.s45);
            arrayList.add(R.drawable.s46);
            arrayList.add(R.drawable.s47);
            arrayList.add(R.drawable.s48);
            arrayList.add(R.drawable.s49);
            arrayList.add(R.drawable.s50);
            arrayList.add(R.drawable.s51);
            arrayList.add(R.drawable.s52);
            arrayList.add(R.drawable.s53);
            arrayList.add(R.drawable.s54);
            arrayList.add(R.drawable.s55);
            arrayList.add(R.drawable.s56);
            arrayList.add(R.drawable.s57);
            arrayList.add(R.drawable.s58);
        arrayList.add(R.drawable.v13);
        arrayList.add(R.drawable.v14);
        arrayList.add(R.drawable.v12);
        arrayList.add(R.drawable.v15);
            arrayList.add(R.drawable.s59);
            arrayList.add(R.drawable.s60);
            arrayList.add(R.drawable.s61);
            arrayList.add(R.drawable.s62);
            arrayList.add(R.drawable.s63);
            arrayList.add(R.drawable.s64);
            arrayList.add(R.drawable.s65);
            arrayList.add(R.drawable.s66);
            arrayList.add(R.drawable.f1);
            arrayList.add(R.drawable.f2);
            arrayList.add(R.drawable.f3);
            arrayList.add(R.drawable.f4);
            arrayList.add(R.drawable.f5);
            arrayList.add(R.drawable.f6);
            arrayList.add(R.drawable.f7);
            arrayList.add(R.drawable.f8);
            arrayList.add(R.drawable.v5);
            arrayList.add(R.drawable.v1);
            arrayList.add(R.drawable.v2);
            arrayList.add(R.drawable.v3);
            arrayList.add(R.drawable.v4);
            arrayList.add(R.drawable.v6);
            arrayList.add(R.drawable.v7);
            arrayList.add(R.drawable.v8);
            arrayList.add(R.drawable.v9);
            arrayList.add(R.drawable.v10);
            arrayList.add(R.drawable.v11);
            arrayList.add(R.drawable.v18);
            arrayList.add(R.drawable.v19);
            arrayList.add(R.drawable.v20);
            arrayList.add(R.drawable.v21);
            arrayList.add(R.drawable.v22);
            arrayList.add(R.drawable.v23);
            arrayList.add(R.drawable.v24);
            arrayList.add(R.drawable.v25);
            arrayList.add(R.drawable.v26);
            arrayList.add(R.drawable.v27);
            arrayList.add(R.drawable.v28);
            arrayList.add(R.drawable.v29);
            arrayList.add(R.drawable.v30);
            arrayList.add(R.drawable.v31);
        arrayList.add(R.drawable.p1);
        arrayList.add(R.drawable.p2);
        arrayList.add(R.drawable.p3);
        arrayList.add(R.drawable.p4);
        arrayList.add(R.drawable.p5);
        arrayList.add(R.drawable.p6);
        arrayList.add(R.drawable.p7);
        arrayList.add(R.drawable.p8);
        arrayList.add(R.drawable.p9);
        arrayList.add(R.drawable.p10);
        arrayList.add(R.drawable.p11);
        arrayList.add(R.drawable.p12);
        arrayList.add(R.drawable.p13);
        arrayList.add(R.drawable.p14);
        arrayList.add(R.drawable.p15);
        arrayList.add(R.drawable.p16);
        arrayList.add(R.drawable.p17);
        arrayList.add(R.drawable.p18);
        arrayList.add(R.drawable.p19);
        arrayList.add(R.drawable.p20);
        arrayList.add(R.drawable.p21);
        arrayList.add(R.drawable.p22);
        arrayList.add(R.drawable.p23);
        arrayList.add(R.drawable.p24);
        arrayList.add(R.drawable.p25);
        arrayList.add(R.drawable.p26);
        arrayList.add(R.drawable.p27);
        arrayList.add(R.drawable.p28);
        arrayList.add(R.drawable.p29);
        arrayList.add(R.drawable.p30);
        arrayList.add(R.drawable.p31);
        arrayList.add(R.drawable.p32);
        arrayList.add(R.drawable.p33);
        arrayList.add(R.drawable.p34);
        arrayList.add(R.drawable.p35);
            arrayList.add(R.drawable.b1);
            arrayList.add(R.drawable.b2);
            arrayList.add(R.drawable.b3);
            arrayList.add(R.drawable.b4);
            arrayList.add(R.drawable.b5);
            arrayList.add(R.drawable.b6);
            arrayList.add(R.drawable.b7);
            arrayList.add(R.drawable.b8);
            arrayList.add(R.drawable.b9);
            arrayList.add(R.drawable.b10);
            arrayList.add(R.drawable.b11);
            arrayList.add(R.drawable.b12);
            arrayList.add(R.drawable.b13);
            arrayList.add(R.drawable.b14);
            arrayList.add(R.drawable.b15);
            arrayList.add(R.drawable.b16);
            arrayList.add(R.drawable.b17);
            arrayList.add(R.drawable.b18);
            arrayList.add(R.drawable.b19);
            arrayList.add(R.drawable.b20);
            arrayList.add(R.drawable.b21);
            arrayList.add(R.drawable.b22);
            arrayList.add(R.drawable.b23);
            arrayList.add(R.drawable.b24);
            arrayList.add(R.drawable.b25);
            arrayList.add(R.drawable.b26);
            arrayList.add(R.drawable.b27);
            arrayList.add(R.drawable.b28);
            arrayList.add(R.drawable.b29);
            arrayList.add(R.drawable.b30);

        stickerAdapter=new StickerAdapter(this,arrayList);
        grid_sticker.setAdapter(stickerAdapter);
        grid_sticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                addStickerView(arrayList.get(i));
            }
        });
    }


    public void addStickerView(int i) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setImageResource(i);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            public void onDeleteClick() {
                mviews.remove(stickerView);
                frameLayout.removeView(stickerView);
            }

            public void onEdit(StickerView stickerView) {
                Sticker_View.setInEdit(false);
                Sticker_View = stickerView;
                Sticker_View.setInEdit(true);
            }

            public void onTop(StickerView stickerView) {
                int indexOf = mviews.indexOf(stickerView);
                if (indexOf != mviews.size() - 1) {
                    mviews.add(mviews.size(), (StickerView) mviews.remove(indexOf));
                }
            }
        });
        this.frameLayout.addView(stickerView, new RelativeLayout.LayoutParams(-1, -1));
        this.mviews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (this.Sticker_View != null) {
            this.Sticker_View.setInEdit(false);
        }
        this.Sticker_View = stickerView;
        stickerView.setInEdit(true);
    }

    public static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >=    100)
        {
            int value = (int) (progress-100) * 255 / 100;

            return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);

        }
        else
        {
            int value = (int) (100-progress) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);


        }
    }

    public static ArrayList HSVColors() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i <= 360; i += 20) {
            arrayList.add(Integer.valueOf(HSVColor((float) i, 1.0f, 1.0f)));
        }
        for (int i2 = 0; i2 <= 360; i2 += 20) {
            arrayList.add(Integer.valueOf(HSVColor((float) i2, 0.25f, 1.0f)));
            arrayList.add(Integer.valueOf(HSVColor((float) i2, 0.5f, 1.0f)));
            arrayList.add(Integer.valueOf(HSVColor((float) i2, 0.75f, 1.0f)));
        }
        for (int i3 = 0; i3 <= 360; i3 += 20) {
            arrayList.add(Integer.valueOf(HSVColor((float) i3, 1.0f, 0.5f)));
            arrayList.add(Integer.valueOf(HSVColor((float) i3, 1.0f, 0.75f)));
        }
        for (float f = 0.0f; f <= 1.0f; f += 0.1f) {
            arrayList.add(Integer.valueOf(HSVColor(0.0f, 0.0f, f)));
        }
        return arrayList;
    }

    public static int HSVColor(float f, float f2, float f3) {
        return Color.HSVToColor(255, new float[]{f, f2, f3});
    }

    public static Bitmap loadBitmapFromView(View view) {
        if (view.getMeasuredHeight() <= 0) {
            view.measure(-2, -2);
            load_bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            load_canvas = new Canvas(load_bitmap);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.draw(load_canvas);
            return load_bitmap;
        }
        load_bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        load_canvas = new Canvas(load_bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(load_canvas);
        return load_bitmap;
    }

    public Bitmap add_bitmap(Bitmap bitmap2) {
        int a;
        int b;
        int c;
        int d;
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        int get_width = width;
        int int_a = -1;
        int int_b = -1;
        int int_c = 0;
        while (int_c < bitmap2.getHeight()) {
            int int_d = 0;
            while (int_d < bitmap2.getWidth()) {
                if (((bitmap2.getPixel(int_d, int_c) >> 24) & 255) > 0) {
                    a = int_d < get_width ? int_d : get_width;
                    if (int_d > int_a) {
                        int_a = int_d;
                    }
                    b = int_c < height ? int_c : height;
                    if (int_c > int_b) {
                        c = int_a;
                        d = int_c;
                    } else {
                        c = int_a;
                        d = int_b;
                    }
                } else {
                    a = get_width;
                    b = height;
                    c = int_a;
                    d = int_b;
                }
                int_d++;
                int_b = d;
                int_a = c;
                height = b;
                get_width = a;
            }
            int_c++;
        }
        if (int_a < get_width || int_b < height) {
            return null;
        }
        return Bitmap.createBitmap(bitmap2, get_width, height, (int_a - get_width) + 1, (int_b - height) + 1);
    }

    public void saveImage(Bitmap bitmap2) {
        Log.v("TAG", "saveImageInCache is called");
        File externalStorageDirectory = Environment.getExternalStorageDirectory();

        File file = new File(externalStorageDirectory.getAbsolutePath() + "/" + Folder_name);
        file.mkdirs();
        String str = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpeg";
        File file2 = new File(file, str);
        file2.renameTo(file2);
        String str2 = "file://" + externalStorageDirectory.getAbsolutePath() + "/" + Folder_name + "/" + str;
        urlImage = externalStorageDirectory.getAbsolutePath() + "/" + Folder_name + "/" + str;
        Log.d("cache uri=", str2);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(str2))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Bitmap getMainFrameBitmap() {
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.setBitmap(createBitmap);
        canvas.drawColor(0);
        frameLayout.draw(new Canvas(createBitmap));
        return createBitmap;
    }

}
