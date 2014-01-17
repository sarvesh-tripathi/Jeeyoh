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
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
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
	public Set<Deals> getDeals(String keyword, String category, String location, String emailId) {
		// TODO Auto-generated method stub
		logger.debug("Get deal ::: ");
		List<Deals> deals =  null;
		
		List<Userdealssuggestion> dealSuggestionList = null;
		if(emailId != null && emailId.length()  != 0)
		{			
			User user = userDAO.getUsersById(emailId);
			logger.debug("user Id :::: "+ user.getUserId());
			dealSuggestionList = dealsDAO.userDealsSuggestedByJeeyoh(keyword, category,
					location, user.getUserId());//getDealsSuggestion(user.getUserId());
		}
		else
		{
			deals = dealsDAO.getDealsByKeywords(keyword,category,location);
		}
		
		//List<DealModel> dealModels = new ArrayList<DealModel>();
		Set<Deals> dealModels = new HashSet<Deals>();
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
							dealModels.add(dealsuggestion.getDeals());
						}
						
					}
					
				}
				else
				{
					if(deal != null)
					{
						dealModels.add(deal);
					}
				}
				
				
				
			}
			
		}
		else
		{
			logger.debug("USER DEALS ::::::::"+dealSuggestionList.size());
			if(dealSuggestionList != null)
			{
				int preId = 0;
				for(Userdealssuggestion dealsuggestion : dealSuggestionList)
				{
			
					if(dealsuggestion.getDeals() != null)
					{
						dealModels.add(dealsuggestion.getDeals());
					}
			    }
					
				}
		}
	
		return dealModels;
		
	}

	@Override
	@Transactional
	public Set<Deals> getUserDeals(String emailId) {
		
		List<Deals> deals = null;
		//List<DealModel> dealModels = new ArrayList<DealModel>();
		Set<Deals> dealModels = new HashSet<Deals>();
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
				
				if(deal != null)
				{
					dealModels.add(deal);
				}
			}
			
		}
		
		return dealModels;
	}
	
	@Override
	@Transactional
	public Set<Deals> getUserContactAndCommunityDeals(String emailId){
		
		List<Deals> deals = null;
		List<Deals> dealModels = new ArrayList<Deals>();
		//Set<DealModel> dealModels = new HashSet<DealModel>();
		if(emailId != null && emailId  != "")
		{			
			User user = userDAO.getUsersById(emailId);
			logger.debug("user Id :::: "+ user.getUserId());
			
			// get user contects				
			List<User> userContacts = userDAO.getUserContacts(user.getUserId());
			
			// user commuinty 
			List<Page> userCommunities = userDAO.getUserCommunities(user.getUserId());
			
			// user group member 
			List<Jeeyohgroup> jeeyohGroup = userDAO.getUserGroups(user.getUserId());
						
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
								
								dealModels.add(deal);
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
							dealModels.add(deal);
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
									dealModels.add(deal);
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
			   
			   List<Deals> catDeals = dealsDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType());
			   for(Deals deal:catDeals)
			   {
				   if(deal != null)
					{
						dealModels.add(deal);
						
					}
			   }
			   
		   }
		  }
		logger.debug("deal Model size after community " + dealModels.size());
		
	}
		Set<Deals> aSet = new HashSet<Deals>(dealModels);		
		logger.debug("SIZE OF SET :::: "+aSet.size());
		
		return aSet;
	}

}
