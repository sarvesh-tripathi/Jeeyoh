package com.jeeyoh.service.userservice;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeeyoh.model.responce.LoginResponce;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.User;

public class UserService implements IUserService {
	
	@Autowired
	IUserDAO userDAO;

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

	@Override
	public void registerUser(UserModel user) {
		// TODO Auto-generated method stub
		
	}

}
