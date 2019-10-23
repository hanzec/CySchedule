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

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.cs309.cychedule.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private boolean isLoading = false;

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


//        ArrayList<HomeRecyclerAdapter.HomeData> homeData = generateEmptyHomeData();
        ArrayList<HomeRecyclerAdapter.HomeData> homeData = generateEmptyAlarmHomeData();
//        ArrayList<HomeRecyclerAdapter.HomeData> homeData = generateHomeData();

        renderHomeView(homeData, activity);
//        initScrollListener();
    }

    private void renderHomeView(ArrayList<HomeRecyclerAdapter.HomeData> homeData, final Activity activity) {

        ConstraintLayout emptyView = activity.findViewById(R.id.fragment_home_empty_container);
        NestedScrollView nonEmptyView = activity.findViewById(R.id.fragment_home_non_empty_container);

        if (homeData.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            nonEmptyView.setVisibility(View.GONE);
        } else {
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
        adapter.notifyItemInserted( homeData.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // remove null data
                int listSize =  homeData.size();
                homeData.remove(listSize - 1);
                adapter.notifyItemRemoved(listSize);
                int currentSize = listSize;
                int nextLimit = currentSize + 1;

                while (currentSize - 1 < nextLimit) {
                    currentSize++;
                    // add new alarm data
                    HomeRecyclerAdapter.Alarm alarm = new HomeRecyclerAdapter.Alarm(
                            currentSize+" Days Left", true, "Saturday Every Day");
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
        list.add(new HomeRecyclerAdapter.Event("12:03", "Canada", "test TTS"));
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
        for (int i = 1; i <3; i++) {
            HomeRecyclerAdapter.Alarm event = new HomeRecyclerAdapter.Alarm(i+" Days Left",true, i + " Final Exam");
            list.add(event);
        }
        return list;
    }
}