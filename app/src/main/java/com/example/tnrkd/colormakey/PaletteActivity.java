package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.tnrkd.colormakey.dialog.SelectGalleryCameraDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by tnrkd on 2017-09-29.
 */

public class PaletteActivity extends Activity {

    private final String TAG = "PaletteActivity";
    private final int GALLERY = 9002;
    private final int CAMERA = 9003;

    private GridView gridView;

    private GalleryCameraDialog galleryCameraDialog;
    private SelectGalleryCameraDialog selectGalleryCameraDialog;
    private ImageView paletteImageView;
    private ImageView paletteImageView2;

    boolean imageOnFlag = false;
    BitmapDrawable d;
    Bitmap resultImage;

    private DatabaseReference mDatabase;
    String rgbcode;
    String hexcode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        gridView = findViewById(R.id.palette_gridview);
        ColorGridPaletteAdapter adapter = new ColorGridPaletteAdapter(PaletteActivity.this.getApplicationContext(), R.layout.row, Global.colors);
        gridView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.color_add_button:
                selectGalleryCameraDialog = new SelectGalleryCameraDialog(this,
                        leftListener, // 왼쪽 버튼 이벤트
                        rightListener); // 오른쪽 버튼 이벤트
                selectGalleryCameraDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY) {
            if(resultCode == Activity.RESULT_OK) {

                galleryCameraDialog = new GalleryCameraDialog(this,
                        "선택한 사진", // 제목
                        "다이얼로그 내용 표시하기", // 내용
                        leftListener, // 왼쪽 버튼 이벤트
                        rightListener);
                galleryCameraDialog.show();

                try {
                    imageOnFlag = true;
                    paletteImageView = (ImageView)galleryCameraDialog.findViewById(R.id.palette_imageView);
                    paletteImageView2 = (ImageView)galleryCameraDialog.findViewById(R.id.palette_imageView2);
                    Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(paletteImageView);

                    paletteImageView.setOnTouchListener(onTouchListener);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {

            if(resultCode == Activity.RESULT_OK) {

                galleryCameraDialog = new GalleryCameraDialog(this,
                        "선택한 사진", // 제목
                        "다이얼로그 내용 표시하기", // 내용
                        leftListener, // 왼쪽 버튼 이벤트
                        rightListener);
                galleryCameraDialog.show();

                paletteImageView = (ImageView)galleryCameraDialog.findViewById(R.id.palette_imageView);
                paletteImageView2 = (ImageView)galleryCameraDialog.findViewById(R.id.palette_imageView2);
                paletteImageView.setOnTouchListener(onTouchListener);

                Uri imgUri = data.getData();
                String imagePath = getRealPathFromURI(imgUri); // path 경로
                Glide.with(this).load(imgUri).into(paletteImageView);

                imageOnFlag=true;
            }

        }
    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.camera_button : {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA);
                    selectGalleryCameraDialog.dismiss();
                    break;
                }
                case R.id.register_button : {

                    com.example.tnrkd.colormakey.dto.Color color = new com.example.tnrkd.colormakey.dto.Color(hexcode, rgbcode, "TestColor");

                    Global.colors.add(color);
                    mDatabase.child("palette").child(Global.userUID).setValue(Global.colors);
                    break;
                }
            }

        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.gallery_button : {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY);
                    selectGalleryCameraDialog.dismiss();
                    break;
                }
                case R.id.cancel_button : {
                    galleryCameraDialog.dismiss();
                }
            }
        }
    };

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
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
                    int R = RemixerActivity.binToDec(argb[1]);
                    int G = RemixerActivity.binToDec(argb[2]);
                    int B = RemixerActivity.binToDec(argb[3]);

                    rgbcode = String.format("%03d",R) + String.format("%03d", G) + String.format("%03d", B);
                    hexcode = String.format("%02X", R) + String.format("%02X", G) + String.format("%02X", B);

//                                    float[] hsv = new float[3];
//                                    Color.RGBToHSV(R, G, B, hsv);

                    paletteImageView2.setBackgroundColor(Color.rgb(R, G, B));
                }
            }
            return true;
        }
    };
}
