package com.cs309.cychedule.activities.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.ScrollView;
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
import com.cs309.cychedule.activities.LoginActivity;
import com.cs309.cychedule.activities.SessionManager;
import com.cs309.cychedule.patterns.Singleton;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.ServerResponse;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


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
 * HomeFragment is the home page of our app
 * It has the main functions and the ways to them
 */
public class HomeFragment extends Fragment {

	SessionManager sessionManager;
	private static String URL_GETALL = "https://dev.hanzec.com/api/v1/event/all";

	private HomeViewModel homeViewModel;
	private RecyclerView recyclerView;
	private boolean isLoading = false;

	private static String events;

	private HomeRecyclerAdapter adapter;

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
		sessionManager = new SessionManager(getContext());
		sessionManager.checkLogin();
		return root;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		init();
	}

	private void init() {
		final Activity activity = getActivity();
		recyclerView = activity.findViewById(R.id.fragment_home_recyclerview);
		LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setNestedScrollingEnabled(true);

/**改逻辑看这里!*/
		//空页面, 什么数据都没的情况
		// ArrayList<HomeRecyclerAdapter.HomeData> homeData = generateEmptyHomeData();
		//空list
		ArrayList<HomeRecyclerAdapter.HomeData> homeData = generateEmptyAlarmHomeData();
		//塞数据
		// ArrayList<HomeRecyclerAdapter.HomeData> homeData = generateHomeData();
		//服务器数据
		// ArrayList<HomeRecyclerAdapter.HomeData> homeData = getServerList();
		renderHomeView(homeData, activity);
		//如果你想关闭下拉刷新的话
		// initScrollListener();


	// 	//refresh layout
	// 	RefreshLayout refreshLayout = activity.findViewById(R.id.refreshLayout);
	// 	refreshLayout.setOnRefreshListener(new OnRefreshListener() {
	// 		@Override
	// 		public void onRefresh(RefreshLayout refreshlayout) {
	// 			final RequestQueue requestQueue = Singleton.getInstance(getContext()).getRequestQueue();
	// 			//RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
	// 			requestQueue.start();
	//
	// 			StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GETALL,
	// 					new Response.Listener<String>() {
	// 						@Override
	// 						public void onResponse(String response) {
	// 							try
	// 							{
	// 								events = response;
	// 								Toast.makeText(getContext(), "Received Events: " + events, Toast.LENGTH_LONG).show();
	// 							}
	// 							catch (Exception e)
	// 							{
	// 								e.printStackTrace();
	// 								Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
	// 							}
	// 						}
	// 					},
	// 					new Response.ErrorListener() {
	// 						@Override
	// 						public void onErrorResponse(VolleyError error) {
	// 							Toast.makeText(getContext(), "Get Events Error: " + error.toString(), Toast.LENGTH_SHORT).show();
	// 						}
	// 					})
	// 			{
	// 				@Override
	// 				public Map<String, String> getHeaders() throws AuthFailureError {
	// 					Map<String, String> requestHeader = new HashMap<String, String>();
	// 					if(sessionManager.getLoginToken().get("tokenID") != null)
	// 						requestHeader.put("Authorization", generateToken(
	// 								"I don't know",
	// 								sessionManager.getLoginToken().get("tokenID"),
	// 								sessionManager.getLoginToken().get("secret")));
	// 					return requestHeader;
	// 				}
	// 			};
	// 			//requestQueue.add(stringRequest);
	// 			Singleton.getInstance(getContext()).addToRequestQueue(stringRequest);
	// 			refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
	// 		}
	// 	});
	// 	refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
	// 		@Override
	// 		public void onLoadMore(RefreshLayout refreshlayout) {
	// 			refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
	// 		}
	// 	});
	}

	private String generateToken(String requestUrl, String tokenID, String password){
		Key key = Keys.hmacShaKeyFor(password.getBytes());
		return Jwts.builder()
				.signWith(key)
				.setId(tokenID)
				.setIssuer("CySchedule")
				.claim("requestUrl",requestUrl)
				.setExpiration(new Date(System.currentTimeMillis() + 20000))
				.compact();
	}

	private void renderHomeView(ArrayList<HomeRecyclerAdapter.HomeData> homeData, final Activity activity) {

		ConstraintLayout emptyView = activity.findViewById(R.id.fragment_home_empty_container);
		NestedScrollView nonEmptyView = activity.findViewById(R.id.fragment_home_non_empty_container);

		if (homeData.size() == 0) {
			emptyView.setVisibility(View.VISIBLE);
			nonEmptyView.setVisibility(View.GONE);
		}
		else {
			emptyView.setVisibility(View.GONE);
			nonEmptyView.setVisibility(View.VISIBLE);

			adapter = new HomeRecyclerAdapter(homeData);

			adapter.setOnEmptyViewClickListener(new HomeRecyclerAdapter.OnEmptyViewClickListener() {
				@Override
				public void onEmptyViewClick() {
					NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
					navController.navigate(R.id.nav_daysCounter);
				}
			});

			recyclerView.setAdapter(adapter);
		}
	}

	/**
	 * When the list is scrolled to the end, load more items into the list
	 * Ref: https://www.journaldev.com/24041/android-recyclerview-load-more-endless-scrolling
	 */

	private void initScrollListener() {

		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

				if (!isLoading) {
					ArrayList<HomeRecyclerAdapter.HomeData> homeData = adapter.getMockHomeData();
					if (linearLayoutManager != null &&
							// //bottom of list
							linearLayoutManager.findLastVisibleItemPosition() == homeData.size() - 1) {
						loadMoreAlarms(homeData);
						isLoading = true;
					}
				}
			}
		});


	}

	private void loadMoreAlarms(final ArrayList<HomeRecyclerAdapter.HomeData> homeData) {
		// add null for loading view
		homeData.add(null);
		adapter.notifyItemInserted(homeData.size() - 1);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				// remove null data
				int listSize = homeData.size();
				homeData.remove(listSize - 1);
				adapter.notifyItemRemoved(listSize);
				int currentSize = listSize;
				int nextLimit = currentSize + 1;

				while (currentSize - 1 < nextLimit) {
					currentSize++;
					// add new alarm data
					HomeRecyclerAdapter.Alarm alarm = new HomeRecyclerAdapter.Alarm(
							currentSize + " Days Left", true, "Saturday Every Day");
					homeData.add(alarm);
				}
				adapter = new HomeRecyclerAdapter(homeData);
				recyclerView.setAdapter(adapter);
				// this line would mess up other switches' status
//                adapter.notifyDataSetChanged();
				isLoading = false;
			}
		}, 2000);

	}


	private ArrayList<HomeRecyclerAdapter.HomeData> generateEmptyHomeData() {
		return new ArrayList<>();
	}

	private ArrayList<HomeRecyclerAdapter.HomeData> generateEmptyAlarmHomeData() {
		ArrayList<HomeRecyclerAdapter.HomeData> list = new ArrayList<>();
		// today
		list.add(new HomeRecyclerAdapter.Event("21:55", "AMES", "test TTS"));
		// event this week
		list.add(new HomeRecyclerAdapter.Event("event this week", "event this week", "event this week"));
		// alarm list header
		list.add(new HomeRecyclerAdapter.Event("alarm header", "alarm header", "alarm header"));
		return list;
	}

	private ArrayList<HomeRecyclerAdapter.HomeData> generateHomeData() {
		ArrayList<HomeRecyclerAdapter.HomeData> list = new ArrayList<>();
		// today
		list.add(new HomeRecyclerAdapter.Event("12:03", "Canada", "test TTS"));
		// event this week
		list.add(new HomeRecyclerAdapter.Event("event this week", "event this week", "event this week"));
		// alarm list header
		list.add(new HomeRecyclerAdapter.Event("alarm header", "alarm header", "alarm header"));
		// alarm list item
		for (int i = 1; i < 3; i++) {
			HomeRecyclerAdapter.Alarm event = new HomeRecyclerAdapter.Alarm(i + " Days Left", true, i + " Final Exam");
			list.add(event);
		}
		return list;
	}
	
	// ArrayList<HomeRecyclerAdapter.HomeData> list = new ArrayList<>();
	// public ArrayList<HomeRecyclerAdapter.HomeData> getServerList() {
	// 	list = new ArrayList<>();
	// 	StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GETALL,
	// 			new Response.Listener<String>() {
	// 				@Override
	// 				public void onResponse(String response) {
	// 					try {
	// 						Gson gson = new Gson();
	// 						ServerResponse serverResponse = gson.fromJson(response, ServerResponse.class);
	// 						Map<String, Object> events = serverResponse.getResponseBody();
	//
	// 						for(Map.Entry<String,Object> entry : events.entrySet()){
	// 							if(entry instanceof Map) {
	// 								Map<String, Objects> event = (Map<String, Objects>) entry.getValue();
	// 								list.add(new HomeRecyclerAdapter.Event(
	// 										gson.fromJson(event.get("endTime").toString(),
	// 												Calendar.class).toString(),
	// 										Objects.requireNonNull(event.get("location")).toString(),
	// 										Objects.requireNonNull(event.get("description")).toString()));
	// 							}
	// 						}
	//
	// 					}
	// 					catch (Exception e)
	// 					{
	// 						e.printStackTrace();
	// 						Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
	// 					}
	// 				}
	// 			},
	// 			new Response.ErrorListener() {
	// 				@Override
	// 				public void onErrorResponse(VolleyError error) {
	// 					Toast.makeText(getContext(), "Get Events Error: " + error.toString(), Toast.LENGTH_SHORT).show();
	// 				}
	// 			})
	// 	{
	// 		@Override
	// 		public Map<String, String> getHeaders() throws AuthFailureError {
	// 			Map<String, String> requestHeader = new HashMap<String, String>();
	// 			if(sessionManager.getLoginToken().get("tokenID") != null)
	// 				requestHeader.put("Authorization", generateToken(
	// 						"I don't know",
	// 						sessionManager.getLoginToken().get("tokenID"),
	// 						sessionManager.getLoginToken().get("secret")));
	// 			return requestHeader;
	// 		}
	// 	};
	// 	//requestQueue.add(stringRequest);
	// 	Singleton.getInstance(getContext()).addToRequestQueue(stringRequest);
	// 	list.add(new HomeRecyclerAdapter.Event("12:03", "Canada", "test TTS"));
	// 	list.add(new HomeRecyclerAdapter.Event("event this week", "event this week", "event this week"));
	// 	list.add(new HomeRecyclerAdapter.Event("alarm header", "alarm header", "alarm header"));
	// 	for (int i = 1; i < 3; i++) {
	// 		HomeRecyclerAdapter.Alarm event = new HomeRecyclerAdapter.Alarm(i + " Days Left", true, i + " Final Exam");
	// 		list.add(event);
	// 	}
	//
	// 	return list;
	// }
	//
	// ArrayList<EventThisWeekRecyclerAdapter.Event> list = new ArrayList<>();
	// public ArrayList<EventThisWeekRecyclerAdapter.Event> getServerList() {
	// 	this.list = new ArrayList<>();
	// 	StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GETALL,
	// 			new Response.Listener<String>() {
	// 				@Override
	// 				public void onResponse(String response) {
	// 					try {
	// 						Gson gson = new Gson();
	// 						ServerResponse serverResponse = gson.fromJson(response, ServerResponse.class);
	// 						Map<String, Object> events = serverResponse.getResponseBody();
	//
	// 						for(Map.Entry<String,Object> entry : events.entrySet()){
	// 							if(entry instanceof Map) {
	// 								Map<String, Objects> event = (Map<String, Objects>) entry.getValue();
	// 								list.add(new EventThisWeekRecyclerAdapter.Event(
	// 										gson.fromJson(event.get("endTime").toString(),
	// 										Objects.requireNonNull(event.get("location")).toString(),
	// 										Objects.requireNonNull(event.get("description")).toString()));
	// 							}
	// 						}
	//
	// 					}
	// 					catch (Exception e)
	// 					{
	// 						e.printStackTrace();
	// 						Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
	// 					}
	// 				}
	// 			},
	// 			new Response.ErrorListener() {
	// 				@Override
	// 				public void onErrorResponse(VolleyError error) {
	// 					Toast.makeText(getContext(), "Get Events Error: " + error.toString(), Toast.LENGTH_SHORT).show();
	// 				}
	// 			})
	// 	{
	// 		@Override
	// 		public Map<String, String> getHeaders() throws AuthFailureError {
	// 			Map<String, String> requestHeader = new HashMap<String, String>();
	// 			if(sessionManager.getLoginToken().get("tokenID") != null)
	// 				requestHeader.put("Authorization", generateToken(
	// 						"I don't know",
	// 						sessionManager.getLoginToken().get("tokenID"),
	// 						sessionManager.getLoginToken().get("secret")));
	// 			return requestHeader;
	// 		}
	// 	};
	// 	//requestQueue.add(stringRequest);
	// 	Singleton.getInstance(getContext()).addToRequestQueue(stringRequest);
	// 	list.add(new EventThisWeekRecyclerAdapter.Event("12:03", "Canada", "test TTS"));
	// 	list.add(new EventThisWeekRecyclerAdapter.Event("event this week", "event this week", "event this week"));
	// 	list.add(new EventThisWeekRecyclerAdapter.Event("alarm header", "alarm header", "alarm header"));
	//
	// 	return list;
	// }
	

	public static void getEvents(String s) {
		events = s;
	}
}