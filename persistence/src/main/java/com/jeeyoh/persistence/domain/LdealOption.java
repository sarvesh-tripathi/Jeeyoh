package com.jeeyoh.persistence.domain;

public class LdealOption implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer ldealOptionId;
	private Ldeal ldeal;
	private String description;
	private Long price;
	private Long originalPrice;
	private Boolean isSoldOut;
	private Long savings;
	private Boolean monthlyCapReached;
	private Integer discount;
	
	public LdealOption(){}
	public LdealOption(Ldeal ldeal,String description,Long price,Long originalPrice,Boolean isSoldOut,Long savings,Boolean monthlyCapReached,Integer discount){
		this.ldeal=ldeal;
		this.description=description;
		this.price=price;
		this.originalPrice=originalPrice;
		this.isSoldOut=isSoldOut;
		this.savings=savings;
		this.monthlyCapReached=monthlyCapReached;
		this.discount=discount;
	}
	public Integer getLdealOptionId() {
		return ldealOptionId;
	}
	public void setLdealOptionId(Integer ldealOptionId) {
		this.ldealOptionId = ldealOptionId;
	}
	public Ldeal getLdeal() {
		return ldeal;
	}
	public void setLdeal(Ldeal ldeal) {
		this.ldeal = ldeal;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Long getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(Long originalPrice) {
		this.originalPrice = originalPrice;
	}
	public Boolean getIsSoldOut() {
		return isSoldOut;
	}
	public void setIsSoldOut(Boolean isSoldOut) {
		this.isSoldOut = isSoldOut;
	}
	public Long getSavings() {
		return savings;
	}
	public void setSavings(Long savings) {
		this.savings = savings;
	}
	public Boolean getMonthlyCapReached() {
		return monthlyCapReached;
	}
	public void setMonthlyCapReached(Boolean monthlyCapReached) {
		this.monthlyCapReached = monthlyCapReached;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	

}
