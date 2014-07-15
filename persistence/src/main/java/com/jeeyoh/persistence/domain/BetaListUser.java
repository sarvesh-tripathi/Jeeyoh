package com.jeeyoh.persistence.domain;

import java.util.Date;

public class BetaListUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String emailId;
    private String firstName;
    private String lastName;
    private Boolean isConfirmed;
    private String confirmationId;
    private Date createdTime;
    private Date updatedTime;
    private Date confirmationTime;
    
    public BetaListUser() {
    }

	
    public BetaListUser(String emailId, Date createdTime, Date updatedTime) {
    	
        this.emailId = emailId;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }
    public BetaListUser(String emailId, String firstName, String lastName, Boolean isConfirmed, String confirmationId, String street, String city, String state, String country, String zipcode, String longitude, String lattitude, Date createdTime, Date updatedTime, Date confirmationTime) {
      
       this.emailId = emailId;
       this.firstName = firstName;
       this.lastName = lastName;
       this.confirmationId = confirmationId;
       this.isConfirmed = isConfirmed;
       this.createdTime = createdTime;
       this.updatedTime = updatedTime;
       this.setConfirmationTime(confirmationTime);
    }
   

    public String getEmailId() {
        return this.emailId;
    }
    
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
   
    public String getFirstName() {
        return this.firstName;
    }
  
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
   
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
   
    public Boolean getIsConfirmed() {
        return this.isConfirmed;
    }
    
    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
    public String getConfirmationId() {
        return this.confirmationId;
    }
    
    public void setConfirmationId(String confirmationId) {
        this.confirmationId = confirmationId;
    }
   
    public Date getCreatedTime() {
        return this.createdTime;
    }
    
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public Date getUpdatedTime() {
        return this.updatedTime;
    }
    
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

	/**
	 * @return the confirmationTime
	 */
	public Date getConfirmationTime() {
		return confirmationTime;
	}


	/**
	 * @param confirmationTime the confirmationTime to set
	 */
	public void setConfirmationTime(Date confirmationTime) {
		this.confirmationTime = confirmationTime;
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}
