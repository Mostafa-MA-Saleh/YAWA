package com.gmail.mostafa.ma.saleh.yawa.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.gmail.mostafa.ma.saleh.yawa.R
import com.gmail.mostafa.ma.saleh.yawa.adapters.DaysRecyclerAdapter
import com.gmail.mostafa.ma.saleh.yawa.models.Day
import com.gmail.mostafa.ma.saleh.yawa.retrofit.OnFinishedListener
import com.gmail.mostafa.ma.saleh.yawa.retrofit.RetrofitManager
import com.gmail.mostafa.ma.saleh.yawa.utilities.FragmentUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.ResourcesUtils
import java.util.*

class MainFragment : Fragment(), DaysRecyclerAdapter.OnItemClickListener {

    private var daysRecyclerView: RecyclerView? = null
    private var tempMaxTextView: TextView? = null
    private var tempMinTextView: TextView? = null
    private var descriptionTextView: TextView? = null
    private var weatherImageView: ImageView? = null
    private var refreshLayout: SwipeRefreshLayout? = null

    private var daysRecyclerAdapter: DaysRecyclerAdapter? = null
    private var onFinishedListener: OnFinishedListener<List<Day>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViewsById(view)
        daysRecyclerAdapter = DaysRecyclerAdapter(context!!)
        daysRecyclerView?.adapter = daysRecyclerAdapter
        refresh()
        refreshLayout?.isRefreshing = true
        refreshLayout?.setOnRefreshListener { refresh() }
        daysRecyclerAdapter?.setOnItemClickListener(this)
    }

    private fun findViewsById(view: View) = with(view) {
        daysRecyclerView = findViewById(R.id.days_recycler_view)
        tempMaxTextView = findViewById(R.id.temp_max_text_view)
        tempMinTextView = findViewById(R.id.temp_min_text_view)
        descriptionTextView = findViewById(R.id.tv_description)
        weatherImageView = findViewById(R.id.weather_image_view)
        refreshLayout = findViewById(R.id.refresh)
    }

    fun refresh() {
        daysRecyclerAdapter?.clear()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val cityId = preferences.getString("city", "524901")
        val tempUnit = preferences.getString("temp_unit", "metric")
        onFinishedListener?.cancel()
        onFinishedListener = object : OnFinishedListener<List<Day>>() {
            override fun onSuccess(arg: List<Day>) {
                tempMaxTextView?.text = String.format("%d°", Math.round(arg[0].temperature.max))
                tempMinTextView?.text = String.format("%d°", Math.round(arg[0].temperature.min))
                descriptionTextView?.text = arg[0].weather[0].main
                weatherImageView?.setImageResource(ResourcesUtils.getArtResourceForWeatherCondition(arg[0].weather[0].id))
                for (i in 1 until arg.size) {
                    daysRecyclerAdapter?.add(arg[i])
                }
                refreshLayout?.isRefreshing = false
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

    override fun onItemClick(view: View, day: Day, position: Int) {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, position + 1)
        FragmentUtils.replaceFragment(fragmentManager, DetailsFragment.newInstance(day, c.time))
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
