package com.jeeyoh.service.userservice;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.utils.RandomGUID;
import com.jeeyoh.utils.Utils;
import com.jeeyoh.model.responce.LoginResponce;

@Component("userService")
public class UserService implements IUserService{
	
	private static Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	private IUserDAO userDAO;

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
		RandomGUID myGUID = new RandomGUID();
		String confirmationId = myGUID.toString();
		user.setConfirmationId(confirmationId);
		UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse();
		userRegistrationResponse.setConfirmationId(confirmationId);
		userDAO.registerUser(user);
		return userRegistrationResponse;
	}

	@Override
	public LoginResponce loginUser(UserModel user) {
		// TODO Auto-generated method stub
		User user1 = userDAO.loginUser(user);
		LoginResponce loginRespoce = new LoginResponce();
		if(user1 != null)
		{
			
			loginRespoce.setUser(user);
			loginRespoce.setStatus("OK");
			loginRespoce.setError("");
			
		}
		else
		{
			loginRespoce.setUser(null);
			loginRespoce.setStatus("OK");
			loginRespoce.setError("Invalid credentials");
		}
		return loginRespoce;
		
	}

}
