package com.jeeyoh.service.search;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.service.groupon.GrouponService;

@Component("nonDealSearch")
public class NonDealSearch implements INonDealSearch {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");

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
	@Transactional
	public void search() {
		List<User> userList = userDAO.getUsers();
		logger.debug("NonDealSearch ==> search ==> ");
		if(userList != null) {
			for(User user : userList) {
				logger.debug("NonDealSearch ==> search ==> userID ==> " + user.getEmailId());
				int userId = user.getUserId();
				if(userId == 1) {
				List<Page> userCommunities = userDAO.getUserCommunities(userId);				
				List<Jeeyohgroup> userGroups = userDAO.getUserGroups(userId);
				if(userCommunities != null) {
					boolean includePage = true;
					for(Page community : userCommunities) {
						logger.debug("NonDealSearch ==> search ==> pageId ==> " + community.getPageId());
						List<Pagetype> pageTypeList = userDAO.getCommunityType(community.getPageId());
						
						Pagetype pageType = null;
						if(pageTypeList != null) {
							pageType = pageTypeList.get(0);
						}
						logger.debug("NonDealSearch ==> search ==> pagetype ==> " + pageType);						
						if(pageType != null) {
							String type = pageType.getPageType();
							logger.debug("NonDealSearch ==> search ==> type ==> " + type);
							if(type.equalsIgnoreCase(MOVIE_CATEGORY) || type.equalsIgnoreCase(RESTAURANT_CATEGORY) || type.equalsIgnoreCase(NIGHTLIFE_CATEGORY) || type.equalsIgnoreCase(EVENTS_CATEGORY) || type.equalsIgnoreCase(GETAWAYS_CATEGORY) || type.equalsIgnoreCase(SPORTS_CATEGORY)) {
								logger.debug("NonDealSearch ==> search ==> type ==> " + type);
								List<Business> businessList = businessDAO.getBusinessById(community.getPageId());
								Business business = new Business();
								if(businessList != null) {
									business = businessList.get(0);
								}
								//Business business = businessDAO.getBusinessById(community.getPageId());
								String ambiance = business.getAmbience();
								String musicType = business.getMusicType();
								long rating = business.getRating();
								List<Pageuserlikes> pageProperties = userDAO.getUserPageProperties(userId, community.getPageId());
								if(pageProperties != null)
								{
									logger.debug("NonDealSearch ==> search ==> pageProperties ==> not null" );
									Pageuserlikes pageProperty = pageProperties.get(0);
									if(pageProperty != null) {
										logger.debug("NonDealSearch ==> search ==> pageProperty ==> not null" );
										boolean isLiked = pageProperty.getIsLike();
										boolean isFavorite = pageProperty.getIsFavorite();
										boolean isVisited = pageProperty.getIsVisited();
										boolean isFollowing = pageProperty.getIsFollowing();
										if(isLiked || isFavorite || isVisited || isFollowing) {											
											includePage = true;
										} else
										{
											includePage = false;
										}
										logger.debug("NonDealSearch ==> search ==> pageProperty ==> includePage ==> " + includePage);
									}
								} else
								{
									includePage = false;
								}
								logger.debug("NonDealSearch ==> search ==> pageProperties ==> includePage ==> " + includePage);
								logger.debug("NonDealSearch ==> search ==> rating ==> " + rating);
								if(rating > 3) {
									if(type.equalsIgnoreCase(RESTAURANT_CATEGORY)) {
										if(!ambiance.equalsIgnoreCase("GOOD") && !musicType.equalsIgnoreCase("SOFT")) {
											includePage = false;
										} else {
											includePage = true;
										}
									} else {
										includePage = true;
									}
								}
								logger.debug("NonDealSearch ==> search ==> rating ==> includePage ==> " + includePage);
								if(includePage) {
									Usernondealsuggestion suggestion = new Usernondealsuggestion();									
									suggestion.setBusiness(business);
									suggestion.setCreatedtime(new Date());
									suggestion.setIsChecked(false);
									suggestion.setIsRelevant(true);
									suggestion.setUpdatedtime(new Date());
									suggestion.setUser(user);
									userDAO.saveNonDealSuggestions(suggestion);
								}
							} else {
								List<User> userContacts = userDAO.getUserContacts(userId);
								if(userContacts != null) {
									for(User contact : userContacts) {
										if(contact != null) {
											int contactId = contact.getUserId();
											//List<Page> userContactPages = userDAO.getUserContactsCommunities(contactId);
											List<Page> userContactPages = userDAO.getUserCommunities(contactId);
											if(userContactPages != null) {
												for(Page contactPage : userContactPages) {
													if(contactPage != null) {
														Pagetype contactPageType = contactPage.getPagetype();
														String type1 = contactPageType.getPageType();
														if(type1.equalsIgnoreCase(MOVIE_CATEGORY) || type1.equalsIgnoreCase(RESTAURANT_CATEGORY) || type1.equalsIgnoreCase(NIGHTLIFE_CATEGORY) || type1.equalsIgnoreCase(EVENTS_CATEGORY) || type1.equalsIgnoreCase(GETAWAYS_CATEGORY) || type1.equalsIgnoreCase(SPORTS_CATEGORY)) {
															Business business = community.getBusiness();
															String ambiance = business.getAmbience();
															String musicType = business.getMusicType();
															long rating = business.getRating();
															List<Pageuserlikes> pageProperties = userDAO.getUserPageProperties(userId, community.getPageId());
															if(pageProperties != null)
															{
																Pageuserlikes pageProperty = pageProperties.get(0);
																if(pageProperty != null) {
																	boolean isLiked = pageProperty.getIsLike();
																	boolean isFavorite = pageProperty.getIsFavorite();
																	boolean isVisited = pageProperty.getIsVisited();
																	boolean isFollowing = pageProperty.getIsFollowing();
																	if(isLiked || isFavorite || isVisited || isFollowing) {
																		includePage = true;
																	} else
																	{
																		includePage = false;
																	}
																}
															} else
															{
																includePage = false;
															}
															if(rating > 3) {
																if(type.equalsIgnoreCase(RESTAURANT_CATEGORY)) {
																	if(!ambiance.equalsIgnoreCase("GOOD") && !musicType.equalsIgnoreCase("SOFT")) {
																		includePage = false;
																	} else {
																		includePage = true;
																	}
																} else {
																	includePage = true;
																}
															}
															if(includePage) {
																Usernondealsuggestion suggestion = new Usernondealsuggestion();									
																suggestion.setBusiness(community.getBusiness());
																suggestion.setCreatedtime(new Date());
																suggestion.setIsChecked(false);
																suggestion.setIsRelevant(true);
																suggestion.setUpdatedtime(new Date());
																suggestion.setUser(user);
																userDAO.saveNonDealSuggestions(suggestion);
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
	}
}
