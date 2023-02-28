package com.example.qrlockapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class profile extends AppCompatActivity {
    private FirebaseAuth Auth;
    TextView name,age,sex,birthday;
    Button backBtn,submit,update;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = findViewById(R.id.fullName);
        age = findViewById(R.id.age);
        sex = findViewById(R.id.sex);
        birthday = findViewById(R.id.birthday);
        update = findViewById(R.id.updateProfie);
        backBtn = findViewById(R.id.backBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Date");
        View view = getLayoutInflater().inflate(R.layout.updateprofile_dialog, null);
        EditText eName,eAge,eSex,eBirthday;
        eName = view.findViewById(R.id.eName);
        eAge = view.findViewById(R.id.eAge);
        eSex = view.findViewById(R.id.eSex);
        eBirthday = view.findViewById(R.id.eBirthday);
        submit = view.findViewById(R.id.submit);
        builder.setView(view);
        dialog=builder.create();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("姓名 :"+eName.getText().toString());
                age.setText("年齡 :"+eAge.getText().toString());
                sex.setText("性別 :"+eSex.getText().toString());
                birthday.setText("生日 :"+eBirthday.getText().toString());
                dialog.dismiss();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}