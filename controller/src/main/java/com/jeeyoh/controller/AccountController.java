package com.jeeyoh.controller;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jeeyoh.model.response.CommunityResponse;
import com.jeeyoh.model.response.SearchResponse;
import com.jeeyoh.model.search.BusinessModel;
import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.model.search.MainModel;
import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.search.SearchRequest;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.notification.service.IMessagingEventPublisher;
import com.jeeyoh.service.fandango.IFandangoService;
import com.jeeyoh.service.groupon.IGrouponFilterEngineService;
import com.jeeyoh.service.groupon.IGrouponService;
import com.jeeyoh.service.jobs.ICalculateTopSuggestionsService;
import com.jeeyoh.service.jobs.IDealSearch;
import com.jeeyoh.service.jobs.IEventSearch;
import com.jeeyoh.service.jobs.INonDealSearch;
import com.jeeyoh.service.search.ICommunitySearchService;
import com.jeeyoh.service.search.IEventsSuggestionSearchService;
import com.jeeyoh.service.search.IManualUpload;
import com.jeeyoh.service.search.INonDealSuggestionSearchService;
import com.jeeyoh.service.search.ISearchDealsService;
import com.jeeyoh.service.search.ISpotSearchService;
import com.jeeyoh.service.search.IUserDealsSearchService;
import com.jeeyoh.service.stubhub.IStubhubFilterEngineService;
import com.jeeyoh.service.stubhub.IStubhubService;
import com.jeeyoh.service.userservice.IUserService;
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
		//yelpService.search();
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
		System.out.println("pageid : "+pageId);
		if(pageId!=0)
		{
			CommunityResponse communityResponse = communitySearchService.searchCommunityDetails(1, pageId);
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
		userService.getUserSuggestions(user);
		//nonDealSearch.caculateTopSuggestions();
		return modelAndView;

	}

	@RequestMapping(value = "/manualUpload", method = RequestMethod.GET)
	public ModelAndView manualUpload(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("manualUpload");
		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public ModelAndView uploadFile(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("manualUpload");

		if(ServletFileUpload.isMultipartContent(request))
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
		}
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
		calculateTopSuggestionsService.caculateTopFriendsSuggestions();
		return modelAndView;
	}

}
