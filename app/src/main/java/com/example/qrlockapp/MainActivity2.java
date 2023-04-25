package com.example.qrlockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity {
    NavigationBarView navigationBarView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();
            navigationBarView = findViewById(R.id.navigation);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new fragment1()).commit(); // 切換主畫面
            }
            navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment Fragment = null;
                    if (item.getItemId() == R.id.navigation_item1) {
                        Fragment = new fragment1();
                    } else if (item.getItemId() == R.id.navigation_item2) {
                        Fragment = new fragment2();
                    } else if (item.getItemId() == R.id.navigation_item3) {
                        Fragment = new fragment3();
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, Fragment).commit();
                    return true;
                }
            });

    }
    }

