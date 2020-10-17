package com.sdp.remotehealthcareapp.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.remotehealthcareapp.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class At_repo extends Fragment {

    View V;

    private Button buttonChoose;
    private Button add_new;
    private EditText editTextName;
    private TextView textViewShow;
    private ImageView imageView;

    private ProgressDialog progressDialog;
    private Uri imageUri = null;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    //private StorageReference storageReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        V=inflater.inflate(R.layout.fragment_attachments, container, false);
        textViewShow=V.findViewById(R.id.text_user);
        textViewShow.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        add_new= V.findViewById(R.id.image_upload);
        imageView= V.findViewById(R.id.image_uploadshow);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);





        firebaseAuth = FirebaseAuth.getInstance();
        user_id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        //storageReference = FirebaseStorage.getInstance().getReference();

        user_upload.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                             if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                                 Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                                                 ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                                             } else {

                                                 choseImage();

                                             }

                                         }

                                     }

        );
        return V;

    }

    private void choseImage() {
        Toast.makeText(getContext(), "going inside", Toast.LENGTH_SHORT).show();

        /*CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);*/
        Intent goEdit;
        goEdit = new Intent(getActivity(), CropImage.class);
        startActivityForResult(goEdit,1);
        Toast.makeText(getContext(), "going outside", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getContext(), "outside if", Toast.LENGTH_SHORT).show();

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == 1) {

                imageUri = result.getUri();
                image_uploadshow.setImageURI(imageUri);
                Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(getContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getContext(), "Not selected", Toast.LENGTH_SHORT).show();

        }

    }

    public void init(){
        text_user=V.findViewById(R.id.text_user);
        text_user.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        user_upload= V.findViewById(R.id.image_upload);
        image_uploadshow= V.findViewById(R.id.image_uploadshow);
    }
}