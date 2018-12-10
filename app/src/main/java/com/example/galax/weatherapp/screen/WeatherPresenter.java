package com.example.galax.weatherapp.screen;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.util.Log;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.data.repository.WeatherRepository;
import com.example.galax.weatherapp.data.repository.WeatherRepositoryImpl;
import com.example.galax.weatherapp.services.Navigator;
import com.example.galax.weatherapp.services.Screen;
import com.example.galax.weatherapp.services.ScreenType;

import org.joda.time.DateTime;
import org.reactivestreams.Subscription;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WeatherPresenter implements WeatherContract.Presenter {

    WeatherContract.View view;
    private WeatherRepository repository;
    private CompositeDisposable subscriptions;
    private Navigator navigator;


    @Override
    public void start(final WeatherContract.View view) {
        this.view = view;
        subscriptions = new CompositeDisposable();
        repository = new WeatherRepositoryImpl();
        Locale locale = new Locale(Locale.getDefault().getLanguage());
        Configuration config = App.getInstance().getResources().getConfiguration();
        config.locale = locale;
        App.getInstance().getResources().updateConfiguration(config, App.getInstance().getResources().getDisplayMetrics());


        view.showEmpty(true);
        view.showResult(false);
        view.searchChanged()
                .debounce(1000, TimeUnit.MILLISECONDS)
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        subscriptions.add(d);
                    }


                    @Override
                    public void onNext(CharSequence charSequence) {
                        String query = charSequence.toString().trim();
                        final Weather[] weather = new Weather[1];
                        final WeatherForecast[] weatherForecast = new WeatherForecast[1];
                        if (!query.isEmpty()) {
                            view.showLoading(true);
                            Disposable d = Observable.combineLatest(repository.search(query), repository.searchForecast(query),
                                    (io.reactivex.functions.BiFunction<Weather, WeatherForecast, Boolean>) (w, wf) -> {
                                weather[0] = w;
                                weatherForecast[0] = wf;
                                return w!=null && wf!=null;
                            }).subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread()).
                                    subscribe(
                                            result -> {
                                                if(result) {
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
                                                            view.setWeatherIcon(setIconView(weather[0].getDescription()));
                                                            view.setWeather(weather[0]);

                                                            DateTime dt = new DateTime();
                                                            GregorianCalendar gregorianCalendar = dt.toGregorianCalendar();
                                                            dt = new DateTime(gregorianCalendar);
                                                            for (int i = 0; i < 5; i++) {
                                                                dt = dt.plusDays(1);
                                                                DateTime.Property days = dt.dayOfWeek();
                                                                String nextDay = days.getAsShortText(Locale.getDefault());
                                                                view.setDaysWeather(i,
                                                                        nextDay,
                                                                        setIconView(weatherForecast[0].getDescription().get(i)),
                                                                        Double.toString(weatherForecast[0].getTemp().get(i)));
                                                            }

                                                            view.showResult(true);
                                                        }
                                                    } else {
                                                        view.showEmpty(true);
                                                        view.showResult(false);
                                                    }
                                                }else{
                                                    view.showEmpty(true);
                                                    view.showResult(false);
                                                }

                                            },
                                            e->{
                                                view.showLoading(false);
                                            }
                                    );
                            subscriptions.add(d);

                        }else{
                            view.showEmpty(true);
                            view.showResult(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                            view.showLoading(false);
                    }

                    @Override
                    public void onComplete() {

                    }

                });

        subscriptions.add(view.settingsBtnAction().subscribe(
                o->{
                    openSettings();
                }
        ));

    }

    private void openSettings(){
        if (navigator!=null) {
            navigator.navigateTo(Screen.SETTINGS,ScreenType.ACTIVITY);
        }
    }

    private int setIconView(String description){
        if (description.contains("thunderstorm") && description.contains("rain")){
            return R.drawable.ic_thunderstorm_with_rain;
        }
        if(description.contains("thunderstorm")){
            return R.drawable.ic_thunderstorm;
        }
        if(description.contains("rain")){
            return R.drawable.ic_rain;
        }
        if(description.contains("snow")){
            return R.drawable.ic_snowy;
        }
        if(description.contains("drizzle")){
            return R.drawable.ic_drizzle;
        }
        if(description.contains("clear")){
            return R.drawable.ic_clear;
        }
        if(description.equals("few clouds")){
            return R.drawable.ic_few_clouds;
        }
        if(description.equals("scattered clouds")){
            return R.drawable.ic_cloud;
        }
        if(description.equals("broken clouds") || description.equals("overcast clouds")){
            return R.drawable.ic_clouds;
        }

        return  R.drawable.ic_cloud;
    }

    @Override
    public void stop() {
        view = null;
        subscriptions.dispose();
        subscriptions = null;
    }

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }
}
