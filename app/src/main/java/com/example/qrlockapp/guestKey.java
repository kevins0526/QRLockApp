package com.example.qrlockapp;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class guestKey extends AppCompatActivity {
    Button backBtn,requestGuestKeyBtn;
    ImageView guestQrcodeView;
    EditText guestNameEdit;
    String guestName;
    public static String aesPassword;
    TextView countDownTimeTextView;
    SharedPreferences pref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Bitmap bit;
    long IV;
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
            String tempKey=read();
            BarcodeEncoder encoder1 = new BarcodeEncoder();
                    try {
                        Bitmap bit = encoder1.encodeBitmap(tempKey, BarcodeFormat.QR_CODE, 800, 800);
                        guestQrcodeView.setImageBitmap(bit);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
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
                String name = guestNameEdit.getText().toString();
                guestName = "guest_"+name;
                //countDownTimeTextView.setText(name+"訪客鑰匙到期時間為"+getDateTime());
                IV=Randomize.IV();
                aesPassword=AEScbc.encrypt(guestName,String.valueOf(IV));
                saveName();
                updateAesPassword(aesPassword,String.valueOf(IV));
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
    public void updateAesPassword(String aesPassword,String IV){
        DatabaseReference userPassword =database.getReference("/aesPassword/"+aesPassword);
        userPassword.setValue(IV);
    }
    public void deleteAesPassword(String aesPassword){
        DatabaseReference userPassword =database.getReference("/aesPassword/"+aesPassword);
        userPassword.removeValue();
        DatabaseReference deleteUserID =database.getReference("/userID/"+guestName);
        deleteUserID.removeValue();
    }
    public void countDownTime(){
        final GlobalVariable app = (GlobalVariable) getApplication();
       new CountDownTimer(60000, 1000) {
            //int time = 60000*30;
            int time = 60;
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimeTextView.setText("訪客鑰匙 "+time+" 秒後失效!");
                time--;
            }
            @Override
            public void onFinish() {//结束后的操作
                deleteAesPassword(aesPassword);
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
                .putString("KEY",aesPassword)
                .apply();                   //或commit()
    }
    //讀取資料
    public String read(){
        pref = getSharedPreferences("DATA",MODE_PRIVATE);
        String name = pref.getString("NAME","");
        String key = pref.getString("KEY","");
        countDownTimeTextView.setText(name+"訪客鑰匙時效截止"+getDateTime());
        return key;
    }
    //清除EditTexts內容
    public void clear(){
        pref = getSharedPreferences("DATA",MODE_PRIVATE);
        //下面程式碼能清除所有資料
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

    }
    private String getDateTime() {
        // 獲取當前時間
        Calendar calendar = Calendar.getInstance();
        // 添加30分鐘
        calendar.add(Calendar.SECOND, 10);
        calendar.add(Calendar.HOUR,8);
        // 將時間格式化為你需要的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String newTime = sdf.format(calendar.getTime());
        return newTime;
    }

}