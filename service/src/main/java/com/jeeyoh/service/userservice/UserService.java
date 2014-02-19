package com.jeeyoh.service.userservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CategoryResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Notificationpermission;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.Privacy;
import com.jeeyoh.persistence.domain.Profiletype;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.utils.RandomGUID;
import com.jeeyoh.utils.Utils;

@Component("userService")
public class UserService implements IUserService{
	
	private static Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IEventsDAO eventsDAO;

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
	@Transactional
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
	@Transactional
	public CategoryResponse getUserProfile(UserModel user) {
		// TODO Auto-generated method stub
		
		CategoryResponse categoryResponse = new CategoryResponse();
		List<Page> pages = eventsDAO.getUserFavourites(user.getUserId());
		logger.debug("First Page ::::: "+pages);
		List<PageModel> pageModels = new ArrayList<PageModel>();
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
				pageModels.add(pageModel);
			}
			
		}
		if(pageModels != null)
		{
			categoryResponse.setPageMode(pageModels);
			categoryResponse.setStatus("OK");
			categoryResponse.setError("SUCESS");
		}
		else
		{
			categoryResponse.setStatus("Fail");
			categoryResponse.setError("No community for these type");
		}
		return categoryResponse;
		
	}

	@SuppressWarnings( "null")
	@Override
	@Transactional
	public CategoryResponse addFavourite(String category) {
		// TODO Auto-generated method stub
		logger.debug("ADD FAV :: ");
		CategoryResponse categoryResponse = new CategoryResponse();
		List<Page> pages = eventsDAO.getCommunityPageByCategoryType(category);
		logger.debug("First Page ::::: "+pages);
		List<PageModel> pageModels = new ArrayList<PageModel>();
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
				pageModels.add(pageModel);
			}
			
		}
		if(pageModels != null)
		{
			categoryResponse.setPageMode(pageModels);
			categoryResponse.setStatus("OK");
			categoryResponse.setError("SUCESS");
		}
		else
		{
			categoryResponse.setStatus("Fail");
			categoryResponse.setError("No community for these type");
		}
		return categoryResponse;
	}

	@Override
	public BaseResponse saveUserFavourite(PageModel page) {
		// TODO Auto-generated method stub
		Pageuserlikes  pageuserlikes = userDAO.isPageExistInUserProfile(page.getUserId(),page.getPageId());
		BaseResponse baseResponse = new BaseResponse();
		if(pageuserlikes != null)
		{
			pageuserlikes.setIsFavorite(true);
			userDAO.updateUserCommunity(pageuserlikes);			
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			Pageuserlikes pageUserLike = new Pageuserlikes();
			List<User> user = userDAO.getUserById(page.getUserId());
			Page page1 = eventsDAO.getCommunityById(page.getPageId());
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
			pageUserLike.setNotificationpermission(notificationpermission);			
			userDAO.saveUserCommunity(pageUserLike);
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		return baseResponse;
	}

	@Override
	public BaseResponse deleteFavourite(int id, int userId) {
		// TODO Auto-generated method stub
		userDAO.deleteUserFavourity(id,userId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return baseResponse;
	}

}
