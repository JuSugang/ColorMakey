package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MixerActivity extends Activity {
    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    public static RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixer);
//-------------------------색 결과 표시-------------------------------------------------
        ImageView colorTexture = (ImageView) findViewById(R.id.color_texture);
        colorTexture.setImageResource(R.drawable.mask);
//-------------------------색 비율 표시-------------------------------------------------
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


