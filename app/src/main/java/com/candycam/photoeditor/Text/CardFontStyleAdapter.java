package com.candycam.photoeditor.Text;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.candycam.photoeditor.R;

public class CardFontStyleAdapter extends BaseAdapter {
    private Context context;
    private final String[] mobileValues;

    static class RecordHolder {
        TextView a;
        Typeface b;

        RecordHolder() {
        }
    }

    public CardFontStyleAdapter(Context context2, String[] strArr) {
        this.context = context2;
        this.mobileValues = strArr;
    }

    public int getCount() {
        return this.mobileValues.length;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordHolder recordHolder;
        if (view == null) {
            view = ((Activity) this.context).getLayoutInflater().inflate(R.layout.fontstyle, viewGroup, false);
            RecordHolder recordHolder2 = new RecordHolder();
            recordHolder2.a = (TextView) view.findViewById(R.id.img);
            view.setTag(recordHolder2);
            recordHolder = recordHolder2;
        } else {
            recordHolder = (RecordHolder) view.getTag();
        }
        recordHolder.b = Typeface.createFromAsset(this.context.getAssets(), this.mobileValues[i]);
        recordHolder.a.setTypeface(recordHolder.b);
        return view;
    }
}
