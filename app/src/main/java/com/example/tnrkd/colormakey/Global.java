package com.example.tnrkd.colormakey;

import com.example.tnrkd.colormakey.dto.Color;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

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

}
