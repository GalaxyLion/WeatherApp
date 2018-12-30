package com.example.galax.weatherapp.services.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DataIntegerTypeConverter {
    private static Gson gson = new Gson();
    @TypeConverter
    public static List<Integer> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Integer>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ListToString(List<Integer> someObjects) {
        return gson.toJson(someObjects);
    }
}
