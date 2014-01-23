package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Usercontacts generated by hbm2java
 */
public class Usercontacts  implements java.io.Serializable {


     private Integer userContactId;
     private User userByContactId;
     private User userByUserId;
     private Boolean isActive;
     private Boolean isDeleted;
     private Boolean isStar;
     private Date createdtime;
     private Date updatedtime;
     private Set usercontactsgroupmaps = new HashSet(0);
     


	public Usercontacts() {
    }

	
    public Usercontacts(User userByContactId, User userByUserId, Date createdtime, Date updatedtime) {
        this.userByContactId = userByContactId;
        this.userByUserId = userByUserId;
        this.createdtime = createdtime;
        this.updatedtime = updatedtime;
    }
    public Usercontacts(User userByContactId, User userByUserId, Boolean isActive, Boolean isDeleted, Date createdtime, Date updatedtime, Set usercontactsgroupmaps) {
       this.userByContactId = userByContactId;
       this.userByUserId = userByUserId;
       this.isActive = isActive;
       this.isDeleted = isDeleted;
       this.createdtime = createdtime;
       this.updatedtime = updatedtime;
       this.usercontactsgroupmaps = usercontactsgroupmaps;
    }
   
    public Integer getUserContactId() {
        return this.userContactId;
    }
    
    public void setUserContactId(Integer userContactId) {
        this.userContactId = userContactId;
    }
    public User getUserByContactId() {
        return this.userByContactId;
    }
    
    public void setUserByContactId(User userByContactId) {
        this.userByContactId = userByContactId;
    }
    public User getUserByUserId() {
        return this.userByUserId;
    }
    
    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }
    public Boolean getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public Boolean getIsDeleted() {
        return this.isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public Date getCreatedtime() {
        return this.createdtime;
    }
    
    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }
    public Date getUpdatedtime() {
        return this.updatedtime;
    }
    
    public void setUpdatedtime(Date updatedtime) {
        this.updatedtime = updatedtime;
    }
    public Set getUsercontactsgroupmaps() {
        return this.usercontactsgroupmaps;
    }
    
    public void setUsercontactsgroupmaps(Set usercontactsgroupmaps) {
        this.usercontactsgroupmaps = usercontactsgroupmaps;
    }


	public void setIsStar(Boolean isStar) {
		this.isStar = isStar;
	}


	public Boolean getIsStar() {
		return isStar;
	}




}


