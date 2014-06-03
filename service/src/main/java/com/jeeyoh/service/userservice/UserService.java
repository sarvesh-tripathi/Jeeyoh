package com.jeeyoh.service.userservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.funboard.MediaContenModel;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CategoryLikesResponse;
import com.jeeyoh.model.response.CategoryResponse;
import com.jeeyoh.model.response.FriendListResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.SuggestionResponse;
import com.jeeyoh.model.response.TopSuggestionResponse;
import com.jeeyoh.model.response.UploadMediaServerResponse;
import com.jeeyoh.model.response.UserFriendsGroupResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.response.UserResponse;
import com.jeeyoh.model.search.AddGroupModel;
import com.jeeyoh.model.search.BusinessModel;
import com.jeeyoh.model.search.CategoryModel;
import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.search.SearchRequest;
import com.jeeyoh.model.search.SuggestionModel;
import com.jeeyoh.model.search.TopCommunitySuggestionModel;
import com.jeeyoh.model.search.TopFriendsSuggestionModel;
import com.jeeyoh.model.search.TopJeeyohSuggestionModel;
import com.jeeyoh.model.search.TopMatchListSuggestionModel;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.notification.service.IMessagingEventPublisher;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IFunBoardDAO;
import com.jeeyoh.persistence.IGroupDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Dealredemptionlocation;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Funboard;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Notificationpermission;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.Privacy;
import com.jeeyoh.persistence.domain.Profiletype;
import com.jeeyoh.persistence.domain.Topcommunitysuggestion;
import com.jeeyoh.persistence.domain.Topdealssuggestion;
import com.jeeyoh.persistence.domain.Topeventsuggestion;
import com.jeeyoh.persistence.domain.Topnondealsuggestion;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.service.search.IMatchingEventsService;
import com.jeeyoh.utils.RandomGUID;
import com.jeeyoh.utils.Utils;


@Component("userService")
public class UserService implements IUserService{

	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	@Value("${host.path}")
	private String hostPath;

	@Value("${app.jeeyoh.favorite.success}")
	private String favSuccess;

	@Value("${app.jeeyoh.favorite.already.exist}")
	private String alreadyExists;

	@Value("${app.jeeyoh.failed}")
	private String errorMessage;


	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Autowired
	private IBusinessDAO businessDAO;

	@Autowired
	private IDealsDAO dealsDAO;

	@Autowired
	IMessagingEventPublisher eventPublisher;

	@Autowired
	private IMatchingEventsService matchingEventsService;

	@Autowired
	private IFunBoardDAO funBoardDAO;

	@Autowired
	private IGroupDAO groupDAO;

	@Transactional
	@Override
	public UserRegistrationResponse registerUser(UserModel userModel) {
		logger.debug("registerUser.......");
		User user = new User();
		user.setFirstName(userModel.getFirstName());
		//user.setPassword(Utils.MD5(userModel.getPassword()));
		user.setPassword(userModel.getPassword());
		user.setLastName(userModel.getLastName());
		user.setBirthDate(userModel.getBirthDate());
		user.setBirthMonth(userModel.getBirthMonth());
		user.setBirthYear(userModel.getBirthYear());
		user.setCity(userModel.getCity());
		user.setCountry(userModel.getCountry());
		user.setCreatedtime(new Date());
		user.setUpdatedtime(new Date());
		user.setGender(userModel.getGender());
		user.setAddressline1(userModel.getAddressline1());
		user.setEmailId(userModel.getEmailId());
		user.setZipcode(userModel.getZipcode());
		double latLongArray[] = Utils.getLatLong(userModel.getZipcode());
		user.setLattitude(latLongArray[0]+"");
		user.setLongitude(latLongArray[1]+"");
		user.setMiddleName(userModel.getMiddleName());
		//user.setIsActive(false);//defualt active after gaurav suggestion
		user.setIsActive(true);
		user.setIsDeleted(false);
		Privacy privacy = userDAO.getUserPrivacyType("OPEN");
		user.setPrivacy(privacy);		
		Profiletype profiletype = userDAO.getUserprofileType("USER");
		user.setProfiletype(profiletype);
		RandomGUID myGUID = new RandomGUID();
		String confirmationId = myGUID.toString();
		user.setConfirmationId(confirmationId);
		user.setIsShareCommunity(true);
		user.setIsShareProfileWithFriend(true);
		user.setIsShareProfileWithGroup(true);
		UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse();
		userRegistrationResponse.setConfirmationId(confirmationId);
		userDAO.registerUser(user);
		User user1 = userDAO.loginUser(userModel);
		logger.debug("User in Login Response :: "+user1);
		if(user1 != null)
		{

			userModel.setUserId(user1.getUserId());
			userRegistrationResponse.setUser(userModel);
		}
		return userRegistrationResponse;
	}

	@Transactional
	@Override
	public LoginResponse loginUser(UserModel user) {
		// TODO Auto-generated method stub
		logger.debug("In LOGIN SERVICE");
		User user1 = userDAO.loginUser(user);
		logger.debug("User in Login Response :: "+user1);
		LoginResponse loginResponse = new LoginResponse();
		if(user1 != null)
		{

			RandomGUID myGUID = new RandomGUID();
			String sessionId = myGUID.toString();
			logger.debug("In LOGIN sessionId "+sessionId);
			user1.setSessionId(sessionId);
			userDAO.updateUser(user1);
			user.setSessionId(sessionId);
			user.setUserId(user1.getUserId());
			if(user1.getImageUrl() != null)
				user.setImageUrl(hostPath+user1.getImageUrl());
			loginResponse.setUser(user);
			loginResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			loginResponse.setError("");

		}
		else
		{
			loginResponse.setUser(user);			
			User user_1 = userDAO.getUserByEmailId(user.getEmailId());
			loginResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			if(user_1 != null)
			{
				loginResponse.setErrorType(ServiceAPIStatus.PASSWORD.getStatus());
				loginResponse.setError("Invalid password");
			}
			else
			{
				loginResponse.setErrorType(ServiceAPIStatus.EMAIL.getStatus());
				loginResponse.setError("Invalid email");
			}


		}
		return loginResponse;

	}

	@Transactional
	@Override
	public BaseResponse logoutUser(UserModel user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public BaseResponse changePassword(UserModel userModel) {

		// TODO Auto-generated method stub
		BaseResponse baseResponse = new BaseResponse();
		User user = userDAO.getUserByEmailId(userModel.getEmailId());
		//user.setPassword(Utils.MD5(userModel.getPassword()));
		user.setPassword(userModel.getPassword());
		userDAO.updateUser(user);
		baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return baseResponse;
	}

	@Transactional
	@Override
	public BaseResponse confirmUser(String confirmationCode) {
		BaseResponse baseResponse = new BaseResponse();
		userDAO.confirmUser(confirmationCode);
		baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return baseResponse;
	}

	@Override
	@Transactional
	public BaseResponse isEmailExist(UserModel user) {

		User user1 = userDAO.getUserByEmailId(user.getEmailId());
		BaseResponse baseResponse = new BaseResponse();
		logger.debug("User OBJECT :: "+user1);
		if(user1 != null)
		{

			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Already Registered");
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		return baseResponse;
	}

	@Override
	@Transactional
	public CategoryResponse getUserProfile(UserModel user) {
		CategoryResponse categoryResponse = new CategoryResponse();
		User userDetails = userDAO.getUserById(user.getUserId());
		if(userDetails!=null && !userDetails.equals(""))
		{
			user.setAddressline1(userDetails.getAddressline1());
			user.setBirthDate(userDetails.getBirthDate());
			user.setBirthMonth(userDetails.getBirthMonth());
			user.setBirthYear(userDetails.getBirthYear());
			user.setCity(userDetails.getCity());
			user.setConfirmationId(userDetails.getConfirmationId());
			user.setCountry(userDetails.getCountry());
			user.setCreatedtime(userDetails.getCreatedtime().toString());
			user.setEmailId(userDetails.getEmailId());
			user.setFirstName(userDetails.getFirstName());
			user.setGender(userDetails.getGender());
			if(userDetails.getImageUrl() != null)
				user.setImageUrl(hostPath+userDetails.getImageUrl());
			user.setIsActive(userDetails.getIsActive());
			user.setIsDeleted(userDetails.getIsDeleted());
			user.setIsShareCommunity(userDetails.getIsShareCommunity());
			user.setIsShareProfileWithFriend(userDetails.getIsShareProfileWithFriend());
			user.setIsShareProfileWithGroup(userDetails.getIsShareProfileWithGroup());
			user.setLastName(userDetails.getLastName());
			user.setMiddleName(userDetails.getMiddleName());
			user.setPassword(userDetails.getPassword());
			user.setSessionId(userDetails.getSessionId());
			user.setState(userDetails.getState());
			user.setStreet(userDetails.getStreet());
			user.setUpdatedtime(userDetails.getUpdatedtime().toString());
			user.setUserId(userDetails.getUserId());
			user.setZipcode(userDetails.getZipcode());
			categoryResponse.setUser(user);
		}
		List<Page> pages = eventsDAO.getUserFavourites(user.getUserId());
		logger.debug("First Page ::::: "+pages);
		List<PageModel> sports = new ArrayList<PageModel>();
		List<PageModel> movies = new ArrayList<PageModel>();
		List<PageModel> foods = new ArrayList<PageModel>();
		if(pages != null)
		{
			for(Page page: pages)
			{
				logger.debug("here we get pages ::::: "+page.getAbout());

				PageModel pageModel = new PageModel();
				pageModel.setUserId(user.getUserId());
				pageModel.setPageId(page.getPageId());
				pageModel.setAbout(page.getAbout());
				pageModel.setPageUrl(page.getPageUrl());
				pageModel.setOwner(page.getUserByOwnerId().getFirstName());
				pageModel.setPageType(page.getPagetype().getPageType());
				pageModel.setCreatedDate(page.getCreatedtime().toString());
				pageModel.setProfilePicture(page.getProfilePicture());
				//pageModel.setProfilePicture("image.jpg");
				if(page.getPagetype().getPageType().equalsIgnoreCase("SPORT"))
				{
					sports.add(pageModel);
				}
				if(page.getPagetype().getPageType().equalsIgnoreCase("RESTAURANT"))
				{
					foods.add(pageModel);
				}
				if(page.getPagetype().getPageType().equalsIgnoreCase("THEATER"))
				{
					movies.add(pageModel);
				}
			}
		}
		if(sports.isEmpty() && movies.isEmpty() && foods.isEmpty() )
		{
			categoryResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			categoryResponse.setError("No community for these type");
		}
		else
		{
			categoryResponse.setSport(sports);
			categoryResponse.setFood(foods);
			categoryResponse.setMovie(movies);
			categoryResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			categoryResponse.setError("");
		}
		return categoryResponse;
	}


	@Override
	@Transactional
	public CategoryResponse addFavourite(String category,int userId) {
		CategoryResponse categoryResponse = new CategoryResponse();
		if(category.equalsIgnoreCase("Movies"))
			category = "THEATER";
		List<Page> pages = eventsDAO.getCommunityPageByCategoryType(category, userId);
		logger.debug("First Page ::::: "+pages.size());
		List<PageModel> sports = new ArrayList<PageModel>();
		List<PageModel> movies = new ArrayList<PageModel>();
		List<PageModel> foods = new ArrayList<PageModel>();
		if(pages != null)
		{
			for(Page page: pages)
			{
				PageModel pageModel = new PageModel();
				pageModel.setPageId(page.getPageId());
				pageModel.setAbout(page.getAbout());
				pageModel.setPageUrl(page.getPageUrl());
				pageModel.setOwner(page.getUserByOwnerId().getFirstName());
				pageModel.setPageType(page.getPagetype().getPageType());
				pageModel.setCreatedDate(page.getCreatedtime().toString());
				pageModel.setProfilePicture(page.getProfilePicture());
				//pageModel.setProfilePicture("image.jpg");
				if(page.getPagetype().getPageType().equalsIgnoreCase("SPORT"))
				{
					sports.add(pageModel);
				}
				if(page.getPagetype().getPageType().equalsIgnoreCase("RESTAURANT"))
				{
					foods.add(pageModel);
				}
				if(page.getPagetype().getPageType().equalsIgnoreCase("THEATER"))
				{
					movies.add(pageModel);
				}
			}
		}
		boolean isListEmpty = false;
		categoryResponse.setSport(sports);
		categoryResponse.setFood(foods);
		categoryResponse.setMovie(movies);
		if(category.equalsIgnoreCase("SPORT"))
		{
			if(!sports.isEmpty())
				isListEmpty = true;
		}
		else if(category.equalsIgnoreCase("RESTAURANT"))
		{
			if(!foods.isEmpty())
				isListEmpty = true;
		}
		else if(category.equalsIgnoreCase("THEATER"))
		{
			if(!movies.isEmpty())
				isListEmpty = true;
		}

		if(isListEmpty)
		{
			categoryResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			categoryResponse.setError("");
		}
		else
		{
			categoryResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			categoryResponse.setError("No community for these type");
		}
		return categoryResponse;
	}

	@Override
	@Transactional
	public BaseResponse saveUserFavourite(PageModel page) {
		Pageuserlikes  pageuserlikes = userDAO.isPageExistInUserProfile(page.getUserId(),page.getPageId());
		BaseResponse baseResponse = new BaseResponse();
		Date date = new Date();
		if(pageuserlikes != null)
		{
			logger.debug("Update if already");
			pageuserlikes.setIsFavorite(true);
			pageuserlikes.setUpdatedtime(date);
			userDAO.updateUserCommunity(pageuserlikes);			
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			Pageuserlikes pageUserLike = new Pageuserlikes();
			User user = userDAO.getUserById(page.getUserId());
			Page page1 = eventsDAO.getPageDetailsByID(page.getPageId());
			pageUserLike.setUser(user);
			pageUserLike.setPage(page1);
			pageUserLike.setCreatedtime(date);
			pageUserLike.setIsFavorite(true);
			pageUserLike.setIsFollowing(false);
			pageUserLike.setIsLike(false);
			pageUserLike.setIsProfileHidden(false);
			pageUserLike.setIsVisited(false);
			pageUserLike.setIsBooked(false);
			pageUserLike.setIsSuggested(false);
			pageUserLike.setIsProfileDetailsHidden(false);
			pageUserLike.setUpdatedtime(date);
			Notificationpermission notificationpermission = userDAO.getDafaultNotification();
			pageUserLike.setNotificationpermission(notificationpermission);			
			userDAO.saveUserCommunity(pageUserLike);
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());

		}
		return baseResponse;
	}

	@Transactional
	@Override
	public BaseResponse deleteFavourite(int id, int userId) {
		userDAO.deleteUserFavourity(id,userId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return baseResponse;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public SuggestionResponse getUserSuggestions(UserModel user) {

		logger.debug("getOffset:::   "+user.getOffset());
		logger.debug("getNearestWeekend:::  "+Utils.getNearestWeekend(null));
		int totalCount = 0;
		//Getting total number of suggestions
		if(user.getOffset() == 0)
		{
			int totalNonDealSuggestions = userDAO.getTotalUserNonDealSuggestions(user.getUserId());
			int totalDealSuggestions = userDAO.getTotalUserDealSuggestions(user.getUserId());
			int totalEventSuggestions = userDAO.getTotalUserEventSuggestions(user.getUserId());
			totalCount = totalNonDealSuggestions + totalDealSuggestions + totalEventSuggestions;
		}

		logger.debug("totalCount::  "+totalCount);


		//Get user's non deal suggestions
		List<Business> userNonDealSuggestions = businessDAO.getUserNonDealSuggestions(user.getEmailId(), user.getOffset(), user.getLimit());
		logger.debug("userNonDealSuggestions ==> "+userNonDealSuggestions.size());
		//Get user's deal suggestions
		List<Userdealssuggestion> dealSuggestionList = dealsDAO.getUserDealSuggestions(user.getEmailId(), user.getOffset(), user.getLimit());
		logger.debug("dealSuggestionList ==> "+dealSuggestionList.size());

		int limit = 30 - (userNonDealSuggestions.size() + dealSuggestionList.size());

		//Get user's event suggestions
		List<Events> eventsList = eventsDAO.getUserEventsSuggestions(user.getEmailId(), user.getOffset(), limit);
		logger.debug("EventsList ==> "+eventsList.size());

		List<BusinessModel> businessModelList = new ArrayList<BusinessModel>();
		for(Business business : userNonDealSuggestions)
		{
			BusinessModel businessModel = new BusinessModel();
			Page page = eventsDAO.getPageByBusinessId(business.getId());
			if(page != null)
			{
				// Check if business is already favorite or not
				Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(user.getUserId(), page.getPageId());
				if(pageuserlikes != null)
				{
					businessModel.setIsFavorite(pageuserlikes.getIsFavorite());
					businessModel.setIsBooked(pageuserlikes.getIsBooked());
					//Checking suggestion Criteria
					if(pageuserlikes.getIsFavorite())
						businessModel.setSuggestionCriteria("favorite");
					else if(pageuserlikes.getIsLike())
						businessModel.setSuggestionCriteria("like");
					else if(pageuserlikes.getIsVisited())
						businessModel.setSuggestionCriteria("following");
				}
				else
					businessModel.setSuggestionCriteria("like");
			}
			else
				businessModel.setSuggestionCriteria("like");

			businessModel.setItemId(business.getId());
			businessModel.setName(business.getName());
			businessModel.setWebsiteUrl(business.getWebsiteUrl());
			//businessModel.setDisplayAddress(business.getDisplayAddress());
			businessModel.setCity(business.getCity());
			businessModel.setImageUrl(business.getImageUrl());
			businessModel.setItemType("Business");
			businessModel.setSource(business.getSource());
			businessModel.setRating(business.getRating());
			businessModel.setBusinessType(business.getBusinesstype().getBusinessType());
			if(business.getDisplayAddress() != null)
				businessModel.setAddress(business.getDisplayAddress().replaceAll("[<>\\[\\],-]", ""));
			Set<Usernondealsuggestion> Usernondealsuggestions = business.getUsernondealsuggestions();

			for(Usernondealsuggestion usernondealsuggestionsObj : Usernondealsuggestions)
			{
				if(usernondealsuggestionsObj.getUser().getEmailId().equals(user.getEmailId()))
				{
					if(usernondealsuggestionsObj.getSuggestionType().contains("Community"))
						businessModel.setSuggestionType("Community");
					//Checking suggestion type
					if(usernondealsuggestionsObj.getSuggestionType().contains("User"))
						businessModel.setSuggestedBy("Self");
					else if(usernondealsuggestionsObj.getSuggestionType().contains("Friend") || usernondealsuggestionsObj.getSuggestionType().contains("Group"))
						businessModel.setSuggestedBy("Friend");
					else if(usernondealsuggestionsObj.getSuggestionType().equalsIgnoreCase("Wall Feed Suggestion") || usernondealsuggestionsObj.getSuggestionType().equalsIgnoreCase("Direct Suggestion"))
						businessModel.setSuggestedBy("FriendSuggest");
					if(usernondealsuggestionsObj.getUserContact() != null)
					{
						User suggestedUser = usernondealsuggestionsObj.getUserContact();
						UserModel userModel = new UserModel();
						userModel.setFirstName(suggestedUser.getFirstName());
						userModel.setLastName(suggestedUser.getLastName());
						if(suggestedUser.getImageUrl() != null)
							userModel.setImageUrl(hostPath+suggestedUser.getImageUrl());
						userModel.setUserId(suggestedUser.getUserId());
						businessModel.setSuggestingUser(userModel);
					}
					break;
				}
			}
			businessModelList.add(businessModel);
		}

		List<DealModel> dealsModeList = new ArrayList<DealModel>();
		if(dealSuggestionList != null)
		{
			for(Userdealssuggestion dealsuggestion : dealSuggestionList)
			{
				if(dealsuggestion.getDeals() != null)
				{
					DealModel dealModel = new DealModel();

					Deals deal1 = dealsuggestion.getDeals();

					// Check if deal is already favorite or not
					Dealsusage dealProperty = userDAO.getUserDealProperties(user.getUserId(), deal1.getId());
					if(dealProperty != null)
					{
						dealModel.setIsFavorite(dealProperty.getIsFavorite());
						dealModel.setIsBooked(dealProperty.getIsBooked());
						//Checking suggestion Criteria
						if(dealProperty.getIsFavorite())
							dealModel.setSuggestionCriteria("favorite");
						else if(dealProperty.getIsLike())
							dealModel.setSuggestionCriteria("like");
						else if(dealProperty.getIsVisited())
							dealModel.setSuggestionCriteria("following");
					}
					else
						dealModel.setSuggestionCriteria("like");

					if(dealsuggestion.getSuggestionType().contains("User"))
						dealModel.setSuggestedBy("Self");
					else if(dealsuggestion.getSuggestionType().contains("Friend") || dealsuggestion.getSuggestionType().contains("Group"))
						dealModel.setSuggestedBy("Friend");
					else if(dealsuggestion.getSuggestionType().equalsIgnoreCase("Wall Feed Suggestion") || dealsuggestion.getSuggestionType().equalsIgnoreCase("Direct Suggestion"))
						dealModel.setSuggestedBy("FriendSuggest");

					if(dealsuggestion.getUserContact() != null)
					{
						User suggestedUser = dealsuggestion.getUserContact();
						UserModel userModel = new UserModel();
						userModel.setFirstName(suggestedUser.getFirstName());
						userModel.setLastName(suggestedUser.getLastName());
						if(suggestedUser.getImageUrl() != null)
							userModel.setImageUrl(hostPath+suggestedUser.getImageUrl());
						userModel.setUserId(suggestedUser.getUserId());
						dealModel.setSuggestingUser(userModel);
					}
					dealModel.setItemId(deal1.getId());
					dealModel.setCategory(deal1.getBusiness().getBusinesstype().getBusinessType());
					dealModel.setSource(deal1.getDealSource());
					Business business = deal1.getBusiness();
					dealModel.setRating(business.getRating());
					dealModel.setMerhcantName(business.getName());
					logger.debug("dealModel.getCategory:: "+dealModel.getCategory());
					if(deal1.getTitle() != null)
					{
						dealModel.setTitle(deal1.getTitle());
					}
					if(deal1.getDealUrl() != null)
					{
						dealModel.setDealUrl(deal1.getDealUrl());
					}
					if(deal1.getStartAt() != null)
					{
						dealModel.setStartAt(deal1.getStartAt().toString());
					}
					if(deal1.getEndAt() != null)
					{
						dealModel.setEndAt(deal1.getEndAt().toString());
					}
					if(deal1.getStatus() != null)
					{
						dealModel.setStatus(deal1.getStatus());
					}
					if(dealsuggestion.getSuggestionType() != null)
					{
						if(dealsuggestion.getSuggestionType().contains("Community"))
							dealModel.setSuggestionType("Community");
					}
					if(deal1.getLargeImageUrl() != null)
					{
						dealModel.setImageUrl(deal1.getLargeImageUrl());
					}
					if(deal1.getDealUrl() != null)
					{
						dealModel.setWebsiteUrl(deal1.getDealUrl());
					}

					//Get price and discount
					Dealoption dealoption = dealsDAO.getDealOptionByDealId(deal1.getId());
					if(dealoption != null)
					{
						if(dealoption.getFormattedOriginalPrice() != null)
							dealModel.setPrice(dealoption.getFormattedOriginalPrice());
						else
							dealModel.setPrice("$"+dealoption.getOriginalPrice());
						dealModel.setDiscount(dealoption.getDiscountPercent());

						//Get Address of redemption location
						logger.debug("getting location..........");
						if(dealoption.getDealredemptionlocations() != null && !dealoption.getDealredemptionlocations().isEmpty())
						{
							Dealredemptionlocation dealredemptionlocation = (Dealredemptionlocation)dealoption.getDealredemptionlocations().iterator().next();
							logger.debug("getting location.........." + dealredemptionlocation);
							if(dealredemptionlocation != null)
							{
								String address = dealredemptionlocation.getName()+"\n"+dealredemptionlocation.getStreetAddress1()+"\n"+dealredemptionlocation.getCity()+","+dealredemptionlocation.getState()+" "+dealredemptionlocation.getPostalCode();
								dealModel.setAddress(address);
							}
						}
					}

					dealModel.setItemType("Deal");
					dealsModeList.add(dealModel);
				}
			}
		}

		List<EventModel> eventsModelList = new ArrayList<EventModel>();
		for(Events events : eventsList)
		{
			EventModel eventModel = new EventModel();
			// Check if event is already favorite or not
			Eventuserlikes eventProperty = userDAO.getUserEventProperties(user.getUserId(), events.getEventId());
			if(eventProperty != null)
			{
				eventModel.setIsFavorite(eventProperty.getIsFavorite());
				eventModel.setIsBooked(eventProperty.getIsBooked());
				//Checking suggestion Criteria
				if(eventProperty.getIsFavorite())
					eventModel.setSuggestionCriteria("favorite");
				else if(eventProperty.getIsLike())
					eventModel.setSuggestionCriteria("like");
				else if(eventProperty.getIsVisited())
					eventModel.setSuggestionCriteria("following");
			}
			else
				eventModel.setSuggestionCriteria("like");


			//List<CommunityReview> communityReviewList = eventsDAO.getCommunityReviewByPageId(events.getPage().getPageId());
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
				eventModel.setRating(avg);
			}*/
			double avgRating = eventsDAO.getCommunityReviewByPageId(events.getPage().getPageId());
			eventModel.setRating(avgRating);
			eventModel.setEventId(events.getEventId());
			eventModel.setDescription(events.getDescription());
			eventModel.setVenue_name(events.getVenue_name());

			eventModel.setEvent_date(events.getEvent_date_local().toString());
			eventModel.setStartDate(events.getEvent_date_local().toString());
			eventModel.setEndDate(events.getEvent_date_local().toString());
			eventModel.setCity(events.getCity());
			eventModel.setTitle(events.getTitle());
			eventModel.setTotalTickets(events.getTotalTickets());
			eventModel.setUrlpath(events.getUrlpath());
			eventModel.setAncestorGenreDescriptions(events.getAncestorGenreDescriptions());
			eventModel.setItemType("Event");
			eventModel.setSource(events.getEventSource());
			eventModel.setCategory(events.getChannel().split(" ")[0]);
			eventModel.setTimeLine(Utils.getTimeLineForEvent(events.getEvent_date_local(),events.getEvent_time_local()));
			String address = events.getVenue_name()+"\n"+events.getCity()+","+events.getState()+" "+events.getZip();
			eventModel.setAddress(address);
			Set<Usereventsuggestion> usereventsuggestions = events.getUsereventsuggestions();

			for(Usereventsuggestion usereventsuggestionsObj : usereventsuggestions)
			{
				if(usereventsuggestionsObj.getUser().getEmailId().equals(user.getEmailId()))
				{
					if(usereventsuggestionsObj.getSuggestionType().contains("Community"))
						eventModel.setSuggestionType("Community");

					//Checking suggestion type
					if(usereventsuggestionsObj.getSuggestionType().contains("User"))
						eventModel.setSuggestedBy("Self");
					else if(usereventsuggestionsObj.getSuggestionType().contains("Friend") || usereventsuggestionsObj.getSuggestionType().contains("Group"))
						eventModel.setSuggestedBy("Friend");
					else if(usereventsuggestionsObj.getSuggestionType().equalsIgnoreCase("Wall Feed Suggestion") || usereventsuggestionsObj.getSuggestionType().equalsIgnoreCase("Direct Suggestion"))
						eventModel.setSuggestedBy("FriendSuggest");

					if(usereventsuggestionsObj.getUserContact() != null)
					{
						User suggestedUser = usereventsuggestionsObj.getUserContact();
						UserModel userModel = new UserModel();
						userModel.setFirstName(suggestedUser.getFirstName());
						userModel.setLastName(suggestedUser.getLastName());
						if(suggestedUser.getImageUrl() != null)
							userModel.setImageUrl(hostPath+suggestedUser.getImageUrl());
						userModel.setUserId(suggestedUser.getUserId());
						eventModel.setSuggestingUser(userModel);
					}
					break;
				}
			}
			eventsModelList.add(eventModel);
		}

		logger.debug("dealsModeList:  "+dealsModeList.size() + " businessModelList: "+ businessModelList.size() + " eventsModelList: "+eventsModelList.size());
		SuggestionResponse suggestionResponse = new SuggestionResponse();
		suggestionResponse.setDealSuggestions(dealsModeList);
		suggestionResponse.setNonDealSuggestions(businessModelList);
		suggestionResponse.setEventSuggestions(eventsModelList);
		suggestionResponse.setTotalCount(totalCount);
		suggestionResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		suggestionResponse.setError("");
		return suggestionResponse;
	}

	@Override
	@Transactional
	public BaseResponse forgetPassword(String emailId) {
		User user = userDAO.getUserByEmailId(emailId);
		BaseResponse  baseResponse = new BaseResponse();
		if(user != null)
		{
			UserModel user1 = new UserModel();
			user1.setFirstName(user.getFirstName());
			user1.setEmailId(user.getEmailId());
			//String password = Utils.MD5ToString(user.getPassword());
			user1.setPassword(user.getPassword());
			eventPublisher.forgetPassword(user1);
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}
		return baseResponse;
	}

	@Override
	@Transactional
	public boolean isUserActive(UserModel user) {
		boolean isActive = userDAO.isUserActive(user.getEmailId());
		return isActive;
	}

	@Transactional
	@Override
	public long userFavouriteCount(PageModel page) {

		long count = userDAO.getUserPageFavouriteCount(page.getPageType(),page.getUserId());
		return count;
	}

	@Transactional
	@Override
	public CategoryLikesResponse getCategoryForCaptureLikes(int userId, String categoryType) {
		// TODO Auto-generated method stub
		List<UserCategory> categoryList = userDAO.getUserNonLikeCategories(userId,categoryType);		
		CategoryLikesResponse categoryLikesResponse = new CategoryLikesResponse();
		if(categoryList != null && !categoryList.isEmpty() && categoryList.size() >0)
		{
			//logger.debug("Category list " +categoryList.get(0).getImageUrl());
			List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
			for(UserCategory category:categoryList)
			{
				CategoryModel categoryModel = new CategoryModel();
				categoryModel.setCategoryUrl(category.getImageUrl());
				categoryModel.setItemCategory(category.getItemCategory());
				categoryModel.setItemSubCategory(category.getItemSubCategory());
				categoryModel.setUserCategoryId(category.getUserCategoryId());
				categoryModel.setUserId(userId);
				categoryModels.add(categoryModel);
			}
			categoryLikesResponse.setCategoryModel(categoryModels);
			categoryLikesResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			categoryLikesResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			categoryLikesResponse.setError("No Category Availble");
		}
		return categoryLikesResponse;

	}

	@Transactional
	@Override
	public BaseResponse saveUserCategoryLikes(CategoryModel categoryModel) {

		BaseResponse baseResponse = new BaseResponse();
		if(categoryModel.getUserCategoryId() != 0 && categoryModel.getUserId() != 0)
		{
			logger.debug("record save");
			UserCategoryLikes categoryLikes =  new UserCategoryLikes();
			User user  = userDAO.getUserById(categoryModel.getUserId());
			categoryLikes.setUser(user);
			categoryLikes.setCreatedTime(new Date());
			UserCategory category = userDAO.getCategory(categoryModel.getUserCategoryId());			
			categoryLikes.setUserCategory(category);
			userDAO.saveUserCategoryLike(categoryLikes);			
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
	public TopSuggestionResponse getUserTopSuggestions(UserModel user) {

		// Calculate FunBoard Activities
		int totalFridayActivity = 0, totalSaturdayActivity = 0, totalSundayActivity = 0;
		List<Funboard> funboards = funBoardDAO.getUserFunBoardItems(user.getUserId());
		logger.debug("funBoardList:  "+funboards);
		for(Funboard funboard : funboards)
		{
			if(funboard.getItemType().equalsIgnoreCase("Event") || funboard.getItemType().equalsIgnoreCase("Deal"))
			{
				if(funboard.getEndDate().compareTo(Utils.getCurrentDate()) >=0)
				{
					int day = 0;
					if(funboard.getItemType().equalsIgnoreCase("Event"))
					{
						day = Utils.getDayOfWeek(funboard.getStartDate());
					}
					else
					{
						day = Utils.getDayOfWeek(funboard.getScheduledTime());
					}
					switch(day){
					case 6: 
						totalFridayActivity++;
						break;	
					case 7: 
						totalSaturdayActivity++;
						break;	
					case 1: 
						totalSundayActivity++;
						break;	
					}
				}
			}
			else
			{
				totalFridayActivity++;
				totalSaturdayActivity++;
				totalSundayActivity++;
			}
		}

		TopSuggestionResponse topSuggestionResponse = new TopSuggestionResponse();

		//Set Total Activity
		topSuggestionResponse.setTotalFridayActivity(totalFridayActivity);
		topSuggestionResponse.setTotalSaturdayActivity(totalSaturdayActivity);
		topSuggestionResponse.setTotalSundayActivity(totalSundayActivity);

		// Top 10 Friends Suggestions
		TopSuggestionResponse topFriendsSuggestionResponse = getTopSuggestions(user.getUserId(), user.getEmailId(), "Friend's");
		topSuggestionResponse.setTopFriendsSuggestions(topFriendsSuggestionResponse.getTopFriendsSuggestions());

		// Top 10 Jeeyoh Suggestions
		TopSuggestionResponse topJeeyohSuggestionResponse = getTopSuggestions(user.getUserId(), user.getEmailId(), "Jeeyoh Suggestion");;
		topSuggestionResponse.setTopJeeyohSuggestions(topJeeyohSuggestionResponse.getTopJeeyohSuggestions());

		// Top 10 Community Suggestions
		List<Object[]> rows = userDAO.getTopCommunitySuggestions(user.getUserId());
		//List<Topcommunitysuggestion> topcommunitysuggestions = userDAO.getTopCommunitySuggestions(user.getEmailId());
		List<PageModel> topConcertSuggestions = new ArrayList<PageModel>();
		List<PageModel> topSportSuggestions = new ArrayList<PageModel>();
		List<PageModel> topRestaurantSuggestions = new ArrayList<PageModel>();
		List<PageModel> topTheaterSuggestions = new ArrayList<PageModel>();
		List<PageModel> topMovieSuggestions = new ArrayList<PageModel>();
		List<PageModel> topNightLifeSuggestions = new ArrayList<PageModel>();
		if(rows != null)
		{
			for(Object[] row : rows)
			{
				double avgRating = 0;
				if(row[0] != null)
					avgRating = Double.parseDouble(row[0].toString());
				Topcommunitysuggestion topcommunitysuggestion = (Topcommunitysuggestion)row[1];
				Page page = topcommunitysuggestion.getPage();
				PageModel pageModel = new PageModel();

				// Check if community is already favorite or not
				Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(user.getUserId(), page.getPageId());
				if(pageuserlikes != null)
				{
					pageModel.setIsFavorite(pageuserlikes.getIsFavorite());
					pageModel.setIsBooked(pageuserlikes.getIsBooked());

					//Checking suggestion criteria
					if(pageuserlikes.getIsFavorite())
						pageModel.setSuggestionCriteria("favorite");
					else if(pageuserlikes.getIsLike())
						pageModel.setSuggestionCriteria("like");
					else if(pageuserlikes.getIsVisited())
						pageModel.setSuggestionCriteria("following");
				}
				else
					pageModel.setSuggestionCriteria("like");

				pageModel.setSuggestedBy("Self");
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
				pageModel.setPageType(topcommunitysuggestion.getCategoryType());
				pageModel.setItemType("Community");
				pageModel.setSource(page.getSource());
				//double avgRating = eventsDAO.getCommunityReviewByPageId(page.getPageId());
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

				logger.debug("avg rating =>"+avgRating);
				pageModel.setRating(avgRating);
				if(topcommunitysuggestion.getCategoryType().equalsIgnoreCase("Sport"))
					topSportSuggestions.add(pageModel);
				else if(topcommunitysuggestion.getCategoryType().equalsIgnoreCase("Restaurant"))
					topRestaurantSuggestions.add(pageModel);
				else if(topcommunitysuggestion.getCategoryType().equalsIgnoreCase("Theater"))
					topTheaterSuggestions.add(pageModel);
				else if(topcommunitysuggestion.getCategoryType().equalsIgnoreCase("Concert"))
					topConcertSuggestions.add(pageModel);
				else if(topcommunitysuggestion.getCategoryType().equalsIgnoreCase("Movie"))
					topMovieSuggestions.add(pageModel);
				else if(topcommunitysuggestion.getCategoryType().equalsIgnoreCase("Nightlife"))
					topNightLifeSuggestions.add(pageModel);
			}
		}

		TopCommunitySuggestionModel topCommunitySuggestionModel = new TopCommunitySuggestionModel();
		topCommunitySuggestionModel.setTopMovieSuggestions(topMovieSuggestions);
		topCommunitySuggestionModel.setTopRestaurantSuggestions(topRestaurantSuggestions);
		topCommunitySuggestionModel.setTopSportsSuggestions(topSportSuggestions);
		topCommunitySuggestionModel.setTopTheaterSuggestions(topTheaterSuggestions);
		topCommunitySuggestionModel.setTopConcertSuggestions(topConcertSuggestions);
		topCommunitySuggestionModel.setTopNightLifeSuggestions(topNightLifeSuggestions);



		topSuggestionResponse.setTopCommunitySuggestions(topCommunitySuggestionModel);

		// Top Matching events
		TopSuggestionResponse topMatchListSuggestionResponse = getTopSuggestions(user.getUserId(), user.getEmailId(), "Match List");
		topSuggestionResponse.setTopMatchListSuggestions(topMatchListSuggestionResponse.getTopMatchListSuggestions());


		topSuggestionResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		topSuggestionResponse.setError("");
		return topSuggestionResponse;
	}

	/**
	 * Get Top Friends Suggestions
	 * @param userEmail
	 * @param Type
	 * @return
	 */
	private TopSuggestionResponse getTopSuggestions(int userId, String userEmail, String type)
	{

		List<SuggestionModel> topConcertSuggestions = new ArrayList<SuggestionModel>();
		List<SuggestionModel> topSportsSuggestions = new ArrayList<SuggestionModel>();
		List<SuggestionModel> topRestaurantSuggestions = new ArrayList<SuggestionModel>();
		List<SuggestionModel> topTheaterSuggestions = new ArrayList<SuggestionModel>();
		List<SuggestionModel> topMovieSuggestions = new ArrayList<SuggestionModel>();
		List<SuggestionModel> topNightLifeSuggestions = new ArrayList<SuggestionModel>();

		logger.debug("getTopSuggestions11: ");
		List<Topnondealsuggestion> topNonDealSuggestions = userDAO.getTopNonDealSuggestions(userEmail,type);
		logger.debug("topNonDealSuggestions: "+topNonDealSuggestions + " : "+type);
		for(Topnondealsuggestion topnondealsuggestion : topNonDealSuggestions)
		{
			Business business = topnondealsuggestion.getBusiness();
			SuggestionModel suggestionModel = new SuggestionModel();
			Page page = eventsDAO.getPageByBusinessId(business.getId());
			if(page != null)
			{
				// Check if business is already favorite or not
				Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(userId, page.getPageId());
				if(pageuserlikes != null)
				{
					suggestionModel.setIsFavorite(pageuserlikes.getIsFavorite());
					suggestionModel.setIsBooked(pageuserlikes.getIsBooked());

					//Checking suggestion criteria
					if(pageuserlikes.getIsFavorite())
						suggestionModel.setSuggestionCriteria("favorite");
					else if(pageuserlikes.getIsLike())
						suggestionModel.setSuggestionCriteria("like");
					else if(pageuserlikes.getIsVisited())
						suggestionModel.setSuggestionCriteria("following");
				}
				else
					suggestionModel.setSuggestionCriteria("like");
			}
			else
				suggestionModel.setSuggestionCriteria("like");

			if(!type.equalsIgnoreCase("Match List"))
			{
				//Checking suggestion type
				if(topnondealsuggestion.getSuggestionType().equalsIgnoreCase("Jeeyoh Suggestion"))
					suggestionModel.setSuggestedBy("Self");
				else if(topnondealsuggestion.getSuggestionType().equalsIgnoreCase("Friend's Suggestion"))
					suggestionModel.setSuggestedBy("Friend");
				else if(topnondealsuggestion.getSuggestionType().equalsIgnoreCase("Friend's Direct Suggestion"))
					suggestionModel.setSuggestedBy("FriendSuggest");
			}
			else
				suggestionModel.setSuggestedBy("Friend");

			suggestionModel.setItemId(business.getId());
			suggestionModel.setTitle(business.getName());
			suggestionModel.setUrl(business.getWebsiteUrl());
			//suggestionModel.setDisplayAddress(business.getDisplayAddress());
			suggestionModel.setCity(business.getCity());
			suggestionModel.setImageUrl(business.getImageUrl());
			suggestionModel.setSuggestionType("Business");
			suggestionModel.setSource(business.getSource());
			suggestionModel.setCategoryType(topnondealsuggestion.getCategoryType());
			suggestionModel.setLikeCount(topnondealsuggestion.getTotalLikes());
			suggestionModel.setRating(business.getRating());
			if(business.getDisplayAddress() != null)
				suggestionModel.setAddress(business.getDisplayAddress().replaceAll("[<>\\[\\],-]", ""));
			if(topnondealsuggestion.getUserContact() != null)
			{
				User user = topnondealsuggestion.getUserContact();
				UserModel userModel = new UserModel();
				userModel.setFirstName(user.getFirstName());
				userModel.setLastName(user.getLastName());
				if(user.getImageUrl() != null)
					userModel.setImageUrl(hostPath+user.getImageUrl());
				userModel.setUserId(user.getUserId());
				suggestionModel.setSuggestingUser(userModel);
			}
			if(topnondealsuggestion.getCategoryType().equalsIgnoreCase("Sport"))
				topSportsSuggestions.add(suggestionModel);
			else if(topnondealsuggestion.getCategoryType().equalsIgnoreCase("Restaurant"))
				topRestaurantSuggestions.add(suggestionModel);
			else if(topnondealsuggestion.getCategoryType().equalsIgnoreCase("Theater"))
				topTheaterSuggestions.add(suggestionModel);
			else if(topnondealsuggestion.getCategoryType().equalsIgnoreCase("Concert"))
				topConcertSuggestions.add(suggestionModel);
			else if(topnondealsuggestion.getCategoryType().equalsIgnoreCase("Movie"))
				topConcertSuggestions.add(suggestionModel);
			else if(topnondealsuggestion.getCategoryType().equalsIgnoreCase("Nightlife"))
				topNightLifeSuggestions.add(suggestionModel);

		}


		logger.debug("getTopSuggestions22: ");
		List<Topdealssuggestion> topDealSuggestions = userDAO.getTopDealSuggestions(userEmail,type);
		logger.debug("topDealSuggestions: "+topDealSuggestions +" : "+type);
		for(Topdealssuggestion topdealsuggestion : topDealSuggestions)
		{
			SuggestionModel suggestionModel = new SuggestionModel();
			Deals deals = topdealsuggestion.getDeals();

			// Check if deal is already favorite or not
			Dealsusage dealProperty = userDAO.getUserDealProperties(userId, deals.getId());
			if(dealProperty != null)
			{
				suggestionModel.setIsFavorite(dealProperty.getIsFavorite());
				suggestionModel.setIsBooked(dealProperty.getIsBooked());

				//Checking suggestion criteria
				if(dealProperty.getIsFavorite())
					suggestionModel.setSuggestionCriteria("favorite");
				else if(dealProperty.getIsLike())
					suggestionModel.setSuggestionCriteria("like");
				else if(dealProperty.getIsVisited())
					suggestionModel.setSuggestionCriteria("following");
			}
			else
				suggestionModel.setSuggestionCriteria("like");

			if(!type.equalsIgnoreCase("Match List"))
			{
				//Checking suggestion type
				if(topdealsuggestion.getSuggestionType().equalsIgnoreCase("Jeeyoh Suggestion"))
					suggestionModel.setSuggestedBy("Self");
				else if(topdealsuggestion.getSuggestionType().equalsIgnoreCase("Friend's Suggestion"))
					suggestionModel.setSuggestedBy("Friend");
				else if(topdealsuggestion.getSuggestionType().equalsIgnoreCase("Friend's Direct Suggestion"))
					suggestionModel.setSuggestedBy("FriendSuggest");
			}
			else
				suggestionModel.setSuggestedBy("Friend");

			suggestionModel.setItemId(deals.getId());
			if(deals.getTitle() != null)
			{
				suggestionModel.setTitle(deals.getTitle());
			}
			if(deals.getDealUrl() != null)
			{
				suggestionModel.setUrl(deals.getDealUrl());
			}
			if(deals.getLargeImageUrl() != null)
			{
				suggestionModel.setImageUrl(deals.getLargeImageUrl());
			}
			Business business = deals.getBusiness();
			suggestionModel.setCategoryType(topdealsuggestion.getCategoryType());
			suggestionModel.setSuggestionType("Deal");
			suggestionModel.setSource(deals.getDealSource());
			suggestionModel.setLikeCount(topdealsuggestion.getTotalLikes());
			suggestionModel.setStartAt(deals.getStartAt().toString());
			suggestionModel.setEndAt(deals.getEndAt().toString());
			suggestionModel.setRating(business.getRating());
			suggestionModel.setMerhcantName(business.getName());

			if(topdealsuggestion.getUserContact() != null)
			{
				User user = topdealsuggestion.getUserContact();
				UserModel userModel = new UserModel();
				userModel.setFirstName(user.getFirstName());
				userModel.setLastName(user.getLastName());
				if(user.getImageUrl() != null)
					userModel.setImageUrl(hostPath+user.getImageUrl());
				userModel.setUserId(user.getUserId());
				suggestionModel.setSuggestingUser(userModel);
			}

			//Get price and discount
			Dealoption dealoption = dealsDAO.getDealOptionByDealId(deals.getId());
			if(dealoption != null)
			{
				if(dealoption.getFormattedOriginalPrice() != null)
					suggestionModel.setPrice(dealoption.getFormattedOriginalPrice());
				else
					suggestionModel.setPrice("$"+dealoption.getOriginalPrice());
				suggestionModel.setDiscount(dealoption.getDiscountPercent());

				//Get Address of redemption location
				logger.debug("getting location..........");
				if(dealoption.getDealredemptionlocations() != null && !dealoption.getDealredemptionlocations().isEmpty())
				{
					logger.debug("getDealredemptionlocations::  "+dealoption.getDealredemptionlocations());
					Dealredemptionlocation dealredemptionlocation = (Dealredemptionlocation)dealoption.getDealredemptionlocations().iterator().next();
					logger.debug("getting location.........." + dealredemptionlocation);
					if(dealredemptionlocation != null)
					{
						String address = dealredemptionlocation.getName()+"\n"+dealredemptionlocation.getStreetAddress1()+"\n"+dealredemptionlocation.getCity()+","+dealredemptionlocation.getState()+" "+dealredemptionlocation.getPostalCode();
						suggestionModel.setAddress(address);
					}
				}
			}


			if(topdealsuggestion.getCategoryType().equalsIgnoreCase("Sport"))
				topSportsSuggestions.add(suggestionModel);
			else if(topdealsuggestion.getCategoryType().equalsIgnoreCase("Restaurant"))
				topRestaurantSuggestions.add(suggestionModel);
			else if(topdealsuggestion.getCategoryType().equalsIgnoreCase("Theater"))
				topTheaterSuggestions.add(suggestionModel);
			else if(topdealsuggestion.getCategoryType().equalsIgnoreCase("Concert"))
				topConcertSuggestions.add(suggestionModel);
			else if(topdealsuggestion.getCategoryType().equalsIgnoreCase("Movie"))
				topConcertSuggestions.add(suggestionModel);
			else if(topdealsuggestion.getCategoryType().equalsIgnoreCase("Nightlife"))
				topNightLifeSuggestions.add(suggestionModel);
		}

		logger.debug("getTopSuggestions33: ");
		List<Topeventsuggestion> topEventSuggestions = userDAO.getTopEventSuggestions(userEmail,type);
		logger.debug("topEventSuggestions: "+topEventSuggestions +" : "+type);
		for(Topeventsuggestion topEventSuggestion : topEventSuggestions)
		{
			SuggestionModel suggestionModel = new SuggestionModel();
			Events events = topEventSuggestion.getEvents();

			// Check if event is already favorite or not
			Eventuserlikes eventProperty = userDAO.getUserEventProperties(userId, events.getEventId());
			if(eventProperty != null)
			{
				suggestionModel.setIsFavorite(eventProperty.getIsFavorite());
				suggestionModel.setIsBooked(eventProperty.getIsBooked());

				//Checking suggestion criteria
				if(eventProperty.getIsFavorite())
					suggestionModel.setSuggestionCriteria("favorite");
				else if(eventProperty.getIsLike())
					suggestionModel.setSuggestionCriteria("like");
				else if(eventProperty.getIsVisited())
					suggestionModel.setSuggestionCriteria("following");
			}
			else
				suggestionModel.setSuggestionCriteria("like");


			if(!type.equalsIgnoreCase("Match List"))
			{
				//Checking suggestion type
				if(topEventSuggestion.getSuggestionType().equalsIgnoreCase("Jeeyoh Suggestion"))
					suggestionModel.setSuggestedBy("Self");
				else if(topEventSuggestion.getSuggestionType().equalsIgnoreCase("Friend's Suggestion"))
					suggestionModel.setSuggestedBy("Friend");
				else if(topEventSuggestion.getSuggestionType().equalsIgnoreCase("Friend's Direct Suggestion"))
					suggestionModel.setSuggestedBy("FriendSuggest");
			}
			else
				suggestionModel.setSuggestedBy("Friend");

			double avgRating = eventsDAO.getCommunityReviewByPageId(events.getPage().getPageId());
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
				suggestionModel.setRating(avg);
			}*/
			logger.debug("avg rating =>"+avgRating);
			suggestionModel.setRating(avgRating);
			suggestionModel.setItemId(events.getEventId());
			suggestionModel.setTitle(events.getTitle());
			suggestionModel.setDescription(events.getDescription());
			suggestionModel.setUrl(events.getUrlpath());
			suggestionModel.setSuggestionType("Event");
			suggestionModel.setSource(events.getEventSource());
			suggestionModel.setStartAt(events.getEvent_date_local().toString());
			suggestionModel.setEndAt(events.getEvent_date_local().toString());
			suggestionModel.setCategoryType(topEventSuggestion.getCategoryType());
			suggestionModel.setLikeCount(topEventSuggestion.getTotalLikes());
			suggestionModel.setTimeLine(Utils.getTimeLineForEvent(events.getEvent_date_local(),events.getEvent_time_local()));
			String address = events.getVenue_name()+"\n"+events.getCity()+","+events.getState()+" "+events.getZip();
			suggestionModel.setAddress(address);
			if(topEventSuggestion.getUserContact() != null)
			{
				User user = topEventSuggestion.getUserContact();
				UserModel userModel = new UserModel();
				userModel.setFirstName(user.getFirstName());
				userModel.setLastName(user.getLastName());
				if(user.getImageUrl() != null)
					userModel.setImageUrl(hostPath+user.getImageUrl());
				userModel.setUserId(user.getUserId());
				suggestionModel.setSuggestingUser(userModel);
			}
			if(topEventSuggestion.getCategoryType().equalsIgnoreCase("Sport"))
				topSportsSuggestions.add(suggestionModel);
			else if(topEventSuggestion.getCategoryType().equalsIgnoreCase("Restaurant"))
				topRestaurantSuggestions.add(suggestionModel);
			else if(topEventSuggestion.getCategoryType().equalsIgnoreCase("Theater"))
				topTheaterSuggestions.add(suggestionModel);
			else if(topEventSuggestion.getCategoryType().equalsIgnoreCase("Concert"))
				topConcertSuggestions.add(suggestionModel);
			else if(topEventSuggestion.getCategoryType().equalsIgnoreCase("Movie"))
				topConcertSuggestions.add(suggestionModel);
			else if(topEventSuggestion.getCategoryType().equalsIgnoreCase("Nightlife"))
				topNightLifeSuggestions.add(suggestionModel);
		}

		Collections.sort(topConcertSuggestions, new SuggestionComparator());
		Collections.sort(topRestaurantSuggestions, new SuggestionComparator());
		Collections.sort(topTheaterSuggestions, new SuggestionComparator());
		Collections.sort(topSportsSuggestions, new SuggestionComparator());
		Collections.sort(topMovieSuggestions, new SuggestionComparator());
		Collections.sort(topNightLifeSuggestions, new SuggestionComparator());

		TopSuggestionResponse topSuggestionResponse = new TopSuggestionResponse();
		if(type.equalsIgnoreCase("Friend's"))
		{
			TopFriendsSuggestionModel topFriendsSuggestionModel = new TopFriendsSuggestionModel();
			topFriendsSuggestionModel.setTopMovieSuggestions(topMovieSuggestions);
			topFriendsSuggestionModel.setTopConcertSuggestions(topConcertSuggestions);
			topFriendsSuggestionModel.setTopRestaurantSuggestions(topRestaurantSuggestions);
			topFriendsSuggestionModel.setTopSportsSuggestions(topSportsSuggestions);
			topFriendsSuggestionModel.setTopTheaterSuggestions(topTheaterSuggestions);
			topFriendsSuggestionModel.setTopNightLifeSuggestions(topNightLifeSuggestions);
			topSuggestionResponse.setTopFriendsSuggestions(topFriendsSuggestionModel);

		}
		else if(type.equalsIgnoreCase("Match List"))
		{
			TopMatchListSuggestionModel topMatchListSuggestionModel = new TopMatchListSuggestionModel();
			topMatchListSuggestionModel.setTopMovieSuggestions(topMovieSuggestions);
			topMatchListSuggestionModel.setTopConcertSuggestions(topConcertSuggestions);
			topMatchListSuggestionModel.setTopRestaurantSuggestions(topRestaurantSuggestions);
			topMatchListSuggestionModel.setTopSportsSuggestions(topSportsSuggestions);
			topMatchListSuggestionModel.setTopTheaterSuggestions(topTheaterSuggestions);
			topMatchListSuggestionModel.setTopNightLifeSuggestions(topNightLifeSuggestions);
			topSuggestionResponse.setTopMatchListSuggestions(topMatchListSuggestionModel);
		}
		else
		{
			logger.debug("topSportsSuggestions in jeeyoh : "+topSportsSuggestions.size());
			TopJeeyohSuggestionModel topJeeyohSuggestionModel = new TopJeeyohSuggestionModel();
			topJeeyohSuggestionModel.setTopMovieSuggestions(topMovieSuggestions);
			topJeeyohSuggestionModel.setTopConcertSuggestions(topConcertSuggestions);
			topJeeyohSuggestionModel.setTopRestaurantSuggestions(topRestaurantSuggestions);
			topJeeyohSuggestionModel.setTopSportsSuggestions(topSportsSuggestions);
			topJeeyohSuggestionModel.setTopTheaterSuggestions(topTheaterSuggestions);
			topJeeyohSuggestionModel.setTopNightLifeSuggestions(topNightLifeSuggestions);
			topSuggestionResponse.setTopJeeyohSuggestions(topJeeyohSuggestionModel);
		}

		return topSuggestionResponse;
	}


	/**
	 * Comparator class to compare suggestions by like count
	 */
	public class SuggestionComparator implements Comparator<SuggestionModel> {
		@Override
		public int compare(SuggestionModel o1, SuggestionModel o2) 
		{
			return (int)(o2.getLikeCount()-o1.getLikeCount());
		}
	}


	@Transactional
	@Override
	public UserResponse editUserProfile(UserModel userModel) 
	{
		UserResponse response = new UserResponse();
		User userList = userDAO.getUserById(userModel.getUserId());
		if(userModel!=null && userList!=null)
		{
			User user = userList;
			if(userModel.getAddressline1()!=null)
			{
				user.setAddressline1(userModel.getAddressline1());
			}
			if(userModel.getCity()!=null)
			{
				user.setCity(userModel.getCity());
			}
			if(userModel.getStreet()!=null)
			{
				user.setStreet(userModel.getStreet());
			}
			if(userModel.getState()!=null)
			{
				user.setState(userModel.getState());
			}
			if(userModel.getZipcode()!=null)
			{
				user.setZipcode(userModel.getZipcode());
			}
			if(userModel.getBirthDate()!=null)
			{
				user.setBirthDate(userModel.getBirthDate());
			}
			if(userModel.getBirthMonth()!=null)
			{
				user.setBirthMonth(userModel.getBirthMonth());
			}
			if(userModel.getBirthYear()!=null)
			{
				user.setBirthYear(userModel.getBirthYear());
			}
			if(userModel.getFirstName()!=null)
			{
				user.setFirstName(userModel.getFirstName());
			}
			if(userModel.getLastName()!=null)
			{
				user.setLastName(userModel.getLastName());
			}
			if(userModel.getMiddleName()!=null)
			{
				user.setMiddleName(userModel.getMiddleName());
			}
			user.setUpdatedtime(new Date());
			userDAO.updateUser(user);
			response.setUser(userModel);
			response.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		return response;
	}


	@Transactional
	@Override
	public BaseResponse updatePrivacySetting(UserModel userModel) 
	{
		logger.debug("updatePrivacySetting =>");
		BaseResponse response = new BaseResponse();
		if(userModel!=null)
		{
			User user = userDAO.getUserById(userModel.getUserId());
			if(userModel.getIsSharePublic())
			{
				logger.debug("public");
				user.setIsShareCommunity(true);
				user.setIsShareProfileWithFriend(true);
				user.setIsShareProfileWithGroup(true);
			}
			else 
			{
				logger.debug("community");
				user.setIsShareCommunity(userModel.getIsShareCommunity());
				user.setIsShareProfileWithFriend(userModel.getIsShareProfileWithFriend());
				user.setIsShareProfileWithGroup(userModel.getIsShareProfileWithGroup());
			}
			userDAO.updateUser(user);
			response.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			response.setError("Task Failed");
			response.setStatus(ServiceAPIStatus.FAILED.getStatus()); 
		}
		return response;
	}

	@Transactional
	@Override
	public BaseResponse saveFavoriteItem(int userId, int itemId, String itemType, boolean isFav) {
		logger.debug("saveIsFavoriteEvent  ===>");
		Date date = new Date();

		//User user = userDAO.getUserById(userId);
		boolean isUpdated = false, isAlreadyExists = false;
		User user = new User();
		user.setUserId(userId);

		BaseResponse baseResponse = new BaseResponse();
		if(itemType.equalsIgnoreCase("Community") || itemType.equalsIgnoreCase("Business"))
		{
			Notificationpermission notificationPermission = userDAO.getDafaultNotification();
			Page page = null;
			if(itemType.equalsIgnoreCase("Business"))
			{
				page = eventsDAO.getPageByBusinessId(itemId);
			}
			else
			{
				page = new Page();
				page.setPageId(itemId);
			}

			Pageuserlikes pageuserlikes = null;

			if(page != null)
			{
				// Check if entry already exists in table
				pageuserlikes = userDAO.getUserPageProperties(userId, page.getPageId());
				logger.debug("pageuserlikes =>"+pageuserlikes);
				if(pageuserlikes!=null && !pageuserlikes.equals(""))
				{
					if(pageuserlikes.getIsFavorite()!=isFav){
						pageuserlikes.setIsFavorite(isFav);
						isUpdated = eventsDAO.updatePageUserLikes(pageuserlikes);
					}
					else
					{
						isAlreadyExists = true;
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
					pageuserlikes.setCreatedtime(date);
					pageuserlikes.setUpdatedtime(date);
					isUpdated = eventsDAO.savePageUserLikes(pageuserlikes);
				}
			}
		}
		else if(itemType.equalsIgnoreCase("Event"))
		{
			Events event = new Events();
			event.setEventId(itemId);

			Eventuserlikes eventUserLikes = new Eventuserlikes();

			// Check if entry already exists in table
			Eventuserlikes existingEventUserLikes = eventsDAO.isEventExistInUserProfile(userId, itemId);
			logger.debug("existingEventUserLikes =>"+existingEventUserLikes);
			if(existingEventUserLikes!=null && !existingEventUserLikes.equals(""))
			{
				if(existingEventUserLikes.getIsFavorite()!=isFav){
					existingEventUserLikes.setIsFavorite(isFav);
					isUpdated = eventsDAO.updateUserEvents(existingEventUserLikes);
				}
				else
				{
					isAlreadyExists = true;
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
				eventUserLikes.setIsSuggested(false);
				eventUserLikes.setIsBooked(false);
				eventUserLikes.setEvent(event);
				eventUserLikes.setUser(user);
				eventUserLikes.setCreatedtime(date);
				eventUserLikes.setUpdatedtime(date);
				isUpdated = eventsDAO.saveUserEvents(eventUserLikes);
			}
		}
		else if(itemType.equalsIgnoreCase("Deal"))
		{
			//Page page = eventsDAO.getPageDetailsByID(pageId);
			Deals deal = new Deals();
			deal.setId(itemId);


			Dealsusage dealsusage = null;

			// Check if entry already exists in table

			dealsusage = userDAO.getUserLikeDeal(userId, itemId);
			logger.debug("dealsusage =>"+dealsusage);
			if(dealsusage!=null && !dealsusage.equals(""))
			{
				if(dealsusage.getIsFavorite()!=isFav){
					dealsusage.setIsFavorite(isFav);
					isUpdated = dealsDAO.updateDealUserLikes(dealsusage);
				}
				else
				{
					isAlreadyExists = true;
				}

			}
			else if (dealsusage == null)
			{
				dealsusage = new Dealsusage();
				dealsusage.setIsFavorite(isFav);
				dealsusage.setIsFollowing(false);
				dealsusage.setIsLike(false);
				dealsusage.setIsVisited(false);
				dealsusage.setIsSuggested(false);
				dealsusage.setIsRedempted(false);
				dealsusage.setIsBooked(false);
				dealsusage.setUser(user);
				dealsusage.setDeals(deal);
				dealsusage.setCreatedtime(date);
				dealsusage.setUpdatedtime(date);
				isUpdated = dealsDAO.saveDealUserLikes(dealsusage);
			}
		}

		if(isUpdated)
		{
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			baseResponse.setMessage(favSuccess);
		}
		else if(isAlreadyExists)
		{
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			baseResponse.setMessage(alreadyExists);
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
	public FriendListResponse getFriendsOfUser(int userId) {
		logger.debug("getFriendsOfUser =>"+userId);
		FriendListResponse response = new FriendListResponse();
		List<UserModel> userList = new ArrayList<UserModel>();
		List<User> friendsList = userDAO.getUserContacts(userId);
		for(User friend: friendsList)
		{
			UserModel user =  new UserModel();
			user.setAddressline1(friend.getAddressline1());
			user.setBirthDate(friend.getBirthDate());
			user.setBirthMonth(friend.getBirthMonth());
			user.setBirthYear(friend.getBirthYear());
			user.setCity(friend.getCity());
			user.setConfirmationId(friend.getConfirmationId());
			user.setCountry(friend.getCountry());
			user.setCreatedtime(friend.getCreatedtime().toString());
			user.setEmailId(friend.getEmailId());
			user.setFirstName(friend.getFirstName());
			user.setGender(friend.getGender());
			user.setIsActive(friend.getIsActive());
			user.setIsDeleted(friend.getIsDeleted());
			user.setLastName(friend.getLastName());
			user.setMiddleName(friend.getMiddleName());
			user.setPassword(friend.getPassword());
			user.setState(friend.getState());
			user.setSessionId(friend.getSessionId());
			user.setStreet(friend.getStreet());
			user.setUpdatedtime(friend.getUpdatedtime().toString());
			user.setUserId(friend.getUserId());
			user.setZipcode(friend.getZipcode());
			if(friend.getImageUrl() != null)
				user.setImageUrl(hostPath+friend.getImageUrl());
			userList.add(user);
		}
		response.setUser(userList);
		return response;
	}

	@Transactional
	@Override
	public UserResponse getPrivacySetting(int userId) 
	{
		logger.debug("getPrivacySetting => userId => "+userId);
		UserResponse response = new UserResponse();
		if(userId!=0)
		{
			User user = (User)userDAO.getUserById(userId);
			UserModel userModel = new UserModel();
			userModel.setUserId(userId);
			if(user.getIsShareCommunity() && user.getIsShareProfileWithFriend() && user.getIsShareProfileWithGroup())
			{
				userModel.setIsSharePublic(true);
				userModel.setIsShareCommunity(false);
				userModel.setIsShareProfileWithFriend(false);
				userModel.setIsShareProfileWithGroup(false);
			}
			else
			{
				userModel.setIsShareCommunity(user.getIsShareCommunity());
				userModel.setIsShareProfileWithFriend(user.getIsShareProfileWithFriend());
				userModel.setIsShareProfileWithGroup(user.getIsShareProfileWithGroup());
				userModel.setIsSharePublic(false);
			}
			response.setUser(userModel);
		}
		return response;
	}

	@Transactional
	@Override
	public UserFriendsGroupResponse getUserFriendsAndGroup(int userId) 
	{
		UserFriendsGroupResponse friendsGroupResponse = new UserFriendsGroupResponse();
		List<User> userContacts = userDAO.getUserContacts(userId);
		List<UserModel> userModels = new ArrayList<UserModel>();
		logger.debug("user contact :: "+userContacts);
		if(userContacts != null)
		{
			for(User user:userContacts)
			{
				UserModel userModel = new UserModel();
				userModel.setFirstName(user.getFirstName());
				userModel.setUserId(user.getUserId());
				userModels.add(userModel);
			}
		}
		List<Jeeyohgroup> jeeyohGroup = groupDAO.getUserGroups(userId);
		logger.debug("user group :: "+jeeyohGroup);
		List<AddGroupModel>	groupModels = new ArrayList<AddGroupModel>();
		if(jeeyohGroup != null)
		{

			for(Jeeyohgroup jeeyohGroup1 : jeeyohGroup) 
			{
				AddGroupModel addGroupModel = new AddGroupModel();
				addGroupModel.setGroupName(jeeyohGroup1.getGroupName());
				addGroupModel.setGroupId(jeeyohGroup1.getGroupId());
				groupModels.add(addGroupModel);
			}
		}

		if(userModels != null || !userModels.isEmpty())
		{
			friendsGroupResponse.setFriends(userModels);
		}
		if(groupModels != null || !groupModels.isEmpty())
		{
			friendsGroupResponse.setGroups(groupModels);
		}
		friendsGroupResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return friendsGroupResponse;
	}


	@Transactional
	@Override
	public UploadMediaServerResponse uploadProfileImage(MediaContenModel mediaContenModel) 
	{
		UploadMediaServerResponse response = new UploadMediaServerResponse();
		User user = userDAO.getUserById(mediaContenModel.getUserId());
		if(user!=null)
		{
			user.setImageUrl(mediaContenModel.getImageUrl());
			userDAO.updateUser(user);
			response.setMediaUrl(hostPath+mediaContenModel.getImageUrl());
			response.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public SuggestionResponse getuserSuggestionByCategory(SearchRequest searchRequest) 
	{

		//Get user's non deal suggestions
		List<Business> userNonDealSuggestions = businessDAO.getUserNonDealSuggestions(searchRequest.getEmailId(), searchRequest.getOffset(), searchRequest.getLimit());
		logger.debug("userNonDealSuggestions ==> "+userNonDealSuggestions.size());
		//Get user's deal suggestions
		List<Userdealssuggestion> dealSuggestionList = dealsDAO.getUserDealSuggestions(searchRequest.getEmailId(), searchRequest.getOffset(), searchRequest.getLimit());
		logger.debug("dealSuggestionList ==> "+dealSuggestionList.size());

		int limit = 30 - (userNonDealSuggestions.size() + dealSuggestionList.size());

		//Get user's event suggestions
		List<Events> eventsList = eventsDAO.getUserEventsSuggestions(searchRequest.getEmailId(), searchRequest.getOffset(), limit);
		logger.debug("EventsList ==> "+eventsList.size());

		List<BusinessModel> businessModelList = new ArrayList<BusinessModel>();
		for(Business business : userNonDealSuggestions)
		{
			BusinessModel businessModel = new BusinessModel();
			Page page = eventsDAO.getPageByBusinessId(business.getId());
			if(page != null)
			{
				// Check if business is already favorite or not
				Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(searchRequest.getUserId(), page.getPageId());
				if(pageuserlikes != null)
				{
					businessModel.setIsFavorite(pageuserlikes.getIsFavorite());
					//Checking suggestion Criteria
					if(pageuserlikes.getIsFavorite())
						businessModel.setSuggestionCriteria("favorite");
					else if(pageuserlikes.getIsLike())
						businessModel.setSuggestionCriteria("like");
					else if(pageuserlikes.getIsVisited())
						businessModel.setSuggestionCriteria("visited");
				}
				else
					businessModel.setSuggestionCriteria("like");
			}
			else
				businessModel.setSuggestionCriteria("like");

			businessModel.setItemId(business.getId());
			businessModel.setName(business.getName());
			businessModel.setWebsiteUrl(business.getWebsiteUrl());
			businessModel.setDisplayAddress(business.getDisplayAddress());
			businessModel.setCity(business.getCity());
			businessModel.setImageUrl(business.getImageUrl());
			businessModel.setItemType("Business");
			businessModel.setSource(business.getSource());
			businessModel.setRating(business.getRating());
			businessModel.setBusinessType(business.getBusinesstype().getBusinessType());
			Set<Usernondealsuggestion> Usernondealsuggestions = business.getUsernondealsuggestions();

			for(Usernondealsuggestion usernondealsuggestionsObj : Usernondealsuggestions)
			{
				if(usernondealsuggestionsObj.getUser().getEmailId().equals(searchRequest.getEmailId()))
				{
					//businessModel.setSuggestionType(usernondealsuggestionsObj.getSuggestionType());
					//Checking suggestion type
					if(usernondealsuggestionsObj.getSuggestionType().contains("User"))
						businessModel.setSuggestedBy("Self");
					else if(usernondealsuggestionsObj.getSuggestionType().contains("Friend") || usernondealsuggestionsObj.getSuggestionType().contains("Group"))
						businessModel.setSuggestedBy("Friend");
					else if(usernondealsuggestionsObj.getSuggestionType().equalsIgnoreCase("Wall Feed Suggestion") || usernondealsuggestionsObj.getSuggestionType().equalsIgnoreCase("Direct Suggestion"))
						businessModel.setSuggestedBy("FriendSuggest");
					if(usernondealsuggestionsObj.getUserContact() != null)
					{
						User suggestedUser = usernondealsuggestionsObj.getUserContact();
						UserModel userModel = new UserModel();
						userModel.setFirstName(suggestedUser.getFirstName());
						userModel.setLastName(suggestedUser.getLastName());
						userModel.setImageUrl(hostPath+suggestedUser.getImageUrl());
						userModel.setUserId(suggestedUser.getUserId());
						businessModel.setSuggestingUser(userModel);
					}
					break;
				}
			}
			businessModelList.add(businessModel);
		}

		List<DealModel> dealsModeList = new ArrayList<DealModel>();
		if(dealSuggestionList != null)
		{
			for(Userdealssuggestion dealsuggestion : dealSuggestionList)
			{
				if(dealsuggestion.getDeals() != null)
				{
					DealModel dealModel = new DealModel();

					Deals deal1 = dealsuggestion.getDeals();

					// Check if deal is already favorite or not
					Dealsusage dealProperty = userDAO.getUserDealProperties(searchRequest.getUserId(), deal1.getId());
					if(dealProperty != null)
					{
						dealModel.setIsFavorite(dealProperty.getIsFavorite());
						//Checking suggestion Criteria
						if(dealProperty.getIsFavorite())
							dealModel.setSuggestionCriteria("favorite");
						else if(dealProperty.getIsLike())
							dealModel.setSuggestionCriteria("like");
						else if(dealProperty.getIsVisited())
							dealModel.setSuggestionCriteria("visited");
					}
					else
						dealModel.setSuggestionCriteria("like");


					if(dealsuggestion.getSuggestionType().contains("User"))
						dealModel.setSuggestedBy("Self");
					else if(dealsuggestion.getSuggestionType().contains("Friend") || dealsuggestion.getSuggestionType().contains("Group"))
						dealModel.setSuggestedBy("Friend");
					else if(dealsuggestion.getSuggestionType().equalsIgnoreCase("Wall Feed Suggestion") || dealsuggestion.getSuggestionType().equalsIgnoreCase("Direct Suggestion"))
						dealModel.setSuggestedBy("FriendSuggest");

					if(dealsuggestion.getUserContact() != null)
					{
						User suggestedUser = dealsuggestion.getUserContact();
						UserModel userModel = new UserModel();
						userModel.setFirstName(suggestedUser.getFirstName());
						userModel.setLastName(suggestedUser.getLastName());
						userModel.setImageUrl(hostPath+suggestedUser.getImageUrl());
						userModel.setUserId(suggestedUser.getUserId());
						dealModel.setSuggestingUser(userModel);
					}

					dealModel.setItemId(deal1.getId());
					dealModel.setCategory(deal1.getBusiness().getBusinesstype().getBusinessType());
					dealModel.setSource(deal1.getDealSource());
					dealModel.setRating(deal1.getBusiness().getRating());
					logger.debug("dealModel.getCategory:: "+dealModel.getCategory());
					if(deal1.getTitle() != null)
					{
						dealModel.setTitle(deal1.getTitle());
					}
					if(deal1.getDealUrl() != null)
					{
						dealModel.setDealUrl(deal1.getDealUrl());
					}
					if(deal1.getStartAt() != null)
					{
						dealModel.setStartAt(deal1.getStartAt().toString());
					}
					if(deal1.getEndAt() != null)
					{
						dealModel.setEndAt(deal1.getEndAt().toString());
					}
					if(deal1.getStatus() != null)
					{
						dealModel.setStatus(deal1.getStatus());
					}
					if(dealsuggestion.getSuggestionType() != null)
					{
						dealModel.setSuggestionType(dealsuggestion.getSuggestionType());
					}
					if(deal1.getLargeImageUrl() != null)
					{
						dealModel.setImageUrl(deal1.getLargeImageUrl());
					}
					if(deal1.getDealUrl() != null)
					{
						dealModel.setWebsiteUrl(deal1.getDealUrl());
					}

					dealModel.setItemType("Deal");
					dealsModeList.add(dealModel);
				}
			}
		}

		List<EventModel> eventsModelList = new ArrayList<EventModel>();
		for(Events events : eventsList)
		{
			EventModel eventModel = new EventModel();
			// Check if event is already favorite or not
			Eventuserlikes eventProperty = userDAO.getUserEventProperties(searchRequest.getUserId(), events.getEventId());
			if(eventProperty != null)
			{
				eventModel.setIsFavorite(eventProperty.getIsFavorite());
				//Checking suggestion Criteria
				if(eventProperty.getIsFavorite())
					eventModel.setSuggestionCriteria("favorite");
				else if(eventProperty.getIsLike())
					eventModel.setSuggestionCriteria("like");
				else if(eventProperty.getIsVisited())
					eventModel.setSuggestionCriteria("visited");
			}
			else
				eventModel.setSuggestionCriteria("like");


			//List<CommunityReview> communityReviewList = eventsDAO.getCommunityReviewByPageId(events.getPage().getPageId());
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
				eventModel.setRating(avg);
			}*/
			double avgRating = eventsDAO.getCommunityReviewByPageId(events.getPage().getPageId());

			logger.debug("avg rating =>"+avgRating);
			eventModel.setRating(avgRating);
			eventModel.setEventId(events.getEventId());
			eventModel.setDescription(events.getDescription());
			eventModel.setVenue_name(events.getVenue_name());
			eventModel.setEvent_date(events.getEvent_date().toString());
			eventModel.setCity(events.getCity());
			eventModel.setTitle(events.getTitle());
			eventModel.setTotalTickets(events.getTotalTickets());
			eventModel.setUrlpath(events.getUrlpath());
			eventModel.setAncestorGenreDescriptions(events.getAncestorGenreDescriptions());
			eventModel.setItemType("Event");
			eventModel.setSource(events.getEventSource());
			eventModel.setCategory(events.getChannel().split(" ")[0]);
			logger.debug("eventModel.getCategory:: "+eventModel.getCategory());
			Set<Usereventsuggestion> usereventsuggestions = events.getUsereventsuggestions();

			for(Usereventsuggestion usereventsuggestionsObj : usereventsuggestions)
			{
				if(usereventsuggestionsObj.getUser().getEmailId().equals(searchRequest.getEmailId()))
				{
					//eventModel.setSuggestionType(usernondealsuggestionsObj.getSuggestionType());
					//Checking suggestion type
					if(usereventsuggestionsObj.getSuggestionType().contains("User"))
						eventModel.setSuggestedBy("Self");
					else if(usereventsuggestionsObj.getSuggestionType().contains("Friend") || usereventsuggestionsObj.getSuggestionType().contains("Group"))
						eventModel.setSuggestedBy("Friend");
					else if(usereventsuggestionsObj.getSuggestionType().equalsIgnoreCase("Wall Feed Suggestion") || usereventsuggestionsObj.getSuggestionType().equalsIgnoreCase("Direct Suggestion"))
						eventModel.setSuggestedBy("FriendSuggest");

					if(usereventsuggestionsObj.getUserContact() != null)
					{
						User suggestedUser = usereventsuggestionsObj.getUserContact();
						UserModel userModel = new UserModel();
						userModel.setFirstName(suggestedUser.getFirstName());
						userModel.setLastName(suggestedUser.getLastName());
						userModel.setImageUrl(hostPath+suggestedUser.getImageUrl());
						userModel.setUserId(suggestedUser.getUserId());
						eventModel.setSuggestingUser(userModel);
					}
					break;
				}
			}
			eventsModelList.add(eventModel);
		}

		logger.debug("dealsModeList:  "+dealsModeList.size() + " businessModelList: "+ businessModelList.size() + " eventsModelList: "+eventsModelList.size());
		SuggestionResponse suggestionResponse = new SuggestionResponse();
		suggestionResponse.setDealSuggestions(dealsModeList);
		suggestionResponse.setNonDealSuggestions(businessModelList);
		suggestionResponse.setEventSuggestions(eventsModelList);
		suggestionResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		suggestionResponse.setError("");
		return suggestionResponse;
	}

	@Transactional
	@Override
	public FriendListResponse getFriendRequestOfUser(int userId) {
		logger.debug("getFriendsOfUser =>"+userId);
		FriendListResponse response = new FriendListResponse();
		List<UserModel> userList = new ArrayList<UserModel>();
		List<User> friendRequestList = userDAO.getUserFriendRequests(userId);
		for(User friend: friendRequestList)
		{
			UserModel user =  new UserModel();
			user.setAddressline1(friend.getAddressline1());
			user.setBirthDate(friend.getBirthDate());
			user.setBirthMonth(friend.getBirthMonth());
			user.setBirthYear(friend.getBirthYear());
			user.setCity(friend.getCity());
			user.setConfirmationId(friend.getConfirmationId());
			user.setCountry(friend.getCountry());
			user.setCreatedtime(friend.getCreatedtime().toString());
			user.setEmailId(friend.getEmailId());
			user.setFirstName(friend.getFirstName());
			user.setGender(friend.getGender());
			user.setIsActive(friend.getIsActive());
			user.setIsDeleted(friend.getIsDeleted());
			user.setLastName(friend.getLastName());
			user.setMiddleName(friend.getMiddleName());
			user.setPassword(friend.getPassword());
			user.setState(friend.getState());
			user.setSessionId(friend.getSessionId());
			user.setStreet(friend.getStreet());
			user.setUpdatedtime(friend.getUpdatedtime().toString());
			user.setUserId(friend.getUserId());
			user.setZipcode(friend.getZipcode());
			if(friend.getImageUrl() != null)
				user.setImageUrl(hostPath+friend.getImageUrl());
			userList.add(user);
		}
		response.setUser(userList);
		return response;
	}
}
