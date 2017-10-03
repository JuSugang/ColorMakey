package com.example.tnrkd.colormakey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tnrkd.colormakey.R;
import com.example.tnrkd.colormakey.dto.Color;

import java.util.ArrayList;

/**
 * Created by XNOTE on 2017-10-02.
 */

public class ColorGridPaletteAdapter extends BaseAdapter {

    Context context;
    int layout;
    LayoutInflater inf;
    ArrayList<Color> colorIDs = null;

    public ColorGridPaletteAdapter(Context context, int layout, ArrayList<Color> colorIDs) {
        this.context = context;
        this.layout = layout;
        this.colorIDs = colorIDs;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return colorIDs.size();
    }
    @Override
    public Object getItem(int position) {
        return colorIDs.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = inf.inflate(layout, null);
        }
        ImageView colorView = (ImageView)convertView.findViewById(R.id.imageView1);
        TextView colorHexCodeTextView = (TextView)convertView.findViewById(R.id.colorName);

        int R=colorIDs.get(position).getRGBarray()[0];
        int G=colorIDs.get(position).getRGBarray()[1];
        int B=colorIDs.get(position).getRGBarray()[2];
        colorView.setImageResource(com.example.tnrkd.colormakey.R.drawable.mask);
        colorView.setBackgroundColor(android.graphics.Color.rgb(R,G,B));
        colorHexCodeTextView.setText("#" + colorIDs.get(position).getHexcode());

        return convertView;
    }
}