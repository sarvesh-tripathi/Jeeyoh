package com.jeeyoh.persistence.domain;

public class Gcategory   implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String category;
	private Gcategory gcategory;
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param gcategory the gcategory to set
	 */
	public void setGcategory(Gcategory gcategory) {
		this.gcategory = gcategory;
	}
	/**
	 * @return the gcategory
	 */
	public Gcategory getGcategory() {
		return gcategory;
	}
	

}
