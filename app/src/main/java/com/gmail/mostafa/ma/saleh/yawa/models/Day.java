package com.gmail.mostafa.ma.saleh.yawa.models;

import java.util.ArrayList;

/**
 * Created by Mostafa Saleh on 04/29/2017.
 */

public class Day {

    public long dt;
    public float pressure;
    public float humidity;
    public float speed;
    public float deg;
    public float clouds;
    public Temperature temp;
    public Weather[] weather;

    public class Temperature {
        public float day;
        public float min;
        public float max;
        public float night;
        public float eve;
        public float morn;
    }

    public class Weather{
        public int id;
        public String main;
        public String description;
        public String icon;
    }

}
