package com.jeeyoh.service.search;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;

@Component("dealSearch")
public class DealSearch implements IDealSearch {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IDealsDAO dealDAO;
	
	@Autowired
	private IBusinessDAO businessDAO;
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void search() {
		
		logger.debug("ENTER IN DEAL data :: ");
		Businesstype businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
		if(businesstype != null)
		{
			//get user detail and deal usage
			User user = userDAO.getUsersById("gaurav.shandilya@gmail.com");
			Set<Dealsusage> dealUsage = user.getDealsusages();	
			
			// get user contects				
			List<User> userContacts = userDAO.getUserContacts(user.getUserId());
			
			// user commuinty 
			List<Page> userCommunities = userDAO.getUserCommunities(user.getUserId());
						
			Set<Business> businessSet = businesstype.getBusinesses();
			for(Business business :businessSet )
			{
				logger.debug("Business data :: "+business.getCity());
				if(business != null)
				{
					
					Set<Deals> deals = business.getDealses();	
					for(Deals deal :deals )
					{
						
						// rules level 1 , level 2 , level3 or  level 6
						boolean saveDeal = false;						
						for(Dealsusage dealsusage : dealUsage) {
							//logger.debug("DealSearch ==> search ==> dealusage ==> " + dealsusage.getIsFavorite());
							if(deal.getId() == dealsusage.getDeals().getId())
							{
								if(dealsusage.getIsFavorite() || dealsusage.getIsLike())
								{
									logger.debug("Cross basic three level3",deal.getId());
									saveDeal = true;
									// Get time---
									Date date = new Date();
									// add top of deal suggestion box
									Userdealssuggestion dealSuggestion = new Userdealssuggestion();
									dealSuggestion.setCreatedtime(date);
									dealSuggestion.setDeals(deal);
									dealSuggestion.setIsFavorite(true);
									dealSuggestion.setIsFollowing(true);
									dealSuggestion.setIsLike(true);
									dealSuggestion.setIsRedempted(true);
									dealSuggestion.setUpdatedtime(date);
									dealSuggestion.setUser(user);
									dealDAO.saveSuggestions(dealSuggestion);						
									 									
									
								}
								
							}
							
						}
						if(!saveDeal)
						{
							logger.debug("IN COMMUNITY:");							
							if(userCommunities != null) {
								
								for(Page community : userCommunities) {
									
									int dealbusinessId = deal.getBusiness().getId();
									int pagebusinessId = community.getBusiness().getId();
									if( dealbusinessId == pagebusinessId)
									{
										logger.debug("ENTRY HERE :::: ");
										saveDeal = true;
										// Get time---
										Date date = new Date();
										// add top of deal suggestion box
										Userdealssuggestion dealSuggestion = new Userdealssuggestion();
										dealSuggestion.setCreatedtime(date);
										dealSuggestion.setDeals(deal);
										dealSuggestion.setIsFavorite(true);
										dealSuggestion.setIsFollowing(true);
										dealSuggestion.setIsLike(true);
										dealSuggestion.setIsRedempted(true);
										dealSuggestion.setUpdatedtime(date);
										dealSuggestion.setUser(user);
										dealDAO.saveSuggestions(dealSuggestion);
									}
									
								}
							}
						}
						
						//get user contacts group for rules level 4
						if(!saveDeal)
						{
								if(userContacts != null) {
									for(User contact : userContacts) {
										if(contact != null) {
											logger.debug("contact 4 ==> "+contact.getFirstName());
											Set<Dealsusage> contactDealUsage = contact.getDealsusages();									
											for(Dealsusage dealsusage1 : contactDealUsage) {
												logger.debug("DEAL LEVEL 4 ==> "+ dealsusage1.getIsFavorite());
										
												int dealId = deal.getId();
												int dealusageId = dealsusage1.getDeals().getId();
												if(dealId == dealusageId)
												{
													if(dealsusage1.getIsFavorite() || dealsusage1.getIsLike() || dealsusage1.getIsSuggested() || dealsusage1.getIsFollowing())
													{
														//logger.debug("Deal in level 4 :: = > "+deal.getId());
														Date date = new Date();
														Userdealssuggestion dealSuggestion = new Userdealssuggestion();
														dealSuggestion.setCreatedtime(date);
														dealSuggestion.setDeals(deal);
														dealSuggestion.setIsFavorite(true);
														dealSuggestion.setIsFollowing(true);
														dealSuggestion.setIsLike(true);
														dealSuggestion.setIsRedempted(true);
														dealSuggestion.setUpdatedtime(date);
														dealSuggestion.setUser(user);
														dealDAO.saveSuggestions(dealSuggestion);
													}
											   }
											}
											
										}
									}
								}
						}
						
						
												
						}
					}
										
				}
			}
							
		}
						
				
}
