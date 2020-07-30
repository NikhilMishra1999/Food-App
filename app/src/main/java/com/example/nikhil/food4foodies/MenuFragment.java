package com.example.nikhil.food4foodies;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuFragment extends Fragment {

    private static final String TAG = "Message";
    ProgressBar progressBar;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_menu, container, false);

        final List MenuList = new ArrayList<menuitem>();

        Log.d(TAG,"running2");
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.menurecyclerview);

        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference dbApps = FirebaseDatabase.getInstance().getReference("fooditems");
        dbApps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar = (ProgressBar) view.findViewById(R.id.progress);
                progressBar.setVisibility(View.INVISIBLE);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        menuitem item = productSnapshot.getValue(menuitem.class);
                        Log.d(TAG,"running3"+item.name);
                        MenuList.add(item);

                    }
                }
                MenuAdapter adapter = new MenuAdapter(getActivity(), MenuList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*MenuList.add(new menuitem("nikhil","nikhil","cdsvfv",75));
        MenuList.add(new menuitem("anshul","jbvjb","vbdfjbvfkvdf",75));
        MenuList.add(new menuitem("modi","dvfdggfndhgfnh","vbdfjbvfkvdf",75));
        MenuAdapter adapter = new MenuAdapter(this.getActivity(),MenuList);
        recyclerView.setAdapter(adapter);*/

        return view;

    }
}
