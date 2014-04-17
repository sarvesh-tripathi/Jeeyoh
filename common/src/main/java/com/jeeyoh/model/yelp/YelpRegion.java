package com.jeeyoh.model.yelp;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpRegion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private YelpSpan span;
	
	private YelpCenter center;

	public YelpSpan getSpan() {
		return span;
	}

	public void setSpan(YelpSpan span) {
		this.span = span;
	}

	public YelpCenter getCenter() {
		return center;
	}

	public void setCenter(YelpCenter center) {
		this.center = center;
	}

}
