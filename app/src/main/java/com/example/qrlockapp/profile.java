package com.example.qrlockapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {
    TextView name,age,sex,birthday;
    Button backBtn,submit,update;
    AlertDialog dialog;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getProfile();
        update = findViewById(R.id.updateProfie);
        backBtn = findViewById(R.id.backBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新資料");
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
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();
                String displayName = user.getDisplayName();
                DatabaseReference userName =database.getReference("/userID/"+displayName+"/userName");
                DatabaseReference userAge =database.getReference("/userID/"+displayName+"/userAge");
                DatabaseReference userSex =database.getReference("/userID/"+displayName+"/userSex");
                DatabaseReference userBirthday =database.getReference("/userID/"+displayName+"/userBirthday");
                userName.setValue(eName.getText().toString());
                userAge.setValue(eAge.getText().toString());
                userSex.setValue(eSex.getText().toString());
                userBirthday.setValue(eBirthday.getText().toString());
                getProfile();
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
    public void getProfile(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String displayName = user.getDisplayName();
        name = findViewById(R.id.fullName);
        age = findViewById(R.id.age);
        sex = findViewById(R.id.sex);
        birthday = findViewById(R.id.birthday);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userName =database.getReference("/userID/"+displayName+"/userName");
        DatabaseReference userAge =database.getReference("/userID/"+displayName+"/userAge");
        DatabaseReference userSex =database.getReference("/userID/"+displayName+"/userSex");
        DatabaseReference userBirthday =database.getReference("/userID/"+displayName+"/userBirthday");
        userName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                name.setText("姓名 :"+value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        userAge.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                age.setText("年齡 :"+value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        userSex.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                sex.setText("性別 :"+value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        userBirthday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                birthday.setText("生日 :"+value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}