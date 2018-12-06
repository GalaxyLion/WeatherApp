package com.example.galax.weatherapp.screen;

import com.example.galax.weatherapp.data.models.Weather;

import io.reactivex.Observable;

public interface WeatherContract {
    interface View{
        Observable<CharSequence> searchChanged();
        void setPressure(String pressure);
        void setHumidity(String humidity);
        void setWind(String wind);
        void setWeather(Weather weather);
        void setWeatherIcon(int icon);
        void showLoading(boolean show);
        void showEmpty(boolean show);
        void showResult(boolean show);
    }
    interface Presenter{
        void start(View view);
        void stop();
    }
}
