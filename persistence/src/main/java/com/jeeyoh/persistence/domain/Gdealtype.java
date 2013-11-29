package com.jeeyoh.persistence.domain;
// Generated Nov 29, 2013 2:14:09 PM by Hibernate Tools 3.2.1.GA



/**
 * Gdealtype generated by hbm2java
 */
public class Gdealtype  implements java.io.Serializable {


     private Integer id;
     private Gdeal gdeal;
     private String dealtypeId;
     private String name;
     private String description;

    public Gdealtype() {
    }

	
    public Gdealtype(Gdeal gdeal, String dealtypeId, String description) {
        this.gdeal = gdeal;
        this.dealtypeId = dealtypeId;
        this.description = description;
    }
    public Gdealtype(Gdeal gdeal, String dealtypeId, String name, String description) {
       this.gdeal = gdeal;
       this.dealtypeId = dealtypeId;
       this.name = name;
       this.description = description;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Gdeal getGdeal() {
        return this.gdeal;
    }
    
    public void setGdeal(Gdeal gdeal) {
        this.gdeal = gdeal;
    }
    public String getDealtypeId() {
        return this.dealtypeId;
    }
    
    public void setDealtypeId(String dealtypeId) {
        this.dealtypeId = dealtypeId;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

}


