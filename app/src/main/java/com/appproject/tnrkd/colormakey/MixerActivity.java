package com.appproject.tnrkd.colormakey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private ArrayList<colorList> mDataset;
    Context context;
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
    public MyAdapter(ArrayList<colorList> myDataset, Context context) {
        mDataset = myDataset;
        this.context=context;
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
        final int index=position;
        holder.mImageView.setImageResource(com.appproject.tnrkd.colormakey.R.drawable.delete_button);
        int R=Global.list.get(position).R;
        int G=Global.list.get(position).G;
        int B=Global.list.get(position).B;
        holder.mback.setBackgroundColor(Color.rgb(R,G,B));
        holder.mTextView.setText(Integer.toString(mDataset.get(position).Ratio));
        float[] hsvArr = new float[3];
        Color.RGBToHSV(R, G, B, hsvArr);
        if(hsvArr[2] > 0.6) {
            holder.mTextView.setTextColor(Color.DKGRAY);
        }else {
            holder.mTextView.setTextColor(Color.WHITE);
        }
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorList target = Global.list.get(index);
                target.downRatio();
                if(target.Ratio==0) {
                    Global.list.remove(index);
                }
                MixerActivity.mAdapter.notifyDataSetChanged();
                Intent i=new Intent(context, MixerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });
        holder.mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorList target = Global.list.get(index);
                target.upRatio();
                MixerActivity.mAdapter.notifyDataSetChanged();
                Intent i=new Intent(context, MixerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

public class MixerActivity extends BaseActivity {
    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    public static RecyclerView.LayoutManager mLayoutManager;
    public static ImageView colorTexture;
    public static LinearLayout ratioView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixer);

//        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
//        setToolbar(toolbar);

        ImageView background=(ImageView)findViewById(R.id.mixer_toolbar_round);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        background.getLayoutParams().height=height*42/100;
        background.getLayoutParams().width=width;
        background.requestLayout();

        ImageView bubble = (ImageView) findViewById(R.id.mixer_bubble);
        bubble.getLayoutParams().height=width*170/432;
        bubble.getLayoutParams().width=width;
        bubble.requestLayout();

//-------------------------색 결과 표시-------------------------------------------------
        colorTexture = (ImageView) findViewById(R.id.color_texture);
//-------------------------색 비율 표시-------------------------------------------------
        ratioView = (LinearLayout)findViewById(R.id.ratioView);

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

        mAdapter = new MyAdapter(Global.list,this);
        mRecyclerView.setAdapter(mAdapter);


    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume","resume");
//-------------------------색 결과물 업데이트-------------------------------------------------
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
            colorTexture.setBackgroundColor(Color.rgb(230,230,230));
        else {
            colorTexture.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
            ratioView.setWeightSum(sum);
        }

//-------------------------색 배율 퍼센트계산-------------------------------------------------
        ratioView.removeAllViews();
        for(int i=0;i<Global.list.size();i++) {
            TextView view1 = new TextView(this);
            int percentText=Math.round(Global.list.get(i).Ratio*100/(float)sum);
            view1.setText(Integer.toString(percentText)+"%");
            view1.setTextSize(20);
            int backgroundColor = Color.rgb(Global.list.get(i).R,Global.list.get(i).G,Global.list.get(i).B);
            float[] hsvArr = new float[3];
            Color.RGBToHSV(Global.list.get(i).R, Global.list.get(i).G, Global.list.get(i).B, hsvArr);
            if(hsvArr[2] > 0.6) {
                view1.setTextColor(Color.DKGRAY);
            }else {
                view1.setTextColor(Color.WHITE);
            }
            view1.setBackgroundColor(backgroundColor);
            view1.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = Global.list.get(i).Ratio;
            view1.setLayoutParams(lp);

            //부모 뷰에 추가
            ratioView.addView(view1);
        }
    }
}


