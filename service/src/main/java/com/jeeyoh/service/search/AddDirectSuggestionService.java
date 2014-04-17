package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public BaseResponse addSuggestions(int userId,ArrayList<Integer> friendsIdList, int suggetstionId,String category, String suggestionType) 
	{
		logger.debug("AddEventSuggestionService => addSuggestions");
		BaseResponse response = new BaseResponse();
		User user = userDAO.getUserById(userId);
		logger.debug("user =>", user.getFirstName());
		if(userId!=0 && friendsIdList!=null && suggetstionId!=0 && category!=null && suggestionType!=null)
		{
			int batch_size = 0;
			if(suggestionType.equalsIgnoreCase("event"))
			{
				logger.debug("event => "+suggetstionId);
				Events event = eventsDAO.getEventById(suggetstionId);
				logger.debug("event desc => "+event.getDescription());
				for(Integer friendId:friendsIdList)
				{
					User friend = userDAO.getUserById(friendId);
					logger.debug("friend => "+friend.getFirstName());
					Usereventsuggestion userEventSuggestion = new Usereventsuggestion();
					userEventSuggestion.setEvents(event);
					userEventSuggestion.setCreatedTime(new Date());
					userEventSuggestion.setSuggestionType("Direct Suggestion");
					userEventSuggestion.setUpdatedTime(new Date());
					userEventSuggestion.setUser(friend);
					userEventSuggestion.setUserContact(user);
					userDAO.saveEventsSuggestions(userEventSuggestion, batch_size);
					batch_size++;
					response.setStatus("Ok");
				}
				
			}
			else if (suggestionType.equalsIgnoreCase("deal"))
			{
				logger.debug("deal => "+suggetstionId);
				Deals deal = dealsDAO.getDealById(suggetstionId);
				logger.debug("event desc => "+deal.getTitle());
				for(Integer friendId:friendsIdList)
				{
					User friend = userDAO.getUserById(friendId);
					logger.debug("friend => "+friend.getFirstName());
					Userdealssuggestion userDealSuggestion = new Userdealssuggestion();
					userDealSuggestion.setDeals(deal);
					userDealSuggestion.setCreatedtime(new Date());
					userDealSuggestion.setSuggestionType("Direct Suggestion");
					userDealSuggestion.setUpdatedtime(new Date());
					userDealSuggestion.setUser(friend);
					userDealSuggestion.setUserContact(user);
					dealsDAO.saveSuggestions(userDealSuggestion);
					batch_size++;
					response.setStatus("Ok");
				}
				
			}
			else if (suggestionType.equalsIgnoreCase("business"))
			{
				logger.debug("business => "+suggetstionId);
				Business business = businessDAO.getBusinessById(suggetstionId);
				logger.debug("event desc => "+business.getName());
				for(Integer friendId:friendsIdList)
				{
					User friend = userDAO.getUserById(friendId);
					logger.debug("friend => "+friend.getFirstName());
					Usernondealsuggestion userNonDealSuggestion = new Usernondealsuggestion();
					userNonDealSuggestion.setBusiness(business);
					userNonDealSuggestion.setCreatedtime(new Date());
					userNonDealSuggestion.setSuggestionType("Direct Suggestion");
					userNonDealSuggestion.setUpdatedtime(new Date());
					userNonDealSuggestion.setUser(friend);
					userNonDealSuggestion.setUserContact(user);
					userDAO.saveNonDealSuggestions(userNonDealSuggestion, batch_size);
					batch_size++;
					response.setStatus("Ok");
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
