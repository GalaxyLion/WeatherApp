package com.example.galax.weatherapp.services.factories;

import android.app.Activity;
import android.content.Intent;

import com.example.galax.weatherapp.WeatherActivity;

import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.services.Screen;


public class ScreenActivityFactory {

    public Intent getActivityByType(Screen screen){
        return new Intent(App.getInstance(),getActivityClassByType(screen));
    }

    private Class<? extends Activity> getActivityClassByType(Screen screen){
        switch (screen){
            case WEATHER:
                return WeatherActivity.class;
                default:return WeatherActivity.class;
        }
    }

}
