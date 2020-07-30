package com.example.nikhil.food4foodies;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HelpFragment extends Fragment {

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toast.makeText(this.getActivity(),"this is help fragment",Toast.LENGTH_SHORT).show();
        //Log.d("items",new MegaClass().getItems());
        HashMap<Integer,cartitem> arr = new MegaClass().getItems();
        for(int i=0;i<arr.size();i++){
            Log.d("item "+i, Objects.requireNonNull(arr.get(i)).toString());
        }

        return inflater.inflate(R.layout.fragment_help, container, false);
    }
}
