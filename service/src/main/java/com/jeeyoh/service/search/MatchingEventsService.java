package com.jeeyoh.service.search;

import java.util.ArrayList;
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
import com.jeeyoh.persistence.domain.Topdealssuggestion;
import com.jeeyoh.persistence.domain.Topeventsuggestion;
import com.jeeyoh.persistence.domain.Topnondealsuggestion;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.utils.Utils;

@Component("matchingEventsService")
public class MatchingEventsService implements IMatchingEventsService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	String[] categoryList = {"RESTAURANT","Movies","Sport","THEATER","CONCERT","SPA","NIGHT_LIFE"};
	@Autowired
	private IEventsDAO eventsDAO;

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IDealsDAO dealsDAO;

	@Autowired
	private IBusinessDAO businessDAO;

	@Override
	@Transactional
	public void searchMatchingEvents() {

		List<User> userList = userDAO.getUsers();
		logger.debug("searchMatchingEvents ==> search ==> ");
		//Date weekendDate = Utils.getNearestWeekend(null);
		if(userList != null) {
			for(User user : userList) {
				logger.debug("caculateTopSuggestions ==> search ==> userID ==> " + user.getEmailId());
				int userId = user.getUserId();
				//List<User> userList = userDAO.getUserById(userId);
				List<UserCategory> userCategoryList = null;

				double[] array = null;
				//if(userList!=null)
				//{
				//User user = userList.get(0);
				if(user.getLattitude()==null && user.getLongitude()==null || user.getLattitude().trim().equals("") && user.getLongitude().trim().equals("")){
					array = Utils.getLatLong(user.getZipcode());
					user.setLattitude(Double.toString(array[0]));
					user.setLongitude(Double.toString(array[1]));
					user.setCreatedtime(userList.get(0).getCreatedtime());

				}
				for(int i = 0; i < categoryList.length; i++)
				{
					String category = categoryList[i];
					userCategoryList = userDAO.getUserCategoryLikesByType(userId,category);
					logger.debug("category::  "+category);
					logger.debug("userCategoryList=>"+userCategoryList);
					List<Events> userFriendsCommonLikeEvents = new ArrayList<Events>();
					List<Deals> userFriendsCommonLikeDeals = new ArrayList<Deals>();
					List<Business> userFriendsCommonLikeBusiness = new ArrayList<Business>();
					//List<Events> getUserFavLikeEventsList = userDAO.getUserLikesEvents(userId,Double.parseDouble(user.getLattitude()),Double.parseDouble(user.getLongitude()));
					List<User> getStarFiftyMilesUserList = userDAO.getStarFriends(userId, Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
					logger.debug("getStarFiftyMilesUserList=>"+getStarFiftyMilesUserList);

					if(userCategoryList != null)
					{
						for(UserCategory userCategory : userCategoryList) {
							logger.debug("userCategoryList"+userCategory.getItemType());
							//UserCategoryLikes userCategoryLikes = (UserCategoryLikes)userCategory.getUserCategoryLikes().iterator().next();

							UserCategoryLikes userCategoryLikes = userDAO.getUserCategoryLikes(userId, userCategory.getUserCategoryId());

							//Get nearest weekend date for UserLike
							Date userLikeWeekend =  userCategoryLikes.getCreatedTime();
							try{
								if(userLikeWeekend.compareTo(Utils.getNearestFriday()) <= 0)
								{
									// User Event List
									List<Events> eventList = eventsDAO.getEventsByuserLikes(userCategory.getItemType().trim(),userCategory.getItemCategory().trim(),userCategory.getProviderName(),Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));

									// User deal list
									List<Deals> dealList = dealsDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName(),Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));

									// User business list
									List<Business> businessList = businessDAO.getBusinessByuserLikes(userCategory.getItemType().trim(),userCategory.getItemCategory().trim(),userCategory.getProviderName(), Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));


									for(User starFiftyMilesUser: getStarFiftyMilesUserList)
									{
										List<UserCategory> friendCategoryList = userDAO.getUserCategoryLikesByType(starFiftyMilesUser.getUserId(),category);
										logger.debug("friendCategoryList =>"+friendCategoryList);
										if(friendCategoryList!=null)
										{
											for(UserCategory friendCategory : friendCategoryList){
												logger.debug("friendCategory =>"+friendCategory.getItemType());
												//UserCategoryLikes friendCategoryLikes = (UserCategoryLikes)userCategory.getUserCategoryLikes().iterator().next();
												if(friendCategory.getItemType().trim().equalsIgnoreCase(userCategory.getItemType().trim()))
												{
													UserCategoryLikes friendCategoryLikes = userDAO.getUserCategoryLikes(starFiftyMilesUser.getUserId(), friendCategory.getUserCategoryId());

													Date friendLikeWeekend =  friendCategoryLikes.getCreatedTime();
													if(friendLikeWeekend.compareTo(Utils.getNearestFriday()) <= 0)
													{
														List<Events> friendEventList = eventsDAO.getEventsByuserLikes(friendCategory.getItemType().trim(),friendCategory.getItemCategory().trim(),friendCategory.getProviderName(), Double.parseDouble(starFiftyMilesUser.getLattitude()), Double.parseDouble(starFiftyMilesUser.getLongitude()));
														List<Deals> friendDealList =  dealsDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName(),Double.parseDouble(starFiftyMilesUser.getLattitude()), Double.parseDouble(starFiftyMilesUser.getLongitude()));
														List<Business> friendBusinessList = businessDAO.getBusinessByuserLikes(userCategory.getItemType().trim(),userCategory.getItemCategory().trim(),userCategory.getProviderName(), Double.parseDouble(starFiftyMilesUser.getLattitude()), Double.parseDouble(starFiftyMilesUser.getLongitude()));
														if(eventList!=null)
														{
															logger.debug("eventList size =>"+eventList.size());
															logger.debug("friendEventList size =>"+friendEventList.size());
															for(Events event:eventList)
															{

																logger.debug("eventList =>"+event);
																for(Events friendEvent: friendEventList)
																{
																	logger.debug("friendEvent =>"+friendEvent);
																	if(event.getEventId()==friendEvent.getEventId())
																	{
																		userFriendsCommonLikeEvents.add(event);
																	}
																}
															}
														}
														if(dealList!=null)
														{
															logger.debug("dealList size =>"+dealList.size());
															logger.debug("friendDealList size =>"+friendDealList.size());
															for(Deals deal:dealList)
															{
																logger.debug("deal =>"+deal);
																for(Deals friendDeal: friendDealList)
																{
																	logger.debug("friendDeal =>"+friendDeal);
																	if(deal.getId()==friendDeal.getId())
																	{
																		userFriendsCommonLikeDeals.add(deal);
																	}
																}
															}
														}
														if(businessList!=null)
														{
															logger.debug("friendBusinessList size =>"+friendBusinessList.size());
															for(Business business:businessList)
															{

																logger.debug("businessList =>"+business);
																for(Business friendBusiness: friendBusinessList)
																{
																	logger.debug("friendBusiness =>"+friendBusiness);
																	if(business.getId()==friendBusiness.getId())
																	{
																		userFriendsCommonLikeBusiness.add(business);
																	}
																}
															}
														}
													}
													break;
												}
											}
										}
									}
								}

							}
							catch(NumberFormatException e){

								e.printStackTrace();
							}
						}
					}

					List<Events> getBookedEventsList = eventsDAO.getBookedEvents(userId);
					List<Events> finalMatchingEventList = new ArrayList<Events>(userFriendsCommonLikeEvents);
					finalMatchingEventList.addAll(getBookedEventsList);

					logger.debug("finalMatchingEventList.size()==,userFriendsCommonLikeDeals.size()==,userFriendsCommonLikeBusiness.size()=="+finalMatchingEventList.size() +" : "+userFriendsCommonLikeDeals.size() +" : "+userFriendsCommonLikeBusiness.size());
					int a =finalMatchingEventList.size(),b=userFriendsCommonLikeDeals.size(),c=userFriendsCommonLikeBusiness.size();

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

					if(finalMatchingEventList!=null)
						saveTopEventMatchLists(user,category,finalMatchingEventList, true, n1);
					if(userFriendsCommonLikeDeals!=null)
						saveTopDealMatchLists(user, category, userFriendsCommonLikeDeals, true, n2);
					if(userFriendsCommonLikeBusiness!=null)
						saveTopNonDealMatchLists(user, category, userFriendsCommonLikeBusiness, true, n3);

					/*List<EventModel> getMatchingEventsModelList = new ArrayList<EventModel>();
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
				}for(int i = 0; i < categoryList.length; i++)
				{
					String category = categoryList[i];

				matchingEventsResponse.setMatchingevents(getMatchingEventsModelList);
				}*/
					//return matchingEventsResponse;
					//	}

				}
			}
		}



	}
	/**
	 * This saves the top Event Match List  into DataBase
	 * @param userId
	 * @param contactId
	 */
	private void saveTopEventMatchLists(User user, String category, List<Events> eventList, boolean forMatchList, int totalCount)
	{
		String idsStr = "";

		logger.debug("event List =>  "+eventList);

		if(eventList != null && !eventList.isEmpty())
		{
			for(int i = 0; i < eventList.size(); i++)
			{
				idsStr +="'"+eventList.get(i).getEventId()+"'";


				if(i < eventList.size() - 1)
				{
					idsStr+=",";
				}
			}

			List<Object[]> rows = new ArrayList<Object[]>();
			logger.debug("idsStr =>"+idsStr);
			// Getting Likes count for even suggestion in descending order
			if(!idsStr.equals("") && idsStr.length() > 0)
			{
				rows = userDAO.userEventSuggestionCount(idsStr,5);
			}


			logger.debug("EventStr: "+idsStr +" : "+rows);

			int likecount = 0;
			ArrayList<String> idsArray = new ArrayList<String>();
			int count = 1;
			for(int i = 0; i < rows.size(); i++)
			{
				if(count <= totalCount)
				{
					idsArray.clear();
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
								boolean isSaved = saveTopEventMatchList(eventList, user, Integer.parseInt(array.get(k)[0].toString()), Integer.parseInt(array.get(k)[1].toString()), category, forMatchList, count);
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
											boolean isSaved = saveTopEventMatchList(eventList, user, Integer.parseInt(idsArray.get(k)), likecount, category, forMatchList, count);
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

						boolean isSaved = saveTopEventMatchList(eventList, user, Integer.parseInt(idsArray.get(0)), likecount, category, forMatchList, count);
						if(isSaved)
							count++;
					}
				}
			}

		}
	}
	/**
	 * This saves the top deal Match List  into DataBase
	 * @param userId
	 * @param contactId
	 */
	private void saveTopDealMatchLists(User user, String category, List<Deals> dealList, boolean forMatchList, int totalCount)
	{
		String idsStr = "";

		logger.debug("deal list =>  "+dealList);

		if(dealList != null && !dealList.isEmpty())
		{
			for(int i = 0; i < dealList.size(); i++)
			{
				idsStr +="'"+dealList.get(i).getId()+"'";


				if(i < dealList.size() - 1)
				{
					idsStr+=",";
				}
			}

			List<Object[]> rows = new ArrayList<Object[]>();
			// Getting Likes count for events in descending order
			logger.debug("idsStr =>"+idsStr);
			if(!idsStr.equals(""))
			{
				rows = userDAO.userDealSuggestionCount(idsStr,5);
			}

			logger.debug("dealIdsStr: "+idsStr +" : "+rows);

			ArrayList<String> idsArray = new ArrayList<String>();

			int likecount = 0;
			if(rows != null)
			{
				logger.debug("rows size: "+rows.size());
				int count = 1;
				for(int i = 0; i < rows.size(); i++)
				{
					if(count <= totalCount)
					{
						idsArray.clear();
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
							ArrayList<String> dealsIds = new ArrayList<String>();
							List<Object[]> rows1 = dealsDAO.getTopDealsByRating(StringUtils.join(idsArray.toArray(new String[idsArray.size()]), ","));
							if(rows1 != null)
							{
								for(int k = 0; k < rows1.size(); k++)
								{
									if(count <= totalCount)
									{
										dealsIds.clear();
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
													boolean isSaved = saveTopDealMatchList(dealList, user, Integer.parseInt(array.get(j)[0].toString()), likecount, category, forMatchList, count);
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
																boolean isSaved = saveTopDealMatchList(dealList, user, Integer.parseInt(dealsIds.get(m).toString()), likecount, category, forMatchList, count);
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
											boolean isSaved = saveTopDealMatchList(dealList, user, Integer.parseInt(dealsIds.get(0)), likecount, category, forMatchList, count);
											if(isSaved)
												count++;
										}
									}
								}
							}
						}
						else
						{
							boolean isSaved = saveTopDealMatchList(dealList, user, Integer.parseInt(idsArray.get(0)), likecount, category, forMatchList, count);
							if(isSaved)
								count++;
						}
					}
				}

			}

		}
	}
	/**
	 * This saves the top non deal Match List  into DataBase
	 * @param userId
	 * @param contactId
	 */
	private void saveTopNonDealMatchLists(User user, String category, List<Business> businessList, boolean forMatchList, int totalCount)
	{
		String idsStr = "";

		logger.debug("business List =>  "+businessList);

		if(businessList != null && !businessList.isEmpty())
		{
			for(int i = 0; i < businessList.size(); i++)
			{
				idsStr +="'"+businessList.get(i).getId()+"'";


				if(i < businessList.size() - 1)
				{
					idsStr+=",";
				}
			}

			// Getting Likes count for events in descending order
			logger.debug("idsStr =>"+idsStr);
			List<Object[]> rows = businessDAO.getNonDealLikeCountByPage(idsStr);

			ArrayList<String> idsArray = new ArrayList<String>();

			int likecount = 0;
			int count = 1;
			if(rows != null)
			{
				logger.debug("rows size: "+rows.size());
				for(int i = 0; i < rows.size(); i++)
				{
					if(count <= totalCount)
					{
						idsArray.clear();
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
							List<Object[]> rows1 = businessDAO.getTopBusinessByRating(StringUtils.join(idsArray.toArray(new String[idsArray.size()]), ","));
							for(int k = 0; k < rows1.size(); k++)
							{
								if(count <= totalCount)
								{
									boolean isSaved = saveTopNonDealMatchList(businessList, user, Integer.parseInt(rows1.get(k)[0].toString()), likecount, category, forMatchList, count);
									if(isSaved)
										count++;
								}
							}
						}
						else
						{
							boolean isSaved = saveTopNonDealMatchList(businessList, user, Integer.parseInt(idsArray.get(0).toString()), likecount, category, forMatchList, count);
							if(isSaved)
								count++;
						}
					}
				}
			}
		}
	}

	private boolean saveTopEventMatchList(List<Events> eventList, User user, int eventId, int totalLikes, String category, boolean forMatchList, int count)
	{
		boolean isSaved = false;

		for(Events event : eventList)
		{
			logger.debug("saveTopEventMatchList =>"+event.getEventId() + " : "+eventId);
			if(event.getEventId() == eventId)
			{
				Topeventsuggestion topeventsuggestion = new Topeventsuggestion();
				topeventsuggestion.setCategoryType(category);
				Date date = new Date();
				topeventsuggestion.setCreatedTime(date);
				topeventsuggestion.setEvents(event);
				topeventsuggestion.setRank((long)count);
				if(forMatchList)
					topeventsuggestion.setSuggestionType("Match List");
				topeventsuggestion.setTotalLikes(totalLikes);
				topeventsuggestion.setUpdatedTime(date);
				topeventsuggestion.setUser(user);
				userDAO.saveTopEventSuggestions(topeventsuggestion);
				isSaved = true;
				break;
			}
		}

		return isSaved;
	}
	private boolean saveTopDealMatchList(List<Deals> dealList, User user, int dealId, int totalLikes, String category, boolean forMatchList, int count)
	{
		boolean isSaved = false;

		Deals dealDetail = dealsDAO.getDealById(dealId);
		for(Deals deal : dealList)
		{
			logger.debug("saveTopDealMatchList =>"+deal.getId() + " : "+dealId);
			if(deal.getId() == dealId)
			{
				/*//Checking if the suggestions is already exists or not
				List<Topdealssuggestion> topdealsuggestions = userDAO.isTopDealSuggestionExists(user.getUserId(), deal.getId());*/
				Topdealssuggestion topdealsuggestion = new Topdealssuggestion();
				topdealsuggestion.setCategoryType(category);
				Date date = new Date();
				topdealsuggestion.setCreatedTime(date);
				topdealsuggestion.setDeals(dealDetail);
				topdealsuggestion.setRank((long)count);
				if(forMatchList)
					topdealsuggestion.setSuggestionType("Match List");
				topdealsuggestion.setTotalLikes(totalLikes);
				topdealsuggestion.setUpdatedTime(date);
				topdealsuggestion.setUser(user);
				userDAO.saveTopDealSuggestions(topdealsuggestion);
				isSaved = true;
				break;
			}
			
		}

		return isSaved;
	}


	private boolean saveTopNonDealMatchList(List<Business> businessList, User user, int businessId, int totalLikes, String category, boolean forMatchList, int count)
	{
		boolean isSaved = false;

		Business businessDetail = businessDAO.getBusinessById(businessId);

		for(Business business : businessList)
		{
			logger.debug("saveTopNonDealMatchList =>"+business.getId() + " : "+businessId);
			if(business.getId() == businessId)
			{

				Topnondealsuggestion topnondealsuggestion =  new Topnondealsuggestion();

				topnondealsuggestion.setCategoryType(category);
				Date date = new Date();
				topnondealsuggestion.setCreatedTime(date);
				topnondealsuggestion.setBusiness(businessDetail);
				topnondealsuggestion.setRank((long)count);
				if(forMatchList)
					topnondealsuggestion.setSuggestionType("Match List");
				topnondealsuggestion.setTotalLikes(totalLikes);
				topnondealsuggestion.setUpdatedTime(date);
				topnondealsuggestion.setUser(user);
				userDAO.saveTopNonDealSuggestions(topnondealsuggestion);
				isSaved = true;
				break;
			}
		}

		return isSaved;
	}

}