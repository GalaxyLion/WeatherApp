package com.example.galax.weatherapp.data.mappers;

import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.rest.DTO.ResponseForecast;

import java.util.ArrayList;

public class WeatherForecastDTOMapper implements Mapper<ResponseForecast,WeatherForecast> {

    @Override
    public WeatherForecast from(ResponseForecast data) {
        WeatherForecast weather =  new WeatherForecast(
                new ArrayList<>(),
                new ArrayList<>()

        );
        if (data.getList() != null && !data.getList().isEmpty()) {
            weather.transformTempAndDescription(data.getList());
        }


        return weather;
    }

    @Override
    public ResponseForecast to(WeatherForecast model) {
        return null;
    }
}
