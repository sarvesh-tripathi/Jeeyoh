package com.jeeyoh.service.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.domain.Business;

@Component("nonDealSuggestionSearch")
public class NonDealSuggestionSearch implements INonDealSuggestionSearch{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IBusinessDAO businessDAO;

	@Override
	@Transactional
	public List<Business> search(String userEmail, String searchText, String category, String location, String rating) {
		List<Business> rows = businessDAO.getBusinessByCriteria(userEmail, searchText, category, location, rating);
		logger.debug("NonDealSuggestionSearch ==> rows ==> "+rows);
		return rows;

	}

}
