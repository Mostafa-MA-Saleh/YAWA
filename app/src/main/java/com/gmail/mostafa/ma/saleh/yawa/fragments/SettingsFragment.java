package com.gmail.mostafa.ma.saleh.yawa.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SettingsFragment extends PreferenceFragment {

    private Preference countryPref;
    private Preference cityPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        countryPref = findPreference("country");
        countryPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                CountryPicker picker = CountryPicker.newInstance("Select City");
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

        cityPref = findPreference("city");
        cityPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new Task().execute();
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
                            }
                        }
                )
                .create()
                .show();
    }

    private class Task extends AsyncTask<Void, Void, City[]> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Cities List...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Task.this.cancel(true);
                }
            });
        }

        @Override
        protected City[] doInBackground(Void... params) {
            String countryCode = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(countryPref.getKey(), "EG");
            ArrayList<City> countryCities = new ArrayList<>();
            City[] cities = new Gson().fromJson(Utility.readJSONFromResources(getResources()), City[].class);
            for (City city : cities) {
                if (isCancelled()) return new City[0];
                if (city.country.equals(countryCode)) {
                    countryCities.add(city);
                }
            }
            Collections.sort(countryCities, new Comparator<City>() {
                @Override
                public int compare(City o1, City o2) {
                    return o1.name.compareTo(o2.name);
                }
            });
            return countryCities.toArray(new City[0]);
        }

        @Override
        protected void onPostExecute(City[] cities) {
            super.onPostExecute(cities);
            progressDialog.dismiss();
            showCitiesDialog(cities, cityPref);
        }
    }
}