package com.jeeyoh.service.jobs;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.IWallFeedDAO;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.WallFeed;
import com.jeeyoh.persistence.domain.WallFeedItems;

@Component("wallService")
public class WallService implements IWallService {
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	IDealsDAO dealsDAO;
	
	@Autowired
	IUserDAO userDAO;
	
	@Autowired
	IEventsDAO eventsDAO;
	
	@Autowired
	IWallFeedDAO wallFeedDAO;

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public void addWeightContentOnItem() {
		// TODO Auto-generated method stub
		
		List<WallFeed> list = wallFeedDAO.getWallFeedSharing();
		if(list != null)
		{
			for(WallFeed wallFeed :list)
			{
				java.util.List<User> userContactsList = userDAO.getUserContacts(wallFeed.getUser().getUserId());
				
				Set<WallFeedItems> feedItems = wallFeed.getWallFeedItems();
				for(WallFeedItems feedSharing:feedItems)
				{
				String itemType = feedSharing.getItemType();
				logger.debug("Item Type ::: "+itemType);
				//int preWeightContent = feedSharing.getPackageRank();
				if(userContactsList != null)
				{
					for(User contact:userContactsList)
					{
						
						//User contact = usercontacts.getUserByContactId();
						logger.debug("user for "+contact.getFirstName());
						if(itemType.equalsIgnoreCase("deal"))
						{
							
							Dealsusage dealsusage = userDAO.getUserLikeDeal(contact.getUserId(), feedSharing.getItemId());
							if(dealsusage != null)
							{
								logger.debug("dealUsage1"+dealsusage);
								logger.debug("dealUsage2 :::"+dealsusage.getIsLike());
								logger.debug("dealUsage3 :::"+dealsusage.getIsFavorite());
								logger.debug("dealUsage4 :::"+dealsusage.getIsSuggested());
								logger.debug("dealUsage 5:::"+dealsusage.getIsVisited());
								int count = calculateWieght(dealsusage.getIsLike(), dealsusage.getIsFavorite(), dealsusage.getIsSuggested(), dealsusage.getIsVisited());
								updateWallFeedSharing(feedSharing,count);
							}
							//break;
										
						}
						else if(itemType.equalsIgnoreCase("event"))
						{
							
							Eventuserlikes eventuserlikes = userDAO.getUserLikeEvent(contact.getUserId(), feedSharing.getItemId());
							if(eventuserlikes != null)
							{
								int count = calculateWieght(eventuserlikes.getIsLike(), eventuserlikes.getIsFavorite(), eventuserlikes.getIsSuggested(), eventuserlikes.getIsVisited());
								updateWallFeedSharing(feedSharing,count);
							}
							//break;
						}
						else if(itemType.equalsIgnoreCase("business"))
						{
							Page page = eventsDAO.getPageByBusinessId(feedSharing.getItemId());	
							if(page != null)
							{
								Pageuserlikes pageuserlikes = userDAO.getUserLikeCommunity(contact.getUserId(), feedSharing.getItemId());
								int count = calculateWieght(pageuserlikes.getIsLike(), pageuserlikes.getIsFavorite(), false, pageuserlikes.getIsVisited());
								updateWallFeedSharing(feedSharing,count);
							}
							
							//break;
						}
						else if(itemType.equalsIgnoreCase("community"))
						{
							
							Pageuserlikes pageuserlikes = userDAO.getUserLikeCommunity(contact.getUserId(), feedSharing.getItemId());
							if(pageuserlikes != null)
							{
								int count = calculateWieght(pageuserlikes.getIsLike(), pageuserlikes.getIsFavorite(), false, pageuserlikes.getIsVisited());
								updateWallFeedSharing(feedSharing,count);
							}
							//break;
						}
					}
						

					}
				}
				
				
				
			}
			
			//populatePackageRank(list);
			populatePackageRank();
			
		}
		
		
	}
	
	private void updateWallFeedSharing(WallFeedItems feedSharing, int count )
	{
		//int commentCount = userDAO.getFunboardComment(feedSharing.getFunboard().getFunBoardId(), feedSharing.getUser().getUserId());
		int commentCount = 0;
		int weightContent = count + commentCount;
		feedSharing.setItemRank(weightContent);
		wallFeedDAO.updateWallFeedSharing(feedSharing);
		
		
	}
	
	private void purgeItemBasedOnWeightCount(WallFeedItems feedSharing)	
	{
		wallFeedDAO.deleteWallFeedSharing(feedSharing);
	}
	private int calculateWieght(boolean isLike, boolean isFavorite, boolean isSuggested, boolean isVisited)
	{
		int newWeight = 1;
		
		if(isLike)
		{
			newWeight = newWeight*1;
		}
		if(isFavorite)
		{
			newWeight = newWeight*2;
		}
		if(isSuggested)
		{
			newWeight = newWeight*3;
		}
		if(isVisited)
		{
			newWeight = newWeight*4;
		}
		
		return newWeight;
		
	}
//	private void populatePackageRank(List<WallFeedSharing> list)
//	{
//		for(WallFeedSharing feedSharing :list)
//		{
//			double packageRank= eventsDAO.getAVGItemRank(feedSharing.getPackageName());
//			feedSharing.setPackageRank(packageRank);
//			feedSharing.setUpdatedTime(new Date());
//			userDAO.updateWallFeedSharing(feedSharing);
//		}
//	}
	
	public void populatePackageRank()
	{
		logger.debug("populate package rank :::: ");
		java.util.List<WallFeedItems> list = wallFeedDAO.getDistinctWallFeedSharing();
		logger.debug("WallFeedSharing :::: "+list.size());
		for(WallFeedItems feedSharing :list)
		{
			double packageRank= wallFeedDAO.getAVGItemRank(feedSharing.getWallFeed().getPackageId());
			wallFeedDAO.updatePackageRank(packageRank,feedSharing.getPackageName());
			
		}
	}

}
