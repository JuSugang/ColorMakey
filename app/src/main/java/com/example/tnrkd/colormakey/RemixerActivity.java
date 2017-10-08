package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;

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
    ImageView remixerPreview;
    TextView remixerRGBtext;
    Button calcButton;
    BitmapDrawable d;
    Bitmap resultImage;
    boolean imageOnFlag=false;
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
        //-------------------------카메라 기능-------------------------------------------------
        loadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPhoto();
            }
        });
        //-------------------------갤러리 기능-------------------------------------------------
        loadGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectGallery();
            }
        });
        //-------------------------컬러픽커 기능-------------------------------------------------
        loadColorTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AmbilWarnaDialog dialog = new AmbilWarnaDialog(RemixerActivity.this, Color.rgb(255,255,255), new AmbilWarnaDialog.OnAmbilWarnaListener() {
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
                        remixerPreview.setBackgroundColor(Color.rgb(R,G,B));
                        remixerRGBtext.setText("("+R+","+G+","+B+")");
                        imageOnFlag=false;
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }
                });
                dialog.show();
            }
        });
        //-------------------------이미지 터치 위치 색 추출-------------------------------------------------

        ImageResultView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(imageOnFlag==true) {
                    d = (BitmapDrawable) ((ImageView) findViewById(R.id.ImageResultView)).getDrawable();
                    resultImage = d.getBitmap();
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN||motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
                        int imageWidth=resultImage.getWidth();
                        int imageHeight=resultImage.getHeight();
                        int viewWidth = ImageResultView.getWidth();
                        int viewHeight = ImageResultView.getHeight();
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

                        remixerPreview.setBackgroundColor(Color.rgb(R, G, B));
                        remixerRGBtext.setText("(" + R + "," + G + "," + B + ")");

                    }

                }
                return true;
            }
        });
        //-------------------------미리보기창-------------------------------------------------
        remixerPreview=(ImageView)findViewById(R.id.remixerPreview);
        remixerRGBtext=(TextView)findViewById(R.id.remixerRGBtext);
        remixerPreview.setImageResource(R.drawable.mask);
        //-------------------------계산하기 버튼 리스너 추가-------------------------------------------------
        Button calcButton=(Button)findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public static int binToDec(String color){
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
        imageOnFlag=true;
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