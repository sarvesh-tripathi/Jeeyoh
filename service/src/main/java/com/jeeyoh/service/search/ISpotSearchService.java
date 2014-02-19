package com.jeeyoh.service.search;

import com.jeeyoh.model.response.SearchResponse;
import com.jeeyoh.model.search.SearchRequest;

public interface ISpotSearchService {

	public SearchResponse search(SearchRequest searchRequest);
}
