package com.example.galax.weatherapp.screen.weather;


import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.base.BaseActivity;
import com.example.galax.weatherapp.base.dialogs.events.HideDialogEvent;
import com.example.galax.weatherapp.base.dialogs.events.ShowDialogEvent;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherDays;
import com.example.galax.weatherapp.data.repository.WeatherLocalRepositoryImpl;
import com.example.galax.weatherapp.ui.ItemDecorator;
import com.jakewharton.rxbinding2.view.RxMenuItem;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class WeatherView implements WeatherContract.View {


    private BaseActivity activity;
    private View root;
    private ImageView weatherIcon;
    private TextView weatherDescription;
    private TextView temperatureText;
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

    private View addLocationBtn;
    private View dialogLocationView;
    private View addLocationDialogBtn;
    private EditText searchCityDialog;

    private WeatherAdapter weatherAdapter;
    private RecyclerView cities;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ItemDecorator itemDecorator;


    public WeatherView(View root, DrawerLayout drawer, BaseActivity activity) {
        this.root = root;
        this.drawer = drawer;
        this.activity = activity;
        initView();
    }


    private void initView() {
        inflater = LayoutInflater.from(root.getContext());
        weatherIcon = root.findViewById(R.id.image_weather);
        weatherDescription = root.findViewById(R.id.weather_description);
        temperatureText = root.findViewById(R.id.temperature);
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

        addLocationBtn = drawer.findViewById(R.id.add_location_btn);

        cities = drawer.findViewById(R.id.cities);

        initIndicatorWeather(pressure);
        initIndicatorWeather(humidity);
        initIndicatorWeather(wind);
        initForecast();
        initDialogAddLocation();

        toolbar = root.findViewById(R.id.toolbar);
        itemDecorator = new ItemDecorator(activity.getApplicationContext(), R.dimen.item_offset);
        initToolbar();
        //activity.getMenuInflater().inflate();

    }


    private void initIndicatorWeather(FrameLayout layout) {
        View indicatorsWeatherView = inflater.inflate(R.layout.weather_character, layout);
        ImageView imageViewIndicator = indicatorsWeatherView.findViewById(R.id.character_image);
        TextView textViewIndicator = indicatorsWeatherView.findViewById(R.id.character);
        if (layout == pressure) {
            imageViewIndicator.setImageResource(R.drawable.ic_pressure);
            textViewPressure = textViewIndicator;
        }
        if (layout == humidity) {
            imageViewIndicator.setImageResource(R.drawable.ic_humidity);
            textViewHumidity = textViewIndicator;
        }
        if (layout == wind) {
            imageViewIndicator.setImageResource(R.drawable.ic_wind);
            textViewWind = textViewIndicator;
        }

    }


    private void initToolbar() {

        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(v -> {
            openDrawer();
        });

    }

    @Override
    public Observable<Object> settingsBtnAction() {
        return RxMenuItem.clicks(toolbar.getMenu().findItem(R.id.action_settings));
    }

    private void initForecast() {

        for (int i = 0; i < 5; i++) {
            View daysView = inflater.inflate(R.layout.next_days, weatherDays.get(i));
            TextView dayOfWeek = daysView.findViewById(R.id.day);
            ImageView clouds = daysView.findViewById(R.id.cloud);
            TextView temperature = daysView.findViewById(R.id.temperature);
            daysList.add(new WeatherDays(dayOfWeek, clouds, temperature));
        }


    }


    @Override
    public void setDaysWeather(int i, String days, int icon, String temp) {
        daysList.get(i).getDay().setText(days.toUpperCase());
        daysList.get(i).getIcon().setImageResource(icon);
        daysList.get(i).getTemp().setText(temp);
    }



    @Override
    public void setPressure(String pressure) {
        String s = pressure + " " + App.getInstance().getString(R.string.hectopascal);

        textViewPressure.setText(s);
    }

    @Override
    public void setHumidity(String humidity) {
        textViewHumidity.setText(humidity + " %");
    }

    @Override
    public void setWind(String wind) {
        String s = wind + " " + App.getInstance().getString(R.string.metr_seconds);
        textViewWind.setText(s);
    }

    @Override
    public void setWeatherIcon(int icon) {
        weatherIcon.setImageResource(icon);
    }

    @Override
    public void setDescription(String description) {
        weatherDescription.setText(description.toUpperCase());
    }

    @Override
    public void setTemperature(String temperature) {
        temperatureText.setText(temperature);
    }

    @Override
    public void showLoading(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmpty(boolean show) {
        empty.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showResult(boolean show) {
        searchResult.setVisibility(show ? View.VISIBLE : View.GONE);
    }



       @Override
    public Observable<Object> addLocationBtnAction() {
        return RxView.clicks(addLocationBtn);
    }

    @Override
    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void showDialogAddLocation(boolean show) {
        dialogLocationView.setVisibility(show?View.VISIBLE:View.GONE);
        activity.getBus().post(new ShowDialogEvent(dialogLocationView));
    }

    private void initDialogAddLocation() {
        dialogLocationView = inflater.inflate(R.layout.add_city_dialog, null);
        View cancelBtn = dialogLocationView.findViewById(R.id.cancel_btn);
        addLocationDialogBtn = dialogLocationView.findViewById(R.id.add_btn);
        searchCityDialog = dialogLocationView.findViewById(R.id.search);
        cancelBtn.setOnClickListener(v -> closeDialog());
        //addLocationDialogBtn.setOnClickListener(v -> activity.getBus().post(new HideDialogEvent()));
        showDialogAddLocation(false);

    }

    @Override
    public void closeDialog(){
        activity.getBus().post(new HideDialogEvent());
    }

    @Override
    public Observable<Object> addLocationDialogBtnAction() {
        return RxView.clicks(addLocationDialogBtn);
    }

    @Override
    public Observable<CharSequence> searchChangedDialog() {
        return RxTextView.textChanges(searchCityDialog);
    }

    @Override
    public void setWeatherList(List<Weather> items, WeatherLocalRepositoryImpl weatherLocalRepository) {
        LinearLayoutManager llm = new LinearLayoutManager(drawer.getContext(),LinearLayoutManager.VERTICAL,false);
        weatherAdapter = new WeatherAdapter(items, weatherLocalRepository);
        cities.setLayoutManager(llm);
        cities.addItemDecoration(itemDecorator);
        cities.setAdapter(weatherAdapter);
    }

    @Override
    public void addWeatherToList(List<Weather> weatherList){
        weatherAdapter.notifyData(weatherList);
        weatherAdapter.notifyItemInserted(weatherList.size()-1);
        weatherAdapter.notifyItemRangeChanged(weatherList.size()-1,weatherList.size());
    }

    @Override
    public void deleteItemCityDecorator(){
        cities.removeItemDecoration(itemDecorator);
    }

    @Override
    public void showNotEqualCityToast(){
            Toast.makeText(activity.getApplicationContext(), App.getInstance().getString(R.string.not_exist_city), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showExistCityToast(){
        Toast.makeText(activity.getApplicationContext(), App.getInstance().getString(R.string.exist_city), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearCityText() {
        searchCityDialog.setText("");
    }

    @Override
    public void setEnabledDialogAddBtn(boolean enable){
        addLocationDialogBtn.setEnabled(enable);
    }

    @Override
    public String getCity(){
        return searchCityDialog.getText().toString().trim();
    }

    @Override
    public void updateList(List<Weather> weathers) {
        weatherAdapter.notifyData(weathers);
    }
}


