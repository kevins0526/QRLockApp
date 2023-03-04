package com.example.qrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
public class guestKey extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button backBtn,requestGuestKeyBtn;
    ImageView guestQrcodeView;
    EditText guestNameEdit;
    String aesPassword;
    TextView countDownTimeTextView;
    private GlobalVariable gv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        backBtn = findViewById(R.id.back_btn);
        requestGuestKeyBtn = findViewById(R.id.requestGuestKey);
        guestQrcodeView = findViewById(R.id.guestQrcode);
        guestNameEdit = findViewById(R.id.guestName);
        countDownTimeTextView = findViewById(R.id.countDownTime);
        final GlobalVariable app = (GlobalVariable) getApplication();
        if(app.switchGuest()){
            requestGuestKeyBtn.setEnabled(true);
        }else{
            requestGuestKeyBtn.setEnabled(false);
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        requestGuestKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestGuestKeyBtn.setEnabled(false);
                app.getSwitchGuest(false);
                String guestName = guestNameEdit.getText().toString();
                countDownTimeTextView.setText("已新增訪客 "+guestName);
                aesPassword=AES.cbcEncrypt(guestName,"1234567812344248");
                updateFirebaseGuestValue(aesPassword);
                BarcodeEncoder encoder = new BarcodeEncoder();
                try {
                    Bitmap bit = encoder.encodeBitmap(aesPassword, BarcodeFormat.QR_CODE, 1000, 1000);
                    guestQrcodeView.setImageBitmap(bit);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                countDownTime();
            }
        });
    }
    public void updateFirebaseGuestValue(String AesPas){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference AesPassword=database.getReference("/guestList/"+guestNameEdit.getText().toString()+"/AesPassword");
        AesPassword.setValue(AesPas);
    }
    public void removeFirebaseGuestValue(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference AesPassword=database.getReference("/guestList/"+guestNameEdit.getText().toString()+"/AesPassword");
        AesPassword.removeValue();
    }
    public void countDownTime(){
        final GlobalVariable app = (GlobalVariable) getApplication();
       new CountDownTimer(10000, 1000) {
            int time = 10;
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimeTextView.setText("訪客鑰匙 "+time+" 分鐘後失效!");
                time--;
            }
            @Override
            public void onFinish() {//结束后的操作
                removeFirebaseGuestValue();
                countDownTimeTextView.setText("可以新增訪客鑰匙囉~");
                requestGuestKeyBtn.setEnabled(true);
                app.getSwitchGuest(true); //時間過後再進才能重新生成
            }
        }.start();
    }

}