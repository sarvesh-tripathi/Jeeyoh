package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.response.MatchingEventsResponse;
import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.utils.Utils;

@Component("matchingEventsService")
public class MatchingEventsService implements IMatchingEventsService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private IEventsDAO eventsDAO;
	
	@Autowired
	private IUserDAO userDAO;
	
	@Override
	@Transactional
	public MatchingEventsResponse searchMatchingEvents(int userId) {
		MatchingEventsResponse matchingEventsResponse = new MatchingEventsResponse();
		List<User> userList = userDAO.getUserById(userId);
		double[] array = null;
		if(userList!=null)
		{
			User user = userList.get(0);
			if(user.getLattitude()==null && user.getLongitude()==null || user.getLattitude().trim().equals("") && user.getLongitude().trim().equals("")){
				array = Utils.getLatLong(user.getZipcode());
				user.setLattitude(Double.toString(array[0]));
				user.setLongitude(Double.toString(array[1]));
			}
		List<Events> userFriendsCommonLikeEvents = new ArrayList<Events>();
		List<Events> getUserFavLikeEventsList = userDAO.getUserLikesEvents(userId,Double.parseDouble(user.getLattitude()),Double.parseDouble(user.getLongitude()));
		List<User> getStarFiftyMilesUserList = userDAO.getStarFriends(userId, Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
		logger.debug("getStarFiftyMilesUserList=>"+getStarFiftyMilesUserList);
		for(User starFiftyMilesUser: getStarFiftyMilesUserList)
		{
			logger.debug("starFiftyMilesUserList.getUserId()"+starFiftyMilesUser.getUserId());
			List<Events> getStarFriendFavLikeEventsList = userDAO.getUserLikesEvents(starFiftyMilesUser.getUserId(),Double.parseDouble(starFiftyMilesUser.getLattitude()),Double.parseDouble(starFiftyMilesUser.getLongitude()));
			logger.debug("getStarFriendFavLikeEventsList=>"+getStarFriendFavLikeEventsList);
			for(Events userLikeEvent:getUserFavLikeEventsList)
			{
				for(Events friendLikeEvent: getStarFriendFavLikeEventsList)
				{
					if(userLikeEvent.getEventId()==friendLikeEvent.getEventId())
					{
						userFriendsCommonLikeEvents.add(userLikeEvent);
					}
				}
			}
		}
		
		List<Events> getBookedEventsList = eventsDAO.getBookedEvents(userId);
		List<Events> finalMatchingEventList = new ArrayList<Events>(userFriendsCommonLikeEvents);
		finalMatchingEventList.addAll(getBookedEventsList);
		List<EventModel> getMatchingEventsModelList = new ArrayList<EventModel>();
		if(finalMatchingEventList!=null)
		{
			for(Events event:finalMatchingEventList)
			{
				EventModel getMatchingEventsModel = new EventModel();
				getMatchingEventsModel.setAncestorGenreDescriptions(event.getAncestorGenreDescriptions());
				getMatchingEventsModel.setChannel(event.getChannel());
				getMatchingEventsModel.setCity(event.getCity());
				getMatchingEventsModel.setCurrency_code(event.getCurrency_code());
				getMatchingEventsModel.setDescription(event.getDescription());
				getMatchingEventsModel.setEvent_date(event.getEvent_date());
				getMatchingEventsModel.setGenreUrlPath(event.getGenreUrlPath());
				getMatchingEventsModel.setGeography_parent(event.getGeography_parent());
				getMatchingEventsModel.setLatitude(event.getLatitude());
				getMatchingEventsModel.setLongitude(event.getLongitude());
				getMatchingEventsModel.setMaxPrice(event.getMaxPrice());
				getMatchingEventsModel.setMinPrice(event.getMinPrice());
				getMatchingEventsModel.setMaxSeatsTogether(event.getMaxSeatsTogether());
				getMatchingEventsModel.setMinSeatsTogether(event.getMinSeatsTogether());
				getMatchingEventsModel.setState(event.getState());
				getMatchingEventsModel.setTitle(event.getTitle());
				getMatchingEventsModel.setTotalTickets(event.getTotalTickets());
				getMatchingEventsModel.setUrlpath(event.getUrlpath());
				getMatchingEventsModel.setVenue_config_name(event.getVenue_config_name());
				getMatchingEventsModel.setVenue_name(event.getVenue_name());
				getMatchingEventsModel.setZip(event.getZip());
				getMatchingEventsModelList.add(getMatchingEventsModel);
			}
		}
		
		matchingEventsResponse.setMatchingevents(getMatchingEventsModelList);
		}
		return matchingEventsResponse;
	}
	
}
