package com.cs309.cychedule.activities.ui.dayscounter;

import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cs309.cychedule.R;
import com.cs309.cychedule.activities.LoginActivity;
import com.cs309.cychedule.activities.Main3Activity;
import com.cs309.cychedule.activities.ui.calendar.CalendarFragment;
import com.cs309.cychedule.patterns.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class DaysCounterFragment extends Fragment {

    private static String URL_DAYSCOUNTER = "";

    @BindView(R.id.btn_dcAdd) Button _addButton;
    @BindView(R.id.btn_dcRemove) Button _removeButton;

    private DaysCounterViewModel daysCounterViewModel;
    private DatePicker datepicker;
    int year, month, day;
    int daysleft;
    String secretText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        daysCounterViewModel = ViewModelProviders.of(this).get(DaysCounterViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dayscounter, container, false);

        // final TextView textView = root.findViewById(R.id.text_dayscounter);
        // daysCounterViewModel.getText().observe(this, new Observer<String>() {
        //     @Override
        //     public void onChanged(@Nullable String s) {
        //         textView.setText(s);
        //     }
        // });


        final EditText secretInput = root.findViewById(R.id.et_material_design);

        final DatePicker datePicker = root.findViewById(R.id.datepicker_dayscounter);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DaysCounterFragment.this.year = year;
                DaysCounterFragment.this.month = monthOfYear+1;
                DaysCounterFragment.this.day = dayOfMonth;
            }
        });

        _removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secretInput.setText("");
            }
        });


        _addButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                secretText = secretInput.getText().toString().trim();
                if (secretText.isEmpty()) {
                    Toast emptyInputWarning = Toast.makeText(root.getContext(), "Please enter something!", Toast.LENGTH_SHORT);
                    emptyInputWarning.show();
                }
                else {
                    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// 输入日期的格式
                    String yearStr = year+"";
                    String monthStr = month+"";
                    String dayStr = day+"";
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(yearStr+monthStr+dayStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    GregorianCalendar timer_fur = new GregorianCalendar();
                    GregorianCalendar timer_cur = new GregorianCalendar();
                    timer_fur.setTime(date);
                    timer_cur.setTime(curDate);

                    int dayCount = (int)((timer_fur.getTimeInMillis() - timer_cur.getTimeInMillis()) / (1000*60*60*24));
                    dayCount++;
                    // Snackbar.make(root, "Secret :" + secretText + " @" + year + "." + month + "." + day+"left"+dayCount, Snackbar.LENGTH_LONG)
                    //         .setAction("Action", null).show();
                    Snackbar.make(root,  Calendar.YEAR + "." + Calendar.MONTH + "." + Calendar.DATE+" to "+yearStr + "." + monthStr + "." + dayStr+" lefts "+dayCount
                            +"\nSecret: " + secretText, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


                //这里实现volley
                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("year", year);
                    jsonObject.put("month", month);
                    jsonObject.put("day", day);
                    jsonObject.put("secretText", secretText);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_DAYSCOUNTER, jsonObject,
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