package com.example.qrlockapp;


import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
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



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fragment1 extends Fragment{
    private FirebaseAuth mAuth;
    Button CreateBtn,GuestBtn;
    String aesPassword="";
    SharedPreferences pref;
    String displayName;
    ImageView ivCode;
    long IV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  //fragment 視圖
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        pref = getActivity().getSharedPreferences("PREF",MODE_PRIVATE);
        aesPassword=readKey();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        displayName = user.getDisplayName();
        ivCode = (ImageView)myView.findViewById(R.id.imageView4);
        if(!displayName.equals(readDisplayName())){
            aesPassword = "";
        }else{
            aesPassword = readKey();
        }
        if(aesPassword.equals("")){
            IV=Randomize.IV();
            aesPassword=AEScbc.encrypt(displayName,String.valueOf(IV));
            saveKey();
            //updateFirebaseValue(aesPassword,IV); //傳加密後密碼到firebase
            BarcodeEncoder encoder = new BarcodeEncoder();
            try {
                Bitmap bit = encoder.encodeBitmap(aesPassword, BarcodeFormat.QR_CODE, 1000, 1000);
                ivCode.setImageBitmap(bit);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else{
            BarcodeEncoder encoder = new BarcodeEncoder();
            try {
                Bitmap bit = encoder.encodeBitmap(aesPassword, BarcodeFormat.QR_CODE, 1000, 1000);
                ivCode.setImageBitmap(bit);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        CreateBtn = (Button) myView.findViewById(R.id.create_btn);
        GuestBtn = (Button) myView.findViewById(R.id.guest_btn);

        CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference deleteKey = database.getReference("/passwordList/"+aesPassword);
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
        displayName = user.getDisplayName();
        DatabaseReference loginTime = database.getReference("/userID/"+displayName+"/loginTime");
        loginTime.addValueEventListener(new ValueEventListener() {
            int times = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(times>0) {
                    getCode();
                }
                times++;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return  myView;
    }
    public void getCode() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        displayName = user.getDisplayName();
        IV=Randomize.IV();
        aesPassword=AEScbc.encrypt(displayName,String.valueOf(IV));
        saveKey();
        //updateFirebaseValue(aesPassword,IV); //傳加密後密碼到firebase
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(aesPassword, BarcodeFormat.QR_CODE, 1000, 1000);
            ivCode.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }



//    public void updateFirebaseValue(String aesPas,long IV){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user=mAuth.getCurrentUser();
//        displayName = user.getDisplayName();
//        DatabaseReference AesPassword = database.getReference("/passwordList/"+aesPas); //讀取的根結點
//        DatabaseReference ivKey=database.getReference("/userID/"+displayName+"/ivKey");
//        ivKey.setValue(IV);
//        AesPassword.setValue(displayName);
//    }
    public void jumpToGuest(){
        Intent intent = new Intent(getActivity(),guestKey.class);
        startActivity(intent);
    }
    public void saveKey(){
        pref.edit()
                .putString("KEY",aesPassword)
                .putString("DisplayName",displayName)
                .apply();                   //或commit()
    }
    public String readKey(){
        return pref.getString("KEY","");
    }
    public String readDisplayName(){
        return pref.getString("DisplayName","");
    }
}
