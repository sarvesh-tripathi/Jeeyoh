package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Groupusermap generated by hbm2java
 */
public class Groupusermap  implements java.io.Serializable {


     private Integer groupUserMapId;
     private Jeeyohgroup jeeyohgroup;
     private User user;
     private Notificationpermission notificationpermission;
     private Boolean isAdmin;
     private Date createdtime;
     private Date updatedtime;

    public Groupusermap() {
    }

	
    public Groupusermap(Jeeyohgroup jeeyohgroup, User user, Notificationpermission notificationpermission, Date createdtime, Date updatedtime) {
        this.jeeyohgroup = jeeyohgroup;
        this.user = user;
        this.notificationpermission = notificationpermission;
        this.createdtime = createdtime;
        this.updatedtime = updatedtime;
    }
    public Groupusermap(Jeeyohgroup jeeyohgroup, User user, Notificationpermission notificationpermission, Boolean isAdmin, Date createdtime, Date updatedtime) {
       this.jeeyohgroup = jeeyohgroup;
       this.user = user;
       this.notificationpermission = notificationpermission;
       this.isAdmin = isAdmin;
       this.createdtime = createdtime;
       this.updatedtime = updatedtime;
    }
   
    public Integer getGroupUserMapId() {
        return this.groupUserMapId;
    }
    
    public void setGroupUserMapId(Integer groupUserMapId) {
        this.groupUserMapId = groupUserMapId;
    }
    public Jeeyohgroup getJeeyohgroup() {
        return this.jeeyohgroup;
    }
    
    public void setJeeyohgroup(Jeeyohgroup jeeyohgroup) {
        this.jeeyohgroup = jeeyohgroup;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    public Notificationpermission getNotificationpermission() {
        return this.notificationpermission;
    }
    
    public void setNotificationpermission(Notificationpermission notificationpermission) {
        this.notificationpermission = notificationpermission;
    }
    public Boolean getIsAdmin() {
        return this.isAdmin;
    }
    
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
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




}


