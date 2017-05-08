package com.gmail.mostafa.ma.saleh.yawa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.mostafa.ma.saleh.yawa.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoInternetFragment extends Fragment {

    public NoInternetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_no_internet, container, false);
        ButterKnife.bind(this, mainView);
        return mainView;
    }

    @OnClick(R.id.btn_retry_connection)
    public void onRetryClick() {
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();
    }

}
