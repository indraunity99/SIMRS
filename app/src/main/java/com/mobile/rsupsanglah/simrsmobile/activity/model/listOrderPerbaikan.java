package com.mobile.rsupsanglah.simrsmobile.activity.model;


import java.io.Serializable;

public class listOrderPerbaikan implements Serializable {

    String id, nama_order;
    String jenis_order;

    public listOrderPerbaikan() {
    }

    public listOrderPerbaikan(String id, String nama_order, String jenis_order) {
        this.id = id;
        this.nama_order = nama_order;
        this.jenis_order = jenis_order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaOrder() {
        return nama_order;
    }

    public void setNamaOrder(String nama_order) {
        this.nama_order = nama_order;
    }

    public String getJenisOrder() {
        return jenis_order;
    }

    public void setJenisOrder(String jenis_order) {
        this.jenis_order = jenis_order;
    }

}

