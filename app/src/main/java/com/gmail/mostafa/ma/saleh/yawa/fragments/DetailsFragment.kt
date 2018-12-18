package com.gmail.mostafa.ma.saleh.yawa.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.mostafa.ma.saleh.yawa.R
import com.gmail.mostafa.ma.saleh.yawa.models.Day
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants
import com.gmail.mostafa.ma.saleh.yawa.utilities.PreferencesUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.ResourcesUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.StringUtils
import kotlinx.android.synthetic.main.fragment_details.*
import java.util.*

class DetailsFragment : Fragment() {

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
        activity?.title = StringUtils.formatDate(Constants.LONG_DATE_PATTERN, mDate!!)
        mDay?.run {
            temp_max_text_view.text = String.format("%d°", Math.round(temperature.max))
            temp_min_text_view.text = String.format("%d°", Math.round(temperature.min))
            weather_text_view.text = weather[0].main
            tv_humidity.text = if (humidity == 0) "N/A" else String.format("%d%%", humidity)
            weather_image_view.setImageResource(ResourcesUtils.getArtResourceForWeatherCondition(weather[0].id))
            tv_pressure.text = String.format("%d %s", Math.round(pressure), getString(R.string.pressure_unit))
            tv_wind_speed.text = String.format("%d %s", Math.round(windSpeed), windSpeedUnit)
            tv_wind_direction.text = getDirection(windDirection)
            clouds_arc_progress.progress = Math.round(clouds).toInt()
        }
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
