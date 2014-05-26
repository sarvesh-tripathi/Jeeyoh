package com.jeeyoh.service.wallfeed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.funboard.FunBoardModel;
import com.jeeyoh.model.funboard.SaveShareWallRequest;
import com.jeeyoh.model.funboard.WallFeedModel;
import com.jeeyoh.model.funboard.WallFeedRequest;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.WallFeedResponse;
import com.jeeyoh.model.search.SuggestionModel;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IFunBoardDAO;
import com.jeeyoh.persistence.IGroupDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.IWallFeedDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Funboard;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.persistence.domain.WallFeed;
import com.jeeyoh.persistence.domain.WallFeedComments;
import com.jeeyoh.persistence.domain.WallFeedItems;
import com.jeeyoh.persistence.domain.WallFeedUserShareMap;
import com.jeeyoh.utils.Utils;

@Component("wallFeedSharingService")
public class WallFeedSharingService implements IWallFeedSharingService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Value("${host.path}")
	private String hostPath;

	@Autowired
	private IWallFeedDAO wallFeedDAO;
	@Autowired
	private IFunBoardDAO funBoardDAO;
	@Autowired
	private IUserDAO userDAO;
	@Autowired
	private IBusinessDAO businessDAO;
	@Autowired
	private IEventsDAO eventsDAO;
	@Autowired
	private IDealsDAO dealsDAO;
	@Autowired
	private IGroupDAO groupDAO;

	@Override
	@Transactional
	public BaseResponse saveWallFeedSharingData(WallFeedRequest wallFeedModel){

		logger.debug("saveWallFeedSharingData =>");
		BaseResponse response = new BaseResponse();
		WallFeed wallFeed = new WallFeed();
		Set<WallFeedItems> wallFeedItemsSet = new HashSet<WallFeedItems>();
		Set<WallFeedUserShareMap> wallFeedUSerShareMapSet = new HashSet<WallFeedUserShareMap>();
		if(wallFeedModel.getUserId()!=0 && wallFeedModel.getSharedfunBoardItemsList()!=null && wallFeedModel.getSharedWithUserList()!=null)
		{
			User user = (User)userDAO.getUserById(wallFeedModel.getUserId());
			UserModel userModel = new UserModel();
			Date date = new Date();
			userModel.setUserId(user.getUserId());
			logger.debug("user =>"+user.getFirstName());
			wallFeed.setUser(user);
			wallFeed.setCreatedTime(date);
			wallFeed.setUpdatedTime(date);
			for(FunBoardModel funBoardList:wallFeedModel.getSharedfunBoardItemsList())
			{
				WallFeedItems wallFeedItems = new WallFeedItems();
				Funboard funboard = funBoardDAO.getFunboardById(funBoardList.getFunBoardId());
				wallFeedItems.setCategory(funboard.getCategory());
				wallFeedItems.setFunboard(funboard);
				wallFeedItems.setItemId(funboard.getItemId());
				wallFeedItems.setItemType(funboard.getItemType());
				wallFeedItems.setWallFeed(wallFeed);
				wallFeedItemsSet.add(wallFeedItems);
			}
			List<UserModel> sharedUSerList = wallFeedModel.getSharedWithUserList();
			sharedUSerList.add(userModel);
			for(UserModel userModelList: sharedUSerList)
			{
				WallFeedUserShareMap wallFeedUSerShareMap = new WallFeedUserShareMap();
				User shareWithUser = (User)userDAO.getUserById(userModelList.getUserId());
				wallFeedUSerShareMap.setUser(user);
				wallFeedUSerShareMap.setShareWithUser(shareWithUser);
				wallFeedUSerShareMap.setCreatedTime(date);
				wallFeedUSerShareMap.setUpdatedTime(date);
				wallFeedUSerShareMap.setWallFeed(wallFeed);
				wallFeedUSerShareMapSet.add(wallFeedUSerShareMap);
			}
			wallFeed.setWallFeedItems(wallFeedItemsSet);
			wallFeed.setWallFeedUserShareMap(wallFeedUSerShareMapSet);
			wallFeedDAO.saveWallFeed(wallFeed);
			response.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			response.setStatus(ServiceAPIStatus.FAILED.getStatus());
			response.setError("Invalid Parameter");
		}
		return response;
	}

	@Transactional
	@Override
	public CommentResponse saveWallFeedComments(CommentModel commentModel) {

		Date date = null;
		User user = userDAO.getUserById(commentModel.getUserId());
		WallFeed wallFeed = wallFeedDAO.getWallFeedDetailsByID(commentModel.getItemId());
		logger.debug("saveWallFeedComments ==> user => "+user+"wallFeed => "+wallFeed.getWallFeedId());
		boolean isSaved = false;
		try {
			if(wallFeed!=null && user!=null)
			{
				WallFeedComments wallFeedComments = new WallFeedComments();
				wallFeedComments.setComment(commentModel.getComment());
				date = new Date();
				wallFeedComments.setCreatedTime(date);
				wallFeedComments.setUpdatedTime(date);
				wallFeedComments.setWallFeed(wallFeed);
				wallFeedComments.setUser(user);
				wallFeedDAO.saveWallFeedComments(wallFeedComments);
			}
			isSaved = true;
		} catch (Exception e) {
			isSaved = false;
		}
		CommentResponse response = new CommentResponse();
		if(isSaved)
		{
			commentModel.setCreatedTime(Utils.getTime(date));
			commentModel.setUserName(user.getFirstName());
			if(user.getImageUrl() != null)
				commentModel.setImageUrl(hostPath + user.getImageUrl());
			response.setComment(commentModel);
			response.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			response.setStatus(ServiceAPIStatus.FAILED.getStatus());
			response.setError("Error");
		}
		return response;
	}

	@Override
	@Transactional
	public WallFeedResponse getWallFeedComments(int userId, int wallFeedId)
	{
		logger.debug("getWallFeedComments ==> userId => "+userId+"wallFeedId => "+wallFeedId);
		WallFeedResponse response = new WallFeedResponse();
		if(wallFeedId!=0)
		{
			List<WallFeedComments> wallFeedCommentsList = wallFeedDAO.getWallFeedCommentsById(wallFeedId);
			List<CommentModel> commentModelList = new ArrayList<CommentModel>();
			for(WallFeedComments wallFeedComment:wallFeedCommentsList)
			{
				User user = wallFeedComment.getUser();
				CommentModel commentModel = new CommentModel();
				commentModel.setComment(wallFeedComment.getComment());
				commentModel.setCreatedTime(Utils.getTime(wallFeedComment.getCreatedTime()));
				commentModel.setItemId(wallFeedComment.getWallFeed().getWallFeedId());
				commentModel.setUserId(user.getUserId());
				if(user.getImageUrl() != null)
					commentModel.setImageUrl(hostPath + user.getImageUrl());
				commentModel.setUserName(user.getFirstName()+" "+user.getLastName());
				commentModelList.add(commentModel);
			}
			logger.debug("add community comments in response");
			response.setWallFeedComments(commentModelList);
		}

		return response;

	}


	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public WallFeedResponse getUserWallFeed(int userId) {
		List<WallFeed> wallFeeds = wallFeedDAO.getUserWallFeed(userId);
		WallFeedResponse feedResponse = new WallFeedResponse();
		List<WallFeedModel> feedModelList = new ArrayList<WallFeedModel>();
		if(wallFeeds != null && !wallFeeds.isEmpty())
		{
			for(WallFeed wallFeed:wallFeeds)
			{
				WallFeedModel feedModel = new WallFeedModel();
				feedModel.setWallFeedId(wallFeed.getWallFeedId());
				Set<WallFeedItems> items = wallFeed.getWallFeedItems();
				List<SuggestionModel> itemsList = new ArrayList<SuggestionModel>();
				for(WallFeedItems item:items)
				{
					SuggestionModel suggestionModel = new SuggestionModel();
					String itemType = item.getItemType();
					if(itemType.equalsIgnoreCase("deal"))
					{
						Deals deals = dealsDAO.getDealById(item.getItemId());
						suggestionModel.setImageUrl(deals.getLargeImageUrl());
						suggestionModel.setCategoryType(itemType);
						suggestionModel.setItemId(item.getItemId());
					}
					else if(itemType.equalsIgnoreCase("event"))
					{
						Events events = eventsDAO.getEventById(item.getItemId());
						suggestionModel.setCategoryType(itemType);
						suggestionModel.setItemId(item.getItemId());

					}
					else
					{
						Business business = businessDAO.getBusinessById(item.getItemId());
						suggestionModel.setImageUrl(business.getImageUrl());
						suggestionModel.setCategoryType(itemType);
						suggestionModel.setItemId(item.getItemId());						
					}
					itemsList.add(suggestionModel);

				}

				Set<WallFeedComments> comments = wallFeed.getWallFeedComments();
				List<CommentModel> commentsList = new ArrayList<CommentModel>();
				for(WallFeedComments comment:comments)
				{
					User user = comment.getUser();
					CommentModel commentModel = new CommentModel();
					commentModel.setComment(comment.getComment());
					commentModel.setUserId(user.getUserId());
					commentModel.setUserName(user.getFirstName());
					if(user.getImageUrl() != null)
						commentModel.setImageUrl(hostPath + user.getImageUrl());
					commentsList.add(commentModel);
				}
				feedModel.setItems(itemsList);
				feedModel.setComments(commentsList);
				feedModelList.add(feedModel);

			}

		}
		feedResponse.setWallFeedModel(feedModelList);
		feedResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return feedResponse;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public BaseResponse saveShareWallFeedRecord(SaveShareWallRequest request) {
		BaseResponse baseResponse = new BaseResponse();
		logger.debug("wall feed id ::"+request.getWallFeedId());
		WallFeed wallFeed = wallFeedDAO.getWallFeedDetailsByID(request.getWallFeedId());
		logger.debug("wall feed id ::"+request.getUserId());
		User users  = userDAO.getUserById(request.getUserId());
		List<Integer> friends = request.getFriends();
		Date date = new Date();
		if(friends != null)
		{
			for(Integer userId:friends)
			{
				logger.debug("User share with id :::"+userId);
				WallFeedUserShareMap wallFeedUserShareMap = wallFeedDAO.isWallFeedAlreadyShared(users.getUserId(), wallFeed.getWallFeedId(), userId);
				logger.debug("wallFeedUserShareMap :::"+wallFeedUserShareMap);
				if(wallFeedUserShareMap == null)
				{
					wallFeedUserShareMap = new WallFeedUserShareMap();
					wallFeedUserShareMap.setWallFeed(wallFeed);
					wallFeedUserShareMap.setCreatedTime(date);
					wallFeedUserShareMap.setUpdatedTime(date);
					wallFeedUserShareMap.setUser(users);
					User users1   = userDAO.getUserById(userId);
					wallFeedUserShareMap.setShareWithUser(users1);
					wallFeedDAO.saveWallFeedShareMap(wallFeedUserShareMap);
				}
			}
		}

		List<Integer> groups = request.getGroups();
		if(groups != null)
		{
			for(Integer groupId:groups)
			{
				Jeeyohgroup jeeyohGroup = groupDAO.getGroupByGroupId(groupId);
				Set<Groupusermap> groups2   = jeeyohGroup.getGroupusermaps();
				for (Groupusermap groups1 : groups2)
				{
					User groupMember = groups1.getUser();
					if(groupMember.getUserId() != request.getUserId())
					{
						WallFeedUserShareMap wallFeedUserShareMap = wallFeedDAO.isWallFeedAlreadyShared(users.getUserId(), wallFeed.getWallFeedId(), groupMember.getUserId());
						logger.debug("wallFeedUserShareMap for group :::"+wallFeedUserShareMap);
						if(wallFeedUserShareMap == null)
						{
							wallFeedUserShareMap = new WallFeedUserShareMap();
							wallFeedUserShareMap.setWallFeed(wallFeed);
							wallFeedUserShareMap.setCreatedTime(date);
							wallFeedUserShareMap.setUpdatedTime(date);
							wallFeedUserShareMap.setUser(users);
							User shareWithUser  = userDAO.getUserById(groupMember.getUserId());
							wallFeedUserShareMap.setShareWithUser(shareWithUser);
							wallFeedDAO.saveWallFeedShareMap(wallFeedUserShareMap);
						}
					}
				}
			}
		}

		baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return baseResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public BaseResponse addWallFeedSuggestions(SaveShareWallRequest request) 
	{
		logger.debug("addWallFeedSuggestions => addSuggestions");
		BaseResponse response = new BaseResponse();
		logger.debug("userId"+request.getUserId());
		User user = userDAO.getUserById(request.getUserId());
		List<Integer> friendsIdList = request.getFriends();
		List<Integer> groups = request.getGroups();
		Date suggestionDate = null;
		Date date = new Date();
		logger.debug("wall feed id"+request.getWallFeedId());
		WallFeed wallFeed = wallFeedDAO.getWallFeedDetailsByID(request.getWallFeedId());
		if(wallFeed.getUser().getUserId() == user.getUserId())
		{
			suggestionDate = Utils.getNearestWeekend(date);
		}
		else
		{
			SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			try {

				suggestionDate = simple.parse(request.getSheduleDate());

			}catch(Exception e)
			{

			}
		}
		if(request != null)
		{
			List<SuggestionModel> suggestionsItems = request.getItems();
			//String suggestionType =
			int batch_size = 0;
			for(SuggestionModel suggestion:suggestionsItems)
			{
				String suggestionType = suggestion.getCategoryType();
				Integer suggetstionId = suggestion.getItemId();

				if(suggestionType.equalsIgnoreCase("event"))
				{
					logger.debug("event => "+suggetstionId);
					Events event = eventsDAO.getEventById(suggetstionId);
					logger.debug("event desc => "+event.getDescription());
					if(friendsIdList != null)
					{
						for(Integer friendId:friendsIdList)
						{
							User users1 = userDAO.getUserById(friendId);
							Usereventsuggestion userEventSuggestion = userDAO.isEventSuggestionExistsForDirectSuggestion(friendId, event.getEventId(), user.getUserId());
							logger.debug("userEventSuggestion:::  "+userEventSuggestion);
							if(userEventSuggestion != null)
							{
								if(userEventSuggestion.getSuggestedTime().compareTo(suggestionDate) < 0)
								{
									userEventSuggestion.setUpdatedTime(date);
									userEventSuggestion.setSuggestedTime(suggestionDate);
									userEventSuggestion.setSuggestionType("Wall Feed Suggestion");
									userDAO.updateUserEventSuggestion(userEventSuggestion);
								}
								response.setStatus("Ok");
							}
							else
							{
								userEventSuggestion = new Usereventsuggestion();
								userEventSuggestion.setEvents(event);
								userEventSuggestion.setCreatedTime(date);
								userEventSuggestion.setSuggestionType("Wall Feed Suggestion");
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
										userEventSuggestion.setSuggestionType("Wall Feed Suggestion");
										userDAO.updateUserEventSuggestion(userEventSuggestion);
									}
									response.setStatus("Ok");
								}
								else
								{
									userEventSuggestion = new Usereventsuggestion();
									userEventSuggestion.setEvents(event);
									userEventSuggestion.setCreatedTime(date);
									userEventSuggestion.setSuggestionType("Wall Feed Suggestion");
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
					logger.debug("deal => "+suggetstionId);
					Deals deal = dealsDAO.getDealById(suggetstionId);
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
									userDealSuggestion.setSuggestionType("Wall Feed Suggestion");
									userDAO.updateUserDealSuggestion(userDealSuggestion);
								}
								response.setStatus("Ok");
							}
							else
							{
								User users1 = userDAO.getUserById(friendId);
								userDealSuggestion = new Userdealssuggestion();
								userDealSuggestion.setDeals(deal);
								userDealSuggestion.setCreatedtime(date);
								userDealSuggestion.setSuggestionType("Wall Feed Suggestion");
								userDealSuggestion.setUpdatedtime(date);
								userDealSuggestion.setUser(users1);
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
										userDealSuggestion.setSuggestionType("Wall Feed Suggestion");
										userDAO.updateUserDealSuggestion(userDealSuggestion);
									}
									response.setStatus("Ok");
								}
								else
								{
									userDealSuggestion = new Userdealssuggestion();
									userDealSuggestion.setDeals(deal);
									userDealSuggestion.setCreatedtime(date);
									userDealSuggestion.setSuggestionType("Wall Feed Suggestion");
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
					logger.debug("business => "+suggetstionId);
					Business business = businessDAO.getBusinessById(suggetstionId);
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
									userNonDealSuggestion.setSuggestionType("Wall Feed Suggestion");
									userDAO.updateUserNonDealSuggestion(userNonDealSuggestion);
								}
								response.setStatus("Ok");
							}
							else
							{
								User users1 = userDAO.getUserById(friendId);
								userNonDealSuggestion = new Usernondealsuggestion();
								userNonDealSuggestion.setBusiness(business);
								userNonDealSuggestion.setCreatedtime(date);
								userNonDealSuggestion.setSuggestionType("Wall Feed Suggestion");
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
										userNonDealSuggestion.setSuggestionType("Wall Feed Suggestion");
										userDAO.updateUserNonDealSuggestion(userNonDealSuggestion);
									}
									response.setStatus("Ok");
								}
								else
								{
									userNonDealSuggestion = new Usernondealsuggestion();
									userNonDealSuggestion.setBusiness(business);
									userNonDealSuggestion.setCreatedtime(date);
									userNonDealSuggestion.setSuggestionType("Wall Feed Suggestion");
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
			}
		}
		logger.debug("done");
		return response;
	}

}
