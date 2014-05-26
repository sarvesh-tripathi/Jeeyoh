package com.jeeyoh.service.stubhub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeeyoh.model.stubhub.Description;
import com.jeeyoh.model.stubhub.StubHubEvents;
import com.jeeyoh.persistence.dao.stubhub.IStubhubDAO;
import com.jeeyoh.persistence.domain.StubhubEvent;

@Component("stubhubService")
public class StubhubService implements IStubhubService {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	IStubhubClient stubhubClient;

	@Autowired
	private IStubhubDAO stubhubDAO;

	@Override
	public void stubhubEvents() {
		logger.debug("Service of Stubhub ");
		StubHubEvents stubHubEvents = stubhubClient.getStubHubEvents();
		
		SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
		simple.setTimeZone(TimeZone.getTimeZone("UTC"));

		
		//logger.debug("Get responce here ::: "+stubHubEvents.getResponce().getNumFound());
		logger.debug("Description  ::: "+stubHubEvents.getResponce().getDocs().get(0).getDescription());
		List<Description> description = stubHubEvents.getResponce().getDocs();
		logger.debug("TOTAL EVENT SIZE :: = > "+description.size());
		if(description != null)
		{
			int count = 0;
			int count1 = 0;
			for(Description stubhubList:description)
			{
				
				if(stubhubList != null)
				{count1++;
				if(count1 <= 2)
				{
					logger.debug("TOTAL Count == >"+count);
					logger.debug("Title == >"+stubhubList.getTitle());
					logger.debug("title_en_US == >"+stubhubList.getTitle_en_US());
					StubhubEvent stubhub = new StubhubEvent();
					stubhub.setChannel(stubhubList.getChannel());
					stubhub.setCity(stubhubList.getCity());
					stubhub.setCurrency_code(stubhubList.getCurrency_code());
					stubhub.setDescription(stubhubList.getDescription());
					stubhub.setEvent_config_template(stubhubList.getEvent_config_template());
					stubhub.setEvent_config_template_id(stubhubList.getEvent_config_template_id());
					if(stubhubList.getEvent_date() != null)
					{
						stubhub.setEvent_date(new DateTime(stubhubList.getEvent_date()).toDate());
					}
					if(stubhubList.getEvent_date_local() != null)
					{
						stubhub.setEvent_date_local(new DateTime(stubhubList.getEvent_date_local()).toDate());
					}
					if(stubhubList.getEvent_date_time_local() != null)
					{
						
						logger.debug("getEvent_date_time_local::  "+new DateTime(stubhubList.getEvent_date_time_local(),DateTimeZone.forID("EST")).toDate() + " : "+stubhubList.getEvent_date_time_local());
						
						DateTime dateTime = new DateTime(stubhubList.getEvent_date_time_local(),DateTimeZone.forID("EST"));
						Calendar cal = Calendar.getInstance();
						//cal.setTimeZone(TimeZone.getTimeZone("EST"));
						logger.debug("cal current time::  "+ cal +" "+cal.getTime());
						cal.setTime(dateTime.toDate());
						logger.debug("cal current time::  after setTime "+ cal +" "+cal.getTime());
						cal.setTimeZone(TimeZone.getTimeZone("EST"));
						logger.debug("cal current time::  after setTimeZone "+ cal +" "+cal.getTime());
						cal.set(Calendar.ZONE_OFFSET, -18000000);
						logger.debug("cal current time::  after set offset "+ cal +" "+cal.getTime());
						//logger.debug("cal time::  "+cal.getTime());
						DateTime dateTimeUtc = dateTime.toDateTime( DateTimeZone.UTC );
						LocalDateTime ldt = new LocalDateTime(stubhubList.getEvent_date_time_local());
						
						Date eventDate = null;
						try {
							eventDate = simple.parse(stubhubList.getEvent_date_time_local());
						} catch (ParseException e) {
							logger.debug("Error: "+e.getMessage());
							e.printStackTrace();
						}
						logger.debug("getEvent_date_time_local::  "+eventDate);
						logger.debug("getEvent_date_time_local111::  "+dateTimeUtc.toDate());
						stubhub.setEvent_date_time_local(eventDate);
					}
						
					
					stubhub.setEvent_time_local(stubhubList.getEvent_time_local());
					stubhub.setGenreUrlPath(stubhubList.getGenreUrlPath());
					stubhub.setGeography_parent(stubhubList.getGeography_parent());
					stubhub.setKeywords_en_US(stubhubList.getKeywords_en_US());
					stubhub.setLatitude(stubhubList.getLatitude());
					stubhub.setLongitude(stubhubList.getLongitude());
					stubhub.setLeaf(stubhubList.getLeaf());
					if(stubhubList.getMaxPrice() != null)
						stubhub.setMaxPrice(Double.parseDouble((stubhubList.getMaxPrice())));
					if(stubhubList.getMaxSeatsTogether() != null)
						stubhub.setMaxSeatsTogether(Double.parseDouble(stubhubList.getMaxSeatsTogether()));
					if(stubhubList.getMinPrice() != null)
						stubhub.setMinPrice(Double.parseDouble(stubhubList.getMinPrice()));
					if(stubhubList.getMinSeatsTogether() != null)
						stubhub.setMinSeatsTogether(Double.parseDouble(stubhubList.getMinSeatsTogether()));
					stubhub.setState(stubhubList.getState());
					stubhub.setTimezone(stubhubList.getTimezone());
					stubhub.setTimezone_id(stubhubList.getTimezone_id());
					stubhub.setTitle(stubhubList.getTitle());
					stubhub.setTotalTickets(stubhubList.getTotalTickets());
					stubhub.setUrlpath(stubhubList.getUrlpath());
					stubhub.setVenue_config_name(stubhubList.getVenue_config_name());
					stubhub.setVenue_name(stubhubList.getVenue_name());
					stubhub.setZip(stubhubList.getZip());
					stubhub.setAncestorGenreDescriptions(stubhubList.getAncestorGenreDescriptions().toString());
					stubhub.setImage_url(stubhubList.getImage_url());
					stubhub.setGenre_parent_name(stubhubList.getGenre_parent_name());
					stubhubDAO.save(stubhub,count);
					count++;
					break;
				}
				}
			}
		}
	}
}
