package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.example.tnrkd.colormakey.adapter.ColorGridPaletteAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by tnrkd on 2017-09-29.
 */

public class PaletteActivity extends Activity {

    private final String TAG = "PaletteActivity";
    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        gridView = findViewById(R.id.palette_gridview);
        ColorGridPaletteAdapter adapter = new ColorGridPaletteAdapter(PaletteActivity.this.getApplicationContext() ,R.layout.row, Global.colors);
        gridView.setAdapter(adapter);

    }
}
