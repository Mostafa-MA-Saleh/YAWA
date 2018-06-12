package com.gmail.mostafa.ma.saleh.yawa.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForecastResponse(
        @SerializedName("list")
        @Expose
        val days: List<Day>)
