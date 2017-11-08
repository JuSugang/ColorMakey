package com.example.tnrkd.colormakey;

import android.*;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.tnrkd.colormakey.dto.Color;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by XNOTE on 2017-09-27.
 */

public class Global {
    public static GoogleSignInResult result;

    public static String userEmail;
    public static String userName;
    public static String userUID;

    public static ArrayList<Color> colors;
    public static ArrayList<colorList> list=new ArrayList<colorList>();

    public static boolean logoutFlag = false;

    public static AppCompatActivity activity;
    public static LoginActivity loginActivity;

    public static int statusBar;

    public static void requestExternalStoragePermission(Activity activity, int requestCode) {
        if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
            }else {
                ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
            }
        }else {

        }
    }

    public static void requestCameraPermission(Activity activity, int requestCode) {
        if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.CAMERA},requestCode);
            }else {
                ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.CAMERA}, requestCode);
            }
        }else {

        }
    }
}
