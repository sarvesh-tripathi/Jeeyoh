package com.jeeyoh.service.userservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CategoryLikesResponse;
import com.jeeyoh.model.response.CategoryResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.SuggestionResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.search.BusinessModel;
import com.jeeyoh.model.search.CategoryModel;
import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.notification.service.IMessagingEventPublisher;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Notificationpermission;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.Privacy;
import com.jeeyoh.persistence.domain.Profiletype;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.utils.RandomGUID;
import com.jeeyoh.utils.Utils;


@Component("userService")
public class UserService implements IUserService{

	private static Logger logger = LoggerFactory.getLogger("debugLogger");

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
		UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse();
		userRegistrationResponse.setConfirmationId(confirmationId);
		userDAO.registerUser(user);
		return userRegistrationResponse;
	}

	@Transactional
	@Override
	public LoginResponse loginUser(UserModel user) {
		// TODO Auto-generated method stub
		logger.debug("In LOGIN SERVICE");
		User user1 = userDAO.loginUser(user);
		logger.debug("User in Login Response :: "+user1);
		LoginResponse loginRespoce = new LoginResponse();
		if(user1 != null)
		{

			RandomGUID myGUID = new RandomGUID();
			String sessionId = myGUID.toString();
			logger.debug("In LOGIN sessionId "+sessionId);
			user1.setSessionId(sessionId);
			userDAO.updateUser(user1);
			user.setSessionId(sessionId);
			user.setUserId(user1.getUserId());
			loginRespoce.setUser(user);
			loginRespoce.setStatus(ServiceAPIStatus.OK.getStatus());
			loginRespoce.setError("");

		}
		else
		{
			loginRespoce.setUser(user);			
			User user_1 = userDAO.getUsersById(user.getEmailId());
			 loginRespoce.setStatus(ServiceAPIStatus.FAILED.getStatus());
			 if(user_1 != null)
			 {
				    loginRespoce.setErrorType(ServiceAPIStatus.PASSWORD.getStatus());
					loginRespoce.setError("Invalid password");
			 }
			 else
			 {
				    loginRespoce.setErrorType(ServiceAPIStatus.EMAIL.getStatus());
					loginRespoce.setError("Invalid email");
			 }
			
			
		}
		return loginRespoce;

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
		User user = userDAO.getUsersById(userModel.getEmailId());
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
		
		User user1 = userDAO.getUsersById(user.getEmailId());
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
		if(sports != null || movies != null ||foods != null )
		{
			categoryResponse.setSport(sports);
			categoryResponse.setFood(foods);
			categoryResponse.setMovie(movies);
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
	public CategoryResponse addFavourite(String category,int userId) {
		logger.debug("ADD FAV :: ");
		CategoryResponse categoryResponse = new CategoryResponse();
		List<Page> pages = eventsDAO.getCommunityPageByCategoryType(category, userId);
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
		if(sports != null || movies != null ||foods != null )
		{
			categoryResponse.setSport(sports);
			categoryResponse.setFood(foods);
			categoryResponse.setMovie(movies);
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
		logger.debug("Page Id :: "+page.getPageId());
		Pageuserlikes  pageuserlikes = userDAO.isPageExistInUserProfile(page.getUserId(),page.getPageId());
		logger.debug("page user like :: "+pageuserlikes);
		BaseResponse baseResponse = new BaseResponse();
		if(pageuserlikes != null)
		{
			logger.debug("Update if already");
			pageuserlikes.setIsFavorite(true);
			userDAO.updateUserCommunity(pageuserlikes);			
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			Pageuserlikes pageUserLike = new Pageuserlikes();
			List<User> user = userDAO.getUserById(page.getUserId());
			logger.debug("User OBJ :: "+user.get(0));
			Page page1 = eventsDAO.getPageDetailsByID(page.getPageId());
			logger.debug("Page OBJ :: "+page1);
			pageUserLike.setUser(user.get(0));
			pageUserLike.setPage(page1);
			pageUserLike.setCreatedtime(new Date());
			pageUserLike.setIsFavorite(true);
			pageUserLike.setIsFollowing(false);
			pageUserLike.setIsLike(false);
			pageUserLike.setIsProfileHidden(false);
			pageUserLike.setIsVisited(false);
			pageUserLike.setUpdatedtime(new Date());
			Notificationpermission notificationpermission = userDAO.getDafaultNotification();
			logger.debug("Notification  :: "+notificationpermission);
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
		//Get user's non deal suggestions
		List<Business> userNonDealSuggestions = businessDAO.getBusinessByCriteria(user.getEmailId(), null, null, null, null);
		logger.debug("userNonDealSuggestions ==> "+userNonDealSuggestions.size());
		//Get user's deal suggestions
		List<Userdealssuggestion> dealSuggestionList = dealsDAO.userDealsSuggestedByJeeyoh(null, null,null, user.getEmailId());
		logger.debug("dealSuggestionList ==> "+dealSuggestionList.size());
		//Get user's event suggestions
		List<Events> eventsList = eventsDAO.getEventsByCriteria(user.getEmailId(), null, null, null);
		logger.debug("EventsList ==> "+eventsList.size());

		List<BusinessModel> businessModelList = new ArrayList<BusinessModel>();
		for(Business business : userNonDealSuggestions)
		{
			BusinessModel businessModel = new BusinessModel();
			businessModel.setName(business.getName());
			businessModel.setWebsiteUrl(business.getWebsiteUrl());
			businessModel.setDisplayAddress(business.getDisplayAddress());
			businessModel.setCity(business.getCity());
			businessModel.setImageUrl(business.getImageUrl());
			businessModel.setBusinessType(business.getBusinesstype().getBusinessType());
			Set<Usernondealsuggestion> Usernondealsuggestions = business.getUsernondealsuggestions();

			for(Usernondealsuggestion usernondealsuggestionsObj : Usernondealsuggestions)
			{
				if(usernondealsuggestionsObj.getUser().getEmailId().equals(user.getEmailId()))
				{
					businessModel.setSuggestionType(usernondealsuggestionsObj.getSuggestionType());
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
					if(dealsuggestion.getDeals() != null)
					{
						DealModel dealModel = new DealModel();
						Deals deal1 = dealsuggestion.getDeals();
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

						dealsModeList.add(dealModel);
					}
				}
			}
		}

		List<EventModel> eventsModelList = new ArrayList<EventModel>();
		for(Events events : eventsList)
		{
			EventModel eventModel = new EventModel();
			eventModel.setDescription(events.getDescription());
			eventModel.setVenue_name(events.getVenue_name());
			eventModel.setEvent_date(events.getEvent_date());
			eventModel.setCity(events.getCity());
			eventModel.setTitle(events.getTitle());
			eventModel.setTotalTickets(events.getTotalTickets());
			eventModel.setAncestorGenreDescriptions(events.getAncestorGenreDescriptions());
			Set<Usereventsuggestion> usereventsuggestions = events.getUsereventsuggestions();

			for(Usereventsuggestion usernondealsuggestionsObj : usereventsuggestions)
			{
				if(usernondealsuggestionsObj.getUser().getEmailId().equals(user.getEmailId()))
				{
					eventModel.setSuggestionType(usernondealsuggestionsObj.getSuggestionType());
					break;
				}
			}
			eventsModelList.add(eventModel);
		}
		
		SuggestionResponse suggestionResponse = new SuggestionResponse();
		suggestionResponse.setDealSuggestions(dealsModeList);
		suggestionResponse.setNonDealSuggestions(businessModelList);
		suggestionResponse.setEventSuggestions(eventsModelList);
		suggestionResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		suggestionResponse.setError("");
		return suggestionResponse;
	}

	@Override
	@Transactional
	public BaseResponse forgetPassword(String emailId) {
		// TODO Auto-generated method stub
		User user = userDAO.getUsersById(emailId);
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
		// TODO Auto-generated method stub
		boolean isActive = userDAO.isUserActive(user.getEmailId());
		return isActive;
	}

	@Transactional
	@Override
	public long userFavouriteCount(PageModel page) {
		// TODO Auto-generated method stub
		
		long count = userDAO.getUserPageFavouriteCount(page.getPageType(),page.getUserId());
		return count;
	}

	@Transactional
	@Override
	public CategoryLikesResponse getCategoryForCaptureLikes(int userId) {
		// TODO Auto-generated method stub
		List<UserCategory> categoryList = userDAO.getUserNonLikeCategories(userId);
		CategoryLikesResponse categoryLikesResponse = new CategoryLikesResponse();
		if(categoryList != null)
		{
			logger.debug("Category list " +categoryList.get(0).getCategoryUrl());
			List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
			for(UserCategory category:categoryList)
			{
				CategoryModel categoryModel = new CategoryModel();
				categoryModel.setCategoryUrl(category.getCategoryUrl());
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
		// TODO Auto-generated method stub
		BaseResponse baseResponse = new BaseResponse();
		if(categoryModel.getUserCategoryId() != 0 && categoryModel.getUserId() != 0)
		{
			logger.debug("record save");
			UserCategoryLikes categoryLikes =  new UserCategoryLikes();
			List<User> user  = userDAO.getUserById(categoryModel.getUserId());
			categoryLikes.setUser(user.get(0));
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

}
