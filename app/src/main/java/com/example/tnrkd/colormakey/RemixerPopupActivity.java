package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        rgb=rgb.substring(1,rgb.length()-1);
        String[] target=rgb.split(",");
        float[] Y_data2= {
                (float)Integer.parseInt(target[0].substring(4,target[0].length())),
                (float)Integer.parseInt(target[1].substring(4,target[1].length())),
                (float)Integer.parseInt(target[2].substring(4,target[2].length()))};
        Y_data=Y_data2;
        targetColor=(TextView)findViewById(R.id.targetColor);
        targetColor.setText("목표 색 ("+target[0]+","+target[1]+","+target[2]+")");
        targetColor.setBackgroundColor(Color.rgb((int)Y_data[0],(int)Y_data[1],(int)Y_data[2]));

        Log.e("생성완료","완료");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //ProgressDialog가 현재 보여지고 있다면 아무작업도 하지 않음.
        new RemixerPopupActivity.AsyncProgressDialog().execute(epoch);
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

    ProgressDialog dialog;   //ProgressDialog 참조변수
    int pos_dilaog=0;
    class AsyncProgressDialog extends AsyncTask<Integer, Integer, String> {
        GradientDescent test=new GradientDescent(Global.colors,Y_data);
        //작업이 시작되기 전에 호출되는 메소드로서 일반적으로 이곳에서 ProgressDialog 객체를 생성.
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog= new ProgressDialog(RemixerPopupActivity.this); //ProgressDialog 객체 생성
            dialog.setTitle("Progress");                   //ProgressDialog 제목
            dialog.setMessage("Loading.....");             //ProgressDialog 메세지
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //막대형태의 ProgressDialog 스타일 설정
            dialog.setCanceledOnTouchOutside(false); //ProgressDialog가 진행되는 동안 dialog의 바깥쪽을 눌러 종료하는 것을 금지
            dialog.show(); //ProgressDialog 보여주기
        }

        //excute() 메소드에 의해 실행되는 작업 스레드의 메소드  ( excute()호출 시에 전달한 값 params에서 받음 )
        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
//            test.minimize(3000, (float)0.00001);
            for(int i=0;i<epoch;i++) {
                test.GradientDescentOptimization((float) 0.00001);
                publishProgress(i);
            }
//            while( pos_dilaog < params[0]){ //현재 ProgessDialog의 위치가 100보다 작은가? 작으면 계속 Progress
//                pos_dilaog++;
//                //ProgressDialog에 변경된 위치 적용 ..
//                publishProgress(pos_dilaog);  //onProgressUpdate()메소드 호출.
//                try {
//                    Thread.sleep(100); //0.1초간 스레드 대기 ..예외처리 필수
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }//while
            //while을 끝마치고 여기까지 오면 Progress가 종료되었다는 것을 의미함.
//            pos_dilaog=0; //다음 프로세스를 위해 위치 초기화
            return "Complete Load"; // AsyncTask 의 작덥종료 후 "Complete Load" String 결과 리턴
        }
        //publishProgress()에 의해 호출되는 메소드
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            Log.e("값",values[0].toString());
            dialog.setProgress((int)(values[0]*100/(float)epoch)); //전달받은 pos_dialog값으로 ProgressDialog에 변경된 위치 적용
        }
        //doInBackground() 메소드 종료 후 자동으로 호출되는 콜백 메소드
        //doInBackground() 메소드로부터 리턴된 결과를 파라미터로 받음
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss(); //ProgressDialog 보이지 않게 하기
            dialog=null;      //참조변수 초기화
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
            //doInBackground() 메소드로부터 리턴된 결과 "Complete Load" string Toast로 화면에 표시
            Toast.makeText(RemixerPopupActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
