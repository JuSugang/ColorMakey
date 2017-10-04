package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
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
                MixerActivity.calcResult(MixerActivity.colorTexture);
            }
        });
//        holder.mback.setTag(position);
        holder.mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorList target = Global.list.get(index);
                target.upRatio();
                MixerActivity.mAdapter.notifyDataSetChanged();
                MixerActivity.calcResult(MixerActivity.colorTexture);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


public class MixerActivity extends Activity {
    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    public static RecyclerView.LayoutManager mLayoutManager;
    public static ImageView colorTexture;
    public static void calcResult(ImageView colorTexture){

        int[] rgb = {0,0,0};
        int sum=0;
        for(int j=0 ; j<Global.list.size() ; j++){
            rgb[0]+=Global.list.get(j).R*Global.list.get(j).Ratio;
            rgb[1]+=Global.list.get(j).G*Global.list.get(j).Ratio;
            rgb[2]+=Global.list.get(j).B*Global.list.get(j).Ratio;
            sum+=Global.list.get(j).Ratio;
        }
        if(sum!=0) {
            for (int i = 0; i < 3; i++) {
                rgb[i] /= sum;
            }
        }
        if(Global.list.size()==0)
            colorTexture.setBackgroundColor(Color.rgb(255,255,255));
        else
            colorTexture.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixer);

//-------------------------색 결과 표시-------------------------------------------------
        colorTexture = (ImageView) findViewById(R.id.color_texture);
        colorTexture.setImageResource(R.drawable.mixer_result);
        calcResult(colorTexture);
//-------------------------색 비율 표시해야함-------------------------------------------------

//-------------------------색 추가 버튼-------------------------------------------------
        ImageView addButton=(ImageView) findViewById(R.id.addButton);
        addButton.setImageResource(R.drawable.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MixerActivity.this, MixerPopupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
//-------------------------color 리스트 관리-------------------------------------------------
        mRecyclerView = (RecyclerView) findViewById(R.id.color_horizontalView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(Global.list);
        mRecyclerView.setAdapter(mAdapter);
    }
}


