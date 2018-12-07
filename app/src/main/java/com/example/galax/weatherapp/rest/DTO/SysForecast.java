package com.example.galax.weatherapp.rest.DTO;

import com.google.gson.annotations.SerializedName;


public class SysForecast {

	@SerializedName("pod")
	private String pod;

	public void setPod(String pod){
		this.pod = pod;
	}

	public String getPod(){
		return pod;
	}

	@Override
 	public String toString(){
		return 
			"SysForecast{" +
			"pod = '" + pod + '\'' + 
			"}";
		}
}