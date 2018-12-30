package com.example.galax.weatherapp.services.weather_db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.galax.weatherapp.services.converters.DataDoubleTypeConverter;
import com.example.galax.weatherapp.services.converters.DataIntegerTypeConverter;

import java.util.List;

import io.reactivex.annotations.NonNull;

@Entity
public class WeatherForecastEntity {


    @PrimaryKey
    @NonNull
    Long id;

    @TypeConverters(DataDoubleTypeConverter.class)
    private List<Double> temp;
    @TypeConverters(DataIntegerTypeConverter.class)
    private List<Integer> conditionId;

    public WeatherForecastEntity(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Double> getTemp() {
        return temp;
    }

    public void setTemp(List<Double> temp) {
        this.temp = temp;
    }

    public List<Integer> getConditionId() {
        return conditionId;
    }

    public void setConditionId(List<Integer> conditionId) {
        this.conditionId = conditionId;
    }

}
