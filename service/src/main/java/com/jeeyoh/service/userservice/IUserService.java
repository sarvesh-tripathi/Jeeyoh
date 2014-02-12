package com.jeeyoh.service.userservice;

import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.user.UserModel;

public interface IUserService {
	
	public UserRegistrationResponse registerUser(UserModel user);
	LoginResponse loginUser(UserModel user);

}
