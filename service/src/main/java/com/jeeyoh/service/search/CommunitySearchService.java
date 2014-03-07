package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.response.CommunityResponse;
import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Page;

@Component("communitySearch")
public class CommunitySearchService implements ICommunitySearchService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Override
	@Transactional
	public List<PageModel> search(String userEmail) {
		List<Page> pageList = userDAO.getUserCommunitiesByEmailId(userEmail);
		List<PageModel> pageModelList = new ArrayList<PageModel>();
		for(Page page : pageList)
		{
			PageModel pageModel = new PageModel();
			pageModel.setAbout(page.getAbout());
			pageModel.setPageUrl(page.getPageUrl());
			pageModel.setOwner(page.getUserByOwnerId().getFirstName());
			pageModel.setPageType(page.getPagetype().getPageType());
			pageModel.setCreatedDate(page.getCreatedtime().toString());
			pageModelList.add(pageModel);
		}
		return pageModelList;
	}

	@Override
	@Transactional
	public CommunityResponse searchCommunityDetails(int userId, int pageId) {
		CommunityResponse communityResponse = new CommunityResponse();

		logger.debug("searchCommunityDetails:::: ");
		
		//community details
		Page page = eventsDAO.getPageDetailsByID(pageId);
		PageModel pageModel = new PageModel();
		if(!page.equals(null))
		{
			pageModel.setAbout(page.getAbout());
			pageModel.setPageUrl(page.getPageUrl());
			pageModel.setOwner(page.getUserByOwnerId().getFirstName());
			pageModel.setPageType(page.getPagetype().getPageType());
			pageModel.setCreatedDate(page.getCreatedtime().toString());
			pageModel.setProfilePicture(page.getProfilePicture());
			communityResponse.setCommunityDetails(pageModel);
		}
		else
		{
			logger.debug("page object is empty");
		}


		//current event details
		List<Events> currentEventList = eventsDAO.getCurrentEvents(pageId);
		List<EventModel> currentEventModelList = new ArrayList<EventModel>();
		for(Events event: currentEventList)
		{
			EventModel currentEventModel = new EventModel();
			currentEventModel.setAncestorGenreDescriptions(event.getAncestorGenreDescriptions());
			currentEventModel.setChannel(event.getChannel());
			currentEventModel.setCity(event.getCity());
			currentEventModel.setCurrency_code(event.getCurrency_code());
			currentEventModel.setDescription(event.getDescription());
			currentEventModel.setEvent_date(event.getEvent_date());
			currentEventModel.setGenreUrlPath(event.getGenreUrlPath());
			currentEventModel.setGeography_parent(event.getGeography_parent());
			currentEventModel.setLatitude(event.getLatitude());
			currentEventModel.setLongitude(event.getLongitude());
			currentEventModel.setMaxPrice(event.getMaxPrice());
			currentEventModel.setMinPrice(event.getMinPrice());
			currentEventModel.setMaxSeatsTogether(event.getMaxSeatsTogether());
			currentEventModel.setMinSeatsTogether(event.getMinSeatsTogether());
			currentEventModel.setState(event.getState());
			currentEventModel.setTitle(event.getTitle());
			currentEventModel.setTotalTickets(event.getTotalTickets());
			currentEventModel.setUrlpath(event.getUrlpath());
			currentEventModel.setVenue_config_name(event.getVenue_config_name());
			currentEventModel.setVenue_name(event.getVenue_name());
			currentEventModel.setZip(event.getZip());
			currentEventModelList.add(currentEventModel);
		}
		communityResponse.setCurrentEvents(currentEventModelList);

		// up coming events list
		List<Events> upcomingEventList = eventsDAO.getUpcomingEvents(pageId);
		List<EventModel> upcomingEventModelList = new ArrayList<EventModel>();
		for(Events event: upcomingEventList)
		{
			EventModel upcomingEventModel = new EventModel();
			upcomingEventModel.setAncestorGenreDescriptions(event.getAncestorGenreDescriptions());
			upcomingEventModel.setChannel(event.getChannel());
			upcomingEventModel.setCity(event.getCity());
			upcomingEventModel.setCurrency_code(event.getCurrency_code());
			upcomingEventModel.setDescription(event.getDescription());
			upcomingEventModel.setEvent_date(event.getEvent_date());
			upcomingEventModel.setGenreUrlPath(event.getGenreUrlPath());
			upcomingEventModel.setGeography_parent(event.getGeography_parent());
			upcomingEventModel.setLatitude(event.getLatitude());
			upcomingEventModel.setLongitude(event.getLongitude());
			upcomingEventModel.setMaxPrice(event.getMaxPrice());
			upcomingEventModel.setMinPrice(event.getMinPrice());
			upcomingEventModel.setMaxSeatsTogether(event.getMaxSeatsTogether());
			upcomingEventModel.setMinSeatsTogether(event.getMinSeatsTogether());
			upcomingEventModel.setState(event.getState());
			upcomingEventModel.setTitle(event.getTitle());
			upcomingEventModel.setTotalTickets(event.getTotalTickets());
			upcomingEventModel.setUrlpath(event.getUrlpath());
			upcomingEventModel.setVenue_config_name(event.getVenue_config_name());
			upcomingEventModel.setVenue_name(event.getVenue_name());
			upcomingEventModel.setZip(event.getZip());
			upcomingEventModelList.add(upcomingEventModel);
		}
		communityResponse.setUpcomingEvents(upcomingEventModelList);

		// past events list
		List<Events> pastEventList = eventsDAO.getPastEvents(pageId);
		List<EventModel> pastEventModelList = new ArrayList<EventModel>();
		for(Events event: pastEventList)
		{
			EventModel pastEventModel = new EventModel();
			pastEventModel.setAncestorGenreDescriptions(event.getAncestorGenreDescriptions());
			pastEventModel.setChannel(event.getChannel());
			pastEventModel.setCity(event.getCity());
			pastEventModel.setCurrency_code(event.getCurrency_code());
			pastEventModel.setDescription(event.getDescription());
			pastEventModel.setEvent_date(event.getEvent_date());
			pastEventModel.setGenreUrlPath(event.getGenreUrlPath());
			pastEventModel.setGeography_parent(event.getGeography_parent());
			pastEventModel.setLatitude(event.getLatitude());
			pastEventModel.setLongitude(event.getLongitude());
			pastEventModel.setMaxPrice(event.getMaxPrice());
			pastEventModel.setMinPrice(event.getMinPrice());
			pastEventModel.setMaxSeatsTogether(event.getMaxSeatsTogether());
			pastEventModel.setMinSeatsTogether(event.getMinSeatsTogether());
			pastEventModel.setState(event.getState());
			pastEventModel.setTitle(event.getTitle());
			pastEventModel.setTotalTickets(event.getTotalTickets());
			pastEventModel.setUrlpath(event.getUrlpath());
			pastEventModel.setVenue_config_name(event.getVenue_config_name());
			pastEventModel.setVenue_name(event.getVenue_name());
			pastEventModel.setZip(event.getZip());
			pastEventModelList.add(pastEventModel);
		}
		communityResponse.setPastEvents(pastEventModelList);
		return communityResponse;
	}
}
