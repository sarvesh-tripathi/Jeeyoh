package com.jeeyoh.service.stubhub;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.dao.stubhub.IStubhubDAO;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.StubhubEvent;
import com.jeeyoh.utils.Utils;


@Component("stubhubFilterEngine")
public class StubhubFilterEngineService implements IStubhubFilterEngineService{

	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IStubhubDAO stubhubDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Override
	@Transactional
	public void filter() {
		List<StubhubEvent> stubhubEventsList = stubhubDAO.getStubhubEvents();
		List<Page> communities = eventsDAO.getCommunities();
		logger.debug("communities::  "+communities.size());
		logger.debug("stubhubEventsList::  "+stubhubEventsList.size());
		//Get current date
		Date currentDate = Utils.getCurrentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//Get weekends including Friday of the current year
		List<Date>weekendList =  Utils.findWeekendsWithFriday();
		if(stubhubEventsList != null)
		{
			int batch_size = 0;
			try {
				for(StubhubEvent stubhubEvent : stubhubEventsList){

					//Get weekend date for Event date
					Date eventDate = Utils.getEventWeekendDates(stubhubEvent.getEvent_date());

					if(eventDate != null)
					{
						//logger.debug("eventDate:: "+eventDate);
						//logger.debug("currentDate:: "+currentDate);
						for(int j = 0; j < weekendList.size(); j++){

							if((eventDate.compareTo(currentDate) >= 0) && (eventDate.compareTo(sdf.parse(sdf.format((Date)weekendList.get(j)))) == 0))
							{
								//logger.debug("eventDate:: "+eventDate +"currentDate:: "+ sdf.parse(sdf.format((Date)weekendList.get(j))));
								/*if((eventDate.compareTo(currentDate) == 0 || eventDate.after(currentDate)))
								{*/

								for(Page community : communities)
								{
									String ancestorGenreDescriptions = stubhubEvent.getAncestorGenreDescriptions().toLowerCase();
									String about = community.getAbout().toLowerCase();
									if(stubhubEvent.getChannel().toLowerCase().contains(community.getPagetype().getPageType().toLowerCase()) && ancestorGenreDescriptions.contains(about))
									{
										batch_size++;
										Events events = new Events();
										events.setChannel(stubhubEvent.getChannel());
										events.setCity(stubhubEvent.getCity());
										events.setCurrency_code(stubhubEvent.getCurrency_code());
										events.setDescription(stubhubEvent.getDescription());
										events.setEvent_config_template(stubhubEvent.getEvent_config_template());
										events.setEvent_config_template_id(stubhubEvent.getEvent_config_template_id());
										events.setEvent_date(stubhubEvent.getEvent_date());
										events.setEvent_date_local(stubhubEvent.getEvent_date_local());
										events.setEvent_time_local(stubhubEvent.getEvent_time_local());
										events.setGenreUrlPath(stubhubEvent.getGenreUrlPath());
										events.setGeography_parent(stubhubEvent.getGeography_parent());
										events.setKeywords_en_US(stubhubEvent.getKeywords_en_US());
										events.setLatitude(stubhubEvent.getLatitude());
										events.setLongitude(stubhubEvent.getLongitude());
										events.setZip(stubhubEvent.getZip());
										events.setLeaf(stubhubEvent.getLeaf());
										events.setMaxPrice(stubhubEvent.getMaxPrice());
										events.setMinPrice(stubhubEvent.getMinPrice());
										events.setMaxSeatsTogether(stubhubEvent.getMaxSeatsTogether());
										events.setMinSeatsTogether(stubhubEvent.getMinSeatsTogether());
										events.setState(stubhubEvent.getState());
										events.setTimezone(stubhubEvent.getTimezone());
										events.setTimezone_id(Long.parseLong(stubhubEvent.getTimezone_id()));
										events.setTitle(stubhubEvent.getTitle());
										events.setTotalTickets(stubhubEvent.getTotalTickets());
										events.setUrlpath(stubhubEvent.getUrlpath());
										events.setVenue_config_name(stubhubEvent.getVenue_config_name());
										events.setVenue_name(stubhubEvent.getVenue_name());
										events.setAncestorGenreDescriptions(stubhubEvent.getAncestorGenreDescriptions());
										events.setPage(community);
										eventsDAO.saveEvents(events, batch_size);
										break;
									}
								}
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
