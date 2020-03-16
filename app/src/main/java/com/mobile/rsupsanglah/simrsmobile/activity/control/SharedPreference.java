package com.mobile.rsupsanglah.simrsmobile.activity.control;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreference {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";
    private static final String TAG_IP = "tagip";
    private static final String TAG_APP_USER = "tagappuser";
    private static final String TAG_ACT_FRAG = "tagactfrag";
    private static final String TAG_IPPGS = "tagippgs";
    private static final String TAG_MENU_ID = "tagmenuid";
    private static final String TAG_TMPT_TGS = "tagtmpttgs";

    private static SharedPreference mInstance;
    private static Context thiscontext;

    private SharedPreference(Context context) {
        thiscontext = context;
    }

    public static synchronized SharedPreference getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreference(context);
        }
        return mInstance;
    }

    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    public String getDeviceToken(){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN, null);
    }

    public boolean saveipAddress(String ip){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_IP, ip);
        editor.apply();
        return true;
    }

    public String getIpAddress(){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_IP, null);
    }

    public boolean saveAppUser(String app){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_APP_USER, app);
        editor.apply();
        return true;
    }

    public String getAppUser(){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_APP_USER, null);
    }

    public boolean saveActtoFrag(String text){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_ACT_FRAG, text);
        editor.apply();
        return true;
    }

    public String getActtoFrag(){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_ACT_FRAG, null);
    }

    public boolean saveUSerIPPGS(String ippgs){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_IPPGS, ippgs);
        editor.apply();
        return true;
    }

    public String getUSERIPPGS(){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_IPPGS, null);
    }

    public void saveArrayList(ArrayList<String> list, String key){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public boolean saveMenuId(String menu_id){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_MENU_ID, menu_id);
        editor.apply();
        return true;
    }

    public String getMenuId(){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_MENU_ID, null);
    }

    public boolean saveTempatTugas(String tmpttgs){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TMPT_TGS, tmpttgs);
        editor.apply();
        return true;
    }

    public String getTempatTugas(){
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TMPT_TGS, null);
    }

}