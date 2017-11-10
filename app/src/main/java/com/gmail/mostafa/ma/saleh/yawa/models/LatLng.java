package com.gmail.mostafa.ma.saleh.yawa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLng {
    @SerializedName("lon")
    @Expose
    public double longitude;
    @SerializedName("lat")
    @Expose
    public double latitude;
}