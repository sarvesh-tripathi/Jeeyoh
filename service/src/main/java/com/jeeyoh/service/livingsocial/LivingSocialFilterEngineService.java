package com.jeeyoh.service.livingsocial;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.dao.livingsocial.ILCitiesDAO;
import com.jeeyoh.persistence.dao.livingsocial.ILivingSocialDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.LCities;
import com.jeeyoh.persistence.domain.Lcategory;
import com.jeeyoh.persistence.domain.Ldeal;
import com.jeeyoh.persistence.domain.LdealCategory;
import com.jeeyoh.persistence.domain.LdealOption;
import com.jeeyoh.utils.Utils;

@Component("livingSocialFilterEngine")
public class LivingSocialFilterEngineService implements ILivingSocialFilterEngineService{

	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private ILivingSocialDAO livingSocialDAO;

	@Autowired
	private IDealsDAO dealsDAO;

	@Autowired
	private IBusinessDAO businessDAO;

	@Autowired
	private ILCitiesDAO lCitiesDAO;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void filter() {
		
		List<Ldeal> rows = livingSocialDAO.getLDeals();
		List<LdealOption> ldealOptions = null;
		
		if(rows != null)
		{
			int batch_size = 0;
			List<Date>weekendList =  Utils.findWeekends();
			logger.debug("loadDeals => weekendList size " + weekendList.size());
			for(Ldeal ldeal : rows)
			{
				for(int j = 0; j < weekendList.size(); j++){
					try {
						//logger.debug("Date::  "+ ldeal.getEndAt() +"  :  "+ sdf.parse(sdf.format((Date)weekendList.get(j))));
						if(ldeal.getEndAt().before(weekendList.get(j))){
							Deals deal = dealsDAO.isDealExists(ldeal.getDealId().toString());
							if(deal == null)
							{
								batch_size++;
								logger.debug("ldeal id ===>"+ ldeal.getId());
								Deals deals =new Deals();
								deals.setDealId(ldeal.getDealId().toString());
								deals.setDealUrl(ldeal.getDealUrl());
								deals.setEndAt(ldeal.getEndAt());
								deals.setStartAt(ldeal.getStartAt());
								deals.setIsSoldOut(ldeal.getIsSoldOut());
								deals.setDealType(ldeal.getDealType());
								deals.setLargeImageUrl(ldeal.getLargeImageUrl());
								deals.setSmallImageUrl(ldeal.getSmallImageUrl());
								deals.setMediumImageUrl(ldeal.getMediumImageUrl());
								deals.setTitle(ldeal.getLongTitle());
								deals.setDealSource("Living Social");

								List<Business> businessList = businessDAO.getBusinessByIdForGroupon(ldeal.getMerchantName());
								logger.debug("loadDeals => businessList " + businessList);
								if(businessList != null)
								{
									if(businessList.isEmpty())
									{
										Set<LdealCategory> ldealCategories = ldeal.getLdealCategory();
										Businesstype businesstype = null;
										for(LdealCategory ldealCategory : ldealCategories)
										{
											String name = ldealCategory.getCategoryName();
											logger.debug("Check Name ::: "+name);
											Lcategory lCategory = businessDAO.getLivingSocialBusinessCategory(name) ;
											if(lCategory!=null)
											{
												String category = lCategory.getLcategory().getCategory();
												logger.debug("category ::: "+category);
												if(category.equalsIgnoreCase("Fitness/Active"))
												{
													businesstype = businessDAO.getBusinesstypeByType("SPORT");
													break;
												}
												else if(category.equalsIgnoreCase("Full-Service Restaurant"))
												{
													businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
													break;
												}
												else if(category.equalsIgnoreCase("Beauty/Health"))
												{
													businesstype = businessDAO.getBusinesstypeByType("SPA");
													break;
												}
												else if(category.equalsIgnoreCase("Nightlife"))
												{
													businesstype = businessDAO.getBusinesstypeByType("NIGHTLIFE");
													break;
												}
												else if(category.equalsIgnoreCase("Fitness/Active") || category.equalsIgnoreCase("Health & Beauty"))
												{
													businesstype = businessDAO.getBusinesstypeByType("SPA");
													break;
												}
											}
										}

										Business business = new Business();
										LCities lCity = lCitiesDAO.getCityByName(ldeal.getCityName());
										if(lCity!=null)
										{
											business.setLattitude(lCity.getLatitude());
											business.setLongitude(lCity.getLongitude());
											business.setPostalCode(lCity.getZipcode());
											business.setCity(lCity.getCityName());
											business.setDisplayAddress(lCity.getAddress());
											business.setAddress(lCity.getAddress());
											business.setState(lCity.getState());
											business.setStateCode(lCity.getStateCode());
										}
										business.setName(ldeal.getMerchantName());
										business.setBusinesstype(businesstype);
										business.setSource("Living Social");

										deals.setBusiness(business);
									}
									else
									{
										deals.setBusiness(businessList.get(0));
									}
								}
								
								ldealOptions = livingSocialDAO.getDealOptions(ldeal.getId());
								
								if(ldealOptions!=null)
								{
									logger.debug("ldealOptions.size()::  "+ldealOptions.size());
									Set<Dealoption> dealOptions = new HashSet<Dealoption>();
									Dealoption dealOption = null;
									for(LdealOption option : ldealOptions)
									{
										dealOption = new Dealoption();
										dealOption.setDescription(option.getDescription());
										dealOption.setPrice(option.getPrice());
										dealOption.setOriginalPrice(option.getOriginalPrice());
										dealOption.setDiscountPercent(option.getDiscount());
										dealOption.setDeals(deals);
										dealOptions.add(dealOption);
									}
									deals.setDealoptions(dealOptions);
								}
								logger.debug("loadDeals => count ");
								dealsDAO.saveDeal(deals,batch_size);
							}
							break;
						}

					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				
			}
		}
		
		/*List<LdealOption> rows = livingSocialDAO.getDeals();
		logger.debug("loadDeals => row size " + rows.size());

		//Get current date
		//Date currentDate = Utils.getCurrentDate();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Set<LdealOption> ldealOption = new HashSet<LdealOption>();
		List<Date>weekendList =  Utils.findWeekends();
		logger.debug("loadDeals => weekendList size " + weekendList.size());
		Ldeal ldeal = null;
		if(rows!=null)
		{
			int batch_size = 0;
			for(int i = 0; i < rows.size();i++){
				LdealOption row = (LdealOption) rows.get(i);
				ldeal = row.getLdeal();
				ldealOption.clear();
				ldealOption.add(row);

				for(int j = i+1; j < rows.size(); j++)
				{
					if(row.getLdeal().getId() == rows.get(j).getLdeal().getId())
					{
						ldealOption.add((LdealOption) rows.get(j));
						rows.remove(j);
						j--;
					}
				}

				LdealOption row = (LdealOption) rows.get(i);
				Integer ldealId = row.getLdeal().getId();
				if (i == 0){
					ldeal = row.getLdeal();
					ldealOption = new HashSet<LdealOption>();
					ldealOption.add(row);
				}
				else{
					int preldealId = rows.get(i - 1).getLdeal().getId();
					if (ldealId != preldealId){
						for(int j = 0; j < weekendList.size(); j++){
							try {
								//logger.debug("Date::  "+ ldeal.getEndAt() +"  :  "+ sdf.parse(sdf.format((Date)weekendList.get(j))));
								if(ldeal.getEndAt().before(sdf.parse(sdf.format((Date)weekendList.get(j))))){
									Deals deal = dealsDAO.isDealExists(ldeal.getDealId().toString());
									if(deal == null)
									{
										batch_size++;
										logger.debug("ldeal id ===>"+ ldeal.getId());
										ldeal.setDealOptions(ldealOption);
										Deals deals =new Deals();
										deals.setDealId(ldeal.getDealId().toString());
										deals.setDealUrl(ldeal.getDealUrl());
										deals.setEndAt(ldeal.getEndAt());
										deals.setStartAt(ldeal.getStartAt());
										deals.setIsSoldOut(ldeal.getIsSoldOut());
										deals.setDealType(ldeal.getDealType());
										deals.setLargeImageUrl(ldeal.getLargeImageUrl());
										deals.setSmallImageUrl(ldeal.getSmallImageUrl());
										deals.setMediumImageUrl(ldeal.getMediumImageUrl());
										deals.setTitle(ldeal.getLongTitle());
										deals.setDealSource("Living Social");

										List<Business> businessList = businessDAO.getBusinessByIdForGroupon(ldeal.getMerchantName());
										logger.debug("loadDeals => businessList " + businessList);
										if(businessList != null)
										{
											if(businessList.isEmpty())
											{
												Set<LdealCategory> ldealCategories = ldeal.getLdealCategory();
												Businesstype businesstype = null;
												for(LdealCategory ldealCategory : ldealCategories)
												{
													String name = ldealCategory.getCategoryName();
													logger.debug("Check Name ::: "+name);
													Lcategory lCategory = businessDAO.getLivingSocialBusinessCategory(name) ;
													if(lCategory!=null)
													{
														String category = lCategory.getLcategory().getCategory();
														logger.debug("category ::: "+category);
														if(category.equalsIgnoreCase("Fitness/Active"))
														{
															businesstype = businessDAO.getBusinesstypeByType("SPORT");
															break;
														}
														else if(category.equalsIgnoreCase("Full-Service Restaurant"))
														{
															businesstype = businessDAO.getBusinesstypeByType("RESTAURANT");
															break;
														}
														else if(category.equalsIgnoreCase("Beauty/Health"))
														{
															businesstype = businessDAO.getBusinesstypeByType("SPA");
															break;
														}
														else if(category.equalsIgnoreCase("Nightlife"))
														{
															businesstype = businessDAO.getBusinesstypeByType("NIGHTLIFE");
															break;
														}
														else if(category.equalsIgnoreCase("Fitness/Active") || category.equalsIgnoreCase("Health & Beauty"))
														{
															businesstype = businessDAO.getBusinesstypeByType("SPA");
															break;
														}
													}
												}

												Business business = new Business();
												LCities lCity = lCitiesDAO.getCityByName(ldeal.getCityName());
												if(lCity!=null)
												{
													business.setLattitude(lCity.getLatitude());
													business.setLongitude(lCity.getLongitude());
													business.setPostalCode(lCity.getZipcode());
													business.setCity(lCity.getCityName());
													business.setDisplayAddress(lCity.getAddress());
													business.setAddress(lCity.getAddress());
													business.setState(lCity.getState());
													business.setStateCode(lCity.getStateCode());
												}
												business.setName(ldeal.getMerchantName());
												business.setBusinesstype(businesstype);
												business.setSource("Living Social");

												//businessDAO.saveBusiness(business);
												//businessList = businessDAO.getBusinessByIdForLivingSocial(business.getBusinessId());
												deals.setBusiness(business);

											}
											else
											{
												deals.setBusiness(businessList.get(0));
											}
										}
										Set<LdealOption> ldealOption1 = ldeal.getDealOptions();

										if(ldealOption1!=null)
										{
											Set<Dealoption> dealOptions = new HashSet<Dealoption>();
											Dealoption dealOption = null;
											for(LdealOption option : ldealOption1)
											{
												dealOption = new Dealoption();
												dealOption.setDescription(option.getDescription());
												dealOption.setPrice(option.getPrice());
												dealOption.setOriginalPrice(option.getOriginalPrice());
												dealOption.setDiscountPercent(option.getDiscount());
												dealOption.setDeals(deals);
												dealOptions.add(dealOption);
											}
											deals.setDealoptions(dealOptions);
										}
										logger.debug("loadDeals => count ");
										dealsDAO.saveDeal(deals,batch_size);
									}
									break;
								}

							}
							catch(ParseException e){
								e.printStackTrace();
							}
						}
						ldeal = row.getLdeal();
						ldealOption = new HashSet<LdealOption>();
						ldealOption.add(row);
					}
					else { 
						ldealOption.add(row);
					} 
				}
			}
		}	*/
	}
}
