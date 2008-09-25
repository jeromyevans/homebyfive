package com.blueskyminds.homebyfive.web.struts2;

import net.sf.json.processors.JsDateJsonBeanProcessor;

/**
 * A java.sql.Date processor for json-lib
 *
 * The default Date processor isn't satisfactory for java.sql.Date as it attempts to invoke
 *  the getHours(), getMinutes() and getSeconds() methods that throw {@link IllegalArgumentException}.
 *
 * This specialization removes those properties
 *
 * Date Started: 21/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class JsDateOnlyJsonBeanProcessor extends JsDateJsonBeanProcessor {
}
