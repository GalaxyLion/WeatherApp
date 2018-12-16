package com.example.galax.weatherapp.screen.weather;


import com.example.galax.weatherapp.services.Navigator;

import io.reactivex.Observable;

public interface WeatherContract {
    interface View{
        Observable<CharSequence> searchChanged();
        void setPressure(String pressure);
        void setHumidity(String humidity);
        void setWind(String wind);
        void setDescription(String description);
        void setTemperature(String temperature);
        void setWeatherIcon(int icon);
        void showLoading(boolean show);
        void showEmpty(boolean show);
        void showResult(boolean show);
        void showCitySearch(String city);
        void setDaysWeather(int i, String days, int icon, String temp);
        Observable <Object> settingsBtnAction();
    }
    interface Presenter{
        void start(View view);
        void stop();
        void setNavigator(Navigator navigator);
    }
}
