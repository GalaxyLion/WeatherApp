package com.example.galax.weatherapp.data.repository;

import com.example.galax.weatherapp.data.models.Weather;

import io.reactivex.Observable;

public interface WeatherRepository {
  Observable<Weather> search(String query);
}
