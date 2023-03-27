package com.example.qrlockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class updateUserID extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    EditText eUserID;
    Button updateUserID_Btn;
    @SuppressLint("MissingInflatedId")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            mAuth = FirebaseAuth.getInstance();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update_user_id);
            eUserID = findViewById(R.id.eUserID);
            FirebaseUser user=mAuth.getCurrentUser();
            updateUserID_Btn = findViewById(R.id.updateUserID_Btn);
            updateUserID_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String displayName = eUserID.getText().toString();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(updateUserID.this,user.getDisplayName(),Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(updateUserID.this, MainActivity2.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            });
        }
}