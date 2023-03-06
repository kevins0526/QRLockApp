package com.example.qrlockapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class guestKey extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener datePickerListener;
    DatePickerDialog.OnDateSetListener datePickerListener2;
    TimePickerDialog.OnTimeSetListener timePickerListener;
    TimePickerDialog.OnTimeSetListener timePickerListener2;
    DatePickerDialog datePickerDialog,datePickerDialog2;
    //時間選擇視窗
    TimePickerDialog timePickerDialog,timePickerDialog2;
    Calendar calendar,calendar2;
    //格式化
    SimpleDateFormat sdfDate,sdfTime,sdfDate2,sdfTime2;

    TextView textView,textView2;
    Button buttonDate,buttonTime,buttonDate2,buttonTime2,buttonBack;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        textView = findViewById(R.id.showTime);
        textView2 = findViewById(R.id.showTime2);
        buttonDate = findViewById(R.id.day_btn);
        buttonDate2 = findViewById(R.id.day_btn2);
        buttonTime = findViewById(R.id.time_btn);
        buttonTime2 = findViewById(R.id.time_btn2);
        buttonBack = findViewById(R.id.back_btn);
        //日期格式 yyyyMMdd
        sdfDate = new SimpleDateFormat("yyyyMMdd", Locale.TAIWAN);
        sdfDate2 = new SimpleDateFormat("yyyyMMdd",Locale.TAIWAN);
        //時間格式 hhMM
        sdfTime = new SimpleDateFormat("HH:mm",Locale.TAIWAN);
        sdfTime2 = new SimpleDateFormat("HH:mm",Locale.TAIWAN);
        //讓calendar抓取當前時間
        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
//--------------------------------------------------------------------------------------------------------
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(guestKey.this,MainActivity2.class);
                startActivity(intent);
            }
        });
        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                textView.setText(sdfDate.format(calendar.getTime()) +" "+ sdfTime.format(calendar.getTime()));
            }
        };
        datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar2.set(Calendar.YEAR,year);
                calendar2.set(Calendar.MONTH,month);
                calendar2.set(Calendar.DAY_OF_MONTH,day);

                textView2.setText(sdfDate2.format(calendar2.getTime()) +" "+ sdfTime2.format(calendar2.getTime()));
            }
        };
        /** 日期選擇視窗 **/
        datePickerDialog = new DatePickerDialog(guestKey.this,
                datePickerListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog2 = new DatePickerDialog(guestKey.this,
                datePickerListener2,
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH));
//--------------------------------------------------------------------------------------------------------

        /** 時間選擇監聽 **/
        timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);

                textView.setText(sdfDate.format(calendar.getTime()) +" "+ sdfTime.format(calendar.getTime()));

            }
        };
        timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar2.set(Calendar.HOUR_OF_DAY,hour);
                calendar2.set(Calendar.MINUTE,minute);

                textView2.setText(sdfDate2.format(calendar2.getTime()) +" "+ sdfTime2.format(calendar2.getTime()));

            }
        };
        /** 時間選擇視窗 **/
        timePickerDialog = new TimePickerDialog(guestKey.this,
                timePickerListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog2 = new TimePickerDialog(guestKey.this,
                timePickerListener2,
                calendar2.get(Calendar.HOUR_OF_DAY),
                calendar2.get(Calendar.MINUTE),
                true);
//--------------------------------------------------------------------------------------------------------
        /** 按鈕點擊監聽 **/
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        buttonDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog2.show();
            }
        });
        /** 按鈕點擊監聽 **/
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });
        buttonTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog2.show();
            }
        });
    }
}