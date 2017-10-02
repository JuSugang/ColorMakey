package com.example.tnrkd.colormakey;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

/**
 * Created by XNOTE on 2017-09-24.
 */

public class HomeMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLUE);

        //각 TextView에 listener 추가
        TextView remixer=(TextView)findViewById(R.id.remixer);
        TextView mixer=(TextView)findViewById(R.id.mixer);
        TextView palette=(TextView)findViewById(R.id.palette);

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
//                intent.putExtra("flag","false");
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

//        GoogleSignInResult result = Global.result;
//        GoogleSignInAccount acct = result.getSignInAccount();
//        Log.d("HomeMenuActivity", acct.getDisplayName());
    }
}
