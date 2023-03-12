package com.example.qrlockapp;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fragment1 extends Fragment{
    private FirebaseAuth mAuth;
    Button CreateBtn,GuestBtn;
    String AesPassword="";
    SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  //fragment 視圖
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        AesPassword=readKey();

        if(AesPassword!=""){
            //deletedKey();
            ImageView ivCode = (ImageView)myView.findViewById(R.id.imageView4);
            BarcodeEncoder encoder = new BarcodeEncoder();
            try {
                Bitmap bit = encoder.encodeBitmap(AesPassword, BarcodeFormat.QR_CODE, 1000, 1000);
                ivCode.setImageBitmap(bit);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else{
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();
            String uid = user.getUid();
            ImageView ivCode = (ImageView)myView.findViewById(R.id.imageView4);
            long IV=Randomize.IV();
            AesPassword = mixKey(uid,IV);
            saveKey();
            Toast.makeText(getActivity(), AesPassword, Toast.LENGTH_SHORT).show();
            updateFirebaseValue(AesPassword,IV); //傳加密後密碼到firebase
            BarcodeEncoder encoder = new BarcodeEncoder();
            try {
                Bitmap bit = encoder.encodeBitmap(AesPassword, BarcodeFormat.QR_CODE, 1000, 1000);
                ivCode.setImageBitmap(bit);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        CreateBtn = (Button) myView.findViewById(R.id.create_btn);
        GuestBtn = (Button) myView.findViewById(R.id.guest_btn);
        //CreateBtn.setOnClickListener(this);

        CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference deleteKey = database.getReference("/passwordList/"+AesPassword);
                deleteKey.removeValue();
                getCode();
            }
        });
        GuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToGuest();
            }
        });
        return  myView;
    }
    public void getCode() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String uid = user.getUid();
        ImageView ivCode = (ImageView)getView().findViewById(R.id.imageView4);
        //TextView password = (TextView)getView().findViewById(R.id.seepassword);
        long IV=Randomize.IV();
        AesPassword = mixKey(uid,IV);
        saveKey();
        Toast.makeText(getActivity(), AesPassword, Toast.LENGTH_SHORT).show();
        updateFirebaseValue(AesPassword,IV); //傳加密後密碼到firebase
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(AesPassword, BarcodeFormat.QR_CODE, 1000, 1000);
            ivCode.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    public String mixKey(String aes,long IV){ //混合原始資料
        //String str=AES.encrypt(aes);
        //Log.i("-=-=解密",AES.decrypt(str));
        String str=AES.cbcEncrypt(aes,String.valueOf(IV));
        return str;
    }
//    public void onClick(View v) {
//        getCode();
//    }


    public void updateFirebaseValue(String aesPas,long IV){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference AesPassword = database.getReference("/passwordList/"+aesPas); //讀取的根結點
        DatabaseReference ivKey=database.getReference("/userID/"+uid+"/ivKey");
        ivKey.setValue(IV);
        AesPassword.setValue(uid);
    }
    public void jumpToGuest(){
        Intent intent = new Intent(getActivity(),guestKey.class);
        startActivity(intent);
    }
    public void saveKey(){
        pref = getActivity().getSharedPreferences("PREF",MODE_PRIVATE);
        pref.edit()
                .putString("KEY",AesPassword)
                .apply();                   //或commit()
    }
    public String readKey(){
        pref = getActivity().getSharedPreferences("PREF",MODE_PRIVATE);
        String key = pref.getString("KEY","");
        return key;
    }
    public void deletedKey(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference deleteKey = database.getReference("/passwordList/"+AesPassword);
        deleteKey.removeValue();
    }
}
