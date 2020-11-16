package com.sdp.remotehealthcareapp.Fragments.HealthFiles;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sdp.remotehealthcareapp.R;

import java.util.HashMap;
import java.util.Objects;


public class Medical extends Fragment {
    View V;
    TextView text_user;
    TextView text_q1;
    TextView text_q2;
    TextView text_q3;
    TextView text_q4;
    EditText a1;
    EditText a2;
    EditText a3;
    EditText a4;
    Button save;
    String res= FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        V= inflater.inflate(R.layout.fragment_medical, container, false);
        init();
        getPreviousUser();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        return V;
    }
    public void updateProfile() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Q1", a1.getText().toString());
        map.put("Q2", a2.getText().toString());
        map.put("Q3", a3.getText().toString());
        map.put("Q4", a4.getText().toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(res)
                .collection("healthfiles")
                .document("medical")
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(getContext(), "Added Sucess", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Error in adding", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getPreviousUser() {
        //String res = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Activity activity;
        db.collection("users")
                .document(res)
                .collection("healthfiles")
                .document("medical")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            assert document != null;
                            a1.setText(document.getString("Q1"));
                            a2.setText(document.getString("Q2"));
                            a3.setText(document.getString("Q3"));
                            a4.setText(document.getString("Q4"));

                            Toast.makeText((Context)getContext(), "Sucessfully added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void init()
    {
        text_user= V.findViewById(R.id.text_user);
        text_q1= V.findViewById(R.id.text_q1);
        text_q2= V.findViewById(R.id.text_q2);
        text_q3= V.findViewById(R.id.text_q3);
        text_q4= V.findViewById(R.id.text_q4);
        a1=V.findViewById(R.id.edittext_a1);
        a2=V.findViewById(R.id.edittext_a2);
        a3=V.findViewById(R.id.edittext_a3);
        a4=V.findViewById(R.id.edittext_a4);
        save= V.findViewById(R.id.button_save);
        text_user.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());



    }
}