package com.appproject.tnrkd.colormakey.dto;

/**
 * Created by XNOTE on 2017-10-02.
 */

public class User {

    private String email;
    private String name;
    private Palette palette;

    public User() {

    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(String email, String name, Palette palette) {
        this.email = email;
        this.name = name;
        this.palette = palette;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }
}
