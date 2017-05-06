package com.gmail.mostafa.ma.saleh.yawa.models;

import java.io.Serializable;

public class Day implements Serializable {

    public long dt;
    public float pressure;
    public float humidity;
    public float speed;
    public float deg;
    public float clouds;
    public Temperature temp;
    public Weather[] weather;

    public class Temperature implements Serializable {
        public float day;
        public float min;
        public float max;
        public float night;
        public float eve;
        public float morn;
    }

    public class Weather implements Serializable {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

}
