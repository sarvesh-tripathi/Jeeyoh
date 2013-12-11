package com.jeeyoh.persistence.domain;

public class Gcategory   implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String category;
	private String parentCategoryId;
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
	 * @param parentCategoryId the parentCategoryId to set
	 */
	public void setParentCategoryId(String parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
	/**
	 * @return the parentCategoryId
	 */
	public String getParentCategoryId() {
		return parentCategoryId;
	}

}
