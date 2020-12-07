package com.sdp.remotehealthcareapp.Fragments.Home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sdp.remotehealthcareapp.Activities.MainActivity;
import com.sdp.remotehealthcareapp.R;

import java.util.ArrayList;
import java.util.List;

public class Tab2 extends Fragment {

    private Button btn_sos;
    View v;
    private String message;
    private Double latitude;
    private Double longitude;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String TAG = "Tab2";
    private List<String> emergencyContacts;

    public Tab2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tab2, container, false);
        btn_sos = (Button) v.findViewById(R.id.btn_sos);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());



        //Bug fix attempt start -> working
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Log.d(TAG, "onSuccess: " + location.getLatitude());
                        Log.d(TAG, "onSuccess: " + location.getLongitude());
                        //                                if (location != null) {
                        //                                    // Logic to handle location object
                        //                                    latitude = location.getLatitude();
                        //                                    longitude = location.getLongitude();
                        //                                }
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
        // bug fix attempt end -> working



        btn_sos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                emergencyContacts = new ArrayList<>();
                emergencyContacts.add("+919205710631");
                emergencyContacts.add("+918795641407");
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                    return;
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                Log.d(TAG, "onSuccess: " + location.getLatitude());
                                Log.d(TAG, "onSuccess: " + location.getLongitude());
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("MapDemoActivity", "Error trying to get last GPS location");
                                e.printStackTrace();
                            }
                        });

                message = "This is a medical emergency and you're listed as my emergency contact. I am at " + "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                for (String phoneNo: emergencyContacts) {
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, message, null, null);
                        Toast.makeText(getActivity().getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "No medical emergency contacts are listed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return v;
    }
}