package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA



/**
 * Countrylocation generated by hbm2java
 */
public class Countrylocation  implements java.io.Serializable {


     private Integer countryLocationId;
     private String city;
     private String state;
     private String stateCode;
     private String country;
     private String countryCode;

    public Countrylocation() {
    }

    public Countrylocation(String city, String state, String stateCode, String country, String countryCode) {
       this.city = city;
       this.state = state;
       this.stateCode = stateCode;
       this.country = country;
       this.countryCode = countryCode;
    }
   
    public Integer getCountryLocationId() {
        return this.countryLocationId;
    }
    
    public void setCountryLocationId(Integer countryLocationId) {
        this.countryLocationId = countryLocationId;
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
    public String getStateCode() {
        return this.stateCode;
    }
    
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
    public String getCountry() {
        return this.country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountryCode() {
        return this.countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }




}


