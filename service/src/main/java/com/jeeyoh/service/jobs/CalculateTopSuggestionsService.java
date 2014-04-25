package com.jeeyoh.service.jobs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
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

	String[] categoryList = {"RESTAURANT","MOVIES","SPORT","THEATER","CONCERT","SPA","NIGHTLIFE"};

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Autowired
	private IBusinessDAO businessDAO;

	@Autowired
	private IDealsDAO dealsDAO;

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
				List<User> userContactsList = userDAO.getUserContacts(userId);
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
						//for(Usercontacts usercontacts:userContactsList)
						//{
							//User contact = usercontacts.getUserByContactId();
							//logger.debug("Friend Name ::"+contact.getFirstName());
							//int contactId = contact.getUserId();
							// Get Non Deal Suggestions
							usernondealsuggestions = getFriendsNonDealSuggestionList(userId, 0, category, usernondealsuggestions, true,false);
							// Get Deal Suggestions
							userdealsuggestions = getFriendsDealSuggestionList(userId, 0, category, userdealsuggestions, true,false,false,false);
							//userdealsuggestions = getFriendsDealSuggestionList(userId, contactId, category, userdealsuggestions, false,true,false,false);
							// Get Event Suggestions
							usereventsuggestions = getFriendsEventSuggestionList(userId, 0, category, usereventsuggestions, true,false,false,false);
							//usereventsuggestions = getFriendsEventSuggestionList(userId, contactId, category, usereventsuggestions, false,true,false,false);
						//}

						//int totalCountForNonDeals = 0, totalCountForEvents = 0,totalCountForDeals = 0;

						int a =usereventsuggestions.size(),b=userdealsuggestions.size(),c=usernondealsuggestions.size();

						int n1=0,n2=0,n3=0,k=0;

						while (a>0||b>0||c>0) {

							if (a>0) {

								a--;
								n1++;
								k++;

								if (k==10)
									break;
							}

							if (b>0) {

								b--;
								n2++;
								k++;

								if (k==10)
									break;
							}

							if (c>0) {

								c--;
								n3++;
								k++;

								if (k==10)
									break;
							}

						}
						logger.debug("n1==,n2==,n3=="+n1 +" : "+n2 +" : "+n3);


						// Save Top 10 Non Deal Suggestions
						saveTopNonDealSuggestions(userId, category, usernondealsuggestions, true, false, false,n3);

						// Save Top 10 Deal Suggestions
						saveTopDealSuggestions(userId, category, userdealsuggestions, true, false, false, n2);

						// Save Top 10 Event Suggestions
						saveTopEventSuggestions(userId, category, usereventsuggestions, true, false, false,n1);
					}
				}
			}
		}
	}



	@Transactional
	@Override
	public void calculateTopCommunitySuggestions() {
		List<User> userList = userDAO.getUsers();
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
					//userdealsuggestions = getFriendsDealSuggestionList(userId, 0, category, userdealsuggestions, false,false,false,true);
					// Get Event Suggestions
					usereventsuggestions = getFriendsEventSuggestionList(userId, 0, category, usereventsuggestions, false,false,true,false);
					//usereventsuggestions = getFriendsEventSuggestionList(userId, 0, category, usereventsuggestions, false,false,false,true);
					

					int a =usereventsuggestions.size(),b=userdealsuggestions.size(),c=usernondealsuggestions.size();

					int n1=0,n2=0,n3=0,k=0;

					while (a>0||b>0||c>0) {

						if (a>0) {

							a--;
							n1++;
							k++;

							if (k==10)
								break;
						}

						if (b>0) {

							b--;
							n2++;
							k++;

							if (k==10)
								break;
						}

						if (c>0) {

							c--;
							n3++;
							k++;

							if (k==10)
								break;
						}

					}
					logger.debug("n1==,n2==,n3=="+n1 +" : "+n2 +" : "+n3);

					// Save Top 10 Non Deal Suggestions
					saveTopNonDealSuggestions(userId, category, usernondealsuggestions, false, false, true,n3);

					// Save Top 10 Deal Suggestions
					saveTopDealSuggestions(userId, category, userdealsuggestions, false, false, true,n2);

					// Save Top 10 Event Suggestions
					saveTopEventSuggestions(userId, category, usereventsuggestions, false, false, true,n1);
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
	private void saveTopNonDealSuggestions(int userId, String category, List<Usernondealsuggestion> usernondealsuggestions, boolean forFriendsSuggestion, boolean forComunitySuggestion, boolean forJeeyohSuggestion, int totalCount)
	{
		String idsStr = "";

		logger.debug("usernondealsuggestions:  "+usernondealsuggestions);

		if(usernondealsuggestions != null && !usernondealsuggestions.isEmpty())
		{
			for(int i = 0; i < usernondealsuggestions.size(); i++)
			{
				//Page page = (Page)usernondealsuggestions.get(i).getBusiness().getPages().iterator().next();
				idsStr +="'"+usernondealsuggestions.get(i).getBusiness().getId()+"'";

				if(i < usernondealsuggestions.size() - 1)
				{
					idsStr+=",";
				}
			}

			// Getting Likes count for non deal suggestion in descending order
			//List<Object[]> rows = userDAO.userNonDealSuggestionCount(idsStr,10);
			List<Object[]> rows = businessDAO.getNonDealLikeCountByPage(idsStr);


			ArrayList<String> idsArray = null;

			ArrayList<ArrayList<String>> main = new ArrayList<ArrayList<String>>();
			int likecount = 0;
			int count = 1;
			if(rows != null)
			{
				logger.debug("rows size: "+rows.size());
				for(int i = 0; i < rows.size(); i++)
				{
					if(count <= totalCount)
					{
						idsArray = new ArrayList<String>();
						//idsArray.setLength(0);
						likecount = Integer.parseInt(rows.get(i)[0].toString());
						//idsArray.append("'"+rows.get(i)[1]+"',");
						idsArray.add(rows.get(i)[1].toString());
						for(int j = i+1; j < rows.size(); j++)
						{
							if(Integer.parseInt(rows.get(i)[0].toString()) == Integer.parseInt(rows.get(j)[0].toString()))
							{
								idsArray.add(rows.get(j)[1].toString());
								//idsArray.append("'"+rows.get(j)[1]+"',");
								rows.remove(j);
								j--;
							}
						}

						if(idsArray.size() > 1)
						{
							List<Object[]> rows1 = businessDAO.getTopBusinessByRating(StringUtils.join(idsArray.toArray(new String[idsArray.size()]), ","));
							for(int k = 0; k < rows1.size(); k++)
							{
								if(count <= totalCount)
								{
									boolean isSaved = saveTopNonDealSuggestion(usernondealsuggestions, userId, Integer.parseInt(rows1.get(k)[0].toString()), likecount, category, forJeeyohSuggestion, forFriendsSuggestion, count);
									if(isSaved)
										count++;
								}
							}
						}
						else
						{
							boolean isSaved = saveTopNonDealSuggestion(usernondealsuggestions, userId, Integer.parseInt(idsArray.get(0).toString()), likecount, category, forJeeyohSuggestion, forFriendsSuggestion, count);
							if(isSaved)
								count++;
						}
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
	private void saveTopDealSuggestions(int userId, String category, List<Userdealssuggestion> userdealsuggestions, boolean forFriendsSuggestion, boolean forComunitySuggestion, boolean forJeeyohSuggestion, int totalCount)
	{
		StringBuilder pageIdsStr = new StringBuilder();
		StringBuilder dealIdsStr = new StringBuilder();

		logger.debug("userdealsuggestions size: "+userdealsuggestions);
		if(userdealsuggestions != null && !userdealsuggestions.isEmpty())
		{
			logger.debug("userdealsuggestions size:  "+userdealsuggestions.size());
			for(int i = 0; i < userdealsuggestions.size(); i++)
			{
				Userdealssuggestion userdealssuggestion = userdealsuggestions.get(i);
				/*if(userdealssuggestion.getSuggestionType().contains("Community"))
				{
					Page page = (Page)userdealssuggestion.getDeals().getBusiness().getPages().iterator().next();
					//pageIdsStr +="'"+page.getPageId()+"'";
					pageIdsStr.append("'"+page.getPageId()+"',");
					if(i < userdealsuggestions.size() - 1)
					{
						pageIdsStr+=",";
					}
				}
				else
				{*/
				dealIdsStr.append("'"+userdealssuggestion.getDeals().getId()+"',");
				//dealIdsStr +="'"+userdealssuggestion.getDeals().getId()+"'";
				/*if(i < userdealsuggestions.size() - 1)
					{
						dealIdsStr+=",";
					}*/
				//}
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
			//List<Object[]> rows1 = null;
			List<Object[]> rows2 = null;

			logger.debug("dealIdsStr: "+dealIdsStr +" : "+dealIdsStr.equals("")+"  pageIdsStr: "+pageIdsStr);
			// Getting Likes count for deal suggestion in descending order
			if(!dealIdsStr.equals(""))
			{
				/*if(dealIdsStr.lastIndexOf(",") == dealIdsStr.length()-1)
				{
					dealIdsStr = dealIdsStr.substring(0,dealIdsStr.length()-1);
				}*/
				dealIdsStr.deleteCharAt(dealIdsStr.length() - 1);
				rows = userDAO.userDealSuggestionCount(dealIdsStr.toString(),5);
				/*if(rows1 != null)
				{
					rows.addAll(rows1);
				}*/
			}
			/*if(!pageIdsStr.equals(""))
			{
				if(pageIdsStr.lastIndexOf(",") == pageIdsStr.length()-1)
				{
					pageIdsStr = pageIdsStr.substring(0,pageIdsStr.length()-1);
				}
				pageIdsStr.deleteCharAt(pageIdsStr.length() - 1);
				rows2 = userDAO.userNonDealSuggestionCount(pageIdsStr.toString(),5);
				logger.debug("rows2: "+rows2);
				if(rows2 != null)
				{
					rows.addAll(rows2);
				}
			}
			 */

			logger.debug("dealIdsStr: "+dealIdsStr +" : "+rows);
			//Collections.sort(rows, new SuggestionComparator());
			logger.debug("rows after sorting: "+rows);
			ArrayList<String> idsArray = null;

			int likecount = 0;
			if(rows != null)
			{
				logger.debug("rows size: "+rows.size());
				int count = 1;
				for(int i = 0; i < rows.size(); i++)
				{
					if(count <= totalCount)
					{
						idsArray = new ArrayList<String>();
						likecount = Integer.parseInt(rows.get(i)[0].toString());
						idsArray.add(rows.get(i)[1].toString());
						for(int j = i+1; j < rows.size(); j++)
						{
							if(Integer.parseInt(rows.get(i)[0].toString()) == Integer.parseInt(rows.get(j)[0].toString()))
							{
								idsArray.add(rows.get(j)[1].toString());
								rows.remove(j);
								j--;
							}
						}

						if(idsArray.size() > 1)
						{
							ArrayList<String> dealsIds = null;
							List<Object[]> rows1 = dealsDAO.getTopDealsByRating(StringUtils.join(idsArray.toArray(new String[idsArray.size()]), ","));
							if(rows1 != null)
							{
								for(int k = 0; k < rows1.size(); k++)
								{
									if(count <= totalCount)
									{
										dealsIds = new ArrayList<String>();
										dealsIds.add(rows1.get(k)[0].toString());
										for(int j = k+1; j < rows1.size(); j++)
										{
											if((rows1.get(k)[1] == null && rows1.get(j)[1] == null) || (Double.parseDouble(rows1.get(k)[1].toString()) == Double.parseDouble(rows1.get(j)[1].toString())))
											{
												dealsIds.add(rows1.get(j)[0].toString());
												rows1.remove(j);
												j--;
											}
										}

										if(dealsIds.size() > 1)
										{
											List<Object[]> array = dealsDAO.getDealLikeCountByPage(StringUtils.join(idsArray.toArray(new String[dealsIds.size()]), ",")) ;
											for(int j = 0; j < array.size(); j++)
											{
												if(count <= totalCount)
												{
													boolean isSaved = saveTopDealSuggestion(userdealsuggestions, userId, Integer.parseInt(array.get(j)[0].toString()), likecount, category, forJeeyohSuggestion, forFriendsSuggestion, count);
													if(isSaved)
														count++;
												}
											}

											if(dealsIds.size() > array.size())
											{
												if(count <= totalCount)
												{
													boolean found = false;
													for(int m = 0; m < dealsIds.size(); m++)
													{
														if(count <= totalCount)
														{
															for(int j = 0; j < array.size(); j++)
															{
																if(Integer.parseInt(dealsIds.get(m)) == Integer.parseInt(array.get(j)[1].toString()))
																{
																	found = true;
																}
															}
															if(!found)
															{
																boolean isSaved = saveTopDealSuggestion(userdealsuggestions, userId, Integer.parseInt(dealsIds.get(m).toString()), likecount, category, forJeeyohSuggestion, forFriendsSuggestion, count);
																if(isSaved)
																	count++;
															}

															//Set found bool back to false
															found = false;
														}
													}
												}
											}
										}
										else
										{
											boolean isSaved = saveTopDealSuggestion(userdealsuggestions, userId, Integer.parseInt(dealsIds.get(0)), likecount, category, forJeeyohSuggestion, forFriendsSuggestion, count);
											if(isSaved)
												count++;
										}
									}
								}
							}
						}
						else
						{
							boolean isSaved = saveTopDealSuggestion(userdealsuggestions, userId, Integer.parseInt(idsArray.get(0)), likecount, category, forJeeyohSuggestion, forFriendsSuggestion, count);
							if(isSaved)
								count++;
						}
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
	private void saveTopEventSuggestions(int userId, String category, List<Usereventsuggestion> usereventsuggestions, boolean forFriendsSuggestion, boolean forComunitySuggestion, boolean forJeeyohSuggestion, int totalCount)
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
				/*if(usereventsuggestion.getSuggestionType().contains("Community"))
				{
					//pageIdsStr +="'"+usereventsuggestions.get(i).getEvents().getPage().getPageId()+"',";
					pageIdsStr.append("'"+usereventsuggestions.get(i).getEvents().getPage().getPageId()+"',");
					if(i < usereventsuggestions.size() - 1)
					{
						pageIdsStr+=",";
					}
				}
				else
				{*/
				logger.debug("eventIdsStr::::  "+usereventsuggestions.get(i).getEvents().getEventId());
				eventIdsStr.append("'"+usereventsuggestions.get(i).getEvents().getEventId()+"',");
				//eventIdsStr +="'"+usereventsuggestions.get(i).getEvents().getEventId()+"',";
				/*if(i < usereventsuggestions.size() - 1)
					{
						eventIdsStr+=",";
					}*/
				//}
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
				rows = userDAO.userEventSuggestionCount(eventIdsStr.toString(),5);
				/*if(rows1 != null)
				{
					rows.addAll(rows1);
				}*/
			}
			/*if(!pageIdsStr.equals("") && pageIdsStr.length() > 0)
			{
				if(pageIdsStr.lastIndexOf(",") == pageIdsStr.length()-1)
				{
					pageIdsStr = pageIdsStr.substring(0,pageIdsStr.length()-1);
				}
				pageIdsStr.deleteCharAt(pageIdsStr.length() - 1);
				rows2 = userDAO.userNonDealSuggestionCount(pageIdsStr.toString(),5);
				logger.debug("rows2: "+rows2);
				if(rows2 != null)
				{
					rows.addAll(rows2);
				}
			}
			 */

			logger.debug("EventStr: "+eventIdsStr +" : "+rows);
			//Collections.sort(rows, new SuggestionComparator());
			int likecount = 0,id = 0;
			//StringBuilder idsArray = new StringBuilder();
			ArrayList<String> idsArray = null;
			HashMap<Integer, ArrayList<String>> rowMap = new HashMap<Integer, ArrayList<String>>();

			ArrayList<ArrayList<String>> main = new ArrayList<ArrayList<String>>();
			int count = 1;
			for(int i = 0; i < rows.size(); i++)
			{
				if(count <= totalCount)
				{
					idsArray = new ArrayList<String>();
					likecount = Integer.parseInt(rows.get(i)[0].toString());
					idsArray.add(rows.get(i)[1].toString());
					for(int j = i+1; j < rows.size(); j++)
					{
						if(Integer.parseInt(rows.get(i)[0].toString()) == Integer.parseInt(rows.get(j)[0].toString()))
						{
							idsArray.add(rows.get(j)[1].toString());
							rows.remove(j);
							j--;
						}
					}

					if(idsArray.size() > 1)
					{
						List<Object[]> array = eventsDAO.getEventLikeCountByPage(StringUtils.join(idsArray.toArray(new String[idsArray.size()]), ",")) ;
						for(int k = 0; k < array.size(); k++)
						{
							if(count <= totalCount)
							{
								boolean isSaved = saveTopEventSuggestion(usereventsuggestions, userId, Integer.parseInt(array.get(k)[0].toString()), Integer.parseInt(array.get(k)[1].toString()), category, forJeeyohSuggestion, forFriendsSuggestion, count);
								if(isSaved)
									count++;
							}
						}

						if(idsArray.size() > array.size())
						{
							if(count <= totalCount)
							{
								boolean found = false;
								for(int k = 0; k < idsArray.size(); k++)
								{
									if(count <= totalCount)
									{
										for(int j = 0; j < array.size(); j++)
										{
											if(Integer.parseInt(idsArray.get(k)) == Integer.parseInt(array.get(j)[1].toString()))
											{
												found = true;
												break;
											}
										}
										if(!found)
										{
											boolean isSaved = saveTopEventSuggestion(usereventsuggestions, userId, Integer.parseInt(idsArray.get(k)), likecount, category, forJeeyohSuggestion, forFriendsSuggestion, count);
											if(isSaved)
												count++;
										}

										//Set found bool back to false
										found = false;
									}
								}
							}
						}
					}
					else
					{
						boolean isSaved = saveTopEventSuggestion(usereventsuggestions, userId, Integer.parseInt(idsArray.get(0)), likecount, category, forJeeyohSuggestion, forFriendsSuggestion, count);
						if(isSaved)
							count++;
					}
				}
			}
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

	private boolean saveTopEventSuggestion(List<Usereventsuggestion> usereventsuggestions, int userId, int eventId, int totalLikes, String category, boolean forJeeyohSuggestion, boolean forFriendsSuggestion,int count)
	{
		boolean isSaved = false;
		for(Usereventsuggestion usereventsuggestion : usereventsuggestions)
		{
			Events events = usereventsuggestion.getEvents();

			/*if(usereventsuggestion.getSuggestionType().contains("Community"))
			{
				if(usereventsuggestion.getEvents().getPage().getPageId() == eventId)
				{
					//Checking if the suggestions is already exists or not
					List<Topeventsuggestion> topeventsuggestions = userDAO.isTopEventSuggestionExists(userId, events.getEventId());
					if(topeventsuggestions == null || topeventsuggestions.size() == 0)
					{
						if(count <= 4)
						{
						Topeventsuggestion topeventsuggestion = new Topeventsuggestion();
						topeventsuggestion.setEvents(events);
						topeventsuggestion.setUser(usereventsuggestion.getUser());

						if(forJeeyohSuggestion)
							topeventsuggestion.setSuggestionType("Jeeyoh Suggestion");
						else if(forFriendsSuggestion)
							topeventsuggestion.setSuggestionType("Friend's Suggestion");

						topeventsuggestion.setRank((long)count);
						topeventsuggestion.setTotalLikes(totalLikes);
						topeventsuggestion.setCategoryType(category);
						topeventsuggestion.setCreatedTime(new Date());
						topeventsuggestion.setUpdatedTime(new Date());
						userDAO.saveTopEventSuggestions(topeventsuggestion);
						//isAccessed = true;
						count++;
						}
					}
					break;
				}
			}
			else
			{*/
			if(events.getEventId() == eventId)
			{
				//Checking if the suggestions is already exists or not
				List<Topeventsuggestion> topeventsuggestions = userDAO.isTopEventSuggestionExists(userId, events.getEventId());
				if(topeventsuggestions == null || topeventsuggestions.size() == 0)
				{
					//Checking if the suggestions is already exists or not

					Topeventsuggestion topeventsuggestion = new Topeventsuggestion();
					if(usereventsuggestion.getUserContact() != null)
						topeventsuggestion.setUserContact(usereventsuggestion.getUserContact());
					topeventsuggestion.setEvents(events);
					topeventsuggestion.setUser(usereventsuggestion.getUser());

					if(forJeeyohSuggestion)
						topeventsuggestion.setSuggestionType("Jeeyoh Suggestion");
					else if(forFriendsSuggestion)
						topeventsuggestion.setSuggestionType("Friend's Suggestion");

					topeventsuggestion.setRank((long)count);
					topeventsuggestion.setTotalLikes(totalLikes);
					topeventsuggestion.setCategoryType(category);
					Date date = new Date();
					topeventsuggestion.setCreatedTime(date);
					topeventsuggestion.setUpdatedTime(date);
					userDAO.saveTopEventSuggestions(topeventsuggestion);
					isSaved = true;
					count++;
				}
				break;
			}
			//}
		}
		return isSaved;
	}



	private boolean saveTopNonDealSuggestion(List<Usernondealsuggestion> usernondealsuggestions, int userId, int businessId, int totalLikes, String category, boolean forJeeyohSuggestion, boolean forFriendsSuggestion,int count)
	{
		boolean isSaved = false;
		for(Usernondealsuggestion usernondealsuggestion : usernondealsuggestions)
		{
			//Page page = (Page)usernondealsuggestion.getBusiness().getPages().iterator().next();
			if(usernondealsuggestion.getBusiness().getId() == businessId)
			{
				Business business = usernondealsuggestion.getBusiness();
				//Checking if the suggestions is already exists or not
				List<Topnondealsuggestion> topnondealsuggestions = userDAO.isTopNonDealSuggestionExists(userId, business.getId());
				if(topnondealsuggestions == null || topnondealsuggestions.size() == 0)
				{
					Topnondealsuggestion topnondealsuggestion = new Topnondealsuggestion();
					if(usernondealsuggestion.getUserContact() != null)
						topnondealsuggestion.setUserContact(usernondealsuggestion.getUserContact());
					topnondealsuggestion.setBusiness(business);
					topnondealsuggestion.setUser(usernondealsuggestion.getUser());
					if(forFriendsSuggestion)
						topnondealsuggestion.setSuggestionType("Friend's Suggestion");
					else if(forJeeyohSuggestion)
						topnondealsuggestion.setSuggestionType("Jeeyoh Suggestion");
					topnondealsuggestion.setRank((long)count);
					topnondealsuggestion.setTotalLikes(totalLikes);
					Date date = new Date();
					topnondealsuggestion.setCreatedTime(date);
					topnondealsuggestion.setUpdatedTime(date);
					topnondealsuggestion.setCategoryType(category);
					userDAO.saveTopNonDealSuggestions(topnondealsuggestion);
					isSaved = true;
				}
				break;
			}
		}
		return isSaved;
	}



	private boolean saveTopDealSuggestion(List<Userdealssuggestion> userdealsuggestions, int userId, int dealId, int totalLikes, String category, boolean forJeeyohSuggestion, boolean forFriendsSuggestion,int count)
	{
		boolean isSaved = false;
		for(Userdealssuggestion userdealsuggestion : userdealsuggestions)
		{
			Deals deal = userdealsuggestion.getDeals();


			if(deal.getId() == dealId)
			{
				//Checking if the suggestions is already exists or not
				List<Topdealssuggestion> topdealsuggestions = userDAO.isTopDealSuggestionExists(userId, deal.getId());
				if(topdealsuggestions == null || topdealsuggestions.size() == 0)
				{
					Topdealssuggestion topdealsuggestion = new Topdealssuggestion();
					if(userdealsuggestion.getUserContact() != null)
						topdealsuggestion.setUserContact(userdealsuggestion.getUserContact());
					topdealsuggestion.setDeals(deal);
					topdealsuggestion.setUser(userdealsuggestion.getUser());
					if(forJeeyohSuggestion)
						topdealsuggestion.setSuggestionType("Jeeyoh Suggestion");
					else if(forFriendsSuggestion)
						topdealsuggestion.setSuggestionType("Friend's Suggestion");
					topdealsuggestion.setRank((long)count);
					topdealsuggestion.setTotalLikes(totalLikes);
					topdealsuggestion.setCategoryType(category);
					Date date = new Date();
					topdealsuggestion.setCreatedTime(date);
					topdealsuggestion.setUpdatedTime(date);
					userDAO.saveTopDealSuggestions(topdealsuggestion);
					isSaved = true;
				}
				break;
			}
		}

		return isSaved;
	}



}
