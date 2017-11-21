package com.example.tnrkd.colormakey;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.text.SimpleDateFormat;
import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by tnrkd on 2017-09-29.
 */

public class RemixerActivity extends BaseActivity {
    static final int CAMERA_CODE=1;
    private final int MY_REQUEST = 1100;
    private final int MY_REQUEST_2 = 1200;
    private final int GALLERY = 9002;
    ImageView loadCamera;
    ImageView loadGallery;
    ImageView loadColorTable;
    ImageView ImageResultView;
    ImageView remixerPreview;
    FrameLayout framePreview;
    FrameLayout frameView;
    TextView remixerRGBtext;
    Button calcButton;
    Bitmap resultImage;
    boolean imageOnFlag=false;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remixer);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);

        ImageView background=(ImageView)findViewById(R.id.remixer_toolbar_round);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        float resolution = dm.density;
        background.getLayoutParams().height=height*42/100;
        background.getLayoutParams().width=width;
        background.requestLayout();

        ImageView bubble = (ImageView) findViewById(R.id.remixer_bubble);
        bubble.getLayoutParams().height=width*170/432;
        bubble.getLayoutParams().width=width;
        bubble.requestLayout();

        Global.requestExternalStoragePermission(this, MY_REQUEST);
        Global.requestCameraPermission(this, MY_REQUEST_2);
        loadCamera = (ImageView)findViewById(R.id.loadCamera);
        loadGallery = (ImageView)findViewById(R.id.loadGallery);
        loadColorTable = (ImageView)findViewById(R.id.loadColorTable);
        ImageResultView =(ImageView)findViewById(R.id.ImageResultView);

        //-------------------------카메라 기능-------------------------------------------------
        loadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPhoto(view);
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
                        ImageResultView.setImageResource(com.example.tnrkd.colormakey.R.drawable.remixer_basicview_alpha);
                        remixerPreview.setBackgroundColor(Color.rgb(R,G,B));
                        remixerRGBtext.setText("빨강: " + R + "\n초록: " + G + "\n파랑: " + B );
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

                    GlideBitmapDrawable d=(GlideBitmapDrawable)ImageResultView.getDrawable();
                    resultImage = d.getBitmap();
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN||motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
                        int imageWidth=resultImage.getWidth();
                        int imageHeight=resultImage.getHeight();
                        int viewWidth = ImageResultView.getWidth();
                        int viewHeight = ImageResultView.getHeight();
                        if(x>0 && x<viewWidth && y>0 && y<viewHeight) {
                            x = x * imageWidth / (float) viewWidth;
                            y = y * imageHeight / (float) viewHeight;
                            final int sourceColor = resultImage.getPixel((int) x, (int) y);

                            String[] argb = new String[4];
                            for (int i = 0; i < 4; i++) {
                                argb[i] = Integer.toBinaryString(sourceColor).substring(8 * i, 8 * i + 8);
                            }
                            int R = binToDec(argb[1]);
                            int G = binToDec(argb[2]);
                            int B = binToDec(argb[3]);
                            remixerPreview.setBackgroundColor(Color.rgb(R, G, B));
                            remixerRGBtext.setText("빨강: " + R + "\n초록: " + G + "\n파랑: " + B );
                        }
                    }
                }
                return true;
            }
        });
        //-------------------------미리보기창-------------------------------------------------
        remixerPreview=(ImageView)findViewById(R.id.remixerPreview);
        remixerPreview.getLayoutParams().height = (int) (60 * resolution);
        remixerPreview.getLayoutParams().width= (int) (60 * resolution);
        remixerRGBtext=(TextView)findViewById(R.id.remixerRGBtext);
        remixerPreview.setImageResource(R.drawable.mask);
        //-------------------------계산하기 버튼 리스너 추가-------------------------------------------------
        ImageView calcButton=(ImageView) findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(remixerRGBtext.getText().equals("빨강:\n초록:\n파랑:")){
                    Toast toastMessage = Toast.makeText(RemixerActivity.this,"색을 선택하세요",Toast.LENGTH_LONG);
                    setToast(toastMessage.getView());
                    toastMessage.setGravity(Gravity.BOTTOM, 0, 80);
                    toastMessage.show();
                }
                else{
                    Intent intent = new Intent(RemixerActivity.this, RemixerPopupActivity.class);
                    intent.putExtra("rgb", remixerRGBtext.getText());
                    startActivity(intent);
                }
            }
        });
        StartAnimations();
    }
    private void StartAnimations() {
        Animation remixer_camera = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.remixer_camera);
        remixer_camera.reset();
        loadCamera.clearAnimation();
        loadCamera.startAnimation(remixer_camera);

        Animation remixer_gallery = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.remixer_gallery);
        remixer_gallery.reset();
        loadGallery.clearAnimation();
        loadGallery.startAnimation(remixer_gallery);

        Animation remixer_color = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.remixer_color);
        remixer_color.reset();
        loadColorTable.clearAnimation();
        loadColorTable.startAnimation(remixer_color);

        Animation remixer_view = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.remixer_view);
        remixer_view.reset();
        frameView=(FrameLayout)findViewById(R.id.framelayout_view);
        frameView.clearAnimation();
        frameView.startAnimation(remixer_view);

        Animation remixer_preview = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.remixer_preview);
        remixer_preview.reset();
        framePreview=(FrameLayout)findViewById(R.id.framelayout_preview);
        framePreview.clearAnimation();
        framePreview.startAnimation(remixer_preview);
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
                case GALLERY:
                    SendPicture2(data); //갤러리에서 가져오기
                    break;
                case CAMERA_CODE:
                    SendPicture(data); //카메라에서 가져오기
                    break;
                default:
                    break;
            }

        }
    }
    private void SelectPhoto(View v) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA_CODE);
        onCaptureImage(v);
    }
    private void SelectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY);
    }
    private void SendPicture(Intent data) {
//        Uri imgUri = data.getData();
        //String imagePath = getRealPathFromURI(imgUri); // path 경로
        Glide.with(this).load(fileUri).centerCrop().into(ImageResultView);

        imageOnFlag=true;
    }
    private void SendPicture2(Intent data){
        try {
            Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(ImageResultView);
            imageOnFlag=true;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
//--------------------------------로딩클래스----------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MY_REQUEST : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else {
                    Toast.makeText(this, "권한 사용을 동의해야 사용이 가능합니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            case MY_REQUEST_2 : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else {
                    Toast.makeText(this, "권한 사용을 동의해야 사용이 가능합니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    public void onCaptureImage(View v)
    {
        // give the image a name so we can store it in the phone's default location
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image (this doesn't work at all for images)
        fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store content values
        intent.putExtra( MediaStore.EXTRA_OUTPUT,  fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CODE);
    }
}