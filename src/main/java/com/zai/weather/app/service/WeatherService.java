package com.zai.weather.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zai.weather.app.model.WeatherReport;

@Service
public class WeatherService {

	private WeatherStackService weatherStackService;
	private OpenWeatherService openWeatherService;
	
    @Autowired
    public WeatherService(WeatherStackService weatherStackService, OpenWeatherService openWeatherService) {
        this.weatherStackService = weatherStackService;
        this.openWeatherService = openWeatherService;
    }
    
    @Cacheable(value = "weatherCache", unless = "#result == null || #result.windSpeed == null || #result.temperatureDegrees == null")
    public WeatherReport getWeatherReport() {

    	WeatherReport weatherReport = weatherStackService.getWeatherReport();
    	
    	if(hasNull(weatherReport)) {
    		
    		weatherReport = openWeatherService.getWeatherReport();
    		
    	}
    	
    	return weatherReport;

    }
    
    // Checks if WeatherReport is null
    private boolean hasNull(WeatherReport weatherReport) {
    	
    	if (weatherReport.getWindSpeed() == null ) {
    		return true;
    	}
    	
    	if (weatherReport.getTemperatureDegrees() == null) {
    		return true;
    	}
    	
    	return false;
    	
    }
	
}
