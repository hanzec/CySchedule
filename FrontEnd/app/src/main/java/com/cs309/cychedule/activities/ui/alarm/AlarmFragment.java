package com.cs309.cychedule.activities.ui.alarm;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cs309.cychedule.R;
import com.cs309.cychedule.activities.LoginActivity;

/**
 * AlarmFragment is a tab to contain our alarm tool.
 * We achieve the alarm function here.
 */
public class AlarmFragment extends Fragment {

    private AlarmViewModel alarmViewModel;

    private Button btnAdd;
    int hour, minute;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_alarm, container, false);
        // final TextView textView = root.findViewById(R.id.text_alram);
        // alarmViewModel.getText().observe(this, new Observer<String>() {
        //     @Override
        //     public void onChanged(@Nullable String s) {
        //         textView.setText(s);
        //     }
        // });

        final TimePicker timePicker = root.findViewById(R.id.timerpicker_timer);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int j) {
                Toast.makeText(root.getContext(), "Timer setting: " + i + ":" + j + "!", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd = root.findViewById(R.id.addAlarm);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
                Snackbar.make(root,  "Alarm is set at"+hour+":"+minute, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


            //这里实现volley
        });
        return root;
    }
}