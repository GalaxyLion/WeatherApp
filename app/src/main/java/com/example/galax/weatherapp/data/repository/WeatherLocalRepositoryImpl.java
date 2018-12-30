package com.example.galax.weatherapp.data.repository;

import android.arch.persistence.room.Room;

import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.data.mappers.WeatherForecastEntityMapper;
import com.example.galax.weatherapp.data.mappers.WeatherEntityMapper;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.services.weather_db.WeatherDB;
import com.example.galax.weatherapp.services.weather_db.entities.WeatherEntity;
import com.example.galax.weatherapp.services.weather_db.entities.WeatherForecastEntity;
import com.example.galax.weatherapp.utils.Constants;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherLocalRepositoryImpl implements WeatherRepository {

    private WeatherDB weatherDB;
    private WeatherEntityMapper weatherEntityMapper;
    private WeatherForecastEntityMapper weaherForecastEntityMapper;

    public WeatherLocalRepositoryImpl() {
        this.weatherDB = Room.databaseBuilder(App.getInstance().getApplicationContext(),
                WeatherDB.class, Constants.DATABASE_NAME).build();
        weatherEntityMapper = new WeatherEntityMapper();
        weaherForecastEntityMapper = new WeatherForecastEntityMapper();
    }

    @Override
    public Observable<Weather> search(String query) {
        return null;
    }

    @Override
    public Observable<WeatherForecast> searchForecast(String query) {
        return null;
    }

    @Override
    public Flowable<List<Weather>> getWeather() {
        return weatherDB.daoAccess().getWeather()
                .map(
                        it->{
                            List <Weather> weathers = new ArrayList<>();
                            for(WeatherEntity entity:it){
                                weathers.add(weatherEntityMapper.from(entity));
                            }
                            return weathers;
                        }
                ) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable saveWeather(Weather weather) {
        return Completable.fromAction(
                ()->{
                    weatherDB.daoAccess().insertSingleWeather(weatherEntityMapper.to(weather));
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable deleteWeather(Weather weather) {
        return Completable.fromAction(
                () -> weatherDB.daoAccess().deleteWeather(weatherEntityMapper.to(weather))
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<List<WeatherForecast>> getWeatherForecast() {
        return weatherDB.daoAccess().getWeatherForecast()
                .map(
                        it->{
                            List <WeatherForecast> weatherForecasts = new ArrayList<>();
                            for(WeatherForecastEntity entity :it){
                                weatherForecasts.add(weaherForecastEntityMapper.from(entity));
                            }
                            return weatherForecasts;
                        }
                ) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable saveWeatherForecast(WeatherForecast weatherForecast) {
        return Completable.fromAction(
                ()->{
                    weatherDB.daoAccess().insertSingleForecastWeather(weaherForecastEntityMapper.to(weatherForecast));
                }
        ) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable deleteWeatherForecast(WeatherForecast weatherForecast) {
        return Completable.fromAction(
                ()-> weatherDB.daoAccess().deleteWeatherForecast(weaherForecastEntityMapper.to(weatherForecast))
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
