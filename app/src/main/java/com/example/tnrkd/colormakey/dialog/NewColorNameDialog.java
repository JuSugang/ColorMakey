package com.example.tnrkd.colormakey.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tnrkd.colormakey.R;

/**
 * Created by XNOTE on 2017-10-14.
 */

public class NewColorNameDialog extends Dialog {

    private ImageView registerButton;
    private ImageView cancelButton;
    public EditText colorNameEdittext;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    public NewColorNameDialog(Context context, View.OnClickListener leftListener, View.OnClickListener rightListener) {
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
        setContentView(R.layout.dialog_new_color_name);

        registerButton = findViewById(R.id.new_color_name_register_button);
        cancelButton = findViewById(R.id.new_color_name_cancel_button);
        colorNameEdittext = findViewById(R.id.color_name_edittext);

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            registerButton.setOnClickListener(mRightClickListener);
            cancelButton.setOnClickListener(mLeftClickListener);
        }else {

        }
    }
}
