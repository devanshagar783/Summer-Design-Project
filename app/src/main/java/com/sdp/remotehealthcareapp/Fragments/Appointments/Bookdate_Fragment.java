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
import android.widget.CalendarView;
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
import com.sdp.remotehealthcareapp.Activities.GridViewAdapter;
import com.sdp.remotehealthcareapp.Activities.MainActivity;
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

    private NotificationManagerCompat notificationManagerCompat;


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


                //This is for notification
                notificationManagerCompat = NotificationManagerCompat.from(getActivity().getApplicationContext());
                String text = "Your Appointment is booked";
                String mesg = "Display the date of appointment";

                Intent intent = new Intent(getContext(), MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getContext(),
                        0, intent, 0);

                Notification notification = new NotificationCompat.Builder(getActivity().getApplicationContext(), CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.oneicon)//notification icon
                        .setContentTitle(text)
                        .setContentText(mesg)
                        .setStyle(new NotificationCompat.BigTextStyle()//only shows around 250 characters
                                .bigText("hello wordsnisag   ugfius kuffds hslbdfdsh oi xsuig dhi ghiuhdoihoig ui ghsoih oif oi hksdmil heyoi    + 5+  u vgut sei ges8sog i hgo dhslik fh osu hgotz olhkjhoiyha oih nisud fgdsiuk beilugsvhjmgikuh geiugsdigzhigo8yhoig hoi hoi hoihoighiughi hlo oi.l gjhurxxdol8 yeo i o i uoeyseoi yhoihio lghpoh oirh hello wordsnisag   ugfius kuffds hslbdfdsh oi xsuig dhi ghiuhdoihoig ui ghsoih oif oi hksdmil heyoi    + 5+  u vgut sei ges8sog i hgo dhslik fh osu hgotz olhkjhoiyha oih nisud fgdsiuk beilugsvhjmgikuh geiugsdigzhigo8yhoig hoi hoi hoihoighiughi hlo oi.l gjhurxxdol8 yeo i o i uoeyseoi yhoihio lghpoh oirh hello wordsnisag   ugfius kuffds hslbdfdsh oi xsuig dhi ghiuhdoihoig ui ghsoih oif oi hksdmil heyoi    + 5+  u vgut sei ges8sog i hgo dhslik fh osu hgotz olhkjhoiyha oih nisud fgdsiuk beilugsvhjmgikuh geiugsdigzhigo8yhoig hoi hoi hoihoighiughi hlo oi.l gjhurxxdol8 yeo i o i uoeyseoi yhoihio lghpoh oirh hello wordsnisag   ugfius kuffds hslbdfdsh oi xsuig dhi ghiuhdoihoig ui ghsoih oif oi hksdmil heyoi    + 5+  u vgut sei ges8sog i hgo dhslik fh osu hgotz olhkjhoiyha oih nisud fgdsiuk beilugsvhjmgikuh geiugsdigzhigo8yhoig hoi hoi hoihoighiughi hlo oi.l gjhurxxdol8 yeo i o i uoeyseoi yhoihio lghpoh oirh hello wordsnisag   ugfius kuffds hslbdfdsh oi xsuig dhi ghiuhdoihoig ui ghsoih oif oi hksdmil heyoi    + 5+  u vgut sei ges8sog i hgo dhslik fh osu hgotz olhkjhoiyha oih nisud fgdsiuk beilugsvhjmgikuh geiugsdigzhigo8yhoig hoi hoi hoihoighiughi hlo oi.l gjhurxxdol8 yeo i o i uoeyseoi yhoihio lghpoh oirh hello wordsnisag   ugfius kuffds hslbdfdsh oi xsuig dhi ghiuhdoihoig ui ghsoih oif oi hksdmil heyoi    + 5+  u vgut sei ges8sog i hgo dhslik fh osu hgotz olhkjhoiyha oih nisud fgdsiuk beilugsvhjmgikuh geiugsdigzhigo8yhoig hoi hoi hoihoighiughi hlo oi.l gjhurxxdol8 yeo i o i uoeyseoi yhoihio lghpoh oirh hello wordsnisag   ugfius kuffds hslbdfdsh oi xsuig dhi ghiuhdoihoig ui ghsoih oif oi hksdmil heyoi    + 5+  u vgut sei ges8sog i hgo dhslik fh osu hgotz olhkjhoiyha oih nisud fgdsiuk beilugsvhjmgikuh geiugsdigzhigo8yhoig hoi hoi hoihoighiughi hlo oi.l gjhurxxdol8 yeo i o i uoeyseoi yhoihio lghpoh oirh hello wordsnisag   ugfius kuffds hslbdfdsh oi xsuig dhi ghiuhdoihoig ui ghsoih oif oi hksdmil heyoi    + 5+  u vgut sei ges8sog i hgo dhslik fh osu hgotz olhkjhoiyha oih nisud fgdsiuk beilugsvhjmgikuh geiugsdigzhigo8yhoig hoi hoi hoihoighiughi hlo oi.l gjhurxxdol8 yeo i o i uoeyseoi yhoihio lghpoh oirh ")
                                .setBigContentTitle(text)
                                .setSummaryText("Appointment Booking"))
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
                AlertDialog alertDialog;
                AlertDialog.Builder dialogBuilder;
                dialogBuilder= new AlertDialog.Builder(getActivity());
                final View contactPopupView = getLayoutInflater().inflate(R.layout.item_calender, null);
                dialogBuilder.setView(contactPopupView);
                alertDialog= dialogBuilder.create();
                alertDialog.show();

                CalendarView calendar = contactPopupView.findViewById(R.id.calender);
                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                        mDisplayDate.setText(Date);
                    }
                });
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