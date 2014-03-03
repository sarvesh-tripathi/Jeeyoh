package com.jeeyoh.service.jobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Gdealoption;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.Topcommunitysuggestion;
import com.jeeyoh.persistence.domain.Topdealssuggestion;
import com.jeeyoh.persistence.domain.Topeventsuggestion;
import com.jeeyoh.persistence.domain.Topnondealsuggestion;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;

@Component("calculateTopSuggestions")
public class CalculateTopSuggestionsService implements ICalculateTopSuggestionsService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	String[] categoryList = {"RESTAURANT","Movies","Sport","THEATER","CONCERT","SPA","NIGHT_LIFE"};

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
					for(int i = 0; i < categoryList.length; i++)
					{
						List<Usernondealsuggestion> usernondealsuggestions = new ArrayList<Usernondealsuggestion>();
						List<Userdealssuggestion> userdealsuggestions = new ArrayList<Userdealssuggestion>();
						List<Usereventsuggestion> usereventsuggestions = new ArrayList<Usereventsuggestion>();
						String category = categoryList[i];
						logger.debug("Category: "+category);
						for(Usercontacts usercontacts:userContactsList)
						{
							User contact = usercontacts.getUserByContactId();
							logger.debug("Friend Name ::"+contact.getFirstName());
							int contactId = contact.getUserId();
							// Get Non Deal Suggestions
							usernondealsuggestions = getFriendsNonDealSuggestionList(userId, contactId, category, usernondealsuggestions, true,false);
							// Get Deal Suggestions
							userdealsuggestions = getFriendsDealSuggestionList(userId, contactId, category, userdealsuggestions, true,false,false,false);
							userdealsuggestions = getFriendsDealSuggestionList(userId, contactId, category, userdealsuggestions, false,true,false,false);
							// Get Event Suggestions
							usereventsuggestions = getFriendsEventSuggestionList(userId, contactId, category, usereventsuggestions, true,false,false,false);
							usereventsuggestions = getFriendsEventSuggestionList(userId, contactId, category, usereventsuggestions, false,true,false,false);
						}

						// Save Top 10 Non Deal Suggestions
						//saveTopNonDealSuggestions(userId, category, usernondealsuggestions, true, false, false);

						// Save Top 10 Deal Suggestions
						//saveTopDealSuggestions(userId, category, userdealsuggestions, true, false, false);

						// Save Top 10 Event Suggestions
						saveTopEventSuggestions(userId, category, usereventsuggestions, true, false, false);
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

				for(int i = 0; i < categoryList.length; i++)
				{
					String category = categoryList[i];
					logger.debug("Category: "+category);
					// Getting Likes count for community suggestion in descending order
					List<Object[]> rows = userDAO.getuserCommunitySuggestionsByLikesCount(userId,category);
					int count = 0;
					if(rows != null)
					{
						logger.debug("rows: "+rows.size());
						for(Object[] object : rows)
						{
							count++;
							if(count <= 10)
							{
								Pageuserlikes pageuserlikes = (Pageuserlikes) object[1];
								Topcommunitysuggestion topcommunitysuggestion = new Topcommunitysuggestion();
								topcommunitysuggestion.setPage(pageuserlikes.getPage());
								topcommunitysuggestion.setUser(user);
								topcommunitysuggestion.setRank((long)count);
								topcommunitysuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
								topcommunitysuggestion.setCreatedTime(new Date());
								topcommunitysuggestion.setUpdatedTime(new Date());
								topcommunitysuggestion.setCategoryType(category);
								userDAO.saveTopCommunitySuggestions(topcommunitysuggestion);
							}
						}
					}
				}

				/*// Get Non Deal Suggestions
				List<Usernondealsuggestion> usernondealsuggestions = userDAO.getUserNonDealsSuggestionByUserIdForCommunity(userId);
				// Get Deal Suggestions
				List<Userdealssuggestion> userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForCommunity(userId);
				// Get Event Suggestions
				List<Usereventsuggestion> usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForCommunity(userId);

				// Save Top 10 Non Deal Suggestions
				saveTopNonDealSuggestions(userId, usernondealsuggestions, true, false, false);

				// Save Top 10 Deal Suggestions
				saveTopDealSuggestions(userId, userdealsuggestions, true, false, false);

				// Save Top 10 Event Suggestions
				saveTopEventSuggestions(userId, usereventsuggestions, true, false, false);*/
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

				for(int i = 0; i < categoryList.length; i++)
				{
					List<Userdealssuggestion> userdealsuggestions = new ArrayList<Userdealssuggestion>();
					List<Usereventsuggestion> usereventsuggestions = new ArrayList<Usereventsuggestion>();

					String category = categoryList[i];
					logger.debug("Category: "+category);
					// Get Non Deal Suggestions
					List<Usernondealsuggestion> usernondealsuggestions = userDAO.getUserNonDealsSuggestionByUserIdForCommunity(userId, category);
					// Get Deal Suggestions
					userdealsuggestions = getFriendsDealSuggestionList(userId, 0, category, userdealsuggestions, false,false,true,false);
					userdealsuggestions = getFriendsDealSuggestionList(userId, 0, category, userdealsuggestions, false,false,false,true);
					// Get Event Suggestions
					usereventsuggestions = getFriendsEventSuggestionList(userId, 0, category, usereventsuggestions, false,false,true,false);
					usereventsuggestions = getFriendsEventSuggestionList(userId, 0, category, usereventsuggestions, false,false,false,true);
					/*// Get Deal Suggestions
					List<Userdealssuggestion> userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForJeeyoh(userId);
					// Get Event Suggestions
					List<Usereventsuggestion> usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForJeeyoh(userId);
					 */
					// Save Top 10 Non Deal Suggestions
					//saveTopNonDealSuggestions(userId, category, usernondealsuggestions, false, false, true);

					// Save Top 10 Deal Suggestions
					//saveTopDealSuggestions(userId, category, userdealsuggestions, false, false, true);

					// Save Top 10 Event Suggestions
					saveTopEventSuggestions(userId, category, usereventsuggestions, false, false, true);
				}

			}
		}
	}



	/**
	 * Get DealSuggestions for All friends
	 * @param userNonDealSuggestionMainList
	 * @return
	 */
	private List<Userdealssuggestion> getFriendsDealSuggestionList(int userId, int contactId, String category, List<Userdealssuggestion> userDealSuggestionMainList, boolean forFriendsSuggestion, boolean forFriendsCommunitySuggestion, boolean forJeeyohSuggestion, boolean forJeeyohCommunitySuggestion)
	{

		// Get Deals Suggestions
		List<Userdealssuggestion> userdealsuggestions = null;
		if(forFriendsSuggestion)
			userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForFriends(userId, contactId, category);
		else if(forFriendsCommunitySuggestion)
			userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForFriendsCommunity(userId, contactId, category);
		else if(forJeeyohSuggestion)
			userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForJeeyoh(userId, category);
		else if(forJeeyohCommunitySuggestion)
			userdealsuggestions = userDAO.getUserDealSuggestionByUserIdForCommunity(userId, category);

		if(userdealsuggestions != null)
			userDealSuggestionMainList.addAll(userdealsuggestions);

		return userDealSuggestionMainList;

	}


	/**
	 * Get EventSuggestions for All friends
	 * @param userNonDealSuggestionMainList
	 * @return
	 */
	private List<Usereventsuggestion> getFriendsEventSuggestionList(int userId, int contactId, String category, List<Usereventsuggestion> userEventSuggestionMainList, boolean forFriendsSuggestion, boolean forFriendsCommunitySuggestion, boolean forJeeyohSuggestion, boolean forJeeyohCommunitySuggestion)
	{
		// Get Event Suggestions
		List<Usereventsuggestion> usereventsuggestions = null;
		if(forFriendsSuggestion)
			usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForFriends(userId, contactId, category);
		else if(forFriendsCommunitySuggestion)
			usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForFriendsCommunity(userId, contactId, category);
		else if(forJeeyohSuggestion)
			usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForJeeyoh(userId, category);
		else if(forJeeyohCommunitySuggestion)
			usereventsuggestions = userDAO.getUserEventsSuggestionByUserIdForCommunity(userId, category);

		if(usereventsuggestions != null)
			userEventSuggestionMainList.addAll(usereventsuggestions);
		return userEventSuggestionMainList;

	}



	/**
	 * Get NonDealSuggestions for All friends
	 * @param userNonDealSuggestionMainList
	 * @return
	 */
	private List<Usernondealsuggestion> getFriendsNonDealSuggestionList(int userId, int contactId, String category, List<Usernondealsuggestion> userNonDealSuggestionMainList, boolean forFriendsSuggestion,  boolean forJeeyohSuggestion)
	{
		// Get Non Deal Suggestions
		List<Usernondealsuggestion> usernondealsuggestions = new ArrayList<Usernondealsuggestion>();
		if(forFriendsSuggestion)
			usernondealsuggestions = userDAO.getUserNonDealsSuggestionByUserIdForFriends(userId,contactId,category);
		else if(forJeeyohSuggestion)
			usernondealsuggestions = userDAO.getUserNonDealsSuggestionByUserIdForJeeyoh(userId,category);

		if(usernondealsuggestions != null)
			userNonDealSuggestionMainList.addAll(usernondealsuggestions);
		return userNonDealSuggestionMainList;

	}

	/**
	 * This saves the top NonDeal suggestions into DataBase
	 * @param userId
	 * @param contactId
	 */
	private void saveTopNonDealSuggestions(int userId, String category, List<Usernondealsuggestion> usernondealsuggestions, boolean forFriendsSuggestion, boolean forComunitySuggestion, boolean forJeeyohSuggestion)
	{
		String pageIdsStr = "";

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

			int count = 1;
			if(rows != null)
			{
				logger.debug("rows: "+rows.size());
				for(Object[] object : rows)
				{
					if(count <= 10)
					{
						for(Usernondealsuggestion usernondealsuggestion : usernondealsuggestions)
						{
							Page page = (Page)usernondealsuggestion.getBusiness().getPages().iterator().next();
							if(page.getPageId() == Integer.parseInt(object[1].toString()))
							{
								Business business = usernondealsuggestion.getBusiness();
								//Checking if the suggestions is already exists or not
								List<Topnondealsuggestion> topnondealsuggestions = userDAO.isTopNonDealSuggestionExists(userId, business.getId());
								if(topnondealsuggestions == null || topnondealsuggestions.size() == 0)
								{
									Topnondealsuggestion topnondealsuggestion = new Topnondealsuggestion();
									topnondealsuggestion.setBusiness(business);
									topnondealsuggestion.setUser(usernondealsuggestion.getUser());
									if(forFriendsSuggestion)
										topnondealsuggestion.setSuggestionType("Friend's Suggestion");
									else if(forJeeyohSuggestion)
										topnondealsuggestion.setSuggestionType("Jeeyoh Suggestion");
									topnondealsuggestion.setRank((long)count);
									topnondealsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
									topnondealsuggestion.setCreatedTime(new Date());
									topnondealsuggestion.setUpdatedTime(new Date());
									topnondealsuggestion.setCategoryType(category);
									userDAO.saveTopNonDealSuggestions(topnondealsuggestion);
									count++;
								}
								break;
							}
						}
						logger.debug("userNonDealSuggestions: "+object[0] +" : "+ object[1]);
					}
				}
			}
		}
	}


	/**
	 * This saves the top suggestions into DataBase
	 * @param userId
	 * @param contactId
	 */
	private void saveTopDealSuggestions(int userId, String category, List<Userdealssuggestion> userdealsuggestions, boolean forFriendsSuggestion, boolean forComunitySuggestion, boolean forJeeyohSuggestion)
	{
		String dealIdsStr = "";
		String pageIdsStr = "";
		logger.debug("userdealsuggestions size: "+userdealsuggestions);
		if(userdealsuggestions != null && !userdealsuggestions.isEmpty())
		{
			logger.debug("userdealsuggestions size:  "+userdealsuggestions.size());
			for(int i = 0; i < userdealsuggestions.size(); i++)
			{
				Userdealssuggestion userdealssuggestion = userdealsuggestions.get(i);
				if(userdealssuggestion.getSuggestionType().contains("Community"))
				{
					Page page = (Page)userdealssuggestion.getDeals().getBusiness().getPages().iterator().next();
					pageIdsStr +="'"+page.getPageId()+"'";
					if(i < userdealsuggestions.size() - 1)
					{
						pageIdsStr+=",";
					}
				}
				else
				{
					dealIdsStr +="'"+userdealssuggestion.getDeals().getId()+"'";
					if(i < userdealsuggestions.size() - 1)
					{
						dealIdsStr+=",";
					}
				}
				/*else if(forComunitySuggestion)
				{
					pageIdsStr = "";
					Page page = (Page)userdealsuggestions.get(i).getDeals().getBusiness().getPages().iterator().next();
					pageIdsStr +="'"+page.getPageId()+"'";
					if(i < userdealsuggestions.size() - 1)
					{
						pageIdsStr+=",";
					}
				}*/
			}


			List<Object[]> rows = new ArrayList<Object[]>();
			List<Object[]> rows1 = null;
			List<Object[]> rows2 = null;

			logger.debug("dealIdsStr: "+dealIdsStr +" : "+dealIdsStr.equals("")+"  pageIdsStr: "+pageIdsStr);
			// Getting Likes count for deal suggestion in descending order
			if(!dealIdsStr.equals(""))
			{
				if(dealIdsStr.lastIndexOf(",") == dealIdsStr.length()-1)
				{
					dealIdsStr = dealIdsStr.substring(0,dealIdsStr.length()-1);
				}
				rows1 = userDAO.userDealSuggestionCount(dealIdsStr);
				if(rows1 != null)
				{
					rows.addAll(rows1);
				}
			}
			if(!pageIdsStr.equals(""))
			{
				if(pageIdsStr.lastIndexOf(",") == pageIdsStr.length()-1)
				{
					pageIdsStr = pageIdsStr.substring(0,pageIdsStr.length()-1);
				}
				rows2 = userDAO.userNonDealSuggestionCount(pageIdsStr);
				logger.debug("rows2: "+rows2);
				if(rows2 != null)
				{
					rows.addAll(rows2);
				}

			}


			logger.debug("dealIdsStr: "+dealIdsStr +" : "+rows);
			Collections.sort(rows, new SuggestionComparator());
			logger.debug("rows after sorting: "+rows);

			if(rows != null)
			{
				int count = 1;
				logger.debug("rows: "+rows.size());
				boolean isAccessed = false;
				for(Object[] object : rows)
				{
					isAccessed = false;
					if(count <= 10)
					{
						for(Userdealssuggestion userdealsuggestion : userdealsuggestions)
						{
							Deals deal = userdealsuggestion.getDeals();
							if(userdealsuggestion.getSuggestionType().contains("Community"))
							{
								Page page = (Page)userdealsuggestion.getDeals().getBusiness().getPages().iterator().next();
								if(page.getPageId() == object[1])
								{
									//Checking if the suggestions is already exists or not
									List<Topdealssuggestion> topdealsuggestions = userDAO.isTopDealSuggestionExists(userId, deal.getId());
									if(topdealsuggestions == null || topdealsuggestions.size() == 0)
									{
										//if(count <= 10)
										//{
										Topdealssuggestion topdealsuggestion = new Topdealssuggestion();
										topdealsuggestion.setDeals(deal);
										topdealsuggestion.setUser(userdealsuggestion.getUser());
										if(forJeeyohSuggestion)
											topdealsuggestion.setSuggestionType("Jeeyoh Suggestion");
										else if(forFriendsSuggestion)
											topdealsuggestion.setSuggestionType("Friend's Suggestion");
										topdealsuggestion.setRank((long)count);
										topdealsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
										topdealsuggestion.setCategoryType(category);
										topdealsuggestion.setCreatedTime(new Date());
										topdealsuggestion.setUpdatedTime(new Date());
										userDAO.saveTopDealSuggestions(topdealsuggestion);
										//isAccessed = true;
										count++;
										//}
									}
									break;
								}
								/*else
								{
									if(isAccessed)
									{
										break;
									}
								}*/
							}

							else
							{
								if(deal.getId() == Integer.parseInt(object[1].toString()))
								{
									//Checking if the suggestions is already exists or not
									List<Topdealssuggestion> topdealsuggestions = userDAO.isTopDealSuggestionExists(userId, deal.getId());
									if(topdealsuggestions == null || topdealsuggestions.size() == 0)
									{
										Topdealssuggestion topdealsuggestion = new Topdealssuggestion();
										topdealsuggestion.setDeals(deal);
										topdealsuggestion.setUser(userdealsuggestion.getUser());
										if(forJeeyohSuggestion)
											topdealsuggestion.setSuggestionType("Jeeyoh Suggestion");
										else if(forFriendsSuggestion)
											topdealsuggestion.setSuggestionType("Friend's Suggestion");
										topdealsuggestion.setRank((long)count);
										topdealsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
										topdealsuggestion.setCategoryType(category);
										topdealsuggestion.setCreatedTime(new Date());
										topdealsuggestion.setUpdatedTime(new Date());
										userDAO.saveTopDealSuggestions(topdealsuggestion);
										count++;
									}
									break;
								}
							}
							/*else if(forJeeyohSuggestion)
							{
								Page page = (Page)userdealsuggestion.getDeals().getBusiness().getPages().iterator().next();
								if(page.getPageId() == object[1])
								{
									//Checking if the suggestions is already exists or not
									List<Topdealssuggestion> topdealsuggestions = userDAO.isTopDealSuggestionExists(userId, deal.getId());
									if(topdealsuggestions == null || topdealsuggestions.size() == 0)
									{
										Topdealssuggestion topdealsuggestion = new Topdealssuggestion();
										topdealsuggestion.setDeals(deal);
										topdealsuggestion.setUser(userdealsuggestion.getUser());
										topdealsuggestion.setSuggestionType("Jeeyoh Suggestion");
										topdealsuggestion.setRank((long)count);
										topdealsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
										topdealsuggestion.setCreatedTime(new Date());
										topdealsuggestion.setUpdatedTime(new Date());
										userDAO.saveTopDealSuggestions(topdealsuggestion);
									}
									break;
								}
							}*/
						}
						logger.debug("userDealSuggestions: "+object[0] +" : "+ object[1]);
					}
				}
			}
		}
	}



	/**
	 * This saves the top suggestions into DataBase
	 * @param userId
	 * @param contactId
	 */
	private void saveTopEventSuggestions(int userId, String category, List<Usereventsuggestion> usereventsuggestions, boolean forFriendsSuggestion, boolean forComunitySuggestion, boolean forJeeyohSuggestion)
	{
		StringBuilder pageIdsStr = new StringBuilder();
		StringBuilder eventIdsStr = new StringBuilder();
		//String eventIdsStr = "";
		if(usereventsuggestions != null && !usereventsuggestions.isEmpty())
		{
			logger.debug("usereventsuggestions size:  "+usereventsuggestions.size());
			for(int i = 0; i < usereventsuggestions.size(); i++)
			{
				Usereventsuggestion usereventsuggestion = usereventsuggestions.get(i);
				if(usereventsuggestion.getSuggestionType().contains("Community"))
				{
					//pageIdsStr +="'"+usereventsuggestions.get(i).getEvents().getPage().getPageId()+"',";
					pageIdsStr.append("'"+usereventsuggestions.get(i).getEvents().getPage().getPageId()+"',");
					/*if(i < usereventsuggestions.size() - 1)
					{
						pageIdsStr+=",";
					}*/
				}
				else
				{
					logger.debug("eventIdsStr::::  "+usereventsuggestions.get(i).getEvents().getEventId());
					eventIdsStr.append("'"+usereventsuggestions.get(i).getEvents().getEventId()+"',");
					//eventIdsStr +="'"+usereventsuggestions.get(i).getEvents().getEventId()+"',";
					/*if(i < usereventsuggestions.size() - 1)
					{
						eventIdsStr+=",";
					}*/
				}
			}

			List<Object[]> rows = new ArrayList<Object[]>();
			List<Object[]> rows1 = null;
			List<Object[]> rows2 = null;

			logger.debug("EventStr: "+eventIdsStr +"  pageIdsStr: "+pageIdsStr);
			// Getting Likes count for even suggestion in descending order
			if(!eventIdsStr.equals("") && eventIdsStr.length() > 0)
			{
				/*if(eventIdsStr.lastIndexOf(",") == eventIdsStr.length()-1)
				{
					eventIdsStr = eventIdsStr.substring(0,eventIdsStr.length()-1);
				}*/

				eventIdsStr.deleteCharAt(eventIdsStr.length() - 1);
				rows1 = userDAO.userEventSuggestionCount(eventIdsStr.toString());
				if(rows1 != null)
				{
					rows.addAll(rows1);
				}
			}
			if(!pageIdsStr.equals("") && pageIdsStr.length() > 0)
			{
				/*if(pageIdsStr.lastIndexOf(",") == pageIdsStr.length()-1)
				{
					pageIdsStr = pageIdsStr.substring(0,pageIdsStr.length()-1);
				}*/
				pageIdsStr.deleteCharAt(pageIdsStr.length() - 1);
				rows2 = userDAO.userNonDealSuggestionCount(pageIdsStr.toString());
				logger.debug("rows2: "+rows2);
				if(rows2 != null)
				{
					rows.addAll(rows2);
				}
			}


			logger.debug("EventStr: "+eventIdsStr +" : "+rows);
			Collections.sort(rows, new SuggestionComparator());
			int likecount = 0,id = 0;
			StringBuilder idsArray = new StringBuilder();
			HashMap<Integer, String> rowMap = new HashMap<Integer, String>();
			for(int i = 0; i < rows.size(); i++)
			{

				Object[] object = rows.get(i);
				logger.debug("Row items: "+object[0] +" : "+object[1]);
				int eventId = Integer.parseInt(object[1].toString());
				if (i == 0) {

					likecount = Integer.parseInt(object[0].toString());
					//idsArray += "'" +object[0]+"'" ;
					idsArray.append("'" +object[1]+"',");

				} else {  
					int pregEventId = Integer.parseInt(rows.get(i - 1)[1].toString());  
					if (eventId != pregEventId) {
						idsArray.deleteCharAt(idsArray.length() - 1);
						rowMap.put(likecount, idsArray.toString());
						likecount = Integer.parseInt(object[0].toString());

						idsArray.setLength(0);
						logger.debug("object[1]1111: "+object[1]);
						idsArray.append("'" +object[1]+"',");
					}
					else
					{
						logger.debug("object[1]: "+object[1]);
						idsArray.append("'" +object[1]+"',");
					}


				}
			}

			logger.debug("HashMap:  "+rowMap);
			/*if(rows != null)
			{
				logger.debug("rows: "+rows.size());
				int count = 1;
				boolean isAccessed = false;
				for(Object[] object : rows)
				{
					isAccessed = false;
					if(count <= 10)
					{
						for(Usereventsuggestion usereventsuggestion : usereventsuggestions)
						{
							Events events = usereventsuggestion.getEvents();

							if(usereventsuggestion.getSuggestionType().contains("Community"))
							{
								if(usereventsuggestion.getEvents().getPage().getPageId() == Integer.parseInt(object[1].toString()))
								{
									//Checking if the suggestions is already exists or not
									List<Topeventsuggestion> topeventsuggestions = userDAO.isTopEventSuggestionExists(userId, events.getEventId());
									if(topeventsuggestions == null || topeventsuggestions.size() == 0)
									{
										//if(count <= 10)
										//{
											Topeventsuggestion topeventsuggestion = new Topeventsuggestion();
											topeventsuggestion.setEvents(events);
											topeventsuggestion.setUser(usereventsuggestion.getUser());

											if(forJeeyohSuggestion)
												topeventsuggestion.setSuggestionType("Jeeyoh Suggestion");
											else if(forFriendsSuggestion)
												topeventsuggestion.setSuggestionType("Friend's Suggestion");

											topeventsuggestion.setRank((long)count);
											topeventsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
											topeventsuggestion.setCategoryType(category);
											topeventsuggestion.setCreatedTime(new Date());
											topeventsuggestion.setUpdatedTime(new Date());
											userDAO.saveTopEventSuggestions(topeventsuggestion);
											//isAccessed = true;
											count++;
										//}
									}
									break;
								}
								else
								{
									if(isAccessed)
									{
										break;
									}
								}

							}
							else
							{
								if(events.getEventId() == Integer.parseInt(object[1].toString()))
								{
									//Checking if the suggestions is already exists or not
									List<Topeventsuggestion> topeventsuggestions = userDAO.isTopEventSuggestionExists(userId, events.getEventId());
									if(topeventsuggestions == null || topeventsuggestions.size() == 0)
									{
										//Checking if the suggestions is already exists or not

										Topeventsuggestion topeventsuggestion = new Topeventsuggestion();
										topeventsuggestion.setEvents(events);
										topeventsuggestion.setUser(usereventsuggestion.getUser());

										if(forJeeyohSuggestion)
											topeventsuggestion.setSuggestionType("Jeeyoh Suggestion");
										else if(forFriendsSuggestion)
											topeventsuggestion.setSuggestionType("Friend's Suggestion");

										topeventsuggestion.setRank((long)count);
										topeventsuggestion.setTotalLikes(Integer.parseInt(object[0].toString()));
										topeventsuggestion.setCategoryType(category);
										topeventsuggestion.setCreatedTime(new Date());
										topeventsuggestion.setUpdatedTime(new Date());
										userDAO.saveTopEventSuggestions(topeventsuggestion);
										count++;
									}
									break;
								}
							}
						}
					}
				}
			}*/
		}
	}


	/**
	 * Comparator class to compare suggestions by like count
	 */
	public class SuggestionComparator implements Comparator<Object[]> {
		@Override
		public int compare(Object[] o1, Object[] o2) 
		{
			return (int)(Integer.parseInt(o2[0].toString())-Integer.parseInt(o1[0].toString()));
		}
	}
}
