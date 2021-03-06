package com.jeeyoh.service.groupon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.dao.groupon.IGDealsDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Dealredemptionlocation;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Gcategory;
import com.jeeyoh.persistence.domain.Gdeal;
import com.jeeyoh.persistence.domain.Gdealoption;
import com.jeeyoh.persistence.domain.Gdealprice;
import com.jeeyoh.persistence.domain.Gdivision;
import com.jeeyoh.persistence.domain.Gmerchant;
import com.jeeyoh.persistence.domain.Gmerchantrating;
import com.jeeyoh.persistence.domain.Goptiondetail;
import com.jeeyoh.persistence.domain.Gredemptionlocation;
import com.jeeyoh.persistence.domain.Gtags;

@Component("grouponFilterEngine")
public class GrouponFilterEngineService implements IGrouponFilterEngineService {

	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IGDealsDAO gDealsDAO;

	@Autowired
	private IDealsDAO dealsDAO;

	@Autowired
	private IBusinessDAO businessDAO;	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void filter() {

		List<Gdeal> rows = gDealsDAO.getGDeals();
		List<Gdealoption> gdealOptions = null;

		if(rows != null)
		{
			List<Deals> dealList = new ArrayList<Deals>();
			int batch_size = 0;
			//List<Date>weekendList =  Utils.findWeekends();
			//logger.debug("loadDeals => weekendList size " + weekendList.size());
			for(Gdeal gdeal : rows)
			{
				//for(int j = 0; j < weekendList.size(); j++){
				try {
					//logger.debug("Date::  "+ ldeal.getEndAt() +"  :  "+ sdf.parse(sdf.format((Date)weekendList.get(j))));
					//if(ldeal.getEndAt().before(weekendList.get(j))){
					int deal = dealsDAO.isDealExists(gdeal.getDealId());
					logger.debug("after query::::  "+deal);
					if(deal == 0)
					{
						Deals deals =new Deals();
						deals.setDealId(gdeal.getDealId());
						deals.setDealUrl(gdeal.getDealUrl());
						deals.setEndAt(gdeal.getEndAt());
						deals.setStartAt(gdeal.getStartAt());
						deals.setIsTipped(gdeal.getIsTipped());
						deals.setIsSoldOut(gdeal.getIsSoldOut());
						deals.setIsNowDeal(gdeal.getIsNowDeal());
						deals.setDealType(gdeal.getDealType());
						deals.setLargeImageUrl(gdeal.getLargeImageUrl());
						deals.setSmallImageUrl(gdeal.getSmallImageUrl());
						deals.setHighlightsHtml(gdeal.getHighlightsHtml());
						deals.setSoldQuantity(gdeal.getSoldQuantity());
						deals.setAnnouncementTitle(gdeal.getAnnouncementTitle());
						deals.setTippedAt(gdeal.getTippedAt());
						deals.setTippingPoint(gdeal.getTippingPoint());
						deals.setShippingAddressRequired(gdeal.getShippingAddressRequired());
						deals.setPitchHtml(gdeal.getPitchHtml());
						deals.setHighlightsHtml(gdeal.getHighlightsHtml());
						deals.setPlacementPriority(gdeal.getPlacementPriority());
						deals.setTitle(gdeal.getTitle());
						deals.setStatus(gdeal.getStatus());
						deals.setSidebarImageUrl(gdeal.getSidebarImageUrl());
						deals.setDealSource("Groupon");

						Gmerchant gmerchant = gdeal.getGmerchant();
						Gmerchantrating gmerchantrating = gmerchant.getGmerchantByRatingId();
						List<Business> businessList = businessDAO.getBusinessByIdForGroupon(gmerchant.getMerchantId());
						logger.debug("loadDeals => businessList " + businessList);
						if(businessList != null)
						{
							if(businessList.isEmpty())
							{
								Set<Gtags> tags = gdeal.getGtagses();
								Businesstype businesstype = null;

								for(Gtags gtag :tags)
								{
									String name = gtag.getName();

									Gcategory gcategory = businessDAO.getBusinessCategory(name);
									logger.debug("getBusinessCategory after query ::: "+gcategory);
									if(gcategory != null)
									{
										String category = gcategory.getGcategory().getCategory();
										if(category.equalsIgnoreCase("Arts and Entertainment"))
										{
											businesstype = businessDAO.getBusinesstypeByType("SPORT");
											break;
										}
										else if(category.equalsIgnoreCase("Restaurants"))
										{
											businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
											break;
										}
										else if(category.equalsIgnoreCase("Beauty & Spas"))
										{
											businesstype = businessDAO.getBusinesstypeByType("SPA");
											break;
										}
										else if(category.equalsIgnoreCase("Nightlife") || category.equalsIgnoreCase("Bars") || category.equalsIgnoreCase("Clubs") || category.equalsIgnoreCase("Lounges"))
										{
											businesstype = businessDAO.getBusinesstypeByType("NIGHTLIFE");
											break;
										}
										else if(category.equalsIgnoreCase("Health & Fitness"))
										{
											businesstype = businessDAO.getBusinesstypeByType("YOGA");
											break;
										}
									}
								}

								Gdivision gdivision = gdeal.getGdivision();

								Business business = new Business();
								business.setName(gmerchant.getName());
								business.setBusinessId(gmerchant.getMerchantId());
								business.setWebsiteUrl(gmerchant.getWebsiteUrl());
								business.setLattitude(gdivision.getLattitude());
								business.setLongitude(gdivision.getLongitude());
								business.setPostalCode(gdivision.getZipCode());
								business.setCity(gdivision.getCity());
								business.setDisplayAddress(gdivision.getAddress());
								business.setAddress(gdivision.getAddress());
								business.setStateCode(gdivision.getStateCode());
								business.setState(gdivision.getState());
								business.setBusinesstype(businesstype);
								if(gmerchantrating.getRating() != null)
									business.setRating(gmerchantrating.getRating());
								else
									business.setRating(0.0);
								business.setRatingImgUrl(gmerchantrating.getUrl());
								business.setSource("Groupon");

								//businessDAO.saveBusiness(business);
								//businessList = businessDAO.getBusinessByIdForGroupon(business.getBusinessId());
								deals.setBusiness(business);
							}
							else
							{
								deals.setBusiness(businessList.get(0));
							}
						}

						gdealOptions = gDealsDAO.getDealOptions(gdeal.getId());

						logger.debug("gdealOptions after query::  "+gdealOptions);
						if(gdealOptions!=null && !gdealOptions.isEmpty())
						{
							Set<Dealoption> dealOptions = new HashSet<Dealoption>();
							Dealoption dealOption = null;
							for(Gdealoption option : gdealOptions)
							{
								dealOption = new Dealoption();
								dealOption.setTitle(option.getTitle());
								dealOption.setSoldQuantity(option.getSoldQuantity());
								dealOption.setOptionId(option.getOptionId());										
								dealOption.setDiscountPercent(option.getDiscountPercent());
								dealOption.setInitialQuantity(option.getInitialQuantity());
								dealOption.setRemainingQuantity(option.getRemainingQuantity());
								dealOption.setMaximumPurchaseQuantity(option.getMaximumPurchaseQuantity());
								dealOption.setMinimumPurchaseQuantity(option.getMinimumPurchaseQuantity());
								dealOption.setExternalUrl(option.getExternalUrl());
								dealOption.setBuyUrl(option.getBuyUrl());
								dealOption.setExpiresAt(new DateTime(option.getExpiresAt()).toDate());

								Gdealprice gdealprice = option.getGdealpriceByPriceId();
								dealOption.setPrice(Long.parseLong(Integer.toString(gdealprice.getAmount())));
								dealOption.setFormattedPrice(gdealprice.getFormattedAmount());

								gdealprice = option.getGdealpriceByDiscountId();
								dealOption.setDiscountPrice(Long.parseLong(Integer.toString(gdealprice.getAmount())));
								dealOption.setFormattedDiscountPrice(gdealprice.getFormattedAmount());

								gdealprice = option.getGdealpriceByValueId();
								dealOption.setOriginalPrice(Long.parseLong(Integer.toString(gdealprice.getAmount()))/100);
								dealOption.setFormattedOriginalPrice(gdealprice.getFormattedAmount());

								Goptiondetail goptiondetail = gDealsDAO.getOptionDeatils(option.getId());
								dealOption.setDescription(goptiondetail.getDescription());

								Set<Gredemptionlocation> redemptionLocations = option.getGredemptionlocations();
								if(redemptionLocations != null) {
									Set<Dealredemptionlocation> dealRedemptionLocations = new HashSet<Dealredemptionlocation>();
									for(Gredemptionlocation redemptionLocation : redemptionLocations) {
										Dealredemptionlocation dealRedemptionLocation = new Dealredemptionlocation();
										dealRedemptionLocation.setStreetAddress1(redemptionLocation.getStreetAddress1());
										dealRedemptionLocation.setStreetAddress2(redemptionLocation.getStreetAddress2());
										dealRedemptionLocation.setCity(redemptionLocation.getCity());
										dealRedemptionLocation.setState(redemptionLocation.getState());
										dealRedemptionLocation.setPostalCode(redemptionLocation.getPostalCode());
										dealRedemptionLocation.setName(redemptionLocation.getName());
										dealRedemptionLocation.setLattitude(redemptionLocation.getLattitude());
										dealRedemptionLocation.setLongitude(redemptionLocation.getLongitude());
										dealRedemptionLocation.setPhoneNumber(redemptionLocation.getPhoneNumber());
										dealRedemptionLocation.setDealoption(dealOption);												
										dealRedemptionLocations.add(dealRedemptionLocation);
									}											
									dealOption.setDealredemptionlocations(dealRedemptionLocations);
								}
								dealOption.setDeals(deals);
								dealOptions.add(dealOption);
							}
							deals.setDealoptions(dealOptions);

							batch_size++;
							logger.debug("loadDeals => count " + batch_size);
							dealList.add(deals);
							dealsDAO.saveDeal(deals,batch_size);
							logger.debug("After Save ====> ");
						}

						//}
						//break;
					}

				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
			//dealsDAO.saveDeal(dealList, batch_size);
		}

		/*List<Gdealoption> rows = gDealsDAO.getDeals();
		logger.debug("loadDeals => row size " + rows.size());

		//Get current date
		//Date currentDate = Utils.getCurrentDate();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Set<Gdealoption> gdealoption = new HashSet<Gdealoption>();
		List<Date>weekendList =  Utils.findWeekends();
		logger.debug("loadDeals => weekendList size " + weekendList.size());
		Gdeal gdeal = null;
		if(rows != null)
		{
			int batch_size = 0;
			for(int i = 0; i < rows.size();i++){

				Gdealoption row = (Gdealoption) rows.get(i);
				gdeal = row.getGdeal();
				gdealoption.clear();
				gdealoption.add(row);

				for(int j = i+1; j < rows.size(); j++)
				{
					if(row.getGdeal().getId() == rows.get(j).getGdeal().getId())
					{
						gdealoption.add((Gdealoption) rows.get(j));
						rows.remove(j);
						j--;
					}
				}


				Integer gdealId = row.getGdeal().getId();
				if (i == 0) {

					gdeal = row.getGdeal();
					gdealoption = new HashSet<Gdealoption>();
					gdealoption.add(row);

				} else {  
					int pregdealId = rows.get(i - 1).getGdeal().getId();  
					if (gdealId != pregdealId) { 

						for(int j = 0; j < weekendList.size(); j++){

							try {
								logger.debug("Date::  "+ gdeal.getEndAt() +"  :  "+ sdf.parse(sdf.format((Date)weekendList.get(j))));

								//if(gdeal.getEndAt().compareTo(currentDate) >= 0 && gdeal.getEndAt().before(sdf.parse(sdf.format((Date)weekendList.get(j)))))
								//{
								if(gdeal.getEndAt().before(sdf.parse(sdf.format((Date)weekendList.get(j)))))
								{
									Deals deal = dealsDAO.isDealExists(gdeal.getDealId());
									if(deal == null)
									{
										batch_size++;
										logger.debug("gdeal id:: "+ gdeal.getId());
										gdeal.setGdealoptions(gdealoption);

										Deals deals = new Deals();
										deals.setDealId(gdeal.getDealId());
										deals.setDealUrl(gdeal.getDealUrl());
										deals.setEndAt(gdeal.getEndAt());
										deals.setStartAt(gdeal.getStartAt());
										deals.setIsTipped(gdeal.getIsTipped());
										deals.setIsSoldOut(gdeal.getIsSoldOut());
										deals.setIsNowDeal(gdeal.getIsNowDeal());
										deals.setDealType(gdeal.getDealType());
										deals.setLargeImageUrl(gdeal.getLargeImageUrl());
										deals.setSmallImageUrl(gdeal.getSmallImageUrl());
										deals.setHighlightsHtml(gdeal.getHighlightsHtml());
										deals.setSoldQuantity(gdeal.getSoldQuantity());
										deals.setAnnouncementTitle(gdeal.getAnnouncementTitle());
										deals.setTippedAt(gdeal.getTippedAt());
										deals.setTippingPoint(gdeal.getTippingPoint());
										deals.setShippingAddressRequired(gdeal.getShippingAddressRequired());
										deals.setPitchHtml(gdeal.getPitchHtml());
										deals.setHighlightsHtml(gdeal.getHighlightsHtml());
										deals.setPlacementPriority(gdeal.getPlacementPriority());
										deals.setTitle(gdeal.getTitle());
										deals.setStatus(gdeal.getStatus());
										deals.setSidebarImageUrl(gdeal.getSidebarImageUrl());
										deals.setDealSource("Groupon");

										Gmerchant gmerchant = gdeal.getGmerchant();
										Gmerchantrating gmerchantrating = gmerchant.getGmerchantByRatingId();
										List<Business> businessList = businessDAO.getBusinessByIdForGroupon(gmerchant.getMerchantId());
										logger.debug("loadDeals => businessList " + businessList);
										if(businessList != null)
										{
											if(businessList.isEmpty())
											{
												Set<Gtags> tags = gdeal.getGtagses();
												Businesstype businesstype = null;

												for(Gtags gtag :tags)
												{
													String name = gtag.getName();
													logger.debug("Check Tag Name ::: "+name);

													Gcategory gcategory = businessDAO.getBusinessCategory(name);
													if(gcategory != null)
													{
														String category = gcategory.getGcategory().getCategory();
														logger.debug("category ::: "+category);
														if(category.equalsIgnoreCase("Arts and Entertainment"))
														{
															businesstype = businessDAO.getBusinesstypeByType("SPORT");
															break;
														}
														else if(category.equalsIgnoreCase("Restaurants"))
														{
															businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
															break;
														}
														else if(category.equalsIgnoreCase("Beauty & Spas"))
														{
															businesstype = businessDAO.getBusinesstypeByType("SPA");
															break;
														}
														else if(category.equalsIgnoreCase("Nightlife") || category.equalsIgnoreCase("Bars") || category.equalsIgnoreCase("Clubs") || category.equalsIgnoreCase("Lounges"))
														{
															businesstype = businessDAO.getBusinesstypeByType("NIGHTLIFE");
															break;
														}
														else if(category.equalsIgnoreCase("Health & Fitness"))
														{
															businesstype = businessDAO.getBusinesstypeByType("YOGA");
															break;
														}
													}
												}

												Gdivision gdivision = gdeal.getGdivision();

												Business business = new Business();
												business.setName(gmerchant.getName());
												business.setBusinessId(gmerchant.getMerchantId());
												business.setWebsiteUrl(gmerchant.getWebsiteUrl());
												business.setLattitude(gdivision.getLattitude());
												business.setLongitude(gdivision.getLongitude());
												business.setPostalCode(gdivision.getZipCode());
												business.setCity(gdivision.getCity());
												business.setDisplayAddress(gdivision.getAddress());
												business.setAddress(gdivision.getAddress());
												business.setStateCode(gdivision.getStateCode());
												business.setState(gdivision.getState());
												business.setBusinesstype(businesstype);
												business.setRating(gmerchantrating.getRating());
												business.setRatingImgUrl(gmerchantrating.getUrl());
												business.setSource("Groupon");

												//businessDAO.saveBusiness(business);
												//businessList = businessDAO.getBusinessByIdForGroupon(business.getBusinessId());
												deals.setBusiness(business);
											}
											else
											{
												deals.setBusiness(businessList.get(0));
											}
										}

										Set<Gdealoption> gdealoption1 = gdeal.getGdealoptions();
										logger.debug("loadDeals => gdealoption size " + gdeal.getId()+"   :   " +gdealoption1.size());

										if(gdealoption1 != null) {
											Set<Dealoption> dealOptions = new HashSet<Dealoption>();
											Dealoption dealOption = null;
											for(Gdealoption option : gdealoption1) {
												dealOption = new Dealoption();
												dealOption.setTitle(option.getTitle());
												dealOption.setSoldQuantity(option.getSoldQuantity());
												dealOption.setOptionId(option.getOptionId());										
												dealOption.setDiscountPercent(option.getDiscountPercent());
												dealOption.setInitialQuantity(option.getInitialQuantity());
												dealOption.setRemainingQuantity(option.getRemainingQuantity());
												dealOption.setMaximumPurchaseQuantity(option.getMaximumPurchaseQuantity());
												dealOption.setMinimumPurchaseQuantity(option.getMinimumPurchaseQuantity());
												dealOption.setExternalUrl(option.getExternalUrl());
												dealOption.setBuyUrl(option.getBuyUrl());
												dealOption.setExpiresAt(new DateTime(option.getExpiresAt()).toDate());
												Gdealprice gdealprice = option.getGdealpriceByPriceId();
												dealOption.setPrice(Long.parseLong(Integer.toString(gdealprice.getAmount())));
												dealOption.setFormattedPrice(gdealprice.getFormattedAmount());
												dealOption.setDeals(deals);
												dealOptions.add(dealOption);
											}
											deals.setDealoptions(dealOptions);
										}		

										logger.debug("loadDeals => count ");
										dealsDAO.saveDeal(deals,batch_size);
									}

									break;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						gdeal = row.getGdeal();
						gdealoption = new HashSet<Gdealoption>();
						gdealoption.add(row);
					} else { 
						gdealoption.add(row);
					} 
				}		
			}
		}*/
	}
}
