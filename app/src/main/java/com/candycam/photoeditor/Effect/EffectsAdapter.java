package com.candycam.photoeditor.Effect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.candycam.photoeditor.Edit_Activity;
import com.candycam.photoeditor.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EffectsAdapter extends BaseAdapter {
    private ArrayList<Integer> effectList = new ArrayList<>();
    private Context mContext;
    int val;


    static class Viewholder {
        ImageView img,select;
        TextView img_name;
        Viewholder() {
        }
    }

    public EffectsAdapter(Context context, ArrayList<Integer> arrayList) {
        this.mContext = context;
        this.effectList = arrayList;
    }

    public int getCount() {
        return this.effectList.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder viewholder;
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.effect, viewGroup, false);
            viewholder = new Viewholder();
            viewholder.img = (ImageView) view.findViewById(R.id.img);
            viewholder.img_name=view.findViewById(R.id.img_name);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }



        viewholder.img.setImageResource(((Integer) this.effectList.get(i)).intValue());
        if (i == 0) {
            viewholder.img_name.setText("None");
        }
        if (i == 1) {
            viewholder.img_name.setText("Purple");
        }
        if (i == 2) {
            viewholder.img_name.setText("Orchid");
        }
        if (i == 3) {
            viewholder.img_name.setText("Soft Light");
        }
        if (i == 4) {
            viewholder.img_name.setText("Forest");
        }
        if (i == 5) {
            viewholder.img_name.setText("Elegant");
        }
        if (i == 6) {
            viewholder.img_name.setText("Cool");
        }
        if (i == 7) {
            viewholder.img_name.setText("Gray");
        }
        if (i == 8) {
            viewholder.img_name.setText("Retro");
        }
        if (i == 9) {
            viewholder.img_name.setText("Violet");
        }
        if (i == 10) {
            viewholder.img_name.setText("Vivian");
        }
        if (i == 11) {
            viewholder.img_name.setText("Crystal");
        }
        if (i == 12) {
            viewholder.img_name.setText("Noir");
        }
        if (i == 13) {
            viewholder.img_name.setText("Ochre");
        }
        if (i == 14) {
            viewholder.img_name.setText("Gloom");
        }
        if (i == 15) {
            viewholder.img_name.setText("Spring");
        }
        if (i == 16) {
            viewholder.img_name.setText("Gentle");
        }
        if (i == 17) {
            viewholder.img_name.setText("Brown");
        }
        if (i == 18) {
            viewholder.img_name.setText("Fresh");
        }
        if (i == 19) {
            viewholder.img_name.setText("Film");
        }
        if (i == 20) {
            viewholder.img_name.setText("Flor");
        }
        if (i == 21) {
            viewholder.img_name.setText("Shear");
        }
        if (i == 22) {
            viewholder.img_name.setText("Comic");
        }
        if (i == 23) {
            viewholder.img_name.setText("Poster");
        }
        return view;
    }
}
