package com.example.qrlockapp;

import android.app.Application;
import android.util.Log;

public class GlobalVariable extends Application {
    private int b =30;
    // 定義全域變數
    public static String floor;
    public static boolean lockFloor = false;
    private boolean switchGuest = true;
    public boolean switchGuest(){
        return this.switchGuest;
 }
    public void getSwitchGuest(boolean o){
        this.switchGuest = o;
    }
    // 設定全域變數的方法
    public void setFloor(String value) {
        this.floor = value;
        onLockFloor();
        Log.e("SET",floor);
    }

    // 取得全域變數的方法
    public String getFloor() {
        return this.floor;
    }
    public void onLockFloor(){
        this.lockFloor = true;
    }
    public void offLockFloor(){
        this.lockFloor = false;
    }
}
