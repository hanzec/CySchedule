package com.cs309.cychedule.activities.ui.calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cs309.cychedule.R;
import com.cs309.cychedule.patterns.Singleton;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.RestAPIService;

import org.json.JSONObject;

import java.security.Key;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * CalendarFragment is a tab to contain our calendar tool.
 * We achieve the calendar function here.
 */
public class CalendarFragment extends Fragment {
    
    private CalendarViewModel calendarViewModel;
    private Button btnAdd, btnRemvoe;
    int startYear, startMonth, startDay, startHour, startMinute;
    String startDateStr, startTimeStr;
    int endYear, endMonth, endDay, endHour, endMinute;
    String endDateStr, endTimeStr;
    
    String startStr, endStr;
    String eventText, locationText;
    private Calendar calendar;
    
    private static String URL_ADDEVENT = "https://dev.hanzec.com/api/v1/event/add";
    String token = "";

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
        
        final ImageView logo = root.findViewById(R.id.cal_logo);
        
        calendar = Calendar.getInstance();
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = calendar.get(Calendar.MINUTE);
        
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        final EditText startDateInput = root.findViewById(R.id.input_cal_startDate);
        startDateInput.setClickable(true);
        startDateInput.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog
                        (root.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startYear = year;
                                startMonth = monthOfYear;
                                startDay = dayOfMonth;
                                startDateStr = String.valueOf(year) + "." + String.valueOf(monthOfYear + 1) + "." + Integer.toString(dayOfMonth);
                                startDateInput.setText(startDateStr);
                            }}, startYear, startMonth, startDay);
                datePickerDialog.show();
                // datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Objects.requireNonNull(datePickerDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        });
        
        final EditText startTimeInput = root.findViewById(R.id.input_cal_startTime);
        startTimeInput.setClickable(true);
        startTimeInput.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TimePickerDialog datePickerDialog = new TimePickerDialog
                        (root.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startHour = hourOfDay;
                                startMinute = minute;
                                startTimeStr= String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                startTimeInput.setText(startTimeStr);
                            }}, startHour, startMinute, true);
                datePickerDialog.show();
                // datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Objects.requireNonNull(datePickerDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        });
        
        final EditText endDateInput = root.findViewById(R.id.input_cal_endDate);
        endDateInput.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog
                        (root.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endYear = year;
                                endMonth = monthOfYear;
                                endDay = dayOfMonth;
                                endDateStr = String.valueOf(year) + "." + String.valueOf(monthOfYear + 1) + "." + Integer.toString(dayOfMonth);
                                endDateInput.setText(endDateStr);
                            }}, endYear, endMonth, endDay);
                datePickerDialog.show();
                // datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Objects.requireNonNull(datePickerDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        });
        
        final EditText endTimeInput = root.findViewById(R.id.input_cal_endTime);
        endTimeInput.setClickable(true);
        endTimeInput.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TimePickerDialog datePickerDialog = new TimePickerDialog
                        (root.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endHour = hourOfDay;
                                endMinute = minute;
                                endTimeStr = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                endTimeInput.setText(endTimeStr);
                            }}, endHour, endMinute, true);
                datePickerDialog.show();
                // datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Objects.requireNonNull(datePickerDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        });
        final EditText eventInput = root.findViewById(R.id.input_cal_event);
        final EditText locationInput = root.findViewById(R.id.input_cal_location);
        
        //
        // timePicker.setIs24HourView(true);
        
        
        final CheckBox cbox_justThisDay = root.findViewById(R.id.cbox_cal_justThisDay);
        cbox_justThisDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    root.findViewById(R.id.cal_endLayout).setVisibility(View.GONE);
                    endDateInput.setText("");
                    endTimeInput.setText("");
                    logo.setImageDrawable(getResources().getDrawable(R.drawable.gitcat3));
                }else{
                    root.findViewById(R.id.cal_endLayout).setVisibility(View.VISIBLE);
                    logo.setImageDrawable(getResources().getDrawable(R.drawable.gitcat));
                }
            }
        });
        
        final CheckBox cbox_allDayAct = root.findViewById(R.id.cbox_cal_allDayActiviy);
        cbox_allDayAct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // startTimeInput.setHint("ALL_DAY");
                    logo.setImageDrawable(getResources().getDrawable(R.drawable.gitcat4));
                    startTimeInput.setText("ALL_DAY");
                    startTimeStr="";
                    startTimeInput.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                        }
                    });
                    // startTimeInput.setFocusable(false);
                    // startTimeInput.setClickable(false);
                    // startTimeInput.setFocusableInTouchMode(false);
                    // startTimeInput.setVisibility(View.INVISIBLE);
                    // endTimeInput.setHint("ALL_DAY");
                    endTimeInput.setText("ALL_DAY");
                    endTimeStr="";
                    endTimeInput.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                        }
                    });
                    // endTimeInput.setFocusable(false);
                    // endTimeInput.setFocusableInTouchMode(false);
                    // endTimeInput.setClickable(false);
                    // endTimeInput.setVisibility(View.INVISIBLE);
                }else{
                    logo.setImageDrawable(getResources().getDrawable(R.drawable.gitcat));
                    startTimeInput.setText("");
                    startTimeInput.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            TimePickerDialog datePickerDialog = new TimePickerDialog
                                    (root.getContext(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            startHour = hourOfDay;
                                            startMinute = minute;
                                            startTimeStr= String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                            startTimeInput.setText(startTimeStr);
                                        }}, startHour, startMinute, true);
                            datePickerDialog.show();
                            // datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            Objects.requireNonNull(datePickerDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        }
                    });
                    
                    endTimeInput.setText("");
                    endTimeInput.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            TimePickerDialog datePickerDialog = new TimePickerDialog
                                    (root.getContext(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            endHour = hourOfDay;
                                            endMinute = minute;
                                            endTimeStr = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                            endTimeInput.setText(endTimeStr);
                                        }}, endHour, endMinute, true);
                            datePickerDialog.show();
                            // datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            Objects.requireNonNull(datePickerDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        }
                    });
                }
            }
        });
        
        
        btnRemvoe = root.findViewById(R.id.btn_cal_clear);
        btnRemvoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventInput.setText("");
                locationInput.setText("");
                startDateInput.setText("");
                startTimeInput.setText("");
                endDateInput.setText("");
                endTimeInput.setText("");
                
            }
        });
        
        
        btnAdd = root.findViewById(R.id.btn_cal_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventText = eventInput.getText().toString();
                locationText = locationInput.getText().toString();
                boolean error = false;
                if (startDateInput.getText().toString().isEmpty()){
                    Toast emptyInputWarning = Toast.makeText(root.getContext(), "Please enter the start date!", Toast.LENGTH_SHORT);
                    emptyInputWarning.show();
                    error = true;
                }
                if (endDateInput.getText().toString().isEmpty() && !cbox_justThisDay.isChecked()){
                    Toast emptyInputWarning = Toast.makeText(root.getContext(), "Please enter the end date!", Toast.LENGTH_SHORT);
                    emptyInputWarning.show();
                    error = true;
                }
                
                if (startTimeInput.getText().toString().isEmpty() && !cbox_allDayAct.isChecked()){
                    startTimeInput.setText("ALL_DAY");
                }
                
                if (endTimeInput.getText().toString().isEmpty() && !cbox_allDayAct.isChecked()){
                    endTimeInput.setText("ALL_DAY");
                }
                if ((startTimeInput.getText().toString().equals("ALL_DAY") || endTimeInput.getText().toString().equals("ALL_DAY"))
                        && !startTimeInput.getText().toString().equals(endTimeInput.getText().toString())
                        && !cbox_justThisDay.isChecked()
                ){
                    Toast emptyInputWarning = Toast.makeText(root.getContext(), "Please check the time or either check All_day_act checkbox!", Toast.LENGTH_SHORT);
                    emptyInputWarning.show();
                    error = true;
                }
                if (locationInput.getText().toString().isEmpty()) {
                    locationInput.setText("Anywhere");
                }
                if (eventText.isEmpty()) {
                    Toast emptyInputWarning = Toast.makeText(root.getContext(), "Please enter the event description!", Toast.LENGTH_SHORT);
                    emptyInputWarning.show();
                    error = true;
                }
                String startIntTemp,endIntTemp;
                if (startTimeInput.getText().toString().equals("ALL_DAY") || endTimeInput.getText().toString().equals("ALL_DAY")) {
                    startIntTemp = "" + startYear + startMonth + startDay;
                    endIntTemp = "" + endYear + endMonth + endDay;
                }else {
                    startIntTemp = "" + startYear + startMonth + startDay+startHour+startMinute;
                    endIntTemp = "" + endYear + endMonth + endDay+endHour+endMinute;
                }
                long startLong = Long.parseLong(startIntTemp);
                long endLong = Long.parseLong(endIntTemp);
                if (startLong>=endLong && !cbox_justThisDay.isChecked()){
                    Log.e("TAG", startLong+" "+endLong);
                    Toast emptyInputWarning = Toast.makeText(root.getContext(), "Please enter a valid end date", Toast.LENGTH_SHORT);
                    emptyInputWarning.show();
                    error = true;
                }
                if(!error){
                    logo.setImageDrawable(getResources().getDrawable(R.drawable.gitcat2));
                    locationText = locationInput.getText().toString();
                    startStr = startDateInput.getText() + " " + startTimeInput.getText();
                    endStr = endDateInput.getText() + " " + endTimeInput.getText();
                    
                    //提交这些变量startStr,endStr,eventText,locationText, and Token
                    if(cbox_justThisDay.isChecked()){
                        Snackbar.make(root,  startStr
                                        +":\nEvent: " + eventText + " @" + locationText
                                , Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else {
                        Snackbar.make(root,  "From"+startStr +"to" + endStr
                                        +"\nEvent: " + eventText + " @" + locationText
                                , Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    
                    //sign token as header
                    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                    final String token = Jwts.builder().setSubject("CySchedule").signWith(key).compact();
                    
                    final RequestQueue requestQueue = Singleton.getInstance(root.getContext()).getRequestQueue();
                    //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.start();
                    
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDEVENT,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try
                                    {
                                        JSONObject object = new JSONObject(response);
                                        String status = object.getString("status");
                                        JSONObject loginToken = object.getJSONObject("responseBody");
                                        
                                        
                                        Log.e("TAG", response);
                                        Log.e("TAG", object.toString());
                                        Log.e("TAG", status.toString());
                                        Log.e("TAG", loginToken.toString());
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace(); Log.e("TAG", e.toString());
                                        Toast.makeText(root.getContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("TAG", error.toString());
                                    Toast.makeText(root.getContext(), "Login Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                    
                                }
                            })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", "NULL");
                            params.put("startTime", startStr);
                            params.put("endTime", endStr);
                            params.put("location", locationText);
                            params.put("description", eventText);
                            return params;
                        }
                        
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("Authorization",token);
                            return params;
                        }
                    };
                    //requestQueue.add(stringRequest);
                    Singleton.getInstance(root.getContext()).addToRequestQueue(stringRequest);
                    
                    //这里实现volley
                }
            }
        });
        return root;
    }
    
}