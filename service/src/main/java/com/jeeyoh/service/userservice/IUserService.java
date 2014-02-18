package com.jeeyoh.service.userservice;

import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CategoryResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.response.UserResponse;
import com.jeeyoh.model.user.UserModel;

public interface IUserService {
	
	public UserRegistrationResponse registerUser(UserModel user);
	public LoginResponse loginUser(UserModel user);
	public BaseResponse logoutUser(UserModel user);
	public BaseResponse changePassword(UserModel user);
	public BaseResponse confirmUser(String confirmationCode);
	public BaseResponse isEmailExist(UserModel user);
	public UserResponse getUserProfile(UserModel user);
	public CategoryResponse addFavourite(String category);

}
