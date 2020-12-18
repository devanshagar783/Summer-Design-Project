package com.sdp.remotehealthcareapp.Activities.ReportFiles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sdp.remotehealthcareapp.Activities.MainActivity;
import com.sdp.remotehealthcareapp.R;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;

public class AttachmentReportActivity extends AppCompatActivity {

    private static final String TAG = "AttachmentReportActivit";
    private TextView userName; // for displaying user's name
    private ImageView openImage; // open new image
    private ImageView showImage; // shows the image opened
    private TextView imageName; // shows image name
    private Button upload; // upload image
    private TextView getPrevious; // see previous reports
    private ProgressBar progressBar;

    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment_report);

        userName = (TextView) findViewById(R.id.user_name);
        userName.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        openImage = (ImageView) findViewById(R.id.image_open);
        showImage = (ImageView) findViewById(R.id.image_uploadshow);
        imageName = (TextView) findViewById(R.id.image_name);
        upload = (Button) findViewById(R.id.upload_image);
        getPrevious = (TextView) findViewById(R.id.see_previous);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        assert email != null;
        String user_email = email.substring(0, email.indexOf("@"));

        mDatabase = FirebaseDatabase.getInstance().getReference("uploads/" + user_email);
        storageReference = FirebaseStorage.getInstance().getReference("uploads/" + user_email);

        openImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else
                    uploadFile();
            }
        });

//        getPrevious.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openImagesActivity();
//            }
//        });

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                openImagesActivity();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Please verify your credentials")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        getPrevious.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

    }


    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            imageUri = data.getData();

            showImage.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            uploadTask = fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setProgress(0);
                                        }
                                    }, 500);

                                    Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                                    Upload upload = new Upload(imageName.getText().toString().trim(),
                                            uri.toString());
                                    upload.setImageUrl(uri.toString());
                                    String uploadId = mDatabase.push().getKey();
                                    mDatabase.child(uploadId).setValue(upload);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void openImagesActivity() {
        Intent intent = new Intent(getApplicationContext(), ImagesActivity.class);
        startActivity(intent);
    }
}