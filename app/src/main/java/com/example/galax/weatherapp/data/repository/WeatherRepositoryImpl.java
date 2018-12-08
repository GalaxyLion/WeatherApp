package com.example.galax.weatherapp.data.repository;

import com.example.galax.weatherapp.data.mappers.WeatherDTOMapper;
import com.example.galax.weatherapp.data.mappers.WeatherForecastDTOMapper;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.rest.RestApi;
import com.example.galax.weatherapp.rest.RestClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherRepositoryImpl implements WeatherRepository {
    private RestApi restApi;
    private WeatherDTOMapper weatherDTOMapper;
    private WeatherForecastDTOMapper weatherForecastDTOMapper;

    public WeatherRepositoryImpl() {
        restApi = RestClient.createApi();
        weatherDTOMapper = new WeatherDTOMapper();
        weatherForecastDTOMapper = new WeatherForecastDTOMapper();
    }

    public Observable<Weather> search(String query) {
        return restApi.search(query ,"metric","9c562b9ba5029c703df42710ce3ba3c6")
                .map(
                        it->{
                            Weather weather = null;
                            if(it.getWeather() != null){
                                weather = weatherDTOMapper.from(it);
                            }
                            return weather;
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WeatherForecast> searchForecast(String query) {
        return restApi.searchForecast(query, "metric", "c8278b9bc99b6c1052a68f933f4e14c6")
                .map(
                        it->{
                            WeatherForecast weatherForecast = null;
                            if (it.getList() != null){
                                weatherForecast = weatherForecastDTOMapper.from(it);
                            }
                            return weatherForecast;
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
