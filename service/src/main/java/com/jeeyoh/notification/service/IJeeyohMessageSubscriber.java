/**
 * 
 */
package com.jeeyoh.notification.service;

import com.jeeyoh.notification.JeeyohMessage;
import com.jeeyoh.notification.JeeyohMessageType;

/**
 * @author Sarvesh
 *
 */
public interface IJeeyohMessageSubscriber
{
    /**
     * Derived implementations must return the message types they are interested
     * in.
     * 
     * @return
     */
	JeeyohMessageType[] getRegisteredMessageTypes();
    
    /**
     * Called by messaging framework when an interesting message is published by
     * someone
     *  
     * @param message
     */
    void onJeeyohMessage(JeeyohMessage message);

}
