package com.blueskyminds.homebyfive.business.address.tools;

import junit.framework.TestCase;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.blueskyminds.homebyfive.framework.core.tools.ResourceTools;
import com.blueskyminds.homebyfive.business.region.States;
import com.blueskyminds.homebyfive.business.region.PostalCodes;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import org.apache.commons.lang.StringUtils;

/**
 * Date Started: 30/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class GeneratePostCodesFile extends TestCase {

    public void testGeneratePostCodesFile() throws Exception {
        Collection<AusPostCodes.State> states = AusPostCodes.readCsv(ResourceTools.openStream("pc-full_20080303.csv"));

        Map<String, State> ausStates = States.mapByAbbr(States.readCSV(ResourceTools.openStream("states_au.csv")));

        for (AusPostCodes.State state : states) {
            State actualState = ausStates.get(state.getName());
            for (AusPostCodes.PostCode postCode : state.getPostCodes()) {
                System.out.println("\""+postCode.getName()+"\", \""+actualState.getPath()+"\"");
            }
        }
    }


    public void testGenerateSuburbsFile() throws Exception {
        Collection<AusPostCodes.State> states = AusPostCodes.readCsv(ResourceTools.openStream("pc-full_20080303.csv"));

        Map<String, State> ausStates = States.mapByAbbr(States.readCSV(ResourceTools.openStream("states_au.csv")));
        Map<String, PostalCode> ausPostCodes = PostalCodes.mapByName(PostalCodes.readCSV(ResourceTools.openStream("postcodes_au.csv")));

        for (AusPostCodes.State state : states) {
            State actualState = ausStates.get(state.getName());
            for (AusPostCodes.Suburb suburb : state.getSuburbs()) {
                PostalCode actualPostalCode = ausPostCodes.get(suburb.getPostCode().getName());
                System.out.println("\""+suburb.getName()+"\", \""+actualState.getPath()+"\", \""+actualPostalCode.getPath()+"\"");
            }
        }
    }
}
