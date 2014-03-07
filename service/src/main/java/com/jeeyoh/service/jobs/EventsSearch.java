package com.jeeyoh.service.jobs;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.utils.Utils;

@Component("eventSearch")
public class EventsSearch implements IEventSearch{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Override
	@Transactional
	public void search() {
		List<User> userList = userDAO.getUsers();
		if(userList != null) {
			for(User user : userList) {
				int userId = user.getUserId();
				boolean isContactsAccessed = false;
				double[] array = null;
				logger.debug("Lat/Long for user :  " + user.getLattitude() +" , "+user.getLongitude());
				/*if(user.getLattitude() == null && user.getLongitude() == null || (user.getLattitude().trim().equals("") && user.getLongitude().trim().equals(""))|| (user.getLattitude().trim().equals("0.0") && user.getLongitude().trim().equals("0.0")))
				{
					array = Utils.getLatLong(user.getZipcode());
					logger.debug("Lat/long length ==> " + Double.toString(array[0]).length()+" : "+Double.toString(array[1]).length());
					user.setLattitude(Double.toString(array[0]));
					user.setLongitude(Double.toString(array[1]));
				}*/
				saveEventsSuggestion(userId, user, true, isContactsAccessed, null,  false,null,false);
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
	@SuppressWarnings("unchecked")
	private void saveEventsSuggestion(int userId, User user, boolean forUser, boolean isContactsAccessed, Date weekendDate, boolean isGroupMember, String groupType, boolean isStar)
	{
		try
		{
			//Get current date
			Date currentDate = Utils.getCurrentDate();

			
			List<Events> eventsList = null;
			if(isGroupMember)
			{
				eventsList = userDAO.getUserLikesEventsByType(userId, groupType,Double.parseDouble(user.getLattitude()),Double.parseDouble(user.getLongitude()));
			}
			else
			{
				eventsList = userDAO.getUserLikesEvents(userId,Double.parseDouble(user.getLattitude()),Double.parseDouble(user.getLongitude()));
			}
			logger.debug("EventSearch => UserLikeEvents List => "+eventsList.size());
			if(eventsList != null)
			{
				int batch_size = 0;
				for(Events event : eventsList) {

					saveEventSuggesstion(event, userId, user, batch_size, currentDate, isGroupMember, isContactsAccessed, forUser, false,false,false,false,false);
				}
			}

			
			List<Page> userCommunities = null;
			if(isGroupMember)
			{
				userCommunities = userDAO.getUserCommunitiesByPageType(userId, groupType,Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
			}
			else
			{
				userCommunities = userDAO.getUserCommunities(userId,Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
			}
			logger.debug("NonDealSearch ==> userCommunities ==> size ==> " + userCommunities.size());

			if(userCommunities != null) 
			{
				int batch_size = 0;
				for(Page community : userCommunities) {
					boolean communityLiked = false;
					boolean isLiked = false;
					boolean isFavorite = false;
					boolean isVisited = false;
					boolean isFollowing = false;
					List<Pageuserlikes> pageProperties = userDAO.getUserPageProperties(userId, community.getPageId());
					if(pageProperties != null)
					{
						logger.debug("NonDealSearch ==> search ==> pageProperties ==> not null" );
						Pageuserlikes pageProperty = pageProperties.get(0);
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
								saveEventSuggesstion(event, userId, user, batch_size, currentDate, isGroupMember, isContactsAccessed, forUser, true,isLiked,isFavorite,isFollowing,isVisited);
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
			List<Jeeyohgroup> groups = userDAO.getUserGroups(userId);
			if(groups != null)
			{
				for(Jeeyohgroup jeeyohgroup : groups)
				{
					Set<Groupusermap> groupusermapList = jeeyohgroup.getGroupusermaps();
					String groupType1 = jeeyohgroup.getGroupType();
					logger.debug("groupType1::  "+groupType1);
					for(Groupusermap groupusermap : groupusermapList)
					{
						int userid = groupusermap.getUser().getUserId();

						logger.debug("USERID for userGroup::  "+userid);
						if(user.getUserId() != userid)
							saveEventsSuggestion(userid, user, false , false,weekendDate,true,groupType1,false);
					}
				}
			}
		}

		if(forUser && !isContactsAccessed)
		{
			logger.debug("Else..................");
			isContactsAccessed = true;
			List<Usercontacts> userContactsList = userDAO.getAllUserContacts(user.getUserId());
			List<User> userList = userDAO.getUserById(userId);
			logger.debug("ContactsList..............==> "+userContactsList);
			if(userContactsList != null)
			{
				logger.debug("Contacts Size..............==> "+userContactsList.size());
				for(Usercontacts usercontacts:userContactsList)
				{
					User contact = usercontacts.getUserByContactId();
					logger.debug("Friend Name ::"+contact.getFirstName());
					logger.debug("IS STAR ::"+usercontacts.getIsStar());
					int contactId = contact.getUserId();
					saveEventsSuggestion(contactId, userList.get(0), false , isContactsAccessed,weekendDate,false,null,usercontacts.getIsStar());
				}
			}
		}
	}

	/**
	 * Save User Events Suggestions
	 * @param events
	 * @param user
	 */
	private void saveEventSuggesstion(Events event, int userId, User user,int batch_size, Date currentDate, boolean isGroupMember, boolean isContactAccessed, boolean forUser, boolean isCommunityLike, boolean isLiked, boolean isFavorite,boolean isFollowing, boolean isVisited)
	{
		try
		{
			List<Usereventsuggestion> usereventsuggestions = userDAO.isEventSuggestionExists(user.getUserId(), event.getEventId());
			logger.debug("EventSearch ==> usereventsuggestions ==> " + usereventsuggestions);
			if(usereventsuggestions == null || usereventsuggestions.size() == 0)
			{
				//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				boolean includePage = true;
				//if(event.getEvent_date().compareTo(currentDate) >= 0)
				//{
					//double distance = Utils.distance(Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()), Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()), "M");
					//logger.debug("Distance::  "+distance +" lat::  "+user.getLattitude()+" lon::  "+user.getLongitude());
					//if(distance <=50)
					//{
						if(!isCommunityLike)
						{
							List<Eventuserlikes> eventproperties = userDAO.getUserEventProperties(userId, event.getEventId());
							if(eventproperties != null && !eventproperties.isEmpty())
							{
								logger.debug("EventSearch ==> search ==> eventProperty ==> not null" );
								Eventuserlikes eventProperty = eventproperties.get(0);
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
							usereventsuggestion.setEvents(event);
							usereventsuggestion.setCreatedTime(new Date());
							usereventsuggestion.setUpdatedTime(new Date());
							usereventsuggestion.setIsFavorite(isFavorite);
							usereventsuggestion.setIsLike(isLiked);
							usereventsuggestion.setIsFollowing(isFollowing);
							if(isCommunityLike)
							{
								if(isGroupMember)
									usereventsuggestion.setSuggestionType("Group Member's Community Like");
								else if(isContactAccessed)
									usereventsuggestion.setSuggestionType("Friend's Community Like");
								else if(forUser)
									usereventsuggestion.setSuggestionType("User's Community Like");
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
				//}
			//}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in EventSearch => "+e.getMessage());
		}
	}
}
