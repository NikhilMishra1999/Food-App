package com.example.nikhil.food4foodies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RateFragment extends Fragment {

    RatingBar ratingBar;
    int myRating = 0;
    ImageView smile;
    TextView action;
    TextView feedbacktext;

    public RateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        ratingBar = view.findViewById(R.id.ratingBar);
        smile = view.findViewById(R.id.reaction);
        action = view.findViewById(R.id.textreaction);
        feedbacktext = view.findViewById(R.id.feedback);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating = (int) v;
                String message = null;
                String feed = null;
                myRating = (int) ratingBar.getRating();

                switch (rating) {
                    case 1:
                        message = "NOT SO GOOD";
                        feed = "Sorry to hear that. What went wrong?";
                        smile.setImageResource(R.drawable.one);
                        break;
                    case 2:
                        message = "CAN BE BETTER";
                        feed = "Sorry to hear that. What went wrong?";
                        smile.setImageResource(R.drawable.two);
                        break;
                    case 3:
                        message = "GOOD";
                        feed = "Thank You. Let us know how we can improve further?";
                        smile.setImageResource(R.drawable.three);
                        break;
                    case 4:
                        message = "LIKED IT";
                        feed = "Thank You. Let us know wha you liked about us?";
                        smile.setImageResource(R.drawable.four);
                        break;
                    case 5:
                        message = "LOVED IT";
                        feed = "Thank You. Let us know wha you liked about us?";
                        smile.setImageResource(R.drawable.five);
                        break;
                }
                action.setText(message);
                feedbacktext.setText(feed);
            }
        });
        return view;
    }
}
