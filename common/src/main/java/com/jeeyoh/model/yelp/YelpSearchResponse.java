package com.jeeyoh.model.yelp;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpSearchResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long total;
	private List<YelpBusiness> businesses;
	private YelpRegion region;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<YelpBusiness> getBusinesses() {
		return businesses;
	}
	public void setBusinesses(List<YelpBusiness> businesses) {
		this.businesses = businesses;
	}
	public YelpRegion getRegion() {
		return region;
	}
	public void setRegion(YelpRegion region) {
		this.region = region;
	}

}
