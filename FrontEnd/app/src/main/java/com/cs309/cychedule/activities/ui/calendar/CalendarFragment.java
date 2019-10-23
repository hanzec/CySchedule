package com.cs309.cychedule.activities.ui.calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cs309.cychedule.R;

import java.util.Calendar;
import java.util.Objects;

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
								String temp = String.valueOf(year) + "." + String.valueOf(monthOfYear + 1) + "." + Integer.toString(dayOfMonth);
								endDateInput.setText(temp);
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
								String temp = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
								endTimeInput.setText(temp);
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
		
		
		btnRemvoe = root.findViewById(R.id.btn_cal_clear);
		btnRemvoe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				eventInput.setText("");
				locationInput.setText("");
			}
		});
		
		
		btnAdd = root.findViewById(R.id.btn_cal_add);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				eventText = eventInput.getText().toString();
				locationText = locationInput.getText().toString();
				if (eventText.isEmpty()) {
					Toast emptyInputWarning = Toast.makeText(root.getContext(), "Please enter the even description!", Toast.LENGTH_SHORT);
					emptyInputWarning.show();
				}
				else {
					if (locationText.isEmpty()) {
						locationInput.setText("Anywhere");
					}
					locationText = locationInput.getText().toString();
					Snackbar.make(root,  eventText
							+"From"+startDateStr+" "+ startTimeStr
							+"to" + endDateStr+" "+endTimeStr
							+ " @" + locationText
							+"\nEvent: " + eventText
							, Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
				}
				//这里实现volley
			}
		});
		return root;
	}
}