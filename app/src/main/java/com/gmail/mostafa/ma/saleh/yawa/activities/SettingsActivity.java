package com.gmail.mostafa.ma.saleh.yawa.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.countrypicker.CountryPicker;
import com.countrypicker.CountryPickerListener;
import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.models.City;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utility;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utility.getTheme(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("theme", "0"))));
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference customPref = findPreference("country");
            customPref.setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(final Preference preference) {
                            final CountryPicker picker = CountryPicker.newInstance("Select City");
                            picker.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "COUNTRY_PICKER");
                            picker.setListener(new CountryPickerListener() {
                                @Override
                                public void onSelectCountry(String name, final String code) {
                                    final ProgressDialog pd = new ProgressDialog(getActivity());
                                    pd.setMessage("Loading Cities List...");
                                    pd.show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final City[] cities = Utility.getCountryCities(getResources(), code);
                                            final ArrayList<String> citiesNames = new ArrayList<>();
                                            for (City city : cities) {
                                                citiesNames.add(city.name);
                                            }
                                            pd.dismiss();
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
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
                                            });
                                        }
                                    }).start();
                                }
                            });
                            return true;
                        }
                    });
        }
    }
}
