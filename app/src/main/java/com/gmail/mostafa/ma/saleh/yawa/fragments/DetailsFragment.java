package com.gmail.mostafa.ma.saleh.yawa.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.models.Day;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants;
import com.gmail.mostafa.ma.saleh.yawa.utilities.SharedPreferencesManager;
import com.gmail.mostafa.ma.saleh.yawa.utilities.StringUtils;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment {

    private static final String PARAM_DAY = "param_day";
    private static final String PARAM_DATE = "param_date";

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

    public static DetailsFragment newInstance(Day day, Date date) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_DAY, day);
        args.putSerializable(PARAM_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDay = (Day) getArguments().getSerializable(PARAM_DAY);
            mDate = (Date) getArguments().getSerializable(PARAM_DATE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            getActivity().setTitle(StringUtils.formatDate(Constants.LONG_DATE_PATTERN, mDate));
            tvTempMax.setText(StringUtils.format("%d°", Math.round(mDay.temp.max)));
            tvTempMin.setText(StringUtils.format("%d°", Math.round(mDay.temp.min)));
            tvStatus.setText(mDay.weather[0].main);
            tvHumidity.setText(mDay.humidity == 0.0 ? "N/A" : StringUtils.format("%d%%", Math.round(mDay.humidity)));
            imgWeatherStatus.setImageResource(Utils.getArtResourceForWeatherCondition(mDay.weather[0].id));
            tvPressure.setText(StringUtils.format("%d %s", Math.round(mDay.pressure), getString(R.string.pressure_unit)));
            tvWindSpeed.setText(StringUtils.format("%d %s", Math.round(mDay.speed), getWindSpeedUnit()));
            tvWindDirection.setText(getDirection(mDay.deg));
            aprgClouds.setProgress(Math.round(mDay.clouds));
        }
    }

    private String getDirection(float angle) {
        int val = (int) ((angle / 22.5) + 0.5);
        return getResources().getStringArray(R.array.directions)[val % 16];
    }

    private String getWindSpeedUnit() {
        SharedPreferencesManager preferencesManager = SharedPreferencesManager.getInstance();
        String tempUnits = preferencesManager.getString(Constants.KEY_TEMP_UNITS, "metric");
        if (tempUnits.contains("imperial")) {
            return getString(R.string.wind_speed_unit_imperial);
        } else {
            return getString(R.string.wind_speed_unit_metric);
        }
    }
}
