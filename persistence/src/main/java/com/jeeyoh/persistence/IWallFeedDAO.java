package com.jeeyoh.persistence;

import java.util.Date;
import java.util.List;

import com.jeeyoh.persistence.domain.WallFeed;
import com.jeeyoh.persistence.domain.WallFeedItems;
import com.jeeyoh.persistence.domain.WallFeedUserShareMap;


public interface IWallFeedDAO {
	public void saveWallFeedSharing(WallFeed wallFeed);
	public List<WallFeed> getWallFeedSharing();
	public List<WallFeedItems> getDistinctWallFeedSharing();
	public void updateWallFeedSharing(WallFeedItems feedSharing);
	public void deleteWallFeedSharing(WallFeedItems feedSharing);
	public double getAVGItemRank(int packageId);
	public void updatePackageRank(double packageRank, String packageName);
	public List<WallFeedUserShareMap> getMemoryCard(int userId, String searchText, Date startDate, Date endDate);
}
