package com.jeeyoh.model.stubhub;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Responce {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private String numFound;
	
	@JsonProperty
	private String start;
	
	private List<Description> docs;

	public String getNumFound() {
		return numFound;
	}

	public void setNumFound(String numFound) {
		this.numFound = numFound;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public List<Description> getDocs() {
		return docs;
	}

	public void setDocs(List<Description> docs) {
		this.docs = docs;
	}

}
