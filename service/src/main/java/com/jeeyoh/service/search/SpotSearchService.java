package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	public List<SearchResult> search(SearchRequest searchRequest) {
		List<SearchResult> exactMatchingSearchResults = new ArrayList<SearchResult>();
		List<SearchResult> likeSearchResults = new ArrayList<SearchResult>();
		List<Business> businessList = businessDAO.getBusinessBySearchKeyword(searchRequest.getSearchText());
		List<Deals> dealsList = dealsDAO.getDealsBySearchKeyword(searchRequest.getSearchText());
		List<Page> pageList = eventsDAO.getCommunityBySearchKeyword(searchRequest.getSearchText());
		List<Events> eventsList = eventsDAO.getEventsBySearchKeyword(searchRequest.getSearchText());
		logger.debug("pageList::  "+pageList.size());
		logger.debug("eventsList::  "+eventsList.size());
		
		// Get Exact matched results
		exactMatchingSearchResults = getSearchResults(businessList, dealsList, pageList, eventsList, exactMatchingSearchResults);

		businessList = businessDAO.getBusinessByLikeSearchKeyword(searchRequest.getSearchText());
		dealsList = dealsDAO.getDealsByLikeSearchKeyword(searchRequest.getSearchText());
		pageList = eventsDAO.getCommunityByLikeSearchKeyword(searchRequest.getSearchText());
		eventsList = eventsDAO.getEventsByLikeSearchKeyword(searchRequest.getSearchText());
		logger.debug("pageList1111::  "+pageList.size());
		logger.debug("eventsList::  "+eventsList.size());
		
		// Get Like matched results
		likeSearchResults = getSearchResults(businessList, dealsList, pageList, eventsList,likeSearchResults);
		exactMatchingSearchResults.addAll(likeSearchResults);
		return exactMatchingSearchResults;
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
				searchResultList.add(searchResult);
			}
		}
		return searchResultList;
	}
}
