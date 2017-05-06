package com.gmail.mostafa.ma.saleh.yawa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.models.Day;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment {

    private static final String ARG_DAY = "param_day";
    private static final String ARG_POSITION = "param_position";

    @BindView(R.id.tv_description)
    TextView tvDescription;

    private Day mDay;
    private int mPosition;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Day day, int position) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DAY, day);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDay = (Day) getArguments().getSerializable(ARG_DAY);
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, mainView);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, mPosition + 1);
        getActivity().setTitle(new SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault()).format(c.getTime()));
        tvDescription.setText(mDay.weather[0].description);
        return mainView;
    }

}
