package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Yregion generated by hbm2java
 */
public class Yregion  implements java.io.Serializable {


     private Integer id;
     private Double lattitude;
     private Double longitude;
     private Double lattitudeDelta;
     private Double longitudeDelta;
     private Set ybusinessregionmaps = new HashSet(0);

    public Yregion() {
    }

    public Yregion(Double lattitude, Double longitude, Double lattitudeDelta, Double longitudeDelta, Set ybusinessregionmaps) {
       this.lattitude = lattitude;
       this.longitude = longitude;
       this.lattitudeDelta = lattitudeDelta;
       this.longitudeDelta = longitudeDelta;
       this.ybusinessregionmaps = ybusinessregionmaps;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Double getLattitude() {
        return this.lattitude;
    }
    
    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }
    public Double getLongitude() {
        return this.longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLattitudeDelta() {
        return this.lattitudeDelta;
    }
    
    public void setLattitudeDelta(Double lattitudeDelta) {
        this.lattitudeDelta = lattitudeDelta;
    }
    public Double getLongitudeDelta() {
        return this.longitudeDelta;
    }
    
    public void setLongitudeDelta(Double longitudeDelta) {
        this.longitudeDelta = longitudeDelta;
    }
    public Set getYbusinessregionmaps() {
        return this.ybusinessregionmaps;
    }
    
    public void setYbusinessregionmaps(Set ybusinessregionmaps) {
        this.ybusinessregionmaps = ybusinessregionmaps;
    }




}


