package com.jeeyoh.service.wallfeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IFunBoardDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.IWallFeedSharingDAO;
import com.jeeyoh.persistence.domain.Funboard;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.WallFeedSharing;

@Component("wallFeedSharingService")
public class WallFeedSharingService implements IWallFeedSharingService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private IWallFeedSharingDAO wallFeedSharingDAO;
	@Autowired
	private IFunBoardDAO funBoardDAO;
	@Autowired
	private IUserDAO userDAO;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void saveWallFeedSharingData(int funboardId, String[] userIdArray) {
		
		//Get data from Funboard corresponding to id
		Funboard funboard = funBoardDAO.getFunboardById(funboardId);
		
		// find userlist corresponding to userids
		
		for(int i=0;i<userIdArray.length;i++)
		{
			int shareWithUserId=Integer.parseInt(userIdArray[i]);
			User shareWithUser = (User)userDAO.getUserById(shareWithUserId);
			if(funboard!=null)
			{
				WallFeedSharing wallFeedSharing = new WallFeedSharing();
				wallFeedSharing.setCategory(funboard.getCategory());
				wallFeedSharing.setCreatedTime(funboard.getCreatedTime());
				wallFeedSharing.setItemId(funboard.getItemId());
				wallFeedSharing.setItemType(funboard.getItemType());
				wallFeedSharing.setUser(funboard.getUser());
				wallFeedSharing.setSharedWithUser(shareWithUser);
				wallFeedSharing.setPackageName("ABC");
				wallFeedSharing.setFunboard(funboard);
				wallFeedSharingDAO.saveWallFeedSharing(wallFeedSharing);
			}
			
		}

	}
}
