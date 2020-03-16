package com.mobile.rsupsanglah.simrsmobile.activity.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class listImages implements Serializable {

    String nama_images, string_img;
    Bitmap images;

    public listImages() {
    }

    public listImages(String nama_images, Bitmap images) {
        this.nama_images = nama_images;
        this.images = images;
    }

    public String getNama_images() {
        return nama_images;
    }

    public void setNama_images (String nama_images) {
        this.nama_images = nama_images;
    }

    public Bitmap getImages() {
        return images;
    }

    public void setImages (Bitmap images) {
        this.images = images;
    }

    public String getStringImages() {
        return string_img;
    }

    public void setStringImages (String string_img) {
        this.string_img = string_img;
    }


}