package com.example.nikhil.food4foodies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class OrderHistoryFragment extends Fragment {

    FirebaseAuth mFirebaseAuth;
    int count =0;
    ArrayList<cartitem> OrderList = new ArrayList<>();
    ArrayList<String> OrderidList = new ArrayList<>();

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        final List OrderList = new ArrayList<orderitem>();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.orderrecyclerview);


        mFirebaseAuth = FirebaseAuth.getInstance();
        String userId = mFirebaseAuth.getCurrentUser().getUid();

        DatabaseReference dbApps = FirebaseDatabase.getInstance().getReference("users").child(userId).child("orders");
        dbApps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                        OrderidList.add(productSnapshot.getKey());
                        for(DataSnapshot itemSnapshot : productSnapshot.getChildren()){
                            OrderList.add(itemSnapshot.getValue(cartitem.class));
                            }
                    }
                }
                Log.d("order",OrderList.toString()+" - "+OrderidList);
                OrderHistoryAdapter adapter = new OrderHistoryAdapter(getActivity(), OrderList,OrderidList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
