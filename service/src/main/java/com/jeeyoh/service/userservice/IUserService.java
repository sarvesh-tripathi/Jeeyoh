package com.jeeyoh.service.userservice;

import com.jeeyoh.model.funboard.MediaContenModel;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CategoryLikesResponse;
import com.jeeyoh.model.response.CategoryResponse;
import com.jeeyoh.model.response.FriendListResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.SuggestionResponse;
import com.jeeyoh.model.response.TopSuggestionResponse;
import com.jeeyoh.model.response.UploadMediaServerResponse;
import com.jeeyoh.model.response.UserFriendsGroupResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.response.UserResponse;
import com.jeeyoh.model.search.CategoryModel;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.search.SearchRequest;
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
	public CategoryResponse addFavourite(String category,int userId);
	public BaseResponse deleteFavourite(int id, int userId);
	public SuggestionResponse getUserSuggestions(SearchRequest searchRequest);
	public BaseResponse forgetPassword(String emailId);
	public boolean isUserActive(UserModel user);
	public long userFavouriteCount(PageModel page);
	public CategoryLikesResponse getCategoryForCaptureLikes(int userId, String categoryType);
	public BaseResponse saveUserCategoryLikes(CategoryModel categoryModel);
	public TopSuggestionResponse getUserTopSuggestions(UserModel user);
	public UserResponse editUserProfile(UserModel userModel);
	public BaseResponse updatePrivacySetting(UserModel userModel);
	public BaseResponse saveFavoriteItem(int userId, int itemId, String itemType, boolean isFav);
	public FriendListResponse getFriendsOfUser(int userId);
	public UserResponse getPrivacySetting(int userId);
	public UserFriendsGroupResponse getUserFriendsAndGroup(int userId);
	public UploadMediaServerResponse uploadProfileImage(MediaContenModel mediaContenModel);
	public SuggestionResponse getuserSuggestionByCategory(SearchRequest searchRequest);
	public FriendListResponse getFriendRequestOfUser(int userId);
	public BaseResponse confirmEmail(String emailId, String confirmationId);
	public BaseResponse emailNotRegistered(String emailId);
	public UserRegistrationResponse registerBetaListUser(UserModel user);
	public CategoryResponse getFavourites(String category,int userId);

}
