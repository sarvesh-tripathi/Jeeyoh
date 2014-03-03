package com.jeeyoh.service.search;

import com.jeeyoh.model.response.MatchingEventsResponse;

public interface IMatchingEventsService 
{
	public MatchingEventsResponse searchMatchingEvents(int userId);
}
