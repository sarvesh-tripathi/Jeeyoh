package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Cuisines generated by hbm2java
 */
public class Cuisines  implements java.io.Serializable {


     private Integer cuisineId;
     private String cuisine;
     private String description;
     private Set cuisinecategories = new HashSet(0);
     private Set foodmenuitems = new HashSet(0);

    public Cuisines() {
    }

    public Cuisines(String cuisine, String description, Set cuisinecategories, Set foodmenuitems) {
       this.cuisine = cuisine;
       this.description = description;
       this.cuisinecategories = cuisinecategories;
       this.foodmenuitems = foodmenuitems;
    }
   
    public Integer getCuisineId() {
        return this.cuisineId;
    }
    
    public void setCuisineId(Integer cuisineId) {
        this.cuisineId = cuisineId;
    }
    public String getCuisine() {
        return this.cuisine;
    }
    
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Set getCuisinecategories() {
        return this.cuisinecategories;
    }
    
    public void setCuisinecategories(Set cuisinecategories) {
        this.cuisinecategories = cuisinecategories;
    }
    public Set getFoodmenuitems() {
        return this.foodmenuitems;
    }
    
    public void setFoodmenuitems(Set foodmenuitems) {
        this.foodmenuitems = foodmenuitems;
    }




}


