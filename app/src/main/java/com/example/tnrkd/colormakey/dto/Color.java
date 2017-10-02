package com.example.tnrkd.colormakey.dto;

/**
 * Created by XNOTE on 2017-10-02.
 */

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

    public int[] getRGBarray(){
        int temp = Integer.parseInt(rgbcode);
        int[] rgb=new int[3];
        rgb[0]=temp/1000000;
        rgb[1]=temp%1000000/1000;
        rgb[2]=temp%1000;
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
