package com.example.galax.weatherapp;

import android.os.Bundle;

import com.example.galax.weatherapp.base.BaseActivity;
import com.example.galax.weatherapp.screen.weather.WeatherContract;
import com.example.galax.weatherapp.screen.weather.WeatherPresenter;
import com.example.galax.weatherapp.screen.weather.WeatherView;

import timber.log.Timber;


public class WeatherActivity extends BaseActivity {

    private WeatherContract.View view;
    private WeatherContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = new WeatherView(findViewById(R.id.content_frame));
        presenter = new WeatherPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setNavigator(getNavigator());
        presenter.start(view);

    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }
}
