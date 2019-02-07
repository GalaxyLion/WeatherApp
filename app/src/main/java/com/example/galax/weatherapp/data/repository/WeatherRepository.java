package com.example.galax.weatherapp.data.repository;

import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface WeatherRepository {
  Observable<Weather> search(String query);
  Observable<WeatherForecast> searchForecast(String query);
  Maybe<List<Weather>> getWeather();
  Completable saveWeather (Weather weather);
  Completable deleteWeather(Weather weather);
  Single<Long> getIdByCityName(String city);
  Completable updateWeather(double temp, String description, int conditionId,  String city);

  Flowable<List<WeatherForecast>> getWeatherForecast();
  Completable saveWeatherForecast (WeatherForecast weatherForecast);
  Completable deleteWeatherForecast(WeatherForecast weatherForecast);
}
