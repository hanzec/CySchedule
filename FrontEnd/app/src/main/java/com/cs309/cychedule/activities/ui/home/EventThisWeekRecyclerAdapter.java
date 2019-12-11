package com.cs309.cychedule.activities.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cs309.cychedule.R;
import com.cs309.cychedule.activities.SessionManager;
import com.cs309.cychedule.patterns.Singleton;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.ServerResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * This class is to show the events in current week with recycle view
 */
public class EventThisWeekRecyclerAdapter extends RecyclerView.Adapter<EventThisWeekRecyclerAdapter.EventThisWeekBasicViewHolder> {
	
	private ArrayList<STDevent> mockEvents;
	private final int VIEW_TYPE_ITEM = 1;
	private final int VIEW_TYPE_HEADER = 0;
	
	EventThisWeekRecyclerAdapter(ArrayList<STDevent> eventList) {
		if (eventList.size() == 0)
			this.mockEvents = generateMockEvents();
		else
			this.mockEvents = eventList;
	}
	
	private SessionManager sessionManager;
	private Context context;
	
	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		context = recyclerView.getContext();
		sessionManager = new SessionManager(context);
	}
	
	
	@NonNull
	@Override
	public EventThisWeekBasicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
		if (viewType == VIEW_TYPE_HEADER) {
			View listItem = layoutInflater.inflate(R.layout.header_fragment_home_event_this_week, viewGroup, false);
			return new EventThisWeekHeaderViewHolder(listItem);
		}
		else {
			View listItem = layoutInflater.inflate(R.layout.item_view_fragment_home_event_this_week, viewGroup, false);
			return new EventThisWeekItemViewHolder(listItem);
		}
	}
	
	@Override
	public void onBindViewHolder(@NonNull EventThisWeekBasicViewHolder viewHolder, int position) {
		STDevent event = mockEvents.get(position);
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
		
		abstract void bindView(STDevent event);
	}
	
	// header view
	class EventThisWeekHeaderViewHolder extends EventThisWeekBasicViewHolder {
		
		EventThisWeekHeaderViewHolder(View itemView) {
			super(itemView, VIEW_TYPE_HEADER);
		}
		
		@Override
		void bindView(STDevent event) {
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
		
		void bindView(STDevent event) {
			this.timeValue.setText(event.time);
			this.locationValue.setText(event.location);
			this.commentsValue.setText(event.comment);
		}
	}
	
	// Event data model
	
	
	// generate fake data, the first position is the place holder for the header
	public ArrayList<STDevent> generateMockEvents() {
		String URL_GETALL = "https://dev.hanzec.com/api/v1/event/all";
		// ArrayList<Event> mockEvents = new ArrayList<>();
		// mockEvents.add(new Event("header", "header", "header"));
		// mockEvents.add(new Event("12:03", "Canada", "test TTS"));
		// mockEvents.add(new Event("event this week", "event this week", "event this week"));
		// mockEvents.add(new Event("alarm header", "alarm header", "alarm header"));
		this.mockEvents = new ArrayList<>();
		mockEvents.add(new STDevent("Header is not shown", "this one is today", "will not be in the week list"));
		mockEvents.add(new STDevent("...", "...", "Loading"));
		Log.e("Adpter.mockEvent", "ARRAYLIST: " + mockEvents.toString());
		return mockEvents;
	}
	
	private String generateToken(String requestUrl, String tokenID, String password) {
		Key key = Keys.hmacShaKeyFor(password.getBytes());
		return Jwts.builder()
				.signWith(key)
				.setId(tokenID)
				.setIssuer("CySchedule")
				.claim("requestUrl", requestUrl)
				.setExpiration(new Date(System.currentTimeMillis() + 20000))
				.compact();
	}
}
