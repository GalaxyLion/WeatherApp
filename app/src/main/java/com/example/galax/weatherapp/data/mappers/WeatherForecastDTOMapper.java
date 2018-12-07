package com.example.galax.weatherapp.data.mappers;

import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.rest.DTO.ResponseForecast;

public class WeatherForecastDTOMapper implements Mapper<ResponseForecast,WeatherForecast> {

    @Override
    public WeatherForecast from(ResponseForecast data) {
        return new WeatherForecast(
                data.getList().get(0).getMainForecast().getTemp(),
                data.getList().get(0).getWeather().get(0).getDescription()
        );
    }

    @Override
    public ResponseForecast to(WeatherForecast model) {
        return null;
    }
}
