package com.jeeyoh.service.stubhub;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.dao.stubhub.IStubhubDAO;
import com.jeeyoh.persistence.dao.yelp.ICountryLocationDAO;
import com.jeeyoh.persistence.domain.Countrylocation;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.StubhubEvent;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.utils.Utils;


@Component("stubhubFilterEngine")
public class StubhubFilterEngineService implements IStubhubFilterEngineService{

	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	@Value("${stubhub.base.url}")
	private String stubhubBaseUrl;

	@Value("${stubhub.image.url}")
	private String stubhubImageUrl;
	@Autowired
	private IStubhubDAO stubhubDAO;

	@Autowired
	private IEventsDAO eventsDAO;

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private ICountryLocationDAO countryLocationDAO;

	@Override
	@Transactional
	public void filter() {
		List<StubhubEvent> stubhubEventsList = stubhubDAO.getStubhubEvents();

		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		//Get weekends including Friday of the current year
		//List<Date>weekendList =  Utils.findWeekendsWithFriday();
		if(stubhubEventsList != null)
		{
			int batch_size = 0;
			int count = 0, count1 = 0;
			try {
				logger.debug("stubhubEventsList::  "+stubhubEventsList.size());
				for(StubhubEvent stubhubEvent : stubhubEventsList){

					count++;
					logger.debug("Count::  "+count);
					//Get weekend date for Event date
					Date eventDate = Utils.getEventWeekendDates(stubhubEvent.getEvent_date_local());

					if(eventDate != null)
					{
						count1++;
						logger.debug("Count1::  "+count1);
						
						batch_size++;
						Events events = new Events();
						//events.setChannel(stubhubEvent.getChannel());
						events.setCity(stubhubEvent.getCity());
						events.setCurrency_code(stubhubEvent.getCurrency_code());
						events.setDescription(stubhubEvent.getDescription());
						events.setEvent_config_template(stubhubEvent.getEvent_config_template());
						events.setEvent_config_template_id(stubhubEvent.getEvent_config_template_id());
						events.setEvent_date(stubhubEvent.getEvent_date());
						events.setEvent_date_local(stubhubEvent.getEvent_date_local());
						events.setEvent_time_local(stubhubEvent.getEvent_time_local());
						events.setEvent_date_time_local(stubhubEvent.getEvent_date_time_local());
						events.setGenreUrlPath(stubhubBaseUrl+stubhubEvent.getGenreUrlPath());
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

						//Get State Description for state code
						Countrylocation countrylocation = countryLocationDAO.getStateNameByStateCode(stubhubEvent.getState());
						if(countrylocation != null)
							events.setState(countrylocation.getState());
						else
							events.setState(stubhubEvent.getState());
						events.setStateCode(stubhubEvent.getState());
						events.setTimezone(stubhubEvent.getTimezone());
						if(stubhubEvent.getTimezone_id() != null)
							events.setTimezone_id(Long.parseLong(stubhubEvent.getTimezone_id()));
						if(events.getTitle() != null && events.getTitle().equals(""))
							events.setTitle(stubhubEvent.getTitle());
						else
							events.setTitle(stubhubEvent.getDescription());
						events.setTotalTickets(stubhubEvent.getTotalTickets());
						events.setUrlpath(stubhubBaseUrl+stubhubEvent.getUrlpath());
						events.setVenue_config_name(stubhubEvent.getVenue_config_name());
						events.setVenue_name(stubhubEvent.getVenue_name());
						events.setAncestorGenreDescriptions(stubhubEvent.getAncestorGenreDescriptions());
						//events.setPage(community);
						//events.setImage_url(stubhubEvent.getImage_url());
						events.setEventSource("Stubhub");
						events.setGenre_parent_name(stubhubEvent.getGenre_parent_name());
						events.setChannel(stubhubEvent.getChannel());
						
						Page isPageExist = eventsDAO.getPageByAbout(stubhubEvent.getGenre_parent_name());
						logger.debug("Is page Exist "+isPageExist);
						if(isPageExist == null)
						{
							Page page = new Page();
							page.setAbout(stubhubEvent.getGenre_parent_name());
							page.setCreatedtime(new Date());
							page.setIsCommunity(true);
							page.setIsEvent(true);
							page.setIsOrganization(true);

							Pagetype pagetype = null;
							if(stubhubEvent.getChannel().toLowerCase().contains("sports"))
							{
								pagetype = eventsDAO.getPageTypeByName("SPORT");
							}
							else if(stubhubEvent.getChannel().toLowerCase().contains("theater"))
							{
								pagetype = eventsDAO.getPageTypeByName("THEATER");
							}
							else if(stubhubEvent.getChannel().toLowerCase().contains("concert"))
							{
								pagetype = eventsDAO.getPageTypeByName("CONCERT");
							}
							else
								pagetype = eventsDAO.getPageTypeByName("NIGHTLIFE");

							User user  = userDAO.getUserById(1);
							page.setUserByCreatorId(user);
							page.setUserByOwnerId(user);
							page.setPagetype(pagetype);										
							page.setPageUrl(stubhubBaseUrl + stubhubEvent.getGenreUrlPath());
							page.setProfilePicture(stubhubImageUrl +stubhubEvent.getGeography_parent() + "/" + stubhubEvent.getImage_url() );
							//eventsDAO.savePage(page,batch_size);
							//Page page1 = eventsDAO.getPageByAbout(stubhubEvent.getGenre_parent_name());
							events.setPage(page);
						}
						else
						{
							events.setPage(isPageExist);
						}
						eventsDAO.saveEvents(events, batch_size);
					}
				}
			} catch (Exception e) {
				logger.debug("Error in filter:::  "+e.getMessage());
				e.printStackTrace();
			}
		}
	}
}