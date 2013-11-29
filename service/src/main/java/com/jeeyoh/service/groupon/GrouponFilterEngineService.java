package com.jeeyoh.service.groupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.dao.groupon.IGDealsDAO;
import com.jeeyoh.persistence.domain.Gdeal;
import com.jeeyoh.persistence.domain.Gdealoption;

@Component("grouponFilterEngine")
public class GrouponFilterEngineService implements IGrouponFilterEngineService {

	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IGDealsDAO gDealsDAO;

	@Autowired
	private IDealsDAO dealsDAO;

	@Autowired
	private IBusinessDAO businessDAO;	

	@Override
	@Transactional
	public void filter() {
		//List<Gdeal> gDealList = gDealsDAO.getDeals();
		List<Object> rows = gDealsDAO.getDeals();
		logger.debug("loadDeals => row size " + rows.size());
		List<Gdeal> gDealList = new ArrayList<Gdeal>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		findWeekends();
		logger.debug("loadDeals => weekendList size " + weekendList.size());
		if(rows != null)
		{
			int count = 0;
			for(Object r: rows){
				count++;
				if(count < 10)
				{
					Gdealoption row = (Gdealoption) r;
					Gdeal gdeal = row.getGdeal();

					for(int i = 0; i < weekendList.size(); i++){
						logger.debug("Date:: "+ i);
						try {
							logger.debug("Date::  "+ gdeal.getEndAt() +"  :  "+ sdf.parse(sdf.format((Date)weekendList.get(i))));
							if(gdeal.getEndAt().before(sdf.parse(sdf.format((Date)weekendList.get(i)))))
							{
								gDealList.add(gdeal);
								break;
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					/*Deals deals = new Deals();
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
					List<Business> businessList = businessDAO.getBusinessById(gmerchant.getMerchantId());
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
							businessList = businessDAO.getBusinessById(business.getBusinessId());
							deals.setBusiness(businessList.get(0));
						}
						else
						{
							deals.setBusiness(businessList.get(0));
						}
					}

					Set<Gdealoption> gdealoption = gdeal.getGdealoptions();
					logger.debug("loadDeals => gdealoption size " + gdealoption.size());

				if(gdealoption != null) {
					Set<Dealoption> dealOptions = new HashSet<Dealoption>();
					Dealoption dealOption = null;
					for(Gdealoption option : gdealoption) {
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
				dealsDAO.saveDeal(deals);*/
				}
				else
					break;	
				
				
				logger.debug("loadDeals => gDealList size " + gDealList.size());
				/*for(Gdeal gdeal: gDealList)
				{
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
					List<Business> businessList = businessDAO.getBusinessById(gmerchant.getMerchantId());
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
							businessList = businessDAO.getBusinessById(business.getBusinessId());
							deals.setBusiness(businessList.get(0));
						}
						else
						{
							deals.setBusiness(businessList.get(0));
						}
					}

					Set<Gdealoption> gdealoption = gdeal.getGdealoptions();
					logger.debug("loadDeals => gdealoption size " + gdealoption.size());

				if(gdealoption != null) {
					Set<Dealoption> dealOptions = new HashSet<Dealoption>();
					Dealoption dealOption = null;
					for(Gdealoption option : gdealoption) {
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
				dealsDAO.saveDeal(deals);
				}*/
			}
		} else {
			logger.debug("loadDeals => no deals found");
		}
	}


	
	public void filterDealsByExpirationDate() {


		/*List<Gdeal> gDealList = gDealsDAO.getDealsByEndDate();
		logger.debug("loadDeals => row size " + gDealList.size());

		if(gDealList != null)
		{
			int count = 0;

			for(Gdeal gdeal: gDealList){
				count++;
				if(count < 10)
				{
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
				List<Business> businessList = businessDAO.getBusinessById(gmerchant.getMerchantId());
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
						businessList = businessDAO.getBusinessById(business.getBusinessId());
						deals.setBusiness(businessList.get(0));
					}
					else
					{
						deals.setBusiness(businessList.get(0));
					}
				}

				Set<Gdealoption> gdealoption = gdeal.getGdealoptions();
				if(gdealoption != null) {
					Set<Dealoption> dealOptions = new HashSet<Dealoption>();
					Dealoption dealOption = null;
					for(Gdealoption option : gdealoption) {
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
				dealsDAO.saveDeal(deals);
				}
				else 
					break;
			}
		} else {
			logger.debug("loadDeals => no deals found");
		}*/
	}

	private ArrayList weekendList = null;
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
