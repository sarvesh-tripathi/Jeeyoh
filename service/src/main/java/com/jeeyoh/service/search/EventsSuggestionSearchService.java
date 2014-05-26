package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Usereventsuggestion;

@Component("eventsSuggestionSearch")
public class EventsSuggestionSearchService implements IEventsSuggestionSearchService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	private IEventsDAO eventsDAO;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<EventModel> search(String userEmail, String searchText,
			String category, String location) {
		List<Events> eventsList = eventsDAO.getEventsByCriteria(userEmail, searchText, category, location);
		logger.debug("EventsList ==> "+eventsList.size());
		List<EventModel> eventsModelList = new ArrayList<EventModel>();
		for(Events events : eventsList)
		{
			EventModel eventModel = new EventModel();
			eventModel.setDescription(events.getDescription());
			eventModel.setVenue_name(events.getVenue_name());
			eventModel.setEvent_date(events.getEvent_date_time_local().toString());
			eventModel.setCity(events.getCity());
			eventModel.setTitle(events.getTitle());
			eventModel.setTotalTickets(events.getTotalTickets());
			eventModel.setAncestorGenreDescriptions(events.getAncestorGenreDescriptions());
			Set<Usereventsuggestion> usereventsuggestions = events.getUsereventsuggestions();
			logger.debug("usereventsuggestions:  "+usereventsuggestions);
			
			if(userEmail != null && !userEmail.trim().equals(""))
			{
				for(Usereventsuggestion usernondealsuggestionsObj : usereventsuggestions)
				{
					if(usernondealsuggestionsObj.getUser().getEmailId().equals(userEmail))
					{
						eventModel.setSuggestionType(usernondealsuggestionsObj.getSuggestionType());
						break;
					}
				}
			}
			eventsModelList.add(eventModel);
		}
		return eventsModelList;
	}

	
}
