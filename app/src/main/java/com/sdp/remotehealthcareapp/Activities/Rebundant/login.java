package com.sdp.remotehealthcareapp.Activities.Rebundant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sdp.remotehealthcareapp.Activities.MainActivity;
import com.sdp.remotehealthcareapp.R;

import java.util.HashMap;

public class login extends AppCompatActivity {
    private EditText email;
    private EditText pw;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        email = (EditText)findViewById(R.id.text_loginemail_id);
        pw = (EditText)findViewById(R.id.text_loginpwid);
        login = (Button)findViewById(R.id.button_email_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), pw.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                                        Intent intent=getIntent();
                                        saveName_pwd();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Please verify email", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private void saveName_pwd() {
        HashMap<String, Object> map = new HashMap<>();
        Intent intent=getIntent();
        map.put("Name", intent.getStringExtra("accountname"));
        map.put("password", intent.getStringExtra("password"));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(map, SetOptions.merge());

    }
}