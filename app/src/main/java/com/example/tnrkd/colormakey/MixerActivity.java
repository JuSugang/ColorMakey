package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by tnrkd on 2017-09-29.
 */



public class MixerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixer);

        Button bLoad = (Button) findViewById(R.id.button2);

        bLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MixerActivity.this, MixerPopupActivity.class));

            }
        });

    }

}
