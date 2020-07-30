package com.example.nikhil.food4foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderPlacedActivity extends AppCompatActivity {

    TextView currentorder;
    TextView useremail,orderprice;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        currentorder = findViewById(R.id.myorderid);
        useremail = findViewById(R.id.myorderid2);
        orderprice=findViewById(R.id.myorderid3);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String orderid = intent.getStringExtra("orderid");
        String price = intent.getStringExtra("total");
        orderprice.setText("₹ 190");
        currentorder.setText(orderid);

        List<? extends UserInfo> infos = user.getProviderData();
        for (UserInfo ui : infos) {
            if (ui.getProviderId().equals(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {
                useremail.setText(ui.getEmail());
                //name.setText(ui.getDisplayName());
                //userphone.setText(ui.getPhoneNumber());
                Log.d("MYID", ui.getProviderId());
            }
            else if (ui.getProviderId().equals(FirebaseAuthProvider.PROVIDER_ID)) {
                useremail.setText(ui.getEmail());
                //name.setText(ui.getDisplayName());
                //userphone.setText(ui.getPhoneNumber());
                Log.d("MYFRID", ui.getProviderId());
            }
        }

        /*mFirebaseAuth = FirebaseAuth.getInstance();
        String userId = mFirebaseAuth.getCurrentUser().getUid();

        DatabaseReference dbApps = FirebaseDatabase.getInstance().getReference("users").child(userId).child("orders");
        dbApps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                       currentorder.setText((CharSequence) productSnapshot.getChildren());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }
}
