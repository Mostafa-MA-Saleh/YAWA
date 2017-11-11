package com.gmail.mostafa.ma.saleh.yawa.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.gmail.mostafa.ma.saleh.yawa.fragments.MainFragment;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(Utils.getTheme(Integer.parseInt(preferences.getString("theme", "0"))));
        setupActionBar();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                updateActionBar(getSupportFragmentManager().getBackStackEntryCount() == 0);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new MainFragment())
                    .commit();
        }
    }

    private void setupActionBar() {
        assert getSupportActionBar() != null;
        getSupportActionBar().setElevation(0);
        updateActionBar(getSupportFragmentManager().getBackStackEntryCount() == 0);
    }

    private void updateActionBar(boolean forHome) {
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(!forHome);
        if (forHome) {
            setTitle(new SimpleDateFormat("'Today,' MMMM dd", Locale.getDefault()).format(new Date()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
