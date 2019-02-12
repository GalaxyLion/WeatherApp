package com.example.galax.weatherapp.screen.weather.event;

public class ClickCityEvent {
    private String city;

    public ClickCityEvent(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

}
