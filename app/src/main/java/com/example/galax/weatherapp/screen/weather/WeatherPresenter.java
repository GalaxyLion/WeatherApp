package com.example.galax.weatherapp.screen.weather;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.data.repository.WeatherRepository;
import com.example.galax.weatherapp.data.repository.WeatherRepositoryImpl;
import com.example.galax.weatherapp.services.Navigator;
import com.example.galax.weatherapp.services.Screen;
import com.example.galax.weatherapp.services.ScreenType;
import com.example.galax.weatherapp.utils.Constants;

import org.joda.time.DateTime;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
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
    private String units;


    @Override
    public void start(final WeatherContract.View view) {
        this.view = view;
        subscriptions = new CompositeDisposable();
        repository = new WeatherRepositoryImpl();
        if(Paper.book().read(Constants.UNIT_TEMP) == null) {
            Paper.book().write(Constants.UNIT_TEMP, App.getInstance().getString(R.string.celsius));
        }else units = Paper.book().read(Constants.UNIT_TEMP);

        if(Paper.book().read(Constants.CITY)!=null) {
            createWeatherView(Paper.book().read(Constants.CITY));
            view.showCitySearch(Paper.book().read(Constants.CITY));
        }



        view.showEmpty(true);
        view.showResult(false);
        subscriptions.add(view.searchChanged()
                .debounce(1000, TimeUnit.MILLISECONDS)
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence ->  {
                        String query = charSequence.toString().trim();
                        if(Paper.book().read(Constants.CITY)==null || !Paper.book().read(Constants.CITY).equals(query)){
                            if(Paper.book().read(Constants.CITY)==null){
                                Paper.book().write(Constants.CITY,query);
                            }else {
                                Paper.book().delete(Constants.CITY);
                                Paper.book().write(Constants.CITY,query);
                            }
                        }

                    createWeatherView(Paper.book().read(Constants.CITY));

                    },e->{
                            view.showLoading(false);
                    }

                ));

        subscriptions.add(view.settingsBtnAction().subscribe(
                o->{
                    openSettings();
                }
        ));

    }

    private void createWeatherView(String query) {
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
                                                                Double.toString(weatherForecast[0].getTemp().get(i))+ " " + units);
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

    private void openSettings(){
        if (navigator!=null) {
            navigator.navigateTo(Screen.SETTINGS,ScreenType.ACTIVITY);
        }
    }

    private int setIconView(int conditionId){

        if (conditionId>=200 && conditionId<210){
            return R.drawable.ic_thunderstorm_with_rain;
        }
        if(conditionId>=210 && conditionId<300){
            return R.drawable.ic_thunderstorm;
        }
        if(conditionId>=500 && conditionId<600){
            return R.drawable.ic_rain;
        }
        if(conditionId>=600 && conditionId<700){
            return R.drawable.ic_snowy;
        }
        if(conditionId>=300 && conditionId<500){
            return R.drawable.ic_drizzle;
        }
        if(conditionId>=701 && conditionId<800){
            return R.drawable.ic_fog;
        }
        if(conditionId==800){
            return R.drawable.ic_clear;
        }
        if(conditionId==801){
            return R.drawable.ic_few_clouds;
        }
        if(conditionId==802){
            return R.drawable.ic_cloud;
        }
        if(conditionId>=803){
            return R.drawable.ic_clouds;
        }

        return  R.drawable.ic_error;
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
