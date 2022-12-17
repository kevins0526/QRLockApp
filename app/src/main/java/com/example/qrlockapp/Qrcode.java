package com.example.qrlockapp;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Qrcode extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    public void getCode() {
        ImageView ivCode = (ImageView) findViewById(R.id.imageView2);
        EditText etContent = (EditText) findViewById(R.id.editText2);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(etContent.getText().toString()
                    , BarcodeFormat.QR_CODE, 250, 250);
            ivCode.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        getCode();
    }
}