package com.gmail.mostafa.ma.saleh.yawa.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.*
import com.gmail.mostafa.ma.saleh.yawa.R
import com.gmail.mostafa.ma.saleh.yawa.adapters.DaysRecyclerAdapter
import com.gmail.mostafa.ma.saleh.yawa.models.Day
import com.gmail.mostafa.ma.saleh.yawa.retrofit.OnFinishedListener
import com.gmail.mostafa.ma.saleh.yawa.retrofit.RetrofitManager
import com.gmail.mostafa.ma.saleh.yawa.utilities.FragmentUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.ResourcesUtils
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main_header.*
import java.util.*

class MainFragment : Fragment() {

    private val daysRecyclerAdapter by lazy { DaysRecyclerAdapter(context!!) }
    private var onFinishedListener: OnFinishedListener<List<Day>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        days_recycler_view.adapter = daysRecyclerAdapter
        refresh()
        refresh.isRefreshing = true
        refresh.setOnRefreshListener { refresh() }
        daysRecyclerAdapter.setOnItemClickListener(::onItemClick)
    }

    fun refresh() {
        daysRecyclerAdapter.clear()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val cityId = preferences.getString("city", "524901")
        val tempUnit = preferences.getString("temp_unit", "metric")
        onFinishedListener?.cancel()
        onFinishedListener = object : OnFinishedListener<List<Day>>() {
            override fun onSuccess(arg: List<Day>) {
                temp_max_text_view.text = String.format("%d°", Math.round(arg[0].temperature.max))
                temp_min_text_view.text = String.format("%d°", Math.round(arg[0].temperature.min))
                tv_description.text = arg[0].weather[0].main
                weather_image_view.setImageResource(ResourcesUtils.getArtResourceForWeatherCondition(arg[0].weather[0].id))
                for (i in 1 until arg.size) {
                    daysRecyclerAdapter.add(arg[i])
                }
                refresh.isRefreshing = false
            }

            override fun onFailure(message: String?) {
                FragmentUtils.replaceFragment(fragmentManager, NoInternetFragment.newInstance(), false)
            }

            override fun onComplete(args: List<Day>?, message: String?) {
                super.onComplete(args, message)
                onFinishedListener = null
            }
        }
        RetrofitManager.getForecast(cityId, tempUnit, onFinishedListener!!)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> FragmentUtils.replaceFragment(fragmentManager, SettingsFragment.newInstance())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onItemClick(view: View, day: Day, position: Int) {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, position + 1)
        FragmentUtils.replaceFragment(fragmentManager, DetailsFragment.newInstance(day, c.time))
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
