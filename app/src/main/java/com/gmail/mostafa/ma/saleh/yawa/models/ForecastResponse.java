package com.gmail.mostafa.ma.saleh.yawa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForecastResponse {
    @SerializedName("list")
    @Expose
    public Day[] days;
}
