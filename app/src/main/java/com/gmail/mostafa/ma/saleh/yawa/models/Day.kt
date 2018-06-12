package com.gmail.mostafa.ma.saleh.yawa.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Day(@SerializedName("date")
               @Expose
               val date: Int,
               @SerializedName("temp")
               @Expose
               val temperature: Temperature,
               @SerializedName("pressure")
               @Expose
               val pressure: Double,
               @SerializedName("humidity")
               @Expose
               val humidity: Int,
               @SerializedName("weather")
               @Expose
               val weather: List<Weather>,
               @SerializedName("windSpeed")
               @Expose
               val windSpeed: Double,
               @SerializedName("windDirection")
               @Expose
               val windDirection: Double,
               @SerializedName("clouds")
               @Expose
               val clouds: Double) : Parcelable {

    @Parcelize
    data class Weather(@SerializedName("id")
                       @Expose
                       val id: Int,
                       @SerializedName("main")
                       @Expose
                       val main: String,
                       @SerializedName("description")
                       @Expose
                       val description: String,
                       @SerializedName("icon")
                       @Expose
                       val icon: String) : Parcelable

    @Parcelize
    data class Temperature(@SerializedName("day")
                           @Expose
                           val day: Double,
                           @SerializedName("min")
                           @Expose
                           val min: Double,
                           @SerializedName("max")
                           @Expose
                           val max: Double,
                           @SerializedName("night")
                           @Expose
                           val night: Double,
                           @SerializedName("eve")
                           @Expose
                           val eve: Double,
                           @SerializedName("morn")
                           @Expose
                           val morn: Double) : Parcelable
}
