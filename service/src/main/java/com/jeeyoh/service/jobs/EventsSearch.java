package com.jeeyoh.service.jobs;

import java.text.SimpleDateFormat;
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
				if(user.getLattitude() == null && user.getLongitude() == null || (user.getLattitude().trim().equals("") && user.getLongitude().trim().equals(""))|| (user.getLattitude().trim().equals("0.0") && user.getLongitude().trim().equals("0.0")))
				{
					array = Utils.getLatLong(user.getZipcode());
					logger.debug("Lat/long length ==> " + Double.toString(array[0]).length()+" : "+Double.toString(array[1]).length());
					user.setLattitude(Double.toString(array[0]));
					user.setLongitude(Double.toString(array[1]));
				}
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

			List<Page> userCommunities = null;
			if(isGroupMember)
			{
				userCommunities = userDAO.getUserCommunitiesByPageType(userId, groupType);
			}
			else
			{
				userCommunities = userDAO.getUserCommunities(userId);
			}
			logger.debug("NonDealSearch ==> userCommunities ==> size ==> " + userCommunities.size());

			if(userCommunities != null) 
			{
				int batch_size = 0;
				for(Page community : userCommunities) {

					List<Events> events = userDAO.getUserCommunityEvents(userId, community.getPageId());
					
					if(events != null)
					{
						logger.debug("EventSearch => Events List => "+events.size());
						for(Events event : events)
						{
							saveEventSuggesstion(event, user, batch_size, currentDate);
						}
					}
				}
			}

			List<Events> eventsList = null;
			if(isGroupMember)
			{
				eventsList = userDAO.getUserLikesEventsByType(userId, groupType);
			}
			else
			{
				eventsList = userDAO.getUserLikesEvents(userId);
			}
			if(eventsList != null)
			{
				int batch_size = 0;
				for(Events event : eventsList) {

					saveEventSuggesstion(event, user, batch_size, currentDate);
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
					
					//Boolean isStar = usercontacts.getIsStar();
					User contact = usercontacts.getUserByContactId();
					logger.debug("Friend Name ::"+contact.getFirstName());
					logger.debug("IS STAR ::"+isStar);
					int contactId = contact.getUserId();
					saveEventsSuggestion(contactId, userList.get(0), false , isContactsAccessed,weekendDate,false,null,usercontacts.getIsStar());
				}
			}
			/*List<User> userContacts = userDAO.getUserContacts(userId);
			List<User> userList = userDAO.getUserById(userId);
			logger.debug("Contacts Size..............==> "+userContacts.size());
			if(userContacts != null) {
				for(User contact : userContacts) {
					if(contact != null) {
						Usercontacts usercontacts2 = (Usercontacts)contact.getUsercontactsesForContactId().iterator().next();
						int contactId = contact.getUserId();
						saveEventsSuggestion(contactId, userList.get(0), false , isContactsAccessed,weekendDate,false,null,usercontacts2.getIsStar());
					}
				}
			}*/
		}
	}

	/**
	 * Save User Events Suggestions
	 * @param events
	 * @param user
	 */
	private void saveEventSuggesstion(Events event, User user,int batch_size, Date currentDate)
	{
		try
		{
			List<Usereventsuggestion> usereventsuggestions = userDAO.isEventSuggestionExists(user.getUserId(), event.getEventId());
			logger.debug("NonDealSearch ==> usernondealsuggestions ==> " + usereventsuggestions);
			if(usereventsuggestions == null || usereventsuggestions.size() == 0)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				boolean includePage = true;
				if(sdf.parse(sdf.format(event.getEvent_date())).compareTo(currentDate) >= 0)
				{
					double distance = Utils.distance(Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()), Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()), "M");
					logger.debug("Distance::  "+distance +" lat::  "+user.getLattitude()+" lon::  "+user.getLongitude());
					if(distance <=50)
					{
						List<Eventuserlikes> eventproperties = userDAO.getUserEventProperties(user.getUserId(), event.getEventId());
						boolean isLiked = false,isFavorite = false,isVisited,isFollowing = false;
						if(eventproperties != null)
						{
							logger.debug("NonDealSearch ==> search ==> eventProperty ==> not null" );
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
								logger.debug("NonDealSearch ==> search ==> eventProperty ==> includePage ==> " + includePage);
							}
						} else
						{
							includePage = false;
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
							userDAO.saveEventsSuggestions(usereventsuggestion, batch_size);
							batch_size++;
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in EventSearch => "+e.getMessage());
		}
	}
}
