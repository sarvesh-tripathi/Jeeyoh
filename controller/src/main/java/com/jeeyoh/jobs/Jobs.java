package com.jeeyoh.jobs;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeeyoh.service.jobs.IDealSearch;
import com.jeeyoh.service.jobs.IEventSearch;
import com.jeeyoh.service.jobs.INonDealSearch;
import com.jeeyoh.service.jobs.IWallService;

public class Jobs {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	private IWallService wallService;
	
	@Autowired
	private INonDealSearch nonDealSearch;

	@Autowired
	private IDealSearch dealSearch;

	@Autowired
	private IEventSearch eventSearch;
	
	public void wallFeed() 
	{ 
		  
		  logger.debug("Quartz wallFeed :::: "+Calendar.getInstance().getTime());
		  wallService.addWeightContentOnItem();
    }  
	public void suggestion() 
	{  
		
		  logger.debug("suggestion :::: "+Calendar.getInstance().getTime());
		  nonDealSearch.search();
		  logger.debug("deal :::: ");
		  dealSearch.search();
		  logger.debug("event :::: ");
		  eventSearch.search();
    }  
	
	public void fetchFilter() 
	{  
		
		  logger.debug("fetchFilter :::: "+Calendar.getInstance().getTime());
		 
    }  

}
