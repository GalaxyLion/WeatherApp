package com.example.galax.weatherapp.rest;

import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static RestApi createApi(){
        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(App.getInstance().getApplicationContext().getString(R.string.base_url));

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder newBuilder = request.newBuilder();
                        String token = "Token";
                        if (!token.isEmpty()) {
                            newBuilder.addHeader("Authorization", "Token " + token);
                        }
                        //TODO add actions to request
                        Request  newRequest = newBuilder.build();

                        Response response = null;
                        String newStringBody = "";
                        try {
                            response = chain.proceed(newRequest);
                            ResponseBody body = response.body();

                            String bodyString = body.string();

                            if (bodyString.startsWith("[")) {
                                newStringBody = "{\"data\":" + bodyString + "}";
                            }
                            else if (bodyString == null || bodyString.isEmpty()){
                                newStringBody = "{}";
                            }
                            else {
                                newStringBody = bodyString;
                            }

                            final Response.Builder newResponse = response.newBuilder()
                                    .body(ResponseBody.create(JSON, newStringBody));
                            response = newResponse.build();
                        } catch (IOException e) {
                            e.printStackTrace();
                            //TODO do all staff with broken internet connection
                        }
                        //TODO add actions with response
                        return response;
                    }
                })
                .build();
        builder.client(client);
        return builder.build().create(RestApi.class);
    }

}
