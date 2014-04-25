package com.jeeyoh.service.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.persistence.domain.WallFeed;
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

	@Override
	@Transactional
	public BaseResponse addSuggestions(int userId,ArrayList<Integer> friendsIdList, int suggetstionId,String category, String suggestionType, String suggestedTime) 
	{
		logger.debug("AddEventSuggestionService => addSuggestions");
		BaseResponse response = new BaseResponse();
		User user = userDAO.getUserById(userId);
		logger.debug("user =>", user.getFirstName());
		if(userId!=0 && friendsIdList!=null && suggetstionId!=0 && category!=null && suggestionType!=null)
		{
			Date suggestionDate = null;
			Date date = new Date();
			if(suggestedTime == null)
			{
				suggestionDate = Utils.getNearestWeekend(new Date());
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
				logger.debug("event => "+suggetstionId);
				Events event = eventsDAO.getEventById(suggetstionId);
				logger.debug("event desc => "+event.getDescription());
				for(Integer friendId:friendsIdList)
				{
					Usereventsuggestion userEventSuggestion = userDAO.isEventSuggestionExistsForDirectSuggestion(friendId, event.getEventId(), user.getUserId());
					if(userEventSuggestion != null)
					{
						if(userEventSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
						{
							userEventSuggestion.setUpdatedTime(date);
							userEventSuggestion.setSuggestedTime(suggestionDate);
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
			else if (suggestionType.equalsIgnoreCase("deal"))
			{
				logger.debug("deal => "+suggetstionId);
				Deals deal = dealsDAO.getDealById(suggetstionId);
				logger.debug("event desc => "+deal.getTitle());
				for(Integer friendId:friendsIdList)
				{
					Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(friendId, deal.getId(), user.getUserId());
					if(userDealSuggestion != null)
					{
						if(userDealSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
						{
							userDealSuggestion.setUpdatedtime(date);
							userDealSuggestion.setSuggestedTime(suggestionDate);
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
			else if (suggestionType.equalsIgnoreCase("business"))
			{
				logger.debug("business => "+suggetstionId);
				Business business = businessDAO.getBusinessById(suggetstionId);
				logger.debug("event desc => "+business.getName());
				for(Integer friendId:friendsIdList)
				{
					Usernondealsuggestion userNonDealSuggestion = userDAO.isNonDealSuggestionExistsForDirectSuggestion(friendId, business.getId(), user.getUserId());
					if(userNonDealSuggestion != null)
					{
						if(userNonDealSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
						{
							userNonDealSuggestion.setUpdatedtime(date);
							userNonDealSuggestion.setSuggestedTime(suggestionDate);
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
			else
			{
				logger.debug("Incorrect suggestion type");
				response.setError("Incorrect suggestion type");
			}
		}
		else
		{
			logger.debug("Required values are missing");
			response.setError("Required values are missing");
		}
		logger.debug("done");
		return response;
	}

}
