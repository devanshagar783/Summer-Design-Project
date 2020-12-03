package com.sdp.remotehealthcareapp.Fragments.Home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sdp.remotehealthcareapp.Activities.MainActivity;
import com.sdp.remotehealthcareapp.Activities.MapsActivity;
import com.sdp.remotehealthcareapp.R;

public class Tab1 extends Fragment {

    private static final String TAG = "Tab1";
    View v;
    private Button bookNow;

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
        bookNow = (Button) v.findViewById(R.id.booknow);
        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                } else {
                    String message = "Red colored markers represent the clinics which wont be visited by the doctor today.\nGreen colored markers represent the clinic in which doctor is currently sitting.\nYellow colored markers represent the clinics which will be later visited by the doctor.\nTo book an appointment, click on the marker and then on the info window which opens.";

                    SpannableString ss = new SpannableString(message);
                    ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);
                    ForegroundColorSpan green = new ForegroundColorSpan(Color.GREEN);
                    ForegroundColorSpan yellow = new ForegroundColorSpan(-1000);

                    ss.setSpan(red, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(green, 85, 90, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(yellow, 166, 173, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                    Log.d(TAG, "onClick: alert here");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                            .setTitle("Understanding what the colors mean")
                            .setMessage(ss)
                            .setPositiveButton("Go Forward", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG, "onClick: did i work");
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new DoctorFragment()).addToBackStack("Doctors list").commit();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    builder.create().show();
                }
            }
        });
        return v;
    }
}