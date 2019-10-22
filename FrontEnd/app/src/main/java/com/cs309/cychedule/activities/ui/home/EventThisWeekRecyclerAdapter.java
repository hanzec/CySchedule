package com.cs309.cychedule.activities.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs309.cychedule.R;

import java.util.ArrayList;

public class EventThisWeekRecyclerAdapter extends RecyclerView.Adapter<EventThisWeekRecyclerAdapter.EventThisWeekBasicViewHolder> {

    private ArrayList<Event> mockEvents;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_HEADER = 0;

    EventThisWeekRecyclerAdapter() {
        this.mockEvents = generateMockEvents();
    }

    @NonNull
    @Override
    public EventThisWeekBasicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        if (viewType == VIEW_TYPE_HEADER) {
            View listItem = layoutInflater.inflate(R.layout.header_fragment_home_event_this_week, viewGroup, false);
            return new EventThisWeekHeaderViewHolder(listItem);
        } else {
            View listItem = layoutInflater.inflate(R.layout.item_view_fragment_home_event_this_week, viewGroup, false);
            return new EventThisWeekItemViewHolder(listItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull EventThisWeekBasicViewHolder viewHolder, int position) {
        Event event = mockEvents.get(position);
        viewHolder.bindView(event);
    }

    @Override
    public int getItemCount() {
        return mockEvents.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }


    abstract class EventThisWeekBasicViewHolder extends RecyclerView.ViewHolder {
        int viewType;

        EventThisWeekBasicViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
        }

        abstract void bindView(Event event);
    }

    // header view
    class EventThisWeekHeaderViewHolder extends EventThisWeekBasicViewHolder {

        EventThisWeekHeaderViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_HEADER);
        }

        @Override
        void bindView(Event event) {
        }
    }

    // item view
    class EventThisWeekItemViewHolder extends EventThisWeekBasicViewHolder {
        private TextView timeValue;
        private TextView locationValue;
        private TextView commentsValue;

        EventThisWeekItemViewHolder(View itemView) {
            super(itemView, VIEW_TYPE_ITEM);
            timeValue = itemView.findViewById(R.id.fragment_home_event_this_week_time_value);
            locationValue = itemView.findViewById(R.id.fragment_home_event_this_week_location_value);
            commentsValue = itemView.findViewById(R.id.fragment_home_event_this_week_comment_value);
        }

        void bindView(Event event) {
            this.timeValue.setText(event.time);
            this.locationValue.setText(event.location);
            this.commentsValue.setText(event.comment);
        }
    }

    // Event data model
    class Event {
        String time;
        String location;
        String comment;

        Event(String time, String location, String comment) {
            this.time = time;
            this.location = location;
            this.comment = comment;
        }
    }

    // generate fake data, the first position is the place holder for the header
    private ArrayList<Event> generateMockEvents() {
        ArrayList<Event> mockEvents = new ArrayList<>();
        mockEvents.add(new Event("header", "header", "header"));
        for (int i = 1; i < 10; i++) {
            Event event = new Event("12:" + i, "Canada", "test");
            mockEvents.add(event);
        }
        return mockEvents;
    }
}
