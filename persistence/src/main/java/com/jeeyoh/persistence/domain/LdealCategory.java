package com.jeeyoh.persistence.domain;

public class LdealCategory implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer ldealCatgoryId;
	private Ldeal ldeal;
	private String categoryName;
	
	public LdealCategory(){}
	public LdealCategory(Ldeal ldeal,String categoryName){
		this.ldeal=ldeal;
		this.categoryName=categoryName;
	}
	
	public Integer getLdealCatgoryId() {
		return ldealCatgoryId;
	}
	public void setLdealCatgoryId(Integer ldealCatgoryId) {
		this.ldealCatgoryId = ldealCatgoryId;
	}
	public Ldeal getLdeal() {
		return ldeal;
	}
	public void setLdeal(Ldeal ldeal) {
		this.ldeal = ldeal;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	

}
