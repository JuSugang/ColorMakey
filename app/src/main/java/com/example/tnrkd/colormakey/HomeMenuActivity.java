package com.example.tnrkd.colormakey;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class HomeMenuActivity extends BaseActivity {

    boolean doubleBackToExitPressedOnce = false;
    ImageView remixer;
    ImageView mixer;
    ImageView palette;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setToolbar(toolbar);

//        ImageView toolbar_round = (ImageView)findViewById(R.id.toolbar_round);
//        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(Color.rgb(255,255,255));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setPadding(0, Global.statusBar, 0, 0);

        ImageView background=(ImageView)findViewById(R.id.toolbar_round);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        background.getLayoutParams().height=height*42/100;
        background.getLayoutParams().width=width;
        background.requestLayout();

        ImageView bubble = (ImageView) findViewById(R.id.bubble);
        bubble.getLayoutParams().height=width*170/432;
        bubble.getLayoutParams().width=width;
        bubble.requestLayout();
        //각 TextView에 listener 추가
        remixer=(ImageView)findViewById(R.id.remixer);
        mixer=(ImageView)findViewById(R.id.mixer);
        palette=(ImageView)findViewById(R.id.palette);

        remixer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeMenuActivity.this,
                        RemixerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        mixer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeMenuActivity.this,
                        MixerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        palette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeMenuActivity.this,
                        PaletteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        StartAnimations();
    }
    private void StartAnimations() {


        Animation home_palette = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.home_palatte);
        home_palette.reset();
        palette.clearAnimation();
        palette.startAnimation(home_palette);

        Animation home_mixer = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.home_mixer);
        home_mixer.reset();
        mixer.clearAnimation();
        mixer.startAnimation(home_mixer);

        Animation home_remixer = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.home_remixer);
        home_remixer.reset();
        remixer.clearAnimation();
        remixer.startAnimation(home_remixer);

        Animation copyright = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.copyright);
        copyright.reset();
        TextView copyright1=(TextView)findViewById(R.id.Copyright1);
        TextView copyright2=(TextView)findViewById(R.id.Copyright2);
        copyright1.clearAnimation();
        copyright2.clearAnimation();
        copyright1.startAnimation(copyright);
        copyright2.startAnimation(copyright);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                LoginActivity.logout(this);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            if(Global.loginActivity != null) {
                Global.loginActivity.finish();
                // 아래의 두 줄만 사용하여 앱을 종료시키면,
                // 실행되고 있는 액티비티가 2개 이상일 때, 이전의 액티비티가 살아나 앱이 다시 실행된다
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "뒤로가기 버튼을 한번 더 클릭하면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
