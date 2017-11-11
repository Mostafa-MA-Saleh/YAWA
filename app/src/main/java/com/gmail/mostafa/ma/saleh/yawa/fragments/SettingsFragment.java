package com.gmail.mostafa.ma.saleh.yawa.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.Toast;

import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.dialogs.AboutDialog;
import com.gmail.mostafa.ma.saleh.yawa.models.City;
import com.gmail.mostafa.ma.saleh.yawa.retrofit.OnFinishedListener;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants;
import com.gmail.mostafa.ma.saleh.yawa.utilities.DialogUtils;
import com.gmail.mostafa.ma.saleh.yawa.utilities.SharedPreferencesManager;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;
import com.google.gson.Gson;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private Preference countryPref;
    private Preference cityPref;
    private Preference themePref;
    private Preference aboutPref;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        findPreferences();
        countryPref.setOnPreferenceClickListener(this);
        cityPref.setOnPreferenceClickListener(this);
        themePref.setOnPreferenceChangeListener(this);
        aboutPref.setOnPreferenceClickListener(this);
    }

    private void findPreferences() {
        countryPref = findPreference(Constants.KEY_COUNTRY);
        cityPref = findPreference(Constants.KEY_CITY);
        themePref = findPreference(Constants.KEY_THEME);
        aboutPref = findPreference(Constants.KEY_ABOUT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            getActivity().setTitle(R.string.settings);
        }
    }

    @Override
    public boolean onPreferenceClick(final Preference preference) {
        if (getContext() != null) {
            switch (preference.getKey()) {
                case Constants.KEY_COUNTRY:
                    showCountryDialog(getContext());
                    return true;
                case Constants.KEY_CITY:
                    prepareToShowCitiesDialog(getContext());
                    break;
                case Constants.KEY_ABOUT:
                    AboutDialog.show(getContext());
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (getActivity() != null) {
            switch (preference.getKey()) {
                case Constants.KEY_THEME:
                    getActivity().recreate();
            }
        }
        return true;
    }

    private void showCountryDialog(@NonNull final Context context) {
        CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                SharedPreferencesManager
                        .getInstance()
                        .saveString(Constants.KEY_COUNTRY, code);
            }
        });
        if (getFragmentManager() != null) {
            picker.show(getFragmentManager(), "COUNTRY_PICKER");
        }
    }


    private void prepareToShowCitiesDialog(@NonNull final Context context) {
        final Dialog progressDialog = DialogUtils.showProgressDialog(context);
        getCitiesList(new OnFinishedListener<City[]>() {
            @Override
            public void onSuccess(City[] arg) {
                showCitiesDialog(arg);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete(@Nullable City[] args, @Nullable String message) {
                progressDialog.dismiss();
                super.onComplete(args, message);
            }
        });
    }

    private void getCitiesList(final OnFinishedListener<City[]> onFinishedListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String countryCode = SharedPreferencesManager.getInstance().getString("country", "EG");
                    String json = Utils.readJSONFromResources(getResources());
                    JSONObject jsonObject = new JSONObject(json);
                    final JSONArray cityArray = jsonObject.optJSONArray(countryCode);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onFinishedListener.onComplete(new Gson().fromJson(cityArray.toString(), City[].class), null);
                        }
                    });
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onFinishedListener.onComplete(null, e.getLocalizedMessage());
                        }
                    });
                }
            }
        }).start();
    }

    private void showCitiesDialog(final City[] cities) {
        if (getContext() != null) {
            final ArrayList<String> citiesNames = new ArrayList<>();
            for (City city : cities) citiesNames.add(city.name);
            DialogUtils.showListDialog(getContext(), citiesNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferencesManager
                            .getInstance()
                            .saveString(Constants.KEY_CITY, cities[which].id);
                    dialog.dismiss();
                }
            });
        }
    }

    private void runOnUiThread(Runnable runnable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(runnable);
        }
    }
}
