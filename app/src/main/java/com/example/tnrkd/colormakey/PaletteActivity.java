package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tnrkd.colormakey.adapter.ColorGridPaletteAdapter;
import com.example.tnrkd.colormakey.dialog.GalleryCameraDialog;

/**
 * Created by tnrkd on 2017-09-29.
 */

public class PaletteActivity extends Activity {

    private final String TAG = "PaletteActivity";
    private final int GALLERY = 9002;
    private final int CAMERA = 9003;

    private GridView gridView;

    private GalleryCameraDialog galleryCameraDialog;
    private ImageView paletteImageView;
    private ImageView paletteImageView2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        gridView = findViewById(R.id.palette_gridview);
        ColorGridPaletteAdapter adapter = new ColorGridPaletteAdapter(PaletteActivity.this.getApplicationContext(), R.layout.row, Global.colors);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY) {
            if(resultCode == Activity.RESULT_OK) {
                try {
                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    paletteImageView = (ImageView)galleryCameraDialog.findViewById(R.id.palette_imageView);
                    paletteImageView2 = (ImageView)galleryCameraDialog.findViewById(R.id.palette_imageView2);
                    paletteImageView.setImageBitmap(bitmap);

                    paletteImageView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {

                            /**
                             * motionEvent.getX(), getY()는 ImageView에서의 절대좌표를 반환
                             * */

                            if(MotionEvent.ACTION_DOWN == motionEvent.getAction()) {


                                int[] locationView = {0, 0};

                                getAbsoluteTouchLocation(view, motionEvent, locationView);

                                //int rgb = bitmap.getPixel(locationView[0], locationView[1]);
                                getWindow().getDecorView().setDrawingCacheEnabled(true);
                                int rgb = getWindow().getDecorView().getDrawingCache().getPixel(locationView[0], locationView[1]);

                                int r = Color.red(rgb);
                                int g = Color.green(rgb);
                                int b = Color.blue(rgb);

                                paletteImageView2.setBackgroundColor(Color.rgb(r,g,b));
                            }

                            return true;
                        }

                        public void getAbsoluteTouchLocation(View v, MotionEvent event,
                                                                    int[] locationView) {
                            v.getLocationOnScreen(locationView);
                            locationView[0]+=(int)event.getX();
                            locationView[1]+=(int)event.getY();
                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {

        }
    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY);
        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
        }
    };

}
