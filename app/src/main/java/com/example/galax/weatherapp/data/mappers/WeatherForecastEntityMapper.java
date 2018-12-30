package com.example.galax.weatherapp.data.mappers;

import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.services.weather_db.entities.WeatherForecastEntity;

public class WeatherForecastEntityMapper implements Mapper<WeatherForecastEntity, WeatherForecast> {
    @Override
    public WeatherForecast from(WeatherForecastEntity data) {
        return new WeatherForecast(
                data.getTemp(),
                data.getConditionId());
    }

    @Override
    public WeatherForecastEntity to(WeatherForecast model) {
        WeatherForecastEntity entity = new WeatherForecastEntity();
        entity.setTemp(model.getTemp());
        entity.setConditionId(model.getConditionId());
        return entity;
    }
}
