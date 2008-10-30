package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.tools.ResourceTools;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.Ostermiller.util.CSVParser;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

/**
 * Date Started: 4/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class Countries {

    public static final String AUS = "AUS";

    /** Reads country information from a 2 column CSV file:
     *    "Name", "2 digit code"
     * 
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static List<Country> readCSV(InputStream inputStream) throws IOException {

        List<Country> countries = new LinkedList<Country>();
        CSVParser csvParser = new CSVParser(inputStream);

        String[] values;
        while ((values = csvParser.getLine()) != null) {
            Country country = new Country(values[0], values[1]);
            countries.add(country);
        }

        return countries;
    }

}
