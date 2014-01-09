package com.jeeyoh.model.search;

public class DealModel {
	
	private String title;
	private String dealUrl;
	private String status;
	private String startAt;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDealUrl() {
		return dealUrl;
	}
	public void setDealUrl(String dealUrl) {
		this.dealUrl = dealUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartAt() {
		return startAt;
	}
	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}
	public String getEndAt() {
		return endAt;
	}
	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}
	private String endAt;

}
