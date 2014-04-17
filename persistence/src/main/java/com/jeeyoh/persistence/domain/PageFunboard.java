package com.jeeyoh.persistence.domain;

import java.io.Serializable;

public class PageFunboard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Page page;
	private String category;
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
	 * @param page the page to set
	 */
	public void setPage(Page page) {
		this.page = page;
	}
	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}
	
	public Boolean getIsEvent() {
        return false;
    }
	
	public void setIsEvent(Boolean isEvent) {
        
    }
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

}
