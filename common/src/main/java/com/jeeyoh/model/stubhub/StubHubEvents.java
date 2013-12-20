package com.jeeyoh.model.stubhub;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class StubHubEvents  {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("responseHeader")
	private ResponseHeader responceHeader;
	
	@JsonProperty("response")
	private Responce responce;

	public ResponseHeader getResponceHeader() {
		return responceHeader;
	}

	public void setResponceHeader(ResponseHeader responceHeader) {
		this.responceHeader = responceHeader;
	}

	public Responce getResponce() {
		return responce;
	}

	public void setResponce(Responce responce) {
		this.responce = responce;
	}
	

}
