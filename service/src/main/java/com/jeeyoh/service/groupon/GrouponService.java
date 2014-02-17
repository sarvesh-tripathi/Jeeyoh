package com.jeeyoh.service.groupon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.groupon.DealTypeModel;
import com.jeeyoh.model.groupon.DealsModel;
import com.jeeyoh.model.groupon.DetailModel;
import com.jeeyoh.model.groupon.DivisionModel;
import com.jeeyoh.model.groupon.DivisionResponseModel;
import com.jeeyoh.model.groupon.ErrorMessage;
import com.jeeyoh.model.groupon.MerchantModel;
import com.jeeyoh.model.groupon.OptionModel;
import com.jeeyoh.model.groupon.PriceModel;
import com.jeeyoh.model.groupon.RatingModel;
import com.jeeyoh.model.groupon.RedemptionLocationModel;
import com.jeeyoh.model.groupon.ResponseModel;
import com.jeeyoh.model.groupon.TagModel;
import com.jeeyoh.persistence.dao.groupon.IDivisionDAO;
import com.jeeyoh.persistence.dao.groupon.IGDealsDAO;
import com.jeeyoh.persistence.domain.Gdeal;
import com.jeeyoh.persistence.domain.Gdealoption;
import com.jeeyoh.persistence.domain.Gdealprice;
import com.jeeyoh.persistence.domain.Gdealtype;
import com.jeeyoh.persistence.domain.Gdivision;
import com.jeeyoh.persistence.domain.Gmerchant;
import com.jeeyoh.persistence.domain.Gmerchantrating;
import com.jeeyoh.persistence.domain.Goptiondetail;
import com.jeeyoh.persistence.domain.Gredemptionlocation;
import com.jeeyoh.persistence.domain.Gtags;
import com.jeeyoh.utils.Utils;

@Component("grouponService")
public class GrouponService implements IGrouponService {
	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IDivisionDAO divisionDAO;

	@Autowired
	private IGrouponClient grouponClient;
	
	@Autowired
	private IGDealsDAO dealsDAO;

	@Override
	@Transactional
	public void populateDivisions() {
		DivisionResponseModel divisionResponse = grouponClient.getDivisions();
		
		if (divisionResponse != null) {
			List<DivisionModel> divisions = divisionResponse.getDivisions();
			if (divisions != null) {
				String[] addressArray;
				for (DivisionModel divisionModel : divisions) {
					addressArray = Utils.getGeographicalInfo(Double.parseDouble(divisionModel.getLattitude()), Double.parseDouble(divisionModel.getLongitude()));
					Gdivision division = new Gdivision();
					division.setDivisionId(divisionModel.getDivisionId());
					division.setName(divisionModel.getName());
					division.setLongitude(divisionModel.getLongitude());
					division.setLattitude(divisionModel.getLattitude());
					division.setTimezone(divisionModel.getTimezone());
					division.setTimezoneOffsetInSeconds(divisionModel
							.getTimezoneOffsetInSeconds());
					division.setUuid(divisionModel.getUuid());
					division.setIsNowCustomerEnabled(divisionModel
							.isNowCustomerEnabled());
					division.setIsPresenceEnabled(divisionModel
							.isPresenceEnabled());
					division.setDefaultLocale(divisionModel.getDefaultLocale());
					division.setIsReserveEnabled(divisionModel
							.isReserveEnabled());
					division.setTimezoneIdentifier(divisionModel
							.getTimezoneIdentifier());
					division.setCountry(divisionModel.getCountry());
					division.setIsNowMerchantEnabled(divisionModel
							.isNowMerchantEnabled());
					if(addressArray != null)
					{
						division.setZipCode(addressArray[0]);
						division.setAddress(addressArray[1]);
					}
					divisionDAO.addDivision(division);
				}
			}
		}
	}
	
	@Override
	@Transactional
	public void loadDeals(String country) {
		logger.debug("Inside loadDeals");
		List<Gdivision> divisions = divisionDAO.getDivisionsByCountry(country);
		//List<Gdivision> divisions = divisionDAO.getDivisionsById(560);
		if(divisions != null) {
			
			String longitude;
			String lattitude;
			int batch_size = 0;
			for(Gdivision division : divisions) {
				longitude = division.getLongitude();
				lattitude = division.getLattitude();
				ResponseModel dealResponse = grouponClient.getDealsByDivision(longitude, lattitude);				
				if(dealResponse != null)
				{
					ErrorMessage error = dealResponse.getError();
					if(error == null )
					{
						List<DealsModel> deals = dealResponse.getDeals();
						if(deals != null)
						{
							for(DealsModel dealModel : deals) {
								batch_size++;
								Gdeal deal = new Gdeal();
								deal.setAnnouncementTitle(dealModel.getAnnouncementTitle());
								deal.setDealId(dealModel.getDealId());
								deal.setDealType(dealModel.getType());
								deal.setDealUrl(dealModel.getDealUrl());								
								deal.setEndAt(new DateTime(dealModel.getEndAt()).toDate());
								deal.setGdivision(division);
								deal.setHighlightsHtml(dealModel.getHighlightsHtml());
								deal.setIsNowDeal(dealModel.isNowDeal());
								deal.setIsSoldOut(dealModel.isSoldOut());
								deal.setIsTipped(dealModel.isTipped());
								deal.setLargeImageUrl(dealModel.getLargeImageUrl());
								deal.setMediumImageUrl(dealModel.getMediumImageUrl());
								deal.setPitchHtml(dealModel.getPitchHtml());
								deal.setPlacementPriority(dealModel.getPlacementPriority());
								deal.setShippingAddressRequired(dealModel.isShippingAddressRequired());
								deal.setSidebarImageUrl(dealModel.getSidebarImageUrl());
								deal.setSmallImageUrl(dealModel.getSmallImageUrl());
								deal.setSoldQuantity("" + dealModel.getSoldQuantity());
								deal.setStartAt(new DateTime(dealModel.getStartAt()).toDate());
								//deal.setStartAt(dealModel.getStartAt());
								deal.setStatus(dealModel.getStatus());
								//deal.setTippedAt(dealModel.getTippedAt());
								deal.setTippedAt(new DateTime(dealModel.getTippedAt()).toDate());
								deal.setTippingPoint(dealModel.getTippingPoint());
								deal.setTitle(dealModel.getTitle());
								MerchantModel merchantModel = dealModel.getMerchant();
								if(merchantModel != null) {									
									Gmerchant merchant = new Gmerchant();
									merchant.setMerchantId(merchantModel.getMerchantId());
									merchant.setName(merchantModel.getName());
									merchant.setWebsiteUrl(merchantModel.getWebsiteUrl());
									RatingModel ratingModel = new RatingModel();
									Gmerchantrating gmerchantrating = new Gmerchantrating();
									gmerchantrating.setLinkText(ratingModel.getLinkText());
									gmerchantrating.setRating(ratingModel.getRating());
									gmerchantrating.setRatingId(ratingModel.getId());
									gmerchantrating.setReviewsCount(ratingModel.getReviewsCount());
									gmerchantrating.setUrl(ratingModel.getUrl());
									merchant.setGmerchantByRatingId(gmerchantrating);
									deal.setGmerchant(merchant);
								}
								
								List<OptionModel> options = dealModel.getOptions();
								if(options != null) {
									Set<Gdealoption> dealOptions = new HashSet<Gdealoption>();
									Gdealoption dealOption = null;
									for(OptionModel option : options) {
										dealOption = new Gdealoption();
										dealOption.setTitle(option.getTitle());
										dealOption.setSoldQuantity(option.getSoldQuantity());
										dealOption.setIsSoldOut(option.isSoldOut());
										dealOption.setOptionId(option.getOptionId());										
										dealOption.setDiscountPercent(option.getDiscountPercent());
										dealOption.setIsLimitedQuantity(option.isLimitedQuantity());
										dealOption.setInitialQuantity(option.getInitialQuantity());
										dealOption.setRemainingQuantity(option.getRemainingQuantity());
										dealOption.setMaximumPurchaseQuantity(option.getMaximumPurchaseQuantity());
										dealOption.setMinimumPurchaseQuantity(option.getMinimumPurchaseQuantity());
										dealOption.setExternalUrl(option.getExternalUrl());
										dealOption.setBuyUrl(option.getBuyUrl());
										dealOption.setExpiresAt(new DateTime(option.getExpiresAt()).toDate());
										PriceModel valueModel = option.getValue();
										Gdealprice value = new Gdealprice();
										value.setAmount(valueModel.getAmount());
										value.setCurrencyCode(valueModel.getCurrencyCode());
										value.setFormattedAmount(valueModel.getFormattedAmount());										
										dealOption.setGdealpriceByValueId(value);
										
										valueModel = option.getPrice();
										Gdealprice price = new Gdealprice();
										price.setAmount(valueModel.getAmount());
										price.setCurrencyCode(valueModel.getCurrencyCode());
										price.setFormattedAmount(valueModel.getFormattedAmount());										
										dealOption.setGdealpriceByPriceId(price);
										
										valueModel = option.getDiscount();
										Gdealprice discount = new Gdealprice();
										discount.setAmount(valueModel.getAmount());
										discount.setCurrencyCode(valueModel.getCurrencyCode());
										discount.setFormattedAmount(valueModel.getFormattedAmount());										
										dealOption.setGdealpriceByDiscountId(discount);
										
										List<DetailModel> optionDetails = option.getDetails();
										if(optionDetails != null) {
											Set<Goptiondetail> dealOptionDetails = new HashSet<Goptiondetail>();
											for(DetailModel optionDetail : optionDetails) {
												Goptiondetail dealOptionDetail = new Goptiondetail();
												dealOptionDetail.setDescription(optionDetail.getDescription());												
												dealOptionDetail.setGdealoption(dealOption);												
												dealOptionDetails.add(dealOptionDetail);
											}											
											dealOption.setGoptiondetails(dealOptionDetails);
										}
										
										List<RedemptionLocationModel> redemptionLocations = option.getRedemptionLocations();
										if(redemptionLocations != null) {
											Set<Gredemptionlocation> dealRedemptionLocations = new HashSet<Gredemptionlocation>();
											for(RedemptionLocationModel redemptionLocation : redemptionLocations) {
												Gredemptionlocation dealRedemptionLocation = new Gredemptionlocation();
												dealRedemptionLocation.setStreetAddress1(redemptionLocation.getStreetAddress1());
												dealRedemptionLocation.setStreetAddress2(redemptionLocation.getStreetAddress2());
												dealRedemptionLocation.setCity(redemptionLocation.getCity());
												dealRedemptionLocation.setState(redemptionLocation.getState());
												dealRedemptionLocation.setPostalCode(redemptionLocation.getPostalCode());
												dealRedemptionLocation.setName(redemptionLocation.getName());
												dealRedemptionLocation.setLattitude(redemptionLocation.getLattitude());
												dealRedemptionLocation.setLongitude(redemptionLocation.getLongitude());
												dealRedemptionLocation.setPhoneNumber(redemptionLocation.getPhoneNumber());
												dealRedemptionLocation.setGdealoption(dealOption);												
												dealRedemptionLocations.add(dealRedemptionLocation);
											}											
											dealOption.setGredemptionlocations(dealRedemptionLocations);
										}
										dealOption.setGdeal(deal);
										dealOptions.add(dealOption);
									}
									deal.setGdealoptions(dealOptions);
									
									List<TagModel> dealTags = dealModel.getTags();
									if(dealTags != null) {
										Set<Gtags> dealTagsSet = new HashSet<Gtags>();
										for(TagModel dealTag : dealTags) {
											Gtags tag = new Gtags();
											tag.setGdeal(deal);
											tag.setName(dealTag.getName());
											dealTagsSet.add(tag);
										}
										deal.setGtagses(dealTagsSet);
									}
									
									List<DealTypeModel> dealTypes = dealModel.getDealTypes();
									if(dealTypes != null) {
										Set<Gdealtype> dealTypesSet = new HashSet<Gdealtype>();
										for(DealTypeModel dealType : dealTypes) {
											Gdealtype type = new Gdealtype();
											type.setDealtypeId(dealType.getDealTypeId());
											type.setDescription(dealType.getDescription());
											type.setGdeal(deal);
											type.setName(dealType.getName());
											dealTypesSet.add(type);
										}
										deal.setGdealtypes(dealTypesSet);
									}
								}								
								dealsDAO.addDeals(deal,batch_size);
							}
						} else {
							logger.debug("loadDeals => no deals found");
						}
					} else {
						logger.debug("loadDeals ==> error code => " + error.getHttpCode() + " error message => " + error.getMessage());
					}
				}
			}
		}
	}
	
	@Override
	public void loadDeals() {
		
	}
}
