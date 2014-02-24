package com.jeeyoh.service.jobs;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Topdealssuggestion;
import com.jeeyoh.persistence.domain.Topeventsuggestion;
import com.jeeyoh.persistence.domain.Topnondealsuggestion;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.utils.Utils;

@Component("calculateTopSuggestions")
public class CalculateTopSuggestionsService implements ICalculateTopSuggestionsService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Override
	@Transactional
	public void caculateTopFriendsSuggestions() {
		List<User> userList = userDAO.getUsers();
		logger.debug("caculateTopSuggestions ==> search ==> ");
		//Date weekendDate = Utils.getNearestWeekend(null);
		if(userList != null) {
			for(User user : userList) {
				logger.debug("caculateTopSuggestions ==> search ==> userID ==> " + user.getEmailId());
				int userId = user.getUserId();
				List<Usercontacts> userContactsList = userDAO.getAllUserContacts(userId);
				logger.debug("Contacts Size..............==> "+userContactsList.size());
				if(userContactsList != null)
				{
					for(Usercontacts usercontacts:userContactsList)
					{
						User contact = usercontacts.getUserByContactId();
						logger.debug("Friend Name ::"+contact.getFirstName());
						int contactId = contact.getUserId();
						saveTopSuggestions(userId, contactId, true, false, false);

					}
				}
			}
		}
	}



	@Transactional
	@Override
	public void calculateTopCommunitySuggestions() {
		List<User> userList = userDAO.getUsers();
		logger.debug("caculateTopSuggestions ==> search ==> ");
		//Date weekendDate = Utils.getNearestWeekend(null);
		if(userList != null) {
			for(User user : userList) {
				logger.debug("caculateTopSuggestions ==> search ==> userID ==> " + user.getEmailId());
				int userId = user.getUserId();
				saveTopSuggestions(userId, 0, false, true, false);
			}
		}

	}


	@Transactional
	@Override
	public void calculateTopJeyoohSuggestions() {
		List<User> userList = userDAO.getUsers();
		logger.debug("caculateTopSuggestions ==> search ==> ");
		//Date weekendDate = Utils.getNearestWeekend(null);
		if(userList != null) {
			for(User user : userList) {
				logger.debug("caculateTopSuggestions ==> search ==> userID ==> " + user.getEmailId());
				int userId = user.getUserId();
				List<Usernondealsuggestion> usernondealsuggestions = userDAO.getUserNonDealsSuggestionByUserIdForJeeyoh(user.getUserId());
				for(Usernondealsuggestion usernondealsuggestion : usernondealsuggestions)
				{

				}

				List<Userdealssuggestion> userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForJeeyoh(userId);
				for(Userdealssuggestion userdealssuggestion : userdealsuggestions)
				{

				}

				List<Usereventsuggestion> usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForJeeyoh(userId);
				for(Usereventsuggestion usereventsuggestion : usereventsuggestions)
				{

				}

			}
		}

	}

	/**
	 * This saves the top suggestions into DataBase
	 * @param userId
	 * @param contactId
	 */
	private void saveTopSuggestions(int userId, int contactId, boolean forFriendsSuggestion, boolean forComunitySuggestion, boolean forJeeyohSuggestion)
	{
		String pageIdsStr = "";
		
		// Get Non Deal Suggestions
		List<Usernondealsuggestion> usernondealsuggestions = null;
		if(forFriendsSuggestion)
			usernondealsuggestions = userDAO.getUserNonDealsSuggestionByUserIdForFriends(userId,contactId);
		else if(forComunitySuggestion)
			usernondealsuggestions = userDAO.getUserNonDealsSuggestionByUserIdForCommunity(userId);
		else if(forJeeyohSuggestion)
			usernondealsuggestions = userDAO.getUserNonDealsSuggestionByUserIdForJeeyoh(userId);

		logger.debug("usernondealsuggestions:  "+usernondealsuggestions);
		if(usernondealsuggestions != null && !usernondealsuggestions.isEmpty())
		{
			for(int i = 0; i < usernondealsuggestions.size(); i++)
			{
				Page page = (Page)usernondealsuggestions.get(i).getBusiness().getPages().iterator().next();
				pageIdsStr +="'"+page.getPageId()+"'";


				if(i < usernondealsuggestions.size() - 1)
				{
					pageIdsStr+=",";
				}
			}
			
			// Getting Likes count for non deal suggestion in descending order
			List<Object[]> rows = userDAO.userNonDealSuggestionCount(pageIdsStr);
			
			int count = 0;
			if(rows != null)
			{
				logger.debug("rows: "+rows.size());
				for(Object[] object : rows)
				{
					count++;
					for(Usernondealsuggestion usernondealsuggestion : usernondealsuggestions)
					{
						Page page = (Page)usernondealsuggestion.getBusiness().getPages().iterator().next();
						if(page.getPageId() == object[1])
						{
							Topnondealsuggestion topnondealsuggestion = new Topnondealsuggestion();
							topnondealsuggestion.setBusiness(usernondealsuggestion.getBusiness());
							topnondealsuggestion.setUser(usernondealsuggestion.getUser());
							if(forFriendsSuggestion)
								topnondealsuggestion.setSuggestionType("Friend's Suggestion");
							else if(forComunitySuggestion)
								topnondealsuggestion.setSuggestionType("Community Suggestion");
							topnondealsuggestion.setRank((long)count);
							topnondealsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
							userDAO.saveTopNonDealSuggestions(topnondealsuggestion);
						}
					}
					logger.debug("userNonDealSuggestions: "+object[0] +" : "+ object[1]);
				}
			}
			
		}

		// Get Deals Suggestions
		List<Userdealssuggestion> userdealsuggestions = null;
		if(forFriendsSuggestion)
			userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForFriends(userId, contactId);
		else if(forComunitySuggestion)
			userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForCommunity(userId);
		else if(forJeeyohSuggestion)
			userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForJeeyoh(userId);

		String dealIdsStr = "";
		if(userdealsuggestions != null && !userdealsuggestions.isEmpty())
		{
			for(int i = 0; i < userdealsuggestions.size(); i++)
			{
				if(forFriendsSuggestion)
				{
					dealIdsStr +="'"+userdealsuggestions.get(i).getDeals().getId()+"'";
					if(i < userdealsuggestions.size() - 1)
					{
						dealIdsStr+=",";
					}
				}
				else if(forComunitySuggestion)
				{
					pageIdsStr = "";
					Page page = (Page)userdealsuggestions.get(i).getDeals().getBusiness().getPages().iterator().next();
					pageIdsStr +="'"+page.getPageId()+"'";
					if(i < userdealsuggestions.size() - 1)
					{
						pageIdsStr+=",";
					}
				}
			}
			List<Object[]> rows = null;
			
			// Getting Likes count for deal suggestion in descending order
			if(forFriendsSuggestion)
				rows = userDAO.userDealSuggestionCount(dealIdsStr);
			else if(forComunitySuggestion)
				rows = userDAO.userNonDealSuggestionCount(pageIdsStr);

			logger.debug("EventStr: "+dealIdsStr +" : "+rows);
			if(rows != null)
			{
				int count = 0;
				logger.debug("rows: "+rows.size());
				for(Object[] object : rows)
				{
					count++;
					for(Userdealssuggestion userdealsuggestion : userdealsuggestions)
					{
						if(forFriendsSuggestion)
						{
							if(userdealsuggestion.getDeals().getId() == object[1])
							{
								Topdealssuggestion topdealsuggestion = new Topdealssuggestion();
								topdealsuggestion.setDeals(userdealsuggestion.getDeals());
								topdealsuggestion.setUser(userdealsuggestion.getUser());
								topdealsuggestion.setSuggestionType("Friend's Suggestion");
								topdealsuggestion.setRank((long)count);
								topdealsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
								userDAO.saveTopDealSuggestions(topdealsuggestion);
							}
						}
						else if(forComunitySuggestion)
						{
							Page page = (Page)userdealsuggestion.getDeals().getBusiness().getPages().iterator().next();
							if(page.getPageId() == object[1])
							{
								Topdealssuggestion topdealsuggestion = new Topdealssuggestion();
								topdealsuggestion.setDeals(userdealsuggestion.getDeals());
								topdealsuggestion.setUser(userdealsuggestion.getUser());
								topdealsuggestion.setSuggestionType("Community Suggestion");
								topdealsuggestion.setRank((long)count);
								topdealsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
								userDAO.saveTopDealSuggestions(topdealsuggestion);
							}
						}
					}
					logger.debug("userNonDealSuggestions: "+object[0] +" : "+ object[1]);
				}
			}
		}

		// Get Event Suggestions
		List<Usereventsuggestion> usereventsuggestions = null;
		if(forFriendsSuggestion)
			usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForFriends(userId, contactId);
		else if(forComunitySuggestion)
			usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForCommunity(userId);
		else if(forJeeyohSuggestion)
			usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForJeeyoh(userId);

		String eventIdsStr = "";
		if(usereventsuggestions != null && !usereventsuggestions.isEmpty())
		{
			for(int i = 0; i < usereventsuggestions.size(); i++)
			{
				if(forFriendsSuggestion)
				{
					eventIdsStr +="'"+usereventsuggestions.get(i).getEvents().getEventId()+"'";
					if(i < usereventsuggestions.size() - 1)
					{
						eventIdsStr+=",";
					}
				}
				else if(forComunitySuggestion)
				{
					pageIdsStr = "";
					pageIdsStr +="'"+usereventsuggestions.get(i).getEvents().getPage().getPageId()+"'";
					if(i < usereventsuggestions.size() - 1)
					{
						pageIdsStr+=",";
					}
				}

			}
			List<Object[]> rows = null;
			
			// Getting Likes count for even suggestion in descending order
			if(forFriendsSuggestion)
				rows = userDAO.userEventSuggestionCount(eventIdsStr);
			else if(forComunitySuggestion)
				rows = userDAO.userNonDealSuggestionCount(pageIdsStr);

			logger.debug("EventStr: "+eventIdsStr +" : "+rows);
			if(rows != null)
			{
				logger.debug("rows: "+rows.size());
				int count = 0;
				for(Object[] object : rows)
				{
					count++;
					for(Usereventsuggestion usereventsuggestion : usereventsuggestions)
					{
						if(forFriendsSuggestion)
						{
							if(usereventsuggestion.getEvents().getEventId() == object[1])
							{
								Topeventsuggestion topeventsuggestion = new Topeventsuggestion();
								topeventsuggestion.setEvents(usereventsuggestion.getEvents());
								topeventsuggestion.setUser(usereventsuggestion.getUser());
								topeventsuggestion.setSuggestionType("Friend's Suggestion");
								topeventsuggestion.setRank((long)count);
								topeventsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
								userDAO.saveTopEventSuggestions(topeventsuggestion);
							}
						}
						else if(forComunitySuggestion)
						{

							if(usereventsuggestion.getEvents().getPage().getPageId() == object[1])
							{
								Topeventsuggestion topeventsuggestion = new Topeventsuggestion();
								topeventsuggestion.setEvents(usereventsuggestion.getEvents());
								topeventsuggestion.setUser(usereventsuggestion.getUser());
								topeventsuggestion.setSuggestionType("Community Suggestion");
								topeventsuggestion.setRank((long)count);
								topeventsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
								userDAO.saveTopEventSuggestions(topeventsuggestion);
							}
						}
					}
				}
			}
		}
	}
}
