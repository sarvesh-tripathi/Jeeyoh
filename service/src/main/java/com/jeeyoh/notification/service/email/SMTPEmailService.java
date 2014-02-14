/**
 * 
 */
package com.jeeyoh.notification.service.email;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

//import com.jeeyoh.common.logging.Loggable;
import com.jeeyoh.utils.HTMLTools;

/**
 * @author Sarvesh
 * 
 */
@Component("smtpEmailService")
@Primary
public class SMTPEmailService implements IMessagingService
{
    final static Logger logger = LoggerFactory.getLogger(SMTPEmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.storistic.notification.service.email.IMessagingService#sendEmail(
     * java.lang.String, java.lang.String, java.util.List, java.util.List,
     * java.lang.String, java.lang.String, java.lang.String,
     * java.io.ByteArrayOutputStream)
     */
    @Override    
    public Boolean sendEmail(String fromEmail, String fromName, List<String> toEmails, List<String> toNames,
            String subject, String body, String fileName, ByteArrayOutputStream byteArrayOutputStream) throws Exception
    {
        logger.debug("SMTPEmailService ==> sendEmail");

        try
        {
            if (fileName != null)
                fileName = fileName + ".pdf";

            sendSMTPMail(fromEmail, fromName, toEmails, toNames, null, null, subject, body, byteArrayOutputStream,
                    fileName);

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.storistic.notification.service.email.IMessagingService#sendEmail(
     * java.lang.String, java.lang.String, java.util.List, java.util.List,
     * java.util.List, java.util.List, java.lang.String, java.lang.String,
     * java.util.Map, java.util.List)
     */
    @Override   
    public Boolean sendEmail(String fromEmail, String fromName, List<String> toEmails, List<String> toNames,
            List<String> ccEmails, List<String> bccEmails, String subjectLine, String emailBodyTemplate,
            Map<String, Object> templateKeywordValues, List<String> attachmentPaths) throws Exception
    {
        try
        {
            ByteArrayOutputStream byteArrayOutputStream = null;
            String fileName = "";
            if (templateKeywordValues.containsKey("byteArrayOutputStream"))
            {
                byteArrayOutputStream = (ByteArrayOutputStream) templateKeywordValues.get("byteArrayOutputStream");
            }
            if (templateKeywordValues.containsKey("fileName"))
            {
                fileName = templateKeywordValues.get("fileName").toString();
            }
            String htmlBody = HTMLTools.getBody(emailBodyTemplate, templateKeywordValues);
            logger.debug(" ->sendEmail ==> htmlBody ==> " + htmlBody);
            sendSMTPMail(fromEmail, fromName, toEmails, toNames, ccEmails, bccEmails, subjectLine, htmlBody,
                    byteArrayOutputStream, fileName);

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * This function will send the HTML format Email
     * 
     * @param fromEmail
     * @param toEmail
     * @param subject
     * @param body
     * @throws Exception
     */
    private void sendSMTPMail(String fromEmail, String fromName, List<String> toEmails, List<String> toNames,
            List<String> ccEmails, List<String> bccEmails, String subject, String body,
            ByteArrayOutputStream byteArrayOutputStream, String fileName) throws Exception
    {
        logger.debug("sendSMTPMail: FROM: " + fromEmail + ", [" + fromName + "], TO: " + toEmails + ", SUBJECT:"
                + subject + ", ATTACHMENT:" + fileName);
        try
        {
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(body, "text/html");
            // textBodyPart.setText(body);
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            InternetAddress iaSender = new InternetAddress(fromEmail, ((fromName == null) ? "" : fromName));
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setSender(iaSender);
            mimeMessage.setFrom(iaSender);
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(mimeMultipart);

            if (null != byteArrayOutputStream)
            {
                byte[] bytes = byteArrayOutputStream.toByteArray();
                // construct the pdf body part
                DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
                MimeBodyPart pdfBodyPart = new MimeBodyPart();
                pdfBodyPart.setDataHandler(new DataHandler(dataSource));
                pdfBodyPart.setFileName(fileName);
                mimeMultipart.addBodyPart(pdfBodyPart);
            }

            List<InternetAddress> listOfInternetAddress = new ArrayList<InternetAddress>(0);
            for (int i = 0; i < toEmails.size(); i++)
            {
                String toEmail = toEmails.get(i);
                String toName = "";
                if (toNames != null && toNames.size() > i)
                    toName = toNames.get(i);
                listOfInternetAddress.add(new InternetAddress(toEmail, toName));
            }

            mimeMessage.setRecipients(Message.RecipientType.TO, listOfInternetAddress.toArray(new InternetAddress[0]));

            if (ccEmails != null && ccEmails.size() > 0)
            {
                List<InternetAddress> ccAddresses = new ArrayList<InternetAddress>(0);
                for (int i = 0; i < ccEmails.size(); i++)
                {
                    String ccEmail = ccEmails.get(i);
                    ccAddresses.add(new InternetAddress(ccEmail, ""));
                }
                mimeMessage.setRecipients(Message.RecipientType.CC, (InternetAddress[]) ccAddresses.toArray());
            }
            if (bccEmails != null && bccEmails.size() > 0)
            {
                List<InternetAddress> bccAddresses = new ArrayList<InternetAddress>(0);
                for (int i = 0; i < bccEmails.size(); i++)
                {
                    String bccEmail = bccEmails.get(i);
                    bccAddresses.add(new InternetAddress(bccEmail, ""));
                }
                mimeMessage.setRecipients(Message.RecipientType.BCC, (InternetAddress[]) bccAddresses.toArray());
            }

            mailSender.send(mimeMessage);
        }
        catch (Exception e)
        {
            logger.error("sendSMTPMail: " + e);
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.storistic.notification.service.email.IMessagingService#sendSMS(java
     * .lang.String, java.lang.String, java.util.Map)
     */
    @Override
    public Boolean sendSMS(String toPhoneNumber, String smsBodyTemplate, Map<String, Object> templateKeywordValues)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
