package com.example.galax.weatherapp.screen.settings;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.base.BaseActivity;
import com.example.galax.weatherapp.utils.Constants;

import io.paperdb.Paper;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class SettingsPresenter implements SettingsContract.SettingsPresenter {

    SettingsContract.SettingsView view;
    private CompositeDisposable subscriptions;
    private BaseActivity activity;

    public SettingsPresenter(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void start(SettingsContract.SettingsView view) {
        this.view = view;
        subscriptions = new CompositeDisposable();


        if (Paper.book().read(Constants.UNIT_TEMP) != null) {
            String unit = Paper.book().read(Constants.UNIT_TEMP);
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
                    if (Paper.book().read(Constants.UNIT_TEMP) != null) {
                        String unit = Paper.book().read(Constants.UNIT_TEMP);
                        if (unit.equals(App.getInstance().getString(R.string.fahrenheit))) {
                            view.setTempUnit(App.getInstance().getString(R.string.celsius_str),
                                    App.getInstance().getString(R.string.celsius));
                            Paper.book().delete(Constants.UNIT_TEMP);
                            Paper.book().write(Constants.UNIT_TEMP, App.getInstance().getString(R.string.celsius));
                        }
                        if (unit.equals(App.getInstance().getString(R.string.celsius))) {
                            view.setTempUnit(App.getInstance().getString(R.string.fahrenheit_str),
                                    App.getInstance().getString(R.string.fahrenheit));
                            Paper.book().delete(Constants.UNIT_TEMP);
                            Paper.book().write(Constants.UNIT_TEMP, App.getInstance().getString(R.string.fahrenheit));
                        }
                    }else{
                        Paper.book().write(Constants.UNIT_TEMP, App.getInstance().getString(R.string.celsius));
                    }


                },
                e -> {
                    activity.showInfoDialog(e.getMessage());
                    Timber.d("Error unit btn");
                }
        ));

        subscriptions.add(view.aboutBtnAction().subscribe(
                o -> {
                    activity.showInfoDialog("Created by Yaroslav Zakharov");
                },
                e -> {
                    activity.showInfoDialog(e.getMessage());
                    Timber.d("Error about btn");
                }
        ));
    }


    @Override
    public void stop() {
        view = null;
        subscriptions.dispose();
        subscriptions = null;
    }
}
