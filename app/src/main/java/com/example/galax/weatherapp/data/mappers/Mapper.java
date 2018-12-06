package com.example.galax.weatherapp.data.mappers;

public interface Mapper<F,M> {

    M from(F data);
    F to(M model);
}