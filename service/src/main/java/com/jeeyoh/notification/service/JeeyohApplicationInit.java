/**
 * 
 */
package com.jeeyoh.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Component;

/**
 * @author Sarvesh
 * 
 */
@Component("storisticApplicationInit")
public class JeeyohApplicationInit
{
    private static final Logger logger = LoggerFactory.getLogger(JeeyohApplicationInit.class);

    @Autowired
    private ApplicationContext context;

    /**
     * Adds all StoristicMessageSubscriber's to the async notification
     * multi-caster list
     */
    private void initializeAsyncNotifiers()
    {
        ApplicationEventMulticaster appEvtMulticaster = (ApplicationEventMulticaster) context
                .getBean("applicationEventMulticaster");

        appEvtMulticaster.addApplicationListenerBean("smtpNotifier");
    }

    /**
     * Invoked by framework on startup
     */
    public void startup()
    {
        logger.info("------- STORISTIC STARTUP -------- ");
        initializeAsyncNotifiers();
    }

}
