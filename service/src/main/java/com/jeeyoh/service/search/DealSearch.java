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
import com.jeeyoh.persistence.domain.User;
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
	
	
	@Override
	@Transactional
	public void search() {
		
		
		Businesstype businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
		if(businesstype != null)
		{
			//get user detail and deal usage
			User user = userDAO.getUsersById("gaurav.shandilya@gmail.com");
			Set<Dealsusage> dealUsage = user.getDealsusages();	
			// get user contects
			
			logger.debug("ID OF USER ::: "+user.getUserId());
			List<User> userContacts = userDAO.getUserContacts(user.getUserId());
			
			logger.debug("CHECK CONTACT ::::: "+userContacts);
			logger.debug("CHECK CONTACT 2::::: "+userContacts.get(0).getFirstName());
			
			Set<Business> businessSet = businesstype.getBusinesses();
			for(Business business :businessSet )
			{
				logger.debug("Business data :: "+business.getCity());
				if(business != null)
				{
					//List<Deals> deals = dealDAO.getDealsByBusinessId(business.getId().toString());
					Set<Deals> deals = business.getDealses();	
					for(Deals deal :deals )
					{
						logger.debug("Deals for resturent :: "+deal.getId());
						// rules level 1 to level 3
						for(Dealsusage dealsusage : dealUsage) {
							logger.debug("DealSearch ==> search ==> dealusage ==> " + dealsusage.getIsFavorite());
							if(deal.getId() == dealsusage.getDeals().getId())
							{
								if(dealsusage.getIsFavorite() || dealsusage.getIsLike())
								{
									logger.debug("Cross basic three level3",deal.getId());
									// Get time---
									Date date = new Date();
									// add top of deal suggestion box
									/*Userdealssuggestion dealSuggestion = new Userdealssuggestion();
									dealSuggestion.setCreatedtime(date);
									dealSuggestion.setDeals(deal);
									dealSuggestion.setIsFavorite(true);
									dealSuggestion.setIsFollowing(true);
									dealSuggestion.setIsLike(true);
									dealSuggestion.setIsRedempted(true);
									dealSuggestion.setUpdatedtime(date);
									dealSuggestion.setUser(user);
									dealDAO.saveSuggestions(dealSuggestion);*/
									//Business business = new business();
									Usernondealsuggestion nondeal = new Usernondealsuggestion();
									nondeal.setBusiness(business);
									nondeal.setCreatedtime(date);
									nondeal.setIsChecked(true);
									nondeal.setIsRelevant(true);
									nondeal.setUpdatedtime(date);
									nondeal.setUser(user);
									dealDAO.saveNonDealSuggestion(nondeal);
									 									
									
								}
							}
							
						}
						//get user contacts for rules level 4			
						/*if(userContacts != null) {
							for(User contact : userContacts) {
								if(contact != null) {
									logger.debug("contact 4 ==> "+contact.getFirstName());
									Set<Dealsusage> contactDealUsage = contact.getDealsusages();									
									for(Dealsusage dealsusage1 : contactDealUsage) {
										logger.debug("DEAL LEVEL 4 ==> "+ dealsusage1.getIsFavorite());
								
										if(deal.getId() == dealsusage1.getDeals().getId())
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
						}*/
												
						}
					}
										
				}
			}
							
		}
						
				
}
