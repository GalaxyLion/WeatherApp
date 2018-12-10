package com.example.galax.weatherapp.rest;

import com.example.galax.weatherapp.rest.DTO.ResponseForecast;
import com.example.galax.weatherapp.rest.DTO.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("weather?")
    Observable<Response> search(@Query("q") String city,
                                @Query("units") String units,
                                @Query("lang") String lang,
                                @Query("appid")String appid);
    @GET("forecast?")
    Observable<ResponseForecast> searchForecast(@Query("q") String city,
                                                @Query("units") String units,
                                                @Query("lang") String lang,
                                                @Query("appid")String appid);
}
