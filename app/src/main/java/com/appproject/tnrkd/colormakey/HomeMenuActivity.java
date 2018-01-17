package com.appproject.tnrkd.colormakey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
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
    TextView toolbarTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setToolbar(toolbar);
        toolbarTextView = (TextView)findViewById(R.id.toolbarTextView);
        toolbarTextView.setText(Global.userName + "님 환영합니다");

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
                if(Global.colors.size() == 0) {
                    Toast toastMessage = Toast.makeText(HomeMenuActivity.this,"\t팔레트에 등록된 색상이 없습니다\t\n\t나만의 팔레트에서 색상을 등록해 주세요\t",Toast.LENGTH_LONG);
                    setToast(toastMessage.getView());
                    toastMessage.setGravity(Gravity.BOTTOM, 0, 80);
                    toastMessage.show();
                }else {
                    Intent intent = new Intent(HomeMenuActivity.this,
                            RemixerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
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
        Animation username = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.username);
        username.reset();
        toolbarTextView.clearAnimation();
        toolbarTextView.startAnimation(username);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // 홈메뉴에서는 홈으로 가는 버튼 지우기
        menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeMenuActivity.this);
                alertDialogBuilder.setMessage("로그아웃 하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoginActivity.logout(HomeMenuActivity.this);
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
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
        Toast toastMessage = Toast.makeText(this, "뒤로가기 버튼을 한번 더 클릭하면\n앱이 종료됩니다", Toast.LENGTH_LONG);
        setToast(toastMessage.getView());
        toastMessage.setGravity(Gravity.BOTTOM, 0, 80);
        toastMessage.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
