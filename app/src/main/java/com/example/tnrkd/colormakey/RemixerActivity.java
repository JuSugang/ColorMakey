package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by tnrkd on 2017-09-29.
 */

public class RemixerActivity extends Activity {
    static final int CAMERA_CODE=1;
    static final int GALLERY_CODE=2;
    ImageView loadCamera;
    ImageView loadGallery;
    ImageView loadColorTable;
    ImageView ImageResultView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remixer);
        loadCamera = (ImageView)findViewById(R.id.loadCamera);
        loadGallery = (ImageView)findViewById(R.id.loadGallery);
        loadColorTable = (ImageView)findViewById(R.id.loadColorTable);
        ImageResultView =(ImageView)findViewById(R.id.ImageResultView);
        loadCamera.setImageResource(R.drawable.add_button);
        loadGallery.setImageResource(R.drawable.add_button);
        loadColorTable.setImageResource(R.drawable.add_button);
        loadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RemixerActivity.this,"카메라호출",Toast.LENGTH_SHORT).show();
                SelectPhoto();
            }
        });
        loadGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RemixerActivity.this,"갤러리호출",Toast.LENGTH_SHORT).show();
                SelectGallery();
            }
        });
        loadColorTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AmbilWarnaDialog dialog = new AmbilWarnaDialog(RemixerActivity.this, Color.rgb(0,0,255), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        // color is the color selected by the user.
                        String[] argb= new String[4];
                        for (int i=0;i<4;i++){
                            argb[i]=Integer.toBinaryString(color).substring(8*i,8*i+8);
                        }
                        int R=binToDec(argb[1]);
                        int G=binToDec(argb[2]);
                        int B=binToDec(argb[3]);
                        ImageResultView.setBackgroundColor(Color.rgb(R,G,B));
                        Toast.makeText(RemixerActivity.this,Integer.toBinaryString(color),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }
                });
                dialog.show();
            }
        });
    }
    private static int binToDec(String color){
        int sum=0;
        int rex=1;
        for(int i=color.length();i>0;i--){
            sum+=rex*Integer.parseInt(color.substring(i-1,i));
            rex*=2;
        }
        return sum;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    SendPicture(data); //갤러리에서 가져오기
                    break;
                case CAMERA_CODE:
                    SendPicture(data); //카메라에서 가져오기
                    break;

                default:
                    break;
            }

        }
    }
    private void SelectPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CODE);
    }
    private void SelectGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);

    }
    private void SendPicture(Intent data) {

        Uri imgUri = data.getData();

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        ImageResultView.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기

    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    public Bitmap rotate(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }
}