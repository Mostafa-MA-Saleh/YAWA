package com.gmail.mostafa.ma.saleh.yawa.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Day(@SerializedName("date")
               @Expose
               var date: Int = 0,
               @SerializedName("temp")
               @Expose
               var temperature: Temperature,
               @SerializedName("pressure")
               @Expose
               var pressure: Double = 0.0,
               @SerializedName("humidity")
               @Expose
               var humidity: Int = 0,
               @SerializedName("weather")
               @Expose
               var weather: List<Weather>,
               @SerializedName("windSpeed")
               @Expose
               var windSpeed: Double = 0.0,
               @SerializedName("windDirection")
               @Expose
               var windDirection: Double = 0.0,
               @SerializedName("clouds")
               @Expose
               var clouds: Double = 0.0) : Parcelable {

    @Parcelize
    data class Weather(@SerializedName("id")
                       @Expose
                       var id: Int = 0,
                       @SerializedName("main")
                       @Expose
                       var main: String? = null,
                       @SerializedName("description")
                       @Expose
                       var description: String? = null,
                       @SerializedName("icon")
                       @Expose
                       var icon: String? = null) : Parcelable

    @Parcelize
    data class Temperature(@SerializedName("day")
                           @Expose
                           var day: Double = 0.0,
                           @SerializedName("min")
                           @Expose
                           var min: Double = 0.0,
                           @SerializedName("max")
                           @Expose
                           var max: Double = 0.0,
                           @SerializedName("night")
                           @Expose
                           var night: Double = 0.0,
                           @SerializedName("eve")
                           @Expose
                           var eve: Double = 0.0,
                           @SerializedName("morn")
                           @Expose
                           var morn: Double = 0.0) : Parcelable
}
