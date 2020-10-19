package com.sdp.remotehealthcareapp.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sdp.remotehealthcareapp.R;

import org.jetbrains.annotations.NotNull;

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
        // [END_EXCLUDE]
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);


        phoneNumber = findViewById(R.id.fieldPhoneNumber);
        smsCode = findViewById(R.id.fieldVerificationCode);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonStartVerification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        findViewById(R.id.buttonVerifyPhone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = findViewById(R.id.fieldPhoneNumber);
                smsCode = findViewById(R.id.fieldVerificationCode);

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
                phoneNumber = findViewById(R.id.fieldPhoneNumber);
                smsCode = findViewById(R.id.fieldVerificationCode);

                resendVerificationCode(phoneNumber.getText().toString(), mResendToken);
            }
        });

        findViewById(R.id.signOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
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
                Log.w(TAG, "onVerificationFailed", e);

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

//        startPhoneNumberVerification(phoneNumber.getText().toString());

/*
        //To test using whitelisted numbers
        // The test phone number and code should be whitelisted in the console.
        String phoneNumber = "+15555555555";
        String smsCode = "123456";

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

        // Configure faking the auto-retrieval with the whitelisted numbers.
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);

        PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneAuthProvider.verifyPhoneNumber(
                phoneNumber,
                60L,
                TimeUnit.SECONDS,
                this, // activity
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // Instant verification is applied and a credential is directly returned.
                        // ...
                        Log.d(TAG, "onVerificationCompleted:" + credential);
                        Toast.makeText(PhoneAuthActivity.this, "Verified! Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);

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
                        //Toast.makeText(this, "Verification failed", Toast.LENGTH_SHORT).show();
                    }
                    // ...
                });
*/



    private void startPhoneNumberVerification(String phoneNumber) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
        auth.setLanguageCode("fr");

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(PhoneAuthActivity.this, "Verified! Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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



    private boolean validatePhoneNumber() {
        phoneNumber = findViewById(R.id.fieldPhoneNumber);
        if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
            phoneNumber.setError("Invalid phone number.");
            return false;
        }

        return true;
    }



    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }



    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }




    private void signOut() {
        mAuth.signOut();
    }

}