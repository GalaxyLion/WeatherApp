package com.example.galax.weatherapp.data.models;

public class Weather {

    private double temp;
    private int humidity;
    private double pressure;
    private String country;
    private String description;
    private double windSpeed;
    private int conditionId;

    public Weather(double temp, int humidity, double pressure, String country, String description, double windSpeed, int conditionId) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.country = country;
        this.description = description;
        this.windSpeed = windSpeed;
        this.conditionId = conditionId;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }
}
