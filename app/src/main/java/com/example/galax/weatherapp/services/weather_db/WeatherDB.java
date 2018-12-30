package com.example.galax.weatherapp.services.weather_db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.galax.weatherapp.services.weather_db.entities.WeatherEntity;
import com.example.galax.weatherapp.services.weather_db.entities.WeatherForecastEntity;

@Database(entities = {WeatherEntity.class, WeatherForecastEntity.class}, version = 1, exportSchema = false)
public abstract class WeatherDB extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
