package com.example.galax.weatherapp.rest.DTO;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ResponseForecast {

	@SerializedName("city")
	private CityForecast cityForecast;

	@SerializedName("cnt")
	private int cnt;

	@SerializedName("cod")
	private String cod;

	@SerializedName("message")
	private double message;

	@SerializedName("list")
	private List<ListItemForecast> list;

	public void setCityForecast(CityForecast cityForecast){
		this.cityForecast = cityForecast;
	}

	public CityForecast getCityForecast(){
		return cityForecast;
	}

	public void setCnt(int cnt){
		this.cnt = cnt;
	}

	public int getCnt(){
		return cnt;
	}

	public void setCod(String cod){
		this.cod = cod;
	}

	public String getCod(){
		return cod;
	}

	public void setMessage(double message){
		this.message = message;
	}

	public double getMessage(){
		return message;
	}

	public void setList(List<ListItemForecast> list){
		this.list = list;
	}

	public List<ListItemForecast> getList(){
		return list;
	}

	@Override
 	public String toString(){
		return 
			"ResponseForecast{" +
			"cityForecast = '" + cityForecast + '\'' +
			",cnt = '" + cnt + '\'' + 
			",cod = '" + cod + '\'' + 
			",message = '" + message + '\'' + 
			",list = '" + list + '\'' + 
			"}";
		}
}