package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
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

    boolean imageOnFlag = false;
    BitmapDrawable d;
    Bitmap resultImage;

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
                    imageOnFlag = true;
                    paletteImageView = (ImageView)galleryCameraDialog.findViewById(R.id.palette_imageView);
                    paletteImageView2 = (ImageView)galleryCameraDialog.findViewById(R.id.palette_imageView2);
                    Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(paletteImageView);

                    paletteImageView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if(imageOnFlag==true) {
                                //d = (BitmapDrawable) paletteImageView.getDrawable();
                                GlideBitmapDrawable dd = (GlideBitmapDrawable)paletteImageView.getDrawable();
                                resultImage = dd.getBitmap();
                                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN||motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                                    float x = motionEvent.getX();
                                    float y = motionEvent.getY();
                                    int imageWidth=resultImage.getWidth();
                                    int imageHeight=resultImage.getHeight();
                                    int viewWidth = paletteImageView.getWidth();
                                    int viewHeight = paletteImageView.getHeight();
                                    x=x*imageWidth/(float)viewWidth;
                                    y=y*imageHeight/(float)viewHeight;
                                    final int sourceColor = resultImage.getPixel((int) x, (int) y);

                                    String[] argb = new String[4];
                                    for (int i = 0; i < 4; i++) {
                                        argb[i] = Integer.toBinaryString(sourceColor).substring(8 * i, 8 * i + 8);
                                    }
                                    int R = binToDec(argb[1]);
                                    int G = binToDec(argb[2]);
                                    int B = binToDec(argb[3]);

                                    paletteImageView2.setBackgroundColor(Color.rgb(R, G, B));
                                    //remixerRGBtext.setText("(" + R + "," + G + "," + B + ")");

                                }

                            }
                            return true;
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

    public static int binToDec(String color){
        int sum=0;
        int rex=1;
        for(int i=color.length();i>0;i--){
            sum+=rex*Integer.parseInt(color.substring(i-1,i));
            rex*=2;
        }
        return sum;
    }

}
