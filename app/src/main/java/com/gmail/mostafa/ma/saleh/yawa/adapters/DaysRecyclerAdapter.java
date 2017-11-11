package com.gmail.mostafa.ma.saleh.yawa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.models.Day;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaysRecyclerAdapter extends RecyclerView.Adapter<DaysRecyclerAdapter.ViewHolder> {

    private List<Day> mDataSet = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;

    public DaysRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = layoutInflater.inflate(R.layout.days_rv_row, parent, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Day day = mDataSet.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, position + 1);
        holder.tvDay.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        holder.tvTempMax.setText(String.format(Locale.getDefault(), "%d°", Math.round(day.temp.max)));
        holder.tvTempMin.setText(String.format(Locale.getDefault(), "%d°", Math.round(day.temp.min)));
        holder.tvDescription.setText(day.weather[0].main);
        holder.imgWeatherIcon.setImageResource(Utils.getIconResourceForWeatherCondition(day.weather[0].id));
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, day, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void add(Day day) {
        mDataSet.add(day);
        notifyItemInserted(mDataSet.size() - 1);
    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Day day, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_weather_icon)
        ImageView imgWeatherIcon;
        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.tv_temp_max)
        TextView tvTempMax;
        @BindView(R.id.tv_temp_min)
        TextView tvTempMin;
        @BindView(R.id.row)
        View row;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
