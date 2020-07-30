package com.example.nikhil.food4foodies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment {

    TextView username, email, userphone;
    ImageView imgname, imgcall, imgmail;
    FirebaseFirestore fstore;
    FirebaseAuth mFirebaseAuth;
    String userID;
    private static final String TAG = "Message";

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        username = view.findViewById(R.id.name);
        email = view.findViewById(R.id.profilemail);
        userphone = view.findViewById(R.id.phone);

        imgcall = view.findViewById(R.id.imageView7);
        imgmail = view.findViewById(R.id.imageView8);
        imgname = view.findViewById(R.id.imageView6);

        //Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        //userID = mFirebaseAuth.getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();

        //String provider = FirebaseAuth.getInstance().getCurrentUser().getProviderId();
        //Log.d(TAG, provider);

        //FirebaseUser user = mFirebaseAuth.getCurrentUser();
        //String prov = user.getProviderData().get(0).getProviderId();
        //Log.d(TAG, prov);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        List<? extends UserInfo> infos = user.getProviderData();
        for (UserInfo ui : infos) {
            if (ui.getProviderId().equals(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {
                email.setText(ui.getEmail());
                username.setText(ui.getDisplayName());
                userphone.setText(ui.getPhoneNumber());
                Log.d("MYID", ui.getProviderId());
            }
            else if (ui.getProviderId().equals(FirebaseAuthProvider.PROVIDER_ID)) {
                email.setText(ui.getEmail());
                username.setText(ui.getDisplayName());
                userphone.setText(ui.getPhoneNumber());
                Log.d("MYFRID", ui.getProviderId());
            }
        }

        //fetching firebase
        /*GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            email.setText(acct.getEmail());
            username.setText(acct.getDisplayName());
        }*/

        /*DocumentReference documentReference = fstore.collection("myusers").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                email.setText(documentSnapshot.getString("email"));
                username.setText(documentSnapshot.getString("name"));
                userphone.setText(documentSnapshot.getString("phone"));
                Log.d(TAG,documentSnapshot.getString("email") );
            }
        });*/

        /*fstore = FirebaseFirestore.getInstance();
        fstore.collection("myusers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                email.setText((String) document.getData().get("email"));
                                username.setText((String) document.getData().get("name"));
                                userphone.setText((String) document.getData().get("phoneno"));
                                Log.d(TAG, document.getId() + " => " + document.getData().get("email"));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/

        return view;
    }
}
