package com.sdp.remotehealthcareapp.Fragments.HealthFiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sdp.remotehealthcareapp.Activities.Login.CreateAccount;
import com.sdp.remotehealthcareapp.R;

import java.util.HashMap;

public class MyProfile extends Fragment {
    FirebaseUser fUser;
    View V;
    TextView name,age,number,email,gender,address;
    ImageView email_edit;
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
        init();
        getPreviousUser();

        Button save = V.findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        email_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(getActivity(), CreateAccount.class);
                intent.putExtra("Title", "Edit and save Email");
                startActivity(intent);*/
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
                           // Toast.makeText(getActivity(), name.getText().toString(), Toast.LENGTH_SHORT).show();
                            number.setText(document.getString("number"));
                            //Toast.makeText(getActivity(), number.getText().toString(), Toast.LENGTH_SHORT).show();
                            email.setText(document.getString("Email"));
                            address.setText(document.getString("address"));
                            age.setText(document.getString("age"));
                            gender.setText(document.getString("gender"));
                            //Toast.makeText(getActivity(), gender.getText().toString(), Toast.LENGTH_SHORT).show();
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
        email_edit=V.findViewById(R.id.email_edit);
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