package com.sdp.remotehealthcareapp.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.remotehealthcareapp.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
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
    private Uri filePath = null;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    //private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;




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




        firebaseAuth = FirebaseAuth.getInstance();
        user_id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        //storageReference = FirebaseStorage.getInstance().getReference();

        add_new.setOnClickListener(new View.OnClickListener() {
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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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






}