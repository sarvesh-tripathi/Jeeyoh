package com.jeeyoh.persistence.domain;

public class Lcategory implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String category;
	private Lcategory lcategory;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Lcategory getLcategory() {
		return lcategory;
	}
	public void setLcategory(Lcategory lcategory) {
		this.lcategory = lcategory;
	}
}
