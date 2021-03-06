package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Gdealoption generated by hbm2java
 */
public class Gdealoption  implements java.io.Serializable {


     private Integer id;
     private Gdealprice gdealpriceByDiscountId;
     private Gdealprice gdealpriceByPriceId;
     private Gdealprice gdealpriceByValueId;
     private Gdeal gdeal;
     private String optionId;
     private String title;
     private Integer soldQuantity;
     private Boolean isSoldOut;
     private Integer discountPercent;
     private Boolean isLimitedQuantity;
     private Integer initialQuantity;
     private Integer remainingQuantity;
     private Integer minimumPurchaseQuantity;
     private Integer maximumPurchaseQuantity;
     private Date expiresAt;
     private String externalUrl;
     private String buyUrl;
     private Set gredemptionlocations = new HashSet(0);
     private Set goptiondetails = new HashSet(0);

    public Gdealoption() {
    }

	
    public Gdealoption(Gdealprice gdealpriceByDiscountId, Gdealprice gdealpriceByPriceId, Gdealprice gdealpriceByValueId, Gdeal gdeal, String optionId) {
        this.gdealpriceByDiscountId = gdealpriceByDiscountId;
        this.gdealpriceByPriceId = gdealpriceByPriceId;
        this.gdealpriceByValueId = gdealpriceByValueId;
        this.gdeal = gdeal;
        this.optionId = optionId;
    }
    public Gdealoption(Gdealprice gdealpriceByDiscountId, Gdealprice gdealpriceByPriceId, Gdealprice gdealpriceByValueId, Gdeal gdeal, String optionId, String title, Integer soldQuantity, Boolean isSoldOut, Integer discountPercent, Boolean isLimitedQuantity, Integer initialQuantity, Integer remainingQuantity, Integer minimumPurchaseQuantity, Integer maximumPurchaseQuantity, Date expiresAt, String externalUrl, String buyUrl, Set gredemptionlocations, Set goptiondetails) {
       this.gdealpriceByDiscountId = gdealpriceByDiscountId;
       this.gdealpriceByPriceId = gdealpriceByPriceId;
       this.gdealpriceByValueId = gdealpriceByValueId;
       this.gdeal = gdeal;
       this.optionId = optionId;
       this.title = title;
       this.soldQuantity = soldQuantity;
       this.isSoldOut = isSoldOut;
       this.discountPercent = discountPercent;
       this.isLimitedQuantity = isLimitedQuantity;
       this.initialQuantity = initialQuantity;
       this.remainingQuantity = remainingQuantity;
       this.minimumPurchaseQuantity = minimumPurchaseQuantity;
       this.maximumPurchaseQuantity = maximumPurchaseQuantity;
       this.expiresAt = expiresAt;
       this.externalUrl = externalUrl;
       this.buyUrl = buyUrl;
       this.gredemptionlocations = gredemptionlocations;
       this.goptiondetails = goptiondetails;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Gdealprice getGdealpriceByDiscountId() {
        return this.gdealpriceByDiscountId;
    }
    
    public void setGdealpriceByDiscountId(Gdealprice gdealpriceByDiscountId) {
        this.gdealpriceByDiscountId = gdealpriceByDiscountId;
    }
    public Gdealprice getGdealpriceByPriceId() {
        return this.gdealpriceByPriceId;
    }
    
    public void setGdealpriceByPriceId(Gdealprice gdealpriceByPriceId) {
        this.gdealpriceByPriceId = gdealpriceByPriceId;
    }
    public Gdealprice getGdealpriceByValueId() {
        return this.gdealpriceByValueId;
    }
    
    public void setGdealpriceByValueId(Gdealprice gdealpriceByValueId) {
        this.gdealpriceByValueId = gdealpriceByValueId;
    }
    public Gdeal getGdeal() {
        return this.gdeal;
    }
    
    public void setGdeal(Gdeal gdeal) {
        this.gdeal = gdeal;
    }
    public String getOptionId() {
        return this.optionId;
    }
    
    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getSoldQuantity() {
        return this.soldQuantity;
    }
    
    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
    public Boolean getIsSoldOut() {
        return this.isSoldOut;
    }
    
    public void setIsSoldOut(Boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }
    public Integer getDiscountPercent() {
        return this.discountPercent;
    }
    
    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }
    public Boolean getIsLimitedQuantity() {
        return this.isLimitedQuantity;
    }
    
    public void setIsLimitedQuantity(Boolean isLimitedQuantity) {
        this.isLimitedQuantity = isLimitedQuantity;
    }
    public Integer getInitialQuantity() {
        return this.initialQuantity;
    }
    
    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }
    public Integer getRemainingQuantity() {
        return this.remainingQuantity;
    }
    
    public void setRemainingQuantity(Integer remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }
    public Integer getMinimumPurchaseQuantity() {
        return this.minimumPurchaseQuantity;
    }
    
    public void setMinimumPurchaseQuantity(Integer minimumPurchaseQuantity) {
        this.minimumPurchaseQuantity = minimumPurchaseQuantity;
    }
    public Integer getMaximumPurchaseQuantity() {
        return this.maximumPurchaseQuantity;
    }
    
    public void setMaximumPurchaseQuantity(Integer maximumPurchaseQuantity) {
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
    }
    public Date getExpiresAt() {
        return this.expiresAt;
    }
    
    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
    public String getExternalUrl() {
        return this.externalUrl;
    }
    
    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }
    public String getBuyUrl() {
        return this.buyUrl;
    }
    
    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
    }
    public Set getGredemptionlocations() {
        return this.gredemptionlocations;
    }
    
    public void setGredemptionlocations(Set gredemptionlocations) {
        this.gredemptionlocations = gredemptionlocations;
    }
    public Set getGoptiondetails() {
        return this.goptiondetails;
    }
    
    public void setGoptiondetails(Set goptiondetails) {
        this.goptiondetails = goptiondetails;
    }




}


