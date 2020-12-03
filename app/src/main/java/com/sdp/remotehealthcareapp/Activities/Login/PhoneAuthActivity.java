package com.sdp.remotehealthcareapp.Activities.Login;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.sdp.remotehealthcareapp.Activities.MainActivity;
import com.sdp.remotehealthcareapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private EditText phoneNumber;
    private EditText smsCode;
    private TextView tvLogin;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            phoneNumber = findViewById(R.id.fieldPhoneNumber);
            smsCode = findViewById(R.id.fieldVerificationCode);
            startPhoneNumberVerification(phoneNumber.getText().toString());
        }
        Log.d(TAG, "onStart" );

        // [END_EXCLUDE]
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneauth);


        phoneNumber = findViewById(R.id.fieldPhoneNumber);
        smsCode = findViewById(R.id.fieldVerificationCode);
        mAuth = FirebaseAuth.getInstance();
        tvLogin= findViewById(R.id.tvLogin);
        signOut();
        Log.d(TAG, "onCreate" );


        findViewById(R.id.buttonStartVerification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "buttonVerify");

                    phoneNumber = findViewById(R.id.fieldPhoneNumber);
                    smsCode = findViewById(R.id.fieldVerificationCode);

                    if (!validatePhoneNumber()) {
                        return;
                    }
                    startPhoneNumberVerification(phoneNumber.getText().toString());

                    //auto retrieval of verification code
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseAuthSettings firebaseAuthSettings = auth.getFirebaseAuthSettings();
                    firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber.getText().toString(), smsCode.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(PhoneAuthActivity.this, "Enter Fields", Toast.LENGTH_SHORT).show();
                }
            }

        });

        findViewById(R.id.buttonVerifyPhone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Otp" );

                if (TextUtils.isEmpty(smsCode.getText().toString())) {
                    smsCode.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, smsCode.getText().toString());
            }
        });

        findViewById(R.id.buttonResend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "buttonResend" );

                phoneNumber = findViewById(R.id.fieldPhoneNumber);
                smsCode = findViewById(R.id.fieldVerificationCode);

                resendVerificationCode(phoneNumber.getText().toString(), mResendToken);
            }
        });




        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.d(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(PhoneAuthActivity.this, "phone number is not linked to any account", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(PhoneAuthActivity.this, "Server overload... could not verify", Toast.LENGTH_SHORT).show();
                }
                // Show a message and update the UI
                // ...
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

    }
    private void startPhoneNumberVerification(String phoneNumber) {

       try {
           Log.d(TAG, "startPhoneNumber" );

           FirebaseAuth auth = FirebaseAuth.getInstance();

           // [START start_phone_auth] rebundant function
        /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);*/
           PhoneAuthOptions options =
                   PhoneAuthOptions.newBuilder(mAuth)
                           .setPhoneNumber(phoneNumber)       // Phone number to verify
                           .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                           .setActivity(this)                 // Activity (for callback binding)
                           .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                           .build();

           PhoneAuthProvider.verifyPhoneNumber(options);

           // OnVerificationStateChangedCallbacks
           // [END start_phone_auth]
           mVerificationInProgress = true;
           auth.setLanguageCode("en");
       }
       catch(Exception e)
       {
           Toast.makeText(PhoneAuthActivity.this, " here", Toast.LENGTH_SHORT).show();
       }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.d(TAG, "signInwithPhone" );


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            checkRegister();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(PhoneAuthActivity.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        // [START verify_with_code]
        Log.d(TAG, "verifyPhoneNumberWithcode" );

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            // [END verify_with_code]
            signInWithPhoneAuthCredential(credential);
        }
        catch (Exception e) {
            Toast.makeText(PhoneAuthActivity.this, " Enter the fields", Toast.LENGTH_SHORT).show();

        }
    }



    private boolean validatePhoneNumber() {
        Log.d(TAG, "validatePhoneNumber" );

        phoneNumber = findViewById(R.id.fieldPhoneNumber);
        if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
            phoneNumber.setError("Invalid phone number.");
            return false;
        }

        return true;
    }



    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    token);             // ForceResendingToken from callbacks
        } catch (IllegalArgumentException e) {
            Toast.makeText(PhoneAuthActivity.this, " Enter the fields", Toast.LENGTH_SHORT).show();

        }
    }




    private void signOut() {
        mAuth.signOut();
    }
    //ye


    //checking previous login or not, return true if record exists else return false
    public void checkRegister() {
        TextView phone = findViewById(R.id.fieldPhoneNumber);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("number",phone.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSucessListner");
                        List<DocumentSnapshot> snapshotsList= queryDocumentSnapshots.getDocuments();
                        Intent intent;
                        if(!snapshotsList.isEmpty())
                        {
                            //Log.d(TAG, "inside if" + " No record found");
                            Toast.makeText(PhoneAuthActivity.this, "Verified!.Loggin in", Toast.LENGTH_SHORT).show();
                            intent= new Intent(PhoneAuthActivity.this, MainActivity.class);

                        }
                        else {
                            Log.d(TAG, "inside else");
                            Toast.makeText(PhoneAuthActivity.this, "Verified! Create your account", Toast.LENGTH_SHORT).show();
                            intent= new Intent(PhoneAuthActivity.this, CreateAccount.class);
                        }
                        /*Pair[] pairs    = new Pair[1];
                        intent.putExtra("Number", phone.getText().toString());
                        pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(PhoneAuthActivity.this,pairs);
                        startActivity(intent, activityOptions.toBundle());*/
                        intent.putExtra("Number", phone.getText().toString());
                        intent.putExtra("Title", "Sign Up");
                        startActivity(intent);
                    }
                });

    }

}