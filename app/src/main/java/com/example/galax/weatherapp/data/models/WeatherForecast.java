package com.example.galax.weatherapp.data.models;

public class WeatherForecast {
    private double temp;
    private String description;

    public WeatherForecast(double temp, String description) {
        this.temp = temp;
        this.description = description;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
