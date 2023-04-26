package com.example.qrlockapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class GlobalVariable extends Application {
    // 定義全域變數
    public static String floor;
    public static String lockName; //門鎖代碼
    public  static String aesPassword;
    public static boolean lockFloor = false;
    private boolean switchGuest = true;
    private SharedPreferences pref;//暫時存取字串用
    public boolean switchGuest(){
        return this.switchGuest;
 }
    public void getSwitchGuest(boolean o){
        this.switchGuest = o;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pref = getSharedPreferences("PREF",MODE_PRIVATE);
        lockName = readLock();
        //問題:要重新啟動後才會更改
    }

    // 設定全域變數的方法
    public void setFloor(String value) {
        this.floor = value;
        onLockFloor();
        Log.e("SET",floor);
    }

    // 取得全域變數的方法
    public String getLockName() {
        return lockName;
    }
    public void setLockName(String name) {
        this.lockName = name;
    }
    public void onLockFloor(){
        this.lockFloor = true;
    }
    public void offLockFloor(){
        this.lockFloor = false;
    }
    public void saveLock(String lockName){
        pref.edit()
                .putString("LOCK",lockName)
                .apply();                   //或commit()
    }
    public String readLock(){
        return pref.getString("LOCK","");
    }
}
