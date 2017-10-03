package com.example.tnrkd.colormakey;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tnrkd on 2017-10-03.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<colorList> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
        public LinearLayout mback;
        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.deleteButton);
            mTextView = (TextView)view.findViewById(R.id.ratio);
            mback = (LinearLayout)view.findViewById(R.id.back);
        }
    }
    public MyAdapter(ArrayList<colorList> myDataset) {
        mDataset = myDataset;
    }
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_mixer, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(Integer.toString(mDataset.get(position).Ratio));
        final int index=position;
        holder.mImageView.setImageResource(com.example.tnrkd.colormakey.R.drawable.delete_button);
        int R=Global.list.get(position).R;
        int G=Global.list.get(position).G;
        int B=Global.list.get(position).B;
        holder.mback.setBackgroundColor(Color.rgb(R,G,B));
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorList target = Global.list.get(index);
                target.downRatio();
                if(target.Ratio==0) {
                    Global.list.remove(index);
                }
                MixerActivity.mAdapter.notifyDataSetChanged();
            }
        });
//        holder.mback.setTag(position);
        holder.mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorList target = Global.list.get(index);
                target.upRatio();
                MixerActivity.mAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
