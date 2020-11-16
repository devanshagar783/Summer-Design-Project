package com.sdp.remotehealthcareapp.Activities.Login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sdp.remotehealthcareapp.Activities.MainActivity;
import com.sdp.remotehealthcareapp.R;

import java.util.HashMap;

public class CreateAccount extends AppCompatActivity {
    private static final String TAG = "CreateAccount";
    private FirebaseAuth mAuth;
    TextView fieldName;
    TextView fieldEmail;
    TextView fieldEmailconfirm;
    Button verify, emailRegister;
    Button go;



    FirebaseUser user;
    String Number;
    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activty_createaccount);
        mAuth = FirebaseAuth.getInstance();
        fieldName= findViewById(R.id.fieldName);
        fieldEmail= findViewById(R.id.fieldEmail);
        fieldEmailconfirm= findViewById(R.id.fieldEmailconfirm);
        verify= findViewById(R.id.button_verify);
        emailRegister= findViewById(R.id.button_register_email);
        go= findViewById(R.id.button_signin);
        Number= getIntent().getStringExtra("Number");

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! (fieldEmail.getText().toString().equals(fieldEmailconfirm.getText().toString())) )
                {
                    fieldEmailconfirm.setError("Email Mismatch");
                    fieldEmailconfirm.requestFocus();
                }
                else
                {
                    if(!(fieldEmail.getText().toString().isEmpty()) )
                    {
                        sendemailverify();
                    }
                }
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldName.getText().toString().isEmpty())
                {
                    fieldName.setError("Provide some name");
                    fieldName.requestFocus();
                }
                else
                {
                    saveDetails();
                    Toast.makeText(CreateAccount.this, "Account created sucessfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateAccount.this, MainActivity.class));
                }
                }
        });

        emailRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(fieldEmail.getText().toString(), "12345_admin")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                        Log.d(TAG, "Email verified sucess");
                                        Toast.makeText(CreateAccount.this, "Email verified", Toast.LENGTH_SHORT).show();
                                        saveEmail();
                                    } else {
                                        Log.d(TAG, "Email verified failed");
                                        Toast.makeText(CreateAccount.this, "Please verify email", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });
            }
        });
    }


    private void saveDetails() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("Name", fieldName.getText().toString());
        map.put("number", Number);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(map, SetOptions.merge());

    }
    private void saveEmail() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Email", fieldEmail.getText().toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(map, SetOptions.merge());
        popupwindow();

    }
    private void sendemailverify() {
        AuthCredential credential = EmailAuthProvider.getCredential(fieldEmail.getText().toString(), "12345_admin");
        /*FirebaseAuth.getInstance().createUserWithEmailAndPassword(fieldEmail.getText().toString(), "12345_admin")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {*/
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task1) {
                        if (task1.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            task1.getResult().getUser().sendEmailVerification();
                            emailRegister.setVisibility(View.VISIBLE);
                            verify.setVisibility(View.INVISIBLE);
                            setUser(task1.getResult().getUser());

                                    /*.addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task2) {
                                            if (task2.isSuccessful()) {
                                                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                                    Log.d(TAG, "Email verified sucess");
                                                    Toast.makeText(CreateAccount.this, "Email verified", Toast.LENGTH_SHORT).show();
                                                } else
                                                {
                                                    Log.d(TAG, "Email verified failed");
                                                    Toast.makeText(CreateAccount.this, "Please verify email", Toast.LENGTH_SHORT).show();

                                                }
                                            } else
                                            {
                                                Log.d(TAG, "task unsucess");
                                                Toast.makeText(CreateAccount.this, task2.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }*/
                                        }

                         else {
                            Log.w(TAG, "linkWithCredential:failure", task1.getException());
                            Toast.makeText(CreateAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void popupwindow(){
        dialogBuilder= new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_verified, null);
        dialogBuilder.setView(contactPopupView);
        alertDialog= dialogBuilder.create();
        alertDialog.show();

        contactPopupView.findViewById(R.id.popup_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    public FirebaseUser getUser() {

        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

}
