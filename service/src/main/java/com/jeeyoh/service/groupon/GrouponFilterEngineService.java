package com.jeeyoh.service.groupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Gdeal;
import com.jeeyoh.persistence.domain.Gdealoption;
import com.jeeyoh.persistence.domain.Gdivision;
import com.jeeyoh.persistence.domain.Gmerchant;
import com.jeeyoh.persistence.domain.Gtags;
import com.jeeyoh.utils.Utils;

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
		List<Gdealoption> rows = gDealsDAO.getDeals();
		logger.debug("loadDeals => row size " + rows.size());

		//Get current date
		Date currentDate = Utils.getCurrentDate();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Set<Gdealoption> gdealoption = null;
		List<Date>weekendList =  Utils.findWeekends();
		logger.debug("loadDeals => weekendList size " + weekendList.size());
		Gdeal gdeal = null;
		if(rows != null)
		{
			int batch_size = 0;
			for(int i = 0; i < rows.size();i++){
				
				Gdealoption row = (Gdealoption) rows.get(i);
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

								if(sdf.parse(sdf.format(gdeal.getEndAt())).compareTo(currentDate) >= 0 && gdeal.getEndAt().before(sdf.parse(sdf.format((Date)weekendList.get(j)))))

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

									Gmerchant gmerchant = gdeal.getGmerchant();
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
												else if(name.toLowerCase().contains("spa"))
												{
													businesstype = businessDAO.getBusinesstypeByType("SPA");
													break;
												}
												else if(name.toLowerCase().contains("yoga"))
												{
													businesstype = businessDAO.getBusinesstypeByType("YOGA");
													break;
												}
												else if(name.toLowerCase().contains("movie"))
												{
													businesstype = businessDAO.getBusinesstypeByType("MOVIE");
													break;
												}
											}
											
											Gdivision gdivision = gdeal.getGdivision();
											
											//businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
											//String[] addressArray = Utils.getCityAndAddress(Double.parseDouble(gdivision.getLattitude()), Double.parseDouble(gdivision.getLongitude()));
											Business business = new Business();
											business.setName(gmerchant.getName());
											business.setBusinessId(gmerchant.getMerchantId());
											business.setWebsiteUrl(gmerchant.getWebsiteUrl());
											business.setLattitude(gdivision.getLattitude());
											business.setLongitude(gdivision.getLongitude());
											business.setPostalCode(gdivision.getZipCode());
											business.setCity(gdivision.getName());
											business.setDisplayAddress(gdivision.getAddress());
											business.setAddress(gdivision.getAddress());
											business.setBusinesstype(businesstype);
											business.setSource("Groupon");
											businessDAO.saveBusiness(business);
											businessList = businessDAO.getBusinessByIdForGroupon(business.getBusinessId());
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

											dealOption.setDeals(deals);
											dealOptions.add(dealOption);
										}
										deals.setDealoptions(dealOptions);
									}		

									logger.debug("loadDeals => count ");
									dealsDAO.saveDeal(deals,batch_size);
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
		}
	}
}
