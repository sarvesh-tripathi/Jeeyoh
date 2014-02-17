package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.search.BusinessModel;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;

@Component("nonDealSuggestionSearch")
public class NonDealSuggestionSearch implements INonDealSuggestionSearch{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IBusinessDAO businessDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<BusinessModel> search(String userEmail, String searchText, String category, String location, String rating) {
		
		List<Business> rows = null;
		rows = businessDAO.getBusinessByCriteria(userEmail, searchText, category, location, rating);
		if(rating != null && !rating.equals(""))
		{
			if(rows == null || rows.isEmpty())
			{
				rows = businessDAO.getBusinessByCriteriaWithoutRating(userEmail, searchText, category, location, rating);
			}
		}
			
		logger.debug("NonDealSuggestionSearch ==> rows ==> "+rows.size());
		List<BusinessModel> businessModelList = new ArrayList<BusinessModel>();
		for(Business business : rows)
		{
			BusinessModel businessModel = new BusinessModel();
			businessModel.setName(business.getName());
			businessModel.setWebsiteUrl(business.getWebsiteUrl());
			businessModel.setDisplayAddress(business.getDisplayAddress());
			businessModel.setCity(business.getCity());
			businessModel.setBusinessType(business.getBusinesstype().getBusinessType());
			Set<Usernondealsuggestion> Usernondealsuggestions = business.getUsernondealsuggestions();
			if(userEmail != null && !userEmail.trim().equals(""))
			{
				for(Usernondealsuggestion usernondealsuggestionsObj : Usernondealsuggestions)
				{
					if(usernondealsuggestionsObj.getUser().getEmailId().equals(userEmail))
					{
						businessModel.setSuggestionType(usernondealsuggestionsObj.getSuggestionType());
						break;
					}
				}
			}
			businessModelList.add(businessModel);
		}
		return businessModelList;

	}

}
