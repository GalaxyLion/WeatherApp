package com.example.galax.weatherapp.screen.weather;


import com.example.galax.weatherapp.base.BaseActivity;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.services.Navigator;

import java.util.List;

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
        Observable <Object> menuBtnAction();
        Observable <Object> addLocationBtnAction();
        void openDrawer();
        void showDialogAddLocation(boolean show);
        Observable<Object> addLocationDialogBtnAction();
        Observable<CharSequence> searchChangedDialog();

    }
    interface Presenter{
        void start(View view);
        void stop();
        void setNavigator(Navigator navigator);
    }

    interface DataBaseCallback{
        void onWeatherLoaded(List<Weather> weathers);

        void onWeatherDeleted();

        void onWeatherAdded();

        void onDataNotAvailable();

        void onWeatherUpdated();
    }
}
