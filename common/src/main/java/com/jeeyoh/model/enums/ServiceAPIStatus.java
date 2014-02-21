package com.jeeyoh.model.enums;

public enum ServiceAPIStatus {
	
	OK("OK"),
    FAILED("FAILED"),
    Failed("Failed"),
    FAILURE("Failure"),
	PASSWORD("PASSWORD"),
	EMAIL("EMAIL"),
	INACTIVE("INACTIVE"),
	EMAILEXIST("EMAILEXIST");
	
	private String status;
	
	private ServiceAPIStatus(String status)
	{
		this.status = status;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	

}
