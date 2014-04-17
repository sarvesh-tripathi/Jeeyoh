package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.domain.Deals;

@Component("userDealsSearch")
public class UserDealsSearchService implements IUserDealsSearchService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IDealsDAO dealsDAO;

	@Override
	@Transactional
	public List<DealModel> search(String userEmail)
	{
		List<Deals> dealList = dealsDAO.getDealsByUserEmail(userEmail);
		logger.debug("UserDealsSearch ==> search ==> dealList ==> "+dealList);
		List<DealModel> dealModelList = new ArrayList<DealModel>();
		for(Deals deals : dealList)
		{

			DealModel dealModel = new DealModel();
			if(deals.getTitle() != null)
			{
				dealModel.setTitle(deals.getTitle());
			}
			if(deals.getDealUrl() != null)
			{
				dealModel.setDealUrl(deals.getDealUrl());
			}
			if(deals.getStartAt() != null)
			{
				dealModel.setStartAt(deals.getStartAt().toString());
			}
			if(deals.getEndAt() != null)
			{
				dealModel.setEndAt(deals.getEndAt().toString());
			}
			if(deals.getStatus() != null)
			{
				dealModel.setStatus(deals.getStatus());
			}

			dealModelList.add(dealModel);
		}
		return dealModelList;
	}

}
