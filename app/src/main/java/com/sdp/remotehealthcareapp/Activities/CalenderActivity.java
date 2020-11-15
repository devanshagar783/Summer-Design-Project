package com.sdp.remotehealthcareapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sdp.remotehealthcareapp.Fragments.Bookdate_Fragment;
import com.sdp.remotehealthcareapp.R;

public class CalenderActivity extends AppCompatActivity {
    CalendarView calendar;
    TextView dateView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_calender);
        calendar = findViewById(R.id.calender);
        dateView = findViewById(R.id.dateView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                dateView.setText(Date);
                Intent intent= getIntent();
                String name, photo, date, time;
                name= intent.getStringExtra("Name");
                photo= intent.getStringExtra("Photo");
                time= intent.getStringExtra("Time");

                Bookdate_Fragment fragment = new Bookdate_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("Date", Date);
                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new Bookdate_Fragment(name, time, photo)).addToBackStack("Profile Setup").commit();

            }
        });
    }
}