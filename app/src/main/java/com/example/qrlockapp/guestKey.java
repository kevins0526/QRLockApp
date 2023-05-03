package com.example.qrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class guestKey extends AppCompatActivity {
    Button backBtn,requestGuestKeyBtn,shareBtn;
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
        shareBtn = findViewById(R.id.shareBtn);
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
                String name = guestNameEdit.getText().toString();
                if(name.equals("")){
                    Toast.makeText(guestKey.this, "請輸入訪客名稱喔!", Toast.LENGTH_SHORT).show();
                }else {
                    requestGuestKeyBtn.setVisibility(View.GONE);
                    guestNameEdit.setVisibility(View.GONE);
                    shareBtn.setVisibility(View.VISIBLE);
                    app.getSwitchGuest(false);
                    guestName = "guest_" + name;
                    IV = Randomize.IV();
                    aesPassword = AEScbc.encrypt(guestName, String.valueOf(IV));
                    saveName();
                    updateAesPassword(aesPassword, String.valueOf(IV));
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    try {
                        bit = encoder.encodeBitmap(aesPassword, BarcodeFormat.QR_CODE, 800, 800);
                        guestQrcodeView.setImageBitmap(bit);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    countDownTime();
                }
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bit == null) {
                    // 如果 bit 為空值，顯示提醒訊息
                    new AlertDialog.Builder(guestKey.this)
                            .setTitle("提醒")
                            .setMessage("沒有 QR Code 圖像可分享")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    // 建立一個分享對象 Intent
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);

                    // 設定分享對象的 MIME 類型為圖像
                    shareIntent.setType("image/*");

                    // 將 Bitmap 圖像加入到分享對象中
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bit, "QR Code", null);
                    Uri imageUri = Uri.parse(path);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

                    PopupMenu popupMenu = new PopupMenu(guestKey.this, shareBtn);
                    popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.line:
                                    // 呼叫 LINE 應用程式的分享功能
                                    shareIntent.setPackage("jp.naver.line.android");
                                    startActivity(shareIntent);
                                    break;
                                case R.id.messenger:
                                    // 執行 Messager 分享功能
                                    shareIntent.setPackage("com.facebook.orca");
                                    startActivity(shareIntent);
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
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
                shareBtn.setVisibility(View.GONE);
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