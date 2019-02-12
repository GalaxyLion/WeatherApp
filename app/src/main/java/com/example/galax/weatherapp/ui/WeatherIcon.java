package com.example.galax.weatherapp.ui;

import com.example.galax.weatherapp.R;

public class WeatherIcon {
    public static int getIconView(int conditionId) {

        if (conditionId >= 200 && conditionId < 210) {
            return R.drawable.ic_thunderstorm_with_rain;
        }
        if (conditionId >= 210 && conditionId < 300) {
            return R.drawable.ic_thunderstorm;
        }
        if (conditionId >= 500 && conditionId < 600) {
            return R.drawable.ic_rain;
        }
        if (conditionId >= 600 && conditionId < 700) {
            return R.drawable.ic_snowy;
        }
        if (conditionId >= 300 && conditionId < 500) {
            return R.drawable.ic_drizzle;
        }
        if (conditionId >= 701 && conditionId < 800) {
            return R.drawable.ic_fog;
        }
        if (conditionId == 800) {
            return R.drawable.ic_clear;
        }
        if (conditionId == 801) {
            return R.drawable.ic_few_clouds;
        }
        if (conditionId == 802) {
            return R.drawable.ic_cloud;
        }
        if (conditionId >= 803) {
            return R.drawable.ic_clouds;
        }

        return R.drawable.ic_error;
    }
}
