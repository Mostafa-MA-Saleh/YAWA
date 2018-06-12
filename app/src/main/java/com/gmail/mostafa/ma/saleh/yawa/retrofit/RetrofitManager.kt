package com.gmail.mostafa.ma.saleh.yawa.retrofit

import com.gmail.mostafa.ma.saleh.yawa.models.Day
import com.gmail.mostafa.ma.saleh.yawa.models.ForecastResponse
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    private val requests by lazy { buildClient().create(Requests::class.java) }

    private fun buildClient(): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original.url().newBuilder()
                            .addQueryParameter("APPID", Constants.APP_ID)
                    val request = original.newBuilder()
                            .url(url.build()).build()
                    chain.proceed(request)
                }
                .build()
        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
    }

    fun getForecast(cityId: String, tempUnit: String, onFinishedListener: OnFinishedListener<List<Day>>) {
        requests.getForecast(cityId, tempUnit).enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                val forecastResponse = response.body()
                onFinishedListener.onComplete(forecastResponse?.days, null)
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                onFinishedListener.onComplete(null, t.localizedMessage)
            }
        })
    }
}
