package com.gmail.mostafa.ma.saleh.yawa.retrofit

import com.gmail.mostafa.ma.saleh.yawa.models.ForecastResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mostafa on 11/05/2017.
 */

interface Requests {

    @GET("/data/2.5/forecast/daily?cnt=16")
    fun getForecast(@Query("id") cityId: String,
                    @Query("units") tempUnit: String): Call<ForecastResponse>

}
