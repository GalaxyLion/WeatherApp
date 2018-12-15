package com.example.galax.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.galax.weatherapp.screen.settings.SettingsContract;
import com.example.galax.weatherapp.screen.settings.SettingsPresenter;
import com.example.galax.weatherapp.screen.settings.SettingsView;

import timber.log.Timber;

public class SettingsActivity extends AppCompatActivity {

    private SettingsContract.SettingsView view;
    private SettingsContract.SettingsPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        view = new SettingsView(findViewById(R.id.content_frame));
        presenter = new SettingsPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.start(view);
        Timber.d("ON_START");
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();

        Timber.d("ON_STOP");
    }
}
