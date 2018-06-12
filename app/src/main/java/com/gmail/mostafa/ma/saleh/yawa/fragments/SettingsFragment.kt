package com.gmail.mostafa.ma.saleh.yawa.fragments

import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.View
import android.widget.Toast
import com.gmail.mostafa.ma.saleh.yawa.R
import com.gmail.mostafa.ma.saleh.yawa.activities.MainActivity
import com.gmail.mostafa.ma.saleh.yawa.dialogs.AboutDialog
import com.gmail.mostafa.ma.saleh.yawa.models.City
import com.gmail.mostafa.ma.saleh.yawa.retrofit.OnFinishedListener
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants
import com.gmail.mostafa.ma.saleh.yawa.utilities.DialogUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.JSONUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.PreferencesUtils
import com.google.gson.Gson
import com.mukesh.countrypicker.CountryPicker
import org.json.JSONArray
import java.util.*


class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private val JSONArray.isEmpty
        get() = this.length() == 0

    private lateinit var countryPref: Preference
    private lateinit var cityPref: Preference
    private lateinit var themePref: Preference
    private lateinit var aboutPref: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        findPreferences()
        countryPref.onPreferenceClickListener = this
        cityPref.onPreferenceClickListener = this
        themePref.onPreferenceChangeListener = this
        aboutPref.onPreferenceClickListener = this
    }

    private fun findPreferences() {
        countryPref = findPreference(Constants.KEY_COUNTRY)
        cityPref = findPreference(Constants.KEY_CITY)
        themePref = findPreference(Constants.KEY_THEME)
        aboutPref = findPreference(Constants.KEY_ABOUT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.theme?.obtainStyledAttributes(intArrayOf(android.R.attr.colorBackground))?.let { attrs ->
            val attributeResourceId = attrs.getResourceId(0, 0)
            view.background = ColorDrawable(ContextCompat.getColor(context!!, attributeResourceId))
        }
        activity?.setTitle(R.string.settings)
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            Constants.KEY_COUNTRY -> {
                showCountryDialog()
                return true
            }
            Constants.KEY_CITY -> prepareToShowCitiesDialog()
            Constants.KEY_ABOUT -> AboutDialog.show(context!!)
        }
        return false
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        preference.key.takeIf { it == Constants.KEY_THEME }?.apply { activity?.recreate() }
        return true
    }

    private fun showCountryDialog() {
        val picker = CountryPicker.newInstance("Select Country")
        picker.setListener { _, countryCode, _, _ ->
            picker.dismiss()
            prepareToShowCitiesDialog(countryCode)
        }
        picker.show(childFragmentManager, "COUNTRY_PICKER")
    }

    private fun prepareToShowCitiesDialog(countryCode: String = PreferencesUtils.getString(Constants.KEY_COUNTRY, "EG")) {
        val progressDialog = DialogUtils.showProgressDialog(context!!)
        getCitiesList(object : OnFinishedListener<Array<City>>() {
            override fun onSuccess(arg: Array<City>) {
                showCitiesDialog(arg, countryCode)
            }

            override fun onFailure(message: String?) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }

            override fun onComplete(args: Array<City>?, message: String?) {
                activity?.run {
                    progressDialog.dismiss()
                    super.onComplete(args, message)
                }
            }
        }, countryCode)
    }

    private fun getCitiesList(onFinishedListener: OnFinishedListener<Array<City>>, countryCode: String) {
        Thread({
            val json = JSONUtils.readFromResources(resources, R.raw.city_list)
            val jsonCityArray = json.optJSONArray(countryCode) ?: JSONArray()
            if (jsonCityArray.isEmpty) {
                runOnUiThread { onFinishedListener.onComplete(message = getString(R.string.country_not_supported)) }
            } else {
                val cityArray = Gson().fromJson(jsonCityArray.toString(), Array<City>::class.java)
                runOnUiThread { onFinishedListener.onComplete(cityArray) }
            }
        }).start()
    }

    private fun showCitiesDialog(cities: Array<City>, countryCode: String) {
        val citiesNames = ArrayList<String>()
        for ((_, name) in cities) citiesNames.add(name)
        DialogUtils.showListDialog(context!!, citiesNames, DialogInterface.OnClickListener { dialog, which ->
            PreferencesUtils.saveString(Constants.KEY_CITY, cities[which].id)
            PreferencesUtils.saveString(Constants.KEY_COUNTRY, countryCode)
            (context as? MainActivity)?.refresh()
            dialog.dismiss()
        })
    }

    private fun runOnUiThread(runnable: () -> Unit) = activity?.runOnUiThread(runnable)

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
