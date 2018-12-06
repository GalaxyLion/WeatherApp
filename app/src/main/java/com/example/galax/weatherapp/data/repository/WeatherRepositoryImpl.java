package com.example.galax.weatherapp.data.repository;

import com.example.galax.weatherapp.data.mappers.WeatherDTOMapper;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.rest.RestApi;
import com.example.galax.weatherapp.rest.RestClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherRepositoryImpl implements WeatherRepository {
    private RestApi restApi;
    private WeatherDTOMapper weatherDTOMapper;

    public WeatherRepositoryImpl() {
        restApi = RestClient.createApi();
        weatherDTOMapper = new WeatherDTOMapper();
    }

    public Observable<Weather> search(String query) {
        return restApi.search(query ,"metric","appid")
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
}
