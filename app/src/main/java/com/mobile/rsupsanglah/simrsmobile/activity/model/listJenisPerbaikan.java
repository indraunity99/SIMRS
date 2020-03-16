package com.mobile.rsupsanglah.simrsmobile.activity.model;

import java.io.Serializable;

public class listJenisPerbaikan implements Serializable {

    String menu, menu_id;

    public listJenisPerbaikan() {
    }

    public listJenisPerbaikan(String menu, String menu_id) {
        this.menu = menu;
        this.menu_id = menu_id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu (String menu) {
        this.menu = menu;
    }

    public String getMenuId() {
        return menu_id;
    }

    public void setMenuId (String menu_id) {
        this.menu_id = menu_id;
    }


}
