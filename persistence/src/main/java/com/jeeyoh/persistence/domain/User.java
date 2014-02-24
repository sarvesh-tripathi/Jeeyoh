package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User generated by hbm2java
 */
public class User  implements java.io.Serializable {


     private Integer userId;
     private Profiletype profiletype;
     private Privacy privacy;
     private String emailId;
     private String password;
     private String firstName;
     private String middleName;
     private String lastName;
     private Integer birthDate;
     private Integer birthMonth;
     private Integer birthYear;
     private Character gender;
     private Boolean isActive;
     private Boolean isDeleted;
     private String addressline1;
     private String street;
     private String city;
     private String state;
     private String country;
     private String zipcode;
     private String longitude;
     private String lattitude;
     private String confirmationId;
     private String sessionId;
     private Date createdtime;
     private Date updatedtime;
     private Set jeeyohgroupsForOwnerId = new HashSet(0);
     private Set dealsusages = new HashSet(0);
     private Set pagesForCreatorId = new HashSet(0);
     private Set pageuserlikeses = new HashSet(0);
     private Set groupusermaps = new HashSet(0);
     private Set usercontactsesForUserId = new HashSet(0);
     private Set pagesForOwnerId = new HashSet(0);
     private Set jeeyohgroupsForCreatorId = new HashSet(0);
     private Set usercontactsesForContactId = new HashSet(0);
     private Set userdealmaps = new HashSet(0);
     private Set usernondealsuggestions = new HashSet(0);
     private Set usereventsuggestions = new HashSet(0);
     private Set userdealssuggestions = new HashSet(0);
     private Set topdealssuggestions = new HashSet(0);
     private Set topnondealsuggestions = new HashSet(0);
     private Set topeventsuggestions = new HashSet(0);

    public User() {
    }

	
    public User(Profiletype profiletype, Privacy privacy, String emailId, String password, String zipcode, Date createdtime, Date updatedtime) {
        this.profiletype = profiletype;
        this.privacy = privacy;
        this.emailId = emailId;
        this.password = password;
        this.zipcode = zipcode;
        this.createdtime = createdtime;
        this.updatedtime = updatedtime;
    }
    public User(Profiletype profiletype, Privacy privacy, String emailId, String password, String firstName, String middleName, String lastName, Integer birthDate, Integer birthMonth, Integer birthYear, Character gender, Boolean isActive, Boolean isDeleted, String addressline1, String street, String city, String state, String country, String zipcode, String longitude, String lattitude, Date createdtime, Date updatedtime, Set jeeyohgroupsForOwnerId, Set dealsusages, Set pagesForCreatorId, Set pageuserlikeses, Set groupusermaps, Set usercontactsesForUserId, Set pagesForOwnerId, Set jeeyohgroupsForCreatorId, Set usercontactsesForContactId) {
       this.profiletype = profiletype;
       this.privacy = privacy;
       this.emailId = emailId;
       this.password = password;
       this.firstName = firstName;
       this.middleName = middleName;
       this.lastName = lastName;
       this.birthDate = birthDate;
       this.birthMonth = birthMonth;
       this.birthYear = birthYear;
       this.gender = gender;
       this.isActive = isActive;
       this.isDeleted = isDeleted;
       this.addressline1 = addressline1;
       this.street = street;
       this.city = city;
       this.state = state;
       this.country = country;
       this.zipcode = zipcode;
       this.longitude = longitude;
       this.lattitude = lattitude;
       this.createdtime = createdtime;
       this.updatedtime = updatedtime;
       this.jeeyohgroupsForOwnerId = jeeyohgroupsForOwnerId;
       this.dealsusages = dealsusages;
       this.pagesForCreatorId = pagesForCreatorId;
       this.pageuserlikeses = pageuserlikeses;
       this.groupusermaps = groupusermaps;
       this.usercontactsesForUserId = usercontactsesForUserId;
       this.pagesForOwnerId = pagesForOwnerId;
       this.jeeyohgroupsForCreatorId = jeeyohgroupsForCreatorId;
       this.usercontactsesForContactId = usercontactsesForContactId;
    }
   
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Profiletype getProfiletype() {
        return this.profiletype;
    }
    
    public void setProfiletype(Profiletype profiletype) {
        this.profiletype = profiletype;
    }
    public Privacy getPrivacy() {
        return this.privacy;
    }
    
    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }
    public String getEmailId() {
        return this.emailId;
    }
    
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleName() {
        return this.middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Integer getBirthDate() {
        return this.birthDate;
    }
    
    public void setBirthDate(Integer birthDate) {
        this.birthDate = birthDate;
    }
    public Integer getBirthMonth() {
        return this.birthMonth;
    }
    
    public void setBirthMonth(Integer birthMonth) {
        this.birthMonth = birthMonth;
    }
    public Integer getBirthYear() {
        return this.birthYear;
    }
    
    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
    public Character getGender() {
        return this.gender;
    }
    
    public void setGender(Character gender) {
        this.gender = gender;
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
    public String getAddressline1() {
        return this.addressline1;
    }
    
    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }
    public String getStreet() {
        return this.street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return this.state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    public String getCountry() {
        return this.country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    public String getZipcode() {
        return this.zipcode;
    }
    
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    public String getLongitude() {
        return this.longitude;
    }
    
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLattitude() {
        return this.lattitude;
    }
    
    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
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
    public Set getJeeyohgroupsForOwnerId() {
        return this.jeeyohgroupsForOwnerId;
    }
    
    public void setJeeyohgroupsForOwnerId(Set jeeyohgroupsForOwnerId) {
        this.jeeyohgroupsForOwnerId = jeeyohgroupsForOwnerId;
    }
    public Set getDealsusages() {
        return this.dealsusages;
    }
    
    public void setDealsusages(Set dealsusages) {
        this.dealsusages = dealsusages;
    }
    public Set getPagesForCreatorId() {
        return this.pagesForCreatorId;
    }
    
    public void setPagesForCreatorId(Set pagesForCreatorId) {
        this.pagesForCreatorId = pagesForCreatorId;
    }
    public Set getPageuserlikeses() {
        return this.pageuserlikeses;
    }
    
    public void setPageuserlikeses(Set pageuserlikeses) {
        this.pageuserlikeses = pageuserlikeses;
    }
    public Set getGroupusermaps() {
        return this.groupusermaps;
    }
    
    public void setGroupusermaps(Set groupusermaps) {
        this.groupusermaps = groupusermaps;
    }
    public Set getUsercontactsesForUserId() {
        return this.usercontactsesForUserId;
    }
    
    public void setUsercontactsesForUserId(Set usercontactsesForUserId) {
        this.usercontactsesForUserId = usercontactsesForUserId;
    }
    public Set getPagesForOwnerId() {
        return this.pagesForOwnerId;
    }
    
    public void setPagesForOwnerId(Set pagesForOwnerId) {
        this.pagesForOwnerId = pagesForOwnerId;
    }
    public Set getJeeyohgroupsForCreatorId() {
        return this.jeeyohgroupsForCreatorId;
    }
    
    public void setJeeyohgroupsForCreatorId(Set jeeyohgroupsForCreatorId) {
        this.jeeyohgroupsForCreatorId = jeeyohgroupsForCreatorId;
    }
    public Set getUsercontactsesForContactId() {
        return this.usercontactsesForContactId;
    }
    
    public void setUsercontactsesForContactId(Set usercontactsesForContactId) {
        this.usercontactsesForContactId = usercontactsesForContactId;
    }


	public Set getUserdealmaps() {
		return userdealmaps;
	}


	public void setUserdealmaps(Set userdealmaps) {
		this.userdealmaps = userdealmaps;
	}


	public Set getUsernondealsuggestions() {
		return usernondealsuggestions;
	}


	public void setUsernondealsuggestions(Set usernondealsuggestions) {
		this.usernondealsuggestions = usernondealsuggestions;
	}


	/**
	 * @param usereventsuggestions the usereventsuggestions to set
	 */
	public void setUsereventsuggestions(Set usereventsuggestions) {
		this.usereventsuggestions = usereventsuggestions;
	}


	/**
	 * @return the usereventsuggestions
	 */
	public Set getUsereventsuggestions() {
		return usereventsuggestions;
	}

	/**
	 * @param userdealssuggestions the userdealssuggestions to set
	 */
	public void setUserdealssuggestions(Set userdealssuggestions) {
		this.userdealssuggestions = userdealssuggestions;
	}


	/**
	 * @return the userdealssuggestions
	 */
	public Set getUserdealssuggestions() {
		return userdealssuggestions;
	}


	/**
	 * @param confirmationId the confirmationId to set
	 */
	public void setConfirmationId(String confirmationId) {
		this.confirmationId = confirmationId;
	}



	/**
	 * @return the confirmationId
	 */
	public String getConfirmationId() {
		return confirmationId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public String getSessionId() {
		return sessionId;
	}


	/**
	 * @return the topdealssuggestions
	 */
	public Set getTopdealssuggestions() {
		return topdealssuggestions;
	}


	/**
	 * @param topdealssuggestions the topdealssuggestions to set
	 */
	public void setTopdealssuggestions(Set topdealssuggestions) {
		this.topdealssuggestions = topdealssuggestions;
	}


	/**
	 * @return the topnondealsuggestions
	 */
	public Set getTopnondealsuggestions() {
		return topnondealsuggestions;
	}


	/**
	 * @param topnondealsuggestions the topnondealsuggestions to set
	 */
	public void setTopnondealsuggestions(Set topnondealsuggestions) {
		this.topnondealsuggestions = topnondealsuggestions;
	}


	/**
	 * @return the topeventsuggestions
	 */
	public Set getTopeventsuggestions() {
		return topeventsuggestions;
	}


	/**
	 * @param topeventsuggestions the topeventsuggestions to set
	 */
	public void setTopeventsuggestions(Set topeventsuggestions) {
		this.topeventsuggestions = topeventsuggestions;
	}




}


