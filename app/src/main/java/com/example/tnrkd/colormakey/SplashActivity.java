package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by XNOTE on 2017-09-24.
 */

public class SplashActivity extends Activity {

    private final String TAG = "SplashActivity";

    Thread splashTread;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        try {
            InputStream in = getResources().openRawResource(R.raw.koeancolorname);
            if (in != null) {
                InputStreamReader stream = new InputStreamReader(in, "utf-8");
                BufferedReader buffer = new BufferedReader(stream);
                String read;
                while ((read = buffer.readLine()) != null) {
                    String[] colorInfoArr = read.split(" ");
                    ko_color node = new ko_color(colorInfoArr[0], colorInfoArr[1]);
                    KoreanColorList.ko_colorlist.add(node);   //basic 색 만 테스트
                }
                in.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StartAnimations();
    }

    private void StartAnimations() {
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
//        anim.reset();
//        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
//        l.clearAnimation();
//        l.startAnimation(anim);
//
//        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
//        anim.reset();
//        ImageView iv = (ImageView) findViewById(R.id.splash);
//        iv.clearAnimation();
//        iv.startAnimation(anim); //애니메이션 구동방식 기록

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashActivity.this,
                            LoginActivity.class);
//                    Intent intent = new Intent(SplashActivity.this,
//                            HomeMenuActivity.class);


                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }
            }
        };
        splashTread.start();
    }
}