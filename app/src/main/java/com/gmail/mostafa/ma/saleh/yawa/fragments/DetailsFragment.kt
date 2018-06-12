package com.gmail.mostafa.ma.saleh.yawa.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.lzyzsd.circleprogress.ArcProgress
import com.gmail.mostafa.ma.saleh.yawa.R
import com.gmail.mostafa.ma.saleh.yawa.models.Day
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants
import com.gmail.mostafa.ma.saleh.yawa.utilities.ResourcesUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.SharedPreferencesManager
import com.gmail.mostafa.ma.saleh.yawa.utilities.StringUtils
import java.util.*

class DetailsFragment : Fragment() {

    private var tvTempMax: TextView? = null
    private var tvTempMin: TextView? = null
    private var tvStatus: TextView? = null
    private var tvHumidity: TextView? = null
    private var tvWindSpeed: TextView? = null
    private var tvWindDirection: TextView? = null
    private var tvPressure: TextView? = null
    private var imgWeatherStatus: ImageView? = null
    private var aprgClouds: ArcProgress? = null

    private var mDay: Day? = null
    private var mDate: Date? = null

    private val windSpeedUnit: String
        get() {
            val tempUnits = SharedPreferencesManager.getString(Constants.KEY_TEMP_UNITS, "metric")
            return if (tempUnits?.contains("imperial") == true) {
                getString(R.string.wind_speed_unit_imperial)
            } else {
                getString(R.string.wind_speed_unit_metric)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDay = arguments?.getParcelable(PARAM_DAY)
        mDate = arguments?.getSerializable(PARAM_DATE) as? Date
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViewsById(view)
        activity?.title = StringUtils.formatDate(Constants.LONG_DATE_PATTERN, mDate!!)
        tvTempMax?.text = String.format("%d°", Math.round(mDay!!.temperature.max))
        tvTempMin?.text = String.format("%d°", Math.round(mDay!!.temperature.min))
        tvStatus?.text = mDay!!.weather[0].main
        tvHumidity?.text = if (mDay!!.humidity == 0) "N/A" else String.format("%d%%", mDay!!.humidity)
        imgWeatherStatus?.setImageResource(ResourcesUtils.getArtResourceForWeatherCondition(mDay!!.weather[0].id))
        tvPressure?.text = String.format("%d %s", Math.round(mDay!!.pressure), getString(R.string.pressure_unit))
        tvWindSpeed?.text = String.format("%d %s", Math.round(mDay!!.windSpeed), windSpeedUnit)
        tvWindDirection?.text = getDirection(mDay!!.windDirection)
        aprgClouds?.progress = Math.round(mDay!!.clouds).toInt()
    }

    private fun findViewsById(view: View) = with(view) {
        tvTempMax = findViewById(R.id.tv_temp_max)
        tvTempMin = findViewById(R.id.tv_temp_min)
        tvStatus = findViewById(R.id.tv_weather_status)
        tvHumidity = findViewById(R.id.tv_humidity)
        tvWindSpeed = findViewById(R.id.tv_wind_speed)
        tvWindDirection = findViewById(R.id.tv_wind_direction)
        tvPressure = findViewById(R.id.tv_pressure)
        imgWeatherStatus = findViewById(R.id.img_weather)
        aprgClouds = findViewById(R.id.aprg_clouds)
    }

    private fun getDirection(angle: Double): String {
        val value = (angle / 22.5 + 0.5).toInt()
        return resources.getStringArray(R.array.directions)[value % 16]
    }

    companion object {

        private const val PARAM_DAY = "param_day"
        private const val PARAM_DATE = "param_date"

        fun newInstance(day: Day, date: Date) = DetailsFragment().apply {
            val args = with(Bundle()) {
                putParcelable(PARAM_DAY, day)
                putSerializable(PARAM_DATE, date)
                return@with this
            }
            arguments = args
            return this
        }
    }
}
