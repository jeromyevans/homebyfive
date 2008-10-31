package com.blueskyminds.homebyfive.business.region;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.LinkedList;
import java.io.InputStream;
import java.io.IOException;

import com.Ostermiller.util.CSVParser;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;

/**
 * Date Started: 30/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class Suburbs {

    private static final Log LOG = LogFactory.getLog(Suburbs.class);

    /**
     * Reads suburb information from a 3 column CSV file:
     *    "Name", "statepath", "postcodepath"
     *
     * Type is optional. values: state|territory (default = state)
     *
     * @param inputStream
     * @return
     * @throws java.io.IOException
     */
    public static List<Suburb> readCSV(InputStream inputStream) throws IOException {

        List<Suburb> suburbs = new LinkedList<Suburb>();
        CSVParser csvParser = new CSVParser(inputStream);

        String[] values;
        while ((values = csvParser.getLine()) != null) {            
            Suburb state = new Suburb(values[1], values[2], values[0]);
            suburbs.add(state);
        }

        return suburbs;
    }
}
