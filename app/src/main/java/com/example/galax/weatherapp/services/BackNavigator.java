package com.example.galax.weatherapp.services;

public interface BackNavigator {
    void navigateBack();
    void tryExitActivity();
    void setCouldNavigateBack(boolean couldNavigateBack);
}
