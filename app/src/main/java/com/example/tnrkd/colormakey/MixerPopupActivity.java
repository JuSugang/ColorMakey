package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
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
public class MixerPopupActivity extends Activity {
    ArrayList<colorInfo> basicColor= new ArrayList<colorInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mixer_popup);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost1);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("1").setContent(R.id.tab1).setIndicator("basic");

        TabHost.TabSpec tab2 = tabHost.newTabSpec("2").setContent(R.id.tab2).setIndicator("red");

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        TextView tv = (TextView) findViewById(R.id.colpal);
        TextView tv2 = (TextView) findViewById(R.id.colpal2);
        try{    //res/raw/txt파일에서 색 hexcode,rgbcode,name 을 불러와 Arraylist에 담는다.
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

        tv.setText(basicColor.get(0).getColorname());
        tv2.setText(basicColor.get(4).getColorname());
    }
}
