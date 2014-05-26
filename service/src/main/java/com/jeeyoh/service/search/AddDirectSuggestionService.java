package com.jeeyoh.service.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IGroupDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.utils.Utils;

@Component("addEventSuggestionService")
public class AddDirectSuggestionService implements IAddDirectSuggestionService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Autowired
	private IDealsDAO dealsDAO;

	@Autowired
	private IBusinessDAO businessDAO;
	
	@Autowired
	private IGroupDAO groupDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public BaseResponse addSuggestions(int userId,ArrayList<Integer> friendsIdList,List<Integer> groups, int suggestionId,String category, String suggestionType, String suggestedTime) 
	{
		logger.debug("AddEventSuggestionService => addSuggestions");
		BaseResponse response = new BaseResponse();
		User user = userDAO.getUserById(userId);
		logger.debug("user =>", user.getFirstName());

		Date suggestionDate = null;
		Date date = new Date();
		if(suggestedTime == null)
		{
			suggestionDate = Utils.getNearestWeekend(date);
		}
		else
		{
			SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			try {
				suggestionDate = simple.parse(suggestedTime);

			}catch(Exception e)
			{

			}
		}

		int batch_size = 0;
		if(suggestionType.equalsIgnoreCase("event"))
		{
			logger.debug("event => "+suggestionId);
			Events event = eventsDAO.getEventById(suggestionId);
			logger.debug("event desc => "+event.getDescription());
			if(friendsIdList != null)
			{
				for(Integer friendId:friendsIdList)
				{
					Usereventsuggestion userEventSuggestion = userDAO.isEventSuggestionExistsForDirectSuggestion(friendId, event.getEventId(), user.getUserId());
					if(userEventSuggestion != null)
					{
						if(userEventSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
						{
							userEventSuggestion.setUpdatedTime(date);
							userEventSuggestion.setSuggestedTime(suggestionDate);
							userEventSuggestion.setSuggestionType("Direct Suggestion");
							userDAO.updateUserEventSuggestion(userEventSuggestion);
						}
						response.setStatus("Ok");
					}
					else
					{
						User friend = userDAO.getUserById(friendId);
						userEventSuggestion = new Usereventsuggestion();
						userEventSuggestion.setEvents(event);
						userEventSuggestion.setCreatedTime(date);
						userEventSuggestion.setSuggestionType("Direct Suggestion");
						userEventSuggestion.setUpdatedTime(date);
						userEventSuggestion.setUser(friend);
						userEventSuggestion.setUserContact(user);
						userEventSuggestion.setSuggestedTime(suggestionDate);
						userDAO.saveEventsSuggestions(userEventSuggestion, batch_size);
						batch_size++;
						response.setStatus("Ok");
					}
				}
			}

			if(groups != null)
			{
				for(Integer groupId:groups)
				{
					Jeeyohgroup jeeyohGroup = groupDAO.getGroupByGroupId(groupId);
					Set<Groupusermap> groups2   = jeeyohGroup.getGroupusermaps();
					for (Groupusermap groups1 : groups2)
					{
						User users1 = groups1.getUser();
						Usereventsuggestion userEventSuggestion = userDAO.isEventSuggestionExistsForDirectSuggestion(users1.getUserId(), event.getEventId(), user.getUserId());
						if(userEventSuggestion != null)
						{
							if(userEventSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
							{
								userEventSuggestion.setUpdatedTime(date);
								userEventSuggestion.setSuggestedTime(suggestionDate);
								userEventSuggestion.setSuggestionType("Direct Suggestion");
								userDAO.updateUserEventSuggestion(userEventSuggestion);
							}
							response.setStatus("Ok");
						}
						else
						{
							userEventSuggestion = new Usereventsuggestion();
							userEventSuggestion.setEvents(event);
							userEventSuggestion.setCreatedTime(date);
							userEventSuggestion.setSuggestionType("Direct Suggestion");
							userEventSuggestion.setUpdatedTime(date);
							userEventSuggestion.setUser(users1);
							userEventSuggestion.setUserContact(user);
							userEventSuggestion.setSuggestedTime(suggestionDate);
							userDAO.saveEventsSuggestions(userEventSuggestion, batch_size);
							batch_size++;
							response.setStatus("Ok");
						}
					}
				}
			}

		}
		else if (suggestionType.equalsIgnoreCase("deal"))
		{
			logger.debug("deal => "+suggestionId);
			Deals deal = dealsDAO.getDealById(suggestionId);
			logger.debug("event desc => "+deal.getTitle());
			if(friendsIdList != null)
			{
				for(Integer friendId:friendsIdList)
				{
					Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(friendId, deal.getId(), user.getUserId());
					if(userDealSuggestion != null)
					{
						if(userDealSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
						{
							userDealSuggestion.setUpdatedtime(date);
							userDealSuggestion.setSuggestedTime(suggestionDate);
							userDealSuggestion.setSuggestionType("Direct Suggestion");
							userDAO.updateUserDealSuggestion(userDealSuggestion);
						}
						response.setStatus("Ok");
					}
					else
					{
						User friend = userDAO.getUserById(friendId);
						userDealSuggestion = new Userdealssuggestion();
						userDealSuggestion.setDeals(deal);
						userDealSuggestion.setCreatedtime(date);
						userDealSuggestion.setSuggestionType("Direct Suggestion");
						userDealSuggestion.setUpdatedtime(date);
						userDealSuggestion.setUser(friend);
						userDealSuggestion.setUserContact(user);
						userDealSuggestion.setSuggestedTime(suggestionDate);
						dealsDAO.saveSuggestions(userDealSuggestion);
						batch_size++;
						response.setStatus("Ok");
					}
				}
			}

			if(groups != null)
			{
				for(Integer groupId:groups)
				{
					Jeeyohgroup jeeyohGroup = groupDAO.getGroupByGroupId(groupId);
					Set<Groupusermap> groups2   = jeeyohGroup.getGroupusermaps();
					for (Groupusermap groups1 : groups2)
					{
						User users1 = groups1.getUser();
						Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(users1.getUserId(), deal.getId(), user.getUserId());
						if(userDealSuggestion != null)
						{
							if(userDealSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
							{
								userDealSuggestion.setUpdatedtime(date);
								userDealSuggestion.setSuggestedTime(suggestionDate);
								userDealSuggestion.setSuggestionType("Direct Suggestion");
								userDAO.updateUserDealSuggestion(userDealSuggestion);
							}
							response.setStatus("Ok");
						}
						else
						{
							userDealSuggestion = new Userdealssuggestion();
							userDealSuggestion.setDeals(deal);
							userDealSuggestion.setCreatedtime(date);
							userDealSuggestion.setSuggestionType("Direct Suggestion");
							userDealSuggestion.setUpdatedtime(date);
							userDealSuggestion.setUser(users1);
							userDealSuggestion.setUserContact(user);
							userDealSuggestion.setSuggestedTime(suggestionDate);
							dealsDAO.saveSuggestions(userDealSuggestion);
							response.setStatus("Ok");
						}
					}
				}
			}

		}
		else if (suggestionType.equalsIgnoreCase("business"))
		{
			logger.debug("business => "+suggestionId);
			Business business = businessDAO.getBusinessById(suggestionId);
			logger.debug("event desc => "+business.getName());
			if(friendsIdList != null)
			{
				for(Integer friendId:friendsIdList)
				{
					Usernondealsuggestion userNonDealSuggestion = userDAO.isNonDealSuggestionExistsForDirectSuggestion(friendId, business.getId(), user.getUserId());
					if(userNonDealSuggestion != null)
					{
						if(userNonDealSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
						{
							userNonDealSuggestion.setUpdatedtime(date);
							userNonDealSuggestion.setSuggestedTime(suggestionDate);
							userNonDealSuggestion.setSuggestionType("Direct Suggestion");
							userDAO.updateUserNonDealSuggestion(userNonDealSuggestion);
						}
						response.setStatus("Ok");
					}
					else
					{
						User friend = userDAO.getUserById(friendId);
						userNonDealSuggestion = new Usernondealsuggestion();
						userNonDealSuggestion.setBusiness(business);
						userNonDealSuggestion.setCreatedtime(date);
						userNonDealSuggestion.setSuggestionType("Direct Suggestion");
						userNonDealSuggestion.setUpdatedtime(date);
						userNonDealSuggestion.setUser(friend);
						userNonDealSuggestion.setUserContact(user);
						userNonDealSuggestion.setSuggestedTime(suggestionDate);
						userDAO.saveNonDealSuggestions(userNonDealSuggestion, batch_size);
						batch_size++;
						response.setStatus("Ok");
					}
				}
			}
			if(groups != null)
			{
				for(Integer groupId:groups)
				{
					Jeeyohgroup jeeyohGroup = groupDAO.getGroupByGroupId(groupId);
					Set<Groupusermap> groups2   = jeeyohGroup.getGroupusermaps();
					for (Groupusermap groups1 : groups2)
					{
						User users1 = groups1.getUser();
						Usernondealsuggestion userNonDealSuggestion = userDAO.isNonDealSuggestionExistsForDirectSuggestion(users1.getUserId(), business.getId(), user.getUserId());
						if(userNonDealSuggestion != null)
						{
							if(userNonDealSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
							{
								userNonDealSuggestion.setUpdatedtime(date);
								userNonDealSuggestion.setSuggestedTime(suggestionDate);
								userNonDealSuggestion.setSuggestionType("Direct Suggestion");
								userDAO.updateUserNonDealSuggestion(userNonDealSuggestion);
							}
							response.setStatus("Ok");
						}
						else
						{
							userNonDealSuggestion = new Usernondealsuggestion();
							userNonDealSuggestion.setBusiness(business);
							userNonDealSuggestion.setCreatedtime(date);
							userNonDealSuggestion.setSuggestionType("Direct Suggestion");
							userNonDealSuggestion.setUpdatedtime(date);
							userNonDealSuggestion.setUser(users1);
							userNonDealSuggestion.setUserContact(user);
							userNonDealSuggestion.setSuggestedTime(suggestionDate);
							userDAO.saveNonDealSuggestions(userNonDealSuggestion, batch_size);
							batch_size++;
							response.setStatus("Ok");
						}
					}
				}
			}
		}
		else
		{
			logger.debug("Incorrect suggestion type");
			response.setError("Incorrect suggestion type");
		}
		logger.debug("done");
		return response;
	}

}
