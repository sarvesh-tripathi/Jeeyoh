/**
 * 
 */
package com.jeeyoh.notification;

import java.util.HashMap;
import java.util.Set;

import org.springframework.context.ApplicationEvent;


/**
 * @author Sarvesh
 *
 */
public class JeeyohMessage extends ApplicationEvent
{
    private static final long serialVersionUID = 8487128002812688230L;

    private JeeyohMessageType messageType;
    private HashMap<String, Object> messageArgumentsMap;
    

    private String uniqueMessageInstanceIdentifier;

    public JeeyohMessage(
            Object source,            
            JeeyohMessageType messageType,
            String uniqueMessageInstanceIdentifier,
            Object... ma)
    {
        super(source);

        this.messageType = messageType;
        this.uniqueMessageInstanceIdentifier = uniqueMessageInstanceIdentifier;

        this.messageArgumentsMap = new HashMap<String, Object>();
        for (int i = 0; i < ma.length; i += 2)
        {
            this.messageArgumentsMap.put((String) ma[i], ma[i + 1]);
        }
        
    }

    public JeeyohMessage(
            Object source,            
            JeeyohMessageType messageType,
            String uniqueMessageInstanceIdentifier,
            HashMap<String, Object> messageArgumentsMap)
    {
        super(source);

        this.messageType = messageType;
        this.uniqueMessageInstanceIdentifier = uniqueMessageInstanceIdentifier;
        this.messageArgumentsMap = messageArgumentsMap;       
    }

    public JeeyohMessageType getMessageType()
    {
        return messageType;
    }

    public Object getMessageArgument(String argName)
    {
        return this.messageArgumentsMap.get(argName);
    }

    public Set<String> getArgumentNames()
    {
        return this.messageArgumentsMap.keySet();
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> cloneMessageArgumentsMap()
    {
        return (HashMap<String, Object>) this.messageArgumentsMap.clone();
    }

    

    /**
     * @return the uniqueMessageInstanceIdentifier
     */
    public String getUniqueMessageInstanceIdentifer()
    {
        return uniqueMessageInstanceIdentifier;
    }

}
