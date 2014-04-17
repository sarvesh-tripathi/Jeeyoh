package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.response.SearchResponse;
import com.jeeyoh.model.search.SearchRequest;
import com.jeeyoh.model.search.SearchResult;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Page;

@Component("spotSearch")
public class SpotSearchService implements ISpotSearchService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IBusinessDAO businessDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Autowired
	private IDealsDAO dealsDAO;

	@Override
	@Transactional
	public SearchResponse search(SearchRequest searchRequest) {

		int totalCount = 0;
		int count = 0;
		//Getting total number of results
		if(searchRequest.getExactMatchBusinessCount() == 0)
		{
			count = businessDAO.getTotalBusinessBySearchKeyWord(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
			logger.debug("businesstotalCount::  "+count);
			totalCount += count;
		}

		if(searchRequest.getExactMatchDealCount() == 0)
		{
			count = dealsDAO.getTotalDealsBySearchKeyWord(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
			logger.debug("dealtotalCount::  "+count);
			totalCount += count;
		}

		if(searchRequest.getExactMatchEventCount() == 0)
		{
			count = eventsDAO.getTotalEventsBySearchKeyWord(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
			logger.debug("eventtotalCount::  "+count);
			totalCount += count;
		}

		if(searchRequest.getExactMatchCommunityCount() == 0)
		{
			count = eventsDAO.getTotalCommunityBySearchKeyWord(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
			logger.debug("pagetotalCount::  "+count);
			totalCount += count;
		}

		logger.debug("totalCount::  "+totalCount);

		SearchResponse searchResponse = new SearchResponse();
		List<SearchResult> exactMatchingSearchResults = new ArrayList<SearchResult>();
		List<SearchResult> likeSearchResults = new ArrayList<SearchResult>();
		List<Business> businessList = businessDAO.getBusinessBySearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getExactMatchBusinessCount(),8);
		List<Deals> dealsList = dealsDAO.getDealsBySearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getExactMatchDealCount(),8);
		List<Page> pageList = eventsDAO.getCommunityBySearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getExactMatchCommunityCount(),8);
		List<Events> eventsList = eventsDAO.getEventsBySearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getExactMatchEventCount(),8);
		logger.debug("dealsList::  "+dealsList.size());
		logger.debug("businessList::  "+businessList.size());		
		logger.debug("pageList::  "+pageList.size());
		logger.debug("eventsList::  "+eventsList.size());

		searchResponse.setExactMatchBusinessCount(businessList.size());
		searchResponse.setExactMatchCommunityCount(pageList.size());
		searchResponse.setExactMatchDealCount(dealsList.size());
		searchResponse.setExactMatchEventCount(eventsList.size());

		List<Business> LikeMatchBusinessList = new ArrayList<Business>();
		List<Deals> likeMatchDealsList = new ArrayList<Deals>();
		List<Page> likematchPageList = new ArrayList<Page>();
		List<Events> likeMatchEventsList = new ArrayList<Events>();

		// Get Exact matched results
		exactMatchingSearchResults = getSearchResults(businessList, dealsList, pageList, eventsList, exactMatchingSearchResults,searchRequest.getCategory());

		if(searchRequest.getSearchText() != null && !searchRequest.getSearchText().trim().equals(""))
		{
			int limit = 0;
			if(businessList.size() < 8)
			{
				limit = 8 - businessList.size();
				LikeMatchBusinessList = businessDAO.getBusinessByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchBusinessCount(),limit);
				searchResponse.setLikeMatchBusinessCount(LikeMatchBusinessList.size());
			}

			if(dealsList.size() < 8)
			{
				limit = 8 - dealsList.size();
				likeMatchDealsList = dealsDAO.getDealsByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchDealCount(),limit);
				searchResponse.setLikeMatchDealCount(likeMatchDealsList.size());
			}

			if(pageList.size() < 8)
			{
				limit = 8 - pageList.size();
				likematchPageList = eventsDAO.getCommunityByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchCommunityCount(),limit);
				searchResponse.setLikeMatchCommunityCount(likematchPageList.size());
			}

			if(eventsList.size() < 8)
			{
				limit = 32 - (exactMatchingSearchResults.size() + LikeMatchBusinessList.size() + likeMatchDealsList.size() +likematchPageList.size());
				likeMatchEventsList = eventsDAO.getEventsByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchEventCount(),limit);
				searchResponse.setLikeMatchEventCount(likeMatchEventsList.size());
			}

			// Get Like matched results
			likeSearchResults = getSearchResults(LikeMatchBusinessList, likeMatchDealsList, likematchPageList, likeMatchEventsList,likeSearchResults,searchRequest.getCategory());
			exactMatchingSearchResults.addAll(likeSearchResults);
		}

		logger.debug("dealsList11::  "+likeMatchDealsList.size());
		logger.debug("businessList11::  "+LikeMatchBusinessList.size());		
		logger.debug("pageList11::  "+likematchPageList.size());
		logger.debug("eventsList11::  "+likeMatchEventsList.size());
		logger.debug("getExactMatchBusinessCount::  "+searchResponse.getExactMatchBusinessCount());		
		logger.debug("getExactMatchCommunityCount::  "+searchResponse.getExactMatchCommunityCount());
		logger.debug("getExactMatchDealCount::  "+searchResponse.getExactMatchDealCount());
		logger.debug("getExactMatchEventCount::  "+searchResponse.getExactMatchEventCount());
		logger.debug("getLikeBusinessCount::  "+searchResponse.getLikeMatchBusinessCount());		
		logger.debug("getLikeCommunityCount::  "+searchResponse.getLikeMatchCommunityCount());
		logger.debug("getLikeDealCount::  "+searchResponse.getLikeMatchDealCount());
		logger.debug("getLikeEventCount::  "+searchResponse.getLikeMatchEventCount());
		logger.debug("exactMatchingSearchResults::  "+exactMatchingSearchResults.size());
		searchResponse.setSearchResult(exactMatchingSearchResults);
		searchResponse.setTotalCount(totalCount);
		searchResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		searchResponse.setError("");

		return searchResponse;
	}


	/**
	 * This creates combined search result list from business,page and deals
	 * @param businessList
	 * @param dealsList
	 * @param pageList
	 * @param searchResultList
	 * @return
	 */
	private List<SearchResult> getSearchResults(List<Business> businessList, List<Deals> dealsList, List<Page> pageList, List<Events> eventsList, List<SearchResult> searchResultList, String category)
	{
		// Add results for Events
		if(eventsList != null)
		{
			for(Events events : eventsList)
			{
				SearchResult searchResult = new SearchResult();
				searchResult.setId(events.getEventId());
				searchResult.setCity(events.getCity());
				searchResult.setName(events.getTitle());
				searchResult.setType("Event");
				searchResult.setStartDate(events.getEvent_date().toString());
				searchResult.setEndDate(events.getEvent_date().toString());
				searchResult.setImageUrl(events.getImage_url());
				searchResult.setWebsiteUrl(events.getUrlpath());
				searchResult.setCategory(category);
				searchResultList.add(searchResult);
			}
		}

		// Add results for Deals
		if(dealsList != null)
		{
			for(Deals deals : dealsList)
			{
				SearchResult searchResult = new SearchResult();
				searchResult.setId(deals.getId());
				searchResult.setCity(deals.getBusiness().getCity());
				searchResult.setName(deals.getTitle());
				searchResult.setType("Deal");
				searchResult.setStartDate(deals.getStartAt().toString());
				searchResult.setEndDate(deals.getEndAt().toString());
				searchResult.setImageUrl(deals.getLargeImageUrl());
				searchResult.setWebsiteUrl(deals.getDealUrl());
				searchResult.setCategory(category);
				searchResultList.add(searchResult);
			}
		}

		// Add results for Community
		if(pageList != null)
		{
			for(Page page : pageList)
			{
				SearchResult searchResult = new SearchResult();
				searchResult.setId(page.getPageId());
				if(page.getBusiness() != null)
					searchResult.setCity(page.getBusiness().getCity());
				searchResult.setName(page.getAbout());
				searchResult.setType("Community");
				searchResult.setImageUrl(page.getProfilePicture());
				searchResult.setWebsiteUrl(page.getPageUrl());
				searchResult.setCategory(category);
				searchResultList.add(searchResult);
			}
		}


		// Add results for business
		if(businessList != null)
		{
			for(Business business : businessList)
			{
				SearchResult searchResult = new SearchResult();
				searchResult.setId(business.getId());
				searchResult.setCity(business.getCity());
				searchResult.setName(business.getName());
				searchResult.setType("Business");
				searchResult.setImageUrl(business.getImageUrl());
				searchResult.setWebsiteUrl(business.getWebsiteUrl());
				searchResult.setCategory(category);
				searchResultList.add(searchResult);
			}
		}

		return searchResultList;
	}
}
