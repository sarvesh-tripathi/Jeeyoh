package com.jeeyoh.service.jobs;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IGroupDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.utils.Utils;

@Component("nonDealSearch")
public class NonDealSearch implements INonDealSearch {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	public final String MOVIE_CATEGORY = "MOVIE";
	public final String RESTAURANT_CATEGORY = "RESTAURANT";

	public final String NIGHTLIFE_CATEGORY = "NIGHTLIFE";
	public final String EVENTS_CATEGORY = "EVENT";
	public final String GETAWAYS_CATEGORY = "GETAWAYS";
	public final String SPORTS_CATEGORY = "SPORT";

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IBusinessDAO businessDAO;
	
	@Autowired
	private IGroupDAO groupDAO;

	@Override
	@Transactional
	public void search() {
		List<User> userList = userDAO.getUsers();
		logger.debug("NonDealSearch ==> search ==> ");
		Date weekendDate = Utils.getNearestWeekend(null);
		if(userList != null) {
			for(User user : userList) {
				logger.debug("NonDealSearch ==> search ==> userID ==> " + user.getEmailId());
				int userId = user.getUserId();
				boolean isContactsAccessed = false;
				double[] array = null;
				logger.debug("Lat/Long for user :  " + user.getLattitude() +" , "+user.getLongitude());
				if(user.getLattitude() == null && user.getLongitude() == null || (user.getLattitude().trim().equals("") && user.getLongitude().trim().equals(""))|| (user.getLattitude().trim().equals("0.0") && user.getLongitude().trim().equals("0.0")))
				{
					array = Utils.getLatLong(user.getZipcode());
					logger.debug("Lat/long length ==> " + Double.toString(array[0]).length()+" : "+Double.toString(array[1]).length());
					user.setLattitude(Double.toString(array[0]));
					user.setLongitude(Double.toString(array[1]));
				}
				saveNonDealSuggestion(user, user, true, isContactsAccessed,weekendDate, false,null,false,true,false,true);
			}
		}
	}


	/**
	 * Save Non deal suggestions
	 * @param userId
	 * @param user
	 * @param forUser
	 * @param isContactsAccessed
	 * @param weekendDate
	 * @param isGroupMember
	 * @param groupType
	 * @param isStar
	 */
	private void saveNonDealSuggestion(User suggestingUser, User user, boolean forUser, boolean isContactsAccessed, Date weekendDate, boolean isGroupMember, String groupType, boolean isStar, boolean isSharedWithFriends, boolean isSharedWithGroup, boolean isSharedWithCommunity)
	{
		int userId = suggestingUser.getUserId();
		int countMain = 0;
		double[] array = null;
		if(user.getLattitude() == null && user.getLongitude() == null || (user.getLattitude().trim().equals("") && user.getLongitude().trim().equals("")) || (user.getLattitude().trim().equals("0.0") && user.getLongitude().trim().equals("0.0")))
		{
			array = Utils.getLatLong(user.getZipcode());
			user.setLattitude(Double.toString(array[0]));
			user.setLongitude(Double.toString(array[1]));
		}

		//For User Category Likes
		List<UserCategory> userCategoryList = null;
		int categoryLikesCount = 0;
		if(isGroupMember)
		{
			if(isSharedWithGroup)
				userCategoryList = userDAO.getUserCategoryLikesByType(userId, groupType);
		}
		else
		{
			if(isSharedWithFriends)
				userCategoryList = userDAO.getUserCategoryLikesById(userId);
		}

		logger.debug("userCategoryList::  "+userCategoryList);
		if(userCategoryList != null)
		{
			logger.debug("userCategoryList size::  "+userCategoryList.size());
			for(UserCategory userCategory : userCategoryList) {

				if(userCategory.getItemCategory().equalsIgnoreCase(MOVIE_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(RESTAURANT_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(NIGHTLIFE_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(EVENTS_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(GETAWAYS_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(SPORTS_CATEGORY)) {

					//UserCategoryLikes userCategoryLikes = (UserCategoryLikes)userCategory.getUserCategoryLikes().iterator().next();
					UserCategoryLikes userCategoryLikes = userDAO.getUserCategoryLikes(userId, userCategory.getUserCategoryId());

					//Get nearest weekend date for UserLike
					Date userLikeWeekend = Utils.getNearestWeekend(userCategoryLikes.getCreatedTime());

					try {
						logger.debug("userLikeWeekend: "+userLikeWeekend +" weekendDate: "+weekendDate);
						if(userLikeWeekend.compareTo(weekendDate) == 0)
						{
							if(isContactsAccessed)
								categoryLikesCount = userDAO.userCategoryLikeCount(userCategory.getUserCategoryId());
							logger.debug("categoryLikesCount: "+categoryLikesCount);
							if(forUser ||isGroupMember || isStar || categoryLikesCount >= 2)
							{
								List<Business> businessList = businessDAO.getBusinessByuserLikes(userCategory.getItemType().trim(),userCategory.getItemCategory().trim(),userCategory.getProviderName(),Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
								if(businessList != null)
								{
									boolean includePage = true;
									for(Business business : businessList)
									{
										List<Usernondealsuggestion> usernondealsuggestions = userDAO.isNonDealSuggestionExists(user.getUserId(), business.getId());
										logger.debug("NonDealSearch ==> usernondealsuggestions ==> " + usernondealsuggestions);
										if(usernondealsuggestions == null || usernondealsuggestions.size() == 0)
										{
											String type = business.getBusinesstype().getBusinessType();
											if(type.equalsIgnoreCase(MOVIE_CATEGORY) || type.equalsIgnoreCase(RESTAURANT_CATEGORY) || type.equalsIgnoreCase(NIGHTLIFE_CATEGORY) || type.equalsIgnoreCase(EVENTS_CATEGORY) || type.equalsIgnoreCase(GETAWAYS_CATEGORY) || type.equalsIgnoreCase(SPORTS_CATEGORY)) {

												if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
												{
													array = Utils.getLatLong(business.getPostalCode());
													business.setLattitude(Double.toString(array[0]));
													business.setLongitude(Double.toString(array[1]));
												}

												//double distance = Utils.distance(Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
												//logger.debug("Distance::  "+distance +" lat::  "+user.getLattitude()+" lon::  "+user.getLongitude());
												//if(distance <=50)
												//{
												String ambiance = "";
												if(business.getAmbience() != null)
													ambiance = business.getAmbience();
												String musicType = "";
												if(business.getMusicType() != null)
													musicType = business.getMusicType();

												Double rating = 0.0;
												if(business.getRating() != null)
												{
													rating = business.getRating();
												}
												if(rating > 3.0) {
													if(business.getSource() != null)
													{
														if(business.getSource().equalsIgnoreCase("yelp"))
														{
															if(type.equalsIgnoreCase(RESTAURANT_CATEGORY)) {
																if(!ambiance.equalsIgnoreCase("GOOD") && !musicType.equalsIgnoreCase("SOFT")) {
																	includePage = false;
																} else {
																	includePage = true;
																}
															}
														}
													}else {
														includePage = true;
													}
												}
												/*else
												{
													includePage = false;
												}*/

												if(includePage) {
													Usernondealsuggestion suggestion = new Usernondealsuggestion();									
													suggestion.setBusiness(business);
													Date currentDate = new Date();
													suggestion.setCreatedtime(currentDate);
													suggestion.setIsChecked(false);
													suggestion.setIsRelevant(true);
													suggestion.setUpdatedtime(currentDate);
													suggestion.setUser(user);
													suggestion.setUserContact(suggestingUser);
													suggestion.setSuggestedTime(weekendDate);
													if(isGroupMember)
														suggestion.setSuggestionType("Group Member's Like");
													else if(isContactsAccessed)
														suggestion.setSuggestionType("Friend's Like");
													else if(forUser)
														suggestion.setSuggestionType("User's Like");
													userDAO.saveNonDealSuggestions(suggestion,countMain);
													countMain ++;
												}
												//}
											}
										}
									}
								}
							}
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();

					}
				}
			}

			if(forUser)
			{
				List<Jeeyohgroup> groups = groupDAO.getUserGroups(user.getUserId());
				if(groups != null)
				{
					for(Jeeyohgroup jeeyohgroup : groups)
					{
						//Set<Groupusermap> groupusermapList = jeeyohgroup.getGroupusermaps();
						List<User> groupusermapList = userDAO.getGroupMembers(jeeyohgroup.getGroupId(), user.getUserId());
						if(groupusermapList != null)
						{
							logger.debug("groupusermapList::  "+groupusermapList.size());
							String groupType1 = jeeyohgroup.getGroupType();
							logger.debug("groupType1::  "+groupType1);
							for(User groupusermap : groupusermapList)
							{
								int userid = groupusermap.getUserId();

								logger.debug("USERID for userGroup::  "+userid);
								//if(user.getUserId() != userid)
								saveNonDealSuggestion(groupusermap, user, false , false,weekendDate,true,groupType1,false,true,groupusermap.getIsShareProfileWithGroup(),groupusermap.getIsShareCommunity());

							}
						}
					}
				}
			}

			// For User Communities
			List<Page> userCommunities = null;
			if(isSharedWithCommunity)
			{
				if(isGroupMember)
				{
					userCommunities = userDAO.getUserCommunitiesByPageType(userId, groupType,Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
				}
				else
				{
					userCommunities = userDAO.getUserCommunities(userId,Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
				}
			}

			logger.debug("NonDealSearch ==> userCommunities ==> size ==> " + userCommunities);
			if(userCommunities != null) {
				logger.debug("NonDealSearch ==> userCommunities ==> size ==> " + userCommunities.size());
				boolean includePage = true;
				for(Page community : userCommunities) {

					if(community.getBusiness() != null)
					{
						List<Usernondealsuggestion> usernondealsuggestions = userDAO.isNonDealSuggestionExists(user.getUserId(), community.getBusiness().getId());
						logger.debug("NonDealSearch ==> usernondealsuggestions ==> " + usernondealsuggestions);
						if(usernondealsuggestions == null || usernondealsuggestions.size() == 0)
						{
							logger.debug("NonDealSearch ==> search ==> pageId ==> " + community.getPageId());
							List<Pagetype> pageTypeList = userDAO.getCommunityType(community.getPageId());

							Pagetype pageType = null;
							if(pageTypeList != null) {
								pageType = pageTypeList.get(0);
							}

							if(pageType != null) {
								String type = pageType.getPageType();

								if(type.equalsIgnoreCase(MOVIE_CATEGORY) || type.equalsIgnoreCase(RESTAURANT_CATEGORY) || type.equalsIgnoreCase(NIGHTLIFE_CATEGORY) || type.equalsIgnoreCase(EVENTS_CATEGORY) || type.equalsIgnoreCase(GETAWAYS_CATEGORY) || type.equalsIgnoreCase(SPORTS_CATEGORY)) {

									Business business = businessDAO.getBusinessById(community.getBusiness().getId());
									if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
									{
										array = Utils.getLatLong(business.getPostalCode());
										business.setLattitude(Double.toString(array[0]));
										business.setLongitude(Double.toString(array[1]));
									}

									//double distance = Utils.distance(Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
									//logger.debug("Distance::  "+distance +" lat::  "+user.getLattitude()+" lon::  "+user.getLongitude());
									//if(distance <=50)
									//{
									String ambiance = "";
									if(business.getAmbience() != null)
										ambiance = business.getAmbience();
									String musicType = "";
									if(business.getMusicType() != null)
										musicType = business.getMusicType();

									Double rating = 0.0;
									if(business.getRating() != null)
									{
										rating = business.getRating();
									}
									Pageuserlikes pageProperty = userDAO.getUserPageProperties(userId, community.getPageId());
									if(pageProperty != null)
									{
										if(pageProperty != null) {
											boolean isLiked = pageProperty.getIsLike();
											boolean isFavorite = pageProperty.getIsFavorite();
											boolean isVisited = pageProperty.getIsVisited();
											boolean isFollowing = pageProperty.getIsFollowing();
											if(isLiked || isFavorite || isVisited || isFollowing) {											
												includePage = true;
											} else
											{
												includePage = false;
											}
											logger.debug("NonDealSearch ==> search ==> pageProperty ==> includePage ==> " + includePage);
										}
									} else
									{
										includePage = false;
									}
									if(!includePage)
									{
										List<Events> eventsList = userDAO.getUserCommunityEvents(userId, community.getPageId());
										if(eventsList != null)
										{
											for(Events event : eventsList) {

												Eventuserlikes eventProperty = userDAO.getUserEventProperties(userId, event.getEventId());
												if(eventProperty != null)
												{
													if(eventProperty != null) {
														logger.debug("NonDealSearch ==> search ==> eventProperty ==> not null" );
														boolean isLiked = eventProperty.getIsLike();
														boolean isFavorite = eventProperty.getIsFavorite();
														boolean isVisited = eventProperty.getIsVisited();
														boolean isFollowing = eventProperty.getIsFollowing();
														if(isLiked || isFavorite || isVisited || isFollowing) {											
															includePage = true;
															break;
														} else
														{
															includePage = false;
														}
														logger.debug("NonDealSearch ==> search ==> eventProperty ==> includePage ==> " + includePage);
													}
												} else
												{
													includePage = false;
												}
											}
										}
									}
									logger.debug("NonDealSearch ==> search ==> pageProperties ==> includePage ==> " + includePage);
									logger.debug("NonDealSearch ==> search ==> rating ==> " + rating);
									if(rating > 3.0) {
										if(business.getSource() != null)
										{
											if(business.getSource().equalsIgnoreCase("yelp"))
											{
												if(type.equalsIgnoreCase(RESTAURANT_CATEGORY)) {
													if(!ambiance.equalsIgnoreCase("GOOD") && !musicType.equalsIgnoreCase("SOFT")) {
														includePage = false;
													} else {
														includePage = true;
													}
												}
											}
										}else {
											includePage = true;
										}
									}
									/*else
									{
										includePage = false;
									}*/
									logger.debug("NonDealSearch ==> search ==> rating ==> includePage ==> " + includePage);
									if(includePage) {
										Usernondealsuggestion suggestion = new Usernondealsuggestion();									
										suggestion.setBusiness(business);
										Date currentDate = new Date();
										suggestion.setCreatedtime(currentDate);
										suggestion.setIsChecked(false);
										suggestion.setIsRelevant(true);
										suggestion.setUpdatedtime(currentDate);
										suggestion.setUser(user);
										suggestion.setUserContact(suggestingUser);
										suggestion.setSuggestedTime(weekendDate);
										if(isGroupMember)
											suggestion.setSuggestionType("Group Member's Community Like");
										else if(isContactsAccessed)
											suggestion.setSuggestionType("Friend's Community Like");
										else if(forUser)
											suggestion.setSuggestionType("User's Community Like");
										userDAO.saveNonDealSuggestions(suggestion,countMain);
										countMain ++;
									}
									//	}
								}
							}
						}
					}
				}
			}

			if(forUser && !isContactsAccessed)
			{
				List<Object[]> userContactsList = userDAO.getAllUserContacts(user.getUserId());
				isContactsAccessed = true;
				User userList = userDAO.getUserById(user.getUserId());
				logger.debug("Contacts Size..............==> "+userContactsList.size());
				if(userContactsList != null)
				{
					if(userContactsList != null)
					{
						for(Object[] row:userContactsList)
						{
							Usercontacts usercontacts = (Usercontacts)row[1];
							User contact = (User)row[0];
							logger.debug("Friend Name ::"+contact.getUserId());
							int contactId = contact.getUserId();
							saveNonDealSuggestion(contact, userList, false , isContactsAccessed,weekendDate,false,null,usercontacts.getIsStar(),contact.getIsShareProfileWithFriend(),contact.getIsShareProfileWithGroup(),contact.getIsShareCommunity());
						}
					}
				}
			}
		}
	}
}
