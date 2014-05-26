package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.Date;
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
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Dealredemptionlocation;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.utils.Utils;

@Component("spotSearch")
public class SpotSearchService implements ISpotSearchService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IBusinessDAO businessDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Autowired
	private IDealsDAO dealsDAO;

	@Autowired
	private IUserDAO userDAO;

	@Override
	@Transactional
	public SearchResponse search(SearchRequest searchRequest) {

		int totalCount = 0;
		int count = 0;
		//Getting total number of results
		if(searchRequest.getExactMatchBusinessCount() == 0 && searchRequest.getExactMatchDealCount() == 0 && searchRequest.getExactMatchEventCount() == 0 && searchRequest.getExactMatchCommunityCount() == 0 && 
				searchRequest.getLikeMatchBusinessCount() == 0 && searchRequest.getLikeMatchCommunityCount() == 0 && searchRequest.getLikeMatchDealCount() == 0 && searchRequest.getLikeMatchEventCount() == 0)
		{
			count = businessDAO.getTotalBusinessBySearchKeyWord(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
			logger.debug("businesstotalCount::  "+count);
			totalCount += count;

			count = dealsDAO.getTotalDealsBySearchKeyWord(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
			logger.debug("dealtotalCount::  "+count);
			totalCount += count;

			count = eventsDAO.getTotalEventsBySearchKeyWord(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
			logger.debug("eventtotalCount::  "+count);
			totalCount += count;

			count = eventsDAO.getTotalCommunityBySearchKeyWordForBusiness(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
			logger.debug("pagetotalCount for business::  "+count);
			totalCount += count;
			if(searchRequest.getLocation() != null && !searchRequest.getLocation().trim().equals(""))
			{
				count = eventsDAO.getTotalCommunityBySearchKeyWordForEvent(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation());
				logger.debug("pagetotalCount::  "+count);
				totalCount += count;
				logger.debug("totalCount pagetotalCount::  "+totalCount);
			}
		}


		logger.debug("totalCount::  "+totalCount);

		SearchResponse searchResponse = new SearchResponse();
		List<SearchResult> exactMatchingSearchResults = new ArrayList<SearchResult>();
		List<SearchResult> likeSearchResults = new ArrayList<SearchResult>();
		List<Business> businessList = businessDAO.getBusinessBySearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getExactMatchBusinessCount(),8);
		List<Deals> dealsList = dealsDAO.getDealsBySearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getExactMatchDealCount(),8);

		List<Page> pageList = new ArrayList<Page>();
		pageList = eventsDAO.getCommunityBySearchKeywordForBusiness(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getExactMatchCommunityCount(),8);
		searchResponse.setExactMatchCommunityCount(searchRequest.getExactMatchCommunityCount() + pageList.size());
		if(searchRequest.getLocation() != null && !searchRequest.getLocation().trim().equals(""))
		{
			int limit = 0;
			if(pageList.size() < 8)
			{
				limit = 8 - pageList.size();
				List<Page> eventsPageList = eventsDAO.getCommunityBySearchKeywordForEvents(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getExactMatchEventCommunityCount(),limit);

				if(eventsPageList != null)
				{
					searchResponse.setExactMatchEventCommunityCount(searchRequest.getExactMatchEventCommunityCount() + eventsPageList.size());
					pageList.addAll(eventsPageList);
				}
			}

		}
		List<Events> eventsList = eventsDAO.getEventsBySearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getExactMatchEventCount(),8);
		logger.debug("dealsList::  "+dealsList.size());
		logger.debug("businessList::  "+businessList.size());		
		logger.debug("pageList::  "+pageList.size());
		logger.debug("eventsList::  "+eventsList.size());

		searchResponse.setExactMatchBusinessCount(searchRequest.getExactMatchBusinessCount() + businessList.size());
		searchResponse.setExactMatchDealCount(searchRequest.getExactMatchDealCount() + dealsList.size());
		searchResponse.setExactMatchEventCount(searchRequest.getExactMatchEventCount() + eventsList.size());

		List<Business> LikeMatchBusinessList = new ArrayList<Business>();
		List<Deals> likeMatchDealsList = new ArrayList<Deals>();
		List<Page> likematchPageList = new ArrayList<Page>();
		List<Events> likeMatchEventsList = new ArrayList<Events>();

		// Get Exact matched results
		exactMatchingSearchResults = getSearchResults(searchRequest.getUserId(), businessList, dealsList, pageList, eventsList, exactMatchingSearchResults,searchRequest.getCategory());

		if(searchRequest.getSearchText() != null && !searchRequest.getSearchText().trim().equals(""))
		{
			int limit = 0;
			if(businessList.size() < 8)
			{
				limit = 8 - businessList.size();
				LikeMatchBusinessList = businessDAO.getBusinessByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchBusinessCount(),limit);
				searchResponse.setLikeMatchBusinessCount(searchRequest.getLikeMatchBusinessCount() + LikeMatchBusinessList.size());
			}

			if(dealsList.size() < 8)
			{
				limit = 8 - dealsList.size();
				likeMatchDealsList = dealsDAO.getDealsByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchDealCount(),limit);
				searchResponse.setLikeMatchDealCount(searchRequest.getLikeMatchDealCount() + likeMatchDealsList.size());
			}

			if(pageList.size() < 8)
			{
				limit = 8 - pageList.size();
				likematchPageList = eventsDAO.getCommunityByLikeSearchKeywordForBusiness(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchCommunityCount(),limit);
				searchResponse.setLikeMatchCommunityCount(searchRequest.getLikeMatchCommunityCount() + likematchPageList.size());
				if(searchRequest.getLocation() != null && !searchRequest.getLocation().trim().equals(""))
				{
					if(likematchPageList.size() < 8)
					{
						limit = 8 - likematchPageList.size();
						List<Page> eventsPageList = eventsDAO.getCommunityByLikeSearchKeywordForEvents(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchEventCommunityCount(),limit);
						if(eventsPageList != null)
						{
							likematchPageList.addAll(eventsPageList);
							searchResponse.setLikeMatchEventCommunityCount(searchRequest.getLikeMatchEventCommunityCount() + eventsPageList.size());
						}
					}
				}
			}

			if(eventsList.size() < 8)
			{
				limit = 32 - (exactMatchingSearchResults.size() + LikeMatchBusinessList.size() + likeMatchDealsList.size() +likematchPageList.size());
				likeMatchEventsList = eventsDAO.getEventsByLikeSearchKeyword(searchRequest.getSearchText(),searchRequest.getCategory(),searchRequest.getLocation(),searchRequest.getLikeMatchEventCount(),limit);
				searchResponse.setLikeMatchEventCount(searchRequest.getLikeMatchEventCount() + likeMatchEventsList.size());
			}

			// Get Like matched results
			likeSearchResults = getSearchResults(searchRequest.getUserId(), LikeMatchBusinessList, likeMatchDealsList, likematchPageList, likeMatchEventsList,likeSearchResults,searchRequest.getCategory());
			exactMatchingSearchResults.addAll(likeSearchResults);
		}

		logger.debug("dealsList11::  "+likeMatchDealsList.size());
		logger.debug("businessList11::  "+LikeMatchBusinessList.size());		
		logger.debug("pageList11::  "+likematchPageList.size());
		logger.debug("eventsList11::  "+likeMatchEventsList.size());
		logger.debug("getExactMatchBusinessCount::  "+searchResponse.getExactMatchBusinessCount());		
		logger.debug("getExactMatchCommunityCount::  "+searchResponse.getExactMatchCommunityCount());
		logger.debug("getExactMatchEventCommunityCount::  "+searchResponse.getExactMatchEventCommunityCount());
		logger.debug("getExactMatchDealCount::  "+searchResponse.getExactMatchDealCount());
		logger.debug("getExactMatchEventCount::  "+searchResponse.getExactMatchEventCount());
		logger.debug("getLikeBusinessCount::  "+searchResponse.getLikeMatchBusinessCount());		
		logger.debug("getLikeCommunityCount::  "+searchResponse.getLikeMatchCommunityCount());
		logger.debug("getLikeEventCommunityCount::  "+searchResponse.getLikeMatchEventCommunityCount());
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
	private List<SearchResult> getSearchResults(int userId, List<Business> businessList, List<Deals> dealsList, List<Page> pageList, List<Events> eventsList, List<SearchResult> searchResultList, String category)
	{
		// Add results for Events
		if(eventsList != null)
		{
			for(Events events : eventsList)
			{
				SearchResult searchResult = new SearchResult();
				// Check if event is already favorite or not
				Eventuserlikes eventProperty = userDAO.getUserEventProperties(userId, events.getEventId());
				if(eventProperty != null)
				{
					searchResult.setIsFavorite(eventProperty.getIsFavorite());
					searchResult.setIsBooked(eventProperty.getIsBooked());
				}
				Page page = events.getPage();
				/*List<CommunityReview> communityReviewList = eventsDAO.getCommunityReviewByPageId(page.getPageId());
				int rating = 0;
				int count = 0;
				if(communityReviewList != null && !communityReviewList.isEmpty())
				{
					for(CommunityReview communityReview:communityReviewList)
					{
						rating = rating + communityReview.getRating();
						count++;
					}
					double avg = (double)rating/count;
					searchResult.setRating(avg);
				}*/
				double avgRating = eventsDAO.getCommunityReviewByPageId(events.getPage().getPageId());
				searchResult.setRating(avgRating);
				searchResult.setId(events.getEventId());
				searchResult.setCity(events.getCity());
				searchResult.setName(events.getTitle());
				searchResult.setType("Event");
				searchResult.setStartDate(events.getEvent_date_local().toString());
				searchResult.setEndDate(events.getEvent_date_local().toString());
				//searchResult.setImageUrl(events.getImage_url());
				searchResult.setWebsiteUrl(events.getUrlpath());
				searchResult.setCategory(page.getPagetype().getPageType());
				searchResult.setSource(events.getEventSource());
				searchResult.setTimeLine(Utils.getTimeLineForEvent(events.getEvent_date_local(),events.getEvent_time_local()));
				searchResult.setTimeSlot(events.getEvent_time_local());
				String address = events.getVenue_name()+"\n"+events.getCity()+","+events.getState()+" "+events.getZip();
				searchResult.setAddress(address);
				searchResultList.add(searchResult);
			}
		}

		// Add results for Deals
		if(dealsList != null)
		{
			for(Deals deals : dealsList)
			{
				Business business = deals.getBusiness();
				SearchResult searchResult = new SearchResult();
				// Check if deal is already favorite or not
				Dealsusage dealProperty = userDAO.getUserDealProperties(userId, deals.getId());
				if(dealProperty != null)
				{
					searchResult.setIsFavorite(dealProperty.getIsFavorite());
					searchResult.setIsBooked(dealProperty.getIsBooked());
				}
					
				searchResult.setId(deals.getId());
				searchResult.setCity(deals.getBusiness().getCity());
				searchResult.setName(deals.getTitle());
				searchResult.setType("Deal");
				searchResult.setStartDate(deals.getStartAt().toString());
				searchResult.setEndDate(deals.getEndAt().toString());
				searchResult.setImageUrl(deals.getLargeImageUrl());
				searchResult.setWebsiteUrl(deals.getDealUrl());
				searchResult.setCategory(category);
				searchResult.setRating(business.getRating());
				searchResult.setSource(deals.getDealSource());
				searchResult.setMerhcantName(business.getName());

				//Get price and discount
				Dealoption dealoption = dealsDAO.getDealOptionByDealId(deals.getId());
				if(dealoption != null)
				{
					if(dealoption.getFormattedOriginalPrice() != null)
						searchResult.setPrice(dealoption.getFormattedOriginalPrice());
					else
						searchResult.setPrice("$"+dealoption.getOriginalPrice());
					searchResult.setDiscount(dealoption.getDiscountPercent());

					//Get Address of redemption location
					logger.debug("getting location..........");
					if(dealoption.getDealredemptionlocations() != null && !dealoption.getDealredemptionlocations().isEmpty())
					{
						Dealredemptionlocation dealredemptionlocation = (Dealredemptionlocation)dealoption.getDealredemptionlocations().iterator().next();
						logger.debug("getting location.........." + dealredemptionlocation);
						if(dealredemptionlocation != null)
						{
							String address = dealredemptionlocation.getName()+"\n"+dealredemptionlocation.getStreetAddress1()+"\n"+dealredemptionlocation.getCity()+","+dealredemptionlocation.getState()+" "+dealredemptionlocation.getPostalCode();
							searchResult.setAddress(address);
						}
					}
				}

				searchResultList.add(searchResult);
			}
		}

		// Add results for Community
		if(pageList != null)
		{
			for(Page page : pageList)
			{
				SearchResult searchResult = new SearchResult();
				//Get recent event date for community
				Object[] event_date = eventsDAO.getRecentEventDate(page.getPageId());
				if(event_date != null)
				{
					Date date = (Date)event_date[0];
					searchResult.setStartDate(date.toString());
					searchResult.setEndDate(date.toString());
					searchResult.setTimeLine(Utils.getTimeLineForEvent(date,event_date[1].toString()));
					searchResult.setTimeSlot(event_date[1].toString());
				}
				// Check if business is already favorite or not
				Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(userId, page.getPageId());
				if(pageuserlikes != null)
				{
					searchResult.setIsFavorite(pageuserlikes.getIsFavorite());
					searchResult.setIsBooked(pageuserlikes.getIsBooked());
				}
					
				searchResult.setId(page.getPageId());
				/*List<CommunityReview> communityReviewList = eventsDAO.getCommunityReviewByPageId(page.getPageId());
				int rating = 0;
				int count = 0;
				if(communityReviewList != null && !communityReviewList.isEmpty())
				{
					for(CommunityReview communityReview:communityReviewList)
					{
						rating = rating + communityReview.getRating();
						count++;
					}
					double avg = (double)rating/count;
					logger.debug("avg rating =>"+avg);
					searchResult.setRating(avg);
				}
				else
					searchResult.setRating(0);*/
				double avgRating = eventsDAO.getCommunityReviewByPageId(page.getPageId());
				searchResult.setRating(avgRating);
				if(page.getBusiness() != null)
					searchResult.setCity(page.getBusiness().getCity());

				searchResult.setName(page.getAbout());
				searchResult.setType("Community");
				searchResult.setImageUrl(page.getProfilePicture());
				searchResult.setWebsiteUrl(page.getPageUrl());
				searchResult.setCategory(page.getPagetype().getPageType());
				searchResult.setSource(page.getSource());
				searchResultList.add(searchResult);
			}
		}


		// Add results for business
		if(businessList != null)
		{
			for(Business business : businessList)
			{
				SearchResult searchResult = new SearchResult();
				Page page = eventsDAO.getPageByBusinessId(business.getId());
				if(page != null)
				{
					// Check if business is already favorite or not
					Pageuserlikes pageuserlikes = userDAO.getUserPageProperties(userId, page.getPageId());
					if(pageuserlikes != null)
					{
						searchResult.setIsFavorite(pageuserlikes.getIsFavorite());
						searchResult.setIsBooked(pageuserlikes.getIsBooked());
					}
				}

				searchResult.setId(business.getId());
				searchResult.setCity(business.getCity());
				searchResult.setName(business.getName());
				searchResult.setType("Business");
				searchResult.setImageUrl(business.getImageUrl());
				searchResult.setWebsiteUrl(business.getWebsiteUrl());
				searchResult.setCategory(category);
				searchResult.setRating(business.getRating());
				searchResult.setSource(business.getSource());
				if(business.getDisplayAddress() != null)
					searchResult.setAddress(business.getDisplayAddress().replaceAll("[<>\\[\\],-]", ""));
				searchResultList.add(searchResult);
			}
		}

		return searchResultList;
	}
}
