package com.cs309.cychedule.activities.ui.calendar;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.cs309.cychedule.R;
import com.cs309.cychedule.activities.MainActivity;

import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {

    private CalendarViewModel galleryViewModel;
    private Button btnAdd;
    private DatePicker datepicker;
    int year,month,day;
    
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        
        final TextView textView = root.findViewById(R.id.text_calendar);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        
        final EditText secret = root.findViewById (R.id.et_material_design);
       final DatePicker datePicker = root.findViewById(R.id.datepicker);
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                CalendarFragment.this.year=year;
                CalendarFragment.this.month=monthOfYear;
                CalendarFragment.this.day=dayOfMonth;
            }
        });
    
      

        btnAdd = root.findViewById(R.id.addCalendar);
        btnAdd.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //这里实现volley
                    Snackbar.make(root, "Secret :" + secret.getText() + "@year"+year+"."+month+"."+day, Snackbar.LENGTH_LONG).setAction("Action", null).show(); }
            });
        return root;
    }
    
    
}