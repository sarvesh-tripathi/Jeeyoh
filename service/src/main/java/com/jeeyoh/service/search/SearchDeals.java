package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Userdealssuggestion;

@Component("searchDeals")
public class SearchDeals implements ISearchDeals {
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	IDealsDAO dealsDAO;
	@Autowired
	private IUserDAO userDAO;

	@Override
	@Transactional
	public List<DealModel> getDeals(String keyword, String category, String location, String emailId) {
		// TODO Auto-generated method stub
		logger.debug("Get deal ::: ");
		List<Deals> deals =  dealsDAO.getDealsByKeywords(keyword,category,location);
		logger.debug("DEAL Object ::: "+deals.size());
		List<Userdealssuggestion> dealSuggestionList = null;
		if(emailId != null && emailId  != "")
		{			
			User user = userDAO.getUsersById(emailId);
			logger.debug("user Id :::: "+ user.getUserId());
			dealSuggestionList = dealsDAO.getDealsSuggestion(user.getUserId());
		}
		
		List<DealModel> dealModels = new ArrayList<DealModel>();
		if(deals != null)
		{
			
			for (Deals deal : deals)
			{
				DealModel dealModel =  new DealModel();
				if(dealSuggestionList != null)
				{
					for(Userdealssuggestion dealsuggestion : dealSuggestionList)
					{
						logger.debug("DEAL 1111111 :::: " + dealsuggestion.getDeals().getId());
						logger.debug("DEAL 2222222 :::: " + deal.getId());
						if(dealsuggestion.getDeals().getId() == deal.getId())
						{
							if(deal.getDealUrl() != null)
							{
								dealModel.setDealUrl(deal.getDealUrl());
							}
							if(deal.getTitle() != null)
							{
								dealModel.setTitle(deal.getTitle());
							}
							if(deal.getStatus() != null)
							{
								dealModel.setStatus(deal.getStatus());
							}
							if(deal.getStartAt().toString() != null)
							{
								dealModel.setStartAt(deal.getStartAt().toString());
							}
							if(deal.getEndAt().toString() != null)
							{
								dealModel.setEndAt(deal.getEndAt().toString());
							}				
							
							if(dealModel != null)
							{
								dealModels.add(dealModel);
							}
						}
					}
					
				}
				else
				{
					if(deal.getDealUrl() != null)
					{
						dealModel.setDealUrl(deal.getDealUrl());
					}
					if(deal.getTitle() != null)
					{
						dealModel.setTitle(deal.getTitle());
					}
					if(deal.getStatus() != null)
					{
						dealModel.setStatus(deal.getStatus());
					}
					if(deal.getStartAt().toString() != null)
					{
						dealModel.setStartAt(deal.getStartAt().toString());
					}
					if(deal.getEndAt().toString() != null)
					{
						dealModel.setEndAt(deal.getEndAt().toString());
					}				
					
					if(dealModel != null)
					{
						dealModels.add(dealModel);
					}
				}
				
				
				
			}
			
		}
		return dealModels;
		
	}
	
	@Override
	@Transactional
	public List<DealModel> getUserDeals(String emailId) {
		
		List<Deals> deals = null;
		List<DealModel> dealModels = new ArrayList<DealModel>();
		if(emailId != null && emailId  != "")
		{			
			User user = userDAO.getUsersById(emailId);
			logger.debug("user Id :::: "+ user.getUserId());
			deals = dealsDAO.getUserDeals(user.getUserId());	
			logger.debug("deals  ::::"+deals.size());
			
		}
		if(deals != null)
		{
			
			for (Deals deal : deals)
			{
				DealModel dealModel =  new DealModel();
				logger.debug("UR L  :: "+deal.getDealUrl());
				if(deal.getDealUrl() != null)
				{
					dealModel.setDealUrl(deal.getDealUrl());
				}
				if(deal.getTitle() != null)
				{
					dealModel.setTitle(deal.getTitle());
				}
				if(deal.getStatus() != null)
				{
					dealModel.setStatus(deal.getStatus());
				}
				if(deal.getStartAt().toString() != null)
				{
					dealModel.setStartAt(deal.getStartAt().toString());
				}
				if(deal.getEndAt().toString() != null)
				{
					dealModel.setEndAt(deal.getEndAt().toString());
				}				
				
				if(dealModel != null)
				{
					dealModels.add(dealModel);
				}
			}
			
		}
		
		return dealModels;
	}
	
	@Override
	@Transactional
	public List<DealModel> getUserContactAndCommunityDeals(String emailId){
		
		List<Deals> deals = null;
		List<DealModel> dealModels = new ArrayList<DealModel>();
		if(emailId != null && emailId  != "")
		{			
			User user = userDAO.getUsersById(emailId);
			logger.debug("user Id :::: "+ user.getUserId());
			
			// get user contects				
			List<User> userContacts = userDAO.getUserContacts(user.getUserId());
			
			// user commuinty 
			List<Page> userCommunities = userDAO.getUserCommunities(user.getUserId());
						
		//contacts deals
		if(userContacts != null) {
			for(User contact : userContacts) {
				if(contact != null) {
					logger.debug("contact 4 ==> "+contact.getFirstName());
					deals = dealsDAO.getUserDeals(contact.getUserId());
					if(deals != null)
					{
						
						for (Deals deal : deals)
						{
							DealModel dealModel =  new DealModel();
							logger.debug("UR L11111  :: "+deal.getDealUrl());
							if(deal.getDealUrl() != null)
							{
								dealModel.setDealUrl(deal.getDealUrl());
							}
							if(deal.getTitle() != null)
							{
								dealModel.setTitle(deal.getTitle());
							}
							if(deal.getStatus() != null)
							{
								dealModel.setStatus(deal.getStatus());
							}
							if(deal.getStartAt().toString() != null)
							{
								dealModel.setStartAt(deal.getStartAt().toString());
							}
							if(deal.getEndAt().toString() != null)
							{
								dealModel.setEndAt(deal.getEndAt().toString());
							}				
							
							if(dealModel != null)
							{
								dealModels.add(dealModel);
							}
						}
						
					}
				}
			}
		}
		
		logger.debug("deal Model size in contacts " + dealModels.size());
		// community deals
		if(userCommunities != null) {
			
			for(Page community : userCommunities) {
				
				List<Deals> deals1 =  dealsDAO.getDealsByBusinessId(community.getBusiness().getId());
				if(deals != null)
				{
					
					for (Deals deal : deals1)
					{
						DealModel dealModel =  new DealModel();
						logger.debug("UR L222222  :: "+deal.getDealUrl());
						if(deal.getDealUrl() != null)
						{
							dealModel.setDealUrl(deal.getDealUrl());
						}
						if(deal.getTitle() != null)
						{
							dealModel.setTitle(deal.getTitle());
						}
						if(deal.getStatus() != null)
						{
							dealModel.setStatus(deal.getStatus());
						}
						if(deal.getStartAt().toString() != null)
						{
							dealModel.setStartAt(deal.getStartAt().toString());
						}
						if(deal.getEndAt().toString() != null)
						{
							dealModel.setEndAt(deal.getEndAt().toString());
						}				
						
						if(dealModel != null)
						{
							dealModels.add(dealModel);
						}
					}
					
				}
			}
		
			}
		
		
		logger.debug("deal Model size after community " + dealModels.size());
		
	}
		return dealModels;
	}

}
