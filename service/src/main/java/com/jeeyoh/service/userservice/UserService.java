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

import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CategoryResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.SuggestionResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.response.UserResponse;
import com.jeeyoh.model.search.BusinessModel;
import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Privacy;
import com.jeeyoh.persistence.domain.Profiletype;
import com.jeeyoh.persistence.domain.User;
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

	@Transactional
	@Override
	public UserRegistrationResponse registerUser(UserModel userModel) {

		logger.debug("registerUser.......");
		User user = new User();
		user.setFirstName(userModel.getFirstName());
		user.setPassword(Utils.MD5(userModel.getPassword()));
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
		user.setMiddleName(userModel.getMiddleName());
		user.setIsActive(false);
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
		LoginResponse loginRespoce = new LoginResponse();
		if(user1 != null)
		{

			RandomGUID myGUID = new RandomGUID();
			String sessionId = myGUID.toString();
			logger.debug("In LOGIN sessionId "+sessionId);
			user1.setSessionId(sessionId);
			userDAO.updateUser(user1);
			user.setSessionId(sessionId);
			loginRespoce.setUser(user);
			loginRespoce.setStatus("OK");
			loginRespoce.setError("");

		}
		else
		{
			loginRespoce.setUser(null);
			loginRespoce.setStatus("NO");
			loginRespoce.setError("Invalid credentials");
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
		user.setPassword(Utils.MD5(userModel.getPassword()));
		userDAO.updateUser(user);
		baseResponse.setStatus("OK");
		return baseResponse;
	}

	@Transactional
	@Override
	public BaseResponse confirmUser(String confirmationCode) {
		// TODO Auto-generated method stub
		BaseResponse baseResponse = new BaseResponse();
		userDAO.confirmUser(confirmationCode);
		baseResponse.setStatus("OK");
		return baseResponse;
	}

	@Override
	public BaseResponse isEmailExist(UserModel user) {
		// TODO Auto-generated method stub

		User user1 = userDAO.getUsersById(user.getEmailId());
		BaseResponse baseResponse = new BaseResponse();
		if(user1 == null)
		{
			baseResponse.setStatus("OK");

		}
		else
		{
			baseResponse.setStatus("FAIL");
			baseResponse.setError("Already Registered");
		}
		return baseResponse;
	}

	@Override
	public UserResponse getUserProfile(UserModel user) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public CategoryResponse addFavourite(String category) {
		// TODO Auto-generated method stub
		List<Page> page = eventsDAO.getCommunityPageByCategoryType(category);
		return null;
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
		return suggestionResponse;
	}

}
