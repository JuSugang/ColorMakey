package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.example.tnrkd.colormakey.adapter.ColorGridPaletteAdapter;
import com.example.tnrkd.colormakey.dialog.GalleryCameraDialog;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by tnrkd on 2017-09-29.
 */

public class PaletteActivity extends Activity {

    private final String TAG = "PaletteActivity";
    private GridView gridView;

    private GalleryCameraDialog galleryCameraDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        gridView = findViewById(R.id.palette_gridview);
        ColorGridPaletteAdapter adapter = new ColorGridPaletteAdapter(PaletteActivity.this.getApplicationContext() ,R.layout.row, Global.colors);
        gridView.setAdapter(adapter);

    }

    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.color_add_button:
                galleryCameraDialog = new GalleryCameraDialog(this,
                        "[다이얼로그 제목]", // 제목
                        "다이얼로그 내용 표시하기", // 내용
                        leftListener, // 왼쪽 버튼 이벤트
                        rightListener); // 오른쪽 버튼 이벤트
                galleryCameraDialog.show();
                break;
        }
    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            galleryCameraDialog.dismiss();
        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
        }
    };

}
