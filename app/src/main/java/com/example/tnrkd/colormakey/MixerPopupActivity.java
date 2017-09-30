package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by tnrkd on 2017-09-30.
 */
class colorInfo{
    private String hexcode;
    private String rgbcode;
    private String colorname;
    colorInfo(String hexcode,String rgbcode,String colorname){
        this.hexcode=hexcode;
        this.rgbcode=rgbcode;
        this.colorname=colorname;
    }
    public int[] getRGBarray(){
        int temp = Integer.parseInt(rgbcode);
        int[] rgb=new int[3];
        rgb[0]=temp/1000000;
        rgb[1]=temp%1000000/1000;
        rgb[2]=temp%1000;
        return rgb;
    }
    public String getHexcode(){
        return hexcode;
    }
    public String getColorname(){
        return  colorname;
    }
}

class colorGridAdapter extends BaseAdapter {
    Context context;
    int layout;
    int img;
    LayoutInflater inf;
    ArrayList<colorInfo> colorIDs = null;

    public colorGridAdapter(Context context, int layout, ArrayList<colorInfo> colorIDs) {
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
        TextView colortext = (TextView) convertView.findViewById(R.id.colorName);
        colortext.setText(colorIDs.get(position).getColorname());
        int R=colorIDs.get(position).getRGBarray()[0];
        int G=colorIDs.get(position).getRGBarray()[1];
        int B=colorIDs.get(position).getRGBarray()[2];
        colorView.setImageResource(com.example.tnrkd.colormakey.R.drawable.mask);
        colorView.setBackgroundColor(Color.rgb(R,G,B));
//            colorView.setOnClickListener(new ImageClickListener(context, colorIDs[position]));

        return convertView;
    }
}

public class MixerPopupActivity extends Activity {
    ArrayList<colorInfo> basicColor= new ArrayList<colorInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mixer_popup);

//-------------------------color text 읽기-------------------------------------------------
        try{                //res/raw/txt파일에서 색 hexcode,rgbcode,name 을 불러와 Arraylist에 담는다.
            // txt 파일을 InpuStream에 넣는다. (open 한다)
            InputStream in = getResources().openRawResource(R.raw.color_basic);
            if(in != null){
                InputStreamReader stream = new InputStreamReader(in, "utf-8");
                BufferedReader buffer = new BufferedReader(stream);

                String read;
                while((read=buffer.readLine())!=null){
                    String[] colorInfoArr=read.split(",");
                    colorInfo node=new colorInfo(colorInfoArr[0],colorInfoArr[1],colorInfoArr[2]);
                    basicColor.add(node);   //basic 색 만 테스트
                }
                in.close();

            }
        }catch(Exception e) {
            e.printStackTrace();
        }

//-------------------------tabHost 구성-------------------------------------------------
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost1);    //tabhost 생성
        tabHost.setup();
        String[] tabName = {"Basic","Red","Green","Blue","Yellow","Brown","Grey","Orange","Violet","직접선택"};
        int[] tabID = {R.id.tab0,R.id.tab1,R.id.tab2,R.id.tab3,R.id.tab4,R.id.tab5,R.id.tab6,R.id.tab7,R.id.tab8,R.id.tab9};
        String[] tabkey =  {"0","1","2","3","4","5","6","7","8","9"};
        for (int i=0;i<10;i++){
            tabHost.addTab(tabHost.newTabSpec(tabkey[i]).setContent(tabID[i]).setIndicator(tabName[i]));  //tabSpec을 tabHost에 추가해준다.
        }
//-------------------------컬러 이미지 생성-------------------------------------------------

        GridView tabArea = (GridView)findViewById(R.id.grid0);
        colorGridAdapter colorAdapter = new colorGridAdapter(getApplicationContext(),R.layout.row, basicColor);

        tabArea.setAdapter(colorAdapter);

        TextView tv0 = (TextView) findViewById(R.id.colpal0);
        tv0.setText(basicColor.get(0).getColorname());
    }
}
