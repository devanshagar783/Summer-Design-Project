package com.sdp.remotehealthcareapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.remotehealthcareapp.R;

import java.util.Objects;

public class HealthFiles extends Fragment {
    View V;

    Button prescriptions;
    Button medical;
    Button at_repo;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         V=inflater.inflate(R.layout.fragment_healthfiles, container, false);
        init();
        button_listerns();
        return V;
    }

    private void button_listerns() {

        prescriptions.setOnClickListener(v -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new Prescriptions());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack("Prescriptions");
            ft.commit();
        });
        medical.setOnClickListener(v -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new Medical());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack("Medical");
            ft.commit();
        });
        at_repo.setOnClickListener(v -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new At_repo());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack("Attachments");
            ft.commit();
        });
    }

    private void init()
    {

        prescriptions= V.findViewById(R.id.button_prescriptions);
        medical= V.findViewById(R.id.button_medical);
        at_repo= V.findViewById(R.id.button_at_repo);


    }
}