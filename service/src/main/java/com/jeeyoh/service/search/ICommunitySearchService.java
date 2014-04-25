package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.CommunityPaginationResponse;
import com.jeeyoh.model.response.CommunityResponse;
import com.jeeyoh.model.search.CommunityReviewModel;
import com.jeeyoh.model.search.PageModel;

public interface ICommunitySearchService {
	
	public List<PageModel> search(String userEmail);
	public CommunityResponse searchCommunityDetails(int userId, int pageId,int offset,int limit);
	public CommunityPaginationResponse searchCommunityPaginationDetails(int userId, int pageId,int offset,int limit, String listType);
	public CommentResponse saveCommunityComments(CommentModel commentModel);
	public BaseResponse saveIsFollowingPage(int userId, int pageId, boolean isFollow);
	public BaseResponse saveIsFollowingEvent(int userId, int eventId, boolean isFollow);
	public BaseResponse saveFavoritePage(int userId, int eventId, boolean isFav);
	public BaseResponse saveCommunityReview(CommunityReviewModel communityReviewModel);
}
