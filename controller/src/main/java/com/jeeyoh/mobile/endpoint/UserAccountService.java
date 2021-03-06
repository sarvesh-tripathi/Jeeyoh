package com.jeeyoh.mobile.endpoint;

import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.funboard.FunBoardModel;
import com.jeeyoh.model.funboard.FunBoardRequest;
import com.jeeyoh.model.funboard.MediaContenModel;
import com.jeeyoh.model.funboard.SaveShareWallRequest;
import com.jeeyoh.model.funboard.WallFeedRequest;
import com.jeeyoh.model.response.AddGroupButtonResponse;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CategoryLikesResponse;
import com.jeeyoh.model.response.CategoryResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.FriendListResponse;
import com.jeeyoh.model.response.FunBoardDetailResponse;
import com.jeeyoh.model.response.FunBoardResponse;
import com.jeeyoh.model.response.GroupDetailResponse;
import com.jeeyoh.model.response.GroupListResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.SuggestionResponse;
import com.jeeyoh.model.response.TopSuggestionResponse;
import com.jeeyoh.model.response.UploadMediaServerResponse;
import com.jeeyoh.model.response.UserFriendsGroupResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.response.UserResponse;
import com.jeeyoh.model.response.WallFeedResponse;
import com.jeeyoh.model.search.AddGroupModel;
import com.jeeyoh.model.search.CategoryModel;
import com.jeeyoh.model.search.DirectSuggestionModel;
import com.jeeyoh.model.search.FavoriteRequestModel;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.search.SearchRequest;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.notification.service.IMessagingEventPublisher;
import com.jeeyoh.service.addgroup.IAddGroupService;
import com.jeeyoh.service.funboard.IFunBoardService;
import com.jeeyoh.service.search.IAddDirectSuggestionService;
import com.jeeyoh.service.userservice.IMediaService;
import com.jeeyoh.service.userservice.IUserService;
import com.jeeyoh.service.wallfeed.IWallFeedSharingService;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("/userService")
public class UserAccountService {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@InjectParam
	IUserService userService;

	@InjectParam
	IMessagingEventPublisher eventPublisher;

	@InjectParam
	IFunBoardService funBoardService;

	@InjectParam
	IMediaService mediaService;

	@InjectParam
	IAddGroupService addGroupService;

	@InjectParam
	IAddDirectSuggestionService addDirectSuggestionService;

	@InjectParam
	IWallFeedSharingService wallFeedSharingService;

	@Path("/test/{name}")
	@GET
	public Response  test(@PathParam("name") String name)
	{
		logger.debug("Enter in mobile app "+name);
		return Response.status(200).entity(name).build();
	}

	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse login(UserModel user)
	{
		logger.debug("Enter in mobile app "+user);
		//String password = user.getPassword();
		//user.setPassword(Utils.MD5(password));		
		LoginResponse loginResponce = userService.loginUser(user);
		return loginResponce;
	}

	@Path("/regiteration")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserRegistrationResponse registration(UserModel user)
	{
		logger.debug("Enter in mobile app "+user.getFirstName());
		UserRegistrationResponse userRegistrationResponse = null;
		String confirmationCode = null;

		if(user != null)
		{

			logger.debug("Enter in mobile app 2222");
			BaseResponse  baseResponse  = userService.isEmailExist(user);
			if(baseResponse.getStatus().equals("OK"))
			{
				userRegistrationResponse    = userService.registerUser(user);
				if(userRegistrationResponse != null)
				{
					confirmationCode = userRegistrationResponse.getConfirmationId();
					userRegistrationResponse.setStatus(ServiceAPIStatus.OK.getStatus());
					logger.debug("confirmation code "+confirmationCode);
					//eventPublisher.sendConfirmationEmail(user,confirmationCode);//gaurav told not email confirmation
				}
			}
			else
			{
				userRegistrationResponse = new UserRegistrationResponse();
				boolean isActive = userService.isUserActive(user);
				userRegistrationResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
				if(isActive)
				{
					userRegistrationResponse.setError("Email Exist");
					userRegistrationResponse.setErrorType(ServiceAPIStatus.EMAILEXIST.getStatus());
				}
				else
				{
					userRegistrationResponse.setError("Please first confirm");
					userRegistrationResponse.setErrorType(ServiceAPIStatus.INACTIVE.getStatus());
				}


			}
		}
		return userRegistrationResponse;
	}

	@Path("/logout")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse logout(UserModel user)
	{
		BaseResponse  baseResponce = userService.logoutUser(user);
		return baseResponce;
	}

	/*
	 * 
	 */

	@Path("/changePassword")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse chnagePassword(UserModel user)
	{
		logger.debug("change passwor ::");
		BaseResponse baseResponse = userService.changePassword(user);
		return baseResponse;
	}
	@GET
	@Path("/confirmEmail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse confirmEmail(@QueryParam("confirmationCode") String confirmationCode)
	{
		logger.debug("IN CONFIRMATION MAIL API");
		BaseResponse baseResponse = userService.confirmUser(confirmationCode);		
		return baseResponse;

	}
	@GET
	@Path("/forgetPassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse forgetPassword(@QueryParam("emailId") String emailId)
	{
		logger.debug("forgetPassword Respose ::: "+emailId);
		BaseResponse baseResponse = userService.forgetPassword(emailId);
		return baseResponse;

	}

	@POST
	@Path("/getProfile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CategoryResponse getProfile(UserModel user)
	{
		CategoryResponse categoryResponse = userService.getUserProfile(user);
		return categoryResponse;

	}

	@GET
	@Path("/addFavourites")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CategoryResponse addFavourites(@QueryParam("category") String category,@QueryParam("userId") int userId)
	{
		logger.debug("Category Respose category ::: "+category);
		logger.debug("Category Respose userId   ::: "+userId);
		CategoryResponse categoryResponse = null;
		if(userId != 0 )
		{
			categoryResponse = userService.addFavourite(category, userId);
		}
		else
		{
			categoryResponse = new CategoryResponse();
			categoryResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}

		logger.debug("Responce ::::: "+categoryResponse.getStatus());
		return categoryResponse;

	}

	@POST
	@Path("/saveFavourite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse saveFavourite(PageModel page)
	{
		BaseResponse baseResponse = new BaseResponse();
		long count  = userService.userFavouriteCount(page);
		logger.debug("Total count ::: "+count);
		if(count < 10)
		{
			baseResponse = userService.saveUserFavourite(page);
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Already added 10 favourite in this category");
		}
		return baseResponse;

	}

	@GET
	@Path("/deleteFavourite")
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse deleteFavourite(@QueryParam("pageId") int pageId,@QueryParam("userId") int userId)
	{
		BaseResponse baseResponse = userService.deleteFavourite(pageId, userId);
		logger.debug("Responce ::::: "+baseResponse.getStatus());
		return baseResponse;

	}
	
	@GET
	@Path("/getFavourites")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CategoryResponse getFavourites(@QueryParam("category") String category,@QueryParam("userId") int userId)
	{
		CategoryResponse categoryResponse = null;
		if(category != null && userId != 0 )
		{
			categoryResponse = userService.getFavourites(category, userId);
		}
		else
		{
			categoryResponse = new CategoryResponse();
			categoryResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}
		return categoryResponse;
	}


	@GET
	@Path("/getCategoriesForLike")
	@Produces(MediaType.APPLICATION_JSON)
	public CategoryLikesResponse getCategoriesForLike(@QueryParam("userId") int userId,@QueryParam("category") String category)
	{
		logger.debug("Enter in categoey mobile api getCategoriesForLike111 :: "+userId + "category type ::: "+category);
		CategoryLikesResponse categoryLikesResponse = new CategoryLikesResponse();
		if(userId != 0 || category != null)
		{
			categoryLikesResponse = userService.getCategoryForCaptureLikes(userId,category);
		}
		else
		{
			categoryLikesResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}
		return categoryLikesResponse;


	}
	@POST
	@Path("/saveUserCategoriesLike")
	@Consumes(MediaType.APPLICATION_JSON)
	public BaseResponse saveUserLike(CategoryModel categoryModel)
	{
		logger.debug("save category like"+categoryModel);
		BaseResponse baseResponse = userService.saveUserCategoryLikes(categoryModel);
		return baseResponse;

	}


	@POST
	@Path("/getuserSuggestions")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SuggestionResponse getUserSuggestions(SearchRequest searchRequest)
	{
		SuggestionResponse response = userService.getUserSuggestions(searchRequest);
		return response;
	}

	@POST
	@Path("/getuserTopSuggestions")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TopSuggestionResponse getUserTopSuggestions(UserModel user)
	{
		TopSuggestionResponse response = userService.getUserTopSuggestions(user);
		logger.debug("response: "+response.getStatus());
		return response;
	}


	@POST
	@Path("/saveFunBoardItems")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse saveFunBoardItems(FunBoardRequest request)
	{
		BaseResponse response = funBoardService.saveFunBoardItem(request);
		return response;
	}


	@POST
	@Path("/funboard/addComment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CommentResponse addFunBoardComment(CommentModel request)
	{
		CommentResponse response = funBoardService.addFunBoardComment(request);
		return response;
	}

	@POST
	@Path("/funboard/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public UploadMediaServerResponse uploadFileForFunBoard(@FormDataParam("photoPath") InputStream photoInputStream,
			@FormDataParam("photoPath") FormDataContentDisposition photoFileDetail,
			@FormDataParam("userId") int userId, @FormDataParam("mediaType") String mediaType,
			@FormDataParam("title") String title,@FormDataParam("funBoardId") int funBoardId)
	{

		MediaContenModel mediaContenModel = new MediaContenModel();
		mediaContenModel.setFunBoardId(funBoardId);
		mediaContenModel.setMediaType(mediaType);
		mediaContenModel.setUserId(userId);

		UploadMediaServerResponse uploadMediaServerResponse = new UploadMediaServerResponse();
		boolean isMediaProcessedAndUploaded = true;
		String mediaProcessingError = "";

		if (photoInputStream != null && photoFileDetail != null)
		{
			uploadMediaServerResponse = mediaService.uploadOnServer(photoInputStream,
					photoFileDetail.getFileName(), userId+"");
			if (uploadMediaServerResponse != null
					&& !uploadMediaServerResponse.getMediaUrl().equalsIgnoreCase("ERROR"))
			{
				mediaContenModel.setImageUrl(uploadMediaServerResponse.getMediaUrl());
				logger.debug("ContentService --> uploadImage --> fearture Image url ::"
						+ uploadMediaServerResponse.getMediaUrl());
			}
			else
			{
				isMediaProcessedAndUploaded = false;
				mediaProcessingError = uploadMediaServerResponse.getMediaUrl();
				logger.debug("ContentService --> uploadImage -->  mediaProcessingError ::"
						+ mediaProcessingError);
			}
		}

		if (isMediaProcessedAndUploaded)
		{
			UploadMediaServerResponse response = funBoardService.uploadMediaContent(mediaContenModel);
			uploadMediaServerResponse.setMediaUrl(response.getMediaUrl());
		}
		else
		{
			uploadMediaServerResponse.setError("Task Failed");
			uploadMediaServerResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}
		return uploadMediaServerResponse;
	}


	@POST
	@Path("/deleteFunBoard")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse deleteFunBoardItem(FunBoardRequest request)
	{
		BaseResponse response = funBoardService.deleteFunBoarditem(request);
		return response;
	}

	@POST
	@Path("/getFunBoardItems")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FunBoardResponse getFunBoardItems(UserModel user)
	{
		FunBoardResponse response = funBoardService.getUserFunBoardItems(user);
		return response;
	}

	@POST
	@Path("/getFunBoardItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FunBoardDetailResponse getFunBoardItem(FunBoardRequest request)
	{
		FunBoardDetailResponse response = funBoardService.getFunBoardItem(request);
		return response;
	}

	
	@POST
	@Path("/uploadProfileImage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public UploadMediaServerResponse uploadProfileImage(@FormDataParam("photoPath") InputStream photoInputStream,
			@FormDataParam("photoPath") FormDataContentDisposition photoFileDetail,
			@FormDataParam("userId") int userId)
	{
		MediaContenModel mediaContenModel = new MediaContenModel();
		mediaContenModel.setUserId(userId);
		
		UploadMediaServerResponse uploadMediaServerResponse = new UploadMediaServerResponse();
		boolean isMediaProcessedAndUploaded = true;
		String mediaProcessingError = "";

		if (photoInputStream != null && photoFileDetail != null)
		{
			uploadMediaServerResponse = mediaService.uploadOnServer(photoInputStream,
					photoFileDetail.getFileName(), userId+"");
			if (uploadMediaServerResponse != null
					&& !uploadMediaServerResponse.getMediaUrl().equalsIgnoreCase("ERROR"))
			{
				mediaContenModel.setImageUrl(uploadMediaServerResponse.getMediaUrl());
				logger.debug("ContentService --> uploadImage --> fearture Image url ::"
						+ uploadMediaServerResponse.getMediaUrl());
			}
			else
			{
				isMediaProcessedAndUploaded = false;
				mediaProcessingError = uploadMediaServerResponse.getMediaUrl();
				logger.debug("ContentService --> uploadImage -->  mediaProcessingError ::"
						+ mediaProcessingError);
			}
		}

		if (isMediaProcessedAndUploaded)
		{
			UploadMediaServerResponse response = userService.uploadProfileImage(mediaContenModel);
			uploadMediaServerResponse.setMediaUrl(response.getMediaUrl());
		}
		else
		{
			uploadMediaServerResponse.setError("Task Failed");
			uploadMediaServerResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}

		logger.debug("Response: "+uploadMediaServerResponse.getMediaUrl());
		return uploadMediaServerResponse;

	}

	@POST
	@Path("/addGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse addGroup(AddGroupModel addGroupModel)
	{
		logger.debug("Enter in categoey mobile api :: "+addGroupModel.getGroupName());
		BaseResponse baseResponse = addGroupService.addGroup(addGroupModel);
		return baseResponse;

	}

	@GET
	@Path("/addGroupButton")
	@Produces(MediaType.APPLICATION_JSON)
	public AddGroupButtonResponse addGroupButton(@QueryParam("userId") int userId)
	{
		logger.debug("Enter in addGroupButton api :: "+userId);
		AddGroupButtonResponse addGroupButtonResponse = addGroupService.addGroupPage(userId);
		return addGroupButtonResponse;

	}

	@POST
	@Path("/addEventSuggestion")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse addEventSuggestion(DirectSuggestionModel directSuggestionModel)
	{
		//logger.debug("addEventSuggestion => userid => "+eventSuggestionModel.getUserId()+" ;friend list => "+eventSuggestionModel.getFriendsIdList()+" ;category => "+eventSuggestionModel.getCategory()+" ;suggestion id => "+eventSuggestionModel.getSuggestionId()+" ;suggestion type =>"+eventSuggestionModel.getSuggestionType());
		BaseResponse response = addDirectSuggestionService.addSuggestions(directSuggestionModel.getUserId(), directSuggestionModel.getFriendsIdList(), directSuggestionModel.getGroups(), directSuggestionModel.getSuggestionId(), directSuggestionModel.getCategory(), directSuggestionModel.getSuggestionType(), directSuggestionModel.getSuggestedTime());
		return response;
	}

	@POST
	@Path("/updateProfile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public UserResponse updateProfile(@FormDataParam("photoPath") InputStream photoInputStream,
			@FormDataParam("photoPath") FormDataContentDisposition photoFileDetail,
			@FormDataParam("userId") String userId, @FormDataParam("mediaType") String mediaType,
			@FormDataParam("firstName") String firstName,@FormDataParam("lastName") String lastName,@FormDataParam("dateOfBirth") Date dateOfBirth,@FormDataParam("address") String address)
	{

		UserModel userModel = new UserModel();
		userModel.setAddressline1(address);
		userModel.setFirstName(firstName);
		userModel.setLastName(lastName);
		userModel.setBirthDate(dateOfBirth.getDate());
		userModel.setBirthMonth(dateOfBirth.getMonth());
		userModel.setBirthYear(dateOfBirth.getYear());
		userModel.setUserId(Integer.parseInt(userId));

		UserResponse userResponse = new UserResponse();

		UploadMediaServerResponse uploadMediaServerResponse = new UploadMediaServerResponse();
		boolean isMediaProcessedAndUploaded = true;
		String mediaProcessingError = "";

		if (photoInputStream != null && photoFileDetail != null)
		{
			uploadMediaServerResponse = mediaService.uploadOnServer(photoInputStream,
					photoFileDetail.getFileName(), userId);
			if (uploadMediaServerResponse != null
					&& !uploadMediaServerResponse.getMediaUrl().equalsIgnoreCase("ERROR"))
			{
				userModel.setImageUrl(uploadMediaServerResponse.getMediaUrl());
				logger.debug("ContentService --> createStroyline --> fearture Image url ::"
						+ uploadMediaServerResponse.getMediaUrl());
			}
			else
			{
				isMediaProcessedAndUploaded = false;
				mediaProcessingError = uploadMediaServerResponse.getMediaUrl();
				logger.debug("ContentService --> createStroyline -->  mediaProcessingError ::"
						+ mediaProcessingError);
			}
		}

		if (isMediaProcessedAndUploaded)
		{
			userResponse = userService.editUserProfile(userModel);
		}
		else
		{
			userResponse.setError("Task Failed");
			userResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}
		return userResponse;
	}

	
	@POST
	@Path("/editProfile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserResponse editProfile(UserModel user)
	{
		UserResponse userResponse = new UserResponse();
		userResponse = userService.editUserProfile(user);
		return userResponse;
	}


	@POST
	@Path("/updatePrivacySetting")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse updatePrivacySetting(UserModel userModel)
	{
		logger.debug("updatePrivacySetting => userid => "+userModel.getUserId()+" ;IsShareProfileWithGroup => "+userModel.getIsShareProfileWithGroup()+" ;IsShareProfileWithFriend() => "+userModel.getIsShareProfileWithFriend()+" ;IsShareCommunity => "+userModel.getIsShareCommunity());
		BaseResponse response = userService.updatePrivacySetting(userModel);
		return response;
	}


	@POST
	@Path("/makeFavorite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse makeFavorite(FavoriteRequestModel requestModel)
	{
		BaseResponse response =null;
		if(requestModel.getUserId()!=0 && requestModel.getItemId()!=0)
			response =userService.saveFavoriteItem(requestModel.getUserId(), requestModel.getItemId(), requestModel.getItemType(), requestModel.getIsFav());
		return response;
	}

	@POST
	@Path("/getFriendsList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FriendListResponse getFriendsList(UserModel user)
	{
		FriendListResponse response = new FriendListResponse();
		if(user.getUserId() != null && user.getUserId() !=0)
		{
			response = userService.getFriendsOfUser(user.getUserId());
		}
		return response;
	}

	@POST
	@Path("/wallFeed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse wallFeed(WallFeedRequest wallFeedModel)
	{
		logger.debug("wallFeed => userid => "+wallFeedModel.getUserId()+" ;SharedWithUserList => "+wallFeedModel.getFriends()+" ;SharedfunBoardItemsList() => "+wallFeedModel.getSharedfunBoardItemsList());
		BaseResponse response = wallFeedSharingService.saveWallFeedSharingData(wallFeedModel);
		return response;
	}

	@GET
	@Path("/getPrivacySetting")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserResponse getPrivacySetting(@QueryParam("userId") int userId)
	{
		UserResponse response = userService.getPrivacySetting(userId);
		return response;
	}

	@GET
	@Path("/shareWith")
	@Produces(MediaType.APPLICATION_JSON)
	public UserFriendsGroupResponse shareWith(@QueryParam("userId") int userId)
	{
		UserFriendsGroupResponse userFriendsGroupResponse = userService.getUserFriendsAndGroup(userId);
		return userFriendsGroupResponse;

	}
	@GET
	@Path("/getUserWall")
	@Produces(MediaType.APPLICATION_JSON)
	public WallFeedResponse getUserWall(@QueryParam("userId") int userId)
	{
		logger.debug("Category Respose ::: "+userId);
		WallFeedResponse userFriendsGroupResponse = wallFeedSharingService.getUserWallFeed(userId);
		logger.debug("Responce ::::: "+userFriendsGroupResponse.getStatus());
		return userFriendsGroupResponse;

	}
	@POST
	@Path("/saveShareWallFeed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse saveShareWallFeed(SaveShareWallRequest request)
	{
		logger.debug(" SAVE USER WALL :: ");
		BaseResponse response = wallFeedSharingService.saveShareWallFeedRecord(request);
		return response;
	}
	@POST
	@Path("/addWallFeedSuggestion")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse addWallFeedSuggestion(SaveShareWallRequest request)
	{
		logger.debug(" SAVE USER WALL :: ");
		BaseResponse response = wallFeedSharingService.addWallFeedSuggestions(request);
		return response;
	}

	@POST
	@Path("/wallFeed/addComment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CommentResponse addWallFeedComment(CommentModel request)
	{
		CommentResponse response = wallFeedSharingService.saveWallFeedComments(request);
		return response;
	}


	@GET
	@Path("/getWallFeedComments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WallFeedResponse getWallFeedComments(@QueryParam("userId") int userId, @QueryParam("wallFeedId") int wallFeedId)
	{
		WallFeedResponse wallFeedResponse = null;
		logger.debug("userId1111 =>"+userId+"; wallFeedId =>"+wallFeedId);
		if(userId!=0 && wallFeedId!=0)
			wallFeedResponse = wallFeedSharingService.getWallFeedComments(userId, wallFeedId);
		logger.debug("wallFeedResponse =>"+wallFeedResponse);
		return wallFeedResponse;
	}
	
	@POST
	@Path("/updateFunBoard")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse updateFunBoard(FunBoardModel funBoardModel)
	{
		BaseResponse response = new BaseResponse();
		response = funBoardService.updateFunBoard(funBoardModel);
		return response;
	}
	
	
	@GET
	@Path("/getGroupDetail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GroupDetailResponse getGroupDetail(@QueryParam("groupId") int groupId)
	{
		GroupDetailResponse response = addGroupService.getGroupDetails(groupId);
		return response;
	}
	
	
	@POST
	@Path("/getGroupList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GroupListResponse getGroupList(UserModel user)
	{
		GroupListResponse response = new GroupListResponse();
		if(user.getUserId() != null && user.getUserId() !=0)
		{
			response = addGroupService.getGroupList(user.getUserId());
		}
		return response;
	}
	
	@POST
	@Path("/editGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse editGroup(AddGroupModel addGroupModel)
	{
		logger.debug("Enter in editGroup :: "+addGroupModel.getGroupName());
		BaseResponse baseResponse = addGroupService.editGroup(addGroupModel);
		return baseResponse;
	}
	
	@POST
	@Path("/getFriendRequestsList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FriendListResponse getFriendRequestsList(UserModel user)
	{
		FriendListResponse response = new FriendListResponse();
		if(user.getUserId() != null && user.getUserId() !=0)
		{
			response = userService.getFriendRequestOfUser(user.getUserId());
		}
		return response;
	}
}
