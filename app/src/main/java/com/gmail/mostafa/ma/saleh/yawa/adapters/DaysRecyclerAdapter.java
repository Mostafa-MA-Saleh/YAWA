package com.gmail.mostafa.ma.saleh.yawa.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.mostafa.ma.saleh.yawa.utilities.Utility;
import com.gmail.mostafa.ma.saleh.yawa.models.Day;
import com.gmail.mostafa.ma.saleh.yawa.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mostafa Saleh on 04/29/2017.
 */

public class DaysRecyclerAdapter extends RecyclerView.Adapter<DaysRecyclerAdapter.ViewHolder> {

    private List<Day> mDataSet;
    private Context mContext;

    public DaysRecyclerAdapter(Context context, @Nullable List<Day> days){
        if (days == null) days = new ArrayList<>();
        mDataSet = new ArrayList<>(days);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.days_rv_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Day d = mDataSet.get(position);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, position+1);
        holder.tvDay.setText(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        holder.tvTempMax.setText(String.format(Locale.getDefault(), "%d°", Math.round(d.temp.max)));
        holder.tvTempMin.setText(String.format(Locale.getDefault(), "%d°", Math.round(d.temp.min)));
        holder.tvDescription.setText(d.weather[0].main);
        holder.imgWeatherIcon.setImageResource(Utility.getIconResourceForWeatherCondition(d.weather[0].id));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void add(Day day){
        mDataSet.add(day);
        notifyItemInserted(mDataSet.size() - 1);
    }

    public void set(int index, Day day){
        mDataSet.set(index, day);
        notifyItemChanged(index);
    }

    public void remove(int index){
        mDataSet.remove(index);
        notifyItemRemoved(index);
    }

    public void remove(Day day){
        int index = mDataSet.indexOf(day);
        mDataSet.remove(day);
        notifyItemRemoved(index);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_weather_icon) ImageView imgWeatherIcon;
        @BindView(R.id.tv_day) TextView tvDay;
        @BindView(R.id.tv_description) TextView tvDescription;
        @BindView(R.id.tv_temp_max) TextView tvTempMax;
        @BindView(R.id.tv_temp_min) TextView tvTempMin;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
