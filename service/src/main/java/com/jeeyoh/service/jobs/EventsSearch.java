package com.jeeyoh.service.jobs;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IGroupDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.utils.Utils;

@Component("eventSearch")
public class EventsSearch implements IEventSearch{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IEventsDAO eventsDAO;
	
	@Autowired
	private IGroupDAO groupDAO;

	@Override
	@Transactional
	public void search() {
		List<User> userList = userDAO.getUsers();
		Date weekendDate = Utils.getNearestWeekend(null);
		if(userList != null) {
			for(User user : userList) {
				int userId = user.getUserId();
				boolean isContactsAccessed = false;
				/*if(user.getLattitude() == null && user.getLongitude() == null || (user.getLattitude().trim().equals("") && user.getLongitude().trim().equals(""))|| (user.getLattitude().trim().equals("0.0") && user.getLongitude().trim().equals("0.0")))
				{
					array = Utils.getLatLong(user.getZipcode());
					logger.debug("Lat/long length ==> " + Double.toString(array[0]).length()+" : "+Double.toString(array[1]).length());
					user.setLattitude(Double.toString(array[0]));
					user.setLongitude(Double.toString(array[1]));
				}*/
				saveEventsSuggestion(user, user, true, isContactsAccessed, weekendDate,  false,null,false,true,false,true);
			}
		}

	}


	/**
	 * Save Events Suggestion for user
	 * @param userId
	 * @param user
	 * @param forUser
	 * @param isContactsAccessed
	 * @param weekendDate
	 * @param isGroupMember
	 * @param groupType
	 * @param isStar
	 */
	private void saveEventsSuggestion(User suggestingUser, User user, boolean forUser, boolean isContactsAccessed, Date weekendDate, boolean isGroupMember, String groupType, boolean isStar, boolean isSharedWithFriends, boolean isSharedWithGroup, boolean isSharedWithCommunity)
	{
		try
		{
			int userId = suggestingUser.getUserId();
			//Get current date
			Date currentDate = Utils.getCurrentDate();

			// For EventUserLikes
			List<Events> eventsList = null;
			if(isGroupMember)
			{
				if(isSharedWithGroup)
					eventsList = userDAO.getUserLikesEventsByType(userId, groupType,Double.parseDouble(user.getLattitude()),Double.parseDouble(user.getLongitude()));
			}
			else
			{
				if(isSharedWithFriends)
					eventsList = userDAO.getUserLikesEvents(userId,Double.parseDouble(user.getLattitude()),Double.parseDouble(user.getLongitude()));

			}
			
			logger.debug("EventSearch => UserLikeEvents List => "+eventsList);
			if(eventsList != null)
			{
				logger.debug("EventSearch => UserLikeEvents List => "+eventsList.size());
				int batch_size = 0;
				for(Events event : eventsList) {

					saveEventSuggesstion(event, suggestingUser, user, batch_size, currentDate, isGroupMember, isContactsAccessed, forUser, false,false,false,false,false,false,weekendDate);
				}
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

			logger.debug("userCategoryList: "+userCategoryList);
			if(userCategoryList != null)
			{
				for(UserCategory userCategory : userCategoryList) {
					//UserCategoryLikes userCategoryLikes = (UserCategoryLikes)userCategory.getUserCategoryLikes().iterator().next();

					UserCategoryLikes userCategoryLikes = userDAO.getUserCategoryLikes(userId, userCategory.getUserCategoryId());

					//Get nearest weekend date for UserLike
					Date userLikeWeekend = Utils.getNearestWeekend(userCategoryLikes.getCreatedTime());

					try {
						logger.debug("userLikeWeekend: "+userLikeWeekend +" weekendDate: "+weekendDate);
						//if(userLikeWeekend.compareTo(weekendDate) == 0)
						//{
							if(isContactsAccessed)
								categoryLikesCount = userDAO.userCategoryLikeCount(userCategory.getUserCategoryId());
							logger.debug("categoryLikesCount: "+categoryLikesCount);
							if(forUser ||isGroupMember || isStar || categoryLikesCount >= 2)
							{
								eventsList = eventsDAO.getEventsByuserLikes(userCategory.getItemType().trim(),userCategory.getItemCategory().trim(),userCategory.getProviderName(),Double.parseDouble(user.getLattitude()),Double.parseDouble(user.getLongitude()));
								if(eventsList != null)
								{
									int batch_size = 0;
									for(Events event : eventsList) {

										saveEventSuggesstion(event, suggestingUser, user, batch_size, currentDate, isGroupMember, isContactsAccessed, forUser, true,false,true,true,false,false,weekendDate);
									}
								}
							}
					//	}
					} catch (NumberFormatException e) {
						e.printStackTrace();

					}
				}

				// For User Communities
				List<Page> userCommunities = null;
				if(isSharedWithCommunity)
				{
					if(isGroupMember)
					{
						userCommunities = userDAO.getUserCommunitiesByPageType(userId, groupType);
					}
					else
					{
						userCommunities = userDAO.getUserCommunities(userId);
					}
				}
				logger.debug("NonDealSearch ==> userCommunities ==> size ==> " + userCommunities);

				if(userCommunities != null) 
				{
					logger.debug("NonDealSearch ==> userCommunities ==> size ==> " + userCommunities.size());
					int batch_size = 0;
					for(Page community : userCommunities) {
						boolean communityLiked = false;
						boolean isLiked = false;
						boolean isFavorite = false;
						boolean isVisited = false;
						boolean isFollowing = false;
						Pageuserlikes pageProperty = userDAO.getUserPageProperties(userId, community.getPageId());
						if(pageProperty != null)
						{
							if(pageProperty != null) {
								logger.debug("NonDealSearch ==> search ==> pageProperty ==> not null" );
								isLiked = pageProperty.getIsLike();
								isFavorite = pageProperty.getIsFavorite();
								isVisited = pageProperty.getIsVisited();
								isFollowing = pageProperty.getIsFollowing();
								if(isLiked || isFavorite || isVisited || isFollowing) {											
									communityLiked = true;
								} else
								{
									communityLiked = false;
								}
								logger.debug("NonDealSearch ==> search ==> pageProperty ==> includePage ==> " + communityLiked);
							}
						} else
						{
							communityLiked = false;
						}
						if(communityLiked)
						{
							List<Events> events = userDAO.getCommunityAllEvents(community.getPageId(),Double.parseDouble(user.getLattitude()),Double.parseDouble(user.getLongitude()));

							if(events != null)
							{
								logger.debug("EventSearch => Events List => "+events.size());
								for(Events event : events)
								{
									saveEventSuggesstion(event, suggestingUser, user, batch_size, currentDate, isGroupMember, isContactsAccessed, forUser, false,true,isLiked,isFavorite,isFollowing,isVisited,weekendDate);
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if(forUser)
		{
			List<Jeeyohgroup> groups = groupDAO.getUserGroups(user.getUserId());
			if(groups != null)
			{
				for(Jeeyohgroup jeeyohgroup : groups)
				{
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
							saveEventsSuggestion(groupusermap, user, false , false,weekendDate,true,groupType1,false,groupusermap.getIsShareProfileWithFriend(),groupusermap.getIsShareProfileWithGroup(),groupusermap.getIsShareCommunity());

						}
					}
				}
			}
		}

		if(forUser && !isContactsAccessed)
		{
			List<Object[]> userContactsList = userDAO.getAllUserContacts(user.getUserId());
			isContactsAccessed = true;
			//List<Usercontacts> userContactsList = userDAO.getAllUserContacts(user.getUserId());
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
						logger.debug("Friend Name ::"+contact.getFirstName());
						int contactId = contact.getUserId();
						saveEventsSuggestion(contact, userList, false , isContactsAccessed,weekendDate,false,null,usercontacts.getIsStar(),contact.getIsShareProfileWithFriend(),contact.getIsShareProfileWithGroup(),contact.getIsShareCommunity());
					}
				}
			}
		}
	}

	/**
	 * Save User Events Suggestions
	 * @param events
	 * @param user
	 */
	private void saveEventSuggesstion(Events event, User suggestingUser, User user,int batch_size, Date currentDate, boolean isGroupMember, boolean isContactAccessed, boolean forUser,boolean isUserCategoryLikes, boolean isCommunityLike, boolean isLiked, boolean isFavorite,boolean isFollowing, boolean isVisited,Date weekendDate)
	{
		try
		{
			boolean isExist = false;
			Usereventsuggestion userEventSuggestion = null;
			List<Usereventsuggestion> usereventsuggestions = userDAO.isEventSuggestionExists(user.getUserId(), event.getEventId());
			if(usereventsuggestions != null && usereventsuggestions.size() != 0)
			{
				isExist = true;
			}
			else
			{
				userEventSuggestion = userDAO.isEventSuggestionExistsForDirectSuggestion(user.getUserId(), event.getEventId());
				if(userEventSuggestion != null)
				{
					isExist = true;
				}
			}
			logger.debug("EventSearch ==> usereventsuggestions ==> " + usereventsuggestions + " : "+ isExist);
			
			if(!isExist)
			{
				boolean includePage = true;
				double distance = 0;
				if(isCommunityLike)
				{
					distance = Utils.distance(Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()), Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()), "M");
					logger.debug("Distance::  "+distance +" lat::  "+user.getLattitude()+" lon::  "+user.getLongitude());
				}
				else
				{
					distance = 45;
				}
				if(distance <=50)
				{
					if(!isCommunityLike && !isUserCategoryLikes)
					{
						Eventuserlikes eventProperty = userDAO.getUserEventProperties(suggestingUser.getUserId(), event.getEventId());
						if(eventProperty != null)
						{
							if(eventProperty != null) {
								logger.debug("NonDealSearch ==> search ==> eventProperty ==> not null" );
								isLiked = eventProperty.getIsLike();
								isFavorite = eventProperty.getIsFavorite();
								isVisited = eventProperty.getIsVisited();
								isFollowing = eventProperty.getIsFollowing();
								if(isLiked || isFavorite || isVisited || isFollowing) {											
									includePage = true;
								} else
								{
									includePage = false;
								}
								logger.debug("EventSearch ==> search ==> eventProperty ==> includePage ==> " + includePage);
							}
						} else
						{
							includePage = false;
						}
					}

					if(includePage)
					{
						Usereventsuggestion usereventsuggestion = new Usereventsuggestion();
						usereventsuggestion.setUser(user);
						usereventsuggestion.setUserContact(suggestingUser);
						usereventsuggestion.setEvents(event);
						currentDate = new Date();
						usereventsuggestion.setCreatedTime(currentDate);
						usereventsuggestion.setUpdatedTime(currentDate);
						usereventsuggestion.setIsFavorite(isFavorite);
						usereventsuggestion.setIsLike(isLiked);
						usereventsuggestion.setIsFollowing(isFollowing);
						usereventsuggestion.setSuggestedTime(event.getEvent_date_time_local());
						if(isCommunityLike)
						{
							if(isGroupMember)
								usereventsuggestion.setSuggestionType("Group Member's Community Like");
							else if(isContactAccessed)
								usereventsuggestion.setSuggestionType("Friend's Community Like");
							else if(forUser)
								usereventsuggestion.setSuggestionType("User's Community Like");
						}
						else if(isUserCategoryLikes)
						{
							if(isGroupMember)
								usereventsuggestion.setSuggestionType("Group Member's category Like");
							else if(isContactAccessed)
								usereventsuggestion.setSuggestionType("Friend's category Like");
							else if(forUser)
								usereventsuggestion.setSuggestionType("User's category Like");
						}
						else
						{
							if(isGroupMember)
								usereventsuggestion.setSuggestionType("Group Member's Like");
							else if(isContactAccessed)
								usereventsuggestion.setSuggestionType("Friend's Like");
							else if(forUser)
								usereventsuggestion.setSuggestionType("User's Like");
						}
						userDAO.saveEventsSuggestions(usereventsuggestion, batch_size);
						batch_size++;
					}
				}
			}
			//}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in EventSearch => "+e.getMessage());
		}
	}

}
