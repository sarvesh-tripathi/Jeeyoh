package com.jeeyoh.service.groupon;

import java.util.HashMap;
import java.util.Map;

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

import com.jeeyoh.model.groupon.DivisionResponseModel;
import com.jeeyoh.model.groupon.ResponseModel;

@Component("grouponClient")
public class GrouponClient implements IGrouponClient {

	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	protected RestTemplate restTemplate;

	@Value("${groupon.client.id}")
	private String grouponClientId;

	@Value("${groupon.division.api.url}")
	private String grouponDivisionAPIUrl;

	@Value("${groupon.deals.api.url}")
	private String grouponDealsAPIUrl;

	@Value("${groupon.deals.bydivisionsid.api.url}")
	private String grouponDealsByDivisionAPIUrl;

	@Value("${groupon.deals.bylatlng.api.url}")
	private String grouponDealsByLatlngAPIUrl;

	@Value("${groupon.deals.isdefault.radius}")
	private boolean isDefaultRadius;

	@Value("${groupon.deals.radius.overridden.value}")
	private String overriddenRadius;

	public GrouponClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		MappingJacksonHttpMessageConverter converter = (MappingJacksonHttpMessageConverter) restTemplate
				.getMessageConverters().get(0);
		ObjectMapper mapper = converter.getObjectMapper();
		mapper
				.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	}

	@Override
	public ResponseModel getDeals() {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("clientId", grouponClientId);
		uriVariables.put("divisionId", "chicago");
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		// requestHeaders.add("X-STORISTIC-SESSION", xSessionId);
		HttpEntity requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<ResponseModel> response = restTemplate.exchange(
				grouponDealsAPIUrl, HttpMethod.GET, requestEntity,
				ResponseModel.class, uriVariables);
		if (response != null) {
			return response.getBody();
		}
		return null;
	}

	@Override
	public DivisionResponseModel getDivisions() {
		DivisionResponseModel divisions = null;
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("clientId", grouponClientId);
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		// requestHeaders.add("X-STORISTIC-SESSION", xSessionId);
		HttpEntity requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<DivisionResponseModel> response = restTemplate.exchange(
				grouponDivisionAPIUrl, HttpMethod.GET, requestEntity,
				DivisionResponseModel.class, uriVariables);
		if (response != null) {
			divisions = response.getBody();
		}
		return divisions;
	}

	@Override
	public ResponseModel getDealsByDivision(String divisionId) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("clientId", grouponClientId);
		uriVariables.put("divisionId", divisionId);
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		// requestHeaders.add("X-STORISTIC-SESSION", xSessionId);
		HttpEntity requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<ResponseModel> response = restTemplate.exchange(
				grouponDealsByDivisionAPIUrl, HttpMethod.GET, requestEntity,
				ResponseModel.class, uriVariables);
		if (response != null) {
			return response.getBody();
		}
		return null;
	}

	public ResponseModel getDealsByDivision(String longitude, String lattitude) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("clientId", grouponClientId);
		uriVariables.put("longitude", longitude);
		uriVariables.put("lattitude", lattitude);
		if (!isDefaultRadius) {
			uriVariables.put("radius", overriddenRadius);
		}
		MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
		// requestHeaders.add("X-STORISTIC-SESSION", xSessionId);
		HttpEntity requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<ResponseModel> response = restTemplate.exchange(
				grouponDealsByLatlngAPIUrl, HttpMethod.GET, requestEntity,
				ResponseModel.class, uriVariables);
		if (response != null) {
			return response.getBody();
		}
		return null;
	}

}
