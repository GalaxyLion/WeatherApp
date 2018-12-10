package com.example.galax.weatherapp.data.models;


import com.example.galax.weatherapp.rest.DTO.ListItemForecast;

import org.joda.time.DateTime;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class WeatherForecast {
    private List<Double> temp;
    private List<String> description;

    public WeatherForecast(List<Double> temp, List<String> description) {
        this.temp = temp;
        this.description = description;
    }

    public List<Double> getTemp() {
        return temp;
    }

    public void setTemp(List<Double> temp) {
        this.temp = temp;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public void transformTempAndDescription(List<ListItemForecast> itemForecastList){
        if(itemForecastList!=null && !itemForecastList.isEmpty()){
            DateTime dt = new DateTime();
            GregorianCalendar gregorianCalendar = dt.toGregorianCalendar();
            dt = new DateTime(gregorianCalendar);
            int forecastItem = 0;
            dt = dt.plusDays(1);
            while (forecastItem!=itemForecastList.size()){
                String dayOfMonthForecast = itemForecastList.get(forecastItem).getDtTxt().substring(8,10);


                if(Integer.parseInt(dayOfMonthForecast) == dt.getDayOfMonth()){
                    dt = dt.plusDays(1);
                    temp.add(itemForecastList.get(forecastItem).getMainForecast().getTemp());
                    description.add(itemForecastList.get(forecastItem).getWeather().get(0).getDescription());
                }

                if(temp.size()<5 && description.size()<5 && forecastItem==itemForecastList.size()-1){
                    temp.add(itemForecastList.get(forecastItem).getMainForecast().getTemp());
                    description.add(itemForecastList.get(forecastItem).getWeather().get(0).getDescription());
                }


                forecastItem++;

            }


        }
    }
}
