package com.jeeyoh.service.userservice;

import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CategoryResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.SuggestionResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.response.UserResponse;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.user.UserModel;

public interface IUserService {
	
	public UserRegistrationResponse registerUser(UserModel user);
	public LoginResponse loginUser(UserModel user);
	public BaseResponse logoutUser(UserModel user);
	public BaseResponse changePassword(UserModel user);
	public BaseResponse confirmUser(String confirmationCode);
	public BaseResponse isEmailExist(UserModel user);
	public CategoryResponse getUserProfile(UserModel user);
	public BaseResponse saveUserFavourite(PageModel page);
	public CategoryResponse addFavourite(String category);
	public BaseResponse deleteFavourite(int id, int userId);
	public SuggestionResponse getUserSuggestions(UserModel user);
	public BaseResponse forgetPassword(String emailId);
	public boolean isUserActive(UserModel user);


}
