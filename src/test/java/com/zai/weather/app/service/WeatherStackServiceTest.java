package com.zai.weather.app.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class WeatherStackServiceTest {

	@Autowired
	@InjectMocks
	private WeatherStackService weatherStackService;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Value("${weather.stack.url}")
	private String weatherStackUrl;
	
	@Test
	void test() {
		
		ResponseEntity<String> responseEntity = new ResponseEntity<>("Test", null, HttpStatus.OK);
		when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
		weatherStackService.getWeatherReport();
	}

}
