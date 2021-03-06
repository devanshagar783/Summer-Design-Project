package com.sdp.remotehealthcareapp.Activities.ReportFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.remotehealthcareapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    private RecyclerView mRecView;
    private ImageAdapter mImageAdapter;
    private ProgressBar mProgressCircle;
    private TextView textView;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRecView = (RecyclerView) findViewById(R.id.recView);
        mRecView.setHasFixedSize(true);
        mRecView.setLayoutManager(new LinearLayoutManager(this));
        textView = (TextView) findViewById(R.id.noData);
        textView.setVisibility(View.GONE);

        mProgressCircle = (ProgressBar) findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mImageAdapter = new ImageAdapter(ImagesActivity.this, mUploads);
        mRecView.setAdapter(mImageAdapter);
        mImageAdapter.setOnItemClickListener(ImagesActivity.this);

        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        assert email != null;
        String user_email = email.substring(0, email.indexOf("@"));

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads/" + user_email);

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                int count = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setmKey(postSnapshot.getKey());
                    mUploads.add(upload);
                    ++count;
                }
                if (count == 0){
                    textView.setVisibility(View.VISIBLE);
                }
                else
                    textView.setVisibility(View.GONE);
                mImageAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at pos " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhateverClick(int position) {
        Toast.makeText(this, "whatever click at pos " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteCLick(int position) {
        Upload selectedItem = mUploads.get(position);
        String selectedKey = selectedItem.getmKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ImagesActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}