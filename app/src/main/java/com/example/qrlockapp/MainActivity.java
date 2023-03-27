package com.example.qrlockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private SharedPreferences pref;//暫時存取字串用
    private SharedPreferences.Editor editor;
    private Button login;
    private CheckBox rememberPass;
    Activity context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        TextView textView = findViewById(R.id.forgotPassword);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        EditText txtUsername = findViewById(R.id.etUsername);
        EditText txtPassword = findViewById(R.id.etPassword);
        rememberPass = findViewById(R.id.remember_account);
        login = (Button) findViewById(R.id.button);
        boolean isRemember = pref.getBoolean("remember_account",false);
        if(isRemember){
            String account = pref.getString("account","");//取pref已存帳號
            String password = pref.getString("password","");//取pref已存密碼
            txtUsername.setText(account);
            txtPassword.setText(password);
            rememberPass.setChecked(true);
        }
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                //調用
                EditText txtUsername = findViewById(R.id.etUsername);
                EditText txtPassword = findViewById(R.id.etPassword);
//                //取值
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
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
                    editor=pref.edit();//取得pref存入功能
                    if(rememberPass.isChecked()){
                        editor.putBoolean("remember_account",true);
                        editor.putString("account",username);//存帳號
                        editor.putString("password",password);//存密碼
                    }else{
                        editor.clear();
                    }
                    editor.commit();//提交
                    mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(user.getDisplayName() == null){
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, updateUserID.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, MainActivity2.class);
                                    startActivity(intent);
                                }
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