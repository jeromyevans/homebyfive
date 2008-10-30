package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.StateType;
import com.blueskyminds.homebyfive.business.region.service.RegionService;
import com.Ostermiller.util.CSVParser;

import java.util.List;
import java.util.LinkedList;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 29/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class States {
    private static final Log LOG = LogFactory.getLog(States.class);
    private static final String TERRITORY = "territory";

    /** Reads state information from a 3/4 column CSV file:
     *    "Name", "abbreviation", "country code", "type"
     *
     * Type is optional. values: state|territory (default = state)
     *
     * @param inputStream
     * @return
     * @throws java.io.IOException
     */
    public static List<State> readCSV(InputStream inputStream) throws IOException {

        List<State> states = new LinkedList<State>();
        CSVParser csvParser = new CSVParser(inputStream);

        String[] values;
        while ((values = csvParser.getLine()) != null) {
            StateType stateType = StateType.State;
            if (values.length > 3) {
                if (TERRITORY.equalsIgnoreCase(values[3])) {
                    stateType = StateType.Territory;
                }
            }

            State state = new State("/"+values[2], values[0], values[1], stateType);
            states.add(state);
        }

        return states;
    }
}
