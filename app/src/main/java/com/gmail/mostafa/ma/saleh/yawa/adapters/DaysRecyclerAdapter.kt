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
import kotlin.collections.ArrayList

class DaysRecyclerAdapter(context: Context) : RecyclerView.Adapter<DaysRecyclerAdapter.ViewHolder>() {

    private val mDataSet = ArrayList<Day>()
    private var onItemClick: ((View, Day, Int) -> Unit)? = null
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    fun setOnItemClickListener(onItemClick: ((View, Day, Int) -> Unit)) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contentView = layoutInflater.inflate(R.layout.list_item_day, parent, false)
        return ViewHolder(contentView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder) {
        val day = mDataSet[position]
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, position + 1)
        dayTextView.text = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        tempMaxTextView.text = String.format("%d°", Math.round(day.temperature.max))
        tempMinTextView.text = String.format("%d°", Math.round(day.temperature.min))
        descriptionTextView.text = day.weather[0].main
        weatherImageView.setImageResource(ResourcesUtils.getIconResourceForWeatherCondition(day.weather[0].id))
    }

    override fun getItemCount() = mDataSet.size

    fun add(day: Day) = with(mDataSet) {
        add(day)
        notifyItemInserted(size - 1)
    }

    fun clear() = with(mDataSet) {
        val size = this.size
        clear()
        notifyItemRangeRemoved(0, size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val weatherImageView: ImageView = itemView.findViewById(R.id.weather_image_view)
        val dayTextView: TextView = itemView.findViewById(R.id.day_text_view)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tv_description)
        val tempMaxTextView: TextView = itemView.findViewById(R.id.temp_max_text_view)
        val tempMinTextView: TextView = itemView.findViewById(R.id.temp_min_text_view)

        init {
            itemView.setOnClickListener { onItemClick?.invoke(it, mDataSet[adapterPosition], adapterPosition) }
        }
    }
}
