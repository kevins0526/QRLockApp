package com.example.qrlockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Activity context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        TextView textView = findViewById(R.id.forgotPassword);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                //調用
                EditText txtUsername = findViewById(R.id.etUsername);
                EditText txtPassword = findViewById(R.id.etPassword);
//                //取值
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
//                //帳號密碼對跳到第二頁
//                if(username.equals("kevin")&&password.equals("123")) {
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this,MainActivity2.class);
//                    startActivity(intent);
//                }
//                else{
//                    String msg = "帳號或密碼錯誤!";
//                    TextView wrongPassword = findViewById(R.id.wrong);
//                    wrongPassword.setText(msg);
//                }

//                if(user == null){
//                    //...
//                } else{
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this,MainActivity2.class);
//                    startActivity(intent);
//                }
                //firebase註冊
//                mAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            FirebaseUser user = mAuth.getCurrentUser();
//                        }
//                    }
//                });

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    String msg = "帳號或密碼不得為空白!";
                    TextView wrongPassword = findViewById(R.id.wrong);
                    wrongPassword.setText(msg);
                } else {
                    mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MainActivity2.class);
                                startActivity(intent);
                            } else {
                                String msg = "帳號或密碼錯誤!";
                                TextView wrongPassword = findViewById(R.id.wrong);
                                wrongPassword.setText(msg);
                                txtUsername.setText(null);
                                txtPassword.setText(null);
                            }
                        }
                    });
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, forgotPassword.class);
                startActivity(intent);
            }
        });
    }
}