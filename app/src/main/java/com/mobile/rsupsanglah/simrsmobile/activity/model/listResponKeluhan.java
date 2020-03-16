package com.mobile.rsupsanglah.simrsmobile.activity.model;

import java.io.Serializable;

public class listResponKeluhan implements Serializable {


    String id, jenis_order, pelapor, keluhan, no_order, query;

    public listResponKeluhan() {
    }

    public listResponKeluhan(String id, String jenis_order, String pelapor, String keluhan, String no_order, String query) {
        this.id = id;
        this.keluhan = keluhan;
        this.jenis_order = jenis_order;
        this.pelapor = pelapor;
        this.no_order = no_order;
        this.query = query;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJenisOrder() {
        return jenis_order;
    }

    public void setJenisOrder(String jenis_order) {
        this.jenis_order = jenis_order;
    }

    public String getPelapor() {
        return pelapor;
    }

    public void setPelapor(String pelapor) {
        this.pelapor = pelapor;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public String getNoOrder() {
        return no_order;
    }

    public void setNoOrder(String no_order) {
        this.no_order = no_order;
    }

    public String getquery() {
        return query;
    }

    public void setquery (String query) {
        this.query = query;
    }

}
