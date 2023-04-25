package com.example.qrlockapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment2 extends Fragment {
    View view;
    private FirebaseAuth mAuth;
    TextView member;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<Object> mData;
    String realName;
    String userFloor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mData = new ArrayList<>();
        mAdapter = new MyAdapter(mData);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String displayName = user.getDisplayName();
        DatabaseReference realNamePath = database.getReference("/userID/"+displayName+"/userName/");
        realNamePath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                realName = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference userFloorPath = database.getReference("/userID/"+displayName+"/房號/");
        userFloorPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userFloor = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference memberData = database.getReference("/Resident/"+userFloor+"/"+realName);
                memberData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Object Key = snapshot1.getKey();
                    mData.add(Key);
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}