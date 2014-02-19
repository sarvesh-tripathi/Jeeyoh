package com.jeeyoh.model.response;

import java.util.List;

import com.jeeyoh.model.search.PageModel;



public class CategoryResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<PageModel> pageMode;

	/**
	 * @param pageMode the pageMode to set
	 */
	public void setPageMode(List<PageModel> pageMode) {
		this.pageMode = pageMode;
	}

	/**
	 * @return the pageMode
	 */
	public List<PageModel> getPageMode() {
		return pageMode;
	}

	
	
	

}
