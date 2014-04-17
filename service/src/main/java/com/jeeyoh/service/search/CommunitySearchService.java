package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.CommunityPaginationResponse;
import com.jeeyoh.model.response.CommunityResponse;
import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.CommunityComments;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Notificationpermission;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.utils.Utils;

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
	public CommunityResponse searchCommunityDetails(int userId, int pageId,int offset,int limit) {
		CommunityResponse communityResponse = new CommunityResponse();

		logger.debug("searchCommunityDetails:::: "+"userId =>"+userId+"; pageId =>"+pageId+";offset =>"+offset);
		
		//community details
		Page page = eventsDAO.getPageDetailsByID(pageId);
		logger.debug("Page::::::"+page);
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
		List<Events> currentEventList = eventsDAO.getCurrentEvents(pageId, offset, limit);
		
		List<EventModel> currentEventModelList = new ArrayList<EventModel>();
		for(Events event: currentEventList)
		{
			EventModel currentEventModel = new EventModel();
			currentEventModel.setEventId(event.getEventId());
			currentEventModel.setAncestorGenreDescriptions(event.getAncestorGenreDescriptions());
			currentEventModel.setChannel(event.getChannel());
			currentEventModel.setCity(event.getCity());
			currentEventModel.setCurrency_code(event.getCurrency_code());
			currentEventModel.setDescription(event.getDescription());
			currentEventModel.setEvent_date(event.getEvent_date().toString());
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
			currentEventModel.setSource(event.getEventSource());
		}
		communityResponse.setCurrentEvents(currentEventModelList);

		// up coming events list
		List<Events> upcomingEventList = eventsDAO.getUpcomingEvents(pageId, offset, limit);
		List<EventModel> upcomingEventModelList = new ArrayList<EventModel>();
		for(Events event: upcomingEventList)
		{
			EventModel upcomingEventModel = new EventModel();
			upcomingEventModel.setEventId(event.getEventId());
			upcomingEventModel.setAncestorGenreDescriptions(event.getAncestorGenreDescriptions());
			upcomingEventModel.setChannel(event.getChannel());
			upcomingEventModel.setCity(event.getCity());
			upcomingEventModel.setCurrency_code(event.getCurrency_code());
			upcomingEventModel.setDescription(event.getDescription());
			upcomingEventModel.setEvent_date(event.getEvent_date().toString());
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
			upcomingEventModel.setSource(event.getEventSource());
		}
		communityResponse.setUpcomingEvents(upcomingEventModelList);

		// past events list
		List<Events> pastEventList = eventsDAO.getPastEvents(pageId, offset, limit);
		List<EventModel> pastEventModelList = new ArrayList<EventModel>();
		for(Events event: pastEventList)
		{
			EventModel pastEventModel = new EventModel();
			pastEventModel.setEventId(event.getEventId());
			pastEventModel.setAncestorGenreDescriptions(event.getAncestorGenreDescriptions());
			pastEventModel.setChannel(event.getChannel());
			pastEventModel.setCity(event.getCity());
			pastEventModel.setCurrency_code(event.getCurrency_code());
			pastEventModel.setDescription(event.getDescription());
			pastEventModel.setEvent_date(event.getEvent_date().toString());
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
			pastEventModel.setSource(event.getEventSource());
		}
		logger.debug("end of comunity response");
		communityResponse.setPastEvents(pastEventModelList);
		
		// append community comments
		
		if(pageId!=0)
		{
			List<CommunityComments> communityCommentsList = eventsDAO.getCommunityCommentsByPageId(pageId);
			List<CommentModel> commentModelList = new ArrayList<CommentModel>();
			for(CommunityComments communityComment:communityCommentsList)
			{
				CommentModel commentModel = new CommentModel();
				commentModel.setComment(communityComment.getComment());
				commentModel.setCreatedTime(Utils.getTime(communityComment.getCreatedTime()));
				commentModel.setItemId(communityComment.getPage().getPageId());
				commentModel.setUserId(communityComment.getUser().getUserId());
				commentModel.setImageUrl(communityComment.getUser().getImageUrl());
				commentModel.setUserName(communityComment.getUser().getFirstName()+" "+communityComment.getUser().getLastName());
				commentModelList.add(commentModel);
			}
			logger.debug("add community comments in response");
			communityResponse.setCommunityComments(commentModelList);
		}
		
		return communityResponse;
	}
	
	@Override
	@Transactional
	public CommunityPaginationResponse searchCommunityPaginationDetails(int userId, int pageId,int offset,int limit, String listType)
	{
		CommunityPaginationResponse communityResponse = new CommunityPaginationResponse();
		logger.debug("searchCommunityDetails => userId =>"+userId+"; pageId =>"+pageId+"; offset =>"+"; limit =>"+limit+"; listType =>"+listType);
		List<Events> eventList = null;
		EventModel eventModel = new EventModel();
		if(listType=="currentEvent" || listType.equals("currentEvent"))
		{
			//current event details
			eventList= eventsDAO.getCurrentEvents(pageId, offset, limit);
		}
		else if(listType=="upcomingEvent" || listType.equals("upcomingEvent"))
		{
			//upcoming event details
			eventList=eventsDAO.getUpcomingEvents(pageId, offset, limit);
		}
		else if(listType=="pastEvent" || listType.equals("pastEvent"))
		{
			//past event details
			eventList=eventsDAO.getPastEvents(pageId, offset, limit);
		}
		else
		{
			logger.debug("wrong input");
		}
		List<EventModel> eventModelList = new ArrayList<EventModel>();
		for(Events event: eventList)
		{
			eventModel.setAncestorGenreDescriptions(event.getAncestorGenreDescriptions());
			eventModel.setChannel(event.getChannel());
			eventModel.setCity(event.getCity());
			eventModel.setCurrency_code(event.getCurrency_code());
			eventModel.setDescription(event.getDescription());
			eventModel.setEvent_date(event.getEvent_date().toString());
			eventModel.setGenreUrlPath(event.getGenreUrlPath());
			eventModel.setGeography_parent(event.getGeography_parent());
			eventModel.setLatitude(event.getLatitude());
			eventModel.setLongitude(event.getLongitude());
			eventModel.setMaxPrice(event.getMaxPrice());
			eventModel.setMinPrice(event.getMinPrice());
			eventModel.setMaxSeatsTogether(event.getMaxSeatsTogether());
			eventModel.setMinSeatsTogether(event.getMinSeatsTogether());
			eventModel.setState(event.getState());
			eventModel.setTitle(event.getTitle());
			eventModel.setTotalTickets(event.getTotalTickets());
			eventModel.setUrlpath(event.getUrlpath());
			eventModel.setVenue_config_name(event.getVenue_config_name());
			eventModel.setVenue_name(event.getVenue_name());
			eventModel.setZip(event.getZip());
			eventModelList.add(eventModel);
		}
		communityResponse.setEvents(eventModelList);
		return communityResponse;
	}
	
	
	@Transactional
	@Override
	public CommentResponse saveCommunityComments(CommentModel commentModel) {
		
		Date date = null;
		//BaseResponse baseResponse = new BaseResponse();
		User user = userDAO.getUserById(commentModel.getUserId());
		Page page = eventsDAO.getPageDetailsByID(commentModel.getItemId());
		boolean isSaved = false;
		try {
			if(page!=null && user!=null)
			{
				CommunityComments communityComment = new CommunityComments();
				communityComment.setComment(commentModel.getComment());
				date = new Date();
				communityComment.setCreatedTime(date);
				communityComment.setUpdatedTime(date);
				communityComment.setPage(page);
				communityComment.setUser(user);
				eventsDAO.saveCommunityComments(communityComment);
			}
			isSaved = true;
		} catch (Exception e) {
			isSaved = false;
		}

		CommentResponse response = new CommentResponse();
		if(isSaved)
		{
			commentModel.setCreatedTime(Utils.getTime(date));
			commentModel.setUserName(user.getFirstName());
			commentModel.setImageUrl(user.getImageUrl());
			response.setComment(commentModel);
			response.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			response.setStatus(ServiceAPIStatus.FAILED.getStatus());
			response.setError("Error");
		}
		return response;
	}
	
	
	@Transactional
	@Override
	public BaseResponse saveIsFollowingPage(int userId, int pageId, boolean isFollow) {
		
		logger.debug("saveIsFollowingPage  ===>");
		User user = userDAO.getUserById(userId);
		Page page = eventsDAO.getPageDetailsByID(pageId);
		BaseResponse baseResponse = new BaseResponse();
		Pageuserlikes pageUserLikes = new Pageuserlikes();
		Notificationpermission notificationPermission = userDAO.getDafaultNotification();
		// Check if entry already exists in table
		Pageuserlikes existingPageUserLikes = userDAO.isPageExistInUserProfile(userId, pageId);
		logger.debug("existingPageUserLikes =>"+existingPageUserLikes);
		if(existingPageUserLikes!=null && !existingPageUserLikes.equals(""))
		{
			if(existingPageUserLikes.getIsFollowing()!=isFollow)
			{
				existingPageUserLikes.setIsFollowing(isFollow);
				userDAO.updateUserCommunity(existingPageUserLikes);
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				return baseResponse;
			}
			else
			{
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				return baseResponse;
			}
			
		}
		else if (existingPageUserLikes == null)
		{
			pageUserLikes.setIsFollowing(isFollow);
			pageUserLikes.setPage(page);
			pageUserLikes.setUser(user);
			pageUserLikes.setCreatedtime(new Date());
			pageUserLikes.setUpdatedtime(new Date());
			pageUserLikes.setNotificationpermission(notificationPermission);
			userDAO.saveUserCommunity(pageUserLikes);
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Invalid Parameter");
		}
		return baseResponse;
	}
	@Transactional
	@Override
	public BaseResponse saveIsFollowingEvent(int userId, int eventId, boolean isFollow) {
		
		logger.debug("saveIsFollowingPage  ===>");
		User user = userDAO.getUserById(userId);
		Events event = eventsDAO.getEventById(eventId);
		BaseResponse baseResponse = new BaseResponse();
		Eventuserlikes eventUserLikes = new Eventuserlikes();
		
		// Check if entry already exists in table
		Eventuserlikes existingEventUserLikes = eventsDAO.isEventExistInUserProfile(userId, eventId);
		logger.debug("existingEventUserLikes =>"+existingEventUserLikes);
		if(existingEventUserLikes!=null && !existingEventUserLikes.equals("") )
		{
			if(existingEventUserLikes.getIsFollowing()!=isFollow)
			{
				existingEventUserLikes.setIsFollowing(isFollow);
				eventsDAO.updateUserEvents(existingEventUserLikes);
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				return baseResponse;
			}
			else
			{
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				return baseResponse;
			}
			
		}
		else if (existingEventUserLikes == null)
		{
			eventUserLikes.setIsFollowing(isFollow);
			eventUserLikes.setIsBooked(false);
			eventUserLikes.setIsFavorite(false);
			eventUserLikes.setIsLike(false);
			eventUserLikes.setIsVisited(false);
			eventUserLikes.setIsProfileDetailsHidden(false);
			eventUserLikes.setIsProfileHidden(false);
			eventUserLikes.setEvent(event);
			eventUserLikes.setUser(user);
			eventUserLikes.setCreatedtime(new Date());
			eventUserLikes.setUpdatedtime(new Date());
			eventsDAO.saveUserEvents(eventUserLikes);
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Invalid Parameter");
		}
		return baseResponse;
	}
	@Transactional
	@Override
	public BaseResponse saveIsFavoriteEvent(int userId, int eventId, boolean isFav) {
		
		logger.debug("saveIsFavoriteEvent  ===>");
		User user = userDAO.getUserById(userId);
		Events event = eventsDAO.getEventById(eventId);
		BaseResponse baseResponse = new BaseResponse();
		Eventuserlikes eventUserLikes = new Eventuserlikes();
		
		// Check if entry already exists in table
		Eventuserlikes existingEventUserLikes = eventsDAO.isEventExistInUserProfile(userId, eventId);
		logger.debug("existingEventUserLikes =>"+existingEventUserLikes);
		if(existingEventUserLikes!=null && !existingEventUserLikes.equals(""))
		{
			if(existingEventUserLikes.getIsFavorite()!=isFav){
				existingEventUserLikes.setIsFavorite(isFav);
				eventsDAO.updateUserEvents(existingEventUserLikes);
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				return baseResponse;
			}
			else
			{
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				return baseResponse;
			}
			
		}
		else if (existingEventUserLikes == null)
		{
			eventUserLikes.setIsFavorite(isFav);
			eventUserLikes.setIsFollowing(false);
			eventUserLikes.setIsBooked(false);
			eventUserLikes.setIsLike(false);
			eventUserLikes.setIsVisited(false);
			eventUserLikes.setIsProfileDetailsHidden(false);
			eventUserLikes.setIsProfileHidden(false);
			eventUserLikes.setEvent(event);
			eventUserLikes.setUser(user);
			eventUserLikes.setCreatedtime(new Date());
			eventUserLikes.setUpdatedtime(new Date());
			eventsDAO.saveUserEvents(eventUserLikes);
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Invalid Parameter");
		}
		return baseResponse;
	}
}
