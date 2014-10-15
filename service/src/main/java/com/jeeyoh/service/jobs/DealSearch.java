package com.jeeyoh.service.jobs;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IGroupDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.utils.Utils;

@Component("dealSearch")
public class DealSearch implements IDealSearch {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IDealsDAO dealDAO;
	
	@Autowired
	private IGroupDAO groupDAO;

	@Override
	@Transactional
	public void search() {

		List<User> userList = userDAO.getUsers();
		// Iterate user list for suggestion
		Date  weekendDate = Utils.getNearestWeekend(null);
		Date currentDate = Utils.getCurrentDate();
		for (User user : userList)
		{
			if(user != null)
			{
				logger.debug("user ::"+user.getUserId());
				//for user Contacts affinity level
				List<Object[]> userContactsList = userDAO.getAllUserContacts(user.getUserId());

				saveDealSuggestion(user,user,false,false,false,null,true,false,true,weekendDate,currentDate);
				if(userContactsList != null)
				{
					for(Object[] row:userContactsList)
					{
						Usercontacts usercontacts = (Usercontacts)row[1];
						Boolean isStar = usercontacts.getIsStar();
						User contact = (User)row[0];
						logger.debug("Friend Name ::"+contact.getFirstName());
						saveDealSuggestion(contact,user,true,isStar,false,null,contact.getIsShareProfileWithFriend(),contact.getIsShareProfileWithGroup(),contact.getIsShareCommunity(),weekendDate,currentDate);
					}
				}

				List<Jeeyohgroup> jeeyohGroup = groupDAO.getUserGroups(user.getUserId());
				if(jeeyohGroup != null)
				{
					for(Jeeyohgroup jeeyohGroup1 : jeeyohGroup) {

						List<User> groupusermapList = userDAO.getGroupMembers(jeeyohGroup1.getGroupId(), user.getUserId());
						if(groupusermapList != null)
						{
							String groupType = jeeyohGroup1.getGroupType();
							for(User groupMember : groupusermapList)
							{
								saveDealSuggestion(groupMember,user,false,false,true,groupType,groupMember.getIsShareProfileWithFriend(),groupMember.getIsShareProfileWithGroup(),groupMember.getIsShareCommunity(),weekendDate,currentDate);
							}
						}
					}
				}
			}
		}
	}


	private void saveDealSuggestion(User user,User userFor,Boolean contactFlag,Boolean isStar, Boolean isGroupMember, String groupType, boolean isSharedWithFriends, boolean isSharedWithGroup, boolean isSharedWithCommunity, Date weekendDate, Date currentDate)
	{

		boolean isExist = false;
		//get user detail and deal usage	
		List<Dealsusage> dealUsage = null;
		if(!isGroupMember)
		{
			if(isSharedWithFriends)
				dealUsage = userDAO.getUseDealUsage(user.getUserId(), Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()));		
			logger.debug("size of dealusage11 :: "+dealUsage);
		}
		else
		{
			if(isSharedWithGroup)
				dealUsage = userDAO.getUserDealUsageByType(user.getUserId(),groupType, Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()));
			
				logger.debug("size of deal usage :: "+dealUsage);
			
		}

		// user community 
		List<Page> userCommunities = null;
		if(isSharedWithCommunity)
		{
			if(!isGroupMember)
			{
				userCommunities = userDAO.getUserCommunities(user.getUserId(),Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()));
			}
			else
			{
				userCommunities = userDAO.getUserCommunitiesByPageType(user.getUserId(), groupType,Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()));
			}
		}


		//user category
		List<UserCategory> userCategoryList =null;
		if(!isGroupMember)
		{
			if(isSharedWithFriends)
				userCategoryList = userDAO.getUserCategoryLikesById(user.getUserId());
		}
		else
		{
			if(isSharedWithGroup)
				userCategoryList = userDAO.getUserCategoryLikesByType(user.getUserId(),groupType);
		}
		
		double[] array = null;	
		if(dealUsage != null){
			for(Dealsusage dealsusage : dealUsage) {
				Deals deal = dealsusage.getDeals();
				isExist = false;
				List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), deal.getId());
				if(userdealsuggestions != null && userdealsuggestions.size() != 0)
				{
					isExist = true;
				}
				else
				{
					Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(userFor.getUserId(), deal.getId());
					if(userDealSuggestion != null)
					{
						isExist = true;
					}
				}
				
				if(!isExist)
				{
					Business business = deal.getBusiness();
					if(business != null)
					{

						if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
						{
							array = Utils.getLatLong(business.getPostalCode());
							business.setLattitude(Double.toString(array[0]));
							business.setLongitude(Double.toString(array[1]));
						}
						//double distance = Utils.distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
						//logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
						//if(distance <= 50)
						//{
						int likeCount = dealDAO.getDealsLikeCounts(dealsusage.getDeals().getId());
						logger.debug("Like COUNT :::::"+likeCount);
						if(dealsusage.getIsFavorite() || dealsusage.getIsLike() || likeCount >= 2)
						{
							try {								
								saveDealsSuggestionInDataBase(deal,user,userFor,isGroupMember, contactFlag, false,false, weekendDate);

							} catch (Exception e) {
								logger.debug("Error:::  "+e.getLocalizedMessage());
							}
						}
					}
				}
			}
		}
		//	For Community							
		if(userCommunities != null) {

			logger.debug("userCommunities size::  "+userCommunities.size());
			for(Page community : userCommunities) {

				Business business = community.getBusiness();
				if(business != null)
				{

					if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
					{
						array = Utils.getLatLong(business.getPostalCode());
						business.setLattitude(Double.toString(array[0]));
						business.setLongitude(Double.toString(array[1]));
					}
					
					List<Deals> deals = dealDAO.getDealsByBusinessId(business.getId());
					for(Deals deal : deals)
					{
						isExist = false;
						List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), deal.getId());
						if(userdealsuggestions != null && userdealsuggestions.size() != 0)
						{
							isExist = true;
						}
						else
						{
							Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(userFor.getUserId(), deal.getId());
							if(userDealSuggestion != null)
							{
								isExist = true;
							}
						}
						
						if(!isExist)
						{
							saveDealsSuggestionInDataBase(deal,user,userFor,isGroupMember, contactFlag, true,false, weekendDate);
						}
					}
				}
			}
		}


		// for user category likes item
		if(userCategoryList != null)
		{
			logger.debug("userCategoryList size::  "+userCategoryList.size());
			for(UserCategory userCategory : userCategoryList) {
				
				UserCategoryLikes userCategoryLikes = userDAO.getUserCategoryLikes(user.getUserId(), userCategory.getUserCategoryId());
				
				//Get nearest weekend date for UserLike
				Date userLikeWeekend = Utils.getNearestWeekend(userCategoryLikes.getCreatedTime());


				logger.debug("Date Comparison: "+userLikeWeekend.compareTo(weekendDate));
				try {
					//if(userLikeWeekend.compareTo(weekendDate) == 0) // Deals is open for nearst weekend only
					//{

						if(!contactFlag )
						{
							List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName(),Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()));

							for(Deals catdeal:catDeals)
							{
								isExist = false;
								Business business = catdeal.getBusiness();
								if(business != null)
								{

									if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
									{
										array = Utils.getLatLong(business.getPostalCode());
										business.setLattitude(Double.toString(array[0]));
										business.setLongitude(Double.toString(array[1]));
									}
							
									List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
									if(userdealsuggestions != null && userdealsuggestions.size() != 0)
									{
										isExist = true;
									}
									else
									{
										Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(userFor.getUserId(), catdeal.getId());
										if(userDealSuggestion != null)
										{
											isExist = true;
										}
									}
									
									if(!isExist)
									{
										saveDealsSuggestionInDataBase(catdeal,user,userFor,isGroupMember, contactFlag, false,true, weekendDate);
									}
								}

							}
						}
						else
						{
							int LikeCount = dealDAO.userCategoryLikeCount(userCategory.getUserCategoryId());
							if(isStar)//check friend is star or non star
							{
								List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName(),Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()));
								for(Deals catdeal:catDeals)
								{
									isExist = false;
									Business business = catdeal.getBusiness();
									if(business != null)
									{

										if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
										{
											array = Utils.getLatLong(business.getPostalCode());
											business.setLattitude(Double.toString(array[0]));
											business.setLongitude(Double.toString(array[1]));
										}
										List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
										if(userdealsuggestions != null && userdealsuggestions.size() != 0)
										{
											isExist = true;
										}
										else
										{
											Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(userFor.getUserId(), catdeal.getId());
											if(userDealSuggestion != null)
											{
												isExist = true;
											}
										}
										
										if(!isExist)
										{
											saveDealsSuggestionInDataBase(catdeal,user,userFor, isGroupMember, contactFlag, false,true, weekendDate);
										}
									}

								}
							}
							else
							{
								if(LikeCount >= 2) // if non star then chek Like count greater than 2.
								{
									List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName(),Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()));
									for(Deals catdeal:catDeals)
									{
										isExist = false;
										Business business = catdeal.getBusiness();
										if(business != null)
										{

											if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
											{
												array = Utils.getLatLong(business.getPostalCode());
												business.setLattitude(Double.toString(array[0]));
												business.setLongitude(Double.toString(array[1]));
											}
											
											List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
											if(userdealsuggestions != null && userdealsuggestions.size() != 0)
											{
												isExist = true;
											}
											else
											{
												Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(userFor.getUserId(), catdeal.getId());
												if(userDealSuggestion != null)
												{
													isExist = true;
												}
											}
											
											if(!isExist)
											{
												saveDealsSuggestionInDataBase(catdeal,user,userFor, isGroupMember, contactFlag, false,true, weekendDate);
											}
										}
									}
								}
							}
						}
					//}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}


	private void saveDealsSuggestionInDataBase(Deals deal, User suggestingUser, User user, boolean isGroupMember, boolean isContact, boolean isCommunityLike, boolean isUserCategoryLikes, Date weekendDate)
	{
		Date date = new Date();
		Userdealssuggestion dealSuggestion = new Userdealssuggestion();
		dealSuggestion.setCreatedtime(date);
		dealSuggestion.setDeals(deal);
		dealSuggestion.setIsFavorite(true);
		dealSuggestion.setIsFollowing(true);
		dealSuggestion.setIsLike(true);
		dealSuggestion.setIsRedempted(true);
		dealSuggestion.setUpdatedtime(date);
		//dealSuggestion.setSuggestedTime(weekendDate);
		dealSuggestion.setUser(user);
		dealSuggestion.setUserContact(suggestingUser);
		if(isCommunityLike)
		{
			if(isGroupMember)
				dealSuggestion.setSuggestionType("Group Member's Community Like");
			else if(isContact)
				dealSuggestion.setSuggestionType("Friend's Community Like");
			else
				dealSuggestion.setSuggestionType("User's Community Like");
		}
		else if(isUserCategoryLikes)
		{
			if(isGroupMember)
				dealSuggestion.setSuggestionType("Group Member's category Like");
			else if(isContact)
				dealSuggestion.setSuggestionType("Friend's category Like");
			else
				dealSuggestion.setSuggestionType("User's category Like");
		}
		else
		{
			if(isGroupMember)
				dealSuggestion.setSuggestionType("Group Member's Like");
			else if(isContact)
				dealSuggestion.setSuggestionType("Friend's Like");
			else 
				dealSuggestion.setSuggestionType("User's Like");
		}
		dealDAO.saveSuggestions(dealSuggestion);
	}

}
