package com.jeeyoh.service.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.domain.Events;

@Component("eventsSuggestionSearch")
public class EventsSuggestionSearch implements IEventsSuggestionSearch{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	private IEventsDAO eventsDAO;
	
	@Override
	@Transactional
	public List<Events> search(String userEmail, String searchText,
			String category, String location) {
		List<Events> eventsList = eventsDAO.getEventsByCriteria(userEmail, searchText, category, location);
		logger.debug("EventsList ==> "+eventsList.size());
		return eventsList;
	}

	
}
