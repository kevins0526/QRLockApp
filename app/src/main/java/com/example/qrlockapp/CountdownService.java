package com.example.qrlockapp;

import static com.example.qrlockapp.guestKey.aesPassword;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CountdownService extends Service {
    private static final long COUNTDOWN_INTERVAL = 1000; // 1秒
    private static final long TOTAL_COUNTDOWN_TIME = 10000; // 60秒

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private NotificationManager notificationManager;
    NotificationChannel channel;
    SharedPreferences pref;
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化倒计时器
        timeLeftInMillis = TOTAL_COUNTDOWN_TIME;
        final GlobalVariable app = (GlobalVariable) getApplication();
        countDownTimer = new CountDownTimer(timeLeftInMillis, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                // 更新UI或执行其他必要的操作
            }

            @Override
            public void onFinish() {
                // 倒计时结束时执行的操作
                NotificationSend();
//                guestKey.deleteAesPassword(aesPassword);
//                clear();
//                countDownTimeTextView.setText("可以新增訪客鑰匙囉~");
//                shareBtn.setVisibility(View.GONE);
//                requestGuestKeyBtn.setEnabled(true);
                app.getSwitchGuest(true); //時間過後再進才能重新生成
                stopSelf(); // 停止Service
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (countDownTimer != null) {
            countDownTimer.start(); // 启动倒计时器
        }
        return START_STICKY; // 确保Service在被系统杀死后重新启动
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // 取消倒计时器
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void NotificationSend() {
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("ID", "notification_text_a", notificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            Intent intent = new Intent(this, guestKey.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent PI = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            android.app.Notification.Builder builder = new android.app.Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId("ID")
                    .setContentTitle("訪客鑰匙已到期")
                    .setContentText("訪客鑰匙已失效，可以重新生成囉!")
                    .setContentIntent(PI);

            android.app.Notification notificaion = builder.build();
            notificationManager.notify(0, notificaion);

        }
    }
    private String getDateTime() {
        // 獲取當前時間
        Calendar calendar = Calendar.getInstance();
        // 添加30分鐘
        calendar.add(Calendar.SECOND, 10);
        // 將時間格式化為你需要的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String newTime = sdf.format(calendar.getTime());
        return newTime;
    }
    public void clear(){
        pref = getSharedPreferences("DATA",MODE_PRIVATE);
        //下面程式碼能清除所有資料
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

    }
}

