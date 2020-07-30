package com.example.nikhil.food4foodies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    Button signup;
    TextView loglink;
    private static final String TAG = "Message";
    private LoginButton loginbutton;
    String userID;
    DatabaseReference dataReference;
    private CallbackManager callbackManager;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //FACEBOOK AUTH-----------------------------------------------------------------------------
        //Checking FB Login status
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn==true){
            Intent i = new Intent(getApplicationContext(),NavDrawerActivity.class);
            startActivity(i);
        }

        loginbutton = findViewById(R.id.fbbutton);
        callbackManager = CallbackManager.Factory.create();
        loginbutton.setReadPermissions("email");
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final Profile profile = Profile.getCurrentProfile();

                Log.d(TAG, "onSuccess: user profile is created for "+ profile.getName());
                Log.d(TAG, "onSuccess: user profile is created for " + profile.getProfilePictureUri(50,50));
                Log.d(TAG, "onSuccess: user profile is created for " + profile.describeContents());

                //storing details
                fstore = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = fstore.collection("fbusers");
                final Map<String, Object> fbuser = new HashMap<>();
                fbuser.put("name", profile.getName());
                collectionReference.add(fbuser).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d(TAG, "onSuccess: user profile is created for "+ profile.getName());
                    }
                });
                //stored

                Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        //FACEBOOK AUTH END-------------------------------------------------------------------------

        loglink = (TextView) findViewById(R.id.loginlink);
        loglink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        create();
        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void create() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    /*//storing details
                    fstore = FirebaseFirestore.getInstance();
                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("myusers").document(userID);
                    final Map<String, Object> newuser = new HashMap<>();
                    newuser.put("email", mAuth.getCurrentUser().getEmail());
                    newuser.put("phoneno",mAuth.getCurrentUser().getPhoneNumber());
                    newuser.put("name", mAuth.getCurrentUser().getDisplayName());
                    documentReference.set(newuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user profile is created for " + mAuth.getCurrentUser().getEmail());
                        }
                    });
                    //stored*/

                    userID = mAuth.getCurrentUser().getUid();
                    dataReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("personal_details");
                    final Map<String, Object> itemfav = new HashMap<>();
                    itemfav.put("email", mAuth.getCurrentUser().getEmail());
                    itemfav.put("phoneno",mAuth.getCurrentUser().getPhoneNumber());
                    itemfav.put("name", mAuth.getCurrentUser().getDisplayName());
                    dataReference.setValue(itemfav);

                    Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
