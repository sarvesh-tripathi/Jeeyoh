package com.jeeyoh.service.search;

import java.util.ArrayList;
<<<<<<< HEAD
import java.util.HashSet;
=======
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
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
<<<<<<< HEAD
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
=======
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
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
<<<<<<< HEAD
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
=======
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
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
		if(deals != null)
		{
			
			for (Deals deal : deals)
			{
<<<<<<< HEAD
				//DealModel dealModel =  new DealModel();
=======
				DealModel dealModel =  new DealModel();
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
				if(dealSuggestionList != null)
				{
					for(Userdealssuggestion dealsuggestion : dealSuggestionList)
					{
<<<<<<< HEAD
						if(dealsuggestion.getDeals() != null)
						{
							dealModels.add(dealsuggestion.getDeals());
						}
						
=======
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
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
					}
					
				}
				else
				{
<<<<<<< HEAD
					if(deal != null)
					{
						dealModels.add(deal);
=======
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
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
					}
				}
				
				
				
			}
			
		}
<<<<<<< HEAD
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
=======
		return dealModels;
		
	}
	
	@Override
	@Transactional
	public List<DealModel> getUserDeals(String emailId) {
		
		List<Deals> deals = null;
		List<DealModel> dealModels = new ArrayList<DealModel>();
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
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
<<<<<<< HEAD
				
				if(deal != null)
				{
					dealModels.add(deal);
=======
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
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
				}
			}
			
		}
		
		return dealModels;
	}
	
	@Override
	@Transactional
<<<<<<< HEAD
	public Set<Deals> getUserContactAndCommunityDeals(String emailId){
		
		List<Deals> deals = null;
		List<Deals> dealModels = new ArrayList<Deals>();
		//Set<DealModel> dealModels = new HashSet<DealModel>();
=======
	public List<DealModel> getUserContactAndCommunityDeals(String emailId){
		
		List<Deals> deals = null;
		List<DealModel> dealModels = new ArrayList<DealModel>();
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
		if(emailId != null && emailId  != "")
		{			
			User user = userDAO.getUsersById(emailId);
			logger.debug("user Id :::: "+ user.getUserId());
			
			// get user contects				
			List<User> userContacts = userDAO.getUserContacts(user.getUserId());
			
			// user commuinty 
			List<Page> userCommunities = userDAO.getUserCommunities(user.getUserId());
<<<<<<< HEAD
			
			// user group member 
			List<Jeeyohgroup> jeeyohGroup = userDAO.getUserGroups(user.getUserId());
=======
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
						
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
<<<<<<< HEAD
							if(deal != null)
							{
								
								dealModels.add(deal);
=======
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
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
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
<<<<<<< HEAD
						
						if(deal != null)
						{
							dealModels.add(deal);
=======
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
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
						}
					}
					
				}
			}
		
			}
<<<<<<< HEAD
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
=======
		
		
		logger.debug("deal Model size after community " + dealModels.size());
		
	}
		return dealModels;
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
	}

}
