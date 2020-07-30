package com.example.nikhil.food4foodies;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ShareFragment extends Fragment {

    Button shareapp;

    public ShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        shareapp = view.findViewById(R.id.share);
        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String link = "Hi friends";
                i.putExtra(Intent.EXTRA_TEXT,link);
                startActivity(Intent.createChooser(i,"Share Using"));
            }
        });

        return view;
    }
}
