package com.jeeyoh.service.livingsocial;

import java.util.List;

import com.jeeyoh.model.groupon.ResponseModel;
import com.jeeyoh.model.livingsocial.CitiesModel;
import com.jeeyoh.model.livingsocial.LdealResponseModel;


public interface ILivingSocialClient {
	public CitiesModel[] getCities();

	public LdealResponseModel getLDeals(String cityId);
}
