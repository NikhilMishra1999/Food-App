package com.example.nikhil.food4foodies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button signin;
    FirebaseAuth mFirebaseAuth;
    TextView resetpass,signuplink;
    FirebaseFirestore fstore;
    String userID;
    private static final String TAG = "Message";
    private LoginButton loginbutton;
    private CallbackManager callbackManager;
    DatabaseReference dataReference;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        signin = (Button) findViewById(R.id.log_in);
        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.pass);
        resetpass = (TextView) findViewById(R.id.forgot);
        signuplink = (TextView) findViewById(R.id.signuplink);

        //FACEBOOK AUTH-----------------------------------------------------------------------------
        //Checking FB Login status
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn==true){
            Intent i = new Intent(getApplicationContext(),NavDrawerActivity.class);
            startActivity(i);
        }

        loginbutton = findViewById(R.id.loginfbbutton);
        callbackManager = CallbackManager.Factory.create();
        loginbutton.setReadPermissions("email");
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
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

        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), NavDrawerActivity.class));
            finish();
        }

        //Google Sign in button
        create();
        findViewById(R.id.loginsignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetmail = new EditText(view.getContext());
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("RESET PASSWORD");
                builder.setMessage("Enter your e-mail to receive reset link.");
                builder.setView(resetmail);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetmail.getText().toString();
                        mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset link has been sent to your E-mail.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close
                    }
                });
                builder.create().show();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String em = email.getText().toString();
                final String pass = password.getText().toString();
                final String valemail = email.getText().toString().trim();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (valemail.matches(emailPattern))
                {
                } else{
                    email.setError("Email is Invalid");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(em)) {
                    email.setError("This field can't be empty");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pass) && pass.length() < 6) {
                    password.setError("This field can't be empty");
                    password.requestFocus();
                    return;
                }

                mFirebaseAuth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), NavDrawerActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                Toast.makeText(this, "You pressed back button", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    /*//storing details
                    fstore = FirebaseFirestore.getInstance();
                    userID = mFirebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("myusers").document(userID);
                    final Map<String, Object> newuser = new HashMap<>();
                    newuser.put("email", mFirebaseAuth.getCurrentUser().getEmail());
                    newuser.put("phoneno",mFirebaseAuth.getCurrentUser().getPhoneNumber());
                    newuser.put("name", mFirebaseAuth.getCurrentUser().getDisplayName());
                    documentReference.set(newuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user profile is created for " + mFirebaseAuth.getCurrentUser().getEmail());
                        }
                    });
                    //stored*/

                    userID = mFirebaseAuth.getCurrentUser().getUid();
                    dataReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("personal_details");
                    final Map<String, Object> itemfav = new HashMap<>();
                    itemfav.put("email", mFirebaseAuth.getCurrentUser().getEmail());
                    itemfav.put("phoneno",mFirebaseAuth.getCurrentUser().getPhoneNumber());
                    itemfav.put("name", mFirebaseAuth.getCurrentUser().getDisplayName());
                    dataReference.setValue(itemfav);

                    Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

