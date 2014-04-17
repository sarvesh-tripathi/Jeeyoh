/**
 * 
 */
package com.jeeyoh.utils;

import java.io.FileReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/**
 * @author Sarvesh
 * 
 */
public class HTMLTools
{
    final static Logger logger = LogManager.getLogger(HTMLTools.class);

    public static void cleanHTMLDocument(String filePath)
    {

    }

    /**
     * This function will parse the VM in the form of HTMl String
     * 
     * @param emailBodyTemplate as vm, templateKeywordValues
     * @return VM Body as a HTML string
     */
    public static String getBody(String emailBodyTemplate, Map<String, Object> templateKeywordValues) throws Exception
    {
        if (emailBodyTemplate == null || emailBodyTemplate.length() == 0)
            throw new Exception("No email template");

        VelocityEngine ve = new VelocityEngine();
        ve.init();
        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
        FileReader reader = null;

        /**
         * This code will be changed later and we only use the code written in
         * the try block We are using this because while testing this using the
         * Test Case getResource function will not
         */

        try
        {

            URL res = HTMLTools.class.getResource("/templates/" + emailBodyTemplate);
            URI uri = new URI(res.toString());
            logger.info("getBody: URI PATH : " + uri.getPath());
            reader = new FileReader(uri.getPath());
        }
        catch (NullPointerException e)
        {
            logger.info("getBody: got a null pointer EXCEPTION: ", e);
            // reader = new FileReader(filePath + emailBodyTemplate);
        }

        SimpleNode node = runtimeServices.parse(reader, "Template name");
        Template t = new Template();
        t.setData(emailBodyTemplate);
        t.setRuntimeServices(runtimeServices);
        t.setData(node);
        t.initDocument();

        if (templateKeywordValues == null)
        {
            return emailBodyTemplate;
        }

        VelocityContext context = new VelocityContext(templateKeywordValues);
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }
}
