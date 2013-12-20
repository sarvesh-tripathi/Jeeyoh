package com.jeeyoh.service.stubhub;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
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
		// TODO Auto-generated method stub
		logger.debug("Service of Stubhub ");
		StubHubEvents stubHubEvents = stubhubClient.getStubHubEvents();
		//logger.debug("Get responce here ::: "+stubHubEvents.getResponce().getNumFound());
		logger.debug("Description  ::: "+stubHubEvents.getResponce().getDocs().get(0).getDescription());
		List<Description> description = stubHubEvents.getResponce().getDocs();
		logger.debug("TOTAL EVENT SIZE :: = > "+description.size());
		if(description != null)
		{
			int count = 0;
			for(Description stubhubList:description)
			{
				if(stubhubList != null)
				{
					logger.debug("TOTAL Count == >"+count);
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
				    stubhub.setEvent_time_local(stubhubList.getEvent_time_local());
				    stubhub.setGenreUrlPath(stubhubList.getGenreUrlPath());
				    stubhub.setGeography_parent(stubhubList.getGeography_parent());
				    stubhub.setKeywords_en_US(stubhubList.getKeywords_en_US());
				    stubhub.setLatitude(stubhubList.getLatitude());
				    stubhub.setLongitude(stubhubList.getLongitude());
				    stubhub.setLeaf(stubhubList.getLeaf());
				    stubhub.setMaxPrice(Double.parseDouble((stubhubList.getMaxPrice())));
				    stubhub.setMaxSeatsTogether(Double.parseDouble(stubhubList.getMaxSeatsTogether()));
				    stubhub.setMinPrice(Double.parseDouble(stubhubList.getMinPrice()));
				    stubhub.setMinSeatsTogether(Double.parseDouble(stubhubList.getMinSeatsTogether()));
				    stubhub.setState(stubhubList.getState());
				    stubhub.setTimezone(stubhubList.getTimezone());
				    stubhub.setTimezone_id(Long.parseLong(stubhubList.getTimezone_id()));
				    stubhub.setTitle(stubhubList.getTitle());
				    stubhub.setTotalTickets(stubhubList.getTotalTickets());
				    stubhub.setUrlpath(stubhubList.getUrlpath());
				    stubhub.setVenue_config_name(stubhubList.getVenue_config_name());
				    stubhub.setVenue_name(stubhubList.getVenue_name());
				    stubhub.setZip(stubhubList.getZip());
				    stubhubDAO.save(stubhub);
				    count++;
				    if(count == 1000)
				    {
				    	break;
				    }
				}
			    
				
			}
		}
		
		
		
		
		
	}

}
