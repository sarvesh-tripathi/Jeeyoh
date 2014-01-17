package com.jeeyoh.service.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.domain.Deals;

@Component("userDealsSearch")
public class UserDealsSearch implements IUserDealsSearch{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	private IDealsDAO dealsDAO;
	
	@Override
	@Transactional
	public List<Deals> search(String userEmail)
	{
		List<Deals> dealList = dealsDAO.getDealsByUserEmail(userEmail);
		logger.debug("UserDealsSearch ==> search ==> dealList ==> "+dealList);
		return dealList;
	}

}
