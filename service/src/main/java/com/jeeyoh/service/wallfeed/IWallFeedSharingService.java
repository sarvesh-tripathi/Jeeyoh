package com.jeeyoh.service.wallfeed;

import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.funboard.SaveShareWallRequest;
import com.jeeyoh.model.funboard.WallFeedRequest;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.WallFeedResponse;


public interface IWallFeedSharingService {
	public BaseResponse saveWallFeedSharingData(WallFeedRequest wallFeedModel);
	public CommentResponse saveWallFeedComments(CommentModel commentModel);
	public WallFeedResponse getWallFeedComments(int userId, int wallFeedId);
	public WallFeedResponse getUserWallFeed(int userId);
	public BaseResponse saveShareWallFeedRecord(SaveShareWallRequest request);
	public BaseResponse addWallFeedSuggestions(SaveShareWallRequest request);
}
