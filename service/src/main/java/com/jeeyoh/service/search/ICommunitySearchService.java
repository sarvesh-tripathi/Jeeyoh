package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.model.response.CommunityResponse;
import com.jeeyoh.model.search.PageModel;

public interface ICommunitySearchService {
	
	public List<PageModel> search(String userEmail);
	public CommunityResponse searchCommunityDetails(int userId, int pageId);

}
