package com.example.galax.weatherapp.data.mappers;

import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.rest.DTO.oneDayDTO.Response;

public class WeatherDTOMapper implements Mapper<Response, Weather> {
    @Override
    public Weather from(Response data) {
        return new Weather(
                data.getMain().getTemp(),
                data.getMain().getHumidity(),
                data.getMain().getPressure(),
                data.getSys().getCountry(),
                data.getWeather().get(0).getDescription(),
                data.getWind().getSpeed()
        );
    }

    @Override
    public Response to(Weather model) {
        return null;
    }
}
