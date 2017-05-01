package com.gmail.mostafa.ma.saleh.yawa.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.adapters.DaysRecyclerAdapter;
import com.gmail.mostafa.ma.saleh.yawa.adapters.NetworkAdapter;
import com.gmail.mostafa.ma.saleh.yawa.models.Day;
import com.gmail.mostafa.ma.saleh.yawa.utilities.Utility;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static int RC_SETTINGS = 22;
    @BindView(R.id.days_rv) RecyclerView rvDays;
    @BindView(R.id.tv_date) TextView tvDate;
    @BindView(R.id.tv_temp_max) TextView tvTempMax;
    @BindView(R.id.tv_temp_min) TextView tvTempMin;
    @BindView(R.id.tv_description) TextView tvDescription;
    @BindView(R.id.img_weather_icon) ImageView imgWeatherIcon;
    private SharedPreferences preferences;
    private DaysRecyclerAdapter daysRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(Utility.getTheme(Integer.parseInt(preferences.getString("theme", "0"))));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);
        daysRecyclerAdapter = new DaysRecyclerAdapter(this, null);
        rvDays.setAdapter(daysRecyclerAdapter);
        rvDays.setLayoutManager(new LinearLayoutManager(this));
        String requestURL =
                "http://api.openweathermap.org/data/2.5/forecast/daily?q=" +
                        "Alexandria,EG" +
                        preferences.getString("temp_unit", "&units=metric") +
                        "&cnt=16" +
                        "&APPID=026ee82032707259db948706d2c48df2";
        JsonObjectRequest request = new JsonObjectRequest(requestURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tvDate.setText(new SimpleDateFormat("'Today,' MMMM dd", Locale.getDefault()).format(new Date()));
                            Day[] days = new Gson().fromJson(response.getJSONArray("list").toString(), Day[].class);
                            tvTempMax.setText(String.format(Locale.getDefault(), "%d°", Math.round(days[0].temp.max)));
                            tvTempMin.setText(String.format(Locale.getDefault(), "%d°", Math.round(days[0].temp.min)));
                            tvDescription.setText(days[0].weather[0].main);
                            imgWeatherIcon.setImageResource(Utility.getArtResourceForWeatherCondition(days[0].weather[0].id));
                            for(int i = 1; i < days.length; i++)
                                daysRecyclerAdapter.add(days[i]);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        NetworkAdapter.getInstance().getRequestQueue(this).add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), RC_SETTINGS);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SETTINGS && resultCode == RESULT_OK) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
