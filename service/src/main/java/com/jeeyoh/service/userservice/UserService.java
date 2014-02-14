package com.jeeyoh.service.userservice;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IUserDAO;
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

}
