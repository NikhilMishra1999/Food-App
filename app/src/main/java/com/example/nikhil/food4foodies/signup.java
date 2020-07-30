package com.example.nikhil.food4foodies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class signup extends AppCompatActivity {

    private static final String TAG = "Message";
    TextView link;
    EditText name, email, password, phone, edittextcode;
    Button sign, reset, resend, verify;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;
    String userID;
    private String verificationid;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        link = (TextView) findViewById(R.id.link);
        sign = (Button) findViewById(R.id.sign_up);
        reset = (Button) findViewById(R.id.reset);
        verify = (Button) findViewById(R.id.verifylink);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        resend = (Button)findViewById(R.id.resend);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int code = 91;

                String number = phone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    phone.setError("Valid number is required");
                    phone.requestFocus();
                    return;
                }

                String phonenumber = "+"+code+number;
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(signup.this, "Sending OTP...", Toast.LENGTH_SHORT).show();
                sendVerificationCode(phonenumber);
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int code = 91;

                String number = phone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    phone.setError("Valid number is required");
                    phone.requestFocus();
                    return;
                }

                String phonenumber = "+"+code+number;
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(signup.this, "Sending OTP...", Toast.LENGTH_SHORT).show();
                sendVerificationCode(phonenumber);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                email.setText("");
                password.setText("");
                phone.setText("");
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), login.class);
                getApplication().startActivity(i);
            }
        });

        if (mFirebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nam = name.getText().toString();
                final String em = email.getText().toString();
                final String pass = password.getText().toString();
                final String pn = phone.getText().toString();
                final String otp = edittextcode.getText().toString();


                if (TextUtils.isEmpty(nam)) {
                    name.requestFocus();
                    name.setError("This field can't be empty");
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

                if (TextUtils.isEmpty(pn)) {
                    phone.setError("This field can't be empty");
                    phone.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(otp)) {
                    edittextcode.requestFocus();
                    edittextcode.setError("This field can't be empty");
                    return;
                }


                mFirebaseAuth.createUserWithEmailAndPassword(em, pass).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(signup.this, "Account Created Successfully.", Toast.LENGTH_SHORT).show();
                            userID = mFirebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
                            final Map<String, Object> user = new HashMap<>();
                            user.put("name", nam);
                            user.put("email", em);
                            user.put("password", pass);
                            user.put("phoneno", pn);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user profile is created for " + userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(signup.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            edittextcode.setVisibility(View.VISIBLE);
                        } else {
                            resend = (Button)findViewById(R.id.resend);
                            resend.setVisibility(View.VISIBLE);
                            Toast.makeText(signup.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60L, TimeUnit.SECONDS, this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(s, forceResendingToken);
                edittextcode = findViewById(R.id.editTextCode);
                progressBar.setVisibility(View.GONE);
                edittextcode.setVisibility(View.VISIBLE);
                verificationid = s;
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null){
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(signup.this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}