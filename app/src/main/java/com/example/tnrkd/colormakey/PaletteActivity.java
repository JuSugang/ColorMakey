package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.tnrkd.colormakey.adapter.ColorGridPaletteAdapter;
import com.example.tnrkd.colormakey.dialog.GalleryCameraDialog;
import com.example.tnrkd.colormakey.dialog.NewColorNameDialog;
import com.example.tnrkd.colormakey.dialog.SelectGalleryCameraDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tnrkd on 2017-09-29.
 */

public class PaletteActivity extends BaseActivity {

    private final String TAG = "PaletteActivity";
    private final int GALLERY = 9002;
    private final int CAMERA = 9003;
    private final int MY_REQUEST = 1100;
    private final int MY_REQUEST_2 = 1200;

    private GridView gridView;
    private ColorGridPaletteAdapter adapter;

    private GalleryCameraDialog galleryCameraDialog;
    private SelectGalleryCameraDialog selectGalleryCameraDialog;
    private NewColorNameDialog newColorNameDialog;

    private ImageView paletteImageView;
    private ImageView paletteImageView2;
    private ImageView noColorImageView;
    private ImageView ResultViewBackground;
    private ImageView paletteInstView;
    private TextView paletteInst1;
    private TextView paletteInst2;
    private ImageButton paletteAddButton;
    boolean imageOnFlag = false;
    private Bitmap resultImage;

    private DatabaseReference mDatabase;
    private String rgbcode;
    private String hexcode;

    private AlertDialog.Builder alertDialogBuilder;

    private Uri fileUri;
    private boolean isSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

//        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
//        setToolbar(toolbar);

        ImageView background=(ImageView)findViewById(R.id.palette_toolbar_round);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        background.getLayoutParams().height=height*42/100;
        background.getLayoutParams().width=width;
        background.requestLayout();

        ImageView bubble = (ImageView) findViewById(R.id.palette_bubble);
        bubble.getLayoutParams().height=width*170/432;
        bubble.getLayoutParams().width=width;
        bubble.requestLayout();

        // 허가
        Global.requestExternalStoragePermission(this, MY_REQUEST);
        Global.requestCameraPermission(this, MY_REQUEST_2);

        ResultViewBackground=(ImageView)findViewById(R.id.ResultViewBackground);
        paletteInstView=(ImageView)findViewById(R.id.paletteInstView);
        paletteInst1=(TextView)findViewById(R.id.paletteInst1);
        paletteInst2=(TextView)findViewById(R.id.paletteInst2);
        paletteAddButton=(ImageButton)findViewById(R.id.paletteAddButton);

        gridView = (GridView)findViewById(R.id.palette_gridview);
        adapter = new ColorGridPaletteAdapter(PaletteActivity.this.getApplicationContext(), R.layout.row, Global.colors,dm.density);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView colorName = (TextView)view.findViewById(R.id.colorName);
                final String selectedColorName = colorName.getText().toString();

                alertDialogBuilder.setMessage("해당 색상을 삭제하시겠습니까?").setCancelable(false).setNegativeButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(com.example.tnrkd.colormakey.dto.Color color : Global.colors) {
                                    if(selectedColorName.equals(color.getColorname().toString())) {
                                        Global.colors.remove(color);
                                        mDatabase.child("palette").child(Global.userUID).setValue(Global.colors);
                                        isColors();
                                        break;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }).setPositiveButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(postListener);

        alertDialogBuilder = new AlertDialog.Builder(PaletteActivity.this);
        noColorImageView = (ImageView)findViewById(R.id.noColorImage);
        isColors();
        StartAnimations();
    }
    private void StartAnimations() {
        Animation palette_view = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.palette_view);
        palette_view.reset();
        gridView.clearAnimation();
        gridView.startAnimation(palette_view);
        ResultViewBackground.clearAnimation();
        ResultViewBackground.startAnimation(palette_view);

        Animation palette_inst_view = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.palette_inst_view);
        palette_inst_view.reset();
        paletteInstView.clearAnimation();
        paletteInstView.startAnimation(palette_inst_view);

        Animation palette_inst_text = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.palette_inst_text);
        palette_inst_text.reset();
        paletteInst1.clearAnimation();
        paletteInst2.clearAnimation();
        paletteInst1.startAnimation(palette_inst_text);
        paletteInst2.startAnimation(palette_inst_text);

        Animation palette_add_button = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.palette_add_button);
        palette_add_button.reset();
        paletteAddButton.clearAnimation();
        paletteAddButton.startAnimation(palette_add_button);


    }
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.paletteAddButton:
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

        // 갤러리에서 사진 불러오기
        if(requestCode == GALLERY) {
            if(resultCode == Activity.RESULT_OK) {

                galleryCameraDialog = new GalleryCameraDialog(this,
                        "선택한 사진", // 제목
                        "다이얼로그 내용 표시하기", // 내용
                        leftListener, // 왼쪽 버튼 이벤트
                        rightListener);
                galleryCameraDialog.show();
                galleryCameraDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isSelected = false;
                    }
                });

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
        }else { // 카메라에서 사진 불러오기

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

                if(fileUri != null) {
                    Log.d(TAG, "Image saved to:\n" + fileUri);
                    Log.d(TAG, "Image path:\n" + fileUri.getPath());
                }

//                Uri imgUri = data.getData();
//                Glide.with(this).load(imgUri).into(paletteImageView);
                Glide.with(this).load(fileUri).into(paletteImageView);

                imageOnFlag=true;
            }
        }
    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.camera_button : {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, CAMERA);
                    onCaptureImage(v);
                    selectGalleryCameraDialog.dismiss();
                    break;
                }
                case R.id.cancel_button : {
                    galleryCameraDialog.dismiss();
                    break;
                }
                case R.id.new_color_name_cancel_button : {
                    newColorNameDialog.dismiss();
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
                case R.id.register_button : {

                    // 색상을 선택했는지 검사
                    if(!isSelected) {
                        Toast toastMessage = Toast.makeText(PaletteActivity.this, "색상을 선택해 주세요", Toast.LENGTH_LONG);
                        setToast(toastMessage.getView());
                        toastMessage.setGravity(Gravity.BOTTOM, 0, 80);
                        toastMessage.show();
                        break;
                    }
                    // 새로 등록할 색상의 이름 적을 다이얼로그 생성
                    newColorNameDialog = new NewColorNameDialog(PaletteActivity.this, this, rightListener);
                    newColorNameDialog.show();

                    // 키보드 올리기
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    break;
                }
                case R.id.new_color_name_register_button : {

                    // 새로 등록할 색상의 이름 받아오기
                    String newColorName = newColorNameDialog.colorNameEdittext.getText().toString();
                    // 색상이름 중복 검사
                    boolean validCheck = false;
                    for(com.example.tnrkd.colormakey.dto.Color color : Global.colors) {
                        if(newColorName.equals(color.getColorname())) {
                            validCheck = true;
                            alertDialogBuilder.setMessage("같은 이름의 색상이 존재합니다").setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });
                            AlertDialog alert = alertDialogBuilder.create();
                            alert.show();
                            break;
                        }
                    }
                    // Color 객체 만들어서 DB에 insert
                    if(!validCheck) {
                        com.example.tnrkd.colormakey.dto.Color color = new com.example.tnrkd.colormakey.dto.Color(hexcode, rgbcode, newColorName);
                        Global.colors.add(color);
                        mDatabase.child("palette").child(Global.userUID).setValue(Global.colors);
                        break;
                    }
                }
            }
        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(imageOnFlag==true) {
                isSelected = true;
                GlideBitmapDrawable dd = (GlideBitmapDrawable)paletteImageView.getDrawable();
                resultImage = dd.getBitmap();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN||motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    int imageWidth=resultImage.getWidth();
                    int imageHeight=resultImage.getHeight();
                    int viewWidth = paletteImageView.getWidth();
                    int viewHeight = paletteImageView.getHeight();
                    if(x>0 && x<viewWidth && y>0 && y<viewHeight) {
                        x = x * imageWidth / (float) viewWidth;
                        y = y * imageHeight / (float) viewHeight;
                        final int sourceColor = resultImage.getPixel((int) x, (int) y);

                        String[] argb = new String[4];
                        for (int i = 0; i < 4; i++) {
                            argb[i] = Integer.toBinaryString(sourceColor).substring(8 * i, 8 * i + 8);
                        }
                        int R = RemixerActivity.binToDec(argb[1]);
                        int G = RemixerActivity.binToDec(argb[2]);
                        int B = RemixerActivity.binToDec(argb[3]);

                        rgbcode = String.format("%03d", R) + String.format("%03d", G) + String.format("%03d", B);
                        hexcode = String.format("%02X", R) + String.format("%02X", G) + String.format("%02X", B);
                        paletteImageView2.setBackgroundColor(Color.rgb(R, G, B));
                    }
                }
            }
            return true;
        }
    };

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(galleryCameraDialog != null && galleryCameraDialog.isShowing()) {
                adapter.notifyDataSetChanged();
//                if(newColorNameDialog!=null){
//                    newColorNameDialog.dismiss();
//                }
//                galleryCameraDialog.startFlicker();
                Toast toastMessage = Toast.makeText(PaletteActivity.this, "새로운 색상이 추가되었습니다", Toast.LENGTH_LONG);
                PaletteActivity.this.setToast(toastMessage.getView());
                toastMessage.setGravity(Gravity.BOTTOM, 0, 80);
                toastMessage.show();
                newColorNameDialog.dismiss();

                // 키보드 내리기
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(newColorNameDialog.colorNameEdittext.getWindowToken(), 0);

                isColors();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };

    // 팔레트나 리믹스에서 카메라 사용할 때, 찍은 사진은 로컬에 저장해 주는 메소드
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
        startActivityForResult(intent, CAMERA);
    }

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

    public void isColors() {
        if(Global.colors.size() == 0) {
            gridView.setVisibility(View.GONE);
            noColorImageView.setVisibility(View.VISIBLE);
        }else {
            gridView.setVisibility(View.VISIBLE);
            noColorImageView.setVisibility(View.GONE);
        }
    }
}
