package com.gmail.mostafa.ma.saleh.yawa.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.countrypicker.CountryPicker;
import com.countrypicker.CountryPickerListener;
import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.models.City;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utility;

import java.util.ArrayList;

/**
 * Created by Mostafa Saleh on 05/01/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    private CountryPicker picker;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        final Preference countryPref = findPreference("country");
        Preference cityPref = findPreference("city");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Cities List...");
        picker = CountryPicker.newInstance("Select City");
        countryPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                picker.show(fm, "COUNTRY_PICKER");
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, final String code) {
                        preference.getEditor().putString(preference.getKey(), code).apply();
                    }
                });
                return true;
            }
        });

        cityPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String countryCode = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(countryPref.getKey(), "EG");
                        final City[] cities = Utility.getCountryCities(getResources(), countryCode);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                showCitiesDialog(cities, preference);
                            }
                        });
                    }
                }).start();
                return false;
            }
        });
    }

    private void showCitiesDialog(final City[] cities, final Preference preference) {
        final ArrayList<String> citiesNames = new ArrayList<>();
        for (City city : cities) citiesNames.add(city.name);
        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(
                        citiesNames.toArray(new String[0]),
                        -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preference.getEditor().putString(preference.getKey(), cities[which].id).apply();
                                dialog.dismiss();
                                picker.dismiss();
                            }
                        }
                )
                .create()
                .show();
    }
}
