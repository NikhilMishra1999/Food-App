package com.example.nikhil.food4foodies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class NavDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;
    TextView name;
    TextView emailid;
    View hview;
    String userID;
    private static final String TAG = "Message";
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_menu);

        //FIREBASE--------------------------------
        fstore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        //TOOLBAR--------------------------------
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FLOATING BUTTON--------------------------------
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        //DRAWER--------------------------------
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NAVIGATION VIEW--------------------------------
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new MenuFragment();
                        loadFragment(fragment);
                        fab.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Explore Menu");
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_help:
                        fragment = new HelpFragment();
                        loadFragment(fragment);
                        fab.setVisibility(View.INVISIBLE);
                        toolbar.setTitle("Help");
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        fab.setVisibility(View.INVISIBLE);
                        toolbar.setTitle("My Profile");
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_signout:
                        fragment = new MenuFragment();

                        AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawerActivity.this);
                        builder.setMessage("Are you sure you want to logout?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseAuth.getInstance().signOut();
                                        LoginManager.getInstance().logOut();
                                        Intent intent = new Intent(NavDrawerActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        NavDrawerActivity.this.finish();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                loadFragment(fragment);
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        fab.setVisibility(View.INVISIBLE);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_share:
                        fragment = new ShareFragment();
                        loadFragment(fragment);
                        fab.setVisibility(View.INVISIBLE);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        toolbar.setTitle("Share and Care");
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_rate:
                        fragment = new RateFragment();
                        loadFragment(fragment);
                        fab.setVisibility(View.INVISIBLE);
                        toolbar.setTitle("Rate Our App");
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        drawer.closeDrawers();
                        return true;
                }
                return false;
            }
        });

        //FIREBASE FETCHING--------------------------------
        //userID = mFirebaseAuth.getCurrentUser().getUid();
        hview = navigationView.getHeaderView(0);
        name = (TextView) hview.findViewById(R.id.textname);
        emailid = (TextView) hview.findViewById(R.id.textuserid);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /*DocumentReference documentReference = fstore.collection("myusers").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                emailid.setText(documentSnapshot.getString("email"));
                name.setText(documentSnapshot.getString("name"));
            }
        });*/

        /*GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            name.setText(signInAccount.getDisplayName());
            emailid.setText(signInAccount.getEmail());
        }*/

        List<? extends UserInfo> infos = user.getProviderData();
        for (UserInfo ui : infos) {
            if (ui.getProviderId().equals(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {
                emailid.setText(ui.getEmail());
                name.setText(ui.getDisplayName());
                //userphone.setText(ui.getPhoneNumber());
                Log.d("MYID", ui.getProviderId());
            }
            else if (ui.getProviderId().equals(FirebaseAuthProvider.PROVIDER_ID)) {
                emailid.setText(ui.getEmail());
                name.setText(ui.getDisplayName());
                //userphone.setText(ui.getPhoneNumber());
                Log.d("MYFRID", ui.getProviderId());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            Toolbar toolbar = findViewById(R.id.toolbar);
            FloatingActionButton fab = findViewById(R.id.fab);
            switch (item.getItemId()) {
                case R.id.navigation_menu:
                    fragment = new MenuFragment();
                    loadFragment(fragment);
                    fab.setVisibility(View.VISIBLE);
                    toolbar.setTitle("Explore Menu");
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_order_history:
                    fragment = new OrderHistoryFragment();
                    loadFragment(fragment);
                    fab.setVisibility(View.INVISIBLE);
                    toolbar.setTitle("Order History");
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_cart:
                    fragment = new CartFragment();
                    loadFragment(fragment);
                    toolbar.setTitle("My Cart");
                    fab.setVisibility(View.INVISIBLE);
                    bottomNavigationView.setVisibility(View.INVISIBLE);
                    return true;

                case R.id.navigation_favourites:
                    fragment = new FavouritesFragment();
                    loadFragment(fragment);
                    toolbar.setTitle("My Favourites");
                    fab.setVisibility(View.INVISIBLE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
