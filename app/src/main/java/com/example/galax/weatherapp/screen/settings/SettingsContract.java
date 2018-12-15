package com.example.galax.weatherapp.screen.settings;

import io.reactivex.Observable;

public interface SettingsContract {
    interface SettingsView{
        Observable<Object> temperatureUnitBtnAction();
        Observable<Object> aboutBtnAction();
        void setTempUnit(String unit, String subUnit);
    }
    interface SettingsPresenter{
        void start(SettingsView view);
        void stop();
    }
}
