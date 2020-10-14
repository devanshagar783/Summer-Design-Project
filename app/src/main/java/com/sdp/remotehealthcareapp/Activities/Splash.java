package com.sdp.remotehealthcareapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.remotehealthcareapp.R;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Signin signin= new Signin();
        //SharedPreferences getsave= getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        //boolean loggedin= getsave.getBoolean("isLoggedin", false);
        if(user==null)
            startActivity(new Intent(this, Intro_Signin.class));
        else
            startActivity(new Intent(this, MainActivity.class));
        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
