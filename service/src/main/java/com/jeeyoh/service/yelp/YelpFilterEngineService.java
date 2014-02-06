package com.jeeyoh.service.yelp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.dao.yelp.IYelpDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Ybusiness;
import com.jeeyoh.persistence.domain.Ybusinesscategorymap;
import com.jeeyoh.persistence.domain.Ycategoryfilter;
import com.jeeyoh.persistence.domain.Ydeal;
import com.jeeyoh.persistence.domain.Ydealoption;

@Component("yelpFilterEngine")
public class YelpFilterEngineService implements IYelpFilterEngineService {
	@Autowired
	IYelpDAO yelpDAO;
	@Autowired
	IDealsDAO dealsDAO;

	@Autowired
	IBusinessDAO businessDAO;


	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Override
	@Transactional
	public void filterDeals() {
		// TODO Auto-generated method stub
		logger.debug("In Yelp Filter ");
		List<Ydealoption> ydealsoptiom = yelpDAO.filterdDealByDiscount();
		logger.debug("Deal List :::  "+ydealsoptiom.size());
		Set<Ydealoption> dealOptionSet = null;
		Ydeal ydeal = null;
		if(ydealsoptiom != null)
		{

			for (int i =0; i < ydealsoptiom.size(); i++)
			{
				Integer ydealId = ydealsoptiom.get(i).getYdeal().getId();

				if (i == 0) 
				{
					ydeal = ydealsoptiom.get(i).getYdeal();
					logger.debug("Ydeal ::: "+ydeal.getDealUrl());
					dealOptionSet = new HashSet<Ydealoption>();
					logger.debug("Ydeal ::: "+ydealsoptiom.get(i));
					dealOptionSet.add(ydealsoptiom.get(i));
				}
				else
				{
					int preydealId = ydealsoptiom.get(i - 1).getYdeal().getId();
					if (ydealId != preydealId)
					{ 
						Deals deal = new Deals();
						if(dealOptionSet != null)
						{
							ydeal.setYdealoptions(dealOptionSet);
						}

						if(ydeal.getAdditionalDeals() != null)
						{
							deal.setAdditionalDeals(ydeal.getAdditionalDeals());
						}
						if(ydeal.getAdditionalRestrictions() != null)
						{
							deal.setAdditionalRestrictions(ydeal.getAdditionalRestrictions());
						}
						deal.setIsPopular(ydeal.getIsPopular());
						deal.setTitle(ydeal.getDealTitle());
						logger.debug("Before save000000000 ::: "+ydeal.getDealUrl());

						deal.setDealUrl(ydeal.getDealUrl());
						if(ydeal.getId() != null)
						{
							logger.debug("IDD::: "+ydeal.getId());
							deal.setDealId(ydeal.getId()+"");
						}
						long timeStamp = Long.parseLong(ydeal.getDealStartTime());
						deal.setStartAt(new java.util.Date((long)timeStamp*1000));
						deal.setImpRestrictions(ydeal.getImpRestrictions());
						Set<Ydealoption> ydealoptions = ydeal.getYdealoptions();
						Set<Dealoption> dealOptionSet1 = new HashSet<Dealoption>();
						if(ydealoptions != null)
						{
							for(Ydealoption ydealoption: ydealoptions)
							{
								logger.debug("Ydeal option :: "+ydealoption.getOriginalPrice());
								Dealoption dealoption = new Dealoption();
								dealoption.setOriginalPrice(ydealoption.getOriginalPrice());
								dealoption.setPrice(ydealoption.getPrice());
								dealoption.setFormattedOriginalPrice(ydealoption.getFormattedOriginalPrice());
								dealoption.setFormattedPrice(ydealoption.getFormattedPrice());
								dealoption.setTitle(ydealoption.getTitle());
								dealoption.setDeals(deal);
								dealOptionSet1.add(dealoption);
							}
						}
						deal.setDealoptions(dealOptionSet1);
						Ybusiness yBusiness = ydeal.getYbusiness();
						List<Business> businessList = businessDAO.getBusinessById(yBusiness.getBusinessId());
						if(businessList != null)
						{
							if(businessList != null)
							{
								if(businessList.isEmpty())
								{
									Set<Ybusinesscategorymap>  ycategorymap   = yBusiness.getYbusinesscategorymaps();
									Businesstype businesstype =null;
									if(ycategorymap != null)
									{
										 for(Ybusinesscategorymap ycategorymap1:ycategorymap)
										 {
											 Ycategoryfilter  ycategoryfilter = ycategorymap1.getYcategoryfilter();
											 int parentId = ycategoryfilter.getYcategoryfilter().getId();
											 logger.debug("Parent id ::: "+parentId);
											 if(parentId >= 491 && parentId <= 608)
											 {
												 businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
												 break;
											 }
											 else if(parentId >= 109 && parentId <= 130)
											 {
												 businesstype = businessDAO.getBusinesstypeByType("SPA");
												 break;
											 }
											 else if(parentId >= 1 && parentId <= 66)
											 {
												 businesstype = businessDAO.getBusinesstypeByType("SPORT");
												 break;
											 }
											 else if(parentId == 72)
											 {
												 businesstype = businessDAO.getBusinesstypeByType("MOVIE");
												 break;
											 }
											 /*Ycategoryfilter  ycategoryfilter = ycategorymap1.getYcategoryfilter();
											 String name = ycategoryfilter.getCategory();
											 if(name.toLowerCase().contains("restaurants"))
											 {
												 businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
												 break;
											 }
											else if(name.toLowerCase().contains("sport"))
											 {
												businesstype = businessDAO.getBusinesstypeByType("SPORT");
												 break;
											 }
											else if(name.toLowerCase().contains("spas"))
											 {
												businesstype = businessDAO.getBusinesstypeByType("SPA");
												 break;
											}*/
										 }
									}
									//Businesstype businesstype = businessDAO.getBusinesstypeByType("OTHERS");
									Business business = new Business();
									business.setName(yBusiness.getName());
									business.setBusinessId(yBusiness.getBusinessId());
									business.setWebsiteUrl(yBusiness.getUrl());
									business.setAddress(yBusiness.getAddress());
									business.setCity(yBusiness.getCity());
									business.setCountryCode(yBusiness.getCountryCode());
									business.setCrossStreets(yBusiness.getCrossStreets());
									business.setDisplayAddress(yBusiness.getDisplayAddress());
									business.setDisplayPhone(yBusiness.getDisplayPhone());
									business.setDistance(yBusiness.getDistance());
									business.setImageUrl(yBusiness.getImageUrl());
									business.setIsActive(yBusiness.getIsClaimed());
									business.setIsClosed(yBusiness.getIsClosed());
									business.setMobileUrl(yBusiness.getMobileUrl());
									business.setMenuProvider(yBusiness.getMenuProvider());
									business.setMenuProviderDate(yBusiness.getMenuProviderDate());
									business.setName(yBusiness.getName());
									business.setNeighborhoods(yBusiness.getNeighborhoods());
									business.setPostalCode(yBusiness.getPostalCode());
									business.setRating(yBusiness.getRating());
									business.setRatingImgUrl(yBusiness.getRatingImgUrl());
									business.setSnippetImageUrl(yBusiness.getSnippetImageUrl());
									business.setSnippetText(yBusiness.getSnippetText());
									business.setStateCode(yBusiness.getStateCode());
									business.setBusinesstype(businesstype);
									business.setSource("Yelp");
									businessDAO.saveBusiness(business);
									businessList = businessDAO.getBusinessByIdForGroupon(business.getBusinessId());
									deal.setBusiness(businessList.get(0));
								}
								else
								{
									deal.setBusiness(businessList.get(0));
								}
							}
							
						}
						logger.debug("Before 1111 ::: "+deal);
						logger.debug("Before save ::: "+deal.getDealUrl());
						dealsDAO.saveFilterdDeal(deal);						    
						ydeal = ydealsoptiom.get(i).getYdeal();
						dealOptionSet = new HashSet<Ydealoption>();
						dealOptionSet.add(ydealsoptiom.get(i));
					}
					else
					{
						dealOptionSet.add(ydealsoptiom.get(i));
					}
				}
			}
		}
	}

	@Override
	@Transactional
	public void filterBusiness()
	{
		logger.debug("In FILTER BUSINESS");
		//List<Ybusiness> ybusinessList = yelpDAO.getFilterBusiness(); 
		List<Ybusiness> ybusinessList = yelpDAO.getBusiness();
		logger.debug("List Size "+ybusinessList.size());
		if(ybusinessList != null)
		{

			//Ybusiness yBusiness = new Ybusiness();
			int count = 0;
			for(Ybusiness yBusiness:ybusinessList)
			{
				logger.debug("YelpFilterEngineService ==> filterBusiness ==> businessID ==> " + yBusiness.getBusinessId());
				Business business = new Business();		    
				business.setAddress(yBusiness.getAddress());
				//business.setAmbience(yBusiness.getA);
				business.setBusinessId(yBusiness.getBusinessId());
				//business.setBusinesstype(yBusiness.get);
				business.setCity(yBusiness.getCity());
				business.setCountryCode(yBusiness.getCountryCode());
				business.setCrossStreets(yBusiness.getCrossStreets());
				business.setDisplayAddress(yBusiness.getDisplayAddress());
				business.setDisplayPhone(yBusiness.getDisplayPhone());
				business.setDistance(yBusiness.getDistance());
				business.setImageUrl(yBusiness.getImageUrl());
				//business.setIsActive(yBusiness.getIsClaimed());//not match
				business.setIsActive(yBusiness.getIsClaimed());
				business.setIsClosed(yBusiness.getIsClosed());
				//business.setIsParkingAvailable(yBusiness.);
				// business.setLattitude(yBusiness.)
				business.setMobileUrl(yBusiness.getMobileUrl());
				business.setMenuProvider(yBusiness.getMenuProvider());
				business.setMenuProviderDate(yBusiness.getMenuProviderDate());
				business.setName(yBusiness.getName());
				//business.setMusicType(yBusiness.getm)
				business.setNeighborhoods(yBusiness.getNeighborhoods());
				//business.setNoiseLevel(noiseLevel)
				// business.setPages(yBusiness.getP)
				business.setPostalCode(yBusiness.getPostalCode());
				business.setRating(yBusiness.getRating());
				business.setRatingImgUrl(yBusiness.getRatingImgUrl());
				//business.setServiceLevel(yBusiness.getS)
				business.setSnippetImageUrl(yBusiness.getSnippetImageUrl());
				business.setSnippetText(yBusiness.getSnippetText());
				business.setStateCode(yBusiness.getStateCode());
				Set<Ybusinesscategorymap>  ycategorymap   = yBusiness.getYbusinesscategorymaps();
				logger.debug("MAP :::::::::::"+ycategorymap);
				Businesstype businesstype =null;
				if(ycategorymap != null)
				{
					 for(Ybusinesscategorymap ycategorymap1:ycategorymap)
					 {
						 Ycategoryfilter  ycategoryfilter = ycategorymap1.getYcategoryfilter();
						 int parentId = ycategoryfilter.getYcategoryfilter().getId();
						 logger.debug("Parent id ::: "+parentId);
						 if(parentId >= 491 && parentId <= 608)
						 {
							 businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
							 break;
						 }
						 else if(parentId >= 109 && parentId <= 130)
						 {
							 businesstype = businessDAO.getBusinesstypeByType("SPA");
							 break;
						 }
						 else if(parentId >= 1 && parentId <= 66)
						 {
							 businesstype = businessDAO.getBusinesstypeByType("SPORT");
							 break;
						 }
						 else if(parentId == 72)
						 {
							 businesstype = businessDAO.getBusinesstypeByType("MOVIE");
							 break;
						 }
						 /*String name = ycategoryfilter.getCategory();
						 if(name.toLowerCase().contains("restaurants"))
						 {
							 businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
							 break;
						 }
						else if(name.toLowerCase().contains("sport"))
						 {
							businesstype = businessDAO.getBusinesstypeByType("SPORT");
							break;
						 }
						else if(name.toLowerCase().contains("spas"))
						 {
							businesstype = businessDAO.getBusinesstypeByType("SPA");
							break;						}
					     }*/
					 }
				}
				//Businesstype businesstype = businessDAO.getBusinesstypeByType("OTHERS");
				business.setBusinesstype(businesstype);
				//Set<Yreview> yreview = yBusiness.getR
				// business.getTraffic(yBusiness.getT)
				//business.setWebsiteUrl(yBusiness.getW)
				//dealsDAO.saveBusiness(business);		    	
				businessDAO.saveBusiness(business, count);	
				count++;
				/*if(count == 2000)
				{
					break;
				}*/

			}
		}




	}

}
