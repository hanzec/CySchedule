package com.cs309.cychedule.activities.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cs309.cychedule.R;
import com.cs309.cychedule.utilities.NestedRecyclerView;

import java.util.ArrayList;

/**
 * Another recycle view for the home page
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeBasicViewHolder> {

    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_EVENT_THIS_WEEK = 1;
    private final int VIEW_TYPE_ALARM_HEADER = 2;
    private final int VIEW_TYPE_ALARM_ITEM = 3;
    private final int VIEW_TYPE_ALARM_LOADING = 4;
    private final int VIEW_TYPE_ALARM_EMPTY = 5;


    private ArrayList<HomeData> mockHomeData;

    private OnEmptyViewClickListener mListener;


    HomeRecyclerAdapter(ArrayList<HomeData> homeData) {
        this.mockHomeData = homeData;
        // no alarm items
        if (mockHomeData.size() == 3)
            mockHomeData.add(new Alarm("empty", true, "empty"));
    }

    interface OnEmptyViewClickListener {
        void onEmptyViewClick();
    }

    @NonNull
    @Override
    public HomeBasicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        if (viewType == VIEW_TYPE_TODAY) {
            View listItem = layoutInflater.inflate(R.layout.item_view_fragment_home_today, viewGroup, false);
            return new HomeTodayViewHolder(listItem);
        } else if (viewType == VIEW_TYPE_EVENT_THIS_WEEK) {
            View listItem = layoutInflater.inflate(R.layout.container_event_this_week, viewGroup, false);
            return new EventThisWeekViewHolder(listItem);
        } else if (viewType == VIEW_TYPE_ALARM_HEADER) {
            View listItem = layoutInflater.inflate(R.layout.header_fragment_home_alarm_list, viewGroup, false);
            return new AlarmListHeaderViewHolder(listItem);
        } else if (viewType == VIEW_TYPE_ALARM_ITEM) {
            View listItem = layoutInflater.inflate(R.layout.item_view_fragment_home_alarm_list, viewGroup, false);
            return new AlarmListItemViewHolder(listItem);
        } else if (viewType == VIEW_TYPE_ALARM_LOADING){
            View listItem = layoutInflater.inflate(R.layout.item_view_alarm_list_loading, viewGroup, false);
            return new AlarmListLoadingViewHolder(listItem);
        } else {
            View listItem = layoutInflater.inflate(R.layout.item_view_fragment_home_alarm_list_empty, viewGroup, false);
            return new AlarmListEmptyViewHolder(listItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBasicViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_TODAY) {
            Event event = (Event) mockHomeData.get(position);
            ((HomeTodayViewHolder) viewHolder).bindView(event);
        } else if (viewType == VIEW_TYPE_EVENT_THIS_WEEK) {
            ((EventThisWeekViewHolder) viewHolder).bindView();
        } else if (viewType == VIEW_TYPE_ALARM_HEADER) {

        } else if (viewType == VIEW_TYPE_ALARM_ITEM) {
            Alarm alarm = (Alarm) mockHomeData.get(position);
            AlarmListItemViewHolder holder = (AlarmListItemViewHolder) viewHolder;
            holder.bindView(alarm, position);
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
        } else if (position == 3 && ((Alarm) mockHomeData.get(position)).daysCounter.equals("empty")) {
            return VIEW_TYPE_ALARM_EMPTY;
        }
        else {
            if (mockHomeData.get(position) == null)
                return VIEW_TYPE_ALARM_LOADING;
            else
                return VIEW_TYPE_ALARM_ITEM;
        }
    }

    ArrayList<HomeData> getMockHomeData() {
        return mockHomeData;
    }

    public void setOnEmptyViewClickListener(OnEmptyViewClickListener listener) {
        this.mListener = listener;
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
        String daysCounter;
        boolean enableAlarm;
        String note;

        Alarm(String daysCounter, boolean enableAlarm, String note) {
            this.daysCounter = daysCounter;
            this.enableAlarm = enableAlarm;
            this.note = note;
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

        private TextView daysCounter;
        private Switch enableAlarm;
        private TextView note;

        AlarmListItemViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_ALARM_ITEM);
            daysCounter = itemView.findViewById(R.id.fragment_home_alarm_list_days_counter);
            enableAlarm = itemView.findViewById(R.id.fragment_home_alarm_list_switch);
            note = itemView.findViewById(R.id.fragment_home_alarm_list_note);
        }

        void bindView(Alarm alarm, final int position) {
            this.daysCounter.setText(alarm.daysCounter);
            enableAlarm.setChecked(alarm.enableAlarm);
            this.note.setText(alarm.note);

            enableAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean enableAlarm) {
                    // save the checked change to data list
                    ((Alarm)mockHomeData.get(position)).enableAlarm = enableAlarm;
                }
            });
        }

    }

    // alarm list loading
    class AlarmListLoadingViewHolder extends HomeBasicViewHolder {

        AlarmListLoadingViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_ALARM_LOADING);
        }
    }

    // alarm list loading
    class AlarmListEmptyViewHolder extends HomeBasicViewHolder {
        private TextView emptyMsg;
        AlarmListEmptyViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_ALARM_EMPTY);
            this.emptyMsg = itemView.findViewById(R.id.fragment_home_alarm_list_empty_msg);
            emptyMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onEmptyViewClick();
                }
            });
        }
    }

}
