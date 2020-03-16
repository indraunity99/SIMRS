package com.mobile.rsupsanglah.simrsmobile.activity.model;

public class listInputBahan {

    String nama_bahan, jumlah_bahan, id_bahan, satuan_bahan, id_bahan_view;

    public listInputBahan() {
    }

    public listInputBahan(String nama_bahan, String jumlah_bahan, String id_bahan, String satuan_bahan, String id_bahan_view) {
        this.nama_bahan = nama_bahan;
        this.jumlah_bahan = jumlah_bahan;
        this.id_bahan = id_bahan;
        this.satuan_bahan = satuan_bahan;
        this.id_bahan_view = id_bahan_view;
    }

    public String getNama_bahan() {
        return nama_bahan;
    }

    public void setNama_bahan(String nama_bahan) {
        this.nama_bahan = nama_bahan;
    }

    public String getJumlah_bahan() {
        return jumlah_bahan;
    }

    public void setJumlah_bahan(String jumlah_bahan) {
        this.jumlah_bahan = jumlah_bahan;
    }

    public String getIdBahan(){ return id_bahan; }

    public void setIdBahan(String id_bahan) { this.id_bahan = id_bahan; }

    public String getSatuanBahan(){ return satuan_bahan; }

    public void setSatuanBahan(String satuan_bahan) { this.satuan_bahan = satuan_bahan; }

    public String getIdBahanatView(){ return id_bahan_view; }

    public void setIdBahanatView(String id_bahan_view) { this.id_bahan_view = id_bahan_view; }
}
