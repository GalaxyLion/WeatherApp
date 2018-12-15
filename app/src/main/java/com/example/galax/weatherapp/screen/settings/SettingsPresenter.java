package com.example.galax.weatherapp.screen.settings;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;

import io.paperdb.Paper;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class SettingsPresenter implements SettingsContract.SettingsPresenter {

    SettingsContract.SettingsView view;
    private CompositeDisposable subscriptions;


    @Override
    public void start(SettingsContract.SettingsView view) {
        this.view = view;
        subscriptions = new CompositeDisposable();


        if (Paper.book().read("UNIT_TEMP") != null) {
            String unit = Paper.book().read("UNIT_TEMP");
            if (unit.equals(App.getInstance().getString(R.string.fahrenheit))) {
                view.setTempUnit(App.getInstance().getString(R.string.fahrenheit_str),
                        App.getInstance().getString(R.string.fahrenheit));
            } else {
                view.setTempUnit(App.getInstance().getString(R.string.celsius_str),
                        App.getInstance().getString(R.string.celsius));
            }
        }else {
            view.setTempUnit(App.getInstance().getString(R.string.celsius_str),
                    App.getInstance().getString(R.string.celsius));
        }

        subscriptions.add(view.temperatureUnitBtnAction().subscribe(
                o -> {
                    if (Paper.book().read("UNIT_TEMP") != null) {
                        String unit = Paper.book().read("UNIT_TEMP");
                        if (unit.equals(App.getInstance().getString(R.string.fahrenheit))) {
                            view.setTempUnit(App.getInstance().getString(R.string.celsius_str),
                                    App.getInstance().getString(R.string.celsius));
                            Paper.book().delete("UNIT_TEMP");
                            Paper.book().write("UNIT_TEMP", App.getInstance().getString(R.string.celsius));
                        }
                        if (unit.equals(App.getInstance().getString(R.string.celsius))) {
                            view.setTempUnit(App.getInstance().getString(R.string.fahrenheit_str),
                                    App.getInstance().getString(R.string.fahrenheit));
                            Paper.book().delete("UNIT_TEMP");
                            Paper.book().write("UNIT_TEMP", App.getInstance().getString(R.string.fahrenheit));
                        }
                    }else{
                        Paper.book().write("UNIT_TEMP", App.getInstance().getString(R.string.celsius));
                    }


                },
                e -> {
                    Timber.d("Error unit btn");
                }
        ));

        subscriptions.add(view.aboutBtnAction().subscribe(
                o -> {

                },
                e -> {
                    Timber.d("Error about btn");
                }
        ));
    }


    @Override
    public void stop() {

    }
}
