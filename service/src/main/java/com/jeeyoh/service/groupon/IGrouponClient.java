package com.jeeyoh.service.groupon;

import com.jeeyoh.model.groupon.DivisionResponseModel;
import com.jeeyoh.model.groupon.ResponseModel;

public interface IGrouponClient {
	public ResponseModel getDeals();
	public DivisionResponseModel getDivisions();
	public ResponseModel getDealsByDivision(String divisionId);
	public ResponseModel getDealsByDivision(String longitude, String lattitude);
}
