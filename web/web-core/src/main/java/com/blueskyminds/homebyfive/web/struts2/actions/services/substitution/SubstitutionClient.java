package com.blueskyminds.homebyfive.web.struts2.actions.services.substitution;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.web.core.client.RESTfulClient;
import com.blueskyminds.homebyfive.web.core.client.RemoteClientException;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 17/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SubstitutionClient extends RESTfulClient<Substitution> {

    private static final Log LOG = LogFactory.getLog(SubstitutionClient.class);
    
    /**
     * Persist a new substitution
     *
     * @param substitution
     * @return the Substitution instance created
     */
    public void createSubstitution(String hostname, Substitution substitution) throws RemoteClientException {
        doPost(hostname, substitution);
    }   

}
