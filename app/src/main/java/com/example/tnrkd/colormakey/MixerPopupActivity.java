package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
//-------------------------컬러 버튼 특성-------------------------------------------------
        LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(0,0); //레이아웃파라미터 생성
        pm.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        pm.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//-------------------------컬러 버튼 특성-------------------------------------------------

        LinearLayout tabArea= (LinearLayout)findViewById(R.id.tab0);
        Button mButton = new Button(this); //버튼을 선언
        mButton.setText("button"); //버튼에 들어갈 텍스트를 지정(String)
//        mButton.setBackgroundResource(R.drawable.button_selector); //버튼 이미지를 지정(int)
        mButton.setLayoutParams(pm); //앞서 설정한 레이아웃파라미터를 버튼에 적용
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tabArea.addView(mButton); //지정된 뷰에 셋팅완료된 mButton을 추가

        TextView tv0 = (TextView) findViewById(R.id.colpal0);

        tv0.setText(basicColor.get(0).getColorname());
    }
}
