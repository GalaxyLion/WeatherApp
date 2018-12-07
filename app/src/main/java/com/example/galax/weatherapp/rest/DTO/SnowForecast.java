package com.example.galax.weatherapp.rest.DTO;

import com.google.gson.annotations.SerializedName;

public class SnowForecast {

	@SerializedName("3h")
	private double jsonMember3h;

	public void setJsonMember3h(double jsonMember3h){
		this.jsonMember3h = jsonMember3h;
	}

	public double getJsonMember3h(){
		return jsonMember3h;
	}

	@Override
 	public String toString(){
		return 
			"SnowForecast{" +
			"3h = '" + jsonMember3h + '\'' + 
			"}";
		}
}