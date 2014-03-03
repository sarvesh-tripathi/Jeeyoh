package com.jeeyoh.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

public class Utils {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	/**
	 * Get weekends date for the current year for Events
	 * @return
	 */
	public static List<Date> findWeekendsWithFriday(){
		List<Date >weekendList = new ArrayList<Date>();
		Calendar cal = null;
		cal = Calendar.getInstance();
		// The while loop ensures that you are only checking dates in the specified year
		while(cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
			// The switch checks the day of the week for Saturdays and Sundays
			switch(cal.get(Calendar.DAY_OF_WEEK)){
			case Calendar.FRIDAY:
			case Calendar.SATURDAY:
			case Calendar.SUNDAY:
				weekendList.add(cal.getTime());
				break;
			}
			// Increment the day of the year for the next iteration of the while loop
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		return weekendList;
	}


	/**
	 * Get weekends date for the current year
	 * @return
	 */
	public static List<Date> findWeekends(){
		List<Date >weekendList = new ArrayList<Date>();
		Calendar cal = null;
		cal = Calendar.getInstance();
		// The while loop ensures that you are only checking dates in the specified year
		while(cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
			// The switch checks the day of the week for Saturdays and Sundays
			switch(cal.get(Calendar.DAY_OF_WEEK)){
			case Calendar.SATURDAY:
			case Calendar.SUNDAY:
				weekendList.add(cal.getTime());
				break;
			}
			// Increment the day of the year for the next iteration of the while loop
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		return weekendList;
	}

	/**
	 * Get latitude and longitude for ZipCode
	 * @param postCode
	 */
	public static double[] getLatLong(String postCode)
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
	 * Get distance of location from current location
	 * @param lat2
	 * @param lon2
	 * @param unit
	 * @return
	 */
	public static double distance(double lat1, double lon1,double lat2, double lon2, String unit)
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
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}


	/**
	 * This function converts radians to decimal degrees   
	 * @param rad
	 * @return
	 */
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	/**
	 * Get nearest weekend for a particular date
	 * @return weekendDate
	 */
	public static Date getNearestWeekend(Date date)
	{
		try {
			Calendar c = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			if(date != null)
				c.setTime(date);
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			cal1.setTime(getCurrentDate());
			if(c.get(Calendar.DAY_OF_MONTH) < cal1.get(Calendar.DAY_OF_MONTH))
				c.add(Calendar.DATE,7);
			
			// Set time fields to end  
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			return c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	/**
	 * Get nearest Friday for a particular date
	 * @return weekendDate
	 */
	public static Date getNearestFriday()
	{
		try {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			
			// Set time fields to end  
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			return c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	

	/**
	 * Get Weekend date for the event date
	 * @param date
	 * @return
	 */
	public static Date getEventWeekendDates(Date date)
	{
		Date eventDate = null;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			switch(cal.get(Calendar.DAY_OF_WEEK)){
			case Calendar.FRIDAY: 
				eventDate = cal.getTime();
				break;	
			case Calendar.SATURDAY: 
				eventDate = cal.getTime();
				break;	
			case Calendar.SUNDAY: 
				eventDate =  cal.getTime();
				break;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventDate;
	}


	/**
	 * Get current date
	 * @return
	 */
	public static Date getCurrentDate()
	{
		Date currentDate = null;
		Calendar cal = Calendar.getInstance();
		try {
			currentDate = cal.getTime();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return currentDate;
	}
	

	/**
	 * Get address from latitude/longitude
	 * @param postCode
	 */
	public static String[] getCityAndAddress(Double latitude, Double longitude)
	{
		logger.debug("latitude :  "+latitude+" longitude: "+longitude);
		String[] addressArray = new String[2];
		try
		{
			LatLng latLng = new LatLng();
			latLng.setLat(BigDecimal.valueOf(latitude));
			latLng.setLng(BigDecimal.valueOf(longitude));
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(latLng).getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			//logger.debug("geocoderResponse :  "+geocoderResponse);
			List<GeocoderResult> results = geocoderResponse.getResults();
			//logger.debug("results :  "+results);
			List<GeocoderAddressComponent> geList= results.get(0).getAddressComponents();

			for(int i =0; i < geList.size(); i++)
			{
				if(geList.get(i).getTypes().get(0).equalsIgnoreCase("locality"))
				{
					addressArray[0] = geList.get(i).getLongName();
					break;
				}
			}
			addressArray[1] = results.get(0).getFormattedAddress();
			logger.debug("addressArray :  " + addressArray);

		}catch (Exception e) {
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return addressArray;
	}


	/**
	 * Get latitude and longitude for ZipCode
	 * @param postCode
	 */
	public static String getZipCode(Double latitude, Double longitude)
	{
		logger.debug("latitude :  "+latitude+" longitude: "+longitude);
		String zipcode = null;
		try
		{
			LatLng latLng = new LatLng();
			latLng.setLat(BigDecimal.valueOf(latitude));
			latLng.setLng(BigDecimal.valueOf(longitude));
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(latLng).getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			//logger.debug("geocoderResponse :  "+geocoderResponse);
			List<GeocoderResult> results = geocoderResponse.getResults();
			//logger.debug("results :  "+results);
			List<GeocoderAddressComponent> geList= results.get(0).getAddressComponents();
			if(geList.get(geList.size()-1).getTypes().get(0).trim().equalsIgnoreCase("postal_code"))
			{
				zipcode = geList.get(geList.size()-1).getLongName();
			}
			else if(geList.get(0).getTypes().get(0).trim().equalsIgnoreCase("postal_code"))
			{
				zipcode = geList.get(0).getLongName();
			}

			logger.debug("zipcode :  " + zipcode);

		}catch (Exception e) {
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return zipcode;
	}


	/**
	 * Get latitude and longitude for ZipCode
	 * @param postCode
	 */
	public static String[] getGeographicalInfo(Double latitude, Double longitude)
	{
		String[] addressArray = new String[2];
		try
		{
			LatLng latLng = new LatLng();
			latLng.setLat(BigDecimal.valueOf(latitude));
			latLng.setLng(BigDecimal.valueOf(longitude));
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(latLng).getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			List<GeocoderResult> results = geocoderResponse.getResults();
			outerloop:
				for(int k =0; k < results.size(); k++)
				{
					List<GeocoderAddressComponent> geList= results.get(k).getAddressComponents();
					for(int i =0; i < geList.size(); i++)
					{
						List<String> types = geList.get(i).getTypes();
						for(int j =0; j < types.size(); j++)
						{
							if(types.get(j).equalsIgnoreCase("postal_code"))
							{
								addressArray[0] = geList.get(i).getLongName();
								break outerloop;
							}
						}
					}
				}

			addressArray[1] = results.get(0).getFormattedAddress();
			logger.debug("zipcode :  " + addressArray);

		}catch (Exception e) {
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return addressArray;
	}

	
	/**
	 * This method encode the provided string
	 * @param text
	 * @return
	 */
	public static String MD5(String text)
	{
		String md5Text = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			md5Text = new BigInteger(1, digest.digest((text).getBytes())).toString(16);
		} catch (Exception e) {
			System.out.println("Error in call to MD5");
		}

		if (md5Text.length() == 31) {
			md5Text = "0" + md5Text;
		}
		return md5Text;
	}
	
	/**
	 * This method encode the provided string
	 * @param text
	 * @return
	 */
	public static String MD5ToString(String password)
	{
		 MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    md.update(password.getBytes());

		    byte byteData[] = md.digest();

		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < byteData.length; i++)
		        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		    
		    return sb.toString();
		    
	}


}
