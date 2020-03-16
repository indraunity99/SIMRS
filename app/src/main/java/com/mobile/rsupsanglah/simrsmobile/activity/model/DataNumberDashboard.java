package com.mobile.rsupsanglah.simrsmobile.activity.model;

import java.io.Serializable;

public class DataNumberDashboard implements Serializable {

    int  menunggu, dikerjakan, pending, selesai;
    int total;

    public DataNumberDashboard() {
    }

    public DataNumberDashboard(int total, int menunggu, int dikerjakan, int pending, int selesai) {
        this.total = total;
        this.menunggu = menunggu;
        this.dikerjakan = dikerjakan;
        this.pending = pending;
        this.selesai = selesai;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal (int total) {
        this.total = total;
    }

    public int getMenunggu() {
        return menunggu;
    }

    public void setMenunggu (int menunggu) {
        this.menunggu = menunggu;
    }

    public int getDikerjakan() {
        return dikerjakan;
    }

    public void setDikerjakan (int dikerjakan) {
        this.dikerjakan = dikerjakan;
    }

    public int getPending() {
        return pending;
    }

    public void setPending (int pending) {
        this.pending = pending;
    }

    public int getSelesai() {
        return selesai;
    }

    public void setSelesai (int selesai) {
        this.selesai = selesai;
    }


}
