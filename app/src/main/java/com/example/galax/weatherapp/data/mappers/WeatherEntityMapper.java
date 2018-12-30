package com.example.galax.weatherapp.data.mappers;

import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.services.weather_db.entities.WeatherEntity;

public class WeatherEntityMapper implements Mapper<WeatherEntity,Weather> {
    @Override
    public Weather from(WeatherEntity data) {
        return new Weather(
                data.getTemp(),
                data.getHumidity(),
                data.getPressure(),
                data.getCountry(),
                data.getDescription(),
                data.getWindSpeed(),
                data.getConditionId()
        );
    }

    @Override
    public WeatherEntity to(Weather model) {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setTemp(model.getTemp());
        weatherEntity.setHumidity(model.getHumidity());
        weatherEntity.setPressure(model.getPressure());
        weatherEntity.setCountry(model.getCountry());
        weatherEntity.setDescription(model.getDescription());
        weatherEntity.setWindSpeed(model.getWindSpeed());
        weatherEntity.setConditionId(model.getConditionId());
        return weatherEntity;
    }
}
