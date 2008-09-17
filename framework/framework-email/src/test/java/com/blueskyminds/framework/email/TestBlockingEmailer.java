package com.blueskyminds.framework.email;

import junit.framework.TestCase;
import com.blueskyminds.framework.tools.PropertiesContext;

/**
 * Date Started: 3/08/2007
 * <p/>
 * History:
 */
public class TestBlockingEmailer extends TestCase {

    private static final String PROPERTIES_FILE = "TestBlockingEmailer.properties";
    private static final String SMTP_SERVER_PROPRETY = "smtp.server";
    private static final String SMTP_USER_PROPRETY = "smtp.mail.user";
    private static final String SMTP_PASS_PROPRETY = "smtp.mail.pass";

    private PropertiesContext properties;
    private static final String HTML_CONTENT = "<b>This is an HTML message</b><p>para.</p>";
    private static final String TEXT_HTML = "text/html";
    private static final String TEXT_PLAIN = "text/plain";


    protected void setUp() throws Exception {
        super.setUp();
        properties = new PropertiesContext(PROPERTIES_FILE);
    }

    public void testEmailerHtml() throws Exception {
        BlockingEmailer emailer = new BlockingEmailer(
                properties.getProperty(SMTP_SERVER_PROPRETY),
                properties.getProperty(SMTP_USER_PROPRETY),
                properties.getProperty(SMTP_PASS_PROPRETY));

        String[] cc = new String[2];
        cc[0] = "jeromy@blueskyminds.com.au";
        cc[1] = "jeromy@jeromyevans.com";
        emailer.send("admin@blueskyminds.com.au", "jeromy.evans@blueskyminds.com.au", cc, "Unit test HTML email", HTML_CONTENT, TEXT_HTML);
    }

    public void testEmailerText() throws Exception {
        BlockingEmailer emailer = new BlockingEmailer(
                properties.getProperty(SMTP_SERVER_PROPRETY),
                properties.getProperty(SMTP_USER_PROPRETY),
                properties.getProperty(SMTP_PASS_PROPRETY));

        String[] cc = new String[2];
        cc[0] = "jeromy@blueskyminds.com.au";
        cc[1] = "jeromy@jeromyevans.com";
        emailer.send("admin@blueskyminds.com.au", "jeromy.evans@blueskyminds.com.au", cc, "Unit test text email", "This is a text message.\npara.", TEXT_PLAIN);
    }

    public void testEmailerWithoutCC() throws Exception {
        BlockingEmailer emailer = new BlockingEmailer(
                properties.getProperty(SMTP_SERVER_PROPRETY),
                properties.getProperty(SMTP_USER_PROPRETY),
                properties.getProperty(SMTP_PASS_PROPRETY));

        emailer.send("admin@blueskyminds.com.au", "jeromy.evans@blueskyminds.com.au", null, "Unit test email", HTML_CONTENT, TEXT_HTML);
    }

    public void testEmailerWithEmptyCC() throws Exception {
        BlockingEmailer emailer = new BlockingEmailer(
                properties.getProperty(SMTP_SERVER_PROPRETY),
                properties.getProperty(SMTP_USER_PROPRETY),
                properties.getProperty(SMTP_PASS_PROPRETY));

        String[] cc = new String[2];
        emailer.send("admin@blueskyminds.com.au", "jeromy.evans@blueskyminds.com.au", cc, "Unit test email", HTML_CONTENT, TEXT_HTML);
    }

     public void testEmailerWithoutTO() throws Exception {
        BlockingEmailer emailer = new BlockingEmailer(
                properties.getProperty(SMTP_SERVER_PROPRETY),
                properties.getProperty(SMTP_USER_PROPRETY),
                properties.getProperty(SMTP_PASS_PROPRETY));

        try {
            emailer.send("admin@blueskyminds.com.au", (String) null, null, "Unit test email", HTML_CONTENT, TEXT_HTML);
            Assert.fail("Should have thrown an exception");
        } catch(EmailerException e) {
            // an exception should be thrown
        }
    }

      public void testEmailerWithoutFrom() throws Exception {
        BlockingEmailer emailer = new BlockingEmailer(
                properties.getProperty(SMTP_SERVER_PROPRETY),
                properties.getProperty(SMTP_USER_PROPRETY),
                properties.getProperty(SMTP_PASS_PROPRETY));

        try {
            emailer.send(null, "jeromy.evans@blueskyminds.com.au", null, "Unit test email", HTML_CONTENT, TEXT_HTML);
            Assert.fail("Should have thrown an exception");
        } catch(EmailerException e) {

        }
    }
}

