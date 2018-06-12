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
import com.gmail.mostafa.ma.saleh.yawa.dialogs.AboutDialog
import com.gmail.mostafa.ma.saleh.yawa.models.City
import com.gmail.mostafa.ma.saleh.yawa.retrofit.OnFinishedListener
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants
import com.gmail.mostafa.ma.saleh.yawa.utilities.DialogUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.JSONUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.SharedPreferencesManager
import com.google.gson.Gson
import com.mukesh.countrypicker.CountryPicker
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private var countryPref: Preference? = null
    private var cityPref: Preference? = null
    private var themePref: Preference? = null
    private var aboutPref: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        findPreferences()
        countryPref?.onPreferenceClickListener = this
        cityPref?.onPreferenceClickListener = this
        themePref?.onPreferenceChangeListener = this
        aboutPref?.onPreferenceClickListener = this
    }

    private fun findPreferences() {
        countryPref = findPreference(Constants.KEY_COUNTRY)
        cityPref = findPreference(Constants.KEY_CITY)
        themePref = findPreference(Constants.KEY_THEME)
        aboutPref = findPreference(Constants.KEY_ABOUT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.background = ColorDrawable(ContextCompat.getColor(context!!, android.R.color.background_light))
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
        picker.setListener { _, code, _, _ ->
            SharedPreferencesManager.saveString(Constants.KEY_COUNTRY, code)
        }
        picker.show(childFragmentManager, "COUNTRY_PICKER")
    }

    private fun prepareToShowCitiesDialog() {
        val progressDialog = DialogUtils.showProgressDialog(context!!)
        getCitiesList(object : OnFinishedListener<Array<City>>() {
            override fun onSuccess(arg: Array<City>) {
                showCitiesDialog(arg)
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
        })
    }

    private fun getCitiesList(onFinishedListener: OnFinishedListener<Array<City>>) {
        Thread({
            try {
                val countryCode = SharedPreferencesManager.getString("country", "EG")
                val json = JSONUtils.readFromResources(resources, R.raw.city_list)
                val jsonObject = JSONObject(json)
                val cityArray = jsonObject.optJSONArray(countryCode)
                runOnUiThread { onFinishedListener.onComplete(Gson().fromJson(cityArray.toString(), Array<City>::class.java), null) }
            } catch (e: JSONException) {
                e.printStackTrace()
                runOnUiThread { onFinishedListener.onComplete(null, e.localizedMessage) }
            }
        }).start()
    }

    private fun showCitiesDialog(cities: Array<City>) {
        val citiesNames = ArrayList<String>()
        for ((_, name) in cities) citiesNames.add(name)
        DialogUtils.showListDialog(context!!, citiesNames, DialogInterface.OnClickListener { dialog, which ->
            SharedPreferencesManager.saveString(Constants.KEY_CITY, cities[which].id)
            dialog.dismiss()
        })
    }

    private fun runOnUiThread(runnable: () -> Unit) = activity?.runOnUiThread(runnable)

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
