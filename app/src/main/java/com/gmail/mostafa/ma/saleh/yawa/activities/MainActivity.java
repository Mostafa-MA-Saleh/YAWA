package com.gmail.mostafa.ma.saleh.yawa.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.gmail.mostafa.ma.saleh.yawa.fragments.MainFragment;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants;
import com.gmail.mostafa.ma.saleh.yawa.utilities.StringUtils;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getTheme());
        setupActionBar();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                updateActionBar(isBackStackEmpty());
            }
        });

        if (savedInstanceState == null) {
            addHomeFragment();
        }
    }

    private void addHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, new MainFragment())
                .commit();
    }

    private void setupActionBar() {
        assert getSupportActionBar() != null;
        getSupportActionBar().setElevation(0);
        updateActionBar(isBackStackEmpty());
    }

    private void updateActionBar(boolean forHome) {
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(!forHome);
        if (forHome) {
            setTitle(StringUtils.formatCurrentDate(Constants.TODAY_DATE_PATTERN));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isBackStackEmpty() {
        return getSupportFragmentManager().getBackStackEntryCount() == 0;
    }
}
