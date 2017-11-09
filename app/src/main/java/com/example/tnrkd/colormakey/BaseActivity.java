package com.example.tnrkd.colormakey;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

/**
 * Created by XNOTE on 2017-11-09.
 */

public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setToolbar(Toolbar toolbar) {
//        ImageView toolbar_round = (ImageView)findViewById(R.id.toolbar_round);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.rgb(255,255,255));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setPadding(0, Global.statusBar, 0, 0);
    }
}
