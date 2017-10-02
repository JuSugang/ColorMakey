package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by tnrkd on 2017-09-29.
 */


class colorList{

}
public class MixerActivity extends Activity {
//    ArrayList
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixer);

        Button bLoad = (Button) findViewById(R.id.button2);

        bLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MixerActivity.this, MixerPopupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        Intent intent=getIntent();
        String flag=intent.getExtras().getString("flag");
        if(flag.matches("true")){
            String hexExtras=intent.getExtras().getString("hexcode");
            String rgbExtras=intent.getExtras().getString("rgbcode");
            String nameExtras=intent.getExtras().getString("namecode");
        }
    }

}
