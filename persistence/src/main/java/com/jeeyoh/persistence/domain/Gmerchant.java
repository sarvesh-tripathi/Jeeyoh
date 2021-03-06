package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Gmerchant generated by hbm2java
 */
public class Gmerchant  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
     private String merchantId;
     private String name;
     private String websiteUrl;
     private Gmerchantrating gmerchantByRatingId;
     private Set gdeals = new HashSet(0);

    public Gmerchant() {
    }

	
    public Gmerchant(String merchantId) {
        this.merchantId = merchantId;
    }
    public Gmerchant(String merchantId, String name, String websiteUrl, Set gdeals) {
       this.merchantId = merchantId;
       this.name = name;
       this.websiteUrl = websiteUrl;
       this.gdeals = gdeals;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMerchantId() {
        return this.merchantId;
    }
    
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getWebsiteUrl() {
        return this.websiteUrl;
    }
    
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
    public Set getGdeals() {
        return this.gdeals;
    }
    
    public void setGdeals(Set gdeals) {
        this.gdeals = gdeals;
    }


	public void setGmerchantByRatingId(Gmerchantrating gmerchantByRatingId) {
		this.gmerchantByRatingId = gmerchantByRatingId;
	}


	public Gmerchantrating getGmerchantByRatingId() {
		return gmerchantByRatingId;
	}






}


