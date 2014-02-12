package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.persistence.domain.Events;

public interface IEventsSuggestionSearch {

	public List<EventModel> search(String userEmail, String searchText, String category, String location);
	
}
