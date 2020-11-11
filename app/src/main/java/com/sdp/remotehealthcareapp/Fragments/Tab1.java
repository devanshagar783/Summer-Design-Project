package com.sdp.remotehealthcareapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sdp.remotehealthcareapp.Activities.MainActivity;
import com.sdp.remotehealthcareapp.Activities.MapsActivity;
import com.sdp.remotehealthcareapp.R;

public class Tab1 extends Fragment {

    View v;
    private Button button;

    public Tab1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tab1, container, false);
        button = (Button) v.findViewById(R.id.btnShow);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MapsActivity.class);
            startActivity(intent);
        });
        return v;
    }
}