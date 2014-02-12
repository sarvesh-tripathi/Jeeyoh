package com.jeeyoh.service.userservice;

import com.jeeyoh.model.responce.LoginResponce;
import com.jeeyoh.model.user.UserModel;

public interface IUserService {
	void registerUser(UserModel user);
	LoginResponce loginUser(UserModel user);
	
	

}
