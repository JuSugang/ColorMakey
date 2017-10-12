package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
/**
 * Created by tnrkd on 2017-10-12.
 */

public class RemixerPopupActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_remixer_popup);
    }
}
