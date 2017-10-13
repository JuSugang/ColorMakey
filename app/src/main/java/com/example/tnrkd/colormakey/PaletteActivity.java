package com.example.tnrkd.colormakey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by tnrkd on 2017-09-29.
 */

public class PaletteActivity extends Activity {

    private final String TAG = "PaletteActivity";
    private final int GALLERY = 9002;
    private final int CAMERA = 9003;

    private GridView gridView;
    private ColorGridPaletteAdapter adapter;

    private GalleryCameraDialog galleryCameraDialog;
    private SelectGalleryCameraDialog selectGalleryCameraDialog;
    private NewColorNameDialog newColorNameDialog;

    private ImageView paletteImageView;
    private ImageView paletteImageView2;

    boolean imageOnFlag = false;
    Bitmap resultImage;

    private DatabaseReference mDatabase;
    String rgbcode;
    String hexcode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        gridView = findViewById(R.id.palette_gridview);
        adapter = new ColorGridPaletteAdapter(PaletteActivity.this.getApplicationContext(), R.layout.row, Global.colors);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView colorName = (TextView)view.findViewById(R.id.colorName);
                final String selectedColorName = colorName.getText().toString();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaletteActivity.this);
                alertDialogBuilder.setMessage("해당 색상을 삭제하시겠습니까?").setCancelable(false).setNegativeButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(com.example.tnrkd.colormakey.dto.Color color : Global.colors) {
                                    if(selectedColorName.equals(color.getColorname().toString())) {
                                        Global.colors.remove(color);
                                        mDatabase.child("palette").child(Global.userUID).setValue(Global.colors);
                                        break;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }).setPositiveButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(postListener);
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

        // 갤러리에서 사진 불러오기
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

                Uri imgUri = data.getData();
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
                    // Color 객체 만들어서 DB에 insert
                    com.example.tnrkd.colormakey.dto.Color color = new com.example.tnrkd.colormakey.dto.Color(hexcode, rgbcode, newColorName);
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
                case R.id.new_color_name_cancel_button : {
                    newColorNameDialog.dismiss();
                }
            }
        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(imageOnFlag==true) {
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

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(galleryCameraDialog != null && galleryCameraDialog.isShowing()) {
                galleryCameraDialog.dismiss();
                adapter.notifyDataSetChanged();
                newColorNameDialog.dismiss();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };
}
