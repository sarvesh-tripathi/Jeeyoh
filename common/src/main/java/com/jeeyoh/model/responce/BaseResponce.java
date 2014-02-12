package com.jeeyoh.model.responce;

import java.io.Serializable;

public class BaseResponce implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status ;
	private String error ;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	

}
