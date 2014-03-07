package com.jeeyoh.service.jobs;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.WallFeed;

@Component("wallService")
public class WallService implements IWallService {
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	IDealsDAO dealsDAO;
	
	@Autowired
	IUserDAO userDAO;
	
	@Autowired
	IEventsDAO eventsDAO;
	

	@Transactional
	@Override
	public void addWeightContentOnItem() {
		// TODO Auto-generated method stub
		//itemId , itemType, category , userId
		
		//select * from eventuserlikes where eventId =29 and userId in (select contactId from usercontacts where userId =1)
		
		String itemType = null;
		//int count = userDAO.getEventWightCount(1,29);
		int count = userDAO.getPageWightCount(2,36);
		logger.debug("Weight Content "+count);
		/*if(itemType.equalsIgnoreCase("deal"))
		{
			int count = userDAO.getDealWightCount(userId,itemId);
			saveWall(funBoard,count);
			break;
						
		}
		else if(itemType.equalsIgnoreCase("events"))
		{
			int count = userDAO.getEventWightCount(userId,itemId);
			saveWall(funBoard,count);
			break;
		}
		else if(itemType.equalsIgnoreCase("business"))
		{
			int count = userDAO.getBusinessWightCount(userId,itemId);
			saveWall(funBoard,count);
			break;
		}*/
		
	}
	
	/*private void saveWall(FunBoard funBoard, int count )
	{
		WallFeed wallFeed = new WallFeed();
		wallFeed.setCategory(funBoard.category);
		wallFeed.setCreatedTime(new Date());
		wallFeed.setItemId(funBoard.itemId)
		wallFeed.setItemType(funBoard.itemType);
		wallFeed.setUser(funBoard.user);
		wallFeed.setWeightCount(count);
		wallFeed.setUpdatedTime(new Date());
		userDAO.saveWallFeed(wallFeed);
	}*/

}
