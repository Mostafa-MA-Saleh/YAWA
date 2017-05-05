package com.gmail.mostafa.ma.saleh.yawa.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.fragments.MainFragment;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utility;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(Utility.getTheme(Integer.parseInt(preferences.getString("theme", "0"))));
        super.onCreate(savedInstanceState);
        assert getSupportActionBar() != null;
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        applyActionBar(getSupportFragmentManager().getBackStackEntryCount() == 0);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                applyActionBar(getSupportFragmentManager().getBackStackEntryCount() == 0);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new MainFragment())
                    .commit();
        }
    }

    private void applyActionBar(boolean home) {
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(!home);
        getSupportActionBar().setDisplayUseLogoEnabled(home);
        if (home) {
            setTitle(R.string.app_name_long);
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
