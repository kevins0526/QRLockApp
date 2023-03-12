package com.example.qrlockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    SharedPreferences pref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Bitmap bit;
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
            requestGuestKeyBtn.setVisibility(View.VISIBLE);
            guestNameEdit.setVisibility(View.VISIBLE);
        }else{
            //requestGuestKeyBtn.setEnabled(false);
            requestGuestKeyBtn.setVisibility(View.GONE);
            guestNameEdit.setVisibility(View.GONE);
            String tempName=read();
            DatabaseReference guest =database.getReference("/guestList/"+tempName+"/AesPassword");
            guest.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String tempPassword = dataSnapshot.getValue(String.class);
                    BarcodeEncoder encoder1 = new BarcodeEncoder();
                    try {
                        Bitmap bit = encoder1.encodeBitmap(tempPassword, BarcodeFormat.QR_CODE, 800, 800);
                        guestQrcodeView.setImageBitmap(bit);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
                requestGuestKeyBtn.setVisibility(View.GONE);
                guestNameEdit.setVisibility(View.GONE);
                app.getSwitchGuest(false);
                String guestName = guestNameEdit.getText().toString();
                saveName();
                countDownTimeTextView.setText("已新增訪客 "+guestName);
                aesPassword=AES.cbcEncrypt(guestName,"1234567812344248");
                updateFirebaseGuestValue(aesPassword);
                BarcodeEncoder encoder = new BarcodeEncoder();
                try {
                    bit = encoder.encodeBitmap(aesPassword, BarcodeFormat.QR_CODE, 800, 800);
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
            //int time = 60000*30;
            int time = 10;
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimeTextView.setText("訪客鑰匙 "+time+" 分鐘後失效!");
                time--;
            }
            @Override
            public void onFinish() {//结束后的操作
                removeFirebaseGuestValue();
                clear();
                countDownTimeTextView.setText("可以新增訪客鑰匙囉~");
                requestGuestKeyBtn.setEnabled(true);
                app.getSwitchGuest(true); //時間過後再進才能重新生成
            }
        }.start();
    }
    public void saveName(){
        pref = getSharedPreferences("DATA",MODE_PRIVATE);
        pref.edit()
                .putString("NAME",guestNameEdit.getText().toString())
                .apply();                   //或commit()
    }
    //讀取資料
    public String read(){
        pref = getSharedPreferences("DATA",MODE_PRIVATE);
        String name = pref.getString("NAME","");
        countDownTimeTextView.setText("目前顧客鑰匙使用者: "+name);
        return name;
    }
    //清除EditTexts內容
    public void clear(){
        pref = getSharedPreferences("DATA",MODE_PRIVATE);
        //下面程式碼能清除所有資料
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

    }
}