package com.sdp.remotehealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.MenuItem;

import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;


import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private CoordinatorLayout coordinatorLayout;
    private androidx.appcompat.widget.Toolbar toolbar;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Signin signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        getDrawer_started();
        //getName();

    }

    private void getName() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            TextView name = findViewById(R.id.user_name);
            name.setText(signInAccount.getDisplayName());
            /*ImageView pic = findViewById(R.id.user_pic);
            Uri uripic = signInAccount.getPhotoUrl();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uripic);
            pic.setImageBitmap(bitmap);*/
        }
    }

    private void getDrawer_started()
    {
        drawerLayout= findViewById(R.id.drawerlayout);
        coordinatorLayout= findViewById(R.id.coordinator);
        toolbar= findViewById(R.id.toolbar);
        // frameLayout= findViewById(R.id.framelayout);
        navigationView= findViewById(R.id.navigation);
        setUpToolbar();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                new Dashboard()).addToBackStack("Dashboard").commit();


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.logout:
                        //finish();
                        startActivity(new Intent(getApplicationContext(), Signin.class));

                }
                return true;
            }
        });
    }
    private void setUpToolbar() {
        //setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        (getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId() == android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);

        }
        return super.onOptionsItemSelected(item);
    }


}