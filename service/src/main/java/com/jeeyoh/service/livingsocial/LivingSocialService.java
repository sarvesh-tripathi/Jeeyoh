package com.jeeyoh.service.livingsocial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.jeeyoh.model.livingsocial.CitiesModel;
import com.jeeyoh.model.livingsocial.ImagesModel;
import com.jeeyoh.model.livingsocial.LdealModel;
import com.jeeyoh.model.livingsocial.LdealOptionModel;
import com.jeeyoh.model.livingsocial.LdealResponseModel;
import com.jeeyoh.persistence.dao.livingsocial.ILCitiesDAO;
import com.jeeyoh.persistence.dao.livingsocial.ILivingSocialDAO;
import com.jeeyoh.persistence.dao.yelp.ICountryLocationDAO;
import com.jeeyoh.persistence.domain.Countrylocation;
import com.jeeyoh.persistence.domain.LCities;
import com.jeeyoh.persistence.domain.Ldeal;
import com.jeeyoh.persistence.domain.LdealCategory;
import com.jeeyoh.persistence.domain.LdealOption;
import com.jeeyoh.utils.Utils;


@Component("livingSocialService")
public class LivingSocialService implements ILivingSocialService{
	private static Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private ILCitiesDAO lCitiesDAO;
	@Autowired
	private ILivingSocialDAO livingSocialDAO;
	@Autowired
	private ILivingSocialClient livingSocialClient;
	
	@Autowired
	private	ICountryLocationDAO countryLocationDAO;
	
	@Override
	@Transactional
	public void loadCities() {
		logger.debug("Inside loadCities ====>");
		CitiesModel[] citiesModel = livingSocialClient.getCities();
		if(citiesModel!=null)
		{
			for(int i = 0;i<citiesModel.length;i++)
			{
				logger.debug("loadCities ====>"+citiesModel[i].getCountryId());
				if(citiesModel[i].getCountryId()==1 || citiesModel[i].getCountryId()==41)
				{
					logger.debug("loadCities ====>"+citiesModel[i].getCityName());
					LCities lCities = new LCities();
					if(citiesModel[i].getLatitude()!=null && citiesModel[i].getLongitude()!=null)
					{
						lCities.setLatitude(citiesModel[i].getLatitude());
						lCities.setLongitude(citiesModel[i].getLongitude());
						String[] addressArray = Utils.getZipCodeAndAddress(Double.parseDouble(citiesModel[i].getLatitude()), Double.parseDouble(citiesModel[i].getLongitude()));
						if(addressArray[0]!=null)
						{
							logger.debug("addressArray[0] ====>"+addressArray[0]);
							lCities.setZipcode(addressArray[0]);
						}
						if(addressArray[1]!=null)
						{
							logger.debug("addressArray[1] ====>"+addressArray[1]);
							lCities.setAddress(addressArray[1]);
						}
					}
					lCities.setBackgroundImageUrl(citiesModel[i].getBackgroundImageUrl());
					lCities.setCityId(citiesModel[i].getCityId());
					lCities.setCityName(citiesModel[i].getCityName());
					lCities.setCountryName(citiesModel[i].getCountryName());
					logger.debug("citiesModel[i].getStateCode() =====>"+citiesModel[i].getStateCode());
					if(citiesModel[i].getStateCode()!=null)
					{
						logger.debug("citiesModel[i].getStateCode() =====>"+citiesModel[i].getStateCode());
						lCities.setStateCode(citiesModel[i].getStateCode());
						Countrylocation countryLoc = countryLocationDAO.getStateNameByStateCode(citiesModel[i].getStateCode());
						logger.debug("countryLoc ====>"+countryLoc.getState()+";;;;;"+countryLoc.getCountry());
						lCities.setState(countryLoc.getState());
					}
					
					lCitiesDAO.addCities(lCities);
				}
				
			}
		}
	}
	@Override
	@Transactional
	public void loadLdeals() {
		logger.debug("loadLdeals ====>");
		List<LCities> lCitiesList = lCitiesDAO.getLCities();
		
		
		for(LCities lCity:lCitiesList)
		{
			logger.debug("loadLdeals lcity====>"+lCity.getCityId().toString());
			LdealResponseModel ldealResponseModel= livingSocialClient.getLDeals(lCity.getCityId().toString());
			
			if(ldealResponseModel!=null)
			{
				logger.debug("ldealResponseModel ===>"+ldealResponseModel);
				List<LdealModel> ldealModelList = ldealResponseModel.getLdeal();
				if(ldealModelList!=null)
				{
					int batch_size = 0;
					logger.debug("ldealModelList ===>"+ldealModelList.size());
					for(LdealModel ldealModel:ldealModelList)
					{
						batch_size ++;
						logger.debug("ldealModel ===>"+ldealModel.getDealId());
						Ldeal ldeal = new Ldeal();
						ldeal.setCityName(lCity.getCityName());
						ldeal.setCountryName(lCity.getCountryName());
						ldeal.setDealId(ldealModel.getDealId());
						ldeal.setDealType(ldealModel.getDealType());
						ldeal.setDealUrl(ldealModel.getDealUrl());
						ldeal.setDistance(ldealModel.getDistance());
						ldeal.setEndAt(ldealModel.getEndAt());
						ldeal.setStartAt(ldealModel.getStartAt());
						ldeal.setIsSoldOut(ldealModel.getIsSoldOut());
						ldeal.setLongTitle(ldealModel.getLongTitle());
						ldeal.setMerchantName(ldealModel.getMerchantName());
						ldeal.setOrderCount(ldealModel.getOrderCount());
						ldeal.setPrice(ldealModel.getPrice());
						ldeal.setShortTitle(ldealModel.getShortTitle());
						ArrayList<ImagesModel> imageModelList = ldealModel.getImages();
						for(ImagesModel imageModel:imageModelList)
						{
							ldeal.setSmallImageUrl(imageModel.getSmallImageUrl());
							ldeal.setMediumImageUrl(imageModel.getMediumImageUrl());
							ldeal.setLargeImageUrl(imageModel.getLargeImageUrl());
						}
						ArrayList<String> lcategoryArray = ldealModel.getCategoryName();
						if(lcategoryArray!=null)
						{
							Set<LdealCategory> ldealCategory = new HashSet<LdealCategory>();
							for(String category:lcategoryArray)
							{
								LdealCategory ldealCategorys = new LdealCategory();
								ldealCategorys.setLdeal(ldeal);
								ldealCategorys.setCategoryName(category);
								ldealCategory.add(ldealCategorys);
							}
							ldeal.setLdealCategory(ldealCategory);
						}
							
						ArrayList<LdealOptionModel> ldealOptionArray = ldealModel.getLdealOptions();
						if(ldealOptionArray!=null)
						{
							Set<LdealOption> dealOptions = new HashSet<LdealOption>();
							for(LdealOptionModel ldealOptionModel:ldealOptionArray)
							{
								LdealOption ldealOption = new LdealOption();
								ldealOption.setDescription(ldealOptionModel.getDescription());
								ldealOption.setDiscount(ldealOptionModel.getDiscount());
								ldealOption.setIsSoldOut(ldealOptionModel.getIsSoldOut());
								ldealOption.setLdeal(ldeal);
								ldealOption.setMonthlyCapReached(ldealOptionModel.getMonthlyCapReached());
								ldealOption.setOriginalPrice(ldealOptionModel.getOriginalPrice());
								ldealOption.setPrice(ldealOptionModel.getPrice());
								ldealOption.setSavings(ldealOptionModel.getSavings());
								dealOptions.add(ldealOption);
							}
							ldeal.setDealOptions(dealOptions);
						}
						
						livingSocialDAO.addLdeals(ldeal, batch_size);
					} 
				}
			}
		}				
	}
}
