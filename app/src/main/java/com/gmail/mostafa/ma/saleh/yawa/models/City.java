package com.gmail.mostafa.ma.saleh.yawa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mostafa Saleh on 05/01/2017.
 */

public class City implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("coord")
    @Expose
    public LatLng coordinates;
    @SerializedName("country")
    @Expose
    public String country;

}
