package com.example.galax.weatherapp.screen.weather;


import android.view.View;

import com.example.galax.weatherapp.base.BaseActivity;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.repository.WeatherLocalRepositoryImpl;
import com.example.galax.weatherapp.services.Navigator;

import java.util.List;

import io.reactivex.Observable;

public interface WeatherContract {
    interface View{
        void setPressure(String pressure);
        void setHumidity(String humidity);
        void setWind(String wind);
        void setDescription(String description);
        void setTemperature(String temperature);
        void setWeatherIcon(int icon);
        void showLoading(boolean show);
        void showEmpty(boolean show);
        void showResult(boolean show);
        void setDaysWeather(int i, String days, int icon, String temp);
        Observable <Object> settingsBtnAction();
        Observable <Object> addLocationBtnAction();
        void openDrawer();
        void showDialogAddLocation(boolean show);
        Observable<Object> addLocationDialogBtnAction();
        Observable<CharSequence> searchChangedDialog();
        void setWeatherList(List<Weather> items, WeatherLocalRepositoryImpl weatherLocalRepository);
        void addWeatherToList(List<Weather> weatherList);
        void closeDialog();
        void deleteItemCityDecorator();
        void showNotEqualCityToast();
        void clearCityText();
        void setEnabledDialogAddBtn(boolean enable);
        String getCity();
        void showExistCityToast();
        void updateList(List<Weather> weatherList);
        void setTitleToolbar(String title);
    }
    interface Presenter{
        void start(View view);
        void stop();
        void setNavigator(Navigator navigator);
    }


}
