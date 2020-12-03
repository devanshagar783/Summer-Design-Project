package com.sdp.remotehealthcareapp.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.remotehealthcareapp.Activities.MapsActivity;
import com.sdp.remotehealthcareapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoctorFragment extends Fragment {

    View v;
    private Button dr1;
    private Button dr2;
    private Button dr3;
    private Button dr4;
    private Button dr5;
    private Button dr6;
    private Button dr7;
    private Button dr8;
    private Button dr9;


    public DoctorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_doctor, container, false);
        dr1 = (Button)v.findViewById(R.id.doc1);
        dr2 = (Button)v.findViewById(R.id.doc2);
        dr3 = (Button)v.findViewById(R.id.doc3);
        dr4 = (Button)v.findViewById(R.id.doc4);
        dr5 = (Button)v.findViewById(R.id.doc5);
        dr6 = (Button)v.findViewById(R.id.doc6);
        dr7 = (Button)v.findViewById(R.id.doc7);
        dr8 = (Button)v.findViewById(R.id.doc8);
        dr9 = (Button)v.findViewById(R.id.doc9);
        List<Button> btns = new ArrayList<>();
        btns.add(dr1);
        btns.add(dr2);
        btns.add(dr3);
        btns.add(dr4);
        btns.add(dr5);
        btns.add(dr6);
        btns.add(dr7);
        btns.add(dr8);
        btns.add(dr9);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document("Doctors")
                .collection("Names")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot doc =task.getResult();
                            List<DocumentSnapshot> documents = doc.getDocuments();
                            for (int i=0;i<9;++i) {
                                btns.get(i).setText(documents.get(i).getId());
                            }
                        }
                    }
                });

        for(int i=0;i<9;++i){
            Button doc = btns.get(i);
            doc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MapsActivity.class);
                    intent.putExtra("Name", doc.getText());
                    startActivity(intent);
                }
            });
        }



        return v;
    }
}