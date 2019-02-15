package com.example.galax.weatherapp.screen.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.data.repository.WeatherLocalRepositoryImpl;
import com.example.galax.weatherapp.data.repository.WeatherRepository;
import com.example.galax.weatherapp.data.repository.WeatherNetworkRepositoryImpl;
import com.example.galax.weatherapp.screen.weather.event.ClickCityEvent;
import com.example.galax.weatherapp.services.Navigator;
import com.example.galax.weatherapp.services.Screen;
import com.example.galax.weatherapp.services.ScreenType;
import com.example.galax.weatherapp.ui.WeatherIcon;
import com.example.galax.weatherapp.utils.Constants;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class WeatherPresenter implements WeatherContract.Presenter {

    private WeatherContract.View view;
    private WeatherRepository weatherNetworkRepository;
    private CompositeDisposable subscriptions;
    private Navigator navigator;
    private String units;
    private final Weather[] weather = new Weather[1];
    private final WeatherForecast[] weatherForecast = new WeatherForecast[1];
    private WeatherLocalRepositoryImpl weatherLocalRepository;
    private String query = "";



    @Override
    public void start(final WeatherContract.View view) {
        this.view = view;

        subscriptions = new CompositeDisposable();
        weatherNetworkRepository = new WeatherNetworkRepositoryImpl();
        weatherLocalRepository = new WeatherLocalRepositoryImpl();

        if (Paper.book().read(Constants.UNIT_TEMP) == null) {
            Paper.book().write(Constants.UNIT_TEMP, App.getInstance().getString(R.string.celsius));
            units = Paper.book().read(Constants.UNIT_TEMP);
        } else units = Paper.book().read(Constants.UNIT_TEMP);

        if(!getCityFromPaper().isEmpty()){
            showWeatherView(getCityFromPaper());
        }

        view.showEmpty(true);
        view.showResult(false);
        view.setEnabledDialogAddBtn(false);

        subscriptions.add(
                weatherLocalRepository.getWeather()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .flatMapIterable(weathers -> {
                            view.setWeatherList(weathers, weatherLocalRepository);
                            return weathers;})
                        .flatMap(weather ->
                                weatherNetworkRepository
                                        .search(weather.getCity())
                                        .flatMapCompletable(w -> weatherLocalRepository.updateWeather(
                                                w.getTemp(),
                                                w.getDescription(),
                                                w.getConditionId(),
                                                w.getCity())
                                        )
                                        .andThen(Observable.just(weather))

                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .toList()
                        .toObservable()
                        .flatMap(new Function<List<Weather>, ObservableSource<List<Weather>>>() {
                            @Override
                            public ObservableSource<List<Weather>> apply(List<Weather> weathers) {
                                return weatherLocalRepository.getWeather().toObservable();
                            }
                        })
                        .subscribe(view::updateList,
                                e->{
                                    view.showEmpty(false);
                                    view.showLoading(false);
                                    view.showCheckInternet(true);
                                })
        );




        subscriptions.add(view.settingsBtnAction().subscribe(
                o -> {
                    openSettings();
                }
        ));

        subscriptions.add(view.addLocationBtnAction().subscribe(
                o -> {
                    view.showDialogAddLocation(true);
                },
                e -> {
                    Timber.d(e.getMessage());
                }
        ));



        subscriptions.add(view.searchChangedDialog()
                .debounce(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<CharSequence, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(CharSequence charSequence) {
                        query = charSequence.toString().trim();
                        return getResultWeather(query);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {

                            view.setEnabledDialogAddBtn(result);

                            if (!view.getCity().isEmpty() && !result) {
                                view.showNotEqualCityToast();
                            }

                        },
                        e -> {
                            view.showLoading(false);
                            if (!view.getCity().isEmpty()) {
                                view.showNotEqualCityToast();
                            }
                        }
                ));


        subscriptions.add(view.addLocationDialogBtnAction().subscribe(
                o -> {
                    rewriteCityPaper(query);

                    weatherLocalRepository.getIdByCityName(weather[0].getCity()).subscribe(aLong -> {
                        view.showExistCityToast();
                        view.clearCityText();
                    }, throwable -> {
                        weatherLocalRepository.saveWeather(weather[0]).subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                subscriptions.add(weatherLocalRepository.getWeather().subscribe(new Consumer<List<Weather>>() {
                                    @Override
                                    public void accept(@NonNull List<Weather> weathers) {
                                        if (weathers != null) {
                                            view.addWeatherToList(weathers);

                                        }
                                    }
                                }));
                                Arrays.fill(weather, null);
                                Arrays.fill(weatherForecast, null);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });

                        view.closeDialog();
                        view.clearCityText();
                        view.setEnabledDialogAddBtn(false);

                    });

                },
                e -> {
                    Log.d("Error adding", e.getMessage());
                }
        ));

    }

    @Subscribe
    public void onEvent(ClickCityEvent clickCityEvent){
        rewriteCityPaper(clickCityEvent.getCity());
        showWeatherView(clickCityEvent.getCity());
    }

    private String getCityFromPaper(){
        String city = "";
        if(Paper.book().read(Constants.CITY) != null){
            city = Paper.book().read(Constants.CITY);
        }
        return city;
    }

    private void rewriteCityPaper(String city){
        if (Paper.book().read(Constants.CITY) == null || !Paper.book().read(Constants.CITY).equals(city)) {
            if (Paper.book().read(Constants.CITY) == null) {
                Paper.book().write(Constants.CITY, city);
            } else {
                Paper.book().delete(Constants.CITY);
                Paper.book().write(Constants.CITY, city);
            }
        }
    }

    private boolean isInternetAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;

    }

    private void showWeatherView(String  city) {
        if (!city.isEmpty() && isInternetAvailable()) {
            view.showLoading(true);
            subscriptions.add(getResultWeather(city)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).
                            subscribe(
                                    result -> {
                                        if (result) {
                                            view.showLoading(false);
                                            if (weather[0] != null && weatherForecast[0] != null) {
                                                if (weather[0].getCountry().isEmpty()) {
                                                    view.showEmpty(true);
                                                    view.showResult(false);
                                                } else {
                                                    view.showEmpty(false);
                                                    view.setTitleToolbar(weather[0].getCity());
                                                    view.setPressure(Double.toString(weather[0].getPressure()));
                                                    view.setHumidity(Integer.toString(weather[0].getHumidity()));
                                                    view.setWind(Double.toString(weather[0].getWindSpeed()));
                                                    view.setWeatherIcon(WeatherIcon.getIconView(weather[0].getConditionId()));
                                                    view.setTemperature(Double.toString(weather[0].getTemp()) + " " + units);
                                                    view.setDescription(weather[0].getDescription());


                                                    DateTime dt = new DateTime();
                                                    GregorianCalendar gregorianCalendar = dt.toGregorianCalendar();
                                                    dt = new DateTime(gregorianCalendar);
                                                    for (int i = 0; i < 5; i++) {
                                                        dt = dt.plusDays(1);
                                                        DateTime.Property days = dt.dayOfWeek();
                                                        String nextDay = days.getAsShortText(Locale.getDefault());
                                                        view.setDaysWeather(i,
                                                                nextDay,
                                                                WeatherIcon.getIconView(weatherForecast[0].getConditionId().get(i)),
                                                                Double.toString(weatherForecast[0].getTemp().get(i)) + " " + units);
                                                    }

                                                    view.showResult(true);
                                                    view.showCheckInternet(false);
                                                }
                                            } else {
                                                view.showEmpty(true);
                                                view.showResult(false);
                                            }
                                        } else {
                                            view.showEmpty(true);
                                            view.showResult(false);
                                        }

                                    },
                                    e -> {
                                        view.showLoading(false);
                                    }
                            ));

        } else {
            view.showCheckInternet(true);
            view.showResult(false);
            view.showLoading(false);
        }
    }

    private Observable<Boolean> getResultWeather(String query) {
        return Observable.combineLatest(weatherNetworkRepository.search(query), weatherNetworkRepository.searchForecast(query),
                (w, wf) -> {
                    weather[0] = w;
                    weatherForecast[0] = wf;
                    return w != null && wf != null;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> false);
    }

    private void openSettings() {
        if (navigator != null) {
            navigator.navigateTo(Screen.SETTINGS, ScreenType.ACTIVITY);
        }
    }


    @Override
    public void stop() {
        view.deleteItemCityDecorator();
        view = null;
        subscriptions.dispose();
        subscriptions = null;
    }

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }
}
