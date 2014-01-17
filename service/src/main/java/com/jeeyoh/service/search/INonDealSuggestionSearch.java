package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.persistence.domain.Business;

public interface INonDealSuggestionSearch {
	
	public List<Business> search(String userEmail, String searchText, String category, String location);
	

}
