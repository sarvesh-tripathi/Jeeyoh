package com.jeeyoh.service.groupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.jeeyoh.persistence.domain.Gmerchant;

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
		List<Gdeal> gDealList = new ArrayList<Gdeal>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Set<Gdealoption> gdealoption = null;
		findWeekends();
		logger.debug("loadDeals => weekendList size " + weekendList.size());
		Gdeal gdeal = null;
		if(rows != null)
		{
			//int count = 0;
			int batch_size = 0;
			for(int i = 0; i < rows.size();i++){
				//count++;
				//if(count < 1000)
				//{
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
									if(gdeal.getEndAt().before(sdf.parse(sdf.format((Date)weekendList.get(j)))))
									{
										batch_size++;
										logger.debug("gdeal id:: "+ gdeal.getId());
										gdeal.setGdealoptions(gdealoption);
										//gDealList.add(gdeal);
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
												Businesstype businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
												Business business = new Business();
												business.setName(gmerchant.getName());
												business.setBusinessId(gmerchant.getMerchantId());
												business.setWebsiteUrl(gmerchant.getWebsiteUrl());
												business.setBusinesstype(businesstype);
												businessDAO.saveBusiness(business);
												businessList = businessDAO.getBusinessByIdForGroupon(business.getBusinessId());
												deals.setBusiness(businessList.get(0));
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
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						gdeal = row.getGdeal();
						gdealoption = new HashSet<Gdealoption>();
						gdealoption.add(row);
					} else { 
						gdealoption.add(row);
					}  
					//}  
				}
				//else
				//break;				
			}
			logger.debug("loadDeals => gDealList size " + gDealList.size());
		}
	}



	public void filterDealsByExpirationDate() {

	}


	@SuppressWarnings("unchecked")
	private ArrayList weekendList = null;
	@SuppressWarnings("unchecked")
	public void findWeekends(){
		weekendList = new ArrayList();
		Calendar cal = null;
		cal = Calendar.getInstance();
		// The while loop ensures that you are only checking dates in the specified year
		while(cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
			// The switch checks the day of the week for Saturdays and Sundays
			switch(cal.get(Calendar.DAY_OF_WEEK)){
			case Calendar.SATURDAY:
			case Calendar.SUNDAY:
				weekendList.add(cal.getTime());
				break;
			}
			// Increment the day of the year for the next iteration of the while loop
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
	}

}
