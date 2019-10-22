package com.cs309.cychedule.activities.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.cs309.cychedule.R;

import java.util.ArrayList;

public class AlarmListRecyclerAdapter extends RecyclerView.Adapter<AlarmListRecyclerAdapter.AlarmListWeekBasicViewHolder> {

    private ArrayList<Alarm> mockAlarms;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_HEADER = 0;

    AlarmListRecyclerAdapter() {
        this.mockAlarms = generateMockAlarms();
    }

    @NonNull
    @Override
    public AlarmListWeekBasicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        if (viewType == VIEW_TYPE_HEADER) {
            View listItem= layoutInflater.inflate(R.layout.header_fragment_home_alarm_list, viewGroup, false);
            return new AlarmListWeekHeaderViewHolder(listItem);
        } else {
            View listItem= layoutInflater.inflate(R.layout.item_view_fragment_home_alarm_list, viewGroup, false);
            return new AlarmListWeekItemViewHolder(listItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmListWeekBasicViewHolder viewHolder, int position) {
        Alarm event = mockAlarms.get(position);
        viewHolder.bindView(event);
    }

    @Override
    public int getItemCount() {
        return mockAlarms.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }


    abstract class AlarmListWeekBasicViewHolder extends RecyclerView.ViewHolder {
        int viewType;

        AlarmListWeekBasicViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
        }

        abstract void bindView(Alarm alarm);
    }

    // header view
    class AlarmListWeekHeaderViewHolder extends AlarmListWeekBasicViewHolder {

        AlarmListWeekHeaderViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_HEADER);
        }

        @Override
        void bindView(Alarm alarm) {
        }
    }

    // item view
    class AlarmListWeekItemViewHolder extends AlarmListWeekBasicViewHolder {
        private TextView time;
        private Switch enableAlarm;
        private TextView daysCounter;

        AlarmListWeekItemViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_ITEM);
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

    // Event data model
    class Alarm {
        String time;
        boolean enableAlarm;
        String daysCounter;

        Alarm(String time, boolean enableAlarm, String daysCounter) {
            this.time = time;
            this.enableAlarm = enableAlarm;
            this.daysCounter = daysCounter;
        }
    }

    // generate fake data, the first position is the place holder for the header
    private ArrayList<Alarm> generateMockAlarms() {
        ArrayList<Alarm> mockAlarms = new ArrayList<>();
        mockAlarms.add(new Alarm("header", false, "header"));
        for (int i = 1; i <10; i++) {
            Alarm event = new Alarm("12:"+ i, true, i + " Days");
            mockAlarms.add(event);
        }
        return mockAlarms;
    }
}
