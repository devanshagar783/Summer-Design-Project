package com.sdp.remotehealthcareapp.Fragments.HealthFiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sdp.remotehealthcareapp.R;

import java.util.HashMap;

public class MyProfile extends Fragment {
    FirebaseUser fUser;
    View V;
    TextView name;
    TextView age;
    TextView number;
    TextView email;
    TextView gender;
    TextView address;
    SharedPreferences sharedPreferences_login;
    String res= FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        sharedPreferences_login=getContext().getSharedPreferences(getString(R.string.preference_file_name_login),
                        Context.MODE_PRIVATE);
        getPreviousUser();
        init();
        Button save = V.findViewById(R.id.buttonSave);
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
        init();
        map.put("Name", name.getText().toString());
        //map.put("number", number.getText().toString());
       // map.put("email", email.getText().toString());
        map.put("address", address.getText().toString());
        map.put("gender", gender.getText().toString());
        map.put("age", age.getText().toString());
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
        //String res = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(res)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
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
                    }
                });
    }


    private void init() {
        name= V.findViewById(R.id.editName1);
        number= V.findViewById(R.id.editPhone);
        age= V.findViewById(R.id.editAge);
        email= V.findViewById(R.id.editEmail);
        address= V.findViewById(R.id.editAddress);
        gender= V.findViewById(R.id.editGender);
        getName();

    }
    private void getName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            TextView username = V.findViewById(R.id.text_user);
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                String name= (document.getString("Name"));
                                username.setText(name);

                            }
                        }
                    });
        }
    }
}