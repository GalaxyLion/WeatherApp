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
    private FrameLayout firstWeatherDay;
    private FrameLayout secondWeatherDay;
    private FrameLayout thirdWeatherDay;
    private FrameLayout fourthWeatherDay;
    private FrameLayout fifthWeatherDay;


    private LayoutInflater inflater;


    private TextView textViewPressure;
    private TextView textViewHumidity;
    private TextView textViewWind;


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

        firstWeatherDay = root.findViewById(R.id.first_day);
        secondWeatherDay = root.findViewById(R.id.second_day);
        thirdWeatherDay = root.findViewById(R.id.third_day);
        fourthWeatherDay = root.findViewById(R.id.fourth_day);
        fifthWeatherDay = root.findViewById(R.id.fifth_day);


        setIndicatorWeather(pressure);
        setIndicatorWeather(humidity);
        setIndicatorWeather(wind);


    }

    private void setIndicatorWeather(FrameLayout layout){
        View characterView = inflater.inflate(R.layout.weather_character, layout);
        ImageView imageViewCharacter = characterView.findViewById(R.id.character_image);
        TextView textViewCharacter = characterView.findViewById(R.id.character);
        if(layout == pressure) {
            imageViewCharacter.setImageResource(R.drawable.ic_pressure);
            textViewPressure = textViewCharacter;
        }
        if (layout == humidity){
            imageViewCharacter.setImageResource(R.drawable.ic_humidity);
            textViewHumidity = textViewCharacter;
        }
        if(layout == wind){
            imageViewCharacter.setImageResource(R.drawable.ic_wind);
            textViewWind = textViewCharacter;
        }


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
        temperature.setText(Double.toString(weather.getTemp()) + " °C");//Double.toString(response.getMain().getTemp()) + "C");
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
