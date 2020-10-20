

            /*    //linking the email with Google account
                AuthCredential credential = EmailAuthProvider.getCredential(email.toString(), pw.toString());
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
                                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        startActivity(new Intent(signup.this, MyProfile.class));

                                                    } else {
                                                        //Log.w(TAG, "linkWithCredential:failure", task.getException());
                                                        Toast.makeText(signup.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();

                                                    }

                                                    // ...
                                                }
                                            });
                                }
                                else
                                {

                                }
                            }
                        });
*/
package com.sdp.remotehealthcareapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.rpc.Status;
import com.sdp.remotehealthcareapp.R;

                public class signup extends AppCompatActivity {
                    private TextView email,email_name;
                    private TextView pw,verify_pw;
                    private Button signup;
                    private TextView login;

                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.activity_email_signup);
                        Intent login_intent = new Intent(getApplicationContext(), login.class);


                        email = findViewById(R.id.email_id);
                        email_name = findViewById(R.id.email_name);
                        pw = findViewById(R.id.pwid);
                        verify_pw = findViewById(R.id.verify_pwid);
                        signup = (Button)findViewById(R.id.button_email_signup);
                        login = (TextView) findViewById(R.id.text_email_login);

                        signup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(pw.getText().toString().equals(verify_pw.getText().toString())) {
                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), pw.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(signup.this, "Registered", Toast.LENGTH_SHORT).show();
                                                                            login_intent.putExtra("accountname", email_name.getText().toString());
                                                                            login_intent.putExtra("password", pw.getText().toString());
                                                                            email.setText("");
                                                                            email_name.setText("");
                                                                            verify_pw.setText("");
                                                                            pw.setText("");
                                                                        } else
                                                                            Toast.makeText(signup.this, "Not Registered", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    } else {
                                                        System.out.println("Error");
                                                        task.getException().printStackTrace();
                                                        Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                                else
                                {
                                    verify_pw.setError("Type correct password");
                                    verify_pw.requestFocus();
                                }
                            }
                        });


                        login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                startActivity(login_intent);
                            }
                        });

                    }
                }