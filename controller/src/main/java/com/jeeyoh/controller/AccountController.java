package com.jeeyoh.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.model.search.MainModel;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.service.fandango.IFandangoService;
import com.jeeyoh.service.groupon.IGrouponClient;
import com.jeeyoh.service.groupon.IGrouponFilterEngineService;
import com.jeeyoh.service.groupon.IGrouponService;
import com.jeeyoh.service.jobs.IDealSearch;
import com.jeeyoh.service.jobs.INonDealSearch;
import com.jeeyoh.service.search.ISearchDeals;
import com.jeeyoh.service.stubhub.IStubhubService;
import com.jeeyoh.service.yelp.IYelpFilterEngineService;
import com.jeeyoh.service.yelp.IYelpService;

@Controller
public class AccountController {
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private IGrouponClient grouponClient;

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
	private ISearchDeals searchDeals;
	
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
		Set<Deals> deals = searchDeals.getDeals(keyword,category,location,emailId);		
		MainModel mainModel = new MainModel();
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
		Set<Deals> deals = searchDeals.getUserDeals(emailId);		
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
		Set<Deals> deals = searchDeals.getUserContactAndCommunityDeals(emailId);	
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
		List<Business> businessList = nonDealSuggestionSearch.search(userEmail.trim(), searchText.trim(), category.trim(), location.trim());
		MainModel model = new MainModel();
		model.setBusinessList(businessList);
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
		List<Page> pageList = communitySearch.search(userEmail.trim());
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
		List<Deals> dealList = userDealsSearch.search(userEmail.trim());
		MainModel model = new MainModel();
		model.setDealList(dealList);
		modelAndView.addObject("mainModel", model);
		return modelAndView;
	}
	
	
	
	
}
