package com.jeeyoh.service.yelp;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.dao.yelp.IYelpDAO;
import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Ydeal;
import com.jeeyoh.persistence.domain.Ydealoption;

@Component("yelpFilterEngine")
public class YelpFilterEngineService implements IYelpFilterEngineService {
	@Autowired
	IYelpDAO yelpDAO;
	@Autowired
	IDealsDAO dealsDAO;
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Override
	@Transactional
	public void filter() {
		// TODO Auto-generated method stub
		logger.debug("In Yelp Filter ");
		List<Ydeal> ydeals = yelpDAO.filterdDealByDiscount();
		logger.debug("Deal List :::  "+ydeals.size());
		if(ydeals != null)
		{
			for(Ydeal ydeal:ydeals)
			{
				Deals deal = new Deals();
				deal.setAdditionalDeals(ydeal.getAdditionalDeals());
				deal.setAdditionalRestrictions(ydeal.getAdditionalRestrictions());
				deal.setIsPopular(ydeal.getIsPopular());
				deal.setTitle(ydeal.getDealTitle());
				deal.setDealUrl(ydeal.getDealUrl());
			    Set<Ydealoption> ydealoptions = ydeal.getYdealoptions();
			    if(ydealoptions != null)
			    {
				    for(Ydealoption ydealoption: ydealoptions)
				    {
				    	Dealoption dealoption = new Dealoption();
				    	dealoption.setOriginalPrice(ydealoption.getOriginalPrice());
				    	//dealoption.set
				    }
			    }
				
				//ydeal.
				
				
				//yelpDAO.saveFilterdDeal(ydeal);
			}
		}
		

	}

}
