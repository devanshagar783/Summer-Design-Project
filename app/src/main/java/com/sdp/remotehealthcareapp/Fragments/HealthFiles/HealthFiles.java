package com.sdp.remotehealthcareapp.Fragments.HealthFiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.sdp.remotehealthcareapp.Activities.MainActivity;
import com.sdp.remotehealthcareapp.Activities.ReportFiles.AttachmentReportActivity;
import com.sdp.remotehealthcareapp.R;

import java.util.Objects;

public class HealthFiles extends Fragment {

    View V;
    TextView userName;
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
        V = inflater.inflate(R.layout.fragment_healthfiles, container, false);
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
        at_repo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AttachmentReportActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        userName = V.findViewById(R.id.text_user);
        userName.setText(Objects.requireNonNull(MainActivity.getName()));
        prescriptions = V.findViewById(R.id.button_prescriptions);
        medical = V.findViewById(R.id.button_medical);
        at_repo = V.findViewById(R.id.button_at_repo);
    }
}