package com.jeeyoh.persistence;

import java.util.Date;
import java.util.List;

import com.jeeyoh.persistence.domain.WallFeed;
import com.jeeyoh.persistence.domain.WallFeedComments;
import com.jeeyoh.persistence.domain.WallFeedItems;
import com.jeeyoh.persistence.domain.WallFeedUserShareMap;


public interface IWallFeedDAO {
	public void saveWallFeed(WallFeed wallFeed);
	public List<WallFeed> getWallFeedSharing();
	public List<WallFeedItems> getDistinctWallFeedSharing();
	public void updateWallFeedSharing(WallFeedItems feedSharing);
	public void deleteWallFeedSharing(WallFeedItems feedSharing);
	public double getAVGItemRank(int wallFeedId);
	public void updatePackageRank(double packageRank, int wallFeedId);
	public List<String> getMemoryCard(int userId, String searchText, Date startDate, Date endDate, String category);
	public void saveWallFeedComments(WallFeedComments wallFeedComments);
	public List<WallFeedComments> getWallFeedCommentsById(int wallFeedId);
	public WallFeed getWallFeedDetailsByID(int wallFeedId);
	public List<WallFeed> getUserWallFeed(int userId);
	public void saveWallFeedShareMap(WallFeedUserShareMap wallFeedUserShareMap);
	public WallFeedUserShareMap isWallFeedAlreadyShared(int userId, int wallFeedId, int sharedUserId);
	public List<Object[]> getSharedWithUserWallFeed(int userId);
}
