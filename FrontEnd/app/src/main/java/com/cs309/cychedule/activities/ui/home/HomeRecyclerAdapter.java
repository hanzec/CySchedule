package com.cs309.cychedule.activities.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.cs309.cychedule.R;
import com.cs309.cychedule.utilities.NestedRecyclerView;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeBasicViewHolder>{

    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_EVENT_THIS_WEEK = 1;
    private final int VIEW_TYPE_ALARM_HEADER = 2;
    private final int VIEW_TYPE_ALARM_ITEM = 3;
    private final int VIEW_TYPE_ALARM_LOADING = 4;

    private ArrayList<HomeData> mockHomeData;

    HomeRecyclerAdapter(ArrayList<HomeData> homeData) {
        this.mockHomeData = homeData;
    }

    @NonNull
    @Override
    public HomeBasicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        if (viewType == VIEW_TYPE_TODAY) {
            View listItem= layoutInflater.inflate(R.layout.item_view_fragment_home_today, viewGroup, false);
            return new HomeTodayViewHolder(listItem);
        } else if (viewType == VIEW_TYPE_EVENT_THIS_WEEK){
            View listItem= layoutInflater.inflate(R.layout.container_event_this_week, viewGroup, false);
            return new EventThisWeekViewHolder(listItem);
        } else if (viewType == VIEW_TYPE_ALARM_HEADER){
            View listItem= layoutInflater.inflate(R.layout.header_fragment_home_alarm_list, viewGroup, false);
            return new AlarmListHeaderViewHolder(listItem);
        } else if (viewType == VIEW_TYPE_ALARM_ITEM ){
            View listItem= layoutInflater.inflate(R.layout.item_view_fragment_home_alarm_list, viewGroup, false);
            return new AlarmListItemViewHolder(listItem);
        } else {
            View listItem= layoutInflater.inflate(R.layout.item_view_alarm_list_loading, viewGroup, false);
            return new AlaramListLoadingViewHolder(listItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBasicViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_TODAY) {
            Event event = (Event) mockHomeData.get(position);
            ((HomeTodayViewHolder)viewHolder).bindView(event);
        } else if (viewType == VIEW_TYPE_EVENT_THIS_WEEK){
            ((EventThisWeekViewHolder)viewHolder).bindView();
        } else if (viewType == VIEW_TYPE_ALARM_HEADER){

        } else if (viewType == VIEW_TYPE_ALARM_ITEM){
            Alarm alarm = (Alarm) mockHomeData.get(position);
            ((AlarmListItemViewHolder)viewHolder).bindView(alarm);
        }
    }

    @Override
    public int getItemCount() {
        return mockHomeData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TODAY;
        } else if (position == 1) {
            return VIEW_TYPE_EVENT_THIS_WEEK;
        } else if (position == 2) {
            return VIEW_TYPE_ALARM_HEADER;
        } else {
            if (mockHomeData.get(position) == null)
                return VIEW_TYPE_ALARM_LOADING;
            else
                return VIEW_TYPE_ALARM_ITEM;
        }
    }

    // data model
    static abstract class HomeData {
    }

    static class Event extends HomeData {
        String time;
        String location;
        String comment;

        Event(String time, String location, String comment) {
            this.time = time;
            this.location = location;
            this.comment = comment;
        }
    }

    static class Alarm extends HomeData {
        String time;
        boolean enableAlarm;
        String daysCounter;

        Alarm(String time, boolean enableAlarm, String daysCounter) {
            this.time = time;
            this.enableAlarm = enableAlarm;
            this.daysCounter = daysCounter;
        }
    }

//    private ArrayList<HomeData> generateHomeData() {
//        ArrayList<HomeData> list = new ArrayList<>();
//        // today
//        list.add(new Event("12:03", "Canada", "test TTS"));
//        // event this week
//        list.add(new Event("event this week", "event this week", "event this week"));
//        // alarm list header
//        list.add(new Event("alarm header", "alarm header", "alarm header"));
//        // alarm list item
//        for (int i = 1; i <5; i++) {
//            Alarm event = new Alarm("12:0"+ i, true, i + " Days");
//            list.add(event);
//        }
//        return list;
//    }

    // base viewHolder
    abstract class HomeBasicViewHolder extends RecyclerView.ViewHolder {

        int viewType;

        HomeBasicViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
        }

    }

    // today viewHolder
    class HomeTodayViewHolder extends HomeBasicViewHolder {
        private TextView timeValue;
        private TextView locationValue;
        private TextView commentsValue;


        HomeTodayViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_TODAY);
            timeValue = itemView.findViewById(R.id.fragment_home_today_time_value);
            locationValue = itemView.findViewById(R.id.fragment_home_today_location_value);
            commentsValue = itemView.findViewById(R.id.fragment_home_today_comment_value);
        }

        void bindView(Event event) {
            this.timeValue.setText(event.time);
            this.locationValue.setText(event.location);
            this.commentsValue.setText(event.comment);
        }
    }

    // eventThisWeek viewHolder
    class EventThisWeekViewHolder extends HomeBasicViewHolder {

        private NestedRecyclerView recyclerView;
        private LinearLayoutManager layoutManager;

        EventThisWeekViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_EVENT_THIS_WEEK);
            this.recyclerView = itemView.findViewById(R.id.fragment_home_event_this_week_recyclerview);
            layoutManager = new LinearLayoutManager(itemView.getContext());
        }

        void bindView() {
            if (recyclerView.getAdapter() == null) {
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                EventThisWeekRecyclerAdapter adapter = new EventThisWeekRecyclerAdapter();
                recyclerView.setAdapter(adapter);
            }
        }
    }


    // alarm list header viewHolder
    class AlarmListHeaderViewHolder extends HomeBasicViewHolder {

        AlarmListHeaderViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_ALARM_HEADER);
        }
    }

    // alarm list item viewHolder
    class AlarmListItemViewHolder extends HomeBasicViewHolder {

        private TextView time;
        private Switch enableAlarm;
        private TextView daysCounter;

        AlarmListItemViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_ALARM_ITEM);
            time = itemView.findViewById(R.id.fragment_home_alarm_list_time);
            enableAlarm = itemView.findViewById(R.id.fragment_home_alarm_list_switch);
            daysCounter = itemView.findViewById(R.id.fragment_home_alarm_list_days_counter);
        }

        void bindView(Alarm alarm) {
            this.time.setText(alarm.time);
            enableAlarm.setChecked(alarm.enableAlarm);
            this.daysCounter.setText(alarm.daysCounter);
        }

    }

    // alarm list loading
    class AlaramListLoadingViewHolder extends HomeBasicViewHolder {

        AlaramListLoadingViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_ALARM_LOADING);
        }
    }


}
