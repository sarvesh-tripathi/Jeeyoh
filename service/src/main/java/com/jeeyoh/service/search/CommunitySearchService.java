package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.CommunityListResponse;
import com.jeeyoh.model.response.CommunityPaginationResponse;
import com.jeeyoh.model.response.CommunityResponse;
import com.jeeyoh.model.search.CommunityReviewModel;
import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.search.SearchRequest;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.CommunityComments;
import com.jeeyoh.persistence.domain.CommunityReview;
import com.jeeyoh.persistence.domain.CommunityReviewMap;
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

	@Value("${app.jeeyoh.favorite.success}")
	private String favSuccess;

	@Value("${app.jeeyoh.favorite.already.exist}")
	private String alreadyExists;

	@Value("${app.jeeyoh.follow.success}")
	private String followSuccess;

	@Value("${app.jeeyoh.follow.already.exists}")
	private String alreadyFollowed;

	@Value("${app.jeeyoh.failed}")
	private String errorMessage;

	@Value("${host.path}")
	private String hostPath;

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
			// Check if community is already favorite or not
			Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(userId, page.getPageId());
			if(pageuserlikes != null)
			{
				pageModel.setIsFavorite(pageuserlikes.getIsFavorite());
				pageModel.setIsFollowed(pageuserlikes.getIsFollowing());
			}

			pageModel.setAbout(page.getAbout());
			pageModel.setPageUrl(page.getPageUrl());
			pageModel.setOwner(page.getUserByOwnerId().getFirstName());
			pageModel.setPageType(page.getPagetype().getPageType());
			pageModel.setCreatedDate(page.getCreatedtime().toString());
			pageModel.setProfilePicture(page.getProfilePicture());
			pageModel.setSource(page.getSource());
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
			currentEventModel.setEvent_date(event.getEvent_date_local().toString());
			currentEventModel.setGenreUrlPath(event.getGenreUrlPath());
			currentEventModel.setGeography_parent(event.getGeography_parent());
			currentEventModel.setLatitude(Double.parseDouble(event.getLatitude()));
			currentEventModel.setLongitude(Double.parseDouble(event.getLongitude()));
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
			currentEventModel.setSource(event.getEventSource());
			currentEventModel.setItemType("Event");
			String address = event.getVenue_name()+"\n"+event.getCity()+","+event.getState()+" "+event.getZip();
			currentEventModel.setAddress(address);
			currentEventModel.setCategory(pageModel.getPageType());
			currentEventModel.setTimeSlot(event.getEvent_time_local());
			currentEventModelList.add(currentEventModel);
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
			upcomingEventModel.setEvent_date(event.getEvent_date_local().toString());
			upcomingEventModel.setGenreUrlPath(event.getGenreUrlPath());
			upcomingEventModel.setGeography_parent(event.getGeography_parent());
			upcomingEventModel.setLatitude(Double.parseDouble(event.getLatitude()));
			upcomingEventModel.setLongitude(Double.parseDouble(event.getLongitude()));
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
			upcomingEventModel.setSource(event.getEventSource());
			upcomingEventModel.setItemType("Event");
			String address = event.getVenue_name()+"\n"+event.getCity()+","+event.getState()+" "+event.getZip();
			upcomingEventModel.setAddress(address);
			upcomingEventModel.setCategory(pageModel.getPageType());
			upcomingEventModel.setTimeSlot(event.getEvent_time_local());
			upcomingEventModelList.add(upcomingEventModel);
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
			pastEventModel.setEvent_date(event.getEvent_date_local().toString());
			pastEventModel.setGenreUrlPath(event.getGenreUrlPath());
			pastEventModel.setGeography_parent(event.getGeography_parent());
			pastEventModel.setLatitude(Double.parseDouble(event.getLatitude()));
			pastEventModel.setLongitude(Double.parseDouble(event.getLongitude()));
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
			pastEventModel.setSource(event.getEventSource());
			pastEventModel.setItemType("Event");
			String address = event.getVenue_name()+"\n"+event.getCity()+","+event.getState()+" "+event.getZip();
			pastEventModel.setAddress(address);
			pastEventModel.setCategory(pageModel.getPageType());
			pastEventModel.setTimeSlot(event.getEvent_time_local());
			pastEventModelList.add(pastEventModel);
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
				User user = communityComment.getUser();
				CommentModel commentModel = new CommentModel();
				commentModel.setComment(communityComment.getComment());
				commentModel.setCreatedTime(Utils.getTime(communityComment.getCreatedTime()));
				commentModel.setItemId(communityComment.getPage().getPageId());
				commentModel.setUserId(user.getUserId());
				if(user.getImageUrl() != null)
					commentModel.setImageUrl(hostPath + user.getImageUrl());
				commentModel.setUserName(communityComment.getUser().getFirstName()+" "+communityComment.getUser().getLastName());
				commentModelList.add(commentModel);
			}
			logger.debug("add community comments in response");
			communityResponse.setCommunityComments(commentModelList);
		}

		if(pageId!=0)
		{
			//List<CommunityReview> communityReviewList = eventsDAO.getCommunityReviewByPageId(pageId);
			/*int rating = 0;
			int count = 0;
			if(communityReviewList != null && !communityReviewList.isEmpty())
			{
				for(CommunityReview communityReview:communityReviewList)
				{
					rating = rating + communityReview.getRating();
					count++;
				}
				double avg = (double)rating/count;
				logger.debug("avg rating =>"+avg);
				communityResponse.setRating(avg);
			}
			else
				communityResponse.setRating(0);*/

			double avgRating = eventsDAO.getCommunityReviewByPageId(page.getPageId());

			logger.debug("avg rating =>"+avgRating);
			communityResponse.setRating(avgRating);
			logger.debug("CommunityReview rating =>"+communityResponse.getRating());
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
			eventModel.setEvent_date(event.getEvent_date_local().toString());
			eventModel.setGenreUrlPath(event.getGenreUrlPath());
			eventModel.setGeography_parent(event.getGeography_parent());
			eventModel.setLatitude(Double.parseDouble(event.getLatitude()));
			eventModel.setLongitude(Double.parseDouble(event.getLongitude()));
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
			eventModel.setSource(event.getEventSource());
			eventModel.setItemType("Event");
			String address = event.getVenue_name()+"\n"+event.getCity()+","+event.getState()+" "+event.getZip();
			eventModel.setAddress(address);
			eventModel.setCategory(event.getChannel().split(" ")[0]);
			eventModel.setTimeSlot(event.getEvent_time_local());
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
			if(user.getImageUrl() != null)
				commentModel.setImageUrl(hostPath + user.getImageUrl());
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
		boolean isUpdated = false;
		//User user = userDAO.getUserById(userId);
		User user = new User();
		user.setUserId(userId);

		//Page page = eventsDAO.getPageDetailsByID(pageId);
		Page page = new Page();
		page.setPageId(pageId);
		BaseResponse baseResponse = new BaseResponse();
		Pageuserlikes pageuserlikes = new Pageuserlikes();
		Notificationpermission notificationPermission = userDAO.getDafaultNotification();
		// Check if entry already exists in table
		Pageuserlikes existingPageUserLikes = userDAO.isPageExistInUserProfile(userId, pageId);
		logger.debug("existingPageUserLikes =>"+existingPageUserLikes);
		if(existingPageUserLikes!=null && !existingPageUserLikes.equals(""))
		{
			if(existingPageUserLikes.getIsFollowing()!=isFollow)
			{
				existingPageUserLikes.setIsFollowing(isFollow);
				isUpdated = eventsDAO.updatePageUserLikes(existingPageUserLikes);
				if(isUpdated)
				{
					baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
					baseResponse.setMessage(followSuccess);
				}
				else
					baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			}
			else
			{
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				baseResponse.setMessage(alreadyFollowed);
			}

		}
		else if (existingPageUserLikes == null)
		{
			pageuserlikes.setIsFollowing(isFollow);
			pageuserlikes.setIsFavorite(false);
			pageuserlikes.setIsLike(false);
			pageuserlikes.setIsVisited(false);
			pageuserlikes.setIsProfileDetailsHidden(false);
			pageuserlikes.setIsProfileHidden(false);
			pageuserlikes.setIsSuggested(false);
			pageuserlikes.setIsBooked(false);
			pageuserlikes.setUser(user);
			pageuserlikes.setPage(page);
			pageuserlikes.setNotificationpermission(notificationPermission);
			pageuserlikes.setCreatedtime(new Date());
			pageuserlikes.setUpdatedtime(new Date());
			isUpdated = eventsDAO.savePageUserLikes(pageuserlikes);
			if(isUpdated)
			{
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				baseResponse.setMessage(followSuccess);
			}
			else
				baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError(errorMessage);
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
			baseResponse.setError(errorMessage);
		}
		return baseResponse;
	}
	@Transactional
	@Override
	public BaseResponse saveFavoritePage(int userId, int pageId, boolean isFav) {

		logger.debug("saveIsFavoriteEvent  ===>");
		boolean isUpdated = false;
		//User user = userDAO.getUserById(userId);
		User user = new User();
		user.setUserId(userId);

		//Page page = eventsDAO.getPageDetailsByID(pageId);
		Page page = new Page();
		page.setPageId(pageId);

		BaseResponse baseResponse = new BaseResponse();
		Pageuserlikes pageuserlikes = null;

		Notificationpermission notificationPermission = userDAO.getDafaultNotification();

		// Check if entry already exists in table
		pageuserlikes = userDAO.getUserPageProperties(userId, pageId);
		logger.debug("existingEventUserLikes =>"+pageuserlikes);
		if(pageuserlikes!=null && !pageuserlikes.equals(""))
		{
			if(pageuserlikes.getIsFavorite()!=isFav){
				pageuserlikes.setIsFavorite(isFav);
				isUpdated = eventsDAO.updatePageUserLikes(pageuserlikes);
				if(isUpdated)
				{
					baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
					baseResponse.setMessage(favSuccess);
				}
				else
					baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			}
			else
			{
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				baseResponse.setMessage(alreadyExists);
			}

		}
		else if (pageuserlikes == null)
		{
			pageuserlikes = new Pageuserlikes();
			pageuserlikes.setIsFavorite(isFav);
			pageuserlikes.setIsFollowing(false);
			pageuserlikes.setIsLike(false);
			pageuserlikes.setIsVisited(false);
			pageuserlikes.setIsProfileDetailsHidden(false);
			pageuserlikes.setIsProfileHidden(false);
			pageuserlikes.setIsSuggested(false);
			pageuserlikes.setIsBooked(false);
			pageuserlikes.setUser(user);
			pageuserlikes.setPage(page);
			pageuserlikes.setNotificationpermission(notificationPermission);
			pageuserlikes.setCreatedtime(new Date());
			pageuserlikes.setUpdatedtime(new Date());
			isUpdated = eventsDAO.savePageUserLikes(pageuserlikes);
			if(isUpdated)
			{
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				baseResponse.setMessage(favSuccess);
			}
			else
				baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError(errorMessage);
		}
		return baseResponse;
	}


	@Transactional
	@Override
	public BaseResponse saveCommunityReview(CommunityReviewModel communityReviewModel) {
		logger.debug("saveCommunityReview ===>");
		BaseResponse baseResponse = new BaseResponse();
		if(communityReviewModel!=null)
		{
			Date date = new Date();
			CommunityReview communityReview = new CommunityReview();
			CommunityReviewMap communityReviewMap = new CommunityReviewMap();
			User user = (User)userDAO.getUserById(communityReviewModel.getUserId());
			Page page = (Page)eventsDAO.getPageDetailsByID(communityReviewModel.getPageId());
			communityReview.setUser(user);
			communityReview.setComment(communityReviewModel.getComment());
			communityReview.setCreatedTime(date);
			communityReview.setUpdatedTime(date);
			communityReview.setRating(communityReviewModel.getRating());
			communityReviewMap.setPage(page);
			communityReviewMap.setCreatedTime(date);
			communityReviewMap.setUpdatedTime(date);
			communityReviewMap.setCommunityReview(communityReview);
			eventsDAO.saveCommunityReview(communityReviewMap);
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
	public CommunityListResponse serachCommunity(SearchRequest searchRequest) 
	{
		int totalCount = 0;
		//Getting total number of results
		if(searchRequest.getExactMatchCommunityCount() == 0)
		{
			totalCount = eventsDAO.getTotalCommunityBySearchKeyWordForBusiness(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
		}
		List<Page> pageList = eventsDAO.getCommunityBySearchKeyword(searchRequest.getSearchText(), searchRequest.getCategory(), searchRequest.getExactMatchCommunityCount(), searchRequest.getLimit());
		List<PageModel> pageModels = new ArrayList<PageModel>();
		int count1 = 0;
		for(Page page : pageList)
		{
			logger.debug("count1 =>"+count1++);
			PageModel pageModel = new PageModel();
			//Get recent event date for community
			Object[] event_date = eventsDAO.getRecentEventDate(page.getPageId());
			if(event_date != null)
			{
				Date date = (Date)event_date[0];
				pageModel.setStartDate(date.toString());
				pageModel.setEndDate(date.toString());
				pageModel.setTimeLine(Utils.getTimeLineForEvent(date,event_date[1].toString()));
			}

			pageModel.setPageId(page.getPageId());
			pageModel.setAbout(page.getAbout());
			pageModel.setPageUrl(page.getPageUrl());
			pageModel.setImageUrl(page.getProfilePicture());
			pageModel.setPageType(searchRequest.getCategory());
			pageModel.setItemType("Community");
			pageModel.setSource(page.getSource());
			//List<CommunityReview> communityReviewList = eventsDAO.getCommunityReviewByPageId(page.getPageId());
			/*int rating = 0;
			int count = 0;
			if(communityReviewList != null && !communityReviewList.isEmpty())
			{
				for(CommunityReview communityReview:communityReviewList)
				{
					rating = rating + communityReview.getRating();
					count++;
				}
				double avg = (double)rating/count;
				logger.debug("avg rating =>"+avg);
				pageModel.setRating(avg);
			}*/
			double avgRating = eventsDAO.getCommunityReviewByPageId(page.getPageId());

			logger.debug("avg rating =>"+avgRating);
			pageModel.setRating(avgRating);
			pageModels.add(pageModel);
		}

		CommunityListResponse communityListResponse = new CommunityListResponse();
		communityListResponse.setTotalCount(totalCount);
		communityListResponse.setExactMatchCommunityCount(searchRequest.getExactMatchCommunityCount() + pageList.size());
		communityListResponse.setCommunityList(pageModels);
		communityListResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		communityListResponse.setError("");
		return communityListResponse;
	}

	@Transactional
	@Override
	public CommunityListResponse getCommunityList() {
		// TODO Auto-generated method stub
		return null;
	}
}
