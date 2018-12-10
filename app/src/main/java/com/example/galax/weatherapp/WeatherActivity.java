package com.example.galax.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.example.galax.weatherapp.base.BaseActivity;
import com.example.galax.weatherapp.screen.WeatherContract;
import com.example.galax.weatherapp.screen.WeatherPresenter;
import com.example.galax.weatherapp.screen.WeatherView;


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
        getBus().register(presenter);
        presenter.setNavigator(getNavigator());
        presenter.start(view);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
        getBus().unregister(presenter);
    }
}
