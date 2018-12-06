package com.example.galax.weatherapp.rest;

import com.example.galax.weatherapp.rest.DTO.oneDayDTO.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {


    @GET("?")
    Observable<Response> search(@Query("q") String city,
                                @Query("units") String units, @Query("appid")String appid);
}
