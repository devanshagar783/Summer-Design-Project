package com.sdp.remotehealthcareapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.sdp.remotehealthcareapp.R;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;

public class MyProfile extends Fragment {
    FirebaseUser fUser;
    View V;
    TextView name;
    TextView age;
    TextView number;
    TextView email;
    TextView gender;
    TextView address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

    }

            @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        V= inflater.inflate(R.layout.fragment_myprofile, container, false);
        getPreviousUser();
        Button save = V.findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        /*Button load = V.findViewById(R.id.buttonload);
        load.setOnClickListener( v ->{
                getPreviousUser();
                }
                );*/

        return V;
    }

    public void updateProfile() {
        HashMap<String, Object> map = new HashMap<>();
        init();
        map.put("Name", name.getText().toString());
        map.put("number", number.getText().toString());
        map.put("email", email.getText().toString());
        map.put("address", address.getText().toString());
        map.put("gender", gender.getText().toString());
        map.put("age", age.getText().toString());
        String res= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(res)
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
        String res= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            init();
                            name.setText(document.getString("Name"));
                            number.setText(document.getString("number"));
                            email.setText(document.getString("email"));
                            address.setText(document.getString("address"));
                            age.setText(document.getString("age"));
                            gender.setText(document.getString("gender"));
                            //name.setText("Ujjwal");
                            Toast.makeText(getContext(), "Sucessfully added", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getContext(), "Error getting documents.", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void init() {
        name= V.findViewById(R.id.editName);
        number= V.findViewById(R.id.editPhone);
        age= V.findViewById(R.id.editAge);
        email= V.findViewById(R.id.editEmail);
        address= V.findViewById(R.id.editAddress);
        gender= V.findViewById(R.id.editGender);



    }
}