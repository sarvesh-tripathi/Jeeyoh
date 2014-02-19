package com.jeeyoh.mobile.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeyoh.model.response.CommunityResponse;
import com.jeeyoh.model.response.SearchResponse;
import com.jeeyoh.model.search.SearchRequest;
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

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SearchResponse search(SearchRequest searchRequest)
	{
		SearchResponse response = spotSearchService.search(searchRequest);
		return response;
	}

	@POST
	@Path("/communityPageDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CommunityResponse communityPageDetails(@QueryParam("userId") String userId, @QueryParam("pageId") String pageId)
	{
		CommunityResponse communityResponse = null;
		communityResponse = communitySearchService.searchCommunityDetails(Integer.parseInt(userId), Integer.parseInt(pageId));
		return communityResponse;
	}

}
