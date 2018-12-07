package com.example.galax.weatherapp.data.repository;

import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;

import io.reactivex.Observable;

public interface WeatherRepository {
  Observable<Weather> search(String query);
  Observable<WeatherForecast> searchForecast(String query);
}
