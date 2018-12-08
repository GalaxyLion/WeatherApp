package com.example.galax.weatherapp.screen;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.data.models.Weather;
import com.jakewharton.rxbinding2.widget.RxTextView;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class WeatherView implements WeatherContract.View {

    private View root;
    private EditText searchCity;
    private ImageView weatherIcon;
    private TextView weatherDescription;
    private TextView temperature;
    private View empty;
    private View progress;
    private View searchResult;

    private FrameLayout pressure;
    private FrameLayout humidity;
    private FrameLayout wind;

    private List<FrameLayout> weatherDays;
    private LayoutInflater inflater;


    private TextView textViewPressure;
    private TextView textViewHumidity;
    private TextView textViewWind;
    private List<WeatherDays> daysList;




    public WeatherView(View root) {
        this.root = root;
        initView();
    }


    private void initView() {
        inflater = LayoutInflater.from(root.getContext());
        searchCity = root.findViewById(R.id.search);
        weatherIcon = root.findViewById(R.id.image_weather);
        weatherDescription = root.findViewById(R.id.weather_description);
        temperature = root.findViewById(R.id.temperature);
        empty = root.findViewById(R.id.empty);
        progress = root.findViewById(R.id.progress);
        searchResult = root.findViewById(R.id.search_result);

        pressure = root.findViewById(R.id.pressure);
        humidity = root.findViewById(R.id.humidity);
        wind = root.findViewById(R.id.wind);

        weatherDays = new ArrayList<>();
        weatherDays.add(root.findViewById(R.id.first_day));
        weatherDays.add(root.findViewById(R.id.second_day));
        weatherDays.add(root.findViewById(R.id.third_day));
        weatherDays.add(root.findViewById(R.id.fourth_day));
        weatherDays.add(root.findViewById(R.id.fifth_day));
        daysList = new ArrayList<>();


        initIndicatorWeather(pressure);
        initIndicatorWeather(humidity);
        initIndicatorWeather(wind);
        initForecast();
        //setForecast();



    }

    private void initIndicatorWeather(FrameLayout layout){
        View indicatorsWeatherView = inflater.inflate(R.layout.weather_character, layout);
        ImageView imageViewIndicator = indicatorsWeatherView.findViewById(R.id.character_image);
        TextView textViewIndicator = indicatorsWeatherView.findViewById(R.id.character);
        if(layout == pressure) {
            imageViewIndicator.setImageResource(R.drawable.ic_pressure);
            textViewPressure = textViewIndicator;
        }
        if (layout == humidity){
            imageViewIndicator.setImageResource(R.drawable.ic_humidity);
            textViewHumidity = textViewIndicator;
        }
        if(layout == wind){
            imageViewIndicator.setImageResource(R.drawable.ic_wind);
            textViewWind = textViewIndicator;
        }

    }


    private void initForecast(){

        for (int i = 0; i <5 ; i++) {
            View daysView = inflater.inflate(R.layout.next_days, weatherDays.get(i));
            TextView dayOfWeek = daysView.findViewById(R.id.day);
            ImageView clouds = daysView.findViewById(R.id.cloud);
            TextView temperature = daysView.findViewById(R.id.temperature);
            daysList.add(new WeatherDays(dayOfWeek,clouds,temperature));
        }


    }

    public void setDaysWeather(int i, String days, int icon, String temp){
        daysList.get(i).getDay().setText(days);
        daysList.get(i).getIcon().setImageResource(icon);
        daysList.get(i).getTemp().setText(temp);
    }



    @Override
    public Observable<CharSequence> searchChanged() {
        return RxTextView.textChanges(searchCity);
    }


    @Override
    public void setPressure(String pressure){

        textViewPressure.setText(pressure + " hps");
    }
    @Override
    public void setHumidity(String humidity){

        textViewHumidity.setText(humidity + " %");
    }
    @Override
    public void setWind(String wind){

        textViewWind.setText(wind + " m/s");
    }

    @Override
    public void setWeatherIcon(int icon) {
        weatherIcon.setImageResource(icon);
    }

    @Override
    public void setWeather(Weather weather) {
        weatherDescription.setText(weather.getDescription().toString().toUpperCase());
        temperature.setText(Double.toString(weather.getTemp()) + " °C");//Double.toString(response.getMainForecast().getTemp()) + "C");
    }


    @Override
    public void showLoading(boolean show) {
        progress.setVisibility(show?View.VISIBLE:View.GONE);
    }

    @Override
    public void showEmpty(boolean show) {
        empty.setVisibility(show?View.VISIBLE:View.GONE);
    }
    @Override
    public void showResult(boolean show) {
        searchResult.setVisibility(show?View.VISIBLE:View.GONE);
    }
}

class WeatherDays{
    private TextView day;
    private ImageView icon;
    private TextView temp;

    public WeatherDays(TextView day, ImageView icon, TextView temp) {
        this.day = day;
        this.icon = icon;
        this.temp = temp;
    }

    public TextView getDay() {
        return day;
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getTemp() {
        return temp;
    }
}
