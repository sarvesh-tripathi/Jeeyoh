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
	
	
	@GET
	@Path("/followPage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse saveIsFollowPage(@QueryParam("userId") int userId, @QueryParam("pageId") int pageId,@QueryParam("isFollow") boolean isFollow)
	{
		BaseResponse response =null;
		logger.debug("userId1111 =>"+userId+"; pageId =>"+pageId + "; isFollow => "+ isFollow);
		if(userId!=0 && pageId!=0)
			response =communitySearchService.saveIsFollowingPage(userId, pageId, isFollow);	
		return response;
	}
	
	
	@GET
	@Path("/makeFavorite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse saveIsFavEvent(@QueryParam("userId") int userId, @QueryParam("eventId") int eventId,@QueryParam("isFav") boolean isFav)
	{
		BaseResponse response =null;
		logger.debug("userId1111 =>"+userId+"; eventId =>"+eventId + " ; isFav => "+ isFav);
		if(userId!=0 && eventId!=0)
			response =communitySearchService.saveIsFavoriteEvent(userId, eventId, isFav);
		return response;
	}
	
	@GET
	@Path("/followEvent")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse saveIsFollowEvent(@QueryParam("userId") int userId, @QueryParam("eventId") int eventId,@QueryParam("isFollow") boolean isFollow)
	{
		BaseResponse response =null;
		logger.debug("userId1111 =>"+userId+"; eventId =>"+eventId + " ; isFav => "+ isFollow);
		if(userId!=0 && eventId!=0)
			response =communitySearchService.saveIsFollowingEvent(userId, eventId, isFollow);
		return response;
	}
	
	
	@GET
	@Path("/searchFriend")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FriendListResponse searchFriend(@QueryParam("location") String location, @QueryParam("name") String name, @QueryParam("userId") int userId)
	{
		logger.debug("searchFriend =>"+location+"; name =>"+name);
		FriendListResponse response = addFriendService.searchFriend(location, name, userId);
		return response;
	}
	
	@GET
	@Path("/sendFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse sendFriendRequest(@QueryParam("userId") int userId, @QueryParam("contactId") int contactId)
	{
		logger.debug("sendFriendRequest userId=>"+userId+"; contactId =>"+contactId);
		BaseResponse response =null;
		if(userId!=0 && contactId!=0)
		{
			response = addFriendService.sendFriendRequest(userId, contactId);
		}
		return response;
	}
	
	@GET
	@Path("/acceptFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse acceptFriendRequest(@QueryParam("userId") int userId, @QueryParam("contactId") int contactId)
	{
		logger.debug("sendFriendRequest userId=>"+userId+"; contactId =>"+contactId);
		BaseResponse response =null;
		if(userId!=0 && contactId!=0)
		{
			response = addFriendService.acceptFriendRequest(userId, contactId);
		}
		return response;
	}
	
	@GET
	@Path("/denyFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse denyFriendRequest(@QueryParam("userId") int userId, @QueryParam("contactId") int contactId, @QueryParam("isDeny") boolean isDeny)
	{
		logger.debug("sendFriendRequest userId=>"+userId+"; contactId =>"+contactId);
		BaseResponse response =null;
		if(userId!=0 && contactId!=0)
		{
			response = addFriendService.denyFriendRequest(userId, contactId);
		}
		return response;
	}
	
	@GET
	@Path("/blockFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse blockFriendRequest(@QueryParam("userId") int userId, @QueryParam("contactId") int contactId, @QueryParam("isBlock") boolean isBlock)
	{
		logger.debug("sendFriendRequest userId=>"+userId+"; contactId =>"+contactId);
		BaseResponse response =null;
		if(userId!=0 && contactId!=0)
		{
			response = addFriendService.denyFriendRequest(userId, contactId);
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

}
