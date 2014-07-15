package com.jeeyoh.service.funboard;

import java.sql.Time;
import java.text.SimpleDateFormat;
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
import com.jeeyoh.model.funboard.FunBoardModel;
import com.jeeyoh.model.funboard.FunBoardRequest;
import com.jeeyoh.model.funboard.MediaContenModel;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.FunBoardDetailResponse;
import com.jeeyoh.model.response.FunBoardResponse;
import com.jeeyoh.model.response.UploadMediaServerResponse;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IFunBoardDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Dealredemptionlocation;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Funboard;
import com.jeeyoh.persistence.domain.FunboardComments;
import com.jeeyoh.persistence.domain.FunboardMediaContent;
import com.jeeyoh.persistence.domain.Notificationpermission;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.Privacy;
import com.jeeyoh.persistence.domain.Timeline;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.utils.Utils;

@Component("funBoard")
public class FunBoardService implements IFunBoardService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Value("${host.path}")
	private String hostPath;

	@Autowired
	private IFunBoardDAO funBoardDAO;

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IBusinessDAO businessDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Autowired
	private IDealsDAO dealsDAO;

	@Transactional
	@Override
	public BaseResponse saveFunBoardItem(FunBoardRequest request) {

		BaseResponse baseResponse = new BaseResponse();
		User user = userDAO.getUserByEmailId(request.getEmailId());
		if(user != null)
		{
			FunBoardModel funBoardModel = request.getFunBoard();
			Funboard funboard = funBoardDAO.isFunBoardExists(user.getUserId(), funBoardModel.getItemId());
			int batch_size = 0;
			logger.debug("funboard::::  "+funboard);
			if(funboard == null)
			{
				// Check privacy id
				Privacy privacyObj = userDAO.getUserPrivacyType("OPEN");

				SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				funboard = new Funboard();
				Timeline timeline = null;
				funboard.setUser(user);
				funboard.setItemId(funBoardModel.getItemId());
				funboard.setItemType(funBoardModel.getType());
				Date currentDate = new Date();
				funboard.setCreatedTime(currentDate);
				funboard.setUpdatedTime(currentDate);
				funboard.setImageUrl(funBoardModel.getImageUrl());
				funboard.setSource(funBoardModel.getSource());
				funboard.setPrivacy(privacyObj);
				if(funBoardModel.getCategory().equalsIgnoreCase("Sports"))
				{
					funboard.setCategory("SPORT");
				}
				else
					funboard.setCategory(funBoardModel.getCategory());

				try {
					Date endDate = simple.parse(funBoardModel.getEndDate());
					if(funBoardModel.getType().equalsIgnoreCase("Event") || funBoardModel.getType().equalsIgnoreCase("Community"))
					{
						funboard.setScheduledTime(endDate);
						Time funboardCreationTime = new Time(endDate.getTime());
						funboard.setTimeSlot(funboardCreationTime);
						logger.debug("funboardCreationTime::  "+funboardCreationTime);
						timeline = funBoardDAO.getTimeLine(funboardCreationTime);
					}
					else
					{
						if(funBoardModel.getScheduledDate() != null)
						{
							funboard.setScheduledTime(simple.parse(funBoardModel.getScheduledDate()));
						}
						if(funBoardModel.getTimeLine() == null)
						{
							timeline = funBoardDAO.getDefaultTimeLine();
							funboard.setTimeSlot(timeline.getStartTime());
						}
						else
						{

						}
					}

					funboard.setStartDate(simple.parse(funBoardModel.getStartDate()));
					funboard.setEndDate(endDate);

				} catch (Exception e) {
					logger.debug("Error funboard : " + e.getLocalizedMessage());
					e.printStackTrace();
				}

				logger.debug("Funboard timeline : " + timeline);
				funboard.setTimeline(timeline);


				funBoardDAO.saveFunBoard(funboard, batch_size);

				// Set isVisited and isBooked flag for a particular item in their respective like tables
				if(funBoardModel.getType().equalsIgnoreCase("Community") || funBoardModel.getType().equalsIgnoreCase("Business"))
				{
					Notificationpermission notificationPermission = userDAO.getDafaultNotification();
					Page page = null;
					if(funBoardModel.getType().equalsIgnoreCase("Business"))
					{
						page = eventsDAO.getPageByBusinessId(funBoardModel.getItemId());
					}
					else
					{
						page = new Page();
						page.setPageId(funBoardModel.getItemId());
					}


					Pageuserlikes pageuserlikes = null;

					if(page != null)
					{
						// Check if entry already exists in table
						// Check if entry already exists in table
						pageuserlikes = userDAO.getUserPageProperties(user.getUserId(), page.getPageId());
						logger.debug("pageuserlikes =>"+pageuserlikes);
						if(pageuserlikes!=null && !pageuserlikes.equals(""))
						{
							pageuserlikes.setIsVisited(true);
							pageuserlikes.setIsBooked(true);
							eventsDAO.updatePageUserLikes(pageuserlikes);
						}
						else if (pageuserlikes == null)
						{
							pageuserlikes = new Pageuserlikes();
							pageuserlikes.setIsFavorite(false);
							pageuserlikes.setIsFollowing(false);
							pageuserlikes.setIsLike(false);
							pageuserlikes.setIsVisited(true);
							pageuserlikes.setIsProfileDetailsHidden(false);
							pageuserlikes.setIsProfileHidden(false);
							pageuserlikes.setIsSuggested(false);
							pageuserlikes.setIsBooked(true);
							pageuserlikes.setUser(user);
							pageuserlikes.setPage(page);
							pageuserlikes.setNotificationpermission(notificationPermission);
							pageuserlikes.setCreatedtime(currentDate);
							pageuserlikes.setUpdatedtime(currentDate);
							eventsDAO.savePageUserLikes(pageuserlikes);
						}
					}
				}
				else if(funBoardModel.getType().equalsIgnoreCase("Event"))
				{
					Events event = new Events();
					event.setEventId(funBoardModel.getItemId());

					Eventuserlikes eventUserLikes = new Eventuserlikes();

					// Check if entry already exists in table
					Eventuserlikes existingEventUserLikes = eventsDAO.isEventExistInUserProfile(user.getUserId(), funBoardModel.getItemId());
					logger.debug("existingEventUserLikes =>"+existingEventUserLikes);
					if(existingEventUserLikes!=null && !existingEventUserLikes.equals(""))
					{
						existingEventUserLikes.setIsVisited(true);
						existingEventUserLikes.setIsBooked(true);
						eventsDAO.updateUserEvents(existingEventUserLikes);
					}
					else if (existingEventUserLikes == null)
					{
						eventUserLikes.setIsFavorite(false);
						eventUserLikes.setIsFollowing(false);
						eventUserLikes.setIsBooked(true);
						eventUserLikes.setIsLike(false);
						eventUserLikes.setIsVisited(true);
						eventUserLikes.setIsProfileDetailsHidden(false);
						eventUserLikes.setIsProfileHidden(false);
						eventUserLikes.setIsSuggested(false);
						eventUserLikes.setEvent(event);
						eventUserLikes.setUser(user);
						eventUserLikes.setCreatedtime(currentDate);
						eventUserLikes.setUpdatedtime(currentDate);
						eventsDAO.saveUserEvents(eventUserLikes);
					}
				}
				else if(funBoardModel.getType().equalsIgnoreCase("Deal"))
				{
					Deals deal = new Deals();
					deal.setId(funBoardModel.getItemId());


					Dealsusage dealsusage = null;

					// Check if entry already exists in table
					dealsusage = userDAO.getUserLikeDeal(user.getUserId(), funBoardModel.getItemId());
					logger.debug("dealsusage =>"+dealsusage);
					if(dealsusage!=null && !dealsusage.equals(""))
					{
						dealsusage.setIsVisited(true);
						dealsusage.setIsBooked(true);
						dealsDAO.updateDealUserLikes(dealsusage);
					}
					else if (dealsusage == null)
					{
						dealsusage = new Dealsusage();
						dealsusage.setIsFavorite(false);
						dealsusage.setIsFollowing(false);
						dealsusage.setIsLike(false);
						dealsusage.setIsVisited(true);
						dealsusage.setIsSuggested(false);
						dealsusage.setIsRedempted(false);
						dealsusage.setIsBooked(true);
						dealsusage.setUser(user);
						dealsusage.setDeals(deal);
						dealsusage.setCreatedtime(currentDate);
						dealsusage.setUpdatedtime(currentDate);
						dealsDAO.saveDealUserLikes(dealsusage);
					}
				}
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			}
			else
			{
				logger.debug("Item Already exists in your Fun Board....");
				baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
				baseResponse.setError("The activity already exists on your Funboard");
			}
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
	public FunBoardResponse getUserFunBoardItems(UserModel user) {
		List<Funboard> funboards = funBoardDAO.getUserFunBoardItems(user.getUserId());
		int alertsCount = 0;
		List<FunBoardModel> fridayActivefunBoardModels = new ArrayList<FunBoardModel>();
		List<FunBoardModel> saturadyActivefunBoardModels = new ArrayList<FunBoardModel>();
		List<FunBoardModel> sundayActivefunBoardModels = new ArrayList<FunBoardModel>();
		int day = 0;
		for(Funboard funboard : funboards)
		{
			FunBoardModel funBoardModel = null;
			alertsCount = funBoardDAO.getNotificationCount(funboard.getFunBoardId());
			funBoardModel = new FunBoardModel();
			funBoardModel.setFunBoardId(funboard.getFunBoardId());
			funBoardModel.setItemId(funboard.getItemId());
			funBoardModel.setCategory(funboard.getCategory());
			funBoardModel.setImageUrl(funboard.getImageUrl());
			funBoardModel.setTimeLine(funboard.getTimeline().getTimeLineType());
			funBoardModel.setNotificationCount(alertsCount);
			funBoardModel.setType(funboard.getItemType());
			funBoardModel.setStartDate(funboard.getStartDate().toString());
			funBoardModel.setEndDate(funboard.getEndDate().toString());
			funBoardModel.setSource(funboard.getSource());
			funBoardModel.setTag(funboard.getTag());
			String privacyType = funboard.getPrivacy().getPrivacyType();
			if(privacyType.equals("OPEN"))
				funBoardModel.setIsPublic(true);
			else
				funBoardModel.setIsPublic(false);
			if(funboard.getItemType().equalsIgnoreCase("Business"))
			{
				Business business = businessDAO.getBusinessById(funboard.getItemId());
				if(business != null)
				{
					funBoardModel.setRating(business.getRating());
					funBoardModel.setDescription(business.getName());
					funBoardModel.setWebsiteUrl(business.getWebsiteUrl());

					if(funboard.getTimeSlot() != null)
						funBoardModel.setTimeSlot(funboard.getTimeSlot().toString());
					day = Utils.getDayOfWeek(funboard.getScheduledTime());
					/*fridayActivefunBoardModels.add(funBoardModel);
					saturadyActivefunBoardModels.add(funBoardModel);
					sundayActivefunBoardModels.add(funBoardModel);*/
				}
			}
			else if(funboard.getItemType().equalsIgnoreCase("Deal"))
			{
				Deals deals = dealsDAO.getDealById(funboard.getItemId());
				if(deals != null)
				{
					funBoardModel.setRating(deals.getBusiness().getRating());
					funBoardModel.setDescription(deals.getTitle());
					funBoardModel.setWebsiteUrl(deals.getDealUrl());
					if(funboard.getTimeSlot() != null)
						funBoardModel.setTimeSlot(funboard.getTimeSlot().toString());
					day = Utils.getDayOfWeek(funboard.getScheduledTime());
				}
			}
			else if(funboard.getItemType().equalsIgnoreCase("community"))
			{
				Object[] page = eventsDAO.getPagesAvergeRatingAndDetails(funboard.getItemId());
				if(page != null)
				{
					double avgRating = 0;
					if(page[0] != null)
						avgRating = Double.parseDouble(page[0].toString());
					Page pageDetails = (Page)page[1];
					funBoardModel.setRating(avgRating);
					funBoardModel.setDescription(pageDetails.getAbout());
					funBoardModel.setWebsiteUrl(pageDetails.getPageUrl());
					if(funboard.getTimeSlot() != null)
						funBoardModel.setTimeSlot(funboard.getTimeSlot().toString());

					day = Utils.getDayOfWeek(funboard.getStartDate());
				}
			}
			else
			{
				Events events = eventsDAO.getEventById(funboard.getItemId());
				if(events != null)
				{
					double avgRating = eventsDAO.getCommunityReviewByPageId(events.getPage().getPageId());

					funBoardModel.setRating(avgRating);
					funBoardModel.setDescription(events.getDescription());
					funBoardModel.setTitle(events.getTitle());
					funBoardModel.setWebsiteUrl(events.getUrlpath());
					if(funboard.getTimeSlot() != null)
						funBoardModel.setTimeSlot(funboard.getTimeSlot().toString());
					day = Utils.getDayOfWeek(funboard.getStartDate());
				}
			}

			switch(day){
			case 6: 
				fridayActivefunBoardModels.add(funBoardModel);
				break;	
			case 7: 
				saturadyActivefunBoardModels.add(funBoardModel);
				break;	
			case 1: 
				sundayActivefunBoardModels.add(funBoardModel);
				break;	
			}
		}

		logger.debug("fridayActivefunBoardModels: "+fridayActivefunBoardModels.size() + " saturadyActivefunBoardModels: "+saturadyActivefunBoardModels.size()+" sundayActivefunBoardModels: "+sundayActivefunBoardModels.size());
		FunBoardResponse funBoardResponse = new FunBoardResponse();
		funBoardResponse.setFridayActivefunBoards(fridayActivefunBoardModels);
		funBoardResponse.setSaturdayActivefunBoards(saturadyActivefunBoardModels);
		funBoardResponse.setSundayActivefunBoards(sundayActivefunBoardModels);
		funBoardResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return funBoardResponse;
	}


	@Transactional
	@Override
	public BaseResponse deleteFunBoarditem(FunBoardRequest request) {

		Funboard funboard = funBoardDAO.getFunboardById(request.getFunBoardId());
		if(funboard.getUser().getUserId() == request.getUserId())
		{
			//Delete FunBoard Comments
			funBoardDAO.deleteFunBoardComments(request.getFunBoardId());

			//Delete FunBoard Media Content
			funBoardDAO.deleteFunBoardMediaContent(request.getFunBoardId());

			//Delete FunBoard 
			funBoardDAO.deleteFunBoard(request.getFunBoardId(), request.getUserId());

			if(funboard.getItemType().equalsIgnoreCase("Business") || funboard.getItemType().equalsIgnoreCase("Community"))
			{
				Page page = eventsDAO.getPageByBusinessId(funboard.getItemId());
				int id = 0;
				if(page != null)
					id = page.getPageId();
				else
					id = funboard.getItemId();

				// Check if entry already exists in table
				Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(request.getUserId(), id);
				logger.debug("pageuserlikes =>"+pageuserlikes);
				if(pageuserlikes!=null && !pageuserlikes.equals(""))
				{
					pageuserlikes.setIsBooked(false);
					eventsDAO.updatePageUserLikes(pageuserlikes);
				}

			}
			else if(funboard.getItemType().equalsIgnoreCase("Event"))
			{
				// Check if entry already exists in table
				Eventuserlikes existingEventUserLikes = eventsDAO.isEventExistInUserProfile(request.getUserId(), funboard.getItemId());
				logger.debug("existingEventUserLikes =>"+existingEventUserLikes);
				if(existingEventUserLikes!=null && !existingEventUserLikes.equals(""))
				{
					existingEventUserLikes.setIsBooked(false);
					eventsDAO.updateUserEvents(existingEventUserLikes);
				}
			}
			else if(funboard.getItemType().equalsIgnoreCase("Deal"))
			{
				// Check if entry already exists in table
				Dealsusage dealsusage = userDAO.getUserLikeDeal(request.getUserId(), funboard.getItemId());
				logger.debug("dealsusage =>"+dealsusage);
				if(dealsusage!=null && !dealsusage.equals(""))
				{
					dealsusage.setIsBooked(false);
					dealsDAO.updateDealUserLikes(dealsusage);
				}
			}
		}
		else
		{

		}

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return baseResponse;
	}


	@Transactional
	@Override
	public CommentResponse addFunBoardComment(CommentModel commentModel) {

		Date date = null;
		boolean isSaved = false;
		try {
			Funboard funboard = new Funboard();
			funboard.setFunBoardId(commentModel.getItemId());

			User user = new User();
			user.setUserId(commentModel.getUserId());

			FunboardComments funboardComments = new FunboardComments();
			funboardComments.setComment(commentModel.getComment());
			funboardComments.setUser(user);
			funboardComments.setFunboard(funboard);
			funboardComments.setIsComment(commentModel.getIsComment());
			date = new Date();
			funboardComments.setCreatedTime(date);
			funboardComments.setUpdatedTime(date);
			funBoardDAO.saveFunBoardComment(funboardComments);
			isSaved = true;
		} catch (Exception e) {
			isSaved = false;
		}

		CommentResponse response = new CommentResponse();
		if(isSaved)
		{
			User user = userDAO.getUserById(commentModel.getUserId());
			commentModel.setCreatedTime(Utils.getTime(date));
			commentModel.setUserName(user.getFirstName());
			commentModel.setEmailId(user.getEmailId());
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
	public FunBoardDetailResponse getFunBoardItem(FunBoardRequest request) {


		FunBoardModel funBoardModel = request.getFunBoard();
		String type = funBoardModel.getType();
		List<User> userList = new ArrayList<User>();
		if(type.equalsIgnoreCase("Business"))
		{
			Business business = businessDAO.getBusinessById(funBoardModel.getItemId());
			funBoardModel.setCategory(business.getBusinesstype().getBusinessType());
			funBoardModel.setDescription(business.getName());
			funBoardModel.setImageUrl(business.getImageUrl());
			funBoardModel.setRating(business.getRating());
			funBoardModel.setTitle(business.getName());
			funBoardModel.setSource(business.getSource());

			if(business.getDisplayAddress() != null)
				funBoardModel.setAddress(business.getDisplayAddress().replaceAll("[<>\\[\\],-]", ""));
			if(business.getLattitude() != null)
				funBoardModel.setLatitude(Double.parseDouble(business.getLattitude()));
			if(business.getLongitude() != null)
				funBoardModel.setLongitude(Double.parseDouble(business.getLongitude()));
			userList = userDAO.getAttendingUsersForBusiness(funBoardModel.getItemId(),request.getUserId());

		}
		else if(type.equalsIgnoreCase("Deal"))
		{
			Deals deal = dealsDAO.getDealById(funBoardModel.getItemId());
			Business business = deal.getBusiness();
			funBoardModel.setCategory(business.getBusinesstype().getBusinessType());
			funBoardModel.setDescription(deal.getTitle());
			funBoardModel.setImageUrl(deal.getLargeImageUrl());
			funBoardModel.setRating(deal.getBusiness().getRating());
			funBoardModel.setTitle(deal.getTitle());
			funBoardModel.setSource(deal.getDealSource());
			funBoardModel.setMerhcantName(business.getName());

			//Get price and discount
			Dealoption dealoption = dealsDAO.getDealOptionByDealId(deal.getId());
			if(dealoption != null)
			{
				if(dealoption.getFormattedOriginalPrice() != null)
					funBoardModel.setPrice(dealoption.getFormattedOriginalPrice());
				else
					funBoardModel.setPrice("$"+dealoption.getOriginalPrice());
				funBoardModel.setDiscount(dealoption.getDiscountPercent());

				//Get Address of redemption location
				logger.debug("getting location..........");
				if(dealoption.getDealredemptionlocations() != null && !dealoption.getDealredemptionlocations().isEmpty())
				{
					Dealredemptionlocation dealredemptionlocation = (Dealredemptionlocation)dealoption.getDealredemptionlocations().iterator().next();
					logger.debug("getting location.........." + dealredemptionlocation);
					if(dealredemptionlocation != null)
					{
						String address = dealredemptionlocation.getName()+"\n"+dealredemptionlocation.getStreetAddress1()+"\n"+dealredemptionlocation.getCity()+","+dealredemptionlocation.getState()+" "+dealredemptionlocation.getPostalCode();
						funBoardModel.setAddress(address);
						if(dealredemptionlocation.getLattitude() != null)
							funBoardModel.setLatitude(Double.parseDouble(dealredemptionlocation.getLattitude()));
						if(dealredemptionlocation.getLongitude() != null)
							funBoardModel.setLongitude(Double.parseDouble(dealredemptionlocation.getLongitude()));
					}
				}
			}
			userList = userDAO.getAttendingUsersForDeals(funBoardModel.getItemId(),request.getUserId());
		}
		else if(type.equalsIgnoreCase("Event"))
		{
			Events events = eventsDAO.getEventById(funBoardModel.getItemId());
			if(events.getPage() != null)
				funBoardModel.setCategory(events.getPage().getPagetype().getPageType());

			funBoardModel.setDescription(events.getDescription());
			//funBoardModel.setImageUrl(deal.getLargeImageUrl());
			/*List<CommunityReview> communityReviewList = eventsDAO.getCommunityReviewByPageId(events.getPage().getPageId());
			int rating = 0;
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
				funBoardModel.setRating(avg);
			}*/
			double avgRating = eventsDAO.getCommunityReviewByPageId(events.getPage().getPageId());

			logger.debug("avg rating =>"+avgRating);
			funBoardModel.setRating(avgRating);

			String address = events.getVenue_name()+"\n"+events.getCity()+","+events.getState()+" "+events.getZip();
			funBoardModel.setAddress(address);
			if(events.getLatitude() != null)
				funBoardModel.setLatitude(Double.parseDouble(events.getLatitude()));
			if(events.getLongitude() != null)
				funBoardModel.setLongitude(Double.parseDouble(events.getLongitude()));
			funBoardModel.setTimeLine(Utils.getTimeLineForEvent(events.getEvent_date_local(), events.getEvent_time_local()));
			funBoardModel.setTitle(events.getTitle());
			funBoardModel.setSource(events.getEventSource());
			userList = userDAO.getAttendingUsersForEvents(funBoardModel.getItemId(),request.getUserId());
		}
		else if(type.equalsIgnoreCase("Community"))
		{
			Page page = eventsDAO.getPageDetailsByID(funBoardModel.getItemId());
			funBoardModel.setCategory(page.getPagetype().getPageType());
			funBoardModel.setDescription(page.getAbout());
			funBoardModel.setImageUrl(page.getProfilePicture());
			funBoardModel.setSource(page.getSource());
			/*List<CommunityReview> communityReviewList = eventsDAO.getCommunityReviewByPageId(page.getPageId());
			int rating = 0;
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
				funBoardModel.setRating(avg);
			}*/
			double avgRating = eventsDAO.getCommunityReviewByPageId(page.getPageId());

			logger.debug("avg rating =>"+avgRating);
			funBoardModel.setRating(avgRating);

			//Get recent event date for community
			Object[] event_date = eventsDAO.getRecentEventDetails(page.getPageId());
			if(event_date != null)
			{
				funBoardModel.setTimeLine(Utils.getTimeLineForEvent((Date)event_date[0], event_date[1].toString()));
				if(event_date[2] != null && !event_date[2].toString().trim().equals(""))
					funBoardModel.setLatitude(Double.parseDouble(event_date[2].toString()));
				if(event_date[3] != null && !event_date[3].toString().trim().equals(""))
					funBoardModel.setLongitude(Double.parseDouble(event_date[3].toString()));
			}

			funBoardModel.setTitle(page.getAbout());
			userList = userDAO.getAttendingUsersForpage(funBoardModel.getItemId(),request.getUserId());

		}

		logger.debug("userList: "+userList);

		//Get FunBoard Comments
		List<FunboardComments> comments = funBoardDAO.getComments(request.getFunBoardId());

		//Get FunBoard Images
		List<FunboardMediaContent> mediaContent = funBoardDAO.getmediaContent(request.getFunBoardId());

		List<CommentModel> commentModels = new ArrayList<CommentModel>();
		List<CommentModel> jAlerts = new ArrayList<CommentModel>();
		List<MediaContenModel> mediaContenModels = new ArrayList<MediaContenModel>();
		List<UserModel> attendingUserList = new ArrayList<UserModel>();
		if(comments != null)
		{
			for(FunboardComments funboardComments : comments)
			{
				User user = funboardComments.getUser();
				CommentModel commentModel = new CommentModel();
				commentModel.setComment(funboardComments.getComment());
				commentModel.setUserId(user.getUserId());
				commentModel.setCreatedTime(Utils.getTime(funboardComments.getCreatedTime()));
				commentModel.setUserName(user.getFirstName());
				commentModel.setEmailId(user.getEmailId());
				if(user.getImageUrl() != null)
					commentModel.setImageUrl(hostPath + user.getImageUrl());
				if(funboardComments.getIsComment())
					commentModels.add(commentModel);
				else
					jAlerts.add(commentModel);
			}
		}

		if(mediaContent != null)
		{
			for(FunboardMediaContent funboardMediaContent : mediaContent)
			{
				MediaContenModel mediaContenModel = new MediaContenModel();
				mediaContenModel.setId(funboardMediaContent.getId());
				mediaContenModel.setImageUrl(hostPath+funboardMediaContent.getMediaPathUrl());
				logger.debug("Media content: "+funboardMediaContent.getMediaPathUrl());
				mediaContenModels.add(mediaContenModel);
			}
		}

		if(userList != null)
		{
			for(User user : userList)
			{
				UserModel userModel = new UserModel();
				userModel.setUserId(user.getUserId());
				if(user.getImageUrl() != null)
					userModel.setImageUrl(hostPath+user.getImageUrl());
				attendingUserList.add(userModel);
			}
		}


		funBoardModel.setjAlerts(jAlerts);
		funBoardModel.setComments(commentModels);
		funBoardModel.setImages(mediaContenModels);
		funBoardModel.setAttendingUserList(attendingUserList);

		logger.debug("Details: "+funBoardModel.getDescription()+" : "+funBoardModel.getTitle());
		FunBoardDetailResponse funBoardDetailResponse = new FunBoardDetailResponse();
		funBoardDetailResponse.setFunBoard(funBoardModel);
		funBoardDetailResponse.setStatus(ServiceAPIStatus.OK.getStatus());

		return funBoardDetailResponse;
	}

	@Transactional
	@Override
	public UploadMediaServerResponse uploadMediaContent(MediaContenModel mediaContenModel) {

		logger.debug("getFunBoardId:  "+mediaContenModel.getFunBoardId() + " : UserId: "+ mediaContenModel.getUserId());
		Funboard funboard = new Funboard();
		funboard.setFunBoardId(mediaContenModel.getFunBoardId());

		User user = new User();
		user.setUserId(mediaContenModel.getUserId());

		FunboardMediaContent funboardMediaContent = new FunboardMediaContent();
		funboardMediaContent.setFunboard(funboard);
		funboardMediaContent.setUser(user);
		funboardMediaContent.setMediaType(mediaContenModel.getMediaType());
		funboardMediaContent.setMediaPathUrl(mediaContenModel.getImageUrl());
		Date date = new Date();
		funboardMediaContent.setCreatedTime(date);
		funboardMediaContent.setUpdatedTime(date);
		funBoardDAO.saveFunBoardMediaContent(funboardMediaContent);
		UploadMediaServerResponse response = new UploadMediaServerResponse();
		response.setMediaUrl(hostPath+mediaContenModel.getImageUrl());
		response.setStatus(ServiceAPIStatus.OK.getStatus());
		return response;
	}


	/*@Transactional
	@Override
	public BaseResponse updateTimeLine(FunBoardModel funBoardModel) {
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			Funboard funboard = funBoardDAO.getFunboardById(funBoardModel.getFunBoardId());
			SimpleDateFormat simple=new SimpleDateFormat("HH:mm:ss");
			Date scheduleTime = simple.parse(funBoardModel.getTimeLine());
			Time funboardCreationTime = new Time(scheduleTime.getTime());
			Timeline timeline = funBoardDAO.getTimeLine(funboardCreationTime);
			if(timeline != null)
			{
				funboard.setTimeSlot(funboardCreationTime);
				funboard.setTimeline(timeline);
				funboard.setUpdatedTime(new Date());
				funBoardDAO.updateFunBoard(funboard);
			}
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());

		}catch(Exception e)
		{
			logger.debug("Error: "+e.getLocalizedMessage());
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Error");
		}
		return baseResponse;
	}


	@Transactional
	@Override
	public BaseResponse updateTag(FunBoardModel funBoardModel) {
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			Funboard funboard = funBoardDAO.getFunboardById(funBoardModel.getFunBoardId());
			funboard.setTag(funBoardModel.getTag());
			funboard.setUpdatedTime(new Date());
			funBoardDAO.updateFunBoard(funboard);
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());

		}catch(Exception e)
		{
			logger.debug("Error: "+e.getLocalizedMessage());
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Error");
		}
		return baseResponse;
	}
	 */

	@Transactional
	@Override
	public BaseResponse updateFunBoard(FunBoardModel funBoardModel) {
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			Funboard funboard = funBoardDAO.getFunboardById(funBoardModel.getFunBoardId());
			if(funboard != null)
			{
				//Update TimeLine
				if(funBoardModel.getTimeLine() != null)
				{
					SimpleDateFormat simple=new SimpleDateFormat("HH:mm:ss");
					Date scheduleTime = simple.parse(funBoardModel.getTimeLine());
					Time funboardCreationTime = new Time(scheduleTime.getTime());
					Timeline timeline = funBoardDAO.getTimeLine(funboardCreationTime);
					if(timeline != null)
					{
						funboard.setTimeSlot(funboardCreationTime);
						funboard.setTimeline(timeline);
					}
				}//Update Tag
				else if(funBoardModel.getTag() != null)
					funboard.setTag(funBoardModel.getTag());
				else 
				{
					Privacy privacyObj = null;
					if(funBoardModel.getIsPublic())
						privacyObj = userDAO.getUserPrivacyType("OPEN");
					else
						privacyObj = userDAO.getUserPrivacyType("CLOSED");
					funboard.setPrivacy(privacyObj);
				}

				funboard.setUpdatedTime(new Date());
				funBoardDAO.updateFunBoard(funboard);
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			}
			else
			{
				baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
				baseResponse.setError("Error");
			}
		}catch(Exception e)
		{
			logger.debug("Error: "+e.getLocalizedMessage());
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Error");
		}
		return baseResponse;
	}
}
