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
import com.gmail.mostafa.ma.saleh.yawa.utilities.PreferencesUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.ResourcesUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.StringUtils
import java.util.*

class DetailsFragment : Fragment() {

    private var tempMaxTextView: TextView? = null
    private var tempMinTextView: TextView? = null
    private var weatherTextView: TextView? = null
    private var humidityTextView: TextView? = null
    private var windSpeedTextView: TextView? = null
    private var windDirectionTextView: TextView? = null
    private var pressureTextView: TextView? = null
    private var weatherImageView: ImageView? = null
    private var cloudsArcProgress: ArcProgress? = null

    private var mDay: Day? = null
    private var mDate: Date? = null

    private val windSpeedUnit: String
        get() {
            val tempUnits = PreferencesUtils.getString(Constants.KEY_TEMP_UNITS, "metric")
            return if (tempUnits.contains("imperial")) {
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
        mDay?.run {
            tempMaxTextView?.text = String.format("%d°", Math.round(temperature.max))
            tempMinTextView?.text = String.format("%d°", Math.round(temperature.min))
            weatherTextView?.text = weather[0].main
            this@DetailsFragment.humidityTextView?.text = if (humidity == 0) "N/A" else String.format("%d%%", humidity)
            weatherImageView?.setImageResource(ResourcesUtils.getArtResourceForWeatherCondition(weather[0].id))
            pressureTextView?.text = String.format("%d %s", Math.round(pressure), getString(R.string.pressure_unit))
            windSpeedTextView?.text = String.format("%d %s", Math.round(windSpeed), windSpeedUnit)
            windDirectionTextView?.text = getDirection(windDirection)
            cloudsArcProgress?.progress = Math.round(clouds).toInt()
        }
    }

    private fun findViewsById(view: View) = with(view) {
        tempMaxTextView = findViewById(R.id.temp_max_text_view)
        tempMinTextView = findViewById(R.id.temp_min_text_view)
        weatherTextView = findViewById(R.id.weather_text_view)
        humidityTextView = findViewById(R.id.tv_humidity)
        windSpeedTextView = findViewById(R.id.tv_wind_speed)
        windDirectionTextView = findViewById(R.id.tv_wind_direction)
        pressureTextView = findViewById(R.id.tv_pressure)
        weatherImageView = findViewById(R.id.weather_image_view)
        cloudsArcProgress = findViewById(R.id.clouds_arc_progress)
    }

    private fun getDirection(angle: Double): String {
        val value = (angle / 22.5 + 0.5).toInt()
        return resources.getStringArray(R.array.directions)[value % 16]
    }

    companion object {

        private const val PARAM_DAY = "param_day"
        private const val PARAM_DATE = "param_date"

        fun newInstance(day: Day, date: Date) = DetailsFragment().apply {
            arguments = with(Bundle()) {
                putParcelable(PARAM_DAY, day)
                putSerializable(PARAM_DATE, date)
                return@with this
            }
            return this
        }
    }
}
