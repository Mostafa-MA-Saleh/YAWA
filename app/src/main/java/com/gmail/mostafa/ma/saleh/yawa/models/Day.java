package com.gmail.mostafa.ma.saleh.yawa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Day implements Serializable {
    @SerializedName("dt")
    @Expose
    public int dt;
    @SerializedName("temp")
    @Expose
    public Temp temp;
    @SerializedName("pressure")
    @Expose
    public double pressure;
    @SerializedName("humidity")
    @Expose
    public int humidity;
    @SerializedName("weather")
    @Expose
    public Weather[] weather;
    @SerializedName("speed")
    @Expose
    public float speed;
    @SerializedName("deg")
    @Expose
    public float deg;
    @SerializedName("clouds")
    @Expose
    public float clouds;

    public class Weather implements Serializable {

        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("main")
        @Expose
        public String main;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("icon")
        @Expose
        public String icon;
    }

    public class Temp implements Serializable {

        @SerializedName("day")
        @Expose
        public double day;
        @SerializedName("min")
        @Expose
        public double min;
        @SerializedName("max")
        @Expose
        public double max;
        @SerializedName("night")
        @Expose
        public double night;
        @SerializedName("eve")
        @Expose
        public double eve;
        @SerializedName("morn")
        @Expose
        public double morn;
    }
}
