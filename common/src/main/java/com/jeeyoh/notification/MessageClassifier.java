/**
 * 
 */
package com.jeeyoh.notification;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Sarvesh
 *
 */
public class MessageClassifier
{
    int priority;
    int messageGroup;
    int messageId;
    int messageVersion;
    ArrayList<String> forcedParamMap;
    ArrayList<JeeyohMessageType> notifierMap;
    ArrayList<String> optionalParamMap;
    HashMap<String,String> vmMap;
    
    public int getPriority() 
    {
        return priority;
    }
    public void setPriority(int priority) 
    {
        this.priority = priority;
    }
    public int getMessageGroup() 
    {
        return messageGroup;
    }
    public void setMessageGroup(int messageGroup) 
    {
        this.messageGroup = messageGroup;
    }
    
    public int getMessageId() 
    {
        return messageId;
    }
    public void setMessageId(int messageId) 
    {
        this.messageId = messageId;
    }
    public int getMessageVersion()
    {
        return messageVersion;
    }
    public void setMessageVersion(int messageVersion)
    {
        this.messageVersion = messageVersion;
    }
    public ArrayList<String> getForcedParamMap() 
    {
        return forcedParamMap;
    }
    public void setForcedParamMap(ArrayList<String> forcedParamMap) 
    {
        this.forcedParamMap = forcedParamMap;
    }
    public ArrayList<JeeyohMessageType> getNotifierMap() 
    {
        return notifierMap;
    }
    public void setNotifierMap(ArrayList<JeeyohMessageType> notifierMap) 
    {
        this.notifierMap = notifierMap;
    }
    public ArrayList<String> getOptionalParamMap() 
    {
        return optionalParamMap;
    }
    public void setOptionalParamMap(ArrayList<String> optionalParamMap) 
    {
        this.optionalParamMap = optionalParamMap;
    }
    public HashMap<String, String> getVmMap() 
    {
        return vmMap;
    }
    public void setVmMap(HashMap<String, String> vmMap) 
    {
        this.vmMap = vmMap;
    }

}
