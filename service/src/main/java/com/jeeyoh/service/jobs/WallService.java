package com.jeeyoh.service.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IFunBoardDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.WallFeedSharing;

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
	IFunBoardDAO funBoardDAO;
	

	@Transactional
	@Override
	public void addWeightContentOnItem() {
		// TODO Auto-generated method stub
		
		java.util.List<WallFeedSharing> list = funBoardDAO.getWallFeedSharing();
		if(list != null)
		{
			for(WallFeedSharing feedSharing :list)
			{
				String itemType = feedSharing.getItemType();
				logger.debug("Item Type ::: "+itemType);
				List<User> userContactsList = userDAO.getUserContacts(feedSharing.getUser().getUserId());
				
				logger.debug("Contacts Size..............==> "+userContactsList.size());
				if(userContactsList != null)
				{
					if(userContactsList != null)
					{
						for(User contact : userContactsList)
						{
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
				/*java.util.List<Usercontacts> userContactsList = userDAO.getAllUserContacts(feedSharing.getUser().getUserId());
				
				//int preWeightContent = feedSharing.getPackageRank();
				if(userContactsList != null)
				{
					for(Usercontacts usercontacts:userContactsList)
					{
						
						User contact = usercontacts.getUserByContactId();
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
				}*/
				
				
				
			}
			
			//populatePackageRank(list);
			populatePackageRank();
			
		}
		
		
	}
	
	private void updateWallFeedSharing(WallFeedSharing feedSharing, int count )
	{
		//int commentCount = userDAO.getFunboardComment(feedSharing.getFunboard().getFunBoardId(), feedSharing.getUser().getUserId());
		int commentCount = 0;
		int weightContent = count + commentCount;
		feedSharing.setItemRank(weightContent);
		funBoardDAO.updateWallFeedSharing(feedSharing);
		
		
	}
	
	private void purgeItemBasedOnWeightCount(WallFeedSharing feedSharing)	
	{
		funBoardDAO.deleteWallFeedSharing(feedSharing);
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
		java.util.List<WallFeedSharing> list = funBoardDAO.getDistinctWallFeedSharing();
		logger.debug("WallFeedSharing :::: "+list.size());
		for(WallFeedSharing feedSharing :list)
		{
			double packageRank= funBoardDAO.getAVGItemRank(feedSharing.getPackageName());
			funBoardDAO.updatePackageRank(packageRank,feedSharing.getPackageName());
			
		}
	}

}
