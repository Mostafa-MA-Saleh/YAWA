package com.gmail.mostafa.ma.saleh.yawa.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoInternetFragment extends Fragment {

    public static NoInternetFragment newInstance() {
        return new NoInternetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_internet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btn_retry_connection)
    public void onRetryClick() {
        Utils.replaceFragment(getFragmentManager(), MainFragment.newInstance(), false);
    }

}
