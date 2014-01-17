package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.persistence.domain.Page;

public interface ICommunitySearch {
	
	public List<Page> search(String userEmail);

}
