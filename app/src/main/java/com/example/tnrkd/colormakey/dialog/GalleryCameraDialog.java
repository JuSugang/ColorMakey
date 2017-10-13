package com.example.tnrkd.colormakey.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tnrkd.colormakey.R;

/**
 * Created by XNOTE on 2017-10-05.
 */

public class GalleryCameraDialog extends Dialog {

    private TextView titleTextView;
    private TextView contentTextView;
    private Button registerButton;
    private Button cancelButton;

    private String title;
    private String content;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public GalleryCameraDialog(Context context, String title, String content, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.title = title;
        this.content = content;
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
        setContentView(R.layout.dialog_gallery_camera);

        titleTextView = (TextView) findViewById(R.id.txt_title);
        contentTextView = (TextView) findViewById(R.id.txt_content);
        registerButton = findViewById(R.id.register_button);
        cancelButton = findViewById(R.id.cancel_button);

        // 제목과 내용을 생성자에서 셋팅한다.
        titleTextView.setText(title);
        //contentTextView.setText(content);

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            registerButton.setOnClickListener(mLeftClickListener);
            cancelButton.setOnClickListener(mRightClickListener);
        }else {

        }
    }
}
