package com.zai.weather.app.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zai.weather.app.model.WeatherReport;

@Service
public class OpenWeatherService {

	Logger logger = Logger.getLogger(WeatherStackService.class.getName()); 
	
	@Value("${open.weather.url}")
	private String openWeatherUrl;
	
	@Value("${open.weather.api.key}")
	private String openWeatherApiKey;
	
	private RestTemplate restTemplate;
	
    @Autowired
    public OpenWeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @CachePut(value = "weatherCache", unless = "#result == null || #result.windSpeed == null || #result.temperatureDegrees == null")
    public WeatherReport getWeatherReport() {
    	
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(openWeatherUrl)
			    .queryParam("q", "melbourne,AU")
			    .queryParam("appid", openWeatherApiKey);
		
		String url = builder.toUriString();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		WeatherReport weatherReport = new WeatherReport();
		
		try {
			
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			
			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			weatherReport.setWindSpeed(jsonNode.get("wind").get("speed").toPrettyString());
			weatherReport.setTemperatureDegrees(jsonNode.get("main").get("temp").toPrettyString());
			
			logger.info("OpenWeather API: " + new ObjectMapper().writeValueAsString(weatherReport));
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		} catch (HttpClientErrorException e) {
			
			logger.info("Error in calling Open Weather API: " + e.getLocalizedMessage());
			
		} catch (Exception e) {
			
			logger.info("Generic Error: " + e.getLocalizedMessage());
			
		}
		
		return weatherReport;
    	
    }
	
}
