package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Notificationpermission generated by hbm2java
 */
public class Notificationpermission  implements java.io.Serializable {


     private Integer notificationPermissionId;
     private String permissionType;
     private Set pageuserlikeses = new HashSet(0);
     private Set groupusermaps = new HashSet(0);

    public Notificationpermission() {
    }

	
    public Notificationpermission(String permissionType) {
        this.permissionType = permissionType;
    }
    public Notificationpermission(String permissionType, Set pageuserlikeses, Set groupusermaps) {
       this.permissionType = permissionType;
       this.pageuserlikeses = pageuserlikeses;
       this.groupusermaps = groupusermaps;
    }
   
    public Integer getNotificationPermissionId() {
        return this.notificationPermissionId;
    }
    
    public void setNotificationPermissionId(Integer notificationPermissionId) {
        this.notificationPermissionId = notificationPermissionId;
    }
    public String getPermissionType() {
        return this.permissionType;
    }
    
    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
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




}


