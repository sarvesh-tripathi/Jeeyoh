package com.jeeyoh.service.livingsocial;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jeeyoh.model.livingsocial.CitiesModel;
import com.jeeyoh.model.livingsocial.LdealResponseModel;

@Component("livingSocialClient")
public class LivingSocialClient implements ILivingSocialClient{
	
	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	protected RestTemplate restTemplate;
	
	@Value("${livingsocial.api.key}")
	private String livingSocialApiKey;
	
	@Value("${livingsocial.deals.api.url}")
	private String livingSocialDealsAPIUrl;
	
	@Value("${livingsocial.cities.api.url}")
	private String livingSocialCitiesAPIUrl;
	
	
	public LivingSocialClient(RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
		MappingJacksonHttpMessageConverter converter = (MappingJacksonHttpMessageConverter) restTemplate
				.getMessageConverters().get(0);
		ObjectMapper mapper = converter.getObjectMapper();
		mapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	}
	@Override
	public CitiesModel[] getCities() {
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		HttpEntity requestEntity = new HttpEntity(requestHeaders);
		logger.debug("livingSocialCitiesAPIUrl =>"+livingSocialCitiesAPIUrl);
		CitiesModel[] response = restTemplate.getForObject(livingSocialCitiesAPIUrl, CitiesModel[].class);
		logger.debug("Length of the array :: "+response.length);
		//ResponseEntity<CitiesResponseModel> response = restTemplate.exchange(livingSocialCitiesAPIUrl, HttpMethod.GET, requestEntity,CitiesResponseModel.class,uriVariables);
		
		if (response != null) {
			for(CitiesModel cityModel:response)
			{
				logger.debug("LivingSocialClient ==> getCities ==>  not null = "+ cityModel);
				System.out.println("LivingSocialClient ==> getCities ==>  not null = "+ cityModel);
				return response;
			}		
		}
		return null;
	}
	
	@Override
	public LdealResponseModel getLDeals(String cityId) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("cityId", cityId);
		uriVariables.put("apiKey", livingSocialApiKey);
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		HttpEntity requestEntity = new HttpEntity(requestHeaders);
		logger.debug("livingSocialDealsAPIUrl =>"+livingSocialDealsAPIUrl);
		ResponseEntity<LdealResponseModel> response = restTemplate.exchange(livingSocialDealsAPIUrl, HttpMethod.GET, requestEntity,LdealResponseModel.class,uriVariables);
		if (response != null) {
				logger.debug("response ====>"+response.getBody());
				
				return response.getBody();		
		}
		return null;
	}
}
