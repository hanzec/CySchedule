package com.cs309.cychedule.activities.ui.dayscounter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class DaysCounterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DaysCounterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}