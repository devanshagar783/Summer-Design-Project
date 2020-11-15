package com.sdp.remotehealthcareapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sdp.remotehealthcareapp.Activities.CalenderActivity;
import com.sdp.remotehealthcareapp.Activities.GridViewAdapter;
import com.sdp.remotehealthcareapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Bookdate_Fragment extends Fragment {
    String name, time;
    String selected_time;
    View v;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    RecyclerView dataList;
    static List<String> titles;
    String photo;
    TextView Name;
    ImageView Photo;
    GridViewAdapter adapter;
    String Date;


    public Bookdate_Fragment()
    {

    }
    public Bookdate_Fragment(String name, String time, String photo) {
        this.name=name;
        this.time= time;
        this.photo=photo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v=inflater.inflate(R.layout.fragment_bookdate_, container, false);
         setCalender(v);
         setTime(v);
         Name= v.findViewById(R.id.booked_name);
         Name.setText(name);
         Photo= v.findViewById(R.id.booked_image);
         Picasso.get().load(photo).error(R.drawable.ic_launcher_background).into(Photo);

        v.findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_time= GridViewAdapter.time;
               // Toast.makeText(getActivity(), selected_time + mDisplayDate.getText().toString(), Toast.LENGTH_SHORT).show();
                set_appointment();
            }
        });
        return v;

    }
    public void setTime(View v)
    {
        dataList = v.findViewById(R.id.recyclerview_booked);
        titles = new ArrayList<>();
        titles=Arrays.asList(time.split(","));
        adapter = new GridViewAdapter(getContext(),titles);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);

    }


    public void setCalender(View v) {
        mDisplayDate = (TextView) v.findViewById(R.id.tvDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), CalenderActivity.class);
                intent.putExtra("Name", name);
                intent.putExtra("Photo", photo);
                intent.putExtra("Time", time);



                startActivity(intent);
                Date = getArguments().getString("Date");

                mDisplayDate.setText(Date);


                /*Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };*/
            }
        });
    }
    public void set_appointment(){

            HashMap<String, Object> map = new HashMap<>();
            map.put("Appointment time", GridViewAdapter.time);
            //map.put("number", number.getText().toString());
            // map.put("email", email.getText().toString());
            map.put("Appointment date", mDisplayDate.getText().toString());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .set(map, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(getContext(), "Appointment done", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Error in appointment", Toast.LENGTH_SHORT).show();
                        }
                    });
        }



}