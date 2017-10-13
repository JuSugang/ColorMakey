package com.example.tnrkd.colormakey.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.tnrkd.colormakey.R;

/**
 * Created by XNOTE on 2017-10-13.
 */

public class SelectGalleryCameraDialog extends Dialog {

    private ImageButton galleryImageButton;
    private ImageButton cameraImageButton;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    public SelectGalleryCameraDialog(Context context, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_select_gallery_camera);

        galleryImageButton = findViewById(R.id.gallery_button);
        cameraImageButton = findViewById(R.id.camera_button);

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            cameraImageButton.setOnClickListener(mLeftClickListener);
            galleryImageButton.setOnClickListener(mRightClickListener);
        }else {

        }
    }
}
