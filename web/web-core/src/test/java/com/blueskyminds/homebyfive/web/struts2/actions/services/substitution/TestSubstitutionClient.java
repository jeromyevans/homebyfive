package com.blueskyminds.homebyfive.web.struts2.actions.services.substitution;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.SubstitutionsFileReader;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.tools.ResourceTools;

import java.util.List;
import java.io.FileInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 17/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestSubstitutionClient extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSubstitutionClient.class);

    private static final String ADDRESS_PATTERNS_FILE_NAME = "addressSubstitutions.csv";

    public void testSerialization() throws Exception {

        SubstitutionsFileReader substitutionsFileReader = new SubstitutionsFileReader();

        List<Substitution> substitutions;
        substitutions = substitutionsFileReader.readCsv(ResourceTools.openStream(ADDRESS_PATTERNS_FILE_NAME));


        SubstitutionClient subsitutionClient = new SubstitutionClient();
        for (Substitution substitution : substitutions) {
            LOG.info(subsitutionClient.serialize(substitution));
        }
    }
}