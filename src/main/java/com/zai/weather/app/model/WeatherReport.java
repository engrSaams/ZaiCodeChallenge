package com.zai.weather.app.model;

import org.springframework.stereotype.Component;

@Component
public class WeatherReport {

	private String windSpeed;
	private String temperatureDegrees;
	
	public String getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}
	public String getTemperatureDegrees() {
		return temperatureDegrees;
	}
	public void setTemperatureDegrees(String temperatureDegrees) {
		this.temperatureDegrees = temperatureDegrees;
	}

	
}
