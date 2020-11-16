package com.sdp.remotehealthcareapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.remotehealthcareapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentAppointment_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentAppointment_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView time,date;
    View v;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecentAppointment_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentAppointment_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentAppointment_Fragment newInstance(String param1, String param2) {
        RecentAppointment_Fragment fragment = new RecentAppointment_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_recent_appointment_, container, false);
        time= v.findViewById(R.id.appointment_time);
        date=v.findViewById(R.id.appointment_date);
        getPreviousUser();

        return v;
    }
    private void getPreviousUser() {
        String res = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(res)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            date.setText(document.getString("Appointment date"));
                            time.setText(document.getString("Appointment time"));
                            Toast.makeText(getContext(), "Sucessfully added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}