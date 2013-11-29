package com.jeeyoh.service.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.dao.groupon.IGDealsDAO;

@Component("dealSearch")
public class DealSearch implements IDealSearch {

	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IGDealsDAO dealDAO;
	
	@Autowired
	private IBusinessDAO businessDAO;
	
	
	@Override
	public void search() {
		// TODO Auto-generated method stub

	}

}
