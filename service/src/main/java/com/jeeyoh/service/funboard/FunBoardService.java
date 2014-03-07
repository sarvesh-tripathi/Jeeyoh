package com.jeeyoh.service.funboard;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.funboard.FunBoardModel;
import com.jeeyoh.model.funboard.FunBoardRequest;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.FunBoardResponse;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IFunBoardDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.BusinessFunboard;
import com.jeeyoh.persistence.domain.DealFunboard;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.EventFunboard;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Funboard;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.PageFunboard;
import com.jeeyoh.persistence.domain.User;

@Component("funBoard")
public class FunBoardService implements IFunBoardService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");


	@Autowired
	private IFunBoardDAO funBoardDAO;
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IBusinessDAO businessDAO;
	
	@Autowired
	private IEventsDAO eventsDAO;
	
	@Autowired
	private IDealsDAO dealsDAO;
	
	@Transactional
	@Override
	public BaseResponse saveFunBoardItem(FunBoardRequest request) {
		
		BaseResponse baseResponse = new BaseResponse();
		User user = userDAO.getUserByEmailId(request.getUserEmail());
		if(user != null)
		{
			int batch_size = 0;
			ArrayList<FunBoardModel> funBoradList = request.getFunBoardList();
			for(FunBoardModel funBoardModel : funBoradList)
			{
				batch_size ++;
				Funboard funboard = new Funboard();
				funboard.setUser(user);
				funboard.setItemId(funBoardModel.getItemId());
				funboard.setCategory(funBoardModel.getCategory());
				funboard.setType(funBoardModel.getType());
				funboard.setCreatedTime(new Date());
				funboard.setUpdatedTime(new Date());
				
				if(funBoardModel.getType().equalsIgnoreCase("Business"))
				{
					Business business = businessDAO.getBusinessById(funBoardModel.getItemId());
					BusinessFunboard businessFunboard = new BusinessFunboard();
					businessFunboard.setBusiness(business);
					businessFunboard.setCategory(funBoardModel.getCategory());
					businessFunboard.setType(funBoardModel.getType());
				}
				else if(funBoardModel.getType().equalsIgnoreCase("Deal"))
				{
					Deals deal = dealsDAO.getDealById(funBoardModel.getItemId());
					DealFunboard dealFunboard = new DealFunboard();
					dealFunboard.setDeal(deal);
					dealFunboard.setCategory(funBoardModel.getCategory());
					dealFunboard.setType(funBoardModel.getType());
				}
				else if(funBoardModel.getType().equalsIgnoreCase("Page"))
				{
					Page page = eventsDAO.getPageDetailsByID(funBoardModel.getItemId());
					PageFunboard pageFunboard = new PageFunboard();
					pageFunboard.setPage(page);
					pageFunboard.setCategory(funBoardModel.getCategory());
					pageFunboard.setType(funBoardModel.getType());
				}
				else if(funBoardModel.getType().equalsIgnoreCase("Event"))
				{
					Events events = eventsDAO.getEventById(funBoardModel.getItemId());
					EventFunboard eventFunboard = new EventFunboard();
					eventFunboard.setEvents(events);
					eventFunboard.setCategory(funBoardModel.getCategory());
					eventFunboard.setType(funBoardModel.getType());
				}
						
				funBoardDAO.saveFunBoard(funboard, batch_size);
			}
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Invalid Parameter");
		}
		return baseResponse;
	}


	@Override
	public FunBoardResponse getUserFunBoardItems(UserModel user) {
		// TODO Auto-generated method stub
		return null;
	}

}
