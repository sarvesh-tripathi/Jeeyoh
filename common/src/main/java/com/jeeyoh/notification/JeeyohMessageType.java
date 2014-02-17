/**
 * 
 */
package com.jeeyoh.notification;

/**
 * @author Sarvesh
 *
 */
public enum JeeyohMessageType
{
    ALL("ALL"),
    WELCOME_EMAIL("WELCOME_EMAIL"),
    CHANGE_PASSWORD("CHANGE_PASSWORD"),
    RESET_PASSWORD("RESET_PASSWORD"),
    SHARE_STORYLINE("SHARE_STORYLINE"),
    INVITE_FRIEND("INVITE_FRIEND"),
    REGISTRATION_CONFIRMATION("REGISTRATION_CONFIRMATION"),
    SHARE_MOMENT("SHARE_MOMENT"),
    ADD_CONTRIBUTOR("ADD_CONTRIBUTOR"),
    ADD_VIEWER("ADD_VIEWER");
    
    private String jeeyohMessageType;
    
    private JeeyohMessageType(String jeeyohMessageType)
    {
        this.jeeyohMessageType = jeeyohMessageType;
    }

    public String getJeeyohMessageType()
    {
        return jeeyohMessageType;
    }

}
