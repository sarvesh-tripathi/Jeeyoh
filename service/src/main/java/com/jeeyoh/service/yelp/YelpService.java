package com.jeeyoh.service.yelp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.yelp.YelpBusiness;
import com.jeeyoh.model.yelp.YelpBusinessResponse;
import com.jeeyoh.model.yelp.YelpDeal;
import com.jeeyoh.model.yelp.YelpDealOption;
import com.jeeyoh.model.yelp.YelpLocation;
import com.jeeyoh.model.yelp.YelpReview;
import com.jeeyoh.model.yelp.YelpSearchResponse;
import com.jeeyoh.persistence.dao.yelp.ICountryLocationDAO;
import com.jeeyoh.persistence.dao.yelp.IYelpDAO;
import com.jeeyoh.persistence.domain.Countrylocation;
import com.jeeyoh.persistence.domain.Ybusiness;
import com.jeeyoh.persistence.domain.Ybusinesscategorymap;
import com.jeeyoh.persistence.domain.Ybusinessregionmap;
import com.jeeyoh.persistence.domain.Ycategoryfilter;
import com.jeeyoh.persistence.domain.Ydeal;
import com.jeeyoh.persistence.domain.Ydealoption;
import com.jeeyoh.persistence.domain.Yregion;
import com.jeeyoh.persistence.domain.Yreview;

@Component("yelpService")
public class YelpService implements IYelpService {

	@Autowired
	private IYelpClient yelpClient; 
	
	@Autowired
	private ICountryLocationDAO countryLocationDAO;
	
	@Autowired
	private IYelpDAO yelpDAO;
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	
	@Override
	@Transactional
	public void search() {
		//YelpSearchResponse searchResponse = yelpClient.search("Los Angeles");
		//loadData(searchResponse);		
		List<Countrylocation> locationList = countryLocationDAO.getCountryLocations("USA");
		if(locationList != null) {
			
			//for(Countrylocation location : locationList) {
			for(int i = 0; i < locationList.size(); i++)
			{
				if(i == 0) {
					Countrylocation location = locationList.get(i);				
					if(location != null)
					{
						String city = location.getCity();
						logger.debug("CITY ::::"+city);
						YelpSearchResponse searchResponse = yelpClient.search(city);					
						if(searchResponse != null) {
							loadData(searchResponse);
							long total = searchResponse.getTotal();
							logger.debug("Total Record : : "+total);
							if(total > 20)
							{
								for(int offset = 20; offset <= 1000;) {
									searchResponse = yelpClient.search(city, offset+"");
									if(searchResponse != null)
									{
										loadData(searchResponse);
									}
									offset += 20;
								}
							}
						}									
					}
				}
			}
		}
	}
	
	//@Transactional
	private void loadData(YelpSearchResponse searchResponse) {
		
		/*try 
		   {
			    
			     ObjectMapper mapper = new ObjectMapper();
			     //System.out.println(mapper.writeValueAsString(locuMenuResponce));
			     logger.debug(mapper.writeValueAsString(searchResponse));
		   } 
		   catch (Exception e) 
		   {
			     e.printStackTrace();
			   
		   }*/
		List<YelpBusiness> businesses = searchResponse.getBusinesses();
		Yregion region = new Yregion();
		if(searchResponse.getRegion() != null)
		{
			region.setLattitude(searchResponse.getRegion().getCenter().getLatitude());
			region.setLongitude(searchResponse.getRegion().getCenter().getLongitude());
			region.setLattitudeDelta(searchResponse.getRegion().getSpan().getLattitudeDelta());
			region.setLongitudeDelta(searchResponse.getRegion().getSpan().getLongitudeDelta());
		}
		else
		{
			region.setLattitude(45.5);
			region.setLongitude(45.5);
			region.setLattitudeDelta(45.5);
			region.setLongitudeDelta(45.5);
		}
		Ybusinessregionmap regionBusinessMap = new Ybusinessregionmap();
		regionBusinessMap.setYregion(region);
		int count = 0;
		if(businesses != null) {
			logger.debug("In Business :::: ");
			for(YelpBusiness businessModel : businesses) {
				Ybusiness business = new Ybusiness();
				business.setBusinessId(businessModel.getBusinessId());
				business.setIsClaimed(businessModel.isClaimed());
				business.setIsClosed(businessModel.isClosed());
				business.setRating((long)businessModel.getRating());
				business.setMobileUrl(businessModel.getMobileUrl());
				business.setRatingImgUrl(businessModel.getRatingImageUrl());
				business.setReviewCount(businessModel.getReviewCount()+"");
				business.setName(businessModel.getName());
				business.setSnippetImageUrl(businessModel.getSnippetImageUrl());
				business.setRatingLargeImgUrl(businessModel.getRatingLargeImageUrl());
				business.setRatingSmallImgUrl(businessModel.getRatingSmallImageUrl());
				business.setUrl(businessModel.getUrl());
				business.setPhone(businessModel.getPhone());
				business.setImageUrl(businessModel.getImageUrl());
				business.setDisplayPhone(business.getDisplayPhone());
				YelpLocation location = businessModel.getLocation();
				if(location != null) {
					if(location.getCrossStreets() != null)
					{
						business.setCrossStreets(location.getCrossStreets());
					}
					if(location.getCity() != null)
					{
						business.setCity(location.getCity());
					}
					if(location.getAddress() != null)
					{
						business.setAddress(location.getAddress().toString());
					}
					if(location.getDisplayAddress() != null)
					{
						business.setDisplayAddress(location.getDisplayAddress().toString());
					}		
					
					if(location.getNeighborhoods()!= null)
					{
						business.setNeighborhoods(location.getNeighborhoods().toString());
					}
					else{
						business.setNeighborhoods("Default");
					}
					if(location.getPostalCode() != null)
					{
						business.setPostalCode(location.getPostalCode());
					}
					if(location.getCountryCode() != null)
					{
						business.setCountryCode(location.getCountryCode());
					}
					if(location.getStateCode() != null)
					{
						business.setStateCode(location.getStateCode());
					}
					
				}				
				regionBusinessMap.setYbusiness(business);
				Set<Ybusinessregionmap> regionBusinessSet = new HashSet<Ybusinessregionmap>();
				regionBusinessSet.add(regionBusinessMap);
				business.setYbusinessregionmaps(regionBusinessSet);
				logger.debug("Category :::: ");
				List<List<String>> categories = businessModel.getCategories();
				if(categories != null)
				{
					
					
					Set<Ybusinesscategorymap> businessCategoryMaps = new HashSet<Ybusinesscategorymap>();
					for(List<String> category : categories) {
						if(category != null) {
							Ybusinesscategorymap categoryMap = new Ybusinesscategorymap();
							//Ycategoryfilter categoryFilter = new Ycategoryfilter();
							//logger.debug("YCategory Object ::: === >"+category);
							logger.debug("YCategory Object ::: === >"+category.get(0));
							Ycategoryfilter categoryFilter =  yelpDAO.getCategories(category.get(0));
							logger.debug("YCategory Object222222 ::: === >"+categoryFilter);
							//categoryFilter.setCategory(category.get(0));
							//categoryFilter.setValue(category.get(1));
							//categoryFilter.setYcategoryfilter(categoryFilter);
							categoryMap.setYcategoryfilter(categoryFilter);
							categoryMap.setYbusiness(business);
							businessCategoryMaps.add(categoryMap);
						}
					}
					business.setYbusinesscategorymaps(businessCategoryMaps);
					yelpDAO.saveBusiness(business,count);
					count++;
				}
			}
		}
	}

	@Override
	@Transactional
	public void searchBusiness() {
		// TODO Auto-generated method stub
		logger.debug("IN Business ==> ");
		List<Ybusiness> businessList = yelpDAO.getBusiness();
		if(businessList != null)
		{
			int count = 0;
			for(Ybusiness business : businessList)
			{
				if(business != null)
				{
					String businessId = business.getBusinessId();
					logger.debug("B ID "+businessId);
					YelpBusinessResponse searchResponse = yelpClient.business(businessId);
					if(searchResponse != null)
					{
						if(count > 20)
						{
							loadYelpDeals(searchResponse,business);
							loadYelpReviews(searchResponse,business);
						}
						count++ ;
					}
					/*if(count == 20)
					{
						break;
					}*/
				}
			}
			
		}

	}
	
	private void loadYelpDeals(YelpBusinessResponse searchResponse, Ybusiness business)
	{
		List<YelpDeal> deals = searchResponse.getDeals();
		if(deals != null)
		{
			logger.debug("loadDeals ::: "+deals.get(0).getCurrencyCode());
			int count = 0;
			for(YelpDeal deal:deals)
			{
				Ydeal ydeal = new Ydeal();
				ydeal.setCurrencyCode(deal.getCurrencyCode());
				ydeal.setDealImageUrl(deal.getImageUrl());
				ydeal.setDealUrl(deal.getUrl());
				ydeal.setDealTitle(deal.getTitle());
				ydeal.setDealStartTime(deal.getStartTime()+"");
				ydeal.setIsPopular(deal.isPopular());
				Ydealoption option = new Ydealoption();
				List<YelpDealOption> optionsList = deal.getOptions();
				Set<Ydealoption> dealsSet = new HashSet<Ydealoption>();
				for(YelpDealOption dealoption : optionsList)
				{
					Ydealoption ydealoption = new Ydealoption();
					ydealoption.setFormattedOriginalPrice(dealoption.getFormattedOrgPrice());
					ydealoption.setFormattedPrice(dealoption.getFormattedPrice());
					ydealoption.setIsLimitedQuantity(dealoption.isLimitedQuantity());
					ydealoption.setOriginalPrice(dealoption.getOrgPrice());
					ydealoption.setPrice(dealoption.getPrice());
					ydealoption.setPurchaseUrl(dealoption.getPurchaseUrl());
					ydealoption.setRemainingCount(dealoption.getRemainingCount()+"");
					ydealoption.setTitle(dealoption.getTitle());
					ydealoption.setYdeal(ydeal);	
					dealsSet.add(ydealoption);
				}
				ydeal.setYbusiness(business);
				ydeal.setYdealoptions(dealsSet);
				count++;
				yelpDAO.saveDeals(ydeal,count);
			}
		}

	}
	
	private void loadYelpReviews(YelpBusinessResponse searchResponse, Ybusiness business)
	{
		logger.debug("In Review ::: ");
		List<YelpReview> reviews = searchResponse.getReviews();
		if(reviews != null)
		{
			   int count = 0;
				for(YelpReview review : reviews)
				{
					Yreview yreview = new Yreview();
					yreview.setCreationTime(review.getCreatedTime()+"");
					yreview.setExcerpt(review.getExcerpt());
					yreview.setRating(review.getRating());
					yreview.setRatingImageUrl(review.getRatingImgUrl());
					yreview.setRatingLargeImageUrl(review.getRatingLargeImgUrl());
					yreview.setRatingSmallImageUrl(review.getRatingSmallImgUrl());
					yreview.setReviewId(review.getExcerpt());
					yreview.setUserId(review.getUser().getId());
					yreview.setUserImageUrl(review.getUser().getImageUrl());
					yreview.setUserName(review.getUser().getName());
					yreview.setYbusiness(business);
					count++;
					yelpDAO.saveReviews(yreview, count);
					
				}
		}
		
	}

}
