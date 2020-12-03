package com.sdp.remotehealthcareapp.Fragments.Appointments;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.sdp.remotehealthcareapp.Activities.GridViewAdapter;
import com.sdp.remotehealthcareapp.Activities.MainActivity;
import com.sdp.remotehealthcareapp.Fragments.Appointments.Dataclass.Dataclass_Doctors;
import com.sdp.remotehealthcareapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.sdp.remotehealthcareapp.Activities.Notifications.App.CHANNEL_1_ID;

public class Bookdate_Fragment extends Fragment {

    String name, time, selected_time, photo, visits, day;
    View v;
    private TextView mDisplayDate, Name;
    RecyclerView dataList;
    static List<String> array_title = new ArrayList<>();
    ImageView Photo;
    GridViewAdapter adapter;
    Spinner spinner;
    String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;
    private static final String TAG = "Bookdate_Fragment";

    private NotificationManagerCompat notificationManagerCompat;


    public Bookdate_Fragment() {

    }

    public Bookdate_Fragment(Dataclass_Doctors model) {
        this.name = model.getName();
        this.time = model.getTime();
        this.photo = model.getPhotoURL();
        this.visits = model.getVisits();
        Log.d(TAG, "Bookdate_Fragment(Constructor)");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bookdate_, container, false);
        setSpinner(v);
        setCalender(v);
        //setTime(v);
        Name = v.findViewById(R.id.booked_name);
        Name.setText(name);
        Photo = v.findViewById(R.id.booked_image);
        Picasso.get().load(photo).error(R.drawable.ic_launcher_background).into(Photo);

        v.findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_time = GridViewAdapter.time;
                // Toast.makeText(getActivity(), selected_time + mDisplayDate.getText().toString(), Toast.LENGTH_SHORT).show();
                set_appointment();
                popupwindow();


                //This is for notification
                notificationManagerCompat = NotificationManagerCompat.from(getActivity().getApplicationContext());
                String text = "Your Appointment is booked";

                Intent intent = new Intent(getContext(), MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getContext(),
                        0, intent, 0);

                Notification notification = new NotificationCompat.Builder(getActivity().getApplicationContext(), CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.oneicon)//notification icon
                        .setContentTitle(text + " with " + name)
                        .setContentText("Date : " + mDisplayDate.getText().toString())
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine("Date : " + mDisplayDate.getText().toString())
                                .addLine("Time : " + GridViewAdapter.time)
                                .addLine(spinner.getSelectedItem().toString()))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_EVENT)
                        .setColor(Color.GREEN)
                        .setContentIntent(contentIntent)//what do open when clicked
                        .setAutoCancel(true)//dismiss noti when clicked
                        .setOnlyAlertOnce(true)//no sound when updated
                        .build();
                notificationManagerCompat.notify(1, notification);
            }
        });
        return v;
    }

    public void setTime(View v, String day_current) {
        Log.d(TAG, "setTime");
        dataList = v.findViewById(R.id.recyclerview_booked);
        //array_title.clear();
        getTime(day_current);
        /*
        titles = new ArrayList<>();
        titles=Arrays.asList(time.split(","));*/
        if (!array_title.isEmpty()) {
            adapter = new GridViewAdapter(getContext(), array_title);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
            dataList.setLayoutManager(gridLayoutManager);
            dataList.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void setCalender(View v) {
        Log.d(TAG, "setCalender");

        mDisplayDate = (TextView) v.findViewById(R.id.tvDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog;
                AlertDialog.Builder dialogBuilder;
                dialogBuilder = new AlertDialog.Builder(getActivity());
                final View contactPopupView = getLayoutInflater().inflate(R.layout.item_calender, null);
                dialogBuilder.setView(contactPopupView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();

                CalendarView calendar = contactPopupView.findViewById(R.id.calender);
                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                        mDisplayDate.setText(Date);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);

                        day = days[calendar.get(calendar.DAY_OF_WEEK) - 1];

                        //Toast.makeText(getActivity(), day,Toast.LENGTH_SHORT).show();
                        setTime(v, day);
                    }
                });
            }
        });
    }

    public void set_appointment() {
        Log.d(TAG, "set_appointment");

        HashMap<String, Object> map = new HashMap<>();
        map.put("time", GridViewAdapter.time);
        map.put("date", mDisplayDate.getText().toString());
        map.put("dr", name);
        map.put("patient", MainActivity.getName());
        map.put("photo", photo);
        map.put("clinic", spinner.getSelectedItem().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Appointments")
                .document()
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getContext(), "Appointment done", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Error in appointment", Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseFirestore.getInstance().collection("users")
                .document("Appointments").collection("Dataset")
                .document()
                .set(map, SetOptions.merge());

    }

    public void setSpinner(View v) {
        Log.d(TAG, "setSpinner");
        spinner = v.findViewById(R.id.spinner_clinic);

        List<String> subjects = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        List<String> names = Arrays.asList(visits.split(","));
        subjects.addAll(names);
        adapter.notifyDataSetChanged();

        //Firebase action
        /*FirebaseFirestore.getInstance().collection("users")
                .document("Clinic").collection("Names")
                .document("MD4hTqldIlMG1Pgk3ThW").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document= task.getResult();
                    assert document != null;
                    List<String> names = Arrays.asList(Objects.requireNonNull(document.getString("Clinics")).split(","));
                    subjects.addAll(names);

                    adapter.notifyDataSetChanged();
                }
            }
        });*/

        spinner.setPrompt("Clinics");
    }

    public void getTime(String current_day) {
        Log.d(TAG, "getTime");

        FirebaseFirestore.getInstance().collection("users")
                .document("Doctors").collection("Names")
                .document(name).collection(spinner.getSelectedItem().toString())
                .document(current_day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            String time = document.getString("Time");
                            try {
                                /*array_title = Arrays.asList(time.split(","));*/
                                array_title.clear();
                                List<String> names = Arrays.asList(time.split(","));
                                array_title.addAll(names);
                            } catch (NullPointerException e) {
                                dataList.setAdapter(null);
                                Toast.makeText(getActivity(), "Doctor is not scheduled, select another date", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

        //checking time clashes
        FirebaseFirestore.getInstance().collection("users")
                .document("Appointments").collection("Dataset")
                .whereEqualTo("clinic", spinner.getSelectedItem().toString())
                .whereEqualTo("date", mDisplayDate.getText().toString())
                .whereEqualTo("dr", name)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : snapshotList) {
                    String time = snapshot.getString("time").toString();
                    array_title.remove(time);
                }
            }
        });
    }

    private void popupwindow() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.confirm_bookedapp, null);
        dialogBuilder.setView(contactPopupView);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        init(contactPopupView);

        /*contactPopupView.findViewById(R.id.popup_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });*/
    }

    private void init(View v) {
        ImageView doc_photo = v.findViewById(R.id.confirm_booked_image);
        TextView doc_name = v.findViewById(R.id.confirm_booked_name);
        TextView patient = v.findViewById(R.id.confirm_booked_patient_name);
        TextView time = v.findViewById(R.id.confirm_booked_time);
        TextView clinic = v.findViewById(R.id.confirm_booked_clinic);

        Picasso.get().load(photo).error(R.drawable.ic_launcher_background).into(doc_photo);
        doc_name.setText(name);
        patient.setText(MainActivity.getName());
        clinic.setText(spinner.getSelectedItem().toString());
        time.setText(GridViewAdapter.time + " " + mDisplayDate.getText().toString());
    }

}