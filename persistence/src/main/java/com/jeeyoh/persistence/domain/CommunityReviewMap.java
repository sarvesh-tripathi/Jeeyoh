package com.jeeyoh.persistence.domain;

import java.util.Date;

public class CommunityReviewMap  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer communityReviewMapId;
	private CommunityReview communityReview;
	private Page page;
	private Date createdTime;
	private Date updatedTime;
	public Integer getCommunityReviewMapId() {
		return communityReviewMapId;
	}
	public void setCommunityReviewMapId(Integer communityReviewMapId) {
		this.communityReviewMapId = communityReviewMapId;
	}
	public CommunityReview getCommunityReview() {
		return communityReview;
	}
	public void setCommunityReview(CommunityReview communityReview) {
		this.communityReview = communityReview;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
}
