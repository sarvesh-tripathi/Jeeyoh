package com.jeeyoh.model.stubhub;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class ResponseHeader {
	private static final long serialVersionUID = 1L;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getQTime() {
		return QTime;
	}
	public void setQTime(String time) {
		QTime = time;
	}
	@JsonProperty
	private String status;
	@JsonProperty
	private String QTime;

}
