package com.candycam.photoeditor.Effect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.candycam.photoeditor.R;

import java.util.ArrayList;

public class Smoke_Adapter extends BaseAdapter {
    private ArrayList<Integer> effectList = new ArrayList<>();
    private Context mContext;
    int val;


    static class Viewholder {
        ImageView img,select;
            TextView img_name;
        Viewholder() {
        }
    }

    public Smoke_Adapter(Context context, ArrayList<Integer> arrayList) {
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
        return view;
    }
}
