package com.gmail.mostafa.ma.saleh.yawa.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.models.Day;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment {

    private static final String ARG_DAY = "param_day";
    private static final String ARG_DATE = "param_date";

    @BindView(R.id.tv_temp_max)
    TextView tvTempMax;
    @BindView(R.id.tv_temp_min)
    TextView tvTempMin;
    @BindView(R.id.tv_weather_status)
    TextView tvStatus;
    @BindView(R.id.tv_humidity)
    TextView tvHumidity;
    @BindView(R.id.tv_wind_speed)
    TextView tvWindSpeed;
    @BindView(R.id.tv_wind_direction)
    TextView tvWindDirection;
    @BindView(R.id.tv_pressure)
    TextView tvPressure;
    @BindView(R.id.img_weather)
    ImageView imgWeatherStatus;
    @BindView(R.id.aprg_clouds)
    ArcProgress aprgClouds;

    private Day mDay;
    private Date mDate;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Day day, Date date) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DAY, day);
        args.putSerializable(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDay = (Day) getArguments().getSerializable(ARG_DAY);
            mDate = (Date) getArguments().getSerializable(ARG_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, mainView);
        getActivity().setTitle(new SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault()).format(mDate));
        tvTempMax.setText(String.format(Locale.getDefault(), "%d°", Math.round(mDay.temp.max)));
        tvTempMin.setText(String.format(Locale.getDefault(), "%d°", Math.round(mDay.temp.min)));
        tvStatus.setText(mDay.weather[0].main);
        tvHumidity.setText(mDay.humidity == 0.0 ? "N/A" : String.format(Locale.getDefault(), "%d%%", Math.round(mDay.humidity)));
        imgWeatherStatus.setImageResource(Utils.getArtResourceForWeatherCondition(mDay.weather[0].id));
        tvPressure.setText(String.format(Locale.getDefault(), "%d %s", Math.round(mDay.pressure), getString(R.string.pressure_unit)));
        tvWindSpeed.setText(String.format(Locale.getDefault(), "%d %s", Math.round(mDay.speed), getWindSpeedUnit()));
        tvWindDirection.setText(getDirection(mDay.deg));
        aprgClouds.setProgress(Math.round(mDay.clouds));
        return mainView;
    }

    private String getDirection(float angle) {
        int val = (int) ((angle / 22.5) + 0.5);
        return getResources().getStringArray(R.array.directions)[val % 16];
    }

    private String getWindSpeedUnit() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (preferences.getString("temp_unit", "&units=metric").contains("imperial")) {
            return getString(R.string.wind_speed_unit_imperial);
        }
        return getString(R.string.wind_speed_unit_metric);
    }
}
