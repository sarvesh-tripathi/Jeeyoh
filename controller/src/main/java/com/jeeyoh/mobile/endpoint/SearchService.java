package com.jeeyoh.mobile.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.CommunityResponse;
import com.jeeyoh.model.response.FriendListResponse;
import com.jeeyoh.model.response.MemoryCardResponse;
import com.jeeyoh.model.response.SearchResponse;
import com.jeeyoh.model.search.CommunityReviewModel;
import com.jeeyoh.model.search.FavoriteRequestModel;
import com.jeeyoh.model.search.FriendModel;
import com.jeeyoh.model.search.MemoryCardModel;
import com.jeeyoh.model.search.SearchRequest;
import com.jeeyoh.service.addfriend.IAddFriendService;
import com.jeeyoh.service.memorycard.IMemoryCardService;
import com.jeeyoh.service.search.ICommunitySearchService;
import com.jeeyoh.service.search.ISpotSearchService;
import com.sun.jersey.api.core.InjectParam;

@Path("/searchService")
public class SearchService {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@InjectParam
	ISpotSearchService spotSearchService;
	
	@InjectParam
	ICommunitySearchService communitySearchService;
	
	@InjectParam
	IAddFriendService addFriendService;
	
	@InjectParam
	IMemoryCardService memoryCardService;

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SearchResponse search(SearchRequest searchRequest)
	{
		SearchResponse response = spotSearchService.search(searchRequest);
		return response;
	}

	@GET
	@Path("/communityPageDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CommunityResponse communityPageDetails(@QueryParam("userId") int userId, @QueryParam("pageId") int pageId,@QueryParam("offset") int offset)
	{
		CommunityResponse communityResponse = null;
		logger.debug("userId1111 =>"+userId+"; pageId =>"+pageId+";offset =>"+offset);
		if(userId!=0 && pageId!=0)
			communityResponse = communitySearchService.searchCommunityDetails(userId, pageId,offset,10);
		logger.debug("communityResponse =>"+communityResponse);
		return communityResponse;
	}
	
	@POST
	@Path("/saveCommunityComments")
	@Produces(MediaType.APPLICATION_JSON)
	public CommentResponse saveCommunityComments(CommentModel commentModel)
	{
		logger.debug("saveCommunityComments :: "+commentModel);
		CommentResponse response = communitySearchService.saveCommunityComments(commentModel);
		return response;
		
	}
	
	
	@POST
	@Path("/followPage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse followPage(FavoriteRequestModel requestModel)
	{
		BaseResponse response = new BaseResponse();
		logger.debug("userId1111 =>"+requestModel.getUserId()+"; eventId =>"+requestModel.getItemId() + " ; isFav => "+ requestModel.getIsFollow());
		if(requestModel.getUserId()!=0 && requestModel.getItemId()!=0)
			response =communitySearchService.saveIsFollowingPage(requestModel.getUserId(), requestModel.getItemId(), requestModel.getIsFollow());	
		return response;
	}
	
	
	@POST
	@Path("/community/makeFavorite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse makeFavorite(FavoriteRequestModel requestModel)
	{
		BaseResponse response = new BaseResponse();
		logger.debug("userId1111 =>"+requestModel.getUserId()+"; eventId =>"+requestModel.getItemId() + " ; isFav => "+ requestModel.getIsFav());
		if(requestModel.getUserId()!=0 && requestModel.getItemId()!=0)
			response =communitySearchService.saveFavoritePage(requestModel.getUserId(), requestModel.getItemId(), requestModel.getIsFav());
		return response;
	}
	
	
	
	
	@POST
	@Path("/followEvent")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse saveIsFollowEvent(FavoriteRequestModel requestModel)
	{
		BaseResponse response = new BaseResponse();
		logger.debug("userId1111 =>"+requestModel.getUserId()+"; eventId =>"+requestModel.getItemId() + " ; isFav => "+ requestModel.getIsFollow());
		if(requestModel.getUserId()!=0 && requestModel.getItemId()!=0)
			response =communitySearchService.saveIsFollowingEvent(requestModel.getUserId(), requestModel.getItemId(), requestModel.getIsFollow());
		return response;
	}
	
	
	@POST
	@Path("/searchFriend")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FriendListResponse searchFriend(FriendModel friendModel)
	{
		logger.debug("searchFriend =>"+friendModel.getLocation()+"; name =>"+friendModel.getName());
		FriendListResponse response = addFriendService.searchFriend(friendModel.getLocation(), friendModel.getName(), friendModel.getUserId());
		return response;
	}
	
	@POST
	@Path("/sendFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse sendFriendRequest(FriendModel friendModel)
	{
		logger.debug("sendFriendRequest userId=>"+friendModel.getUserId()+"; contactId =>"+friendModel.getContactId());
		BaseResponse response =new BaseResponse();
		if(friendModel.getUserId()!=0 && friendModel.getContactId()!=0)
		{
			response = addFriendService.sendFriendRequest(friendModel.getUserId(), friendModel.getContactId());
		}
		return response;
	}
	
	@POST
	@Path("/acceptFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse acceptFriendRequest(FriendModel friendModel)
	{
		logger.debug("sendFriendRequest userId=>"+friendModel.getUserId()+"; contactId =>"+friendModel.getContactId());
		BaseResponse response =new BaseResponse();
		if(friendModel.getUserId()!=0 && friendModel.getContactId()!=0)
		{
			response = addFriendService.acceptFriendRequest(friendModel.getUserId(), friendModel.getContactId());
		}
		return response;
	}
	
	@POST
	@Path("/denyFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse denyFriendRequest(FriendModel friendModel)
	{
		logger.debug("sendFriendRequest userId=>"+friendModel.getUserId()+"; contactId =>"+friendModel.getContactId());
		BaseResponse response =new BaseResponse();
		if(friendModel.getUserId()!=0 && friendModel.getContactId()!=0)
		{
			response = addFriendService.denyFriendRequest(friendModel.getUserId(), friendModel.getContactId());
		}
		return response;
	}
	
	@POST
	@Path("/blockFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse blockFriendRequest(FriendModel friendModel)
	{
		logger.debug("sendFriendRequest userId=>"+friendModel.getUserId()+"; contactId =>"+friendModel.getContactId());
		BaseResponse response =new BaseResponse();
		if(friendModel.getUserId()!=0 && friendModel.getContactId()!=0)
		{
			response = addFriendService.blockFriendRequest(friendModel.getUserId(), friendModel.getContactId());
		}
		return response;
	}
	
	@POST
	@Path("/getMemoryCard")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MemoryCardResponse getMemoryCard(MemoryCardModel memoryCardRequest)
	{
		MemoryCardResponse response = memoryCardService.getMemoryCardDetails(memoryCardRequest);
		return response;
	}
	
	@POST
   	@Path("/communityReview")
   	@Consumes(MediaType.APPLICATION_JSON)
   	@Produces(MediaType.APPLICATION_JSON)
   	public BaseResponse communityReview(CommunityReviewModel communityReviewModel)
   	{
   		logger.debug("communityReview => userid => "+communityReviewModel.getUserId()+" ;pageid => "+communityReviewModel.getPageId()+ " ;rating => "+communityReviewModel.getRating()+" ;comment => "+communityReviewModel.getComment());
   		BaseResponse response = communitySearchService.saveCommunityReview(communityReviewModel);
   		return response;
   	}

}
