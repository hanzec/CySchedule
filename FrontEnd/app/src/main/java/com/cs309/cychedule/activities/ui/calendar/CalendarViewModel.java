package com.cs309.cychedule.activities.ui.calendar;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class CalendarViewModel extends ViewModel {
	
	private MutableLiveData<String> mText;
	
	public CalendarViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("This is gallery fragment");
	}
	
	public LiveData<String> getText() {
		return mText;
	}
}