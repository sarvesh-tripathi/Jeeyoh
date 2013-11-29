package com.jeeyoh.service.yelp;

import com.jeeyoh.model.yelp.YelpBusinessResponse;
import com.jeeyoh.model.yelp.YelpSearchResponse;

public interface IYelpClient {
	public YelpSearchResponse search(String location, String offset);
	public void searchBusiness();
	//public YelpSearchResponse search(String location);
	public YelpSearchResponse search(String location);
	YelpBusinessResponse business(String businessId);
}
