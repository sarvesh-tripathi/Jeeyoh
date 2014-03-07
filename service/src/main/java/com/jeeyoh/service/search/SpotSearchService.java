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
		
		// Get Exact matched results
		exactMatchingSearchResults = getSearchResults(businessList, dealsList, pageList, eventsList, exactMatchingSearchResults);

		int limit = 0;
		if(businessList.size() < 8)
		{
			limit = 8 - businessList.size();
			businessList = businessDAO.getBusinessByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchBusinessCount(),limit);
			searchResponse.setLikeMatchBusinessCount(businessList.size());
		}
			
		if(dealsList.size() < 8)
		{
			limit = 8 - dealsList.size();
			dealsList = dealsDAO.getDealsByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchDealCount(),limit);
			searchResponse.setLikeMatchDealCount(dealsList.size());
		}
			
		if(pageList.size() < 8)
		{
			limit = 8 - pageList.size();
			pageList = eventsDAO.getCommunityByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchCommunityCount(),limit);
			searchResponse.setLikeMatchCommunityCount(pageList.size());
		}
			
		if(eventsList.size() < 8)
		{
			limit = 32 - (exactMatchingSearchResults.size() + businessList.size() + dealsList.size() +pageList.size());
			eventsList = eventsDAO.getEventsByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchEventCount(),limit);
			searchResponse.setLikeMatchEventCount(eventsList.size());
		}
			
		logger.debug("dealsList11::  "+dealsList.size());
		logger.debug("businessList11::  "+businessList.size());		
		logger.debug("pageList11::  "+pageList.size());
		logger.debug("eventsList11::  "+eventsList.size());
		logger.debug("getExactMatchBusinessCount::  "+searchResponse.getExactMatchBusinessCount());		
		logger.debug("getExactMatchCommunityCount::  "+searchResponse.getExactMatchCommunityCount());
		logger.debug("getExactMatchDealCount::  "+searchResponse.getExactMatchDealCount());
		logger.debug("getExactMatchEventCount::  "+searchResponse.getExactMatchEventCount());
		logger.debug("getExactMatchBusinessCount::  "+searchResponse.getLikeMatchBusinessCount());		
		logger.debug("getExactMatchCommunityCount::  "+searchResponse.getLikeMatchCommunityCount());
		logger.debug("getExactMatchDealCount::  "+searchResponse.getLikeMatchDealCount());
		logger.debug("getExactMatchEventCount::  "+searchResponse.getLikeMatchEventCount());

		
		// Get Like matched results
		likeSearchResults = getSearchResults(businessList, dealsList, pageList, eventsList,likeSearchResults);
		exactMatchingSearchResults.addAll(likeSearchResults);

		logger.debug("exactMatchingSearchResults::  "+exactMatchingSearchResults.size());
		searchResponse.setSearchResult(exactMatchingSearchResults);
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
	private List<SearchResult> getSearchResults(List<Business> businessList, List<Deals> dealsList, List<Page> pageList, List<Events> eventsList, List<SearchResult> searchResultList)
	{
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
				searchResult.setImageUrl(deals.getLargeImageUrl());
				searchResult.setWebsiteUrl(deals.getDealUrl());
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
				searchResultList.add(searchResult);
			}
		}

		// Add results for Community
		if(eventsList != null)
		{
			for(Events events : eventsList)
			{
				SearchResult searchResult = new SearchResult();
				searchResult.setId(events.getEventId());
				searchResult.setCity(events.getCity());
				searchResult.setName(events.getDescription());
				searchResult.setType("Event");
				searchResult.setWebsiteUrl(events.getUrlpath());
				searchResultList.add(searchResult);
			}
		}
		return searchResultList;
	}
}
