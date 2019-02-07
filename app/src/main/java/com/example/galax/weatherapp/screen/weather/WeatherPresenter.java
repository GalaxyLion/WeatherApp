package com.example.galax.weatherapp.screen.weather;

import android.util.Log;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.data.repository.WeatherLocalRepositoryImpl;
import com.example.galax.weatherapp.data.repository.WeatherRepository;
import com.example.galax.weatherapp.data.repository.WeatherNetworkRepositoryImpl;
import com.example.galax.weatherapp.services.Navigator;
import com.example.galax.weatherapp.services.Screen;
import com.example.galax.weatherapp.services.ScreenType;
import com.example.galax.weatherapp.utils.Constants;

import org.joda.time.DateTime;
import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
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
                        .observeOn(AndroidSchedulers.mainThread()) // = AndroidSchedulers.mainThread()
                        .toList()
                        .toObservable()
                        .flatMap(new Function<List<Weather>, ObservableSource<List<Weather>>>() {
                            @Override
                            public ObservableSource<List<Weather>> apply(List<Weather> weathers) throws Exception {
                                return weatherLocalRepository.getWeather().toObservable();
                            }
                        })
                        .subscribe(view::updateList)
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
                    public ObservableSource<Boolean> apply(CharSequence charSequence) throws Exception {
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
                    if (Paper.book().read(Constants.CITY) == null || !Paper.book().read(Constants.CITY).equals(query)) {
                        if (Paper.book().read(Constants.CITY) == null) {
                            Paper.book().write(Constants.CITY, query);
                        } else {
                            Paper.book().delete(Constants.CITY);
                            Paper.book().write(Constants.CITY, query);
                        }
                    }

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
                                    public void accept(@NonNull List<Weather> weathers) throws Exception {
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


    private void showWeatherView(String query) {

        if (!query.isEmpty()) {
            view.showLoading(true);
            subscriptions.add(getResultWeather(query)
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
                                                    view.setPressure(Double.toString(weather[0].getPressure()));
                                                    view.setHumidity(Integer.toString(weather[0].getHumidity()));
                                                    view.setWind(Double.toString(weather[0].getWindSpeed()));
                                                    view.setWeatherIcon(setIconView(weather[0].getConditionId()));
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
                                                                setIconView(weatherForecast[0].getConditionId().get(i)),
                                                                Double.toString(weatherForecast[0].getTemp().get(i)) + " " + units);
                                                    }

                                                    view.showResult(true);
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
            view.showEmpty(true);
            view.showResult(false);
        }
    }

    private Observable<Boolean> getResultWeather(String query) {
        return Observable.combineLatest(weatherNetworkRepository.search(query), weatherNetworkRepository.searchForecast(query),
                (io.reactivex.functions.BiFunction<Weather, WeatherForecast, Boolean>) (w, wf) -> {
                    weather[0] = w;
                    weatherForecast[0] = wf;
                    return w != null && wf != null;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, Boolean>() {
                    @Override
                    public Boolean apply(Throwable throwable) throws Exception {
                        return false;
                    }
                });
    }

    private void openSettings() {
        if (navigator != null) {
            navigator.navigateTo(Screen.SETTINGS, ScreenType.ACTIVITY);
        }
    }

    private int setIconView(int conditionId) {

        if (conditionId >= 200 && conditionId < 210) {
            return R.drawable.ic_thunderstorm_with_rain;
        }
        if (conditionId >= 210 && conditionId < 300) {
            return R.drawable.ic_thunderstorm;
        }
        if (conditionId >= 500 && conditionId < 600) {
            return R.drawable.ic_rain;
        }
        if (conditionId >= 600 && conditionId < 700) {
            return R.drawable.ic_snowy;
        }
        if (conditionId >= 300 && conditionId < 500) {
            return R.drawable.ic_drizzle;
        }
        if (conditionId >= 701 && conditionId < 800) {
            return R.drawable.ic_fog;
        }
        if (conditionId == 800) {
            return R.drawable.ic_clear;
        }
        if (conditionId == 801) {
            return R.drawable.ic_few_clouds;
        }
        if (conditionId == 802) {
            return R.drawable.ic_cloud;
        }
        if (conditionId >= 803) {
            return R.drawable.ic_clouds;
        }

        return R.drawable.ic_error;
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
