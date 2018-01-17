package com.appproject.tnrkd.colormakey.dto;

import java.util.ArrayList;

/**
 * Created by XNOTE on 2017-10-02.
 */

public class Palette {

    private ArrayList<Color> color;

    public Palette() {

    }

    public Palette(ArrayList<Color> color) {
        this.color = color;
    }

    public ArrayList<Color> getColor() {
        return color;
    }

    public void setColor(ArrayList<Color> color) {
        this.color = color;
    }
}
