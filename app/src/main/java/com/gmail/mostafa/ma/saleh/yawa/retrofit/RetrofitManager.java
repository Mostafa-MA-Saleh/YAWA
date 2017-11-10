package com.gmail.mostafa.ma.saleh.yawa.retrofit;

import android.support.annotation.NonNull;

import com.gmail.mostafa.ma.saleh.yawa.models.Day;
import com.gmail.mostafa.ma.saleh.yawa.models.ForecastResponse;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static RetrofitManager INSTANCE = new RetrofitManager();
    private Requests requests;

    private RetrofitManager() {
        buildClient();
    }

    public static RetrofitManager getInstance() {
        return INSTANCE;
    }

    private void buildClient() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor appIdInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl.Builder url = original.url().newBuilder()
                        .addQueryParameter("APPID", Constants.APP_ID);
                Request request = original.newBuilder()
                        .url(url.build()).build();
                return chain.proceed(request);
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(appIdInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        requests = retrofit.create(Requests.class);
    }

    public void getForecast(String cityId, String tempUnit, final OnFinishedListener<Day[]> onFinishedListener) {
        requests.getForecast(cityId, tempUnit).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                ForecastResponse forecastResponse = response.body();
                if (forecastResponse != null) {
                    onFinishedListener.onComplete(forecastResponse.days, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {
                onFinishedListener.onComplete(null, t.getLocalizedMessage());
            }
        });
    }

}
