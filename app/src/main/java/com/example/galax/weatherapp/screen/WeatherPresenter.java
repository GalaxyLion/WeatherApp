package com.example.galax.weatherapp.screen;

import android.util.Log;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.data.repository.WeatherRepository;
import com.example.galax.weatherapp.data.repository.WeatherRepositoryImpl;
import com.example.galax.weatherapp.services.Navigator;

import org.joda.time.DateTime;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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
                        if (!query.isEmpty()) {
                            view.showLoading(true);
                            repository.search(query)
                                    .subscribe(new Observer<Weather>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            subscriptions.add(d);
                                        }

                                        @Override
                                        public void onNext(Weather weather) {
                                            view.showLoading(false);
                                            if (weather != null) {
                                                if (weather.getCountry().isEmpty()) {
                                                    view.showEmpty(true);
                                                    view.showResult(false);
                                                } else {
                                                    view.showEmpty(false);
                                                    view.setPressure(Double.toString(weather.getPressure()));
                                                    view.setHumidity(Integer.toString(weather.getHumidity()));
                                                    view.setWind(Double.toString(weather.getWindSpeed()));
                                                    view.setWeatherIcon(setIconView(weather.getDescription()));
                                                    view.setWeather(weather);
                                                    view.showResult(true);
                                                }
                                            } else {
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
                            repository.searchForecast(query)
                                    .subscribe(new Observer<WeatherForecast>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            subscriptions.add(d);
                                        }

                                        @Override
                                        public void onNext(WeatherForecast weatherForecast) {
                                            view.showLoading(false);
                                            if (weatherForecast != null) {
                                                if (weatherForecast.getDescription().isEmpty()) {
                                                    view.showEmpty(true);
                                                    view.showResult(false);
                                                } else {
                                                    view.showEmpty(false);

                                                    DateTime dt = new DateTime();
                                                    GregorianCalendar gregorianCalendar = dt.toGregorianCalendar();
                                                    dt = new DateTime(gregorianCalendar);
                                                    DateTime.Property day = dt.dayOfWeek();
                                                    String today = day.getAsShortText(Locale.US);
                                                    for (int i = 1; i <= 5; i++) {
                                                        dt = dt.plusDays(1);
                                                        DateTime.Property days = dt.dayOfWeek();
                                                        String nextDay = days.getAsShortText(Locale.US);
                                                        view.setDaysWeather(i-1,nextDay,setIconView(weatherForecast.getDescription()),Double.toString(weatherForecast.getTemp()));
                                                        //Log.d("Days", nextDay);
                                                    }

                                                    view.showResult(true);
                                                }
                                            }else {
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
            return R.drawable.ic_snow;
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

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }
}
