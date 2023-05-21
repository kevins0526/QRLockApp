package com.example.qrlockapp;

import static com.example.qrlockapp.GlobalVariable.lockName;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class fragment4 extends Fragment {
    View view;
    private FirebaseAuth mAuth;
    TextView member;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    Button Hint,resdient,add;
    private List<Object> mData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment4, container, false);
        mData = new ArrayList<>();
        mAdapter = new MyAdapter(mData);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String displayName = user.getDisplayName();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/Hint");

        // 使用orderByKey()來依時間排序，並使用limitToLast()來取得最後十筆
        Query query = ref.orderByKey().limitToLast(15);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                List<String> HintEntries = new ArrayList<String>();
                List<String> E531Entries = new ArrayList<String>();


                // 迭代資料並取出
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    if(key.equalsIgnoreCase("allHint")){
                        String imform = childSnapshot.getValue(String.class);

                        HintEntries.add(imform);

                    }else {
                        String imform = childSnapshot.getValue(String.class);
                        E531Entries.add(imform);
                    }
                    count++;
                }

                Collections.reverse(HintEntries);
                mData.addAll(HintEntries);
                mAdapter.notifyDataSetChanged();

                Hint=(Button)view.findViewById(R.id.Hint);
                resdient=(Button)view.findViewById(R.id.resident);
                add=(Button) view.findViewById(R.id.add);

                Hint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mData.removeAll(HintEntries);
                        mData.removeAll(E531Entries);
                        Collections.reverse(HintEntries);
                        mData.addAll(HintEntries);
                        mAdapter.notifyDataSetChanged();
                    }
                });

                resdient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mData.removeAll(HintEntries);
                        mData.removeAll(E531Entries);
                        Collections.reverse(E531Entries);
                        mData.addAll(E531Entries);
                        mAdapter.notifyDataSetChanged();
                    }
                });



                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchToActivity();
                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        return view;
    }
    public void switchToActivity() {
        // 获取宿主 Activity 的引用
        Activity hostActivity = getActivity();

        // 创建一个 Intent 对象，指定要切换到的目标 Activity
        Intent intent = new Intent(hostActivity, PushInform.class);

        // 使用宿主 Activity 启动目标 Activity
        hostActivity.startActivity(intent);
    }
}