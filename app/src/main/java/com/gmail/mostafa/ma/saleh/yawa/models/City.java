package com.gmail.mostafa.ma.saleh.yawa.models;

/**
 * Created by Mostafa Saleh on 05/01/2017.
 */

public class City {
    public String id;
    public String name;
    public String country;
    public Coordinates coord;

    public class Coordinates {
        public double lat;
        public double lon;
    }
}
