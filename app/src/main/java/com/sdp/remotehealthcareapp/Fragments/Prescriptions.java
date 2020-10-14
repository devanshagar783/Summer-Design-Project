package com.sdp.remotehealthcareapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.remotehealthcareapp.R;

import java.util.Objects;


public class Prescriptions extends Fragment {
    View V;
    TextView text_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        V=inflater.inflate(R.layout.fragment_prescriptions, container, false);
        init();
        return V;
    }

    private void init() {
        text_user=V.findViewById(R.id.text_user);
        text_user.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());

    }
}