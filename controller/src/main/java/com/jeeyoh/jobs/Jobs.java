package com.jeeyoh.jobs;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeeyoh.service.fandango.IFandangoService;
import com.jeeyoh.service.groupon.IGrouponFilterEngineService;
import com.jeeyoh.service.groupon.IGrouponService;
import com.jeeyoh.service.jobs.ICalculateTopSuggestionsService;
import com.jeeyoh.service.jobs.IDealSearch;
import com.jeeyoh.service.jobs.IEventSearch;
import com.jeeyoh.service.jobs.INonDealSearch;
import com.jeeyoh.service.jobs.IWallService;
import com.jeeyoh.service.search.ICommunitySearchService;
import com.jeeyoh.service.search.IMatchingEventsService;
import com.jeeyoh.service.search.INonDealSuggestionSearchService;
import com.jeeyoh.service.stubhub.IStubhubFilterEngineService;
import com.jeeyoh.service.stubhub.IStubhubService;
import com.jeeyoh.service.yelp.IYelpFilterEngineService;
import com.jeeyoh.service.yelp.IYelpService;

public class Jobs {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IWallService wallService;

	@Autowired
	private INonDealSearch nonDealSearch;

	@Autowired
	private IDealSearch dealSearch;

	@Autowired
	private IEventSearch eventSearch;

	@Autowired
	private IMatchingEventsService matchingEventsService;
	
	@Autowired
	private ICalculateTopSuggestionsService calculateTopSuggestionsService;
	
	@Autowired
	private IGrouponService grouponService;

	@Autowired 
	private IYelpService yelpService;
	
	@Autowired
	private IGrouponFilterEngineService grouponFilterEngineService;

	@Autowired
	private IYelpFilterEngineService yelpFilterEngineService;

	@Autowired
	private IFandangoService fandangoService;

	@Autowired
	private IStubhubService stubhubService;

	@Autowired
	private ICommunitySearchService communitySearchService;

	@Autowired
	private INonDealSuggestionSearchService nonDealSuggestionSearch;

	@Autowired
	private IStubhubFilterEngineService stubhubFilterEngineService;


	/**
	 * This job will calculate wallFeed's package rank
	 */
	public void wallFeed() 
	{ 

		logger.debug("Quartz wallFeed :::: "+Calendar.getInstance().getTime());
		wallService.addWeightContentOnItem();
	}  
	
	/**
	 * This job fire Jeeyoh Suggestion Engine
	 */
	public void suggestion() 
	{  
		logger.debug("Quartz suggestion :::: "+Calendar.getInstance().getTime());
		/*nonDealSearch.search();
		logger.debug("deal :::: ");
		dealSearch.search();
		logger.debug("event :::: ");
		eventSearch.search();
		calculateTopSuggestionsService.calculateTopJeyoohSuggestions();
		calculateTopSuggestionsService.caculateTopFriendsSuggestions();
		calculateTopSuggestionsService.calculateTopCommunitySuggestions();
		matchingEventsService.searchMatchingEvents(); */
	}  

	/**
	 * This job will fire filter engines
	 */
	public void fetchFilter() 
	{  
		logger.debug("fetchFilter :::: "+Calendar.getInstance().getTime());
		/*//Run filter Engine for Stubhub Events
		stubhubFilterEngineService.filter();
		
		//Run filter Engine for Groupon Deals
		grouponFilterEngineService.filter();
		
		//Run filter Engine for Yelp deals and business
		yelpFilterEngineService.filterDeals();
		yelpFilterEngineService.filterBusiness();*/
		
	}  
	
	
	/**
	 * This job will fire code to fetch raw data from APIs
	 */
	public void fetchRawData() 
	{  
		logger.debug("fetchRawData :::: "+Calendar.getInstance().getTime());
		/*//Fetch Data from Yelp
		yelpService.search();
		yelpService.searchBusiness();
		
		// Fetch data from Groupon
		String country = "USA";
		grouponService.loadDeals(country);
		
		// Fetch data from Stubhub
		stubhubService.stubhubEvents();*/
	}  

	/**
	 * This job calculates matching events
	 */
	public void matchingEvents() 
	{  
		logger.debug("Quartz matching events :::: "+Calendar.getInstance().getTime());
	}  
	
	/**
	 * This job calculates Top Jeeyoh Suggestions
	 */
	public void calculateTopJeeyohSuggestion() 
	{  
		logger.debug("Quartz calculateTopJeeyohSuggestion :::: "+Calendar.getInstance().getTime());
		//calculateTopSuggestionsService.calculateTopJeyoohSuggestions();
	}  

}
