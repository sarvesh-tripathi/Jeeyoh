package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Page;

@Component("communitySearch")
public class CommunitySearch implements ICommunitySearch{

	@Autowired
	private IUserDAO userDAO;
	
	@Override
	@Transactional
	public List<PageModel> search(String userEmail) {
		List<Page> pageList = userDAO.getUserCommunitiesByEmailId(userEmail);
		List<PageModel> pageModelList = new ArrayList<PageModel>();
		for(Page page : pageList)
		{
			PageModel pageModel = new PageModel();
			pageModel.setAbout(page.getAbout());
			pageModel.setPageUrl(page.getPageUrl());
			pageModel.setOwner(page.getUserByOwnerId().getFirstName());
			pageModel.setPageType(page.getPagetype().getPageType());
			pageModel.setCreatedDate(page.getCreatedtime().toString());
			pageModelList.add(pageModel);
		}
		return pageModelList;
	}

}
