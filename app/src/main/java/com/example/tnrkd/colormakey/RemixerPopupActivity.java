package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    Button confirmButton;

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
        rgb=rgb.substring(1,rgb.length()-1);
        String[] target=rgb.split(",");
        float[] Y_data= {
                (float)Integer.parseInt(target[0].substring(4,target[0].length())),
                (float)Integer.parseInt(target[1].substring(4,target[1].length())),
                (float)Integer.parseInt(target[2].substring(4,target[2].length()))};
        targetColor=(TextView)findViewById(R.id.targetColor);
        targetColor.setText("목표 색 ("+target[0]+","+target[1]+","+target[2]+")");
        targetColor.setBackgroundColor(Color.rgb((int)Y_data[0],(int)Y_data[1],(int)Y_data[2]));

        GradientDescent test=new GradientDescent(Global.colors,Y_data);
        test.minimize(3000, (float)0.00001);

        ArrayList<com.example.tnrkd.colormakey.dto.Color> result_Color=test.getColor();
        ArrayList<Float> result_W=test.getW();
        float[] result_hypo=test.getRGBResult();
        float percent=test.getPercent();

        pieChart = (PieChart) findViewById(R.id.chart1);

        entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<String>();

        AddValuesToPIEENTRY(result_W);
        AddValuesToPieEntryLabels(result_Color);
//
        pieDataSet = new PieDataSet(entries, "");

        pieData = new PieData(PieEntryLabels, pieDataSet);
//        new PieData()
        int[] color=new int[result_Color.size()];
        for (int i=0;i<result_Color.size();i++){
            ArrayList<Float> tempRGB=result_Color.get(i).mGetRGBarray();
            color[i]=Color.rgb((int)(float)tempRGB.get(0),(int)(float)tempRGB.get(1),(int)(float)tempRGB.get(2));
        }
        pieDataSet.setColors(color);

        pieChart.setData(pieData);
        pieChart.animateXY(1000,1000);
        confirmButton=(Button)findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void AddValuesToPIEENTRY(ArrayList<Float> result_W){
        for (int i=0;i<result_W.size();i++){
            entries.add(new BarEntry(result_W.get(i), 0));
        }
    }

    public void AddValuesToPieEntryLabels(ArrayList<com.example.tnrkd.colormakey.dto.Color> color){
        for (int i=0;i<color.size();i++){
            PieEntryLabels.add(color.get(i).getColorname());
        }
    }
}
