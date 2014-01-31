package com.jeeyoh.service.jobs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.persistence.domain.Usercontacts;
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
	
	@Override
	@Transactional
	public void search() {
		
		/*String[] type = {"RESTAURANT","SPA","SPORT"};
		List<Businesstype> businesstypes = businessDAO.getBusinesstypeByTypeArray(type);	*/
		List<User> userList = userDAO.getUsers();
		// Iterate user list for suggestion
		int count = 0;
		for (User user : userList)
		{
			if(user != null)
			{
				  //for user Contacts affinity level
				  List<Usercontacts> userContactsList = userDAO.getAllUserContacts(user.getUserId());
	              saveDealSuggestion(user,user,false,false);
				  if(userContactsList != null)
					{
						for(Usercontacts usercontacts:userContactsList)
						{
							
							Boolean isStar = usercontacts.getIsStar();
							User contact = usercontacts.getUserByContactId();
							logger.debug("Friend Name ::"+contact.getFirstName());
							logger.debug("IS STAR ::"+isStar);
							saveDealSuggestion(contact,user,true,false);
							
						}
					}
			}
			count++;
			if(count == 5)
			{
				break;
			}
		}
				
		
							
	}
	
	@SuppressWarnings("unchecked")
	private void saveDealSuggestion(User user,User userFor,Boolean contactFlag,Boolean isStar)
	{
		
		//get user detail and deal usage			
		Set<Dealsusage> dealUsage = user.getDealsusages();		
			
		// user commuinty 
		List<Page> userCommunities = userDAO.getUserCommunities(user.getUserId());
		
		//user category
		
		List<UserCategory> userCategoryList = userDAO.getUserCategoryLikesById(user.getUserId());
		// user group member
		List<Jeeyohgroup> jeeyohGroup = null;
		if(!contactFlag)
		{
			jeeyohGroup = userDAO.getUserGroups(user.getUserId());
		}
	   
	    double[] array = null;		
		for(Dealsusage dealsusage : dealUsage) {
			//logger.debug("DealSearch ==> search ==> dealusage ==> " + dealsusage.getIsFavorite());
			Deals deal = dealsusage.getDeals();
			List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), deal.getId());
			if(userdealsuggestions == null || userdealsuggestions.size() == 0)
			{
				    Business business = deal.getBusiness();
				    if(business != null)
					{
						
							if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
							{
								array = getLatLong(business.getPostalCode());
								business.setLattitude(Double.toString(array[0]));
								business.setLongitude(Double.toString(array[1]));
							}
							double distance = distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
							logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
							if(distance <= 50)
							{
								int likeCount = dealDAO.getDealsLikeCounts(dealsusage.getDeals().getId());
								logger.debug("Like COUNT :::::"+likeCount);
								if(dealsusage.getIsFavorite() || dealsusage.getIsLike() || likeCount >= 2)
								{
									logger.debug("Cross basic three level3",deal.getId());									
									saveDealsSuggestionInDataBase(deal,userFor);
									
								}
							}
						
						
			        }
			}
			
		}
			//	For Community							
			if(userCommunities != null) {
				
				for(Page community : userCommunities) {
					
					Business business = community.getBusiness();
				    if(business != null)
					{
						
							if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
							{
								array = getLatLong(business.getPostalCode());
								business.setLattitude(Double.toString(array[0]));
								business.setLongitude(Double.toString(array[1]));
							}
							double distance = distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
							logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
							if(distance <= 50)
							{
								List<Deals> deals = dealDAO.getDealsByBusinessId(business.getId());
								for(Deals deal : deals)
								{
									List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), deal.getId());
									if(userdealsuggestions == null || userdealsuggestions.size() == 0)
									{
										saveDealsSuggestionInDataBase(deal,userFor);
									}
								}
							}
						
					}
					
				}
			}
		
								
				// for user category likes item
							
				
					///List<UserCategory> userCategoryList = userDAO.getUserCategoryLikesById(user.getUserId());
					  if(userCategoryList != null)
					  {
					     for(UserCategory userCategory : userCategoryList) {
						    UserCategoryLikes userCategoryLikes = (UserCategoryLikes)userCategory.getUserCategoryLikes().iterator().next();
							Date  weekendDate = getNearestWeekend();
							Calendar cal = Calendar.getInstance();
							cal.setTime(userCategoryLikes.getCreatedTime());
							cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						    logger.debug("Date Comparison: "+cal.getTime()+" : "+weekendDate);
						    logger.debug("Date Comparison: "+cal.getTime().compareTo(weekendDate));
						   try {
							if(sdf.parse(sdf.format(cal.getTime())).compareTo(weekendDate) == 0) // Deals is open for nearst weekend only
							{
							   
							   if(!contactFlag )
							   {
									   List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
									   
									   for(Deals catdeal:catDeals)
									   {
										   Business business = catdeal.getBusiness();
										    if(business != null)
											{
												
													if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
													{
														array = getLatLong(business.getPostalCode());
														business.setLattitude(Double.toString(array[0]));
														business.setLongitude(Double.toString(array[1]));
													}
													double distance = distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
													logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
													if(distance <= 50)
													{
														List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
														if(userdealsuggestions == null || userdealsuggestions.size() == 0)
														{
															saveDealsSuggestionInDataBase(catdeal,userFor);
														}
													}
											}
									   
								  }
								}
							   else
							   {
								   int LikeCount = dealDAO.userCategoryLikeCount(userCategory.getUserCategoryId());
								   if(isStar)//check friend is star or non star
								   {
									   List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
									   for(Deals catdeal:catDeals)
									   {
										   Business business = catdeal.getBusiness();
										    if(business != null)
											{
												
													if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
													{
														array = getLatLong(business.getPostalCode());
														business.setLattitude(Double.toString(array[0]));
														business.setLongitude(Double.toString(array[1]));
													}
													double distance = distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
													logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
													if(distance <= 50)
													{
														List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
														if(userdealsuggestions == null || userdealsuggestions.size() == 0)
														{
															saveDealsSuggestionInDataBase(catdeal,userFor);
														}
													}
											}
										   
									   }
								   }
								   else
								   {
									   if(LikeCount >= 2) // if non star then chek Like count greater than 2.
									   {
										   List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
										   for(Deals catdeal:catDeals)
										   {
											   Business business = catdeal.getBusiness();
											    if(business != null)
												{
													
														if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
														{
															array = getLatLong(business.getPostalCode());
															business.setLattitude(Double.toString(array[0]));
															business.setLongitude(Double.toString(array[1]));
														}
														double distance = distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
														logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
														if(distance <= 50)
														{
															List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
															if(userdealsuggestions == null || userdealsuggestions.size() == 0)
															{
																saveDealsSuggestionInDataBase(catdeal,userFor);
															}
														}
												}
											   
										   }
									   }
								   }
							   }
							}
							}catch(Exception e)
							{
								e.printStackTrace();
							}
						   
					   
					   }
					}
						
						
				
								
				//// for jeeyohgroups
				if(!contactFlag)
				{
						
				  if(jeeyohGroup != null)
				  {
				   for(Jeeyohgroup jeeyohGroup1 : jeeyohGroup) {
					   
					   String groupType = jeeyohGroup1.getGroupType();
					   Set<Groupusermap> groups   = jeeyohGroup1.getGroupusermaps();
					   for (Groupusermap groups1 : groups)
					   {
						   User groupMember = groups1.getUser();
						   if(groupMember.getUserId() != userFor.getUserId())
						   {
							   //List<UserCategory> userCategoryList1 = userDAO.getUserCategoryLikesById(groupMember.getUserId());
							   List<UserCategory> userCategoryList1   = userDAO.getUserCategoryLikesByType(groupMember.getUserId(), groupType);
								  if(userCategoryList != null)
								  {
								   for(UserCategory userCategory : userCategoryList1) {											  
									   
										   List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
										   for(Deals catdeal:catDeals)
										   {
											   Business business = catdeal.getBusiness();
											    if(business != null)
												{
													
														if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
														{
															array = getLatLong(business.getPostalCode());
															business.setLattitude(Double.toString(array[0]));
															business.setLongitude(Double.toString(array[1]));
														}
														double distance = distance(Double.parseDouble(userFor.getLattitude()), Double.parseDouble(userFor.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
														logger.debug("Distance::  "+distance +" lat::  "+userFor.getLattitude()+" lon::  "+userFor.getLongitude());
														if(distance <= 50)
														{
															List<Userdealssuggestion> userdealsuggestions = userDAO.isDealSuggestionExists(userFor.getUserId(), catdeal.getId());
															if(userdealsuggestions == null || userdealsuggestions.size() == 0)
															{
																saveDealsSuggestionInDataBase(catdeal,userFor);
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
	
	 private void saveDealsSuggestionInDataBase(Deals deal, User user)
	 {
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
	
	private Date getNearestWeekend()
	{
	 try {
	  Calendar c = Calendar.getInstance();
	  c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	  Date weekendDate = sdf.parse(sdf.format(c.getTime()));
	  return weekendDate;
	 } catch (Exception e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	  return null;
	 }
	
	}
	
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
			logger.debug("results :  "+results);
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

	
				
}
