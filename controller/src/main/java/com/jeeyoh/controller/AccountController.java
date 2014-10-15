package com.jeeyoh.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.funboard.FunBoardModel;
import com.jeeyoh.model.funboard.FunBoardRequest;
import com.jeeyoh.model.funboard.MediaContenModel;
import com.jeeyoh.model.funboard.WallFeedRequest;
import com.jeeyoh.model.response.CommunityResponse;
import com.jeeyoh.model.response.FriendListResponse;
import com.jeeyoh.model.response.SearchResponse;
import com.jeeyoh.model.response.UploadMediaServerResponse;
import com.jeeyoh.model.search.AddGroupModel;
import com.jeeyoh.model.search.BusinessModel;
import com.jeeyoh.model.search.CommunityReviewModel;
import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.model.search.MainModel;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.search.SearchRequest;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.notification.service.IMessagingEventPublisher;
import com.jeeyoh.service.addfriend.IAddFriendService;
import com.jeeyoh.service.addgroup.IAddGroupService;
import com.jeeyoh.service.fandango.IFandangoService;
import com.jeeyoh.service.funboard.IFunBoardService;
import com.jeeyoh.service.groupon.IGrouponFilterEngineService;
import com.jeeyoh.service.groupon.IGrouponService;
import com.jeeyoh.service.jobs.ICalculateTopSuggestionsService;
import com.jeeyoh.service.jobs.IDealSearch;
import com.jeeyoh.service.jobs.IEventSearch;
import com.jeeyoh.service.jobs.INonDealSearch;
import com.jeeyoh.service.jobs.IWallService;
import com.jeeyoh.service.livingsocial.ILivingSocialFilterEngineService;
import com.jeeyoh.service.livingsocial.ILivingSocialService;
import com.jeeyoh.service.search.IAddDirectSuggestionService;
import com.jeeyoh.service.search.ICommunitySearchService;
import com.jeeyoh.service.search.IEventsSuggestionSearchService;
import com.jeeyoh.service.search.IManualUpload;
import com.jeeyoh.service.search.IMatchingEventsService;
import com.jeeyoh.service.search.INonDealSuggestionSearchService;
import com.jeeyoh.service.search.ISearchDealsService;
import com.jeeyoh.service.search.ISpotSearchService;
import com.jeeyoh.service.search.IUserDealsSearchService;
import com.jeeyoh.service.stubhub.IStubhubFilterEngineService;
import com.jeeyoh.service.stubhub.IStubhubService;
import com.jeeyoh.service.userservice.IMediaService;
import com.jeeyoh.service.userservice.IUserService;
import com.jeeyoh.service.wallfeed.IWallFeedSharingService;
import com.jeeyoh.service.yelp.IYelpFilterEngineService;
import com.jeeyoh.service.yelp.IYelpService;

@Controller
public class AccountController {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IGrouponService grouponService;

	@Autowired 
	private IYelpService yelpService;

	@Autowired
	private IGrouponFilterEngineService grouponFilterEngineService;

	@Autowired
	private IYelpFilterEngineService yelpFilterEngineService;

	@Autowired
	private INonDealSearch nonDealSearch;

	@Autowired
	private IDealSearch dealSearch;

	@Autowired
	private IFandangoService fandangoService;

	@Autowired
	private IStubhubService stubhubService;

	@Autowired
	private ICommunitySearchService communitySearchService;

	@Autowired
	private IUserDealsSearchService userDealsSearch;

	@Autowired
	private INonDealSuggestionSearchService nonDealSuggestionSearch;

	@Autowired
	private ISearchDealsService searchDeals;

	@Autowired
	private IStubhubFilterEngineService stubhubFilterEngineService;

	@Autowired
	private IEventSearch eventSearch;

	@Autowired
	private IEventsSuggestionSearchService eventsSuggestionSearch;

	@Autowired
	private ISpotSearchService spotSearchService;

	@Autowired
	IMessagingEventPublisher eventPublisher;

	@Autowired
	private IManualUpload manualUpload;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICalculateTopSuggestionsService calculateTopSuggestionsService;

	@Autowired
	private IMatchingEventsService matchingEventsService;

	@Autowired
	private IFunBoardService funBoardService;

	@Autowired
	private IWallService wallService;

	@Autowired
	private IMediaService mediaService;
	
	@Autowired
	private IAddGroupService addGroupService;
	
	@Autowired
	private ILivingSocialService livingSocialService;

	@Autowired
	private ILivingSocialFilterEngineService livingSocialFilterEngineService;
	
	@Autowired
	private IAddFriendService addFriendService;
	
	@Autowired
	private IAddDirectSuggestionService addDirectSuggestionService;
	
	@Autowired
	private IWallFeedSharingService wallFeedSharingService;

	private final String UPLOAD_DIRECTORY = "C:/uploads";

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("home");
		return modelAndView;
	}
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("search");
		return modelAndView;
	}

	@RequestMapping(value = "/mydeals", method = RequestMethod.GET)
	public ModelAndView mydeals(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("mydeals");
		return modelAndView;
	}
	@RequestMapping(value = "/contactandcommunitydeals", method = RequestMethod.GET)
	public ModelAndView contactandcommunitydeals(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("contactandcommunitydeals");
		return modelAndView;
	}

	@RequestMapping(value = "/nonDealSuggestionSearch", method = RequestMethod.GET)
	public ModelAndView nonDealSuggestionSearch(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("nonDealSuggestionSearch");
		return modelAndView;
	}

	@RequestMapping(value = "/community", method = RequestMethod.GET)
	public ModelAndView community(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("community");
		return modelAndView;
	}

	@RequestMapping(value = "/myDeals", method = RequestMethod.GET)
	public ModelAndView myDeals(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("userDeals");
		return modelAndView;
	}

	@RequestMapping(value = "/eventSuggestionSearch", method = RequestMethod.GET)
	public ModelAndView eventSuggestionSearch(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("eventSuggestionSearch");
		return modelAndView;
	}

	@RequestMapping(value = "/spotSearch", method = RequestMethod.GET)
	public ModelAndView spotSearch(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("spotSearch");
		return modelAndView;
	}
	
	@RequestMapping(value = "/searchFriend", method = RequestMethod.GET)
	public ModelAndView searchFriend(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("searchFriend");
		return modelAndView;
	}

	@RequestMapping(value = "/getDivisions", method = RequestMethod.GET)
	public ModelAndView getDivisions(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		grouponService.populateDivisions();
		return modelAndView;
	}

	@RequestMapping(value = "/getDealsByCountry", method = RequestMethod.GET)
	public ModelAndView getDealsByCountry(HttpServletRequest request,
			HttpServletResponse httpresponse) {		
		ModelAndView modelAndView = new ModelAndView("home");
		String country = "USA";
		grouponService.loadDeals(country);
		return modelAndView;
	}

	@RequestMapping(value = "/searchYelp", method = RequestMethod.GET)
	public ModelAndView searchYelp(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		yelpService.search();
		yelpService.searchBusiness();
		return modelAndView;
	}


	@RequestMapping(value = "/filterDealsByDiscount", method = RequestMethod.GET)
	public ModelAndView filterDealsByDiscount(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		grouponFilterEngineService.filter();
		return modelAndView;
	}


	@RequestMapping(value = "/filterDealsForYelp", method = RequestMethod.GET)
	public ModelAndView filterDealsForYelp(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		yelpFilterEngineService.filterDeals();
		return modelAndView;
	}

	@RequestMapping(value = "/filterBusinessForYelp", method = RequestMethod.GET)
	public ModelAndView filterBusinessForYelp(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		yelpFilterEngineService.filterBusiness();
		return modelAndView;
	}



	@RequestMapping(value = "/nonDealSuggestion", method = RequestMethod.GET)
	public ModelAndView nonDealSuggestion(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		nonDealSearch.search();		
		return modelAndView;
	}

	@RequestMapping(value = "/dealSuggestion", method = RequestMethod.GET)
	public ModelAndView dealSuggestion(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		//nonDealSearch.search();
		dealSearch.search();
		return modelAndView;
	}

	@RequestMapping(value = "/fandangoData", method = RequestMethod.GET)
	public ModelAndView fandangoData(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		fandangoService.topTen();
		return modelAndView;
	}

	@RequestMapping(value = "/stubhub", method = RequestMethod.GET)
	public ModelAndView stubhub(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		stubhubService.stubhubEvents();
		return modelAndView;
	}

	/*
	 *  Get request and show deal suggestions
	 */
	@RequestMapping(value = "/dealSuggestionResult", method = RequestMethod.GET)
	public ModelAndView dealSuggestionResult(HttpServletRequest request, HttpServletResponse httpresponse){

		logger.debug("Deal Suggestion  :: ");
		ModelAndView modelAndView = new ModelAndView("search");
		String keyword = request.getParameter("keyword");
		String category = request.getParameter("category");
		String location = request.getParameter("location");
		String emailId = request.getParameter("emailId");
		logger.debug("PARAMETER :: "+keyword+ " category "+ category + "Location" + location);
		Set<DealModel> deals = searchDeals.getDeals(keyword,category,location,emailId);		
		MainModel mainModel = new MainModel();
		if(emailId != null && !emailId.trim().equals(""))
		{
			mainModel.setIsUser(true);
		}
		if(deals != null)
		{
			mainModel.setDealModel(deals);
		}
		modelAndView.addObject("mainModel", mainModel);
		return modelAndView;
	}

	/*
	 *  Get request and show users deal
	 */
	@RequestMapping(value = "/getmydeals", method = RequestMethod.GET)
	public ModelAndView getmydeals(HttpServletRequest request, HttpServletResponse httpresponse){

		logger.debug("Deal getmydeals  :: ");
		ModelAndView modelAndView = new ModelAndView("mydeals");

		String emailId = request.getParameter("emailId");
		Set<DealModel> deals = searchDeals.getUserDeals(emailId);		
		MainModel mainModel = new MainModel();
		if(deals != null)
		{
			mainModel.setDealModel(deals);
		}
		modelAndView.addObject("mainModel", mainModel);
		return modelAndView;
	}


	/*
	 *  User Contacts and Community Deals
	 */
	@RequestMapping(value = "/contactandCommuity", method = RequestMethod.GET)
	public ModelAndView contactandCommuity(HttpServletRequest request, HttpServletResponse httpresponse){

		logger.debug("Deal getmydeals  :: ");
		ModelAndView modelAndView = new ModelAndView("contactandcommunitydeals");
		String emailId = request.getParameter("emailId");
		///Set<DealModel> deals = searchDeals.getUserContactAndCommunityDeals(emailId);		
		Set<DealModel> deals = searchDeals.getUserContactAndCommunityDeals(emailId);	
		MainModel mainModel = new MainModel();
		if(deals != null)
		{
			mainModel.setDealModel(deals);
		}
		modelAndView.addObject("mainModel", mainModel);
		return modelAndView;
	}

	@RequestMapping(value = "/nonDealSearch", method = RequestMethod.GET)
	public ModelAndView nonDealSearch(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("nonDealSuggestionSearch");
		String userEmail = request.getParameter("userEmail");
		String searchText = request.getParameter("searchText");
		String location = request.getParameter("location");
		String category = request.getParameter("businessCategory");
		String rating = request.getParameter("businessRating");
		List<BusinessModel> businessList = nonDealSuggestionSearch.search(userEmail.trim(), searchText.trim(), category.trim(), location.trim(),rating.trim());
		MainModel model = new MainModel();
		model.setBusinessList(businessList);
		if(userEmail != null && !userEmail.trim().equals(""))
		{
			model.setIsUser(true);
			model.setName(userEmail);
		}

		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}


	@RequestMapping(value = "/communitySearch", method = RequestMethod.GET)
	public ModelAndView communitySearch(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("community");
		String userEmail = request.getParameter("userEmail");
		/*String searchText = request.getParameter("searchText");
		String location = request.getParameter("location");
		String category = request.getParameter("businessCategory");*/
		List<PageModel> pageList = communitySearchService.search(userEmail.trim());
		MainModel model = new MainModel();
		model.setPageList(pageList);
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}

	@RequestMapping(value = "/myDealsSearch", method = RequestMethod.GET)
	public ModelAndView myDealsSearch(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("userDeals");
		String userEmail = request.getParameter("userEmail");
		/*String searchText = request.getParameter("searchText");
		String location = request.getParameter("location");
		String category = request.getParameter("businessCategory");*/
		List<DealModel> dealList = userDealsSearch.search(userEmail.trim());
		MainModel model = new MainModel();
		model.setDealList(dealList);
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}

	@RequestMapping(value = "/filterEventsForStubhub", method = RequestMethod.GET)
	public ModelAndView filterEventsForStubhub(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		stubhubFilterEngineService.filter();
		return modelAndView;
	}

	@RequestMapping(value = "/eventsSuggestion", method = RequestMethod.GET)
	public ModelAndView eventsSuggestion(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		eventSearch.search();		
		return modelAndView;
	}

	@RequestMapping(value = "/eventsSearch", method = RequestMethod.GET)
	public ModelAndView eventsSearch(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("eventSuggestionSearch");
		String userEmail = request.getParameter("userEmail");
		String searchText = request.getParameter("searchText");
		String location = request.getParameter("location");
		String category = request.getParameter("eventCategory");
		List<EventModel> eventsList = eventsSuggestionSearch.search(userEmail.trim(), searchText.trim(), category.trim(), location.trim());
		MainModel model = new MainModel();
		model.setEventsList(eventsList);
		if(userEmail != null && !userEmail.trim().equals(""))
		{
			model.setIsUser(true);
		}
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	@RequestMapping(value = "/testing", method = RequestMethod.GET)
	public ModelAndView testing(HttpServletRequest request, HttpServletResponse httpresponse){

		ModelAndView modelAndView = new ModelAndView("home");
		//eventPublisher.sendConfirmationEmail();
		//UserModel user = new UserModel();
		//user.setEmailId("gaurav.shandilya@gmail.com");
		//userService.getUserSuggestions(user);
		int pageId = Integer.parseInt(request.getParameter("pageId"));
		int offset = Integer.parseInt(request.getParameter("offset"));
		if(pageId!=0)
		{
			CommunityResponse communityResponse = communitySearchService.searchCommunityDetails(1, pageId,offset,10);
			PageModel pageDetails = communityResponse.getCommunityDetails();
			logger.debug("getCommunityDetails => about: "+pageDetails.getAbout()+";created date: "+pageDetails.getCreatedDate()+";Owner: "+pageDetails.getOwner()+";PageType: "+pageDetails.getPageType()+";profile picture: "+pageDetails.getProfilePicture()+";page url: "+pageDetails.getPageUrl());
			List<EventModel> currentEventsList = communityResponse.getCurrentEvents();
			for(EventModel eventModel:currentEventsList)
			{
				logger.debug("getCurrentEvents => description: "+eventModel.getDescription()+"; event date: "+eventModel.getEvent_date()+"\n");
			}
			List<EventModel> upcomingEventsList = communityResponse.getUpcomingEvents();
			for(EventModel eventModel:upcomingEventsList)
			{
				logger.debug("getUpcomingEvents => description: "+eventModel.getDescription()+"; event date: "+eventModel.getEvent_date()+"\n");
			}
			List<EventModel> lastEventsList = communityResponse.getPastEvents();
			for(EventModel eventModel:lastEventsList)
			{
				logger.debug("getPastEvents => description: "+eventModel.getDescription()+"; event date: "+eventModel.getEvent_date()+"\n");
			}
		}
		//eventPublisher.sendConfirmationEmail();
		return modelAndView;

	}

	@RequestMapping(value = "/testingSuggestion", method = RequestMethod.GET)
	public ModelAndView testingSuggestion(HttpServletRequest request, HttpServletResponse httpresponse){

		ModelAndView modelAndView = new ModelAndView("home");
		//eventPublisher.sendConfirmationEmail();

		UserModel user = new UserModel();
		user.setEmailId("gaurav.shandilya@gmail.com");
		user.setUserId(1);
		user.setLimit(10);
		//userService.getUserSuggestions(user);
		//userService.getUserTopSuggestions(user);

		//nonDealSearch.caculateTopSuggestions();
		//wallService.addWeightContentOnItem();
		return modelAndView;

	}

	@RequestMapping(value = "/manualUpload", method = RequestMethod.GET)
	public ModelAndView manualUpload(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("manualUpload");
		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST) 
	public ModelAndView uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException {
		ModelAndView modelAndView = new ModelAndView("manualUpload");

		logger.debug("Upload File");
		UploadMediaServerResponse uploadMediaServerResponse = new UploadMediaServerResponse();

		List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		for(FileItem item : multiparts)
		{
			if(!item.isFormField())
			{
				String name = new File(item.getName()).getName();
				logger.debug("uploadedMediaServerPATH = " + name);
				uploadMediaServerResponse = mediaService.uploadOnServer(item.getInputStream(), name, "34");
				String output = "File uploaded to : " + uploadMediaServerResponse.getMediaUrl();
				logger.debug(output);
			}
		}
		// File mediaFileObject = new File(uploadedMediaServerPATH + "34_" +
		// randomNumber + "_" + fileDetail.getFileName());
		// save it
		MediaContenModel mediaContenModel = new MediaContenModel();
		mediaContenModel.setFunBoardId(1);
		mediaContenModel.setUserId(1);
		mediaContenModel.setMediaType("image");
		mediaContenModel.setImageUrl(uploadMediaServerResponse.getMediaUrl());
		funBoardService.uploadMediaContent(mediaContenModel);
		/*if(ServletFileUpload.isMultipartContent(request))
		{
			try{
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for(FileItem item : multiparts)
				{
					if(!item.isFormField())
					{
						String name = new File(item.getName()).getName();
						item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
						String fullFilePath = UPLOAD_DIRECTORY + File.separator + name;
						manualUpload.uploadExcel(fullFilePath);
					}
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}       
		}
		else{
			request.setAttribute("message","Sorry this Servlet only handles file upload request");
		}*/
		return modelAndView;
	}

	@RequestMapping(value = "/spotSearchResult", method = RequestMethod.GET)
	public ModelAndView spotSearchResult(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("spotSearch");

		String searchText = request.getParameter("searchText");
		String category = request.getParameter("searchCategory");
		String location = request.getParameter("location");
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setSearchText(searchText.trim());
		searchRequest.setCategory(category.trim());
		searchRequest.setLocation(location);
		SearchResponse searchResponse = spotSearchService.search(searchRequest);
		MainModel model = new MainModel();
		model.setSearchResult(searchResponse.getSearchResult());
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}

	@RequestMapping(value = "/calculateTop10Suggestions", method = RequestMethod.GET)
	public ModelAndView calculateTop10Suggestions(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		calculateTopSuggestionsService.calculateTopJeyoohSuggestions();
		calculateTopSuggestionsService.caculateTopFriendsSuggestions();
		calculateTopSuggestionsService.calculateTopCommunitySuggestions();
		return modelAndView;
	}


	@RequestMapping(value = "/matchingEvents", method = RequestMethod.GET)
	public ModelAndView matchingEvents(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("home");
		int userId = Integer.parseInt(request.getParameter("userId"));
		if(userId!=0)
		{
			matchingEventsService.searchMatchingEvents();
		}
		return modelAndView;
	}

	@RequestMapping(value = "/funBaord", method = RequestMethod.GET)
	public ModelAndView funBaord(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("home");
		FunBoardRequest funBoardRequest = new FunBoardRequest();
		funBoardRequest.setEmailId("gaurav.shandilya@gmail.com");
		funBoardRequest.setUserId(1);
		//ArrayList<FunBoardModel> list = new ArrayList<FunBoardModel>();
		FunBoardModel funBoardModel = new FunBoardModel();
		funBoardModel.setCategory("Sport");
		funBoardModel.setType("Deal");
		funBoardModel.setItemId(3232);
		//list.add(funBoardModel);

		funBoardModel = new FunBoardModel();
		funBoardModel.setCategory("Sport");
		funBoardModel.setType("Deal");
		funBoardModel.setItemId(3233);
	

		/*funBoardModel = new FunBoardModel();
		funBoardModel.setCategory("Restaurant");
		funBoardModel.setType("Deal");
		funBoardModel.setItemId(691);
		list.add(funBoardModel);

		funBoardModel = new FunBoardModel();
		funBoardModel.setCategory("Theater");
		funBoardModel.setType("Events");
		funBoardModel.setItemId(51);
		list.add(funBoardModel);

		funBoardModel = new FunBoardModel();
		funBoardModel.setCategory("Theater");
		funBoardModel.setType("Page");
		funBoardModel.setItemId(26);
		list.add(funBoardModel);*/

		funBoardRequest.setFunBoard(funBoardModel);
		funBoardService.saveFunBoardItem(funBoardRequest);
		funBoardModel.setItemId(26);
		funBoardService.saveFunBoardItem(funBoardRequest);

		UserModel user = new UserModel();
		user.setEmailId("gaurav.shandilya@gmail.com");
		user.setUserId(1);

		CommentModel commentModel = new CommentModel();
		commentModel.setUserId(1);
		commentModel.setItemId(2);
		commentModel.setComment("hi all, how are you?");
		commentModel.setIsComment(false);
		funBoardService.addFunBoardComment(commentModel);


		commentModel.setUserId(2);
		commentModel.setItemId(3);
		commentModel.setComment("hi all");
		commentModel.setIsComment(false);
		funBoardService.addFunBoardComment(commentModel);

		commentModel.setUserId(2);
		commentModel.setItemId(3);
		commentModel.setComment("hi all, how are you?");
		commentModel.setIsComment(false);
		//funBoardService.addFunBoardComment(commentModel);

		funBoardService.getUserFunBoardItems(user);

		//funBoardService.getFunBoardItem(funBoardRequest);

		//funBoardService.deleteFunBoarditem(funBoardRequest);
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/addGroup", method = RequestMethod.GET)
	public ModelAndView addGroup(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String groupName = request.getParameter("groupName");
		String category = request.getParameter("category");
		String members = request.getParameter("addMember");
		String privacy = request.getParameter("privacy");
		logger.debug("add group => userId =>"+userId+"; groupName => "+groupName+"; category =>"+category+"; addMember =>"+members+"; privacy =>"+privacy);
		AddGroupModel addGroupModel = new AddGroupModel();
		addGroupService.addGroup(addGroupModel);
		return modelAndView;
	}
	
	@RequestMapping(value = "/savePageComment", method = RequestMethod.GET)
	public ModelAndView savePageComment(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String itemId = request.getParameter("itemId");
		String comment = request.getParameter("comment");
		CommentModel commentModel = new CommentModel();
		commentModel.setComment(comment);
		commentModel.setUserId(Integer.parseInt(userId));
		commentModel.setItemId(Integer.parseInt(itemId));
		communitySearchService.saveCommunityComments(commentModel);
		return modelAndView;
	}
	
	@RequestMapping(value = "/getCities", method = RequestMethod.GET)
	public ModelAndView getCities(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		livingSocialService.loadCities();
		return modelAndView;
	}
	
	@RequestMapping(value = "/getLivingSocialDeals", method = RequestMethod.GET)
	public ModelAndView getLivingSocialDeals(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		livingSocialService.loadLdeals();
		return modelAndView;
	}@RequestMapping(value = "/filterLivingSocialDealsByDiscount", method = RequestMethod.GET)
	public ModelAndView filterLivingSocialDealsByDiscount(HttpServletRequest request,
			HttpServletResponse httpresponse) {
		ModelAndView modelAndView = new ModelAndView("home");
		livingSocialFilterEngineService.filter();
		return modelAndView;
	}
	
	@RequestMapping(value = "/saveIfFollowingPage", method = RequestMethod.GET)
	public ModelAndView saveIfFollowingPage(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String pageId = request.getParameter("pageId");
		String isFollow = request.getParameter("isFollow");
		communitySearchService.saveIsFollowingPage(Integer.parseInt(userId), Integer.parseInt(pageId), Boolean.parseBoolean(isFollow));
		MainModel model = new MainModel();
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/wallFeed", method = RequestMethod.GET)
	public ModelAndView wallFeed(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("home");
		wallService.addWeightContentOnItem();
		
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/saveIsFollowingEvent", method = RequestMethod.GET)
	public ModelAndView saveIsFollowingEvent(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String eventId = request.getParameter("eventId");
		String isFollow = request.getParameter("isFollow");
		communitySearchService.saveIsFollowingEvent(Integer.parseInt(userId), Integer.parseInt(eventId), Boolean.parseBoolean(isFollow));
		MainModel model = new MainModel();
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	@RequestMapping(value = "/saveIsFavEvent", method = RequestMethod.GET)
	public ModelAndView saveIsFavEvent(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String eventId = request.getParameter("eventId");
		String isFav = request.getParameter("isFav");
		logger.debug("saveIsFavEvent => userID :" + userId + "eventID : "+eventId + "isFav : "+isFav);
		communitySearchService.saveFavoritePage(Integer.parseInt(userId), Integer.parseInt(eventId), Boolean.parseBoolean(isFav));
		MainModel model = new MainModel();
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	
	
	
	
	@RequestMapping(value = "/friendSearch", method = RequestMethod.GET)
	public ModelAndView friendSearch(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("searchFriend");
		String location = request.getParameter("location");
		String name = request.getParameter("name");
		String userId = request.getParameter("userId");
		
		FriendListResponse friendList = addFriendService.searchFriend(location, name,"", Integer.parseInt(userId));
		MainModel model = new MainModel();
		if(friendList!=null)
		{
			model.setUsersList(friendList.getUser());	
		}
	
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	@RequestMapping(value = "/sendFriendRequest", method = RequestMethod.GET)
	public ModelAndView sendFriendRequest(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String contactId = request.getParameter("contactId");
		logger.debug("sendFriendRequest => userID :" + userId + "contactId : "+contactId);
		addFriendService.sendFriendRequest(Integer.parseInt(userId), Integer.parseInt(contactId));
		MainModel model = new MainModel();
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	@RequestMapping(value = "/acceptFriendRequest", method = RequestMethod.GET)
	public ModelAndView acceptFriendRequest(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String contactId = request.getParameter("contactId");
		logger.debug("acceptFriendRequest => userID :" + userId + "contactId : "+contactId);
		addFriendService.acceptFriendRequest(Integer.parseInt(userId), Integer.parseInt(contactId));
		MainModel model = new MainModel();
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	@RequestMapping(value = "/denyFriendRequest", method = RequestMethod.GET)
	public ModelAndView denyFriendRequest(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String contactId = request.getParameter("contactId");
		logger.debug("denyFriendRequest => userID :" + userId + "contactId : "+contactId);
		addFriendService.denyFriendRequest(Integer.parseInt(userId), Integer.parseInt(contactId));
		MainModel model = new MainModel();
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	@RequestMapping(value = "/blockFriendRequest", method = RequestMethod.GET)
	public ModelAndView BlockFriendRequest(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String contactId = request.getParameter("contactId");
		logger.debug("blockFriendRequest => userID :" + userId + "contactId : "+contactId);
		addFriendService.blockFriendRequest(Integer.parseInt(userId), Integer.parseInt(contactId));
		MainModel model = new MainModel();
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/addDirectSuggestion", method = RequestMethod.GET)
	public ModelAndView addEventSuggestion(HttpServletRequest request, HttpServletResponse httpresponse){
		ModelAndView modelAndView = new ModelAndView("home");
		String userId = request.getParameter("userId");
		String contactId = request.getParameter("contactId");
		String category = request.getParameter("category");
		String suggestionId = request.getParameter("suggestionId");
		String suggestionType = request.getParameter("suggestionType");
		String suggestedTime = request.getParameter("suggestedTime");
		ArrayList<Integer> friendsList = new ArrayList<Integer>();
		String[] s =contactId.split(",");
		for(int i = 0; i<s.length;i++)
		{
			friendsList.add(Integer.parseInt(s[i]));
		}
		addDirectSuggestionService.addSuggestions(Integer.parseInt(userId), friendsList,null, Integer.parseInt(suggestionId), category, suggestionType,suggestedTime);
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/wallFeedSharing", method = RequestMethod.GET)
	public ModelAndView wallFeedSharing(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("home");
		WallFeedRequest wallFeedModel = new WallFeedRequest();
		UserModel userModel = new UserModel();
		userModel.setUserId(2);
		UserModel userModel1 = new UserModel();
		userModel1.setUserId(3);
		List<Integer> userList = new ArrayList<Integer>();
		userList.add(1);
		userList.add(2);
		
		FunBoardModel funBoardModel = new FunBoardModel();
		funBoardModel.setFunBoardId(1);
		FunBoardModel funBoardModel1 = new FunBoardModel();
		funBoardModel1.setFunBoardId(2);
		List<FunBoardModel> funBoardList = new ArrayList<FunBoardModel>();
		funBoardList.add(funBoardModel);
		funBoardList.add(funBoardModel1);
		
		wallFeedModel.setUserId(1);
		wallFeedModel.setFriends(userList);
		wallFeedModel.setSharedfunBoardItemsList(funBoardList);
		wallFeedSharingService.saveWallFeedSharingData(wallFeedModel);
		return modelAndView;
	}
	
	@RequestMapping(value = "/communityReview", method = RequestMethod.GET)
	public ModelAndView communityReview(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("home");
		CommunityReviewModel reviewModel = new CommunityReviewModel();
		reviewModel.setComment("testing");
		reviewModel.setRating(1);
		reviewModel.setUserId(1);
		reviewModel.setPageId(25);
		communitySearchService.saveCommunityReview(reviewModel);
		return modelAndView;
	}
    
}
