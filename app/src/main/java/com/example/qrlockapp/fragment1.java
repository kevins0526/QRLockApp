package com.example.qrlockapp;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fragment1 extends Fragment implements View.OnClickListener{

    Button CreateBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  //fragment 視圖
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        CreateBtn = (Button) myView.findViewById(R.id.create_btn);
        CreateBtn.setOnClickListener(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference password = database.getReference("password"); //讀取的根結點
        password.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                TextView password = (TextView)getView().findViewById(R.id.seepassword);
                password.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        //getCode();
        return  myView;
    }
    public void getCode() {
        ImageView ivCode = (ImageView)getView().findViewById(R.id.imageView4);
        //mixKey(etContent.getText().toString());
        TextView password = (TextView)getView().findViewById(R.id.seepassword);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(password.getText().toString()
                    , BarcodeFormat.QR_CODE, 1000, 1000);
            ivCode.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    public void mixKey(String origin){ //混合原始資料

    }
    public void onClick(View v) {
        getCode();
    }

}
