package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Privacy generated by hbm2java
 */
public class Privacy  implements java.io.Serializable {


     private Integer privacyId;
     private String privacyType;
     private Set jeeyohgroups = new HashSet(0);
     private Set users = new HashSet(0);
     private Set funboards = new HashSet(0);

    public Privacy() {
    }

	
    public Privacy(String privacyType) {
        this.privacyType = privacyType;
    }
    public Privacy(String privacyType, Set jeeyohgroups, Set users,  Set funboards) {
       this.privacyType = privacyType;
       this.jeeyohgroups = jeeyohgroups;
       this.users = users;
       this.funboards = funboards;
    }
   
    public Integer getPrivacyId() {
        return this.privacyId;
    }
    
    public void setPrivacyId(Integer privacyId) {
        this.privacyId = privacyId;
    }
    public String getPrivacyType() {
        return this.privacyType;
    }
    
    public void setPrivacyType(String privacyType) {
        this.privacyType = privacyType;
    }
    public Set getJeeyohgroups() {
        return this.jeeyohgroups;
    }
    
    public void setJeeyohgroups(Set jeeyohgroups) {
        this.jeeyohgroups = jeeyohgroups;
    }
    public Set getUsers() {
        return this.users;
    }
    
    public void setUsers(Set users) {
        this.users = users;
    }


	/**
	 * @return the funboards
	 */
	public Set getFunboards() {
		return funboards;
	}


	/**
	 * @param funboards the funboards to set
	 */
	public void setFunboards(Set funboards) {
		this.funboards = funboards;
	}




}


