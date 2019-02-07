package com.example.galax.weatherapp.services.weather_db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.services.weather_db.entities.WeatherEntity;
import com.example.galax.weatherapp.services.weather_db.entities.WeatherForecastEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface DaoAccess {
    @Insert
    void insertSingleWeather(WeatherEntity weatherEntity);

    @Query("SELECT * FROM WeatherEntity")
    Maybe<List<WeatherEntity>> getWeather();

    @Insert
    void insertSingleForecastWeather(WeatherForecastEntity weatherForecastEntity);

    @Query("SELECT * FROM WeatherForecastEntity")
    Flowable<List<WeatherForecastEntity>> getWeatherForecast();

    @Query("SELECT id FROM WeatherEntity WHERE city = :city LIMIT 1")
    Single<Long> getIdByCityName(String city);

    @Delete
    void deleteWeather(WeatherEntity weatherEntity);


    @Query("UPDATE WeatherEntity SET temp = :temp, description = :description, conditionId = :conditionId WHERE city = :city")
    void updateWeather2(double temp, String description, int conditionId, String city);

    @Delete
    void deleteWeatherForecast(WeatherForecastEntity weatherForecastEntity);
}
