package com.jeeyoh.service.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Page;

@Component("communitySearch")
public class CommunitySearch implements ICommunitySearch{

	@Autowired
	private IUserDAO userDAO;
	
	@Override
	@Transactional
	public List<Page> search(String userEmail) {
		List<Page> pageList = userDAO.getUserCommunitiesByEmailId(userEmail);
		return pageList;
	}

}
