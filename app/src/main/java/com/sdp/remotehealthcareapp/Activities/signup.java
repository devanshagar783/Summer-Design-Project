package com.sdp.remotehealthcareapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.rpc.Status;
import com.sdp.remotehealthcareapp.R;

public class signup extends AppCompatActivity {
    private EditText email;
    private EditText pw;
    private Button signup;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = (EditText)findViewById(R.id.emailid);
        pw = (EditText)findViewById(R.id.pwid);
        signup = (Button)findViewById(R.id.email_signup);
        login = (Button)findViewById(R.id.email_login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), pw.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(signup.this, "Registered", Toast.LENGTH_SHORT).show();
                                                        email.setText("");
                                                        pw.setText("");
                                                    }
                                                    else
                                                        Toast.makeText(signup.this, "Not Registered", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else{
                                    System.out.println("Error");
                                    task.getException().printStackTrace();
                                    Toast.makeText(signup.this, "Some error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });

    }
}