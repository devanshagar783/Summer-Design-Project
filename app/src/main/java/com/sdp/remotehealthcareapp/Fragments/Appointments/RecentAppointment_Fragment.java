package com.sdp.remotehealthcareapp.Fragments.Appointments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sdp.remotehealthcareapp.Activities.MapsActivity;
import com.sdp.remotehealthcareapp.Fragments.Appointments.Dataclass.Dataclass_Doctors;
import com.sdp.remotehealthcareapp.Fragments.Appointments.Dataclass.Dataclass_recentapp;
import com.sdp.remotehealthcareapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecentAppointment_Fragment extends Fragment {
    private static final String TAG = "RecentAppointment_Fragm";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView Time, Date, Patient, Dr, Clinic;
    ImageView Photo;
    View v;
    private FirestoreRecyclerAdapter recyclerAdapter;
    FirestoreRecyclerOptions<Dataclass_recentapp> options;

    private ImageView mapl;
    private TextView cli;
    private List<String> locations;

    private String dr, patient, clinic, date, photo, time;

    public RecentAppointment_Fragment() {
        // Required empty public constructor
    }

    public RecentAppointment_Fragment(String dr, String patient, String clinic, String time, String date, String photo) {
        this.dr = dr;
        this.patient = patient;
        this.clinic = clinic;
        this.time = time;
        this.date = date;
        this.photo = photo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recent_appointment_, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_recentapp);
        Query query = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Appointments");

        options = new FirestoreRecyclerOptions.Builder<Dataclass_recentapp>()
                .setQuery(query, Dataclass_recentapp.class)
                .build();
        recyclerAdapter = new FirestoreRecyclerAdapter<Dataclass_recentapp, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_bookedapp, parent, false);

                mapl = (ImageView) view.findViewById(R.id.imageView2);
                cli = (TextView) view.findViewById(R.id.confirm_booked_clinic);
                mapl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "This is location " + cli.getText(), Toast.LENGTH_SHORT).show();
                        final String[] loc = new String[1];
                        locations = new ArrayList<>();
                        Log.d(TAG, "getlongitude: " + cli.getText());

                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document("Clinic")
                                .collection(cli.getText().toString())
                                .document("Location")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot doc = task.getResult();
                                        assert doc != null;
                                        loc[0] = doc.getString("loca");
                                        if (loc[0] != null) {
                                            locations = Arrays.asList(loc[0].split(","));
                                            Intent intent = new Intent(getContext(), MapsActivity.class);
                                            intent.putExtra("Latitude", locations.get(0));
                                            intent.putExtra("Longitude", locations.get(1));
                                            intent.putExtra("Clinic", cli.getText().toString());
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Error message", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Dataclass_recentapp model) {
                holder.setApp_Clinic(model.getClinic());
                holder.setApp_Dr(model.getDr());
                holder.setApp_Pat(model.getPatient());
                holder.setApp_Photo(model.getPhoto());
                holder.setApp_Time(model.getTime() + " " + model.getDate());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);
        //((MainScreen)getActivity()).getSupportActionBar().setTitle(category);
        return v;
    }


    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    public double getlongitude(String clinicName) {
        double a = 0.0;
        final String[] loc = new String[1];
        locations = new ArrayList<>();
        Log.d(TAG, "getlongitude: " + clinicName);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document("Clinic")
                .collection(clinicName)
                .document("Location")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        assert doc != null;
                        loc[0] = doc.getString("loca");
                        locations = Arrays.asList(loc[0].split(","));
                        Log.d(TAG, "onComplete: map" + locations);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error message", Toast.LENGTH_SHORT).show();
                    }
                });
        return Double.parseDouble(locations.get(0));
    }
}