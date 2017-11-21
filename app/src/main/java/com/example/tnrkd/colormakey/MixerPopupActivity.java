package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

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
    LayoutInflater inf;
    ArrayList<colorInfo> colorIDs = null;
    float resolution;
    public colorGridAdapter(Context context, int layout, ArrayList<colorInfo> colorIDs,float resolution) {
        this.context = context;
        this.layout = layout;
        this.colorIDs = colorIDs;
        this.resolution=resolution;
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
        colorView.getLayoutParams().height = (int) (60 * resolution);
        colorView.getLayoutParams().width= (int) (60 * resolution);
        colorView.setBackgroundColor(Color.rgb(R,G,B));

        return convertView;
    }
}
class colorClickListener implements AdapterView.OnItemClickListener {

    ArrayList<colorInfo> colorIDs = null;
    ImageView colorPreview;
    TextView hexTextView;
    TextView rgbTextView;
    TextView nameTextView;
    public colorClickListener(ArrayList<colorInfo> colorIDs,ImageView colorPreview,TextView hexTextView,TextView rgbTextView,TextView nameTextView) {
        this.colorIDs = colorIDs;
        this.colorPreview=colorPreview;
        this.hexTextView=hexTextView;
        this.rgbTextView=rgbTextView;
        this.nameTextView=nameTextView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        int R=colorIDs.get(position).getRGBarray()[0];
        int G=colorIDs.get(position).getRGBarray()[1];
        int B=colorIDs.get(position).getRGBarray()[2];
        colorPreview.setImageResource(com.example.tnrkd.colormakey.R.drawable.mask_preview);
        colorPreview.setBackgroundColor(Color.rgb(R,G,B));
        hexTextView.setText("#" + colorIDs.get(position).getHexcode());
        rgbTextView.setText("("+R+","+G+","+B+")");
        nameTextView.setText(colorIDs.get(position).getColorname());
    }
}

public class MixerPopupActivity extends Activity {
    ArrayList<colorInfo>[] basicColor= new ArrayList[9];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mixer_popup);

        int[] txtarr={R.raw.color_basic,R.raw.color_red,R.raw.color_green,R.raw.color_blue,
                R.raw.color_yellow,R.raw.color_brown,R.raw.color_grey,R.raw.color_orange,R.raw.color_violet};
        for (int i=0; i<9 ; i++){
            basicColor[i]=new ArrayList<colorInfo>();
        }

//-------------------------color text 읽기-------------------------------------------------
        for(int i=0;i<9;i++) {
            try {
                InputStream in = getResources().openRawResource(txtarr[i]);
                if (in != null) {
                    InputStreamReader stream = new InputStreamReader(in, "utf-8");
                    BufferedReader buffer = new BufferedReader(stream);

                    String read;
                    while ((read = buffer.readLine()) != null) {
                        String[] colorInfoArr = read.split(",");
                        colorInfo node = new colorInfo(colorInfoArr[0], colorInfoArr[1], colorInfoArr[2]);
                        basicColor[i].add(node);   //basic 색 만 테스트
                    }
                    in.close();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//-------------------------tabHost 구성-------------------------------------------------
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost1);    //tabhost 생성
        tabHost.setup();
        String[] tabName = {"기초색상","빨강","초록","파랑","노랑","갈색","회색","오렌지","보라","직접선택"};
        int[] tabID = {R.id.tab0,R.id.tab1,R.id.tab2,R.id.tab3,R.id.tab4,R.id.tab5,R.id.tab6,R.id.tab7,R.id.tab8,R.id.tab9};
        String[] tabkey =  {"0","1","2","3","4","5","6","7","8","9"};
        for (int i=0;i<10;i++){
            tabHost.addTab(tabHost.newTabSpec(tabkey[i]).setContent(tabID[i]).setIndicator(tabName[i]));  //tabSpec을 tabHost에 추가해준다.
        }
//-------------------------컬러 그리드 생성-------------------------------------------------
        final ImageView colorPreview = (ImageView) findViewById(R.id.colorPreview);
        final TextView hexTextView = (TextView) findViewById(R.id.hexTextView);
        final TextView rgbTextView = (TextView) findViewById(R.id.rgbTextView);
        final TextView nameTextView = (TextView) findViewById(R.id.nameTextView);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int[] gridarr={R.id.grid0,R.id.grid1,R.id.grid2,R.id.grid3,R.id.grid4,R.id.grid5,R.id.grid6,R.id.grid7,R.id.grid8};
        GridView[] colorArea = new GridView[9];
        for (int i=0;i<9;i++) {
            colorArea[i]=(GridView) findViewById(gridarr[i]);
            colorArea[i].setAdapter(new colorGridAdapter(getApplicationContext(),R.layout.row, basicColor[i],dm.density));
            colorClickListener itemClickListener= new colorClickListener(basicColor[i],colorPreview,hexTextView,rgbTextView,nameTextView);
            colorArea[i].setOnItemClickListener(itemClickListener);
        }
        TextView colorPick = (TextView) findViewById(R.id.colorPick);
        colorPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(MixerPopupActivity.this, Color.rgb(255,255,255), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        // color is the color selected by the user.
                        String[] argb= new String[4];
                        for (int i=0;i<4;i++){
                            argb[i]=Integer.toBinaryString(color).substring(8*i,8*i+8);
                        }
                        int R=RemixerActivity.binToDec(argb[1]);
                        int G=RemixerActivity.binToDec(argb[2]);
                        int B=RemixerActivity.binToDec(argb[3]);
                        colorPreview.setImageResource(com.example.tnrkd.colormakey.R.drawable.mask_preview);
                        colorPreview.setBackgroundColor(Color.rgb(R,G,B));
                        hexTextView.setText("#" + Integer.toHexString(R)+Integer.toHexString(G)+Integer.toHexString(B));
                        rgbTextView.setText("("+R+","+G+","+B+")");
                        nameTextView.setText("직접선택했어요");
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }
                });
                dialog.show();
            }
        });
//-------------------------버튼 리스너 생성-------------------------------------------------
        Button popupCancel=(Button)findViewById(R.id.popupCancel);
        Button popupConfirm=(Button)findViewById(R.id.popupConfirm);
        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        popupConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence cs = rgbTextView.getText();
                if(cs.equals("rgb")){
                    Toast toastMessage = Toast.makeText(MixerPopupActivity.this,"색을 선택하세요",Toast.LENGTH_LONG);
                    View toastView = toastMessage.getView();
                    toastView.setBackgroundResource(R.color.toastBackgroundColor);
                    toastView.setPadding(60,20,60,20);
                    toastMessage.setGravity(Gravity.BOTTOM, 0, 80);
                    toastMessage.show();
                }else{
                    String temp = cs.toString();
                    String[] RGBstring=temp.substring(1,temp.length()-1).split(",");
                    int flag=0;
                    for(int i=0;i<Global.list.size();i++){
                        int flagCount=0;
                        if(Global.list.get(i).R==Integer.parseInt(RGBstring[0])){flagCount++;}
                        if(Global.list.get(i).G==Integer.parseInt(RGBstring[1])){flagCount++;}
                        if(Global.list.get(i).B==Integer.parseInt(RGBstring[2])){flagCount++;}
                        if(flagCount==3){
                            Toast toastMessage = Toast.makeText(MixerPopupActivity.this,"이미 등록된 색입니다",Toast.LENGTH_LONG);
                            
                            View toastView = toastMessage.getView();
                            toastView.setBackgroundResource(R.color.toastBackgroundColor);
                            toastView.setPadding(60,20,60,20);
                            toastMessage.setGravity(Gravity.BOTTOM, 0, 80);
                            toastMessage.show();
                            flag=1;
                        }
                    }
                    if(flag==0) {
                        Global.list.add(new colorList(Integer.parseInt(RGBstring[0]), Integer.parseInt(RGBstring[1]), Integer.parseInt(RGBstring[2]), 1));
                        MixerActivity.mAdapter.notifyDataSetChanged();
                        finish();
                    }
                }
            }
        });
    }
}
