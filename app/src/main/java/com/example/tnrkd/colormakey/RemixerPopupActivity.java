package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by tnrkd on 2017-10-12.
 */

public class RemixerPopupActivity extends Activity {
    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;
    TextView targetColor;
    ImageView confirmButton;
    float[] Y_data;
    int epoch=3000;
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_remixer_popup);
        Intent intent = getIntent();
        String rgb = intent.getExtras().getString("rgb");
        String[] target=rgb.split("\n");
        float[] Y_data2= {
                (float)Integer.parseInt(target[0].substring(4,target[0].length())),
                (float)Integer.parseInt(target[1].substring(4,target[1].length())),
                (float)Integer.parseInt(target[2].substring(4,target[2].length()))};
        int[] Y_data_int={(int)Y_data2[0],(int)Y_data2[1],(int)Y_data2[2]};
        Y_data=Y_data2;
        targetColor=(TextView)findViewById(R.id.targetColor);
        float[] hsvArr = new float[3];
        Color.RGBToHSV((int)Y_data[0], (int)Y_data[1], (int)Y_data[2], hsvArr);
        if(hsvArr[2] > 0.6) {
            targetColor.setTextColor(Color.DKGRAY);
        }else {
            targetColor.setTextColor(Color.WHITE);
        }
        targetColor.setText("선택하신 색은 '" +KoreanColorList.getName(Y_data_int)+"'이에요!");
        targetColor.setBackgroundColor(Color.rgb((int)Y_data[0],(int)Y_data[1],(int)Y_data[2]));
    }

    @Override
    protected void onStart() {
        super.onStart();
        new RemixerPopupActivity.AsyncProgressDialog().execute(epoch);
    }

    public void AddValuesToPIEENTRY(ArrayList<Float> result_W){
        for (int i=0;i<result_W.size();i++){
            float a = (int)(result_W.get(i) * 100);
            entries.add(new Entry(a, 0));
        }
    }

    public void AddValuesToPieEntryLabels(ArrayList<com.example.tnrkd.colormakey.dto.Color> color){
        for (int i=0;i<color.size();i++){
            PieEntryLabels.add(color.get(i).getColorname());
        }
    }

    ProgressDialog dialog;
    int pos_dilaog=0;
    class AsyncProgressDialog extends AsyncTask<Integer, Integer, String> {
        GradientDescent test=new GradientDescent(Global.colors,Y_data);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog= new ProgressDialog(RemixerPopupActivity.this);
            dialog.setTitle("색을 섞어보고 있어요!");
            dialog.setMessage("잠시만 기다려주세요!");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
//            test.minimize(3000, (float)0.00001);
            for(int i=0;i<epoch;i++) {
                test.GradientDescentOptimization((float) 0.00001);
                publishProgress(i);
            }
            return "Complete Load";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            dialog.setProgress((int)(values[0]*100/(float)epoch));
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            dialog=null;
            ArrayList<com.example.tnrkd.colormakey.dto.Color> result_Color=test.getColor();
            ArrayList<Float> result_W=test.getW();
            float[] result_hypo=test.getRGBResult();
            float percent=test.getPercent();

            pieChart = (PieChart) findViewById(R.id.chart1);

            entries = new ArrayList<>();
            PieEntryLabels = new ArrayList<String>();

            AddValuesToPIEENTRY(result_W);
            AddValuesToPieEntryLabels(result_Color);

            pieDataSet = new PieDataSet(entries, "");
            pieData = new PieData(PieEntryLabels, pieDataSet);
            pieData.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return new String((int)value + "%");
                }
            });

            int[] color=new int[result_Color.size()];
            List<Integer> listTextColor = new ArrayList<>();
            for (int i=0;i<result_Color.size();i++){
                ArrayList<Float> tempRGB=result_Color.get(i).mGetRGBarray();
                color[i]=Color.rgb((int)(float)tempRGB.get(0),(int)(float)tempRGB.get(1),(int)(float)tempRGB.get(2));
                float[] hsvArr = new float[3];
                Color.RGBToHSV((int)(float)tempRGB.get(0), (int)(float)tempRGB.get(1), (int)(float)tempRGB.get(2), hsvArr);
                if(hsvArr[2] > 0.6) {
                    listTextColor.add(Color.DKGRAY);
                }else {
                    listTextColor.add(Color.WHITE);
                }
            }

            for(int i = 0 ; i < pieData.getDataSetCount() ; i ++) {

            }

            pieData.setValueTextColors(listTextColor);
            pieData.setValueTextSize((float)13.0);

            pieDataSet.setColors(color);

            pieChart.setData(pieData);
            pieChart.animateXY(1000,1000);
            float[] calcColor=test.getRGBResult();
            pieChart.setHoleColor(Color.rgb((int)calcColor[0],(int)calcColor[1],(int)calcColor[2]));
            float[] hsvArr = new float[3];
            Color.RGBToHSV((int)calcColor[0], (int)calcColor[1], (int)calcColor[2], hsvArr);
            if(hsvArr[2] > 0.6) {
                pieChart.setCenterTextColor(Color.DKGRAY);
            }else {
                pieChart.setCenterTextColor(Color.WHITE);
            }
            pieChart.setCenterText("이게 우리가\n계산한 색이에요!\n\n정확도 : "+test.getPercent()+"%");
            pieChart.setCenterTextSize(17);

            confirmButton=(ImageView)findViewById(R.id.confirmButton);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            if(test.getPercent()<94) {
                Toast toastMessage = Toast.makeText(RemixerPopupActivity.this, "현재 가지고 있는 색으로는\n원하는 색상을 만들기 어렵습니다.", Toast.LENGTH_LONG);
                View toastView = toastMessage.getView();
                toastView.setBackgroundResource(R.color.toastBackgroundColor);
                toastView.setPadding(60,20,60,20);
                toastMessage.setGravity(Gravity.BOTTOM, 0, 80);
                toastMessage.show();
            }
            else if(test.getPercent()<95) {
                Toast toastMessage = Toast.makeText(RemixerPopupActivity.this, "현재 가지고 있는 색으로는\n비슷한 색만 만들 수 있습니다.", Toast.LENGTH_LONG);

                View toastView = toastMessage.getView();
                toastView.setBackgroundResource(R.color.toastBackgroundColor);
                toastView.setPadding(60,20,60,20);
                toastMessage.setGravity(Gravity.BOTTOM, 0, 80);
                toastMessage.show();
            }
        }
    }
}
