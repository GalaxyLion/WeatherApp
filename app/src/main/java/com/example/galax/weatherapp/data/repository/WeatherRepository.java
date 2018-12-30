package com.example.galax.weatherapp.data.repository;

import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface WeatherRepository {
  Observable<Weather> search(String query);
  Observable<WeatherForecast> searchForecast(String query);
  Flowable<List<Weather>> getWeather();
  Completable saveWeather (Weather weather);
  Completable deleteWeather(Weather weather);
  Flowable<List<WeatherForecast>> getWeatherForecast();
  Completable saveWeatherForecast (WeatherForecast weatherForecast);
  Completable deleteWeatherForecast(WeatherForecast weatherForecast);
}
