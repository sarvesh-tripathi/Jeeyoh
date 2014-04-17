/**
 * 
 */
package com.jeeyoh.notification.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import com.jeeyoh.notification.JeeyohMessage;
import com.jeeyoh.notification.JeeyohMessageType;

/**
 * @author Sarvesh
 * 
 */
public abstract class JeeyohMessageSubscriber implements ApplicationListener<JeeyohMessage>,
        IJeeyohMessageSubscriber
{
    private boolean all = false;
    private HashMap<JeeyohMessageType, Object> registeredMessageTypesMap = null;

    private Logger logger = LoggerFactory.getLogger(JeeyohMessageSubscriber.class);

    /*
     * (non-Javadoc)
     * 
     * @seecom.storistic.notification.service.IStoristicMessageSubscriber#
     * getRegisteredMessageTypes()
     */
    @Override
    public abstract JeeyohMessageType[] getRegisteredMessageTypes();

    /*
     * (non-Javadoc)
     * 
     * @seecom.storistic.notification.service.IStoristicMessageSubscriber#
     * onStoristicMessage(com.storistic.notification.StoristicMessage)
     */
    @Override
    public abstract void onJeeyohMessage(JeeyohMessage message);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationListener#onApplicationEvent(org
     * .springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(JeeyohMessage event)
    {
        logger.debug("StoristicMessageSubscriber ==> onApplicationEvent ==>");
        if (registeredMessageTypesMap == null)
        {
            buildRegisteredMessageMap();
        }

        // call derived sink (subscriber) based on the subscribers
        // registration of interested message types
        if (this.all)
        {
        	onJeeyohMessage(event);
        }
        else if (registeredMessageTypesMap.containsKey(event.getMessageType()))
        {
        	onJeeyohMessage(event);
        }
    }

    private void buildRegisteredMessageMap()
    {
        registeredMessageTypesMap = new HashMap<JeeyohMessageType, Object>();

        JeeyohMessageType[] types = getRegisteredMessageTypes();
        if (types != null)
        {
            for (JeeyohMessageType type : types)
            {
                if (type == JeeyohMessageType.ALL)
                {
                    this.all = true;
                }
                registeredMessageTypesMap.put(type, null);
            }
        }
    }

}
