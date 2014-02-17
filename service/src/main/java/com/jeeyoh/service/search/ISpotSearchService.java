package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.model.search.SearchRequest;
import com.jeeyoh.model.search.SearchResult;

public interface ISpotSearchService {

	public List<SearchResult> search(SearchRequest searchRequest);
}
