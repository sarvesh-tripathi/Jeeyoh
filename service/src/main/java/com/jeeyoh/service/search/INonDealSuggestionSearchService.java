package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.model.search.BusinessModel;

public interface INonDealSuggestionSearchService {
	
	public List<BusinessModel> search(String userEmail, String searchText, String category, String location, String rating);
	

}
