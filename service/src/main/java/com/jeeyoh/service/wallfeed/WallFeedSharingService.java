package com.jeeyoh.service.wallfeed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
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
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
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

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public BaseResponse saveWallFeedSharingData(WallFeedRequest wallFeedModel){

		logger.debug("saveWallFeedSharingData =>");
		BaseResponse response = new BaseResponse();
		WallFeed wallFeed = new WallFeed();
		Set<WallFeedItems> wallFeedItemsSet = new HashSet<WallFeedItems>();
		Set<WallFeedUserShareMap> wallFeedUSerShareMapSet = new HashSet<WallFeedUserShareMap>();
		if(wallFeedModel.getUserId()!=0 && wallFeedModel.getSharedfunBoardItemsList()!=null)
		{
			User user = (User)userDAO.getUserById(wallFeedModel.getUserId());
			UserModel userModel = new UserModel();
			Date date = new Date();
			userModel.setUserId(user.getUserId());
			logger.debug("user =>"+user.getFirstName());
			wallFeed.setUser(user);
			wallFeed.setCreatedTime(date);
			wallFeed.setUpdatedTime(date);
			wallFeed.setTag(wallFeedModel.getTag());
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

			List<Integer> friends = wallFeedModel.getFriends();
			if(friends != null)
			{
				for(Integer userId:friends)
				{
					logger.debug("User share with id :::"+userId);
					WallFeedUserShareMap wallFeedUserShareMap = new WallFeedUserShareMap();
					wallFeedUserShareMap.setWallFeed(wallFeed);
					wallFeedUserShareMap.setCreatedTime(date);
					wallFeedUserShareMap.setUpdatedTime(date);
					wallFeedUserShareMap.setUser(user);
					User users1   = userDAO.getUserById(userId);
					wallFeedUserShareMap.setShareWithUser(users1);
					logger.debug("in friends shareWithUser Id: "+users1.getUserId() + "shareWithUser: "+users1.hashCode() + " user : "+user.hashCode() + " wallFeed : " + wallFeed.hashCode());

					wallFeedUSerShareMapSet.add(wallFeedUserShareMap);
				}
			}

			List<Integer> groups = wallFeedModel.getGroups();
			if(groups != null)
			{
				for(Integer groupId:groups)
				{
					Jeeyohgroup jeeyohGroup = groupDAO.getGroupByGroupId(groupId);
					Set<Groupusermap> groups2   = jeeyohGroup.getGroupusermaps();
					for (Groupusermap groups1 : groups2)
					{
						User groupMember = groups1.getUser();
						if(groupMember.getUserId() != user.getUserId())
						{
							WallFeedUserShareMap wallFeedUserShareMap = new WallFeedUserShareMap();
							wallFeedUserShareMap.setWallFeed(wallFeed);
							wallFeedUserShareMap.setCreatedTime(date);
							wallFeedUserShareMap.setUpdatedTime(date);
							wallFeedUserShareMap.setUser(user);
							User shareWithUser  = userDAO.getUserById(groupMember.getUserId());
							wallFeedUserShareMap.setShareWithUser(shareWithUser);
							logger.debug("shareWithUser Id: "+shareWithUser.getUserId() + "shareWithUser: "+shareWithUser.hashCode() + " user : "+user.hashCode() + " wallFeed : " + wallFeed.hashCode());
							wallFeedUSerShareMapSet.add(wallFeedUserShareMap);
						}
					}
				}
			}
			for(WallFeedUserShareMap wallFeedUserShareMap : wallFeedUSerShareMapSet)
			{
				logger.debug("shareWithUser Id: "+wallFeedUserShareMap.getShareWithUser().getUserId() + "shareWithUser: "+wallFeedUserShareMap.getShareWithUser().hashCode() + " user : "+wallFeedUserShareMap.getUser().hashCode() + " wallFeed : " + wallFeedUserShareMap.getWallFeed().hashCode());

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
				commentModel.setEmailId(user.getEmailId());
				if(user.getImageUrl() != null)
					commentModel.setImageUrl(hostPath + user.getImageUrl());
				commentModel.setUserName(user.getFirstName()+" "+user.getLastName());
				commentModelList.add(commentModel);
			}
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
			logger.debug("wallFeed size=> " + wallFeeds.size());
			for(WallFeed wallFeed:wallFeeds)
			{
				WallFeedModel feedModel = new WallFeedModel();
				feedModel.setWallFeedId(wallFeed.getWallFeedId());
				feedModel.setTag(wallFeed.getTag());

				// Set wallFeed creator
				User creatorUser = wallFeed.getUser();
				UserModel userModel = new UserModel();
				userModel.setFirstName(creatorUser.getFirstName());
				userModel.setLastName(creatorUser.getLastName());
				if(creatorUser.getImageUrl() != null)
					userModel.setImageUrl(hostPath + creatorUser.getImageUrl());
				userModel.setUserId(creatorUser.getUserId());
				feedModel.setCreatorUser(userModel);

				Set<WallFeedItems> items = wallFeed.getWallFeedItems();
				List<SuggestionModel> itemsList = new ArrayList<SuggestionModel>();
				for(WallFeedItems item:items)
				{
					SuggestionModel suggestionModel =null;
					String itemType = item.getItemType();
					if(itemType.equalsIgnoreCase("deal"))
					{
						Deals deals = dealsDAO.getDealById(item.getItemId());
						if(deals != null)
						{
							suggestionModel = new SuggestionModel();
							suggestionModel.setImageUrl(deals.getLargeImageUrl());
							suggestionModel.setTitle(deals.getTitle());
							suggestionModel.setSuggestionType(itemType);
							suggestionModel.setItemId(item.getItemId());
						}		
					}
					else if(itemType.equalsIgnoreCase("event"))
					{
						Events events = eventsDAO.getEventById(item.getItemId());
						if(events != null)
						{
							suggestionModel = new SuggestionModel();
							suggestionModel.setTitle(events.getTitle());
							suggestionModel.setSuggestionType(itemType);
							suggestionModel.setItemId(item.getItemId());
						}
					}
					else if(itemType.equalsIgnoreCase("business"))
					{
						Business business = businessDAO.getBusinessById(item.getItemId());
						if(business != null)
						{
							suggestionModel = new SuggestionModel();
							suggestionModel.setTitle(business.getName());
							suggestionModel.setImageUrl(business.getImageUrl());
							suggestionModel.setSuggestionType(itemType);
							suggestionModel.setItemId(item.getItemId());	
						}					
					}
					else if(itemType.equalsIgnoreCase("community"))
					{
						Page page = eventsDAO.getPageDetailsByID(item.getItemId());
						if(page != null)
						{
							suggestionModel = new SuggestionModel();
							suggestionModel.setTitle(page.getAbout());
							suggestionModel.setImageUrl(page.getProfilePicture());
							suggestionModel.setSuggestionType(itemType);
							suggestionModel.setItemId(item.getItemId());		
						}
					}
					if(suggestionModel != null)
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
					commentModel.setEmailId(user.getEmailId());
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
		boolean isSaved = false;
		logger.debug("addWallFeedSuggestions => addSuggestions");
		BaseResponse response = new BaseResponse();
		logger.debug("userId"+request.getUserId());
		User user = userDAO.getUserById(request.getUserId());
		List<Integer> friendsIdList = request.getFriends();
		List<Integer> groups = request.getGroups();
		Date currentDate = Utils.getCurrentDate();
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
				String suggestionType = suggestion.getSuggestionType();
				Integer suggestionId = suggestion.getItemId();

				if(suggestionType.equalsIgnoreCase("event"))
				{

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
							Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(friendId, deal.getId(), user.getUserId());
							if(userDealSuggestion != null)
							{
								if(userDealSuggestion.getSuggestedTime() == null || userDealSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
								{
									userDealSuggestion.setUpdatedtime(date);
									userDealSuggestion.setSuggestedTime(suggestionDate);
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
								if(users1.getUserId() != user.getUserId())
								{
									Userdealssuggestion userDealSuggestion = userDAO.isDealSuggestionExistsForDirectSuggestion(users1.getUserId(), deal.getId(), user.getUserId());
									if(userDealSuggestion != null)
									{
										if(userDealSuggestion.getSuggestedTime() == null || userDealSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
										{
											userDealSuggestion.setUpdatedtime(date);
											userDealSuggestion.setSuggestedTime(suggestionDate);
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
											batch_size++;
											dealsDAO.saveSuggestions(userDealSuggestion);
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
										batch_size++;
										response.setStatus("Ok");
									}
								}
							}
						}
					}

				}
				else if (suggestionType.equalsIgnoreCase("business"))
				{
					Business business = businessDAO.getBusinessById(suggestionId);
					if(friendsIdList != null)
					{
						for(Integer friendId:friendsIdList)
						{
							Usernondealsuggestion userNonDealSuggestion = userDAO.isNonDealSuggestionExistsForDirectSuggestion(friendId, business.getId(), user.getUserId());
							if(userNonDealSuggestion != null)
							{
								if(userNonDealSuggestion.getSuggestedTime() == null || userNonDealSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
								{
									userNonDealSuggestion.setUpdatedtime(date);
									userNonDealSuggestion.setSuggestedTime(suggestionDate);
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
								if(users1.getUserId() != user.getUserId())
								{
									Usernondealsuggestion userNonDealSuggestion = userDAO.isNonDealSuggestionExistsForDirectSuggestion(users1.getUserId(), business.getId(), user.getUserId());
									if(userNonDealSuggestion != null)
									{
										if(userNonDealSuggestion.getSuggestedTime() == null || userNonDealSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
										{
											userNonDealSuggestion.setUpdatedtime(date);
											userNonDealSuggestion.setSuggestedTime(suggestionDate);
											userNonDealSuggestion.setSuggestionType("Direct Suggestion");
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
				}
				else if(suggestionType.equalsIgnoreCase("community"))
				{
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
			}
		}
		logger.debug("done");
		return response;
	}


	private boolean saveEventDirectSuggestion(User user, int friendId,int eventId, Date date, Date suggestionDate, int batch_size, Date currentDate)
	{
		Usereventsuggestion userEventSuggestion = userDAO.isEventSuggestionExistsForDirectSuggestion(friendId, eventId, user.getUserId());
		if(userEventSuggestion != null)
		{
			if(userEventSuggestion.getSuggestedTime() == null || userEventSuggestion.getSuggestedTime().compareTo(currentDate) >= 0)
			{
				userEventSuggestion.setUpdatedTime(date);
				userEventSuggestion.setSuggestedTime(suggestionDate);
				userEventSuggestion.setSuggestionType("Direct Suggestion");
				userDAO.updateUserEventSuggestion(userEventSuggestion);
			}
			else
			{
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
			}

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


	/**
	 * Comparator class to compare wallFeed by like package rank
	 */
	public class WallFeedComparator implements Comparator<Object[]> {
		@Override
		public int compare(Object[] o1, Object[] o2) 
		{
			return (int)(Integer.parseInt(o2[1].toString())-Integer.parseInt(o2[2].toString()));
		}
	}

}
