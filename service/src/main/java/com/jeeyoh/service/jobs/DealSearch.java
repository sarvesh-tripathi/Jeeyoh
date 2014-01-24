package com.jeeyoh.service.jobs;

import java.text.SimpleDateFormat;
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
		
		String[] type = {"RESTAURANT","SPA","SPORT"};
		List<Businesstype> businesstypes = businessDAO.getBusinesstypeByTypeArray(type);	
		//Businesstype businesstype = businessDAO.getBusinesstypeByType("SPORT");
		List<User> userList = userDAO.getUsers();		
		
		for (User user : userList)
		{
			if(user != null)
			{
				saveDealSuggestion(user,user,businesstypes,false,false);
				//List<User> userContacts = userDAO.getUserContacts(user.getUserId());
				List<Usercontacts> userContactsList = userDAO.getAllUserContacts(user.getUserId());
				if(userContactsList != null)
				{
					for(Usercontacts usercontacts:userContactsList)
					{
						
						Boolean isStar = usercontacts.getIsStar();
						User contact = usercontacts.getUserByContactId();
						logger.debug("Friend Name ::"+contact.getFirstName());
						logger.debug("IS STAR ::"+isStar);
						saveDealSuggestion(contact,user,businesstypes,true,isStar);
					}
				}
				
			}
		}
							
	}
	
	@SuppressWarnings("unchecked")
	private void saveDealSuggestion(User user,User userFor, List<Businesstype> businesstypes,Boolean contactFlag,Boolean isStar)
	{
		double[] array = null;
		logger.debug("USER FOR :: "+user.getEmailId());
		logger.debug("Lat/Long for user :  " + user.getLattitude() +" , "+user.getLongitude());
		if(user.getLattitude() == null && user.getLongitude() == null || (user.getLattitude().trim().equals("") && user.getLongitude().trim().equals(""))|| (user.getLattitude().trim().equals("0.0") && user.getLongitude().trim().equals("0.0")))
		{
			array = getLatLong(user.getZipcode());
			logger.debug("Lat/long length ==> " + Double.toString(array[0]).length()+" : "+Double.toString(array[1]).length());
			user.setLattitude(Double.toString(array[0]));
			user.setLongitude(Double.toString(array[1]));
		}
		if(businesstypes != null)
		{
			//get user detail and deal usage
			//User user = userDAO.getUsersById("gaurav.shandilya@gmail.com");
			Set<Dealsusage> dealUsage = user.getDealsusages();	
			
			// get user contects				
			//List<User> userContacts = userDAO.getUserContacts(user.getUserId());
			
			// user commuinty 
			List<Page> userCommunities = userDAO.getUserCommunities(user.getUserId());
			
			//user category
			
			List<UserCategory> userCategoryList = userDAO.getUserCategoryLikesById(user.getUserId());
			// user group member
			
			List<Jeeyohgroup> jeeyohGroup = userDAO.getUserGroups(user.getUserId());
			
			for(Businesstype businesstype:businesstypes)
			{
				Set<Business> businessSet = businesstype.getBusinesses();
				for(Business business :businessSet )
				{
					logger.debug("Business data :: "+business.getCity());
					if(business != null)
					{
						
						if(business.getLattitude() == null && business.getLongitude() == null || (business.getLattitude().trim().equals("") && business.getLongitude().trim().equals(""))  || (business.getLattitude().trim().equals("0.0") && business.getLongitude().trim().equals("0.0")))
						{
							array = getLatLong(business.getPostalCode());
							business.setLattitude(Double.toString(array[0]));
							business.setLongitude(Double.toString(array[1]));
						}
						double distance = distance(Double.parseDouble(user.getLattitude()), Double.parseDouble(user.getLongitude()), Double.parseDouble(business.getLattitude()), Double.parseDouble(business.getLongitude()), "M");
						logger.debug("Distance::  "+distance +" lat::  "+user.getLattitude()+" lon::  "+user.getLongitude());
						if(distance <= 50)
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
										int likeCount = dealDAO.getDealsLikeCounts(dealsusage.getDeals().getId());
										logger.debug("Like COUNT :::::"+likeCount);
										if(dealsusage.getIsFavorite() || dealsusage.getIsLike() || likeCount >= 2)
										{
											logger.debug("Cross basic three level3",deal.getId());
											saveDeal = true;
											saveDealsSuggestionInDataBase(deal,userFor);						
											 									
											
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
												saveDealsSuggestionInDataBase(deal,userFor);
											}
											
										}
									}
								}
								
								
								
								// for user likes item
							
								if(!saveDeal)
								{
									 
								    
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
									        if(sdf.parse(sdf.format(cal.getTime())).compareTo(weekendDate) == 0)
									        {
											   int LikeCount = dealDAO.userCategoryLikeCount(userCategory.getUserCategoryId());
											   if(!contactFlag )
											   {
												   List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
												   for(Deals catdeal:catDeals)
												   {
													    int dealId = deal.getId();
														int catDealId = catdeal.getId();
														if(dealId == catDealId)
														{
															saveDeal = true;
															saveDealsSuggestionInDataBase(deal,userFor);
														}
													   
												   }
												   
										      }
											   else
											   {
												   if(isStar)
												   {
													   List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
													   for(Deals catdeal:catDeals)
													   {
														    int dealId = deal.getId();
															int catDealId = catdeal.getId();
															if(dealId == catDealId)
															{
																saveDeal = true;
																saveDealsSuggestionInDataBase(deal,userFor);
															}
														   
													   }
												   }
												   else
												   {
													   if(LikeCount >= 2)
													   {
														   List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
														   for(Deals catdeal:catDeals)
														   {
															    int dealId = deal.getId();
																int catDealId = catdeal.getId();
																if(dealId == catDealId)
																{
																	saveDeal = true;
																	saveDealsSuggestionInDataBase(deal,userFor);
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
									    
									    
								}
								
								//// for jeeyohgroups
								if(!contactFlag && !saveDeal)
								{
								
									  if(jeeyohGroup != null)
									  {
									   for(Jeeyohgroup jeeyohGroup1 : jeeyohGroup) {
										   
										   String groupType = jeeyohGroup1.getGroupType();
										   Set<Groupusermap> groups   = jeeyohGroup1.getGroupusermaps();
										   for (Groupusermap groups1 : groups)
										   {
											   User groupMember = groups1.getUser();
											   if(groupMember.getUserId() != user.getUserId())
											   {
												   List<UserCategory> userCategoryList1 = userDAO.getUserCategoryLikesById(groupMember.getUserId());
													  if(userCategoryList != null)
													  {
													   for(UserCategory userCategory : userCategoryList1) {
														  if(groupType.equals(userCategory.getItemCategory()))
														  {
														   
															   List<Deals> catDeals = dealDAO.getDealsByUserCategory(userCategory.getItemCategory(),userCategory.getItemType(),userCategory.getProviderName());
															   for(Deals catdeal:catDeals)
															   {
																    int dealId = deal.getId();
																	int catDealId = catdeal.getId();
																	if(dealId == catDealId)
																	{
																		saveDeal = true;
																		saveDealsSuggestionInDataBase(deal,userFor);
																		
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
