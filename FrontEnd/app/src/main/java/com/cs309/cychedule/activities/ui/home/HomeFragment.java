package com.cs309.cychedule.activities.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.cs309.cychedule.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();

        RecyclerView eventsThisWeekRecyclerView = activity.findViewById(R.id.fragment_home_event_this_week_recyclerview);
        eventsThisWeekRecyclerView.setHasFixedSize(false);

        LinearLayoutManager eventThisWeeklayoutManager = new LinearLayoutManager(activity);
        eventsThisWeekRecyclerView.setLayoutManager(eventThisWeeklayoutManager);

        EventThisWeekRecyclerAdapter eventThisWeeekAdapter = new EventThisWeekRecyclerAdapter();
        eventsThisWeekRecyclerView.setAdapter(eventThisWeeekAdapter);


        RecyclerView alarmListRecyclerView = activity.findViewById(R.id.fragment_home_alarm_recyclerview);
        alarmListRecyclerView.setHasFixedSize(false);

        LinearLayoutManager alarmListlayoutManager = new LinearLayoutManager(activity);
        alarmListRecyclerView.setLayoutManager(alarmListlayoutManager);

        AlarmListRecyclerAdapter alarmListAdapter = new AlarmListRecyclerAdapter();
        alarmListRecyclerView.setAdapter(alarmListAdapter);


//        eventsThisWeekrecyclerView.setNestedScrollingEnabled(false);
    }
}