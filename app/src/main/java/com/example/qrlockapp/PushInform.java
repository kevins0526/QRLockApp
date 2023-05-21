package com.example.qrlockapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PushInform extends AppCompatActivity {
    FirebaseDatabase database;
    Button push;
    ImageButton back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.addinform);
        View view = getLayoutInflater().inflate(R.layout.addinform, null);
        push=(Button) view.findViewById(R.id.informsubmit);
        back= (ImageButton) view.findViewById(R.id.backButton);
        EditText whom,massage;
        whom =view.findViewById(R.id.Whom);
        massage=view.findViewById(R.id.massage);

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BUTTON", "onClick: ");
                DatabaseReference ref=database.getReference("/Hint"+whom);
                ref.setValue(massage.getText().toString());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }




}
