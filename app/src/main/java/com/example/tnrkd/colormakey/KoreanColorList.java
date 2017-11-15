package com.example.tnrkd.colormakey;

import java.util.ArrayList;


public class KoreanColorList {
    public static ArrayList<ko_color> ko_colorlist=new ArrayList<ko_color>();
    public static String getName(int[] tar){
        float max=0;
        int index=-1;
        for(int i=0;i<ko_colorlist.size();i++){
            float percent=getPercent(ko_colorlist.get(i).rgb,tar);
            if(percent>max) {
                max = percent;
                index = i;
            }
        }
        return ko_colorlist.get(index).name;
    }
    private static float getPercent(int[] A,int[] B) {
        float rmean=(float)(A[0]+B[0])/2;
        float r=A[0]-B[0];
        float g=A[1]-B[1];
        float b=A[2]-B[2];
        float c=(float)Math.sqrt((2+r/256)*r*r+4*g*g+(2+(255-r)/256)*b*b);
        return (float) Math.round(((764.834-c)*1000/764.834))/10;
    }
}
class ko_color{
    public String name;
    private String hex;
    public int[] rgb;
    public ko_color(String name,String hex){
        this.name=name;
        this.hex=hex;
        rgb=func(hex);
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