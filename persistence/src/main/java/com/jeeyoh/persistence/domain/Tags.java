package com.jeeyoh.persistence.domain;

public class Tags implements java.io.Serializable {


    private Integer id;
    private Deals deal;
    private String name;
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Deals getDeal() {
		return deal;
	}
	public void setDeal(Deals deal) {
		this.deal = deal;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
