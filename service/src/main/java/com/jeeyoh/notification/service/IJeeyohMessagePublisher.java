/**
 * 
 */
package com.jeeyoh.notification.service;

import java.util.HashMap;

import com.jeeyoh.notification.JeeyohMessage;
import com.jeeyoh.notification.JeeyohMessageType;


/**
 * @author Sarvesh
 *
 */
public interface IJeeyohMessagePublisher
{
    public static final String INSTANCE_DEPRECATED = "DEPRECATED";
    public static final String INSTANCE_SINGLETON = "SINGLETON";
    public static final String INSTANCE_NO_REF = "NO_REF";
    
    void publish(Object source, JeeyohMessageType messageType, String uniqueMessageInstanceIdentifier,
            HashMap<String, Object> messageArgumentsMap);
    
    void publish(JeeyohMessage message);

}
