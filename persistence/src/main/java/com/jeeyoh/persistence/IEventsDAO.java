package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Page;

public interface IEventsDAO {
	
	public void saveEvents(Events events, int batch_size);
	public List<Events> getEventsByCriteria(String userEmail, String searchText,
			String category, String location);
	public List<Page> getCommunities();
	public List<Events> getEventsByCommunityId(int pageId);
	public List<Events> getEventsByCommunityType(int pageId, int pageType);
	public List<Events> getEventsByLikeSearchKeyword(String searchText);
	public List<Events> getEventsBySearchKeyword(String searchText);
	public List<Page> getCommunityByLikeSearchKeyword(String searchText);
	public List<Page> getCommunityBySearchKeyword(String searchText);

}
