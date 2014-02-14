/**
 * 
 */
package com.jeeyoh.notification.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Component;

import com.jeeyoh.notification.JeeyohMessage;
import com.jeeyoh.notification.JeeyohMessageType;


/**
 * @author Sarvesh
 * 
 */
@Component("JeeyohMessagePublisher")
public class JeeyohMessagePublisher implements ApplicationContextAware, IJeeyohMessagePublisher
{

    final static Logger logger = LoggerFactory.getLogger(JeeyohMessagePublisher.class);

    @Autowired
    private ApplicationContext context;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext
     * (org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.storistic.notification.service.IStoristicMessagePublisher#publish
     * (java.lang.Object, com.storistic.userservice.pojo.User,
     * com.storistic.notification.StoristicMessageType, java.lang.String,
     * java.util.HashMap)
     */
    @Override
    public void publish(Object source, JeeyohMessageType messageType,
            String uniqueMessageInstanceIdentifier, HashMap<String, Object> messageArgumentsMap)
    {
        publish(new JeeyohMessage(source, messageType, uniqueMessageInstanceIdentifier, messageArgumentsMap));

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.storistic.notification.service.IStoristicMessagePublisher#publish
     * (com.storistic.notification.StoristicMessage)
     */
    @Override
    public void publish(JeeyohMessage message)
    {
        logger.debug("StoristicMessagePublisher ==> publish ==>");
        ApplicationEventMulticaster appEvtMulticaster = (ApplicationEventMulticaster) context
                .getBean("applicationEventMulticaster");
        if (appEvtMulticaster != null)
        {
            appEvtMulticaster.multicastEvent(message);
        }
        else
        {
            logger.error("Could not find bean: applicationEventMulticaster in context, cannot send message: "
                    + message.getMessageType().name());
        }

    }

}
