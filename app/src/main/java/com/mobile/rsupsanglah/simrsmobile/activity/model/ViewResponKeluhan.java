package com.mobile.rsupsanglah.simrsmobile.activity.model;

public class ViewResponKeluhan {

    String waktu_mulai, waktu_selesai, jenis_pekerjaan, nama_pelaksana, no_order_view, id_pelaksana, id_respon;
    int respon;

    public ViewResponKeluhan() {
    }

    public ViewResponKeluhan(int respon, String waktu_mulai, String waktu_selesai, String jenis_pekerjaan, String nama_pelaksana, String no_order_view, String id_pelaksana, String id_respon) {
        this.respon = respon;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
        this.jenis_pekerjaan = jenis_pekerjaan;
        this.nama_pelaksana = nama_pelaksana;
        this.no_order_view = no_order_view;
        this.id_pelaksana = id_pelaksana;
        this.id_respon = id_respon;
    }

    public String getNoOrderfromView () { return no_order_view; }

    public void setNoOrderfromView (String no_order_view ) { this.no_order_view = no_order_view;}

    public int getRespon() {
        return respon;
    }

    public void setRespon(int respon) {
        this.respon = respon;
    }

    public String getWaktu_mulai() {
        return waktu_mulai;
    }

    public void setWaktu_mulai(String waktu_mulai) {
        this.waktu_mulai = waktu_mulai;
    }

    public String getWaktu_selesai() {
        return waktu_selesai;
    }

    public void setWaktu_selesai(String waktu_selesai) {
        this.waktu_selesai = waktu_selesai;
    }

    public String getJenis_pekerjaan() {
        return jenis_pekerjaan;
    }

    public void setJenis_pekerjaan(String jenis_pekerjaan) { this.jenis_pekerjaan = jenis_pekerjaan; }

    public String getNama_pelaksana() {
        return nama_pelaksana;
    }

    public void setNama_pelaksana(String nama_pelaksana) {
        this.nama_pelaksana = nama_pelaksana;
    }

    public String getId_pelaksana() {
        return id_pelaksana;
    }

    public void setId_pelaksana(String id_pelaksana) {
        this.id_pelaksana = id_pelaksana;
    }

    public String getIdResponKeluhan() {
        return id_respon;
    }

    public void setIdResponKeluhan(String id_respon) {
        this.id_respon = id_respon;
    }

}
