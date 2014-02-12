package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.model.search.PageModel;

public interface ICommunitySearch {
	
	public List<PageModel> search(String userEmail);

}
