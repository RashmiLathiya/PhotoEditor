package com.candycam.photoeditor.Sticker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.candycam.photoeditor.R;

import java.util.ArrayList;

public class StickerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Integer> arrayList;
    ImageView imageView;
    private ViewHolder viewholder;

    public class ViewHolder {
        public ViewHolder() {
        }
    }

    public StickerAdapter(Context context, ArrayList<Integer> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public int getCount() {
        return this.arrayList.size();
    }

    public Object getItem(int i) {
        return this.arrayList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.sticker_activity, null);
        }
        this.imageView = (ImageView) view.findViewById(R.id.img);
        Glide.with(this.context).load(Integer.valueOf(((Integer) this.arrayList.get(i)).intValue())).into(this.imageView);
        return view;
    }
}
