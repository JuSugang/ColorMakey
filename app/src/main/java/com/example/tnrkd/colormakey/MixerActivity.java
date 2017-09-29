package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by tnrkd on 2017-09-29.
 */

public class MixerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixer);

        final EditText et = (EditText) findViewById(R.id.editText1);
        Button bSave = (Button) findViewById(R.id.button1);
        Button bLoad = (Button) findViewById(R.id.button2);
        final TextView tv = (TextView) findViewById(R.id.textView1);

        bSave.setOnClickListener(new View.OnClickListener(){
            @Override    // 입력한 데이터를 파일에 추가로 저장하기
            public void onClick(View v) {
                String data = et.getText().toString();

                try {
                    FileOutputStream fos = openFileOutput
                            ("raw/color_basic.txt", // 파일명 지정
                                    Context.MODE_APPEND);// 저장모드
                    PrintWriter out = new PrintWriter(fos);
                    out.println(data);
                    out.close();

                    tv.setText("파일 저장 완료");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 파일의 내용을 읽어서 TextView 에 보여주기
                try{

                    // getResources().openRawResource()로 raw 폴더의 원본 파일을 가져온다.
                    // txt 파일을 InpuStream에 넣는다. (open 한다)
                    InputStream in = getResources().openRawResource(R.raw.color_basic);

                    if(in != null){

                        InputStreamReader stream = new InputStreamReader(in, "utf-8");
                        BufferedReader buffer = new BufferedReader(stream);

                        String read;
                        StringBuilder sb = new StringBuilder("");

                        while((read=buffer.readLine())!=null){
                            sb.append(read);
                        }

                        in.close();

                        tv.setText(sb.toString());
                    }

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
