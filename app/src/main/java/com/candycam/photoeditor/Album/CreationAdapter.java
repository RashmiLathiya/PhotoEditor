package com.candycam.photoeditor.Album;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.candycam.photoeditor.R;
import java.util.ArrayList;

public class CreationAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    SparseBooleanArray array;
    ArrayList<String> arrayList = new ArrayList<>();
    public MyCreationActivity dactivity;
    private int imageSize;

    static class ViewHolder {
        ImageView image_list;

        ViewHolder() {
        }
    }

    public interface background {
        void back();
    }

    public CreationAdapter(MyCreationActivity myCreationActivity, ArrayList<String> arrayList) {
        this.dactivity = myCreationActivity;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) this.dactivity.getSystemService("layout_inflater");
        this.array = new SparseBooleanArray(this.arrayList.size());
    }


    public int getCount() {
        return this.arrayList.size();
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        int i2 = this.dactivity.getResources().getDisplayMetrics().widthPixels;
        if (view == null) {
            view = LayoutInflater.from(this.dactivity).inflate(R.layout.list_gallary, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.image_list = (ImageView) view.findViewById(R.id.image_list);
            viewHolder.image_list.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {

                }
            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with((Activity) this.dactivity).load((String) this.arrayList.get(i)).into(viewHolder.image_list);
        System.gc();
        return view;
    }
}
