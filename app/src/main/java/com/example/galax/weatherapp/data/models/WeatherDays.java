package com.example.galax.weatherapp.data.models;

import android.widget.ImageView;
import android.widget.TextView;

public class WeatherDays{
    private TextView day;
    private ImageView icon;
    private TextView temp;

    public WeatherDays(TextView day, ImageView icon, TextView temp) {
        this.day = day;
        this.icon = icon;
        this.temp = temp;
    }

    public TextView getDay() {
        return day;
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getTemp() {
        return temp;
    }
}