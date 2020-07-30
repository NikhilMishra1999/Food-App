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

public class FavouritesFragment extends Fragment {

    ProgressBar progressBar;
    private static final String TAG = "Message";
    FirebaseAuth mFirebaseAuth;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_favourites, container, false);


        final List FavList = new ArrayList<menuitem>();

        Log.d(TAG,"running2");
        final RecyclerView recyclerView = view.findViewById(R.id.fav_recyclerview);

        progressBar = (ProgressBar)view.findViewById(R.id.fav_progress);
        progressBar.setVisibility(View.VISIBLE);

        mFirebaseAuth = FirebaseAuth.getInstance();
        final String userId = mFirebaseAuth.getCurrentUser().getUid();

        DatabaseReference dbApps = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favourites");
        dbApps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar = view.findViewById(R.id.fav_progress);
                progressBar.setVisibility(View.INVISIBLE);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        menuitem item = productSnapshot.getValue(menuitem.class);
                        Log.d(TAG,"running3"+item.name);
                        FavList.add(item);

                    }
                }
                FavouriteAdapter adapter = new FavouriteAdapter(getActivity(), FavList);
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
