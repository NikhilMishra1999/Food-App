package com.example.nikhil.food4foodies;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    Button order;
    DatabaseReference dataReference;
    FirebaseAuth mFirebaseAuth;
    ImageView cart;
    TextView totalprice,message,message2;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("items",new MegaClass().getItems().toString());
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.cartrecyclerview);

        cart=view.findViewById(R.id.cartimage);
        message= view.findViewById(R.id.textView29);
        message2=view.findViewById(R.id.textView34);
        totalprice=view.findViewById(R.id.textView26);

        mFirebaseAuth = FirebaseAuth.getInstance();
        final String userId = mFirebaseAuth.getCurrentUser().getUid();

     double price= 0.0;
        for(int i=0; i<new MegaClass().getItems().size(); i++){
            price += new MegaClass().getItems().get(i).getPrice();
        }
        totalprice.setText("Grand Total: "+price);

        order = view.findViewById(R.id.placeorder);
        final double finalPrice = price;
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int randomPIN = (int)(Math.random()*9000)+1000;
                String val = "FO"+randomPIN;
                dataReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("orders").child(val);
                for(int i=0;i<new MegaClass().getItems().size();i++) {
                    dataReference.child(""+i).setValue(new MegaClass().getItems().get(i));
                }
                Intent intent = new Intent(getActivity(),OrderPlacedActivity.class);
                intent.putExtra("orderid", val);
                intent.putExtra("total", finalPrice);
                startActivity(intent);
            }
        });

        if(new MegaClass().getItems().size()==0){
            cart.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
            message2.setVisibility(View.VISIBLE);
            totalprice.setVisibility(View.INVISIBLE);
            order.setVisibility(View.INVISIBLE);
        }
        else {
            cart.setVisibility(View.INVISIBLE);
            totalprice.setVisibility(View.VISIBLE);
            order.setVisibility(View.VISIBLE);
        }

        CartAdapter adapter = new CartAdapter(getActivity(), new MegaClass().getItems());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }
}
