package com.cs309.cychedule.activities.ui.alarm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class AlarmViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AlarmViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}