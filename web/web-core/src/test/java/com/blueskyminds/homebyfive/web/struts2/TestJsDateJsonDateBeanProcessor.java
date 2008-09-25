package com.blueskyminds.homebyfive.web.struts2;

import junit.framework.TestCase;
import net.sf.json.processors.JsDateJsonBeanProcessor;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.Calendar;
import java.util.Date;

/**
 * Date Started: 21/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestJsDateJsonDateBeanProcessor extends TestCase {

    private JsDateJsonBeanProcessor processor;

    protected void setUp() throws Exception {
        processor = new JsDateJsonBeanProcessor();
    }

    public void testProcessBean_sqlDate() {
        Calendar c = Calendar.getInstance();
        c.set( Calendar.YEAR, 2007 );
        c.set( Calendar.MONTH, 5 );
        c.set( Calendar.DAY_OF_MONTH, 17 );
        c.set( Calendar.HOUR_OF_DAY, 12 );
        c.set( Calendar.MINUTE, 13 );
        c.set( Calendar.SECOND, 14 );
        c.set( Calendar.MILLISECOND, 150 );
        Date date = c.getTime();
        JSONObject jsonObject = processor.processBean( new java.sql.Date( date.getTime() ),
              new JsonConfig() );
        assertNotNull( jsonObject );
        assertEquals( 2007, jsonObject.getInt( "year" ) );
        assertEquals( 5, jsonObject.getInt( "month" ) );
        assertEquals( 17, jsonObject.getInt( "day" ) );
        assertEquals( 12, jsonObject.getInt( "hours" ) );
        assertEquals( 13, jsonObject.getInt( "minutes" ) );
        assertEquals( 14, jsonObject.getInt( "seconds" ) );
        assertEquals( 150, jsonObject.getInt( "milliseconds" ) );
     }
}
