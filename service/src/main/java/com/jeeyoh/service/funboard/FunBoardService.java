package com.jeeyoh.service.funboard;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.funboard.FunBoardModel;
import com.jeeyoh.model.funboard.FunBoardRequest;
import com.jeeyoh.model.funboard.MediaContenModel;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.FunBoardDetailResponse;
import com.jeeyoh.model.response.FunBoardResponse;
import com.jeeyoh.model.response.UploadMediaServerResponse;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IFunBoardDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Funboard;
import com.jeeyoh.persistence.domain.FunboardComments;
import com.jeeyoh.persistence.domain.FunboardMediaContent;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Timeline;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.utils.Utils;

@Component("funBoard")
public class FunBoardService implements IFunBoardService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Value("${host.path}")
	private String hostPath;

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

	@Transactional
	@Override
	public BaseResponse saveFunBoardItem(FunBoardRequest request) {

		BaseResponse baseResponse = new BaseResponse();
		User user = userDAO.getUserByEmailId(request.getEmailId());
		if(user != null)
		{
			FunBoardModel funBoardModel = request.getFunBoard();
			Funboard funboard = funBoardDAO.isFunBoardExists(user.getUserId(), funBoardModel.getItemId());
			int batch_size = 0;

			//ArrayList<FunBoardModel> funBoradList = request.getFunBoardList();

			//for(FunBoardModel funBoardModel : funBoradList)
			//{
			//batch_size ++;
			logger.debug("funboard::::  "+funboard);
			if(funboard == null)
			{
				SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

				logger.debug("funboard...." +funBoardModel.getStartDate());
				funboard = new Funboard();
				funboard.setUser(user);
				funboard.setItemId(funBoardModel.getItemId());
				if(funBoardModel.getCategory().equalsIgnoreCase("Sports"))
				{
					funboard.setCategory("SPORT");
				}
				else
					funboard.setCategory(funBoardModel.getCategory());
				funboard.setItemType(funBoardModel.getType());
				try {
					funboard.setStartDate(simple.parse(funBoardModel.getStartDate()));
					funboard.setEndDate(simple.parse(funBoardModel.getEndDate()));
				} catch (Exception e) {
					e.printStackTrace();
				}

				Date currentDate = new Date();
				funboard.setCreatedTime(currentDate);
				funboard.setUpdatedTime(currentDate);
				funboard.setImageUrl(funBoardModel.getImageUrl());

				Time funboardCreationTime = new Time(currentDate.getTime());

				//Timeline timeline = new Timeline();
				//timeline.setTimeLineId(1);

				logger.debug("funboardCreationTime::  "+funboardCreationTime);
				Timeline timeline = funBoardDAO.getTimeLine(funboardCreationTime);

				logger.debug("Funboard timeline : " + timeline);
				funboard.setTimeline(timeline);

				/*if(funBoardModel.getType().equalsIgnoreCase("Business"))
					{
						Business business = businessDAO.getBusinessById(funBoardModel.getItemId());
						BusinessFunboard businessFunboard = new BusinessFunboard();
						businessFunboard.setBusiness(business);
						businessFunboard.setCategory(funBoardModel.getCategory());


					}
					else if(funBoardModel.getType().equalsIgnoreCase("Deal"))
					{
						Deals deal = dealsDAO.getDealById(funBoardModel.getItemId());
						DealFunboard dealFunboard = new DealFunboard();
						dealFunboard.setDeal(deal);
						dealFunboard.setCategory(funBoardModel.getCategory());

					}
					else if(funBoardModel.getType().equalsIgnoreCase("Page"))
					{
						Page page = eventsDAO.getPageDetailsByID(funBoardModel.getItemId());
						PageFunboard pageFunboard = new PageFunboard();
						pageFunboard.setPage(page);
						pageFunboard.setCategory(funBoardModel.getCategory());

					}
					else if(funBoardModel.getType().equalsIgnoreCase("Event"))
					{
						Events events = eventsDAO.getEventById(funBoardModel.getItemId());
						EventFunboard eventFunboard = new EventFunboard();
						eventFunboard.setEvents(events);
						eventFunboard.setCategory(funBoardModel.getCategory());

					}
				 */
				funBoardDAO.saveFunBoard(funboard, batch_size);
				baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
				//}
			}
			else
			{
				logger.debug("Item Already exists in your Fun Board....");
				baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
				baseResponse.setError("Item Already exists in your Fun Board");
			}

		}
		else
		{

			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Invalid Parameter");
		}
		return baseResponse;
	}


	@Transactional
	@Override
	public FunBoardResponse getUserFunBoardItems(UserModel user) {
		List<Funboard> funboards = funBoardDAO.getUserFunBoardItems(user.getUserId());
		int alertsCount = 0;
		List<FunBoardModel> funBoardModels = new ArrayList<FunBoardModel>();
		List<FunBoardModel> fridayActivefunBoardModels = new ArrayList<FunBoardModel>();
		List<FunBoardModel> saturadyActivefunBoardModels = new ArrayList<FunBoardModel>();
		List<FunBoardModel> sundayActivefunBoardModels = new ArrayList<FunBoardModel>();
		for(Funboard funboard : funboards)
		{
			FunBoardModel funBoardModel = null;
			if(funboard.getItemType().equalsIgnoreCase("Event") || funboard.getItemType().equalsIgnoreCase("Deal"))
			{
				if(funboard.getEndDate().compareTo(Utils.getCurrentDate()) >=0)
				{
					alertsCount = funBoardDAO.getNotificationCount(funboard.getFunBoardId());
					logger.debug("funboard: "+funboard.getCategory() +" : "+alertsCount);
					funBoardModel = new FunBoardModel();
					funBoardModel.setFunBoardId(funboard.getFunBoardId());
					funBoardModel.setItemId(funboard.getItemId());
					funBoardModel.setCategory(funboard.getCategory());
					funBoardModel.setImageUrl(funboard.getImageUrl());
					funBoardModel.setTimeLine(funboard.getTimeline().getTimeLineType());
					funBoardModel.setNotificationCount(alertsCount);
					funBoardModel.setType(funboard.getItemType());
					funBoardModel.setStartDate(funboard.getStartDate().toString());
					funBoardModel.setEndDate(funboard.getEndDate().toString());
					if(funboard.getItemType().equalsIgnoreCase("Event"))
					{
						int day = Utils.getDayOfWeek(funboard.getStartDate());
						switch(day){
						case 6: 
							fridayActivefunBoardModels.add(funBoardModel);
							break;	
						case 7: 
							saturadyActivefunBoardModels.add(funBoardModel);
							break;	
						case 1: 
							sundayActivefunBoardModels.add(funBoardModel);
							break;	
						}
					}
					else
					{
						Calendar start = Calendar.getInstance();
						start.setTime(funboard.getStartDate());
						Calendar end = Calendar.getInstance();
						end.setTime(funboard.getEndDate());
						int count = 1;
						int day = 0;
						for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
							if(count <= 3)
							{
								day = Utils.getDayOfWeek(date);
								switch(day){
								case 6: 
									fridayActivefunBoardModels.add(funBoardModel);
									count++;
									break;	
								case 7: 
									saturadyActivefunBoardModels.add(funBoardModel);
									count++;
									break;	
								case 1: 
									sundayActivefunBoardModels.add(funBoardModel);
									count++;
									break;	
								}
							}
							else 
								break;

						}
					}
				}
			}
			else
			{
				alertsCount = funBoardDAO.getNotificationCount(funboard.getFunBoardId());
				logger.debug("funboard: "+funboard.getCategory() +" : "+alertsCount);
				funBoardModel = new FunBoardModel();
				funBoardModel.setFunBoardId(funboard.getFunBoardId());
				funBoardModel.setItemId(funboard.getItemId());
				funBoardModel.setCategory(funboard.getCategory());
				funBoardModel.setImageUrl(funboard.getImageUrl());
				funBoardModel.setTimeLine(funboard.getTimeline().getTimeLineType());
				funBoardModel.setNotificationCount(alertsCount);
				funBoardModel.setType(funboard.getItemType());
				funBoardModel.setStartDate(funboard.getStartDate().toString());
				funBoardModel.setEndDate(funboard.getEndDate().toString());
				fridayActivefunBoardModels.add(funBoardModel);
				saturadyActivefunBoardModels.add(funBoardModel);
				sundayActivefunBoardModels.add(funBoardModel);
			}

		}

		logger.debug("fridayActivefunBoardModels: "+fridayActivefunBoardModels.size() + " saturadyActivefunBoardModels: "+saturadyActivefunBoardModels.size()+" sundayActivefunBoardModels: "+sundayActivefunBoardModels.size());
		FunBoardResponse funBoardResponse = new FunBoardResponse();
		funBoardResponse.setFunBoards(funBoardModels);
		funBoardResponse.setFridayActivefunBoards(fridayActivefunBoardModels);
		funBoardResponse.setSaturdayActivefunBoards(saturadyActivefunBoardModels);
		funBoardResponse.setSundayActivefunBoards(sundayActivefunBoardModels);
		funBoardResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return funBoardResponse;
	}


	@Transactional
	@Override
	public BaseResponse deleteFunBoarditem(FunBoardRequest request) {

		Funboard funboard = funBoardDAO.getFunboardById(request.getFunBoardId());
		if(funboard.getUser().getUserId() == request.getUserId())
		{
			//Delete FunBoard Comments
			funBoardDAO.deleteFunBoardComments(request.getFunBoardId());

			//Delete FunBoard Media Content
			funBoardDAO.deleteFunBoardMediaContent(request.getFunBoardId());

			//Delete FunBoard 
			funBoardDAO.deleteFunBoard(request.getFunBoardId(), request.getUserId());
		}
		else
		{

		}

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return baseResponse;
	}


	@Transactional
	@Override
	public CommentResponse addFunBoardComment(CommentModel commentModel) {

		Date date = null;
		boolean isSaved = false;
		try {
			Funboard funboard = new Funboard();
			funboard.setFunBoardId(commentModel.getItemId());

			User user = new User();
			user.setUserId(commentModel.getUserId());

			FunboardComments funboardComments = new FunboardComments();
			funboardComments.setComment(commentModel.getComment());
			funboardComments.setUser(user);
			funboardComments.setFunboard(funboard);
			funboardComments.setIsComment(commentModel.getIsComment());
			date = new Date();
			funboardComments.setCreatedTime(date);
			funboardComments.setUpdatedTime(date);
			funBoardDAO.saveFunBoardComment(funboardComments);
			isSaved = true;
		} catch (Exception e) {
			isSaved = false;
		}

		CommentResponse response = new CommentResponse();
		if(isSaved)
		{
			User user = userDAO.getUserById(commentModel.getUserId());
			commentModel.setCreatedTime(Utils.getTime(date));
			commentModel.setUserName(user.getFirstName());
			commentModel.setImageUrl(user.getImageUrl());
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

	@Transactional
	@Override
	public FunBoardDetailResponse getFunBoardItem(FunBoardRequest request) {


		FunBoardModel funBoardModel = request.getFunBoard();
		String type = funBoardModel.getType();
		List<User> userList = new ArrayList<User>();
		if(type.equalsIgnoreCase("Business"))
		{
			Business business = businessDAO.getBusinessById(funBoardModel.getItemId());
			funBoardModel.setCategory(business.getBusinesstype().getBusinessType());
			funBoardModel.setDescription(business.getName());
			funBoardModel.setImageUrl(business.getImageUrl());
			funBoardModel.setRating(business.getRating());
			funBoardModel.setTitle(business.getName());
			funBoardModel.setSource(business.getSource());
			userList = userDAO.getAttendingUsersForBusiness(funBoardModel.getItemId(),request.getUserId());

		}
		else if(type.equalsIgnoreCase("Deal"))
		{
			Deals deal = dealsDAO.getDealById(funBoardModel.getItemId());
			funBoardModel.setCategory(deal.getBusiness().getBusinesstype().getBusinessType());
			funBoardModel.setDescription(deal.getTitle());
			funBoardModel.setImageUrl(deal.getLargeImageUrl());
			funBoardModel.setRating(deal.getBusiness().getRating());
			funBoardModel.setTitle(deal.getTitle());
			funBoardModel.setSource(deal.getDealSource());
			userList = userDAO.getAttendingUsersForDeals(funBoardModel.getItemId(),request.getUserId());
		}
		else if(type.equalsIgnoreCase("Event"))
		{
			Events events = eventsDAO.getEventById(funBoardModel.getItemId());
			if(events.getPage() != null)
				funBoardModel.setCategory(events.getPage().getPagetype().getPageType());

			funBoardModel.setDescription(events.getDescription());
			//funBoardModel.setImageUrl(deal.getLargeImageUrl());
			funBoardModel.setRating(0.0);
			funBoardModel.setTitle(events.getTitle());
			funBoardModel.setSource(events.getEventSource());
			userList = userDAO.getAttendingUsersForEvents(funBoardModel.getItemId(),request.getUserId());
		}
		else if(type.equalsIgnoreCase("Community"))
		{
			Page page = eventsDAO.getPageDetailsByID(funBoardModel.getItemId());
			funBoardModel.setCategory(page.getPagetype().getPageType());
			funBoardModel.setDescription(page.getAbout());
			//funBoardModel.setImageUrl(deal.getLargeImageUrl());
			if(page.getBusiness() != null)
				funBoardModel.setRating(page.getBusiness().getRating());
			else
				funBoardModel.setRating(0.0);
			funBoardModel.setTitle(page.getAbout());
			userList = userDAO.getAttendingUsersForpage(funBoardModel.getItemId(),request.getUserId());

		}

		logger.debug("userList: "+userList);

		//Get FunBoard Comments
		List<FunboardComments> comments = funBoardDAO.getComments(request.getFunBoardId());

		//Get FunBoard Images
		List<FunboardMediaContent> mediaContent = funBoardDAO.getmediaContent(request.getFunBoardId());

		List<CommentModel> commentModels = new ArrayList<CommentModel>();
		List<CommentModel> jAlerts = new ArrayList<CommentModel>();
		List<MediaContenModel> mediaContenModels = new ArrayList<MediaContenModel>();
		List<UserModel> attendingUserList = new ArrayList<UserModel>();
		if(comments != null)
		{
			for(FunboardComments funboardComments : comments)
			{
				User user = funboardComments.getUser();
				CommentModel commentModel = new CommentModel();
				commentModel.setComment(funboardComments.getComment());
				commentModel.setUserId(user.getUserId());
				commentModel.setCreatedTime(Utils.getTime(funboardComments.getCreatedTime()));
				commentModel.setUserName(user.getFirstName());
				commentModel.setImageUrl(user.getImageUrl());
				if(funboardComments.getIsComment())
					commentModels.add(commentModel);
				else
					jAlerts.add(commentModel);
			}
		}

		if(mediaContent != null)
		{
			for(FunboardMediaContent funboardMediaContent : mediaContent)
			{
				MediaContenModel mediaContenModel = new MediaContenModel();
				mediaContenModel.setId(funboardMediaContent.getId());
				mediaContenModel.setImageUrl(hostPath+funboardMediaContent.getMediaPathUrl());
				logger.debug("Media content: "+funboardMediaContent.getMediaPathUrl());
				mediaContenModels.add(mediaContenModel);
			}
		}

		if(userList != null)
		{
			for(User user : userList)
			{
				UserModel userModel = new UserModel();
				userModel.setUserId(user.getUserId());
				userModel.setImageUrl(user.getImageUrl());
				attendingUserList.add(userModel);
			}
		}


		funBoardModel.setjAlerts(jAlerts);
		funBoardModel.setComments(commentModels);
		funBoardModel.setImages(mediaContenModels);
		funBoardModel.setAttendingUserList(attendingUserList);

		logger.debug("Details: "+funBoardModel.getDescription()+" : "+funBoardModel.getTitle());
		FunBoardDetailResponse funBoardDetailResponse = new FunBoardDetailResponse();
		funBoardDetailResponse.setFunBoard(funBoardModel);
		funBoardDetailResponse.setStatus(ServiceAPIStatus.OK.getStatus());

		return funBoardDetailResponse;
	}

	@Transactional
	@Override
	public UploadMediaServerResponse uploadMediaContent(MediaContenModel mediaContenModel) {

		logger.debug("getFunBoardId:  "+mediaContenModel.getFunBoardId() + " : UserId: "+ mediaContenModel.getUserId());
		Funboard funboard = new Funboard();
		funboard.setFunBoardId(mediaContenModel.getFunBoardId());

		User user = new User();
		user.setUserId(mediaContenModel.getUserId());

		FunboardMediaContent funboardMediaContent = new FunboardMediaContent();
		funboardMediaContent.setFunboard(funboard);
		funboardMediaContent.setUser(user);
		funboardMediaContent.setMediaType(mediaContenModel.getMediaType());
		funboardMediaContent.setMediaPathUrl(mediaContenModel.getImageUrl());
		Date date = new Date();
		funboardMediaContent.setCreatedTime(date);
		funboardMediaContent.setUpdatedTime(date);
		funBoardDAO.saveFunBoardMediaContent(funboardMediaContent);
		UploadMediaServerResponse response = new UploadMediaServerResponse();
		response.setMediaUrl(hostPath+mediaContenModel.getImageUrl());
		response.setStatus(ServiceAPIStatus.OK.getStatus());
		return response;
	}
}
