package com.cs309.cychedule.activities.ui.dayscounter;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.cs309.cychedule.R;
import com.cs309.cychedule.activities.SignupActivity;
import com.cs309.cychedule.activities.ui.calendar.CalendarFragment;
import com.cs309.cychedule.patterns.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * DaysCounterFragment is a tab to contain our days count down tool.
 * We achieve the count down function here.
 */
public class DaysCounterFragment extends Fragment {

    private static String URL_EVENT = "https://dev.hanzec.com/api/v1/event/add";
    private DaysCounterViewModel daysCounterViewModel;
    private Button btnAdd, btnRemvoe;
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

        btnRemvoe = root.findViewById(R.id.removeText_dayscounter);
        btnRemvoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secretInput.setText("");
            }
        });


        btnAdd = root.findViewById(R.id.addEvent_dayscounter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secretText = secretInput.getText().toString().trim();
                if (secretText.isEmpty()) {
                    Toast emptyInputWarning = Toast.makeText(root.getContext(), "Please enter something!", Toast.LENGTH_SHORT);
                    emptyInputWarning.show();
                }
                else {
                    final Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
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
                    GregorianCalendar cal1 = new GregorianCalendar();
                    GregorianCalendar cal2 = new GregorianCalendar();
                    cal1.setTime(date);
                    cal2.setTime(curDate);
                    int dayCount = (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis()) / (1000*60*60*24));
                    dayCount++;
                    // Snackbar.make(root, "Secret :" + secretText + " @" + year + "." + month + "." + day+"left"+dayCount, Snackbar.LENGTH_LONG)
                    //         .setAction("Action", null).show();
                    Snackbar.make(root,  year + "." + month + "." + day+" to "+yearStr + "." + monthStr + "." + dayStr+" lefts "+dayCount
                            +"\nSecret: " + secretText, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
    
                    //提交year,month,day, daysleft无所谓, 主界面展示的话复制上面的实现方法就行
                    //这里实现volley
                    final Date endDate = date;
                    //sign token as header
                    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                    final String token = Jwts.builder().setSubject("CySchedule").signWith(key).compact();

                    final RequestQueue requestQueue = Singleton.getInstance(root.getContext()).getRequestQueue();
                    //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.start();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EVENT,
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
                                        Log.e("TAG", status);
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
                                    Toast.makeText(root.getContext(), "Add Event Error: " + error.toString(), Toast.LENGTH_SHORT).show();

                                }
                            })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", "Days Counter");
                            params.put("startTime", curDate.toString());
                            params.put("endTime", endDate.toString());
                            params.put("location", "No Location");
                            params.put("description", secretText);
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
                }
            }
        });
        return root;
    }
}