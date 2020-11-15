package com.sdp.remotehealthcareapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MenuItem;

import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.remotehealthcareapp.Fragments.AppointmentFragment;
import com.sdp.remotehealthcareapp.Fragments.Dashboard;
import com.sdp.remotehealthcareapp.Fragments.HealthFiles;
import com.sdp.remotehealthcareapp.Fragments.MyProfile;
import com.sdp.remotehealthcareapp.Fragments.RecentAppointment_Fragment;
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
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main);
        getDrawer_started();
        getProfile();
    }

    private void getProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.user_name);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                String name= (document.getString("Name"));
                                navUsername.setText(name);

                            }
                        }
                    });
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
                    selectorFragment=new Dashboard();
                    if(item.getItemId() == R.id.logout) {
                        //finish();
                        /*sharedPreferences= getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean("isLoggedin", false).apply();*/
                        startActivity(new Intent(getApplicationContext(), PhoneAuthActivity.class));
                        finish();
                    }
                    else if(item.getItemId()== R.id.book_appointment)
                        selectorFragment= new AppointmentFragment();
                    else if(item.getItemId() == R.id.profile){
                        selectorFragment = new MyProfile();
                    }
                    else if(item.getItemId() == R.id.healthfiles) {
                        selectorFragment= new HealthFiles();
                    }
                    else if(item.getItemId()== R.id.dashboard){
                        selectorFragment= new Dashboard();
                    }
                    else if(item.getItemId() == R.id.recent)
                    {
                        selectorFragment= new RecentAppointment_Fragment();
                    }
                    else if(item.getItemId() == R.id.about){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("About Hygeia")
                                .setMessage("Build and published by \nAkshat Srivastava(201851013)\nDevansh Agarwal(201851038)\nUjjwal Shrivastava(201851136)\n"+"\nIf you want to hire us or\n"+"if you want to check our other works\n"+"Have a look at our website:")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(MainActivity.this, MyWebViewActivity.class);
                                        intent.putExtra("url","https://www.example.com/");
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment,selectorFragment).addToBackStack("Profile Setup").commit();
                    drawerLayout.closeDrawers();
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