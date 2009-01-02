package com.blueskyminds.homebyfive.web.struts2.actions.services.substitution;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.net.RemoteClientException;
import com.blueskyminds.homebyfive.framework.core.net.RESTfulClient;
import com.google.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.HttpConnectionManager;

/**
 * Date Started: 17/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SubstitutionClient extends RESTfulClient<Substitution> {

    private static final Log LOG = LogFactory.getLog(SubstitutionClient.class);

    @Inject
    public SubstitutionClient(HttpConnectionManager connectionManager) {
        super(connectionManager);
    }

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
