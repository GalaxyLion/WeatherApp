package com.example.galax.weatherapp.rest.DTO;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class ListItemForecast {

	@SerializedName("dt")
	private int dt;

	@SerializedName("rain")
	private RainForecast rainForecast;

	@SerializedName("dt_txt")
	private String dtTxt;

	@SerializedName("snow")
	private SnowForecast snowForecast;

	@SerializedName("weather")
	private List<WeatherItem> weather;

	@SerializedName("main")
	private MainForecast mainForecast;

	@SerializedName("clouds")
	private Clouds clouds;

	@SerializedName("sys")
	private SysForecast sysForecast;

	@SerializedName("wind")
	private Wind wind;

	public void setDt(int dt){
		this.dt = dt;
	}

	public int getDt(){
		return dt;
	}

	public void setRainForecast(RainForecast rainForecast){
		this.rainForecast = rainForecast;
	}

	public RainForecast getRainForecast(){
		return rainForecast;
	}

	public void setDtTxt(String dtTxt){
		this.dtTxt = dtTxt;
	}

	public String getDtTxt(){
		return dtTxt;
	}

	public void setSnowForecast(SnowForecast snowForecast){
		this.snowForecast = snowForecast;
	}

	public SnowForecast getSnowForecast(){
		return snowForecast;
	}

	public void setWeather(List<WeatherItem> weather){
		this.weather = weather;
	}

	public List<WeatherItem> getWeather(){
		return weather;
	}

	public void setMainForecast(MainForecast mainForecast){
		this.mainForecast = mainForecast;
	}

	public MainForecast getMainForecast(){
		return mainForecast;
	}

	public void setClouds(Clouds clouds){
		this.clouds = clouds;
	}

	public Clouds getClouds(){
		return clouds;
	}

	public void setSysForecast(SysForecast sysForecast){
		this.sysForecast = sysForecast;
	}

	public SysForecast getSysForecast(){
		return sysForecast;
	}

	public void setWind(Wind wind){
		this.wind = wind;
	}

	public Wind getWind(){
		return wind;
	}

	@Override
 	public String toString(){
		return 
			"ListItemForecast{" +
			"dt = '" + dt + '\'' + 
			",rainForecast = '" + rainForecast + '\'' +
			",dt_txt = '" + dtTxt + '\'' + 
			",snowForecast = '" + snowForecast + '\'' +
			",weather = '" + weather + '\'' + 
			",mainForecast = '" + mainForecast + '\'' +
			",clouds = '" + clouds + '\'' + 
			",sysForecast = '" + sysForecast + '\'' +
			",wind = '" + wind + '\'' + 
			"}";
		}
}