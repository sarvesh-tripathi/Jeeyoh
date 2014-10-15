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
import com.jeeyoh.persistence.domain.Pageuserlikes;
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
		boolean isExist = false;
		logger.debug("AddEventSuggestionService => addSuggestions");
		BaseResponse response = new BaseResponse();
		User user = userDAO.getUserById(userId);

		Date currentDate = Utils.getCurrentDate();
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
			boolean isSaved = false;
			Events event = eventsDAO.getEventById(suggestionId);
			if(friendsIdList != null)
			{
				for(Integer friendId:friendsIdList)
				{
					isSaved = saveEventDirectSuggestion(user, friendId, event.getEventId(), date, suggestionDate, batch_size, currentDate);
					if(isSaved)
						batch_size++;
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
						if(users1.getUserId() != user.getUserId())
						{
							isSaved = saveEventDirectSuggestion(user, users1.getUserId(), event.getEventId(), date, suggestionDate, batch_size, currentDate);
							if(isSaved)
								batch_size++;
						}
					}
				}
			}
			response.setStatus("Ok");
		}
		else if (suggestionType.equalsIgnoreCase("deal"))
		{
			Deals deal = dealsDAO.getDealById(suggestionId);
			if(friendsIdList != null)
			{
				for(Integer friendId:friendsIdList)
				{
					isExist = false;
					Userdealssuggestion userDealSuggestion = null;
					List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(friendId, deal.getId());
					if(userdealsuggestions != null && userdealsuggestions.size() != 0)
					{
						userDealSuggestion = userdealsuggestions.get(0);
						isExist = true;
					}
					else
					{
						userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(friendId, deal.getId(), user.getUserId());
						if(userDealSuggestion != null)
						{
							if(userDealSuggestion.getSuggestedTime() != null && userDealSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
							{
								isExist = true;
							}
						}
					}

					if(isExist)
					{
						userDealSuggestion.setUpdatedtime(date);
						userDealSuggestion.setSuggestedTime(suggestionDate);
						userDealSuggestion.setUserContact(user);
						userDealSuggestion.setSuggestionType("Direct Suggestion");
						userDAO.updateUserDealSuggestion(userDealSuggestion);
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
						
					}
				}
				response.setStatus("Ok");
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
						if(users1.getUserId() != user.getUserId())
						{
							isExist = false;
							Userdealssuggestion userDealSuggestion = null;
							List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(users1.getUserId(), deal.getId());
							if(userdealsuggestions != null && userdealsuggestions.size() != 0)
							{
								userDealSuggestion = userdealsuggestions.get(0);
								isExist = true;
							}
							else
							{
								userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(users1.getUserId(), deal.getId(), user.getUserId());
								if(userDealSuggestion != null)
								{
									if(userDealSuggestion.getSuggestedTime() != null && userDealSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
									{
										isExist = true;
									}
								}
							}

							if(isExist)
							{
								userDealSuggestion.setUpdatedtime(date);
								userDealSuggestion.setSuggestedTime(suggestionDate);
								userDealSuggestion.setUserContact(user);
								userDealSuggestion.setSuggestionType("Direct Suggestion");
								userDAO.updateUserDealSuggestion(userDealSuggestion);
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
								batch_size++;
							}
						}
					}
				}
				response.setStatus("Ok");
			}
		}
		else if (suggestionType.equalsIgnoreCase("business"))
		{
			Business business = businessDAO.getBusinessById(suggestionId);
			if(friendsIdList != null)
			{
				for(Integer friendId:friendsIdList)
				{
					isExist = false;
					Usernondealsuggestion userNonDealSuggestion = null;
					List<Usernondealsuggestion> usernondealsuggestions = userDAO.isNonDealSuggestionExists(friendId, business.getId());
					if(usernondealsuggestions != null && usernondealsuggestions.size() != 0)
					{
						userNonDealSuggestion = usernondealsuggestions.get(0);
						isExist = true;
					}
					else
					{
						userNonDealSuggestion = userDAO.isNonDealSuggestionExistsForDirectSuggestion(friendId, business.getId(), user.getUserId());
						if(userNonDealSuggestion != null)
						{
							if(userNonDealSuggestion.getSuggestedTime() != null && userNonDealSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
							{
								isExist = true;
							}
						}
					}
					if(isExist)
					{

						userNonDealSuggestion.setUpdatedtime(date);
						userNonDealSuggestion.setSuggestedTime(suggestionDate);
						userNonDealSuggestion.setUserContact(user);
						userNonDealSuggestion.setSuggestionType("Direct Suggestion");
						userDAO.updateUserNonDealSuggestion(userNonDealSuggestion);

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
					}
				}
				response.setStatus("Ok");
			}
			if(groups != null)
			{
				for(Integer groupId:groups)
				{
					Jeeyohgroup jeeyohGroup = groupDAO.getGroupByGroupId(groupId);
					Set<Groupusermap> groups2   = jeeyohGroup.getGroupusermaps();
					for (Groupusermap groups1 : groups2)
					{
						isExist = false;
						User users1 = groups1.getUser();
						if(users1.getUserId() != user.getUserId())
						{
							Usernondealsuggestion userNonDealSuggestion = null;
							List<Usernondealsuggestion> usernondealsuggestions = userDAO.isNonDealSuggestionExists(users1.getUserId(), business.getId());
							if(usernondealsuggestions != null && usernondealsuggestions.size() != 0)
							{
								userNonDealSuggestion = usernondealsuggestions.get(0);
								isExist = true;
							}
							else
							{
								userNonDealSuggestion = userDAO.isNonDealSuggestionExistsForDirectSuggestion(users1.getUserId(), business.getId(), user.getUserId());
								if(userNonDealSuggestion != null)
								{
									if(userNonDealSuggestion.getSuggestedTime() != null && userNonDealSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
									{
										isExist = true;
									}
								}
							}

							if(isExist)
							{
								userNonDealSuggestion.setUpdatedtime(date);
								userNonDealSuggestion.setSuggestedTime(suggestionDate);
								userNonDealSuggestion.setSuggestionType("Direct Suggestion");
								userNonDealSuggestion.setUserContact(user);
								userDAO.updateUserNonDealSuggestion(userNonDealSuggestion);
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
							}

						}
					}
				}
				response.setStatus("Ok");
			}
		}
		else if(suggestionType.equalsIgnoreCase("community"))
		{
			boolean isSaved = false;
			int eventId = eventsDAO.getRecentEvent(suggestionId);
			List<Integer> eventList = eventsDAO.getEventsByPgaeId(suggestionId);
			if(friendsIdList != null)
			{
				for(Integer friendId:friendsIdList)
				{
					Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(friendId, suggestionId);
					if(pageuserlikes != null)
					{
						if(pageuserlikes.getIsFollowing())
						{
							if(eventList != null)
							{
								for(Integer event : eventList)
								{
									isSaved = saveEventDirectSuggestion(user, friendId, event, date, suggestionDate, batch_size, currentDate);
									if(isSaved)
										batch_size++;
								}
							}
						}
						else
						{
							if(eventId != 0)
							{
								isSaved = saveEventDirectSuggestion(user, friendId, eventId, date, suggestionDate, batch_size, currentDate);
								if(isSaved)
									batch_size++;
							}
						}
					}
					else
					{
						if(eventId != 0)
						{
							isSaved = saveEventDirectSuggestion(user, friendId, eventId, date, suggestionDate, batch_size, currentDate);
							if(isSaved)
								batch_size++;
						}
					}

				}
				response.setStatus("Ok");
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
						if(users1.getUserId() != user.getUserId())
						{
							Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(users1.getUserId(), suggestionId);
							if(pageuserlikes != null)
							{
								if(pageuserlikes.getIsFollowing())
								{
									if(eventList != null)
									{
										for(Integer event : eventList)
										{
											isSaved = saveEventDirectSuggestion(user, users1.getUserId(), event, date, suggestionDate, batch_size, currentDate);
											if(isSaved)
												batch_size++;
										}
									}
								}
								else
								{
									if(eventId != 0)
									{
										isSaved = saveEventDirectSuggestion(user, users1.getUserId(), eventId, date, suggestionDate, batch_size, currentDate);
										if(isSaved)
											batch_size++;
									}
								}
							}
							else
							{
								if(eventId != 0)
								{
									isSaved = saveEventDirectSuggestion(user, users1.getUserId(), eventId, date, suggestionDate, batch_size, currentDate);
									if(isSaved)
										batch_size++;
								}
							}
						}
					}
				}
			}
			response.setStatus("Ok");
		}
		else
		{
			logger.debug("Incorrect suggestion type");
			response.setError("Incorrect suggestion type");
		}
		logger.debug("done");
		return response;
	}


	private boolean saveEventDirectSuggestion(User user, int friendId,int eventId, Date date, Date suggestionDate, int batch_size, Date currentDate)
	{
		boolean isExist = false;
		Usereventsuggestion userEventSuggestion = null;
		List<Usereventsuggestion> userventsuggestions = userDAO.isEventSuggestionExists(friendId, eventId);
		if(userventsuggestions != null && userventsuggestions.size() != 0)
		{
			userEventSuggestion = userventsuggestions.get(0);
			isExist = true;
		}
		else
		{
			userEventSuggestion = userDAO.isEventSuggestionExistsForDirectSuggestion(friendId, eventId, user.getUserId());
			if(userEventSuggestion != null)
			{
				if(userEventSuggestion.getSuggestedTime() != null && userEventSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
				{
					isExist = true;
				}
			}
		}
		if(isExist)
		{
			userEventSuggestion.setUpdatedTime(date);
			userEventSuggestion.setSuggestedTime(suggestionDate);
			userEventSuggestion.setSuggestionType("Direct Suggestion");
			userEventSuggestion.setUserContact(user);
			userDAO.updateUserEventSuggestion(userEventSuggestion);
			return false;
		}
		else
		{
			userEventSuggestion = new Usereventsuggestion();

			Events events = new Events();
			events.setEventId(eventId);

			User users1 = new User();
			users1.setUserId(friendId);

			userEventSuggestion.setEvents(events);
			userEventSuggestion.setCreatedTime(date);
			userEventSuggestion.setSuggestionType("Direct Suggestion");
			userEventSuggestion.setUpdatedTime(date);
			userEventSuggestion.setUser(users1);
			userEventSuggestion.setUserContact(user);
			userEventSuggestion.setSuggestedTime(suggestionDate);
			userDAO.saveEventsSuggestions(userEventSuggestion, batch_size);
			return true;
		}
	}

}
