/**
 * 
 */
package com.jeeyoh.notification.service.email;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Sarvesh
 * 
 */
public interface IMessagingService
{
    /**
     * Send an email
     * 
     * @param fromEmail
     * @param fromName
     * @param toEmails
     * @param toNames
     * @param subject
     * @param body
     * @param fileName
     * @param byteArrayOutputStream
     * @throws Exception
     */
    Boolean sendEmail(String fromEmail, String fromName, List<String> toEmails, List<String> toNames, String subject,
            String body, String fileName, ByteArrayOutputStream byteArrayOutputStream) throws Exception;

    /**
     * Send an email
     * 
     * @param fromEmail
     * @param fromName
     * @param toEmails
     * @param toNames
     * @param ccEmails
     * @param bccEmails
     * @param subjectLine
     * @param emailBodyTemplate
     * @param templateKeywordValues
     * @param attachmentPaths
     */
    Boolean sendEmail(String fromEmail, String fromName, List<String> toEmails, List<String> toNames,
            List<String> ccEmails, List<String> bccEmails, String subjectLine, String emailBodyTemplate,
            Map<String, Object> templateKeywordValues, List<String> attachmentPaths) throws Exception;

    /**
     * This method will send an SMS
     * 
     * @param toPhoneNumber
     * @param smsBodyTemplate
     * @param templateKeywordValues
     * @return
     */
    Boolean sendSMS(String toPhoneNumber, String smsBodyTemplate, Map<String, Object> templateKeywordValues);

}
