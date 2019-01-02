package com.example.galax.weatherapp.data.repository;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.data.mappers.WeatherDTOMapper;
import com.example.galax.weatherapp.data.mappers.WeatherForecastDTOMapper;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.rest.RestApi;
import com.example.galax.weatherapp.rest.RestClient;
import com.example.galax.weatherapp.utils.Constants;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherNetworkRepositoryImpl implements WeatherRepository {
    private RestApi restApi;
    private WeatherDTOMapper weatherDTOMapper;
    private WeatherForecastDTOMapper weatherForecastDTOMapper;
    String lang;

    public WeatherNetworkRepositoryImpl() {
        restApi = RestClient.createApi();
        weatherDTOMapper = new WeatherDTOMapper();
        weatherForecastDTOMapper = new WeatherForecastDTOMapper();
        lang = Locale.getDefault().getLanguage();
    }
    @Override
    public Observable<Weather> search(String query) {
        if(Paper.book().read(Constants.UNIT_TEMP)!=null) {
            String unit = Paper.book().read(Constants.UNIT_TEMP);
            if(unit.equals(App.getInstance().getString(R.string.celsius))){
                return changeUnit(query, "metric");
            }
            if(unit.equals(App.getInstance().getString(R.string.fahrenheit))){
                return changeUnit(query, "imperial");
            }

        }
            return changeUnit(query,"metric");

    }

    private Observable<Weather> changeUnit(String query, String units) {
        return restApi.search(query, units, lang, "9c562b9ba5029c703df42710ce3ba3c6")
                .map(
                        it -> {
                            Weather weather = null;
                            if (it.getWeather() != null) {
                                weather = weatherDTOMapper.from(it);
                                weather.setCity(query);
                            }
                            return weather;
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    @Override
    public Observable<WeatherForecast> searchForecast(String query) {
        if(Paper.book().read(Constants.UNIT_TEMP)!=null) {
            String unit = Paper.book().read(Constants.UNIT_TEMP);
            if(unit.equals(App.getInstance().getString(R.string.celsius))){
                return changeUnitForecast(query, "metric");
            }
            if(unit.equals(App.getInstance().getString(R.string.fahrenheit))){
                return changeUnitForecast(query, "imperial");
            }

        }

        return changeUnitForecast(query, "metric");
    }

    private Observable<WeatherForecast> changeUnitForecast(String query, String units) {
        return restApi.searchForecast(query, units, lang, "c8278b9bc99b6c1052a68f933f4e14c6")
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

    @Override
    public Flowable<List<Weather>> getWeather() {
        return null;
    }

    @Override
    public Completable saveWeather(Weather weather) {
        return Completable.complete();
    }

    @Override
    public Completable deleteWeather(Weather weather) {
        return Completable.complete();
    }

    @Override
    public Completable deleteWeatherForecast(WeatherForecast weatherForecast) {
        return Completable.complete();
    }

    @Override
    public Flowable<List<WeatherForecast>> getWeatherForecast() {
        return null;
    }

    @Override
    public Completable saveWeatherForecast(WeatherForecast weatherForecast) {
        return Completable.complete();
    }
}
