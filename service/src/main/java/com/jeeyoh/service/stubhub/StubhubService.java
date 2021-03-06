package com.jeeyoh.service.stubhub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
		//int offset = 0, limit = 1000;
		int count = 0;
		int count1 = 0;
		/*while(true)
		{*/
		StubHubEvents stubHubEvents = stubhubClient.getStubHubEvents();

		SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		//logger.debug("Description  ::: "+stubHubEvents.getResponce().getDocs().get(0).getDescription());

		if(stubHubEvents != null && stubHubEvents.getResponce() != null)
		{
			logger.debug("Response:::  "+stubHubEvents.getResponce());
			List<Description> description = stubHubEvents.getResponce().getDocs();
			if(description != null && description.size() != 0)
			{
				logger.debug("TOTAL EVENT SIZE :: = > "+description.size());
				try {
					for(Description stubhubList:description)
					{

						if(stubhubList != null)
						{

							if(stubhubList.getAncestorGeoDescriptions().contains("United States"))
							{//count1++;
								//if(count1 <= 4)
								//{
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
									Date eventDate = null;
									eventDate = simple.parse(stubhubList.getEvent_date());
									logger.debug("getEvent_date::  "+stubhubList.getEvent_date()+ " : "+eventDate);
									stubhub.setEvent_date(eventDate);
								}
								if(stubhubList.getEvent_date_local() != null)
								{
									//Date eventDate = null;
									//eventDate = simple.parse(stubhubList.getEvent_date_local());
									logger.debug("getEvent_date_local::  "+stubhubList.getEvent_date_local());
									stubhub.setEvent_date_local(new DateTime(stubhubList.getEvent_date_local()).toDate());
								}
								if(stubhubList.getEvent_date_time_local() != null)
								{
									Date eventDate = null;
									eventDate = simple.parse(stubhubList.getEvent_date_time_local());
									logger.debug("getEvent_date_time_local::  "+stubhubList.getEvent_date_time_local()+ " : "+eventDate);
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
								stubhub.setAncestorGeoDescriptions(stubhubList.getAncestorGeoDescriptions().toString());
								stubhubDAO.save(stubhub,count);
								count++;
								//break;
								//}
							}
							else
							{
								count1++;
							}

						}
					}
					logger.debug("TOTAL count1 == >"+count1);

				} catch (ParseException e) {
					logger.debug("Error: "+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		/*else
				break;
			//offset = limit;
			offset += 1000;
		}

		logger.debug("TOTAL count1 == >"+count);*/
	}
}
