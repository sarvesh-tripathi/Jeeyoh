package com.jeeyoh.service.jobs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.utils.Utils;

@Component("dealSearch")
public class DealSearch implements IDealSearch {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IDealsDAO dealDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void search() {

		/*String[] type = {"RESTAURANT","SPA","SPORT"};
		List<Businesstype> businesstypes = businessDAO.getBusinesstypeByTypeArray(type);	*/
		List<User> userList = userDAO.getUsers();
		// Iterate user list for suggestion

		for (User user : userList)
		{
			if(user != null)
			{
				//for user Contacts affinity level
				List<Usercontacts> userContactsList = userDAO.getAllUserContacts(user.getUserId());
				
				saveDealSuggestion(user,user,false,false,false,null);
				if(userContactsList != null)
				{
					for(Usercontacts usercontacts:userContactsList)
					{

						Boolean isStar = usercontacts.getIsStar();
						User contact = usercontacts.getUserByContactId();
						logger.debug("Friend Name ::"+contact.getFirstName());
						logger.debug("IS STAR ::"+isStar);
						saveDealSuggestion(contact,user,true,isStar,false,null);

					}
				}

				List<Jeeyohgroup> jeeyohGroup = userDAO.getUserGroups(user.getUserId());
				if(jeeyohGroup != null)
				{
					for(Jeeyohgroup jeeyohGroup1 : jeeyohGroup) {

						String groupType = jeeyohGroup1.getGroupType();
						Set<Groupusermap> groups   = jeeyohGroup1.getGroupusermaps();
						for (Groupusermap groups1 : groups)
						{
							User groupMember = groups1.getUser();
							if(groupMember.getUserId() != user.getUserId())
							{
								saveDealSuggestion(groupMember,user,false,false,true,groupType);
							}
						}
					}
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	private void saveDealSuggestion(User user,User userFor,Boolean contactFlag,Boolean isStar, Boolean isGroupMember, String groupType)
	{

		//get user detail and deal usage	
		Set<Dealsusage> dealUsage = null;
		if(!isGroupMember)
		{
			dealUsage = user.getDealsusages();		
		}
		else
		{
			dealUsage = userDAO.getUserDealUsageByType(user.getUserId(),groupType);
			logger.debug("Group MEMBER "+isGroupMember);
			if(dealUsage != null)
			{
				logger.debug("size of deal usage :: "+dealUsage.size());
			}
		}

		// user commuinty 
		List<Page> userCommunities = null;
		if(!isGroupMember)
		{
			userCommunities = userDAO.getUserCommunities(user.getUserId(),Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
		}
		else
		{
			userCommunities = userDAO.getUserCommunitiesByPageType(user.getUserId(), groupType,Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
		}



		//user category
		List<UserCategory> userCategoryList =null;
		if(!isGroupMember)
		{
			userCategoryList = userDAO.getUserCategoryLikesById(user.getUserId());
		}
		else
		{
			userCategoryList = userDAO.getUserCategoryLikesByType(user.getUserId(),groupType);
		}
		// user group member
		/*List<Jeeyohgroup> jeeyohGroup = null;
		if(!contactFlag)
		{
			jeeyohGroup = userDAO.getUserGroups(user.getUserId());
		}*/

		double[] array = null;	
		if(dealUsage != null){
			for(Dealsusage dealsusage : dealUsage) {
				//logger.debug("DealSearch ==> search ==> dealusage ==> " + dealsusage.getIsFavorite());
				Deals deal = dealsusage.getDeals();
				List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), deal.getId());
				if(userdealsuggestions == null || userdealsuggestions.size() == 0)
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
						double distance = Utils.distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
						logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
						if(distance <= 50)
						{
							int likeCount = dealDAO.getDealsLikeCounts(dealsusage.getDeals().getId());
							logger.debug("Like COUNT :::::"+likeCount);
							if(dealsusage.getIsFavorite() || dealsusage.getIsLike() || likeCount >= 2)
							{
								logger.debug("Cross basic three level3",deal.getId());									
								saveDealsSuggestionInDataBase(deal,userFor,isGroupMember, contactFlag, false);
							}
						}
					}
				}
			}
		}
		//	For Community							
		if(userCommunities != null) {

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
					double distance = Utils.distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
					logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
					if(distance <= 50)
					{
						List<Deals> deals = dealDAO.getDealsByBusinessId(business.getId());
						for(Deals deal : deals)
						{
							List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), deal.getId());
							if(userdealsuggestions == null || userdealsuggestions.size() == 0)
							{
								saveDealsSuggestionInDataBase(deal,userFor,isGroupMember, contactFlag, true);
							}
						}
					}

				}

			}
		}


		// for user category likes item


		///List<UserCategory> userCategoryList = userDAO.getUserCategoryLikesById(user.getUserId());
		if(userCategoryList != null)
		{
			for(UserCategory userCategory : userCategoryList) {
				UserCategoryLikes userCategoryLikes = (UserCategoryLikes)userCategory.getUserCategoryLikes().iterator().next();
				Date  weekendDate = getNearestWeekend();
				Calendar cal = Calendar.getInstance();
				cal.setTime(userCategoryLikes.getCreatedTime());
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				logger.debug("Date Comparison: "+cal.getTime()+" : "+weekendDate);
				logger.debug("Date Comparison: "+cal.getTime().compareTo(weekendDate));
				try {
					if(sdf.parse(sdf.format(cal.getTime())).compareTo(weekendDate) == 0) // Deals is open for nearst weekend only
					{

						if(!contactFlag )
						{
							List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());

							for(Deals catdeal:catDeals)
							{
								Business business = catdeal.getBusiness();
								if(business != null)
								{

									if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
									{
										array = Utils.getLatLong(business.getPostalCode());
										business.setLattitude(Double.toString(array[0]));
										business.setLongitude(Double.toString(array[1]));
									}
									double distance = Utils.distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
									logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
									if(distance <= 50)
									{
										List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
										if(userdealsuggestions == null || userdealsuggestions.size() == 0)
										{
											saveDealsSuggestionInDataBase(catdeal,userFor,isGroupMember, contactFlag, false);
										}
									}
								}

							}
						}
						else
						{
							int LikeCount = dealDAO.userCategoryLikeCount(userCategory.getUserCategoryId());
							if(isStar)//check friend is star or non star
							{
								List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
								for(Deals catdeal:catDeals)
								{
									Business business = catdeal.getBusiness();
									if(business != null)
									{

										if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
										{
											array = Utils.getLatLong(business.getPostalCode());
											business.setLattitude(Double.toString(array[0]));
											business.setLongitude(Double.toString(array[1]));
										}
										double distance = Utils.distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
										logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
										if(distance <= 50)
										{
											List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
											if(userdealsuggestions == null || userdealsuggestions.size() == 0)
											{
												saveDealsSuggestionInDataBase(catdeal,userFor, isGroupMember, contactFlag, false);
											}
										}
									}

								}
							}
							else
							{
								if(LikeCount >= 2) // if non star then chek Like count greater than 2.
								{
									List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
									for(Deals catdeal:catDeals)
									{
										Business business = catdeal.getBusiness();
										if(business != null)
										{

											if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
											{
												array = Utils.getLatLong(business.getPostalCode());
												business.setLattitude(Double.toString(array[0]));
												business.setLongitude(Double.toString(array[1]));
											}
											double distance = Utils.distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
											logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
											if(distance <= 50)
											{
												List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
												if(userdealsuggestions == null || userdealsuggestions.size() == 0)
												{
													saveDealsSuggestionInDataBase(catdeal,userFor, isGroupMember, contactFlag, false);
												}
											}
										}

									}
								}
							}
						}
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}


	private void saveDealsSuggestionInDataBase(Deals deal, User user, boolean isGroupMember, boolean isContact, boolean isCommunityLike)
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
		dealSuggestion.setUser(user);
		if(isCommunityLike)
		{
			if(isGroupMember)
				dealSuggestion.setSuggestionType("Group Member's Community Like");
			else if(isContact)
				dealSuggestion.setSuggestionType("Friend's Community Like");
			else
				dealSuggestion.setSuggestionType("User's Community Like");
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

	private Date getNearestWeekend()
	{
		try {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date weekendDate = sdf.parse(sdf.format(c.getTime()));
			return weekendDate;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	@Override
	public void caculateTopSuggestions() {
		List<User> userList = userDAO.getUsers();
		// Iterate user list for suggestion

		for (User user : userList)
		{
			if(user != null)
			{
				//for user Contacts affinity level
				List<Usercontacts> userContactsList = userDAO.getAllUserContacts(user.getUserId());
				
				saveDealSuggestion(user,user,false,false,false,null);
				if(userContactsList != null)
				{
					for(Usercontacts usercontacts:userContactsList)
					{
						Boolean isStar = usercontacts.getIsStar();
						User contact = usercontacts.getUserByContactId();
						logger.debug("Friend Name ::"+contact.getFirstName());
						logger.debug("IS STAR ::"+isStar);
						saveDealSuggestion(contact,user,true,isStar,false,null);

					}
				}

				List<Jeeyohgroup> jeeyohGroup = userDAO.getUserGroups(user.getUserId());
				if(jeeyohGroup != null)
				{
					for(Jeeyohgroup jeeyohGroup1 : jeeyohGroup) {

						String groupType = jeeyohGroup1.getGroupType();
						Set<Groupusermap> groups   = jeeyohGroup1.getGroupusermaps();
						for (Groupusermap groups1 : groups)
						{
							User groupMember = groups1.getUser();
							if(groupMember.getUserId() != user.getUserId())
							{
								saveDealSuggestion(groupMember,user,false,false,true,groupType);
							}
						}
					}
				}
			}
		}
	}
}
