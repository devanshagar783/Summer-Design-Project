package com.sdp.remotehealthcareapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MenuItem;

import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.remotehealthcareapp.Fragments.Dashboard;
import com.sdp.remotehealthcareapp.Fragments.MyProfile;
import com.sdp.remotehealthcareapp.R;


public class MainActivity extends AppCompatActivity {

    private Fragment selectorFragment;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private CoordinatorLayout coordinatorLayout;
    private androidx.appcompat.widget.Toolbar toolbar;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        getDrawer_started();
        getProfile();
    }

    private void getProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null) {
            String name = user.getDisplayName();
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.user_name);
            navUsername.setText(name);
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

                    if(item.getItemId() == R.id.logout) {
                        //finish();
                        /*sharedPreferences= getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean("isLoggedin", false).apply();*/
                        startActivity(new Intent(getApplicationContext(), Signin.class));
                        finish();
                    }
                    else if(item.getItemId() == R.id.profile){
                        selectorFragment = new MyProfile();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,selectorFragment).addToBackStack("Profile Setup").commit();
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