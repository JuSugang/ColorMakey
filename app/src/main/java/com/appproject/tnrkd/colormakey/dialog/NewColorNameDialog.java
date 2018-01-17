package com.appproject.tnrkd.colormakey.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.appproject.tnrkd.colormakey.KoreanColorList;
import com.appproject.tnrkd.colormakey.R;

/**
 * Created by XNOTE on 2017-10-14.
 */

public class NewColorNameDialog extends Dialog {

    private ImageView registerButton;
    private ImageView cancelButton;
    public EditText colorNameEdittext;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    private String hexcode;

    public NewColorNameDialog(Context context, View.OnClickListener leftListener, View.OnClickListener rightListener, String hexcode) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.hexcode = hexcode;
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

        int[] i = func(hexcode);
        colorNameEdittext.setHint(KoreanColorList.getName(i));

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            registerButton.setOnClickListener(mRightClickListener);
            cancelButton.setOnClickListener(mLeftClickListener);
        }else {

        }
    }

    public static int[] func(String a) {
        int[] result=new int[3];
        result[0]=toint(a.charAt(0))*16+toint(a.charAt(1));
        result[1]=toint(a.charAt(2))*16+toint(a.charAt(3));
        result[2]=toint(a.charAt(4))*16+toint(a.charAt(5));

        return result;
    }
    public static int toint(char a) {
        switch(a) {
            case '0':return 0;
            case '1':return 1;
            case '2':return 2;
            case '3':return 3;
            case '4':return 4;
            case '5':return 5;
            case '6':return 6;
            case '7':return 7;
            case '8':return 8;
            case '9':return 9;
            case 'a':
            case 'A':return 10;
            case 'b':
            case 'B':return 11;
            case 'c':
            case 'C':return 12;
            case 'd':
            case 'D':return 13;
            case 'e':
            case 'E':return 14;
            case 'f':
            case 'F':return 15;
        }
        return 0;
    }
}
