package com.jeeyoh.service.jobs;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;

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
				boolean isContactsAccessed = false;
				double[] array = null;
				logger.debug("Lat/Long for user :  " + user.getLattitude() +" , "+user.getLongitude());
				if(user.getLattitude() == null && user.getLongitude() == null || (user.getLattitude().trim().equals("") && user.getLongitude().trim().equals(""))|| (user.getLattitude().trim().equals("0.0") && user.getLongitude().trim().equals("0.0")))
				{
					array = getLatLong(user.getZipcode());
					logger.debug("Lat/long length ==> " + Double.toString(array[0]).length()+" : "+Double.toString(array[1]).length());
					user.setLattitude(Double.toString(array[0]));
					user.setLongitude(Double.toString(array[1]));
				}
				saveNonDealSuggestion(userId, user, true, isContactsAccessed);
			}
		}
	}

	/**
	 * Get distance of location from current location
	 * @param lat2
	 * @param lon2
	 * @param unit
	 * @return
	 */
	private double distance(double lat1, double lon1,double lat2, double lon2, String unit)
	{

		if(lat1 !=0)
		{
			double theta = lon1 - lon2;
			double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
			dist = Math.acos(dist);
			dist = rad2deg(dist);
			dist = dist * 60 * 1.1515;
			if (unit == "K") 
			{
				dist = dist * 1.609344;
			} else if (unit == "N") 
			{
				dist = dist * 0.8684;
			}
			return Math.round(dist);
		}
		else
		{
			return 0;
		}
	}

	/**
	 * This function converts decimal degrees to radians  
	 * @param deg
	 * @return
	 */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}


	/**
	 * This function converts radians to decimal degrees   
	 * @param rad
	 * @return
	 */
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}


	/**
	 * Get latitude and longitude for ZipCode
	 * @param postCode
	 */
	private double[] getLatLong(String postCode)
	{
		double[] array = new double[2];
		try
		{
			logger.debug("Lat/Long :  "+postCode);
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(postCode).getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			List<GeocoderResult> results = geocoderResponse.getResults();
			float latitude = results.get(0).getGeometry().getLocation().getLat().floatValue();
			float longitude = results.get(0).getGeometry().getLocation().getLng().floatValue();

			array[0] = (double)latitude;
			array[1] = (double)longitude;
			logger.debug("Lat/Long :  " + latitude +" , "+longitude);

		}catch (Exception e) {
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return array;
	}


	/**
	 * Save Non deal suggestions
	 * @param cmmunity
	 * @param userId
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	private void saveNonDealSuggestion(int userId, User user, boolean forUser, boolean isContactsAccessed)
	{
		int countMain = 0;
		double[] array = null;
		if(user.getLattitude() == null && user.getLongitude() == null || (user.getLattitude().trim().equals("") && user.getLongitude().trim().equals("")) || (user.getLattitude().trim().equals("0.0") && user.getLongitude().trim().equals("0.0")))
		{
			array = getLatLong(user.getZipcode());
			logger.debug("Lat/long length ==> " + Double.toString(array[0]).length()+" : "+Double.toString(array[1]).length());
			user.setLattitude(Double.toString(array[0]));
			user.setLongitude(Double.toString(array[1]));
		}
		
		List<UserCategory> userCategoryList = userDAO.getUserCategoryLikesById(userId);
		if(userCategoryList != null)
		{
			for(UserCategory userCategory : userCategoryList) {
				
				if(userCategory.getItemCategory().equalsIgnoreCase(MOVIE_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(RESTAURANT_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(NIGHTLIFE_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(EVENTS_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(GETAWAYS_CATEGORY) || userCategory.getItemCategory().equalsIgnoreCase(SPORTS_CATEGORY)) {
					
					List<Business> businessList = businessDAO.getBusinessByuserLikes(userCategory.getItemType(),userCategory.getItemCategory());
					if(businessList != null)
					{
						boolean includePage = true;
						for(Business business : businessList)
						{
							List<Usernondealsuggestion> usernondealsuggestions = userDAO.isNonDealSuggestionExists(user.getUserId(), business.getId());
							logger.debug("NonDealSearch ==> usernondealsuggestions ==> " + usernondealsuggestions);
							if(usernondealsuggestions == null || usernondealsuggestions.size() == 0)
							{
							String type = business.getBusinesstype().getBusinessType();
							if(type.equalsIgnoreCase(MOVIE_CATEGORY) || type.equalsIgnoreCase(RESTAURANT_CATEGORY) || type.equalsIgnoreCase(NIGHTLIFE_CATEGORY) || type.equalsIgnoreCase(EVENTS_CATEGORY) || type.equalsIgnoreCase(GETAWAYS_CATEGORY) || type.equalsIgnoreCase(SPORTS_CATEGORY)) {
								
								if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
								{
									array = getLatLong(business.getPostalCode());
									business.setLattitude(Double.toString(array[0]));
									business.setLongitude(Double.toString(array[1]));
								}

								double distance = distance(Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
								logger.debug("Distance::  "+distance +" lat::  "+user.getLattitude()+" lon::  "+user.getLongitude());
								if(distance <=15)
								{
									String ambiance = "";
									if(business.getAmbience() != null)
										ambiance = business.getAmbience();
									String musicType = "";
									if(business.getAmbience() != null)
										musicType = business.getMusicType();

									long rating = 0;
									if(business.getRating() != null)
									{
										rating = business.getRating();
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
									logger.debug("NonDealSearch ==> search ==> rating ==> includePage ==> " + includePage);
									if(includePage) {
										Usernondealsuggestion suggestion = new Usernondealsuggestion();									
										suggestion.setBusiness(business);
										suggestion.setCreatedtime(new Date());
										suggestion.setIsChecked(false);
										suggestion.setIsRelevant(true);
										suggestion.setUpdatedtime(new Date());
										suggestion.setUser(user);
										userDAO.saveNonDealSuggestions(suggestion,countMain);
										countMain ++;
									}
								}
							}
						}
						}
					}
				}
			}
		}
		
		if(forUser)
		{
			List<Jeeyohgroup> groups = userDAO.getUserGroups(userId);
			if(groups != null)
			{
				for(Jeeyohgroup jeeyohgroup : groups)
				{
					Set<Groupusermap> groupusermapList = jeeyohgroup.getGroupusermaps();
					for(Groupusermap groupusermap : groupusermapList)
					{
						int userid = groupusermap.getUser().getUserId();
						logger.debug("USERID for userGroup::  "+userid);
						saveNonDealSuggestion(userid, user, false , false);
					}
				}
			}
		}
		
		List<Page> userCommunities = userDAO.getUserCommunities(userId);
		logger.debug("NonDealSearch ==> userCommunities ==> size ==> " + userCommunities.size());

		if(userCommunities != null) {
			boolean includePage = true;
			for(Page community : userCommunities) {
				
				List<Usernondealsuggestion> usernondealsuggestions = userDAO.isNonDealSuggestionExists(user.getUserId(), community.getBusiness().getId());
				logger.debug("NonDealSearch ==> usernondealsuggestions ==> " + usernondealsuggestions);
				if(usernondealsuggestions == null || usernondealsuggestions.size() == 0)
				{
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
							List<Business> businessList = businessDAO.getBusinessById(community.getBusiness().getId());
							Business business = null;
							if(businessList != null) {
								business = businessList.get(0);
							}

							logger.debug("Lat/Long for business :  " + business.getLattitude() +" , "+business.getLongitude());
							logger.debug("Lat/Long for business :  " + business.getLattitude() +" , "+business.getLongitude()+" Lat/Long for user:  "+user.getLattitude()+" , "+user.getLongitude());
							if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
							{
								array = getLatLong(business.getPostalCode());
								business.setLattitude(Double.toString(array[0]));
								business.setLongitude(Double.toString(array[1]));
							}

							double distance = distance(Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
							logger.debug("Distance::  "+distance +" lat::  "+user.getLattitude()+" lon::  "+user.getLongitude());
							if(distance <=15)
							{
								String ambiance = "";
								if(business.getAmbience() != null)
									ambiance = business.getAmbience();
								String musicType = "";
								if(business.getAmbience() != null)
									musicType = business.getMusicType();

								long rating = 0;
								if(business.getRating() != null)
								{
									rating = business.getRating();
								}
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
								if(!includePage)
								{
									List<Events> eventsList = userDAO.getUserCommunityEvents(userId, community.getPageId());
									if(eventsList != null)
									{
										for(Events event : eventsList) {

											List<Eventuserlikes> eventproperties = userDAO.getUserEventProperties(userId, event.getEventId());
											if(eventproperties != null)
											{
												logger.debug("NonDealSearch ==> search ==> eventProperty ==> not null" );
												Eventuserlikes eventProperty = eventproperties.get(0);
												if(eventProperty != null) {
													logger.debug("NonDealSearch ==> search ==> eventProperty ==> not null" );
													boolean isLiked = eventProperty.getIsLike();
													boolean isFavorite = eventProperty.getIsFavorite();
													boolean isVisited = eventProperty.getIsVisited();
													boolean isFollowing = eventProperty.getIsFollowing();
													if(isLiked || isFavorite || isVisited || isFollowing) {											
														includePage = true;
														break;
													} else
													{
														includePage = false;
													}
													logger.debug("NonDealSearch ==> search ==> eventProperty ==> includePage ==> " + includePage);
												}
											} else
											{
												includePage = false;
											}
										}
									}
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
									userDAO.saveNonDealSuggestions(suggestion,countMain);
									countMain ++;
								}
							}
						}
					}
				}
			}
		}

		if(forUser && !isContactsAccessed)
		{
			logger.debug("Else..................");
			isContactsAccessed = true;
			List<User> userContacts = userDAO.getUserContacts(userId);
			List<User> userList = userDAO.getUserById(userId);
			logger.debug("Contacts Size..............==> "+userContacts.size());
			if(userContacts != null) {
				for(User contact : userContacts) {
					if(contact != null) {
						int contactId = contact.getUserId();
						saveNonDealSuggestion(contactId, userList.get(0), false , isContactsAccessed);
					}
				}
			}
		}
	}

}
