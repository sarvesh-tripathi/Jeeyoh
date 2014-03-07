package com.jeeyoh.persistence.domain;

public class PageFunboard extends Funboard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Page page;
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

}
