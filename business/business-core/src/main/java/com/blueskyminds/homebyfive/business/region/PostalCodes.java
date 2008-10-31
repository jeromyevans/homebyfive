package com.blueskyminds.homebyfive.business.region;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;

import com.Ostermiller.util.CSVParser;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;

/**
 * Date Started: 30/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class PostalCodes {

    private static final Log LOG = LogFactory.getLog(PostalCodes.class);

    /**
     * Reads postcode information from a 2 column CSV file:
     *    "Name", "statePath"
     *
     * @param inputStream
     * @return
     * @throws java.io.IOException
     */
    public static List<PostalCode> readCSV(InputStream inputStream) throws IOException {

        List<PostalCode> postalCodes = new LinkedList<PostalCode>();
        CSVParser csvParser = new CSVParser(inputStream);

        String[] values;
        while ((values = csvParser.getLine()) != null) {
            PostalCode postalCode = new PostalCode(values[1], values[0]);
            postalCodes.add(postalCode);
        }

        return postalCodes;
    }

    public static Map<String, PostalCode> mapByName(List<PostalCode> postalCodes) {
        Map<String, PostalCode> map = new HashMap<String, PostalCode>();
        for (PostalCode state : postalCodes) {
            map.put(state.getName(), state);
        }
        return map;
    }

}
