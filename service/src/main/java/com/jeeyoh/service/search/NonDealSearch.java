package com.jeeyoh.service.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.User;

@Component("nonDealSearch")
public class NonDealSearch implements INonDealSearch {

	public final String MOVIE_CATEGORY = "MOVIE";
	public final String RESTAURANT_CATEGORY = "RESTAURANT";
	public final String NIGHTLIFE_CATEGORY = "NIGHT LIFE";
	public final String EVENTS_CATEGORY = "EVENT";
	public final String GETAWAYS_CATEGORY = "GETAWAYS";
	public final String SPORTS_CATEGORY = "SPORT";
	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IBusinessDAO businessDAO;

	@Override
	public void search() {
		List<User> userList = userDAO.getUsers();
		if(userList != null) {
			for(User user : userList) {
				int userId = user.getUserId();
				
				List<Page> userCommunities = userDAO.getUserCommunities(userId);				
				List<Jeeyohgroup> userGroups = userDAO.getUserGroups(userId);
				if(userCommunities != null) {
					for(Page community : userCommunities) {
						Pagetype pageType = community.getPagetype();
						if(pageType != null) {
							String type = pageType.getPageType();
							if(type.equalsIgnoreCase(MOVIE_CATEGORY) || type.equalsIgnoreCase(RESTAURANT_CATEGORY) || type.equalsIgnoreCase(NIGHTLIFE_CATEGORY) || type.equalsIgnoreCase(EVENTS_CATEGORY) || type.equalsIgnoreCase(GETAWAYS_CATEGORY) || type.equalsIgnoreCase(SPORTS_CATEGORY)) {
								Business business = community.getBusiness();
								String ambiance = business.getAmbience();
								String musicType = business.getMusicType();
								long rating = business.getRating();
							} else {
								List<User> userContacts = userDAO.getUserContacts(userId);
								if(userContacts != null) {
									for(User contact : userContacts) {
										if(contact != null) {
											int contactId = contact.getUserId();
											List<Page> userContactPages = userDAO.getUserContactsCommunities(contactId);
											if(userContactPages != null) {
												for(Page contactPage : userContactPages) {
													if(contactPage != null) {
														Pagetype contactPageType = contactPage.getPagetype();
														String type1 = contactPageType.getPageType();
														if(type1.equalsIgnoreCase(MOVIE_CATEGORY) || type1.equalsIgnoreCase(RESTAURANT_CATEGORY) || type1.equalsIgnoreCase(NIGHTLIFE_CATEGORY) || type1.equalsIgnoreCase(EVENTS_CATEGORY) || type1.equalsIgnoreCase(GETAWAYS_CATEGORY) || type1.equalsIgnoreCase(SPORTS_CATEGORY)) {
															
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

	}
}
