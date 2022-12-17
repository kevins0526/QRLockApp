package com.example.qrlockapp;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class fragment1 extends Fragment implements View.OnClickListener{
    View view;
    Button CreateBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        CreateBtn = (Button) myView.findViewById(R.id.create_btn);
        CreateBtn.setOnClickListener(this);
        return  myView;
    }
    public void getCode() {
        ImageView ivCode = (ImageView)getView().findViewById(R.id.imageView4);
        EditText etContent = (EditText)getView().findViewById(R.id.editText3);
        //mixKey(etContent.getText().toString());
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(etContent.getText().toString()
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