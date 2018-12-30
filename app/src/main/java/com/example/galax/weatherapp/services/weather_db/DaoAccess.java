package com.example.galax.weatherapp.services.weather_db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.services.weather_db.entities.WeatherEntity;
import com.example.galax.weatherapp.services.weather_db.entities.WeatherForecastEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface DaoAccess {
    @Insert
    void insertSingleWeather(WeatherEntity weatherEntity);

    @Query("SELECT * FROM WeatherEntity")
    Flowable<List<WeatherEntity>> getWeather();

    @Insert
    void insertSingleForecastWeather(WeatherForecastEntity weatherForecastEntity);

    @Query("SELECT * FROM WeatherForecastEntity")
    Flowable<List<WeatherForecastEntity>> getWeatherForecast();

    @Delete
    void deleteWeather(WeatherEntity weatherEntity);

    @Delete
    void deleteWeatherForecast(WeatherForecastEntity weatherForecastEntity);
}
