package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA



/**
 * Goptiondetail generated by hbm2java
 */
public class Goptiondetail  implements java.io.Serializable {


     private Integer id;
     private Gdealoption gdealoption;
     private String description;

    public Goptiondetail() {
    }

	
    public Goptiondetail(Gdealoption gdealoption) {
        this.gdealoption = gdealoption;
    }
    public Goptiondetail(Gdealoption gdealoption, String description) {
       this.gdealoption = gdealoption;
       this.description = description;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Gdealoption getGdealoption() {
        return this.gdealoption;
    }
    
    public void setGdealoption(Gdealoption gdealoption) {
        this.gdealoption = gdealoption;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }




}


