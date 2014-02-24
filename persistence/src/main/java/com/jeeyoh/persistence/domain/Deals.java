package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Deals generated by hbm2java
 */
public class Deals  implements java.io.Serializable {


     private Integer id;
     private Business business;
     private String dealId;
     private String title;
     private String placementPriority;
     private String sidebarImageUrl;
     private String smallImageUrl;
     private String mediumImageUrl;
     private String largeImageUrl;
     private String announcementTitle;
     private String dealUrl;
     private String status;
     private Boolean isTipped;
     private Integer tippingPoint;
     private Boolean isSoldOut;
     private Boolean isPopular;
     private String additionalDeals;
     private String impRestrictions;
     private String additionalRestrictions;
     private String soldQuantity;
     private Boolean shippingAddressRequired;
     private String highlightsHtml;
     private String pitchHtml;
     private String dealType;
     private Date startAt;
     private Date endAt;
     private Boolean isNowDeal;
     private Date tippedAt;
     private String dealSource;
     private Set dealsusages = new HashSet(0);
     private Set dealoptions = new HashSet(0);
     private Set userdealmaps = new HashSet(0);
     private Set tags = new HashSet(0);
     private Set userdealssuggestions = new HashSet(0);
     private Set topdealssuggestions = new HashSet(0);
 

	public Set getTags() {
		return tags;
	}


	public void setTags(Set tags) {
		this.tags = tags;
	}


	public Deals() {
    }

	
    public Deals(Business business) {
        this.business = business;
    }
    public Deals(Business business, String dealId, String title, String placementPriority, String sidebarImageUrl, String smallImageUrl, String mediumImageUrl, String largeImageUrl, String announcementTitle, String dealUrl, String status, Boolean isTipped, Integer tippingPoint, Boolean isSoldOut, Boolean isPopular, String additionalDeals, String impRestrictions, String additionalRestrictions, String soldQuantity, Boolean shippingAddressRequired, String highlightsHtml, String pitchHtml, String dealType, Date startAt, Date endAt, Boolean isNowDeal, Date tippedAt, String dealSource, Set dealsusages, Set dealoptions, Set tags) {
       this.business = business;
       this.dealId = dealId;
       this.title = title;
       this.placementPriority = placementPriority;
       this.sidebarImageUrl = sidebarImageUrl;
       this.smallImageUrl = smallImageUrl;
       this.mediumImageUrl = mediumImageUrl;
       this.largeImageUrl = largeImageUrl;
       this.announcementTitle = announcementTitle;
       this.dealUrl = dealUrl;
       this.status = status;
       this.isTipped = isTipped;
       this.tippingPoint = tippingPoint;
       this.isSoldOut = isSoldOut;
       this.isPopular = isPopular;
       this.additionalDeals = additionalDeals;
       this.impRestrictions = impRestrictions;
       this.additionalRestrictions = additionalRestrictions;
       this.soldQuantity = soldQuantity;
       this.shippingAddressRequired = shippingAddressRequired;
       this.highlightsHtml = highlightsHtml;
       this.pitchHtml = pitchHtml;
       this.dealType = dealType;
       this.startAt = startAt;
       this.endAt = endAt;
       this.isNowDeal = isNowDeal;
       this.tippedAt = tippedAt;
       this.dealSource = dealSource;
       this.dealsusages = dealsusages;
       this.dealoptions = dealoptions;
      
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Business getBusiness() {
        return this.business;
    }
    
    public void setBusiness(Business business) {
        this.business = business;
    }
    public String getDealId() {
        return this.dealId;
    }
    
    public void setDealId(String dealId) {
        this.dealId = dealId;
    }
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPlacementPriority() {
        return this.placementPriority;
    }
    
    public void setPlacementPriority(String placementPriority) {
        this.placementPriority = placementPriority;
    }
    public String getSidebarImageUrl() {
        return this.sidebarImageUrl;
    }
    
    public void setSidebarImageUrl(String sidebarImageUrl) {
        this.sidebarImageUrl = sidebarImageUrl;
    }
    public String getSmallImageUrl() {
        return this.smallImageUrl;
    }
    
    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }
    public String getMediumImageUrl() {
        return this.mediumImageUrl;
    }
    
    public void setMediumImageUrl(String mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
    }
    public String getLargeImageUrl() {
        return this.largeImageUrl;
    }
    
    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }
    public String getAnnouncementTitle() {
        return this.announcementTitle;
    }
    
    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }
    public String getDealUrl() {
        return this.dealUrl;
    }
    
    public void setDealUrl(String dealUrl) {
        this.dealUrl = dealUrl;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public Boolean getIsTipped() {
        return this.isTipped;
    }
    
    public void setIsTipped(Boolean isTipped) {
        this.isTipped = isTipped;
    }
    public Integer getTippingPoint() {
        return this.tippingPoint;
    }
    
    public void setTippingPoint(Integer tippingPoint) {
        this.tippingPoint = tippingPoint;
    }
    public Boolean getIsSoldOut() {
        return this.isSoldOut;
    }
    
    public void setIsSoldOut(Boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }
    public Boolean getIsPopular() {
        return this.isPopular;
    }
    
    public void setIsPopular(Boolean isPopular) {
        this.isPopular = isPopular;
    }
    public String getAdditionalDeals() {
        return this.additionalDeals;
    }
    
    public void setAdditionalDeals(String additionalDeals) {
        this.additionalDeals = additionalDeals;
    }
    public String getImpRestrictions() {
        return this.impRestrictions;
    }
    
    public void setImpRestrictions(String impRestrictions) {
        this.impRestrictions = impRestrictions;
    }
    public String getAdditionalRestrictions() {
        return this.additionalRestrictions;
    }
    
    public void setAdditionalRestrictions(String additionalRestrictions) {
        this.additionalRestrictions = additionalRestrictions;
    }
    public String getSoldQuantity() {
        return this.soldQuantity;
    }
    
    public void setSoldQuantity(String soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
    public Boolean getShippingAddressRequired() {
        return this.shippingAddressRequired;
    }
    
    public void setShippingAddressRequired(Boolean shippingAddressRequired) {
        this.shippingAddressRequired = shippingAddressRequired;
    }
    public String getHighlightsHtml() {
        return this.highlightsHtml;
    }
    
    public void setHighlightsHtml(String highlightsHtml) {
        this.highlightsHtml = highlightsHtml;
    }
    public String getPitchHtml() {
        return this.pitchHtml;
    }
    
    public void setPitchHtml(String pitchHtml) {
        this.pitchHtml = pitchHtml;
    }
    public String getDealType() {
        return this.dealType;
    }
    
    public void setDealType(String dealType) {
        this.dealType = dealType;
    }
    public Date getStartAt() {
        return this.startAt;
    }
    
    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }
    public Date getEndAt() {
        return this.endAt;
    }
    
    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }
    public Boolean getIsNowDeal() {
        return this.isNowDeal;
    }
    
    public void setIsNowDeal(Boolean isNowDeal) {
        this.isNowDeal = isNowDeal;
    }
    public Date getTippedAt() {
        return this.tippedAt;
    }
    
    public void setTippedAt(Date tippedAt) {
        this.tippedAt = tippedAt;
    }
    public String getDealSource() {
        return this.dealSource;
    }
    
    public void setDealSource(String dealSource) {
        this.dealSource = dealSource;
    }
    public Set getDealsusages() {
        return this.dealsusages;
    }
    
    public void setDealsusages(Set dealsusages) {
        this.dealsusages = dealsusages;
    }
    public Set getDealoptions() {
        return this.dealoptions;
    }
    
    public void setDealoptions(Set dealoptions) {
        this.dealoptions = dealoptions;
    }


	public Set getUserdealmaps() {
		return userdealmaps;
	}


	public void setUserdealmaps(Set userdealmaps) {
		this.userdealmaps = userdealmaps;
	}


	public void setUserdealssuggestions(Set userdealssuggestions) {
		this.userdealssuggestions = userdealssuggestions;
	}


	public Set getUserdealssuggestions() {
		return userdealssuggestions;
	}


	/**
	 * @param topdealssuggestions the topdealssuggestions to set
	 */
	public void setTopdealssuggestions(Set topdealssuggestions) {
		this.topdealssuggestions = topdealssuggestions;
	}


	/**
	 * @return the topdealssuggestions
	 */
	public Set getTopdealssuggestions() {
		return topdealssuggestions;
	}

   


}


