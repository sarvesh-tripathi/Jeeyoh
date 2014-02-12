package com.jeeyoh.service.userservice;

import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.model.responce.LoginResponce;

public interface IUserService {
	
	public UserRegistrationResponse registerUser(UserModel user);
	LoginResponce loginUser(UserModel user);

}
