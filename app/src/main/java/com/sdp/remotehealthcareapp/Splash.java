package com.sdp.remotehealthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Signin signin= new Signin();
        SharedPreferences getsave= getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        boolean loggedin= getsave.getBoolean("isLoggedin", false);
        if(!loggedin)
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
