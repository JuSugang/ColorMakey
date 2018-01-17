package com.appproject.tnrkd.colormakey.dto;

import java.util.ArrayList;

/**
 * Created by XNOTE on 2017-10-02.
 */

/**
 * setValue()의 경우 child 아래의 데이터를 모두 덮어 쓴다.
 * List 형태로 데이터를 저장했다면, 해당 List를 불러와서 자바 내의 List로 저장한 후에,
 * 자바 List에 add 메소드로 DTO를 추가한 후, 그 List를 setValue()의 인자로 전달하자.
 * */

public class Color {

    private String hexcode;
    private String rgbcode;
    private String colorname;

    public Color() {

    }

    public Color(String hexcode, String rgbcode, String colorname){
        this.hexcode=hexcode;
        this.rgbcode=rgbcode;
        this.colorname=colorname;
    }

    /**
     * Firebase Database에서 setValue()를 통해 DTO를 넣을 때,
     * getter뒤의 단어를 키값으로 하여 객체에 넣음
     *
     * 변수가 들어가는 것이 아닌 getXXX 에서의 XXX가 들어간다
     *
     * 또한, getter의 반환값이 배열이라면 데이터를 넣을 수 없다. List를 사용해야함
     * */
//    public ArrayList mGetRGBarray() {
//        int temp = Integer.parseInt(rgbcode);
//        ArrayList rgb = new ArrayList();
//        rgb.add(temp/1000000);
//        rgb.add(temp%1000000/1000);
//        rgb.add(temp%1000);
//        return rgb;
//    }
    public ArrayList mGetRGBarray() {
        int temp = Integer.parseInt(rgbcode);
        ArrayList<Float> rgb = new ArrayList<Float>();
        rgb.add((float)(temp/1000000));
        rgb.add((float)(temp%1000000/1000));
        rgb.add((float)(temp%1000));
        return rgb;
    }

    public String getHexcode(){
        return hexcode;
    }
    public String getColorname(){
        return  colorname;
    }

    public void setHexcode(String hexcode) {
        this.hexcode = hexcode;
    }

    public String getRgbcode() {
        return rgbcode;
    }

    public void setRgbcode(String rgbcode) {
        this.rgbcode = rgbcode;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }
}
