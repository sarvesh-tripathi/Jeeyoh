package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IGroupDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.Userdealssuggestion;

@Component("searchDeals")
public class SearchDealsService implements ISearchDealsService {
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	IDealsDAO dealsDAO;
	@Autowired
	private IUserDAO userDAO;
	@Autowired
	private IGroupDAO groupDAO;

	@Override
	@Transactional
	public Set<DealModel> getDeals(String keyword, String category, String location, String emailId) {
		// TODO Auto-generated method stub
		logger.debug("Get deal ::: ");
		List<Deals> deals =  null;
		
		List<Userdealssuggestion> dealSuggestionList = null;
		if(emailId != null && emailId.length()  != 0)
		{			
			//User user = userDAO.getUsersById(emailId);
			//logger.debug("user Id :::: "+ user.getUserId());
			dealSuggestionList = dealsDAO.userDealsSuggestedByJeeyoh(keyword, category,
					location, emailId.trim());//getDealsSuggestion(user.getUserId());
		}
		else
		{
			deals = dealsDAO.getDealsByKeywords(keyword,category,location);
		}
		
		//List<DealModel> dealModels = new ArrayList<DealModel>();
		Set<DealModel> dealModels = new HashSet<DealModel>();
		if(deals != null)
		{
			
			for (Deals deal : deals)
			{
				//DealModel dealModel =  new DealModel();
				if(dealSuggestionList != null)
				{
					for(Userdealssuggestion dealsuggestion : dealSuggestionList)
					{
						if(dealsuggestion.getDeals() != null)
						{
							DealModel dealModel = new DealModel();
							//dealModel.setUserId(dealsuggestion.getUser().getUserId());
							Deals deal1 = dealsuggestion.getDeals();
							if(deal1.getTitle() != null)
							{
								dealModel.setTitle(deal1.getTitle());
							}
							if(deal1.getDealUrl() != null)
							{
								dealModel.setDealUrl(deal1.getDealUrl());
							}
							if(deal1.getStartAt() != null)
							{
								dealModel.setStartAt(deal1.getStartAt().toString());
							}
							if(deal1.getEndAt() != null)
							{
								dealModel.setEndAt(deal1.getEndAt().toString());
							}
							if(deal1.getStatus() != null)
							{
								dealModel.setStatus(deal1.getStatus());
							}
							if(dealsuggestion.getSuggestionType() != null)
							{
								dealModel.setSuggestionType(dealsuggestion.getSuggestionType());
							}
							
							dealModels.add(dealModel);
						}
						
					}
					
				}
				else
				{
					if(deal != null)
					{
						//dealModels.add(deal);
						DealModel dealModel = new DealModel();
						if(deal.getTitle() != null)
						{
							dealModel.setTitle(deal.getTitle());
						}
						if(deal.getDealUrl() != null)
						{
							dealModel.setDealUrl(deal.getDealUrl());
						}
						if(deal.getStartAt() != null)
						{
							dealModel.setStartAt(deal.getStartAt().toString());
						}
						if(deal.getEndAt() != null)
						{
							dealModel.setEndAt(deal.getEndAt().toString());
						}
						if(deal.getStatus() != null)
						{
							dealModel.setStatus(deal.getStatus());
						}
						
						dealModels.add(dealModel);
					}
				}
				
				
				
			}
			
		}
		else
		{
			logger.debug("USER DEALS ::::::::"+dealSuggestionList.size());
			if(dealSuggestionList != null)
			{
				for(Userdealssuggestion dealsuggestion : dealSuggestionList)
				{
			
					if(dealsuggestion.getDeals() != null)
					{
						DealModel dealModel = new DealModel();
						Deals deal1 = dealsuggestion.getDeals();
						if(deal1.getTitle() != null)
						{
							dealModel.setTitle(deal1.getTitle());
						}
						if(deal1.getDealUrl() != null)
						{
							dealModel.setDealUrl(deal1.getDealUrl());
						}
						if(deal1.getStartAt() != null)
						{
							dealModel.setStartAt(deal1.getStartAt().toString());
						}
						if(deal1.getEndAt() != null)
						{
							dealModel.setEndAt(deal1.getEndAt().toString());
						}
						if(deal1.getStatus() != null)
						{
							dealModel.setStatus(deal1.getStatus());
						}
						if(dealsuggestion.getSuggestionType() != null)
						{
							dealModel.setSuggestionType(dealsuggestion.getSuggestionType());
						}
						dealModels.add(dealModel);
					}
			    }
					
				}
		}
	
		return dealModels;
		
	}

	@Override
	@Transactional
	public Set<DealModel> getUserDeals(String emailId) {
		
		List<Deals> deals = null;
		//List<DealModel> dealModels = new ArrayList<DealModel>();
		Set<DealModel> dealModels = new HashSet<DealModel>();
		if(emailId != null && emailId  != "")
		{			
			User user = userDAO.getUserByEmailId(emailId);
			logger.debug("user Id :::: "+ user.getUserId());
			deals = dealsDAO.getUserDeals(user.getUserId());	
			logger.debug("deals  ::::"+deals.size());
			
		}
		if(deals != null)
		{
			
			for (Deals deal : deals)
			{
				
				if(deal != null)
				{
					DealModel dealModel = new DealModel();
					if(deal.getTitle() != null)
					{
						dealModel.setTitle(deal.getTitle());
					}
					if(deal.getDealUrl() != null)
					{
						dealModel.setDealUrl(deal.getDealUrl());
					}
					if(deal.getStartAt() != null)
					{
						dealModel.setStartAt(deal.getStartAt().toString());
					}
					if(deal.getEndAt() != null)
					{
						dealModel.setEndAt(deal.getEndAt().toString());
					}
					if(deal.getStatus() != null)
					{
						dealModel.setStatus(deal.getStatus());
					}
					
					dealModels.add(dealModel);
				}
			}
			
		}
		
		return dealModels;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Set<DealModel> getUserContactAndCommunityDeals(String emailId){
		
		List<Deals> deals = null;
		List<DealModel> dealModels = new ArrayList<DealModel>();
		//Set<DealModel> dealModels = new HashSet<DealModel>();
		if(emailId != null && emailId  != "")
		{			
			User user = userDAO.getUserByEmailId(emailId);
			logger.debug("user Id :::: "+ user.getUserId());
			
			// get user contects				
			List<User> userContacts = userDAO.getUserContacts(user.getUserId());
			
			// user commuinty 
			List<Page> userCommunities = userDAO.getUserCommunities(user.getUserId(),Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()));
			
			// user group member 
			List<Jeeyohgroup> jeeyohGroup = groupDAO.getUserGroups(user.getUserId());
						
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
							if(deal != null)
							{
								
								DealModel dealModel = new DealModel();
								if(deal.getTitle() != null)
								{
									dealModel.setTitle(deal.getTitle());
								}
								if(deal.getDealUrl() != null)
								{
									dealModel.setDealUrl(deal.getDealUrl());
								}
								if(deal.getStartAt() != null)
								{
									dealModel.setStartAt(deal.getStartAt().toString());
								}
								if(deal.getEndAt() != null)
								{
									dealModel.setEndAt(deal.getEndAt().toString());
								}
								if(deal.getStatus() != null)
								{
									dealModel.setStatus(deal.getStatus());
								}
								
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
						
						if(deal != null)
						{
							DealModel dealModel = new DealModel();
							if(deal.getTitle() != null)
							{
								dealModel.setTitle(deal.getTitle());
							}
							if(deal.getDealUrl() != null)
							{
								dealModel.setDealUrl(deal.getDealUrl());
							}
							if(deal.getStartAt() != null)
							{
								dealModel.setStartAt(deal.getStartAt().toString());
							}
							if(deal.getEndAt() != null)
							{
								dealModel.setEndAt(deal.getEndAt().toString());
							}
							if(deal.getStatus() != null)
							{
								dealModel.setStatus(deal.getStatus());
							}
							
							dealModels.add(dealModel);
						}
					}
					
				}
			}
		
			}
	 // jeeyoh group
		if(jeeyohGroup != null)
		  {
			   for(Jeeyohgroup jeeyohGroup1 : jeeyohGroup) {
				   
				   Set<Groupusermap> groups   =jeeyohGroup1.getGroupusermaps();
				  
				   for (Groupusermap groups1 : groups)
				   {
					   User groupMember = groups1.getUser();
					   List<Deals> deals2 = dealsDAO.getUserDeals(groupMember.getUserId());
					   if(deals2 != null)
						{
							
							for (Deals deal : deals2)
							{
								if(deal != null)
								{
									DealModel dealModel = new DealModel();
									if(deal.getTitle() != null)
									{
										dealModel.setTitle(deal.getTitle());
									}
									if(deal.getDealUrl() != null)
									{
										dealModel.setDealUrl(deal.getDealUrl());
									}
									if(deal.getStartAt() != null)
									{
										dealModel.setStartAt(deal.getStartAt().toString());
									}
									if(deal.getEndAt() != null)
									{
										dealModel.setEndAt(deal.getEndAt().toString());
									}
									if(deal.getStatus() != null)
									{
										dealModel.setStatus(deal.getStatus());
									}
									
									dealModels.add(dealModel);
								}
							}
						}
				   }
			   }
		  }
		// user cat likes
		List<UserCategory> userCategoryList = userDAO.getUserCategoryLikesById(user.getUserId());
		  if(userCategoryList != null)
		  {
		   for(UserCategory userCategory : userCategoryList) {
			   
			   List<Deals> catDeals = dealsDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
			   for(Deals deal:catDeals)
			   {
				   if(deal != null)
					{
					   DealModel dealModel = new DealModel();
						if(deal.getTitle() != null)
						{
							dealModel.setTitle(deal.getTitle());
						}
						if(deal.getDealUrl() != null)
						{
							dealModel.setDealUrl(deal.getDealUrl());
						}
						if(deal.getStartAt() != null)
						{
							dealModel.setStartAt(deal.getStartAt().toString());
						}
						if(deal.getEndAt() != null)
						{
							dealModel.setEndAt(deal.getEndAt().toString());
						}
						if(deal.getStatus() != null)
						{
							dealModel.setStatus(deal.getStatus());
						}
						
						dealModels.add(dealModel);
						
					}
			   }
			   
		   }
		  }
		logger.debug("deal Model size after community " + dealModels.size());
		
	}
		Set<DealModel> aSet = new HashSet<DealModel>(dealModels);		
		logger.debug("SIZE OF SET :::: "+aSet.size());
		
		return aSet;
	}

}
