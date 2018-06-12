package com.gmail.mostafa.ma.saleh.yawa.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gmail.mostafa.ma.saleh.yawa.R
import com.gmail.mostafa.ma.saleh.yawa.models.Day
import com.gmail.mostafa.ma.saleh.yawa.utilities.ResourcesUtils
import java.util.*

class DaysRecyclerAdapter(context: Context) : RecyclerView.Adapter<DaysRecyclerAdapter.ViewHolder>() {

    private val mDataSet = ArrayList<Day>()
    private var onItemClickListener: OnItemClickListener? = null
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contentView = layoutInflater.inflate(R.layout.days_rv_row, parent, false)
        return ViewHolder(contentView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder) {
        val day = mDataSet[position]
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, position + 1)
        tvDay.text = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        tvTempMax.text = String.format("%d°", Math.round(day.temperature.max))
        tvTempMin.text = String.format("%d°", Math.round(day.temperature.min))
        tvDescription.text = day.weather[0].main
        imgWeatherIcon.setImageResource(ResourcesUtils.getIconResourceForWeatherCondition(day.weather[0].id))
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    fun add(day: Day) = with(mDataSet) {
        add(day)
        notifyItemInserted(size - 1)
    }

    fun clear() = with(mDataSet) {
        val size = this.size
        clear()
        notifyItemRangeRemoved(0, size)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, day: Day, position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val imgWeatherIcon: ImageView = itemView.findViewById(R.id.img_weather_icon)
        val tvDay: TextView = itemView.findViewById(R.id.tv_day)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvTempMax: TextView = itemView.findViewById(R.id.tv_temp_max)
        val tvTempMin: TextView = itemView.findViewById(R.id.tv_temp_min)
        val row: View = itemView.findViewById(R.id.row)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onItemClickListener?.onItemClick(v, mDataSet[adapterPosition], adapterPosition)
        }
    }
}
