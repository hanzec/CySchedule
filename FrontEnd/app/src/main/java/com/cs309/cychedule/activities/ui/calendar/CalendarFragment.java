package com.cs309.cychedule.activities.ui.calendar;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cs309.cychedule.R;
import com.cs309.cychedule.patterns.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;

public class CalendarFragment extends Fragment {

    private static String URL_CALENDAR = "";

    @BindView(R.id.btn_cAdd) Button _addButton;
    @BindView(R.id.btn_cRemove) Button _removeButton;

    private CalendarViewModel calendarViewModel;
    private DatePicker datepicker;
    int year, month, day, hour, minute;
    String eventText, locationText;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        // final TextView textView = root.findViewById(R.id.text_calendar);
        // calendarViewModel.getText().observe(this, new Observer<String>() {
        // 	@Override
        // 	public void onChanged(@Nullable String s) {
        // 		textView.setText(s);
        // 	}
        // });

        final EditText eventInput = root.findViewById(R.id.eventTextInput);
        final EditText locationInput = root.findViewById(R.id.locationTextInput);


        final DatePicker datePicker = root.findViewById(R.id.datepicker_calendar);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                CalendarFragment.this.year = year;
                CalendarFragment.this.month = monthOfYear+1;
                CalendarFragment.this.day = dayOfMonth;
            }
        });

        final TimePicker timePicker = root.findViewById(R.id.timepicker_calendar);
        timePicker.setIs24HourView(true);


        _removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventInput.setText("");
                locationInput.setText("");
            }
        });


        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventText = eventInput.getText().toString();
                locationText = locationInput.getText().toString();
                if (eventText.isEmpty()) {
                    Toast emptyInputWarning = Toast.makeText(root.getContext(), "Please enter the even description!", Toast.LENGTH_SHORT);
                    emptyInputWarning.show();
                }
                else {
                    if(locationText.isEmpty()){
                        locationInput.setText("Anywhere");
                    }
                    locationText = locationInput.getText().toString();
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                    Snackbar.make(root, "Secret :" + eventText
                                    + " @" + year + "." + month + "." + day + "." + hour + ":" + minute
                                    + " @" + locationText
                            , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                //这里实现volley
                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("eventText", eventText);
                    jsonObject.put("year", year);
                    jsonObject.put("month", month);
                    jsonObject.put("day", day);
                    jsonObject.put("hour", hour);
                    jsonObject.put("minute", minute);
                    jsonObject.put("locationText", locationText);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_CALENDAR, jsonObject,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                try
                                {
                                    String status = response.getString("status");
                                    if (status.equals("201"))
                                    {
                                        Toast.makeText(root.getContext(), "Add Event Success!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                    Toast.makeText(root.getContext(), "Add Event Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                                    _addButton.setVisibility(View.VISIBLE);
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(root.getContext(), "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                                _addButton.setVisibility(View.VISIBLE);
                            }
                        }
                )
                {
                    @Override
                    public int getMethod() {
                        return Method.POST;
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.NORMAL;
                    }
                };
                Singleton.getInstance(root.getContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
        return root;
    }


}