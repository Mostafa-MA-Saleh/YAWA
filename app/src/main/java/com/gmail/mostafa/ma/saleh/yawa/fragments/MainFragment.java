package com.gmail.mostafa.ma.saleh.yawa.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import com.gmail.mostafa.ma.saleh.yawa.utilities.StringUtils;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment {

    @BindView(R.id.days_rv)
    RecyclerView rvDays;
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

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        daysRecyclerAdapter = new DaysRecyclerAdapter(getContext());
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
                Utils.replaceFragment(getFragmentManager(), DetailsFragment.newInstance(day, c.getTime()), true);
            }
        });
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
                tvTempMax.setText(StringUtils.format("%d°", Math.round(days[0].temp.max)));
                tvTempMin.setText(StringUtils.format("%d°", Math.round(days[0].temp.min)));
                tvDescription.setText(days[0].weather[0].main);
                imgWeatherIcon.setImageResource(Utils.getArtResourceForWeatherCondition(days[0].weather[0].id));
                for (int i = 1; i < days.length; i++)
                    daysRecyclerAdapter.add(days[i]);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String message) {
                Utils.replaceFragment(getFragmentManager(), NoInternetFragment.newInstance(), false);
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
                Utils.replaceFragment(getFragmentManager(), SettingsFragment.newInstance(), true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
