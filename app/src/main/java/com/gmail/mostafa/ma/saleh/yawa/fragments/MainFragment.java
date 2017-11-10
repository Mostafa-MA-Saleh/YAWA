package com.gmail.mostafa.ma.saleh.yawa.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.adapters.DaysRecyclerAdapter;
import com.gmail.mostafa.ma.saleh.yawa.models.Day;
import com.gmail.mostafa.ma.saleh.yawa.retrofit.OnFinishedListener;
import com.gmail.mostafa.ma.saleh.yawa.retrofit.RetrofitManager;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment {

    @BindView(R.id.days_rv)
    RecyclerView rvDays;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_temp_max)
    TextView tvTempMax;
    @BindView(R.id.tv_temp_min)
    TextView tvTempMin;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.img_weather_icon)
    ImageView imgWeatherIcon;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private DaysRecyclerAdapter daysRecyclerAdapter;
    private OnFinishedListener<Day[]> onFinishedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, mainView);
        daysRecyclerAdapter = new DaysRecyclerAdapter(getContext(), null);
        rvDays.setAdapter(daysRecyclerAdapter);
        rvDays.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh();
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        daysRecyclerAdapter.setOnItemClickListener(new DaysRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Day day, int position) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, position + 1);
                getFragmentManager()
                        .beginTransaction()
                        .add(android.R.id.content, DetailsFragment.newInstance(day, c.getTime()))
                        .addToBackStack(null)
                        .commit();
            }
        });
        return mainView;
    }

    private void refresh() {
        daysRecyclerAdapter.clear();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String cityId = preferences.getString("city", "524901");
        String tempUnit = preferences.getString("temp_unit", "metric");
        if (onFinishedListener != null) {
            onFinishedListener.cancel();
        }
        onFinishedListener = new OnFinishedListener<Day[]>() {
            @Override
            public void onSuccess(Day[] days) {
                tvDate.setText(new SimpleDateFormat("'Today,' MMMM dd", Locale.getDefault()).format(new Date()));
                tvTempMax.setText(String.format(Locale.getDefault(), "%d°", Math.round(days[0].temp.max)));
                tvTempMin.setText(String.format(Locale.getDefault(), "%d°", Math.round(days[0].temp.min)));
                tvDescription.setText(days[0].weather[0].main);
                imgWeatherIcon.setImageResource(Utils.getArtResourceForWeatherCondition(days[0].weather[0].id));
                for (int i = 1; i < days.length; i++)
                    daysRecyclerAdapter.add(days[i]);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String message) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, new NoInternetFragment())
                        .commit();
            }

            @Override
            public void onComplete(@Nullable Day[] args, @Nullable String message) {
                super.onComplete(args, message);
                onFinishedListener = null;
            }
        };
        RetrofitManager.getInstance().getForecast(cityId, tempUnit, onFinishedListener);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                getFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, new SettingsFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
