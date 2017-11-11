package com.gmail.mostafa.ma.saleh.yawa.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.models.City;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;
import com.gmail.mostafa.ma.saleh.yawa.utilities.countrypicker.CountryPicker;
import com.gmail.mostafa.ma.saleh.yawa.utilities.countrypicker.CountryPickerListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference countryPref;
    private Preference cityPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        countryPref = findPreference("country");
        countryPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                CountryPicker picker = CountryPicker.newInstance("Select City");
                picker.show(getFragmentManager(), "COUNTRY_PICKER");
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, final String code) {
                        PreferenceManager
                                .getDefaultSharedPreferences(getContext())
                                .edit()
                                .putString(preference.getKey(), code)
                                .apply();
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

        findPreference("theme").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                getActivity().recreate();
                return true;
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
                                PreferenceManager
                                        .getDefaultSharedPreferences(getContext())
                                        .edit()
                                        .putString(preference.getKey(), cities[which].id)
                                        .apply();
                                dialog.dismiss();
                            }
                        }
                )
                .create()
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.settings);
        return super.onCreateView(inflater, container, savedInstanceState);
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
            try {
                String countryCode = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(countryPref.getKey(), "EG");
                String json = Utils.readJSONFromResources(getResources());
                JSONObject jsonObject = new JSONObject(json);
                JSONArray cityArray = jsonObject.optJSONArray(countryCode);
                return new Gson().fromJson(cityArray.toString(), City[].class);
            } catch (JSONException e) {
                e.printStackTrace();
                return new City[0];
            }
        }

        @Override
        protected void onPostExecute(City[] cities) {
            super.onPostExecute(cities);
            progressDialog.dismiss();
            if (!isCancelled()) {
                showCitiesDialog(cities, cityPref);
            }
        }
    }
}
