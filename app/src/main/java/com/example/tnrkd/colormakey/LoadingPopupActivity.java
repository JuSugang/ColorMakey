package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by tnrkd on 2017-10-31.
 */

public class LoadingPopupActivity extends Activity {
    ProgressDialog dialog;   //ProgressDialog 참조변수
    int pos_dilaog=0;         //ProgressDialog의 진행 위치

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }
    //Button 클릭시에 자동으로 호출되는 callback Method...
    public void mOnClick(View v){
        switch( v.getId() ){
            case R.id.button:
                if(dialog!=null) return;  //ProgressDialog가 현재 보여지고 있다면 아무작업도 하지 않음.
                new AsyncProgressDialog().execute(100); // 다이얼로그의 max 값으로 100 전달
                break;
        }
    }
    //AsyncTask를 상속한 클래스선언
    //AsyncTask 클래스의 콜백 메소드들 중 doInBackground를 제외하고는 기본적으로 main(UI) Thread에서 실행됨.
    //doInBackground를 제외한 다른 메소드에서는 어디서든 UI 작업 가능
    //AsyncTask< 작업스레드가 처리할 데이터 타입 , 다이얼로그의 설정 데이터 타입, 작업의 결과로 리턴할 데이터 타입 >
    //쉽게 소개하면
    //첫번재 데이터 타입은 doInBackground() 메소드의 파라미터 타입을 지정
    //두번재 파라미터의 타입은 onProgressUpdate() 메소드의 파라미터 타입을 지정
    //세번째 파라미터의 타입은 onPostExecute() 메소드의 파라미터 타입을 지정

    class AsyncProgressDialog extends AsyncTask<Integer, Integer, String> {
        //작업이 시작되기 전에 호출되는 메소드로서 일반적으로 이곳에서 ProgressDialog 객체를 생성.
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog= new ProgressDialog(LoadingPopupActivity.this); //ProgressDialog 객체 생성
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
            while( pos_dilaog < params[0]){ //현재 ProgessDialog의 위치가 100보다 작은가? 작으면 계속 Progress
                pos_dilaog++;
                //ProgressDialog에 변경된 위치 적용 ..
                publishProgress(pos_dilaog);  //onProgressUpdate()메소드 호출.
                try {
                    Thread.sleep(100); //0.1초간 스레드 대기 ..예외처리 필수
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }//while
            //while을 끝마치고 여기까지 오면 Progress가 종료되었다는 것을 의미함.
            pos_dilaog=0; //다음 프로세스를 위해 위치 초기화
            return "Complete Load"; // AsyncTask 의 작덥종료 후 "Complete Load" String 결과 리턴
        }
        //publishProgress()에 의해 호출되는 메소드
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]); //전달받은 pos_dialog값으로 ProgressDialog에 변경된 위치 적용
        }
        //doInBackground() 메소드 종료 후 자동으로 호출되는 콜백 메소드
        //doInBackground() 메소드로부터 리턴된 결과를 파라미터로 받음
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss(); //ProgressDialog 보이지 않게 하기
            dialog=null;      //참조변수 초기화
            //doInBackground() 메소드로부터 리턴된 결과 "Complete Load" string Toast로 화면에 표시
            Toast.makeText(LoadingPopupActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
