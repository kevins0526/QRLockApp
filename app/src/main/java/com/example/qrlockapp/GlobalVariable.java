package com.example.qrlockapp;

import android.app.Application;

public class GlobalVariable extends Application {
    private int b =30;
    private boolean switchGuest = true;
    public boolean switchGuest(){
        return this.switchGuest;
 }
    public void getSwitchGuest(boolean o){
        this.switchGuest = o;
    }
    public int theTime(int a){
        this.b = a;
        return a;
    }
}
