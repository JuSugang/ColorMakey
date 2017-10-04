package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tnrkd on 2017-09-29.
 */

class CustomColorAdapter extends BaseAdapter {

    private Context context = null;
    private int layout;
    private LayoutInflater inf = null;
    private ArrayList<colorList> infoList = null;

    public CustomColorAdapter(Context c,  int layout, ArrayList<colorList> arrays){
        this.context = c;
        this.layout=layout;
        this.infoList = arrays;
        this.inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return infoList.size();
    }
    @Override
    public colorList getItem(int position) {
        return infoList.get(position);
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

        TextView colorRatio = (TextView) convertView.findViewById(R.id.ratio);
        ImageView deleteButton = (ImageView)convertView.findViewById(R.id.deleteButton);
        LinearLayout colorBack = (LinearLayout)convertView.findViewById(R.id.back);

        colorRatio.setText(Integer.toString(infoList.get(position).Ratio));
        int R=infoList.get(position).R;
        int G=infoList.get(position).G;
        int B=infoList.get(position).B;
        colorBack.setBackgroundColor(Color.rgb(R,G,B));
        deleteButton.setImageResource(com.example.tnrkd.colormakey.R.drawable.delete_button);

        return convertView;
    }
    public void setArrayList(ArrayList<colorList> arrays){
        this.infoList = arrays;
    }

    public ArrayList<colorList> getArrayList(){
        return infoList;
    }
}

public class MixerActivity extends Activity {
    public static ListView colorListView;
    public static CustomColorAdapter colorAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixer);
<<<<<<< HEAD
//<<<<<<< HEAD
//=======
//-------------------------색 결과 표시-------------------------------------------------
        colorTexture = (ImageView) findViewById(R.id.color_texture);
        colorTexture.setImageResource(R.drawable.mixer_result);
        calcResult(colorTexture);
//-------------------------색 비율 표시해야함-------------------------------------------------
//>>>>>>> sugang
=======

>>>>>>> parent of 51e8f7c... Revert "Merge branch 'master' into pyw"
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
        colorListView = (ListView) findViewById(R.id.MixerColorList);
        colorAdapter=new CustomColorAdapter(getApplicationContext(),R.layout.row_mixer,Global.list);
        colorListView.setAdapter(colorAdapter);
    }
}
