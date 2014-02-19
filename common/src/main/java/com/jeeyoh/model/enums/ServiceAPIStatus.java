package com.jeeyoh.model.enums;

public enum ServiceAPIStatus {
	
	OK("OK"),
    FAILED("FAILED"),
    Failed("Failed"),
    FAILURE("Failure");
	
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
