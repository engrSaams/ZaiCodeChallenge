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
public class WeatherStackService {

	Logger logger = Logger.getLogger(WeatherStackService.class.getName()); 
	
	@Value("${weather.stack.url}")
	private String weatherStackUrl;
	
	@Value("${weather.stack.api.key}")
	private String weatherStackApiKey;
	
	private RestTemplate restTemplate;
	
    @Autowired
    public WeatherStackService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
	
    @CachePut(value = "weatherCache", unless = "#result == null || #result.windSpeed == null || #result.temperatureDegrees == null")
	public WeatherReport getWeatherReport() {
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(weatherStackUrl)
			    .queryParam("access_key", weatherStackApiKey)
			    .queryParam("query", "Melbourne");
		
		String url = builder.toUriString();
		

		
		ObjectMapper objectMapper = new ObjectMapper();
		WeatherReport weatherReport = new WeatherReport();
		
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			
			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			weatherReport.setWindSpeed(jsonNode.get("current").get("wind_speed").toPrettyString());
			weatherReport.setTemperatureDegrees(jsonNode.get("current").get("temperature").toPrettyString());
			
			logger.info("WeatherStack API: " + new ObjectMapper().writeValueAsString(weatherReport));
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		} catch (HttpClientErrorException e) {
			
			logger.info("Error in calling Weather Stack API: " + e.getLocalizedMessage());
			
		} catch (Exception e) {
			
			logger.info("Generic Error: " + e.getLocalizedMessage());
			
		}

		
		return weatherReport;
	}
	
}
